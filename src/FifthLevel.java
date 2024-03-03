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

public class FifthLevel extends Level {
    private Scene gameScene;
    private ImageView duckImageView;
    private ImageView duckImageView2;
    private ImageView duckImageView3;
    private Timeline moveAnimationTimeline;
    private Timeline moveAnimationTimeline2;
    private Timeline moveAnimationTimeline3;
    private Timeline flapAnimationTimeline;
    private Timeline flapAnimationTimeline2;
    private Timeline flapAnimationTimeline3;
    private int ammoNum = 9;
    private boolean gameOver = false;
    private boolean duckDead = false;
    private boolean duckDead2 = false;
    private boolean duckDead3 = false;
    private Text ammoText;

    /**
     * Starts the fifth level by creating the game scene, configuring the duck image view, creating animation timelines,
     * and setting event handlers for mouse clicks and key presses.
     *
     * @param primaryStage the primary stage of the Duck Hunt.
     */
    public void start(Stage primaryStage) {
        // Create the game scene
        createGameScene();

        // Create the ImageView for the ducks and set the initial positions
        duckImageView = new ImageView(new Image("assets/duck_blue/4.png"));
        duckImageView2 = new ImageView(new Image("assets/duck_black/4.png"));
        duckImageView3 = new ImageView(new Image("assets/duck_red/4.png"));
        double initialX = 0;
        double initialY = 45 * DuckHunt.SCALE;
        configureDuckImageView(initialX, initialY, duckImageView);
        configureDuckImageView(160 * DuckHunt.SCALE, 65 * DuckHunt.SCALE, duckImageView2);
        configureDuckImageView(0, 45 * DuckHunt.SCALE, duckImageView3);

        // Create the animation timeline for the ducks
        createAnimationTimeline();
        createAnimationTimeline2(duckImageView2, 0, 160 * DuckHunt.SCALE);
        createAnimationTimeline3(duckImageView3, 0, DuckHunt.SCREEN_WIDTH - duckImageView3.getFitWidth());

        // Add the ducks ImageView to the game scene
        Group root = (Group) gameScene.getRoot();
        root.getChildren().add(1, duckImageView);
        root.getChildren().add(2, duckImageView2);
        root.getChildren().add(3, duckImageView3);

        // Add the foreground image view to the game scene
        ImageView foregroundImageView = new ImageView(new Image(BackgroundManager.foregroundImages[BackgroundManager.currentBackgroundIndex]));
        foregroundImageView.setFitWidth(DuckHunt.SCREEN_WIDTH);
        foregroundImageView.setFitHeight(DuckHunt.SCREEN_HEIGHT);
        root.getChildren().add(4, foregroundImageView);

        // Set the game scene to the primary stage and show it
        primaryStage.setScene(gameScene);
        primaryStage.show();

        // Event handler for mouse clicks
        gameScene.setOnMouseClicked(event -> {
            if (gameOver || (duckDead && duckDead2 && duckDead3)) {
                return; // Ignore clicks if the game is over or the ducks are already dead
            }
            DuckHunt.gunShotMusic.play();

            double clickX = event.getX();
            double clickY = event.getY();

            if (duckImageView.getBoundsInParent().contains(clickX, clickY)) {
                duckDead = true;
                duckImageView.setImage(new Image("assets/duck_black/7.png"));
                moveAnimationTimeline.stop();
                flapAnimationTimeline.stop();

                Timeline fallAnimationTimeline = new Timeline(
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_black/8.png"))),
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView.layoutYProperty(), duckImageView.getLayoutY())),
                        new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView.layoutYProperty(), gameScene.getHeight()))
                );
                fallAnimationTimeline.play();
                DuckHunt.duckFallsMusic.play();
            }

            if (duckImageView2.getBoundsInParent().contains(clickX, clickY)) {
                duckDead2 = true;
                duckImageView2.setImage(new Image("assets/duck_red/7.png"));
                moveAnimationTimeline2.stop();
                flapAnimationTimeline2.stop();

                Timeline fallAnimationTimeline2 = new Timeline(
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView2.imageProperty(), new Image("assets/duck_red/8.png"))),
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView2.layoutYProperty(), duckImageView2.getLayoutY())),
                        new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView2.layoutYProperty(), gameScene.getHeight()))
                );
                fallAnimationTimeline2.play();
                DuckHunt.duckFallsMusic.play();
            }

            if (duckImageView3.getBoundsInParent().contains(clickX, clickY)) {
                duckDead3 = true;
                duckImageView3.setImage(new Image("assets/duck_blue/7.png"));
                moveAnimationTimeline3.stop();
                flapAnimationTimeline3.stop();

                Timeline fallAnimationTimeline3 = new Timeline(
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView3.imageProperty(), new Image("assets/duck_blue/8.png"))),
                        new KeyFrame(Duration.millis(400), new KeyValue(duckImageView3.layoutYProperty(), duckImageView3.getLayoutY())),
                        new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView3.layoutYProperty(), gameScene.getHeight()))
                );
                fallAnimationTimeline3.play();
                DuckHunt.duckFallsMusic.play();
            }

            if (ammoNum > 0) {
                ammoNum--;
                ammoText.setText("Ammo Left: " + ammoNum);
            }

            if (duckDead && duckDead2 && duckDead3) {
                showText(winText); // "YOU WIN" yazısını göster
                showFlashText(nextLevelText); // "PRESS ENTER to play next level" yazısını göster
                DuckHunt.levelCompletedMusic.play();
            }

            if (ammoNum == 0 && (!duckDead || !duckDead2 || !duckDead3)) {
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
            } else if (event.getCode() == KeyCode.ENTER && duckDead && duckDead2 && duckDead3) {
                DuckHunt.levelCompletedMusic.stop();
                DuckHunt.gameOverMusic.stop();
                SixthLevel sixthLevel = new SixthLevel();
                sixthLevel.start(primaryStage);
            }
        });
    }

    /**
     * Creates the fifth level scene with the background, level text, ammo text, and various game-related texts.
     * The root group is created and the background image is added to it.
     * The level text, ammo text, win text, next level text, game over text, and play again text are initialized and added to the root group.
     * The crosshair is set for the game scene.
     */
    public void createGameScene() {
        Group root = new Group();
        ImageView backgroundImageView = new ImageView(new Image(BackgroundManager.backgroundImages[BackgroundManager.currentBackgroundIndex]));
        root.getChildren().add(backgroundImageView);

        Text levelText = getLevelText(5);

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
        double targetX = DuckHunt.SCREEN_WIDTH - duckImageView.getLayoutBounds().getWidth();

        KeyFrame[] keyFrames = new KeyFrame[17];
        keyFrames[0] = new KeyFrame(Duration.ZERO, new KeyValue(duckImageView.translateXProperty(), initialX));
        keyFrames[1] = new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView.translateXProperty(), targetX));
        keyFrames[2] = new KeyFrame(Duration.millis(2400), new KeyValue(duckImageView.translateXProperty(), initialX));
        keyFrames[3] = new KeyFrame(Duration.millis(800), new KeyValue(duckImageView.translateYProperty(), 180 * DuckHunt.SCALE));
        keyFrames[4] = new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView.translateYProperty(), 60 * DuckHunt.SCALE));
        keyFrames[5] = new KeyFrame(Duration.millis(1600), new KeyValue(duckImageView.translateYProperty(), -50 * DuckHunt.SCALE));
        keyFrames[6] = new KeyFrame(Duration.millis(2400), new KeyValue(duckImageView.translateYProperty(), 45 * DuckHunt.SCALE));
        keyFrames[7] = new KeyFrame(Duration.millis(0), event -> duckImageView.setScaleX(1));
        keyFrames[8] = new KeyFrame(Duration.millis(1200), event -> duckImageView.setScaleX(1));
        keyFrames[9] = new KeyFrame(Duration.millis(1201), event -> duckImageView.setScaleX(-1));
        keyFrames[10] = new KeyFrame(Duration.millis(2400), event -> duckImageView.setScaleX(-1));
        keyFrames[11] = new KeyFrame(Duration.millis(0), event -> duckImageView.setScaleY(-1));
        keyFrames[12] = new KeyFrame(Duration.millis(800), event -> duckImageView.setScaleY(-1));
        keyFrames[13] = new KeyFrame(Duration.millis(801), event -> duckImageView.setScaleY(1));
        keyFrames[14] = new KeyFrame(Duration.millis(1600), event -> duckImageView.setScaleY(1));
        keyFrames[15] = new KeyFrame(Duration.millis(1601), event -> duckImageView.setScaleY(-1));
        keyFrames[16] = new KeyFrame(Duration.millis(2400), event -> duckImageView.setScaleY(-1));

        moveAnimationTimeline = new Timeline(keyFrames);
        moveAnimationTimeline.setCycleCount(Timeline.INDEFINITE);

        flapAnimationTimeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_black/1.png"))),
                new KeyFrame(Duration.millis(100), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_black/2.png"))),
                new KeyFrame(Duration.millis(200), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_black/3.png"))),
                new KeyFrame(Duration.millis(300))
        );

        flapAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
        flapAnimationTimeline.setAutoReverse(true);
        flapAnimationTimeline.play();
        moveAnimationTimeline.play();
    }

    /**
     * Creates the animation timeline for second the duck.
     * The duck moves horizontally from initialX to -targetX and back in a loop.
     * The duck image flips horizontally at specific time intervals to simulate flapping wings.
     */
    public void createAnimationTimeline2(ImageView duckImageView, double initialX, double targetX) {
        KeyFrame[] keyFrames = new KeyFrame[7];
        keyFrames[0] = new KeyFrame(Duration.ZERO, new KeyValue(duckImageView.translateXProperty(), initialX + DuckHunt.SCALE * 66));
        keyFrames[1] = new KeyFrame(Duration.millis(1200), new KeyValue(duckImageView.translateXProperty(), -targetX));
        keyFrames[2] = new KeyFrame(Duration.millis(2400), new KeyValue(duckImageView.translateXProperty(), initialX + DuckHunt.SCALE * 66));
        keyFrames[3] = new KeyFrame(Duration.millis(0), event -> duckImageView.setScaleX(-1));
        keyFrames[4] = new KeyFrame(Duration.millis(1200), event -> duckImageView.setScaleX(-1));
        keyFrames[5] = new KeyFrame(Duration.millis(1201), event -> duckImageView.setScaleX(1));
        keyFrames[6] = new KeyFrame(Duration.millis(2400), event -> duckImageView.setScaleX(1));

        moveAnimationTimeline2 = new Timeline(keyFrames);
        moveAnimationTimeline2.setCycleCount(Timeline.INDEFINITE);

        flapAnimationTimeline2 = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_red/4.png"))),
                new KeyFrame(Duration.millis(100), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_red/5.png"))),
                new KeyFrame(Duration.millis(200), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_red/6.png"))),
                new KeyFrame(Duration.millis(300))
        );

        flapAnimationTimeline2.setCycleCount(Timeline.INDEFINITE);
        flapAnimationTimeline2.setAutoReverse(true);
        flapAnimationTimeline2.play();
        moveAnimationTimeline2.play();
    }

    /**
     * Creates the animation timeline for the third duck.
     * The duck moves horizontally from initialX to targetX and back in a loop.
     * The duck image flips horizontally at specific time intervals to simulate flapping wings.
     */
    public void createAnimationTimeline3(ImageView duckImageView, double initialX, double targetX) {
        KeyFrame[] keyFrames = new KeyFrame[7];
        keyFrames[0] = new KeyFrame(Duration.ZERO, new KeyValue(duckImageView.translateXProperty(), initialX ));
        keyFrames[1] = new KeyFrame(Duration.millis(1000), new KeyValue(duckImageView.translateXProperty(), targetX));
        keyFrames[2] = new KeyFrame(Duration.millis(2000), new KeyValue(duckImageView.translateXProperty(), initialX));
        keyFrames[3] = new KeyFrame(Duration.millis(0), event -> duckImageView.setScaleX(1));
        keyFrames[4] = new KeyFrame(Duration.millis(1000), event -> duckImageView.setScaleX(1));
        keyFrames[5] = new KeyFrame(Duration.millis(1001), event -> duckImageView.setScaleX(-1));
        keyFrames[6] = new KeyFrame(Duration.millis(2000), event -> duckImageView.setScaleX(-1));

        moveAnimationTimeline3 = new Timeline(keyFrames);
        moveAnimationTimeline3.setCycleCount(Timeline.INDEFINITE);

        flapAnimationTimeline3 = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_blue/4.png"))),
                new KeyFrame(Duration.millis(100), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_blue/5.png"))),
                new KeyFrame(Duration.millis(200), new KeyValue(duckImageView.imageProperty(), new Image("assets/duck_blue/6.png"))),
                new KeyFrame(Duration.millis(300))
        );

        flapAnimationTimeline3.setCycleCount(Timeline.INDEFINITE);
        flapAnimationTimeline3.setAutoReverse(true);
        flapAnimationTimeline3.play();
        moveAnimationTimeline3.play();
    }

}
