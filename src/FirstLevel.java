import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FirstLevel extends Level {
    private Scene gameScene;
    private ImageView duckImageView;
    private Timeline moveAnimationTimeline;
    private Timeline flapAnimationTimeline;
    private int ammoNum = 3;
    private boolean gameOver = false;
    private boolean duckDead = false;
    private Text ammoText; // Ammo sayısı metnini tutmak için


    /**
     * Starts the first level by creating the game scene, configuring the duck image view, creating animation timelines,
     * and setting event handlers for mouse clicks and key presses.
     *
     * @param primaryStage the primary stage of the Duck Hunt.
     */
    public void start(Stage primaryStage) {
        // Create the game scene
        createGameScene();

        // Create the ImageView for the duck and set the initial position
        duckImageView = new ImageView(new Image("assets/duck_black/4.png"));
        double initialX = 0;
        double initialY = 30 * DuckHunt.SCALE;
        configureDuckImageView(initialX, initialY, duckImageView);

        // Create the animation timeline for the duck
        createAnimationTimeline();

        // Add the duck ImageView to the game scene
        Group root = (Group) gameScene.getRoot();
        root.getChildren().add(1, duckImageView);

        // Add the foreground image view to the game scene
        ImageView foregroundImageView = new ImageView(new Image(BackgroundManager.foregroundImages[BackgroundManager.currentBackgroundIndex]));
        foregroundImageView.setFitWidth(DuckHunt.SCREEN_WIDTH);
        foregroundImageView.setFitHeight(DuckHunt.SCREEN_HEIGHT);
        root.getChildren().add(2, foregroundImageView);

        // Set the game scene to the primary stage and show it
        primaryStage.setScene(gameScene);
        primaryStage.show();

        // Event handler for mouse clicks
        gameScene.setOnMouseClicked(event -> {
            if (gameOver || duckDead) {
                return; // Ignore clicks if the game is over or the duck is already dead
            }
            DuckHunt.gunShotMusic.play();

            double clickX = event.getX();
            double clickY = event.getY();

            // Get the current position of the duck
            double duckX = duckImageView.getLayoutX() + duckImageView.getTranslateX();
            double duckY = duckImageView.getLayoutY() + duckImageView.getTranslateY();

            // Check if the click point intersects with the duck image
            if (clickX >= duckX && clickX <= (duckX + duckImageView.getFitWidth())
                    && clickY >= duckY && clickY <= (duckY + duckImageView.getFitHeight())) {
                duckDead = true;
                duckImageView.setImage(new Image("assets/duck_black/7.png")); // Change the image to a dead duck
                moveAnimationTimeline.stop(); // Stop the duck animation
                flapAnimationTimeline.stop(); // Stop the duck animation
                Timeline fallAnimationTimeline = new Timeline(
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_black/8.png"))),
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView.layoutYProperty(), duckImageView.getLayoutY())),
                        new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView.layoutYProperty(), gameScene.getHeight()))
                );
                fallAnimationTimeline.play();
                DuckHunt.duckFallsMusic.play();
                showText(winText); // Show "YOU WIN" text
                showFlashText(nextLevelText); // Show "PRESS ENTER to play next level" text
                DuckHunt.levelCompletedMusic.play();
            }

            // Decrease the ammo count
            if (ammoNum > 0) {
                ammoNum--;
                ammoText.setText("Ammo Left: " + ammoNum); // Update the ammo text
            }

            // Game over if ammo is depleted and the duck is not yet dead
            if (ammoNum == 0 && !duckDead) {
                gameOver = true; // Mark the game as over
                showText(gameOverText); // Show "Game Over" text
                showFlashText(playAgainText);
                DuckHunt.gameOverMusic.play();
            }
        });

        // Event handler for key presses
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && gameOver) {
                DuckHunt.levelCompletedMusic.stop();
                DuckHunt.gameOverMusic.stop();
                FirstLevel firstLevel = new FirstLevel();
                firstLevel.start(primaryStage);
            } else if (event.getCode() == KeyCode.ESCAPE && gameOver) {
                DuckHunt.levelCompletedMusic.stop();
                DuckHunt.gameOverMusic.stop();
                BackgroundManager.changeScreenShown = false;
                DuckHunt titleScreen = new DuckHunt();
                titleScreen.start(primaryStage);
            } else if (event.getCode() == KeyCode.ENTER && duckDead) {
                DuckHunt.levelCompletedMusic.stop();
                DuckHunt.gameOverMusic.stop();
                SecondLevel secondLevel = new SecondLevel();
                secondLevel.start(primaryStage);
            }
        });
    }


    /**
     * Creates the first level scene with the background, level text, ammo text, and various game-related texts.
     * The root group is created and the background image is added to it.
     * The level text, ammo text, win text, next level text, game over text, and play again text are initialized and added to the root group.
     * The crosshair is set for the game scene.
     */
    public void createGameScene() {
        Group root = new Group();
        ImageView backgroundImageView = new ImageView(new Image(BackgroundManager.backgroundImages[BackgroundManager.currentBackgroundIndex]));
        root.getChildren().add(backgroundImageView);
        Text levelText = getLevelText(1);
        ammoText = new Text("Ammo Left: " + ammoNum);
        ammoTextSetting(ammoText);
        gameScene = new Scene(root, DuckHunt.SCREEN_WIDTH, DuckHunt.SCREEN_HEIGHT);
        backgroundImageView.setFitWidth(DuckHunt.SCREEN_WIDTH);
        backgroundImageView.setFitHeight(DuckHunt.SCREEN_HEIGHT);
        root.getChildren().addAll(levelText, ammoText, winText, nextLevelText, gameOverText, playAgainText);
        setCursor(root, gameScene);
    }

    /**
     * Creates the animation timeline for the duck.
     * The duck moves horizontally from initialX to targetX and back in a loop.
     * The duck image flips horizontally at specific time intervals to simulate flapping wings.
     */
    public void createAnimationTimeline() {
        double initialX = 0;
        double targetX = DuckHunt.SCREEN_WIDTH - duckImageView.getLayoutBounds().getWidth();

        KeyFrame[] keyFrames = new KeyFrame[7];
        keyFrames[0] = new KeyFrame(Duration.ZERO, new KeyValue(duckImageView.translateXProperty(), initialX));
        keyFrames[1] = new KeyFrame(Duration.millis(1800), new KeyValue(duckImageView.translateXProperty(), targetX));
        keyFrames[2] = new KeyFrame(Duration.millis(3600), new KeyValue(duckImageView.translateXProperty(), initialX));
        keyFrames[3] = new KeyFrame(Duration.millis(0), event -> duckImageView.setScaleX(1));
        keyFrames[4] = new KeyFrame(Duration.millis(1800), event -> duckImageView.setScaleX(1));
        keyFrames[5] = new KeyFrame(Duration.millis(1801), event -> duckImageView.setScaleX(-1));
        keyFrames[6] = new KeyFrame(Duration.millis(3600), event -> duckImageView.setScaleX(-1));

        moveAnimationTimeline = new Timeline(keyFrames);
        moveAnimationTimeline.setCycleCount(Timeline.INDEFINITE);

        flapAnimationTimeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_black/4.png"))),
                new KeyFrame(Duration.millis(100), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_black/5.png"))),
                new KeyFrame(Duration.millis(200), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_black/6.png"))),
                new KeyFrame(Duration.millis(300))
        );

        flapAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
        flapAnimationTimeline.setAutoReverse(true);
        flapAnimationTimeline.play();
        moveAnimationTimeline.play();
    }

}