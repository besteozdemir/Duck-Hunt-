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

public class SecondLevel extends Level {
    private Scene gameScene;
    private ImageView duckImageView;
    private Timeline moveAnimationTimeline;
    private Timeline flapAnimationTimeline;
    private int ammoNum = 3;
    private boolean gameOver = false;
    private boolean duckDead = false;
    private Text ammoText;

    /**
     * Starts the second level by creating the game scene, configuring the duck image view, creating animation timelines,
     * and setting event handlers for mouse clicks and key presses.
     *
     * @param primaryStage the primary stage of the Duck Hunt.
     */
    public void start(Stage primaryStage) {
        // Create the game scene
        createGameScene();

        // Create the ImageView for the duck and set the initial position
        duckImageView = new ImageView(new Image("assets/duck_blue/4.png"));
        double initialX = 0;
        double initialY = 45 * DuckHunt.SCALE;
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
        root.getChildren().add(2,foregroundImageView);

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

            double duckX = duckImageView.getLayoutX() + duckImageView.getTranslateX();
            double duckY = duckImageView.getLayoutY() + duckImageView.getTranslateY();

            if (clickX >= duckX && clickX <= (duckX + duckImageView.getFitWidth())
                    && clickY >= duckY && clickY <= (duckY + duckImageView.getFitHeight())) {
                duckDead = true;
                duckImageView.setImage(new Image("assets/duck_blue/7.png"));
                moveAnimationTimeline.stop();
                flapAnimationTimeline.stop();

                Timeline fallAnimationTimeline = new Timeline(
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_blue/8.png"))),
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView.layoutYProperty(), duckImageView.getLayoutY())),
                        new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView.layoutYProperty(), gameScene.getHeight()))
                );
                fallAnimationTimeline.play();
                DuckHunt.duckFallsMusic.play();

                showText(winText); // "YOU WIN" yazısını göster
                showFlashText(nextLevelText); // "PRESS ENTER to play next level" yazısını göster
                DuckHunt.levelCompletedMusic.play();
            }

            if (ammoNum > 0) {
                ammoNum--;
                ammoText.setText("Ammo Left: " + ammoNum);
            }

            if (ammoNum == 0 && !duckDead) {
                gameOver = true;
                showText(gameOverText);
                showFlashText(playAgainText);
                DuckHunt.gameOverMusic.play();
            }
        });

        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && gameOver) {
                DuckHunt.levelCompletedMusic.stop();
                DuckHunt.gameOverMusic.stop();
                FirstLevel firsTlevel = new FirstLevel();
                firsTlevel.start(primaryStage);
            } else if (event.getCode() == KeyCode.ESCAPE && gameOver) {
                DuckHunt.levelCompletedMusic.stop();
                DuckHunt.gameOverMusic.stop();
                BackgroundManager.changeScreenShown = false;
                DuckHunt titleScreen = new DuckHunt();
                titleScreen.start(primaryStage);
            } else if (event.getCode() == KeyCode.ENTER && duckDead) {
                DuckHunt.levelCompletedMusic.stop();
                DuckHunt.gameOverMusic.stop();
                ThirdLevel thirdLevel = new ThirdLevel();
                thirdLevel.start(primaryStage);
            }
        });
    }

    /**
     * Creates the second level scene with the background, level text, ammo text, and various game-related texts.
     * The root group is created and the background image is added to it.
     * The level text, ammo text, win text, next level text, game over text, and play again text are initialized and added to the root group.
     * The crosshair is set for the game scene.
     */
    public void createGameScene() {
        Group root = new Group();
        ImageView backgroundImageView = new ImageView(new Image(BackgroundManager.backgroundImages[BackgroundManager.currentBackgroundIndex]));
        root.getChildren().add(backgroundImageView);

        Text levelText = getLevelText(2);

        ammoText = new Text("Ammo Left: " + ammoNum);
        ammoTextSetting(ammoText);

        gameScene = new Scene(root, DuckHunt.SCREEN_WIDTH, DuckHunt.SCREEN_HEIGHT);

        backgroundImageView.setFitWidth(DuckHunt.SCREEN_WIDTH);
        backgroundImageView.setFitHeight(DuckHunt.SCREEN_HEIGHT);

        root.getChildren().addAll(levelText, ammoText, winText, nextLevelText, gameOverText, playAgainText);

        setCursor(root, gameScene);
    }

    /**
     * Creates the animation timeline for the first duck.
     * The duck moves horizontally from initialX to targetX and back in a loop.
     * The duck also moves vertically in a sequence of different heights.
     * The duck image flips horizontally and vertically at specific time intervals to simulate flapping wings and changing direction.
     */
    public void createAnimationTimeline() {
        double initialX = 0;
        double targetX = DuckHunt.SCREEN_WIDTH - duckImageView.getFitWidth();

        KeyFrame[] keyFrames = new KeyFrame[17];
        keyFrames[0] = new KeyFrame(Duration.ZERO, new KeyValue(duckImageView.translateXProperty(), initialX));
        keyFrames[1] = new KeyFrame(Duration.millis(1800), new KeyValue(duckImageView.translateXProperty(), targetX));
        keyFrames[2] = new KeyFrame(Duration.millis(3600), new KeyValue(duckImageView.translateXProperty(), initialX));
        keyFrames[3] = new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView.translateYProperty(), 180*DuckHunt.SCALE));
        keyFrames[4] = new KeyFrame(Duration.millis(1800), new KeyValue(duckImageView.translateYProperty(), 60*DuckHunt.SCALE));
        keyFrames[5] = new KeyFrame(Duration.millis(2400), new KeyValue(duckImageView.translateYProperty(), -50*DuckHunt.SCALE));
        keyFrames[6] = new KeyFrame(Duration.millis(3600), new KeyValue(duckImageView.translateYProperty(), 45*DuckHunt.SCALE));
        keyFrames[7] = new KeyFrame(Duration.millis(0), event -> duckImageView.setScaleX(1));
        keyFrames[8] = new KeyFrame(Duration.millis(1800), event -> duckImageView.setScaleX(1));
        keyFrames[9] = new KeyFrame(Duration.millis(1801), event -> duckImageView.setScaleX(-1));
        keyFrames[10] = new KeyFrame(Duration.millis(3600), event -> duckImageView.setScaleX(-1));
        keyFrames[11] = new KeyFrame(Duration.millis(0), event -> duckImageView.setScaleY(-1));
        keyFrames[12] = new KeyFrame(Duration.millis(1200), event -> duckImageView.setScaleY(-1));
        keyFrames[13] = new KeyFrame(Duration.millis(1201), event -> duckImageView.setScaleY(1));
        keyFrames[14] = new KeyFrame(Duration.millis(2400), event -> duckImageView.setScaleY(1));
        keyFrames[15] = new KeyFrame(Duration.millis(2401), event -> duckImageView.setScaleY(-1));
        keyFrames[16] = new KeyFrame(Duration.millis(3600), event -> duckImageView.setScaleY(-1));


        moveAnimationTimeline = new Timeline(keyFrames);
        moveAnimationTimeline.setCycleCount(Timeline.INDEFINITE);

        flapAnimationTimeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_blue/1.png"))),
                new KeyFrame(Duration.millis(100), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_blue/2.png"))),
                new KeyFrame(Duration.millis(200), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_blue/3.png"))),
                new KeyFrame(Duration.millis(300))
        );

        flapAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
        flapAnimationTimeline.setAutoReverse(true);
        flapAnimationTimeline.play();
        moveAnimationTimeline.play();
    }

}
