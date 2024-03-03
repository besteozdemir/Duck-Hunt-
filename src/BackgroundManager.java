import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * The BackgroundManager class handles the management of background images, foreground images,
 * and crosshair images for the game.
 */
public class BackgroundManager {
    public static boolean changeScreenShown = false;
    public static int currentBackgroundIndex = 0;
    public static int currentCrosshairIndex = 0;

    /**
     * An array of file paths for the background images.
     */
    public static final String[] backgroundImages = {
            "assets/background/1.png",
            "assets/background/2.png",
            "assets/background/3.png",
            "assets/background/4.png",
            "assets/background/5.png",
            "assets/background/6.png"
    };

    /**
     * An array of file paths for the foreground images.
     */
    public static final String[] foregroundImages = {
            "assets/foreground/1.png",
            "assets/foreground/2.png",
            "assets/foreground/3.png",
            "assets/foreground/4.png",
            "assets/foreground/5.png",
            "assets/foreground/6.png"
    };

    /**
     * An array of file paths for the crosshair images.
     */
    public static final String[] crosshairImages = {
            "assets/crosshair/1.png",
            "assets/crosshair/2.png",
            "assets/crosshair/3.png",
            "assets/crosshair/4.png",
            "assets/crosshair/5.png",
            "assets/crosshair/6.png",
            "assets/crosshair/7.png"
    };

    /**
     * Sets the background image of the background selection screen.
     *
     * @param imagePath The file path of the background image.
     * @param root The root pane of the game's scene graph.
     */
    public static void setBackgroundImage(String imagePath, Pane root) {
        root.getChildren().removeIf(node -> node instanceof ImageView);
        Image backgroundImage = new Image(imagePath);
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(DuckHunt.SCREEN_WIDTH);
        backgroundImageView.setFitHeight(DuckHunt.SCREEN_HEIGHT);
        root.getChildren().add(backgroundImageView);
        setTexts(root);
    }

    /**
     * Sets the key events for the background selection screen.
     *
     * @param scene        The scene where the key events will be handled.
     * @param primaryStage The primary stage of the game.
     * @param root         The root pane of the game's scene graph.
     */
    public static void setKeyEvents(Scene scene, Stage primaryStage, Pane root) {
        Image cursorImage = new Image(crosshairImages[currentCrosshairIndex]);
        ImageView cursorImageView = new ImageView(cursorImage);
        cursorImageView.setFitWidth(cursorImageView.getLayoutBounds().getWidth() * DuckHunt.SCALE);
        cursorImageView.setFitHeight(cursorImageView.getLayoutBounds().getHeight() * DuckHunt.SCALE);
        cursorImageView.setX(DuckHunt.SCREEN_WIDTH / 2 - cursorImageView.getLayoutBounds().getWidth() / 2);
        cursorImageView.setY(DuckHunt.SCREEN_HEIGHT / 2 - cursorImageView.getLayoutBounds().getHeight() / 2);
        cursorImageView.setVisible(false);
        Pane cursorPane = new Pane();
        cursorPane.getChildren().add(cursorImageView);
        root.getChildren().add(cursorPane);
        cursorPane.toFront();


        scene.setOnKeyPressed(event -> {
            if (!changeScreenShown) {
                if (event.getCode() == KeyCode.ENTER) {
                    setBackgroundImage(backgroundImages[currentBackgroundIndex], root);
                    changeScreenShown = true;
                    cursorImageView.setVisible(true);
                    cursorPane.toFront();
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                }
            } else {
                if (event.getCode() == KeyCode.LEFT) {
                    currentBackgroundIndex = (currentBackgroundIndex - 1 + backgroundImages.length) % backgroundImages.length;
                } else if (event.getCode() == KeyCode.RIGHT) {
                    currentBackgroundIndex = (currentBackgroundIndex + 1) % backgroundImages.length;
                } else if (event.getCode() == KeyCode.UP) {
                    currentCrosshairIndex = (currentCrosshairIndex + 1) % crosshairImages.length;
                } else if (event.getCode() == KeyCode.DOWN) {
                    currentCrosshairIndex = (currentCrosshairIndex - 1 + crosshairImages.length) % crosshairImages.length;
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    DuckHunt.backgroundMusic.stop();
                    changeScreenShown=false;
                    DuckHunt duckHunt = new DuckHunt();
                    duckHunt.start(primaryStage);
                } else if (event.getCode() == KeyCode.ENTER) {
                    DuckHunt.backgroundMusic.stop();
                    DuckHunt.introMusic.play();
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    FirstLevel firstLevel = new FirstLevel();
                    firstLevel.start(primaryStage);
                }
                cursorImageView.setImage(new Image(crosshairImages[currentCrosshairIndex]));
                setBackgroundImage(backgroundImages[currentBackgroundIndex], root);
                cursorPane.toFront();
            }
        });
    }

    /**
     * Sets the texts displayed on the background selection screen.
     *
     * @param root The root pane of the game's scene graph.
     */
    private static void setTexts(Pane root) {
        Text text = new Text("USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START\n PRESS ESC TO ESCAPE");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 8 * DuckHunt.SCALE));
        text.setFill(Color.ORANGE);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setX((root.getWidth() - text.getLayoutBounds().getWidth()) / 2);
        text.setY(text.getLayoutBounds().getHeight() / 2);
        root.getChildren().addAll(text);
    }
}