import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Objects;

/**
 * The DuckHunt class is the main class that represents the Duck Hunt game application.
 */
public class DuckHunt extends Application {

    /**
     * The scale factor for the game window.
     */
    public static final double SCALE = 3;

    /**
     * The volume level for the sound effects.
     * Must be in the range [0, 1].
     */
    public static final double VOLUME = 0.025;
    private static final String TITLE = "HUBBM Duck Hunt";
    public static final String BACKGROUND_MUSIC_PATH = "assets/effects/Title.mp3";
    public static AudioClip backgroundMusic;
    public static AudioClip introMusic;
    public static AudioClip gunShotMusic;
    public static AudioClip duckFallsMusic;
    public static AudioClip gameCompletedMusic;
    public static AudioClip levelCompletedMusic;
    public static AudioClip gameOverMusic;
    private static final Image backgroundImage = new Image("assets/welcome/1.png");
    private static final ImageView backgroundImageView = new ImageView(backgroundImage);
    public static final double SCREEN_WIDTH = backgroundImageView.getLayoutBounds().getWidth() * DuckHunt.SCALE;
    public static final double SCREEN_HEIGHT = backgroundImageView.getLayoutBounds().getWidth() * DuckHunt.SCALE;

    /**
     * The root pane for the game's scene graph.
     */
    Pane root = new Pane();

    /**
     * The main method that launches the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and starts the Duck Hunt title screen.
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(TITLE);
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("assets/favicon/1.png"));
        primaryStage.setResizable(false);

        setBackgroundImage(root);
        setBackgroundMusic();
        setTexts(root, scene);
        BackgroundManager.setKeyEvents(scene, primaryStage, root);

        primaryStage.show();
    }

    /**
     * Sets the welcome image for the specified pane.
     *
     * @param root the root pane to set the welcome image for
     */
    private void setBackgroundImage(Pane root) {
        backgroundImageView.setFitWidth(SCREEN_WIDTH);
        backgroundImageView.setFitHeight(SCREEN_HEIGHT);
        root.getChildren().add(backgroundImageView);
    }

    /**
     * Sets the background music and sound effects for beginning of the game.
     * Loads the all audio clips from the specified resource paths and configures their properties.
     * Starts playing the background music for title screen.
     */
    private void setBackgroundMusic() {
        introMusic = new AudioClip(Objects.requireNonNull(getClass().getResource("assets/effects/Intro.mp3")).toExternalForm());
        introMusic.setCycleCount(1);
        introMusic.setVolume(VOLUME);
        gunShotMusic = new AudioClip(Objects.requireNonNull(getClass().getResource("assets/effects/Gunshot.mp3")).toExternalForm());
        gunShotMusic.setCycleCount(1);
        gunShotMusic.setVolume(VOLUME);
        duckFallsMusic = new AudioClip(Objects.requireNonNull(getClass().getResource("assets/effects/DuckFalls.mp3")).toExternalForm());
        duckFallsMusic.setCycleCount(1);
        duckFallsMusic.setVolume(VOLUME);
        gameCompletedMusic = new AudioClip(Objects.requireNonNull(getClass().getResource("assets/effects/GameCompleted.mp3")).toExternalForm());
        gameCompletedMusic.setCycleCount(1);
        gameCompletedMusic.setVolume(VOLUME);
        levelCompletedMusic = new AudioClip(Objects.requireNonNull(getClass().getResource("assets/effects/LevelCompleted.mp3")).toExternalForm());
        levelCompletedMusic.setCycleCount(1);
        levelCompletedMusic.setVolume(VOLUME);
        gameOverMusic = new AudioClip(Objects.requireNonNull(getClass().getResource("assets/effects/GameOver.mp3")).toExternalForm());
        gameOverMusic.setCycleCount(1);
        gameOverMusic.setVolume(VOLUME);
        backgroundMusic = new AudioClip(Objects.requireNonNull(getClass().getResource(BACKGROUND_MUSIC_PATH)).toExternalForm());
        backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
        backgroundMusic.setVolume(VOLUME);
        backgroundMusic.play();
    }

    /**
     * Sets the texts for the title screen.
     * Creates a text object with the specified content and font properties.
     * Applies a flash effect to the text.
     * Positions the text at the center of the screen.
     * Adds the text to the specified root pane.
     *
     * @param root  the root pane to which the text will be added
     * @param scene the scene associated with the root pane
     */
    private void setTexts(Pane root, Scene scene) {
        Text text = new Text("PRESS ENTER TO START\nPRESS ESC TO EXIT");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 18 * DuckHunt.SCALE));
        text.setTextAlignment(TextAlignment.CENTER);
        flashText(text);
        text.setX((SCREEN_WIDTH - text.getLayoutBounds().getWidth()) / 2);
        text.setY((scene.getHeight() - text.getLayoutBounds().getHeight()) / 2 + 60 * DuckHunt.SCALE);
        text.setFill(Color.ORANGE);
        root.getChildren().addAll(text);
    }

    /**
     * Applies a flash effect to the specified text.
     * Creates a fade transition that alternates the opacity of the text between 1.0 and 0.0.
     * Sets the transition to auto-reverse and indefinite cycle count to create a continuous flash effect.
     *
     * @param text the text to apply the flash effect to
     */
    private void flashText(Text text) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), text);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();
    }
}