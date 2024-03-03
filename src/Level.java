import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The Level class represents common properties shared by all levels.
 */
public class Level {
    Text gameOverText = new Text("GAME OVER!");
    Text winText = new Text("YOU WIN!");
    Text nextLevelText = new Text("Press ENTER to play next level");
    Text playAgainText = new Text("Press ENTER to play again\n       Press ESC to exit");
    Text completedGameText = new Text("You have completed the game!");

    /**
     * Constructs a Level object.
     * Initializes the game over text, win text, next level text,
     * completed game text, and play again text with their respective font, color, and positions.
     */
    public Level() {
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 16 * DuckHunt.SCALE));
        gameOverText.setFill(Color.ORANGE);
        gameOverText.setX((DuckHunt.SCREEN_WIDTH - gameOverText.getLayoutBounds().getWidth()) / 2);
        gameOverText.setY((DuckHunt.SCREEN_HEIGHT + gameOverText.getLayoutBounds().getHeight()) / 2 - 18 * DuckHunt.SCALE);
        gameOverText.setVisible(false);

        winText.setFont(Font.font("Arial", FontWeight.BOLD, 16 * DuckHunt.SCALE));
        winText.setFill(Color.ORANGE);
        winText.setX((DuckHunt.SCREEN_WIDTH - winText.getLayoutBounds().getWidth()) / 2);
        winText.setY((DuckHunt.SCREEN_HEIGHT + winText.getLayoutBounds().getHeight()) / 2 - 8 * DuckHunt.SCALE);
        winText.setVisible(false);

        nextLevelText.setFont(Font.font("Arial", FontWeight.BOLD, 16 * DuckHunt.SCALE));
        nextLevelText.setFill(Color.ORANGE);
        nextLevelText.setX((DuckHunt.SCREEN_WIDTH - nextLevelText.getLayoutBounds().getWidth()) / 2); // Ekran ortasında konumlandırma
        nextLevelText.setY((DuckHunt.SCREEN_HEIGHT + nextLevelText.getLayoutBounds().getHeight()) / 2 + 8 * DuckHunt.SCALE); // Ekran ortasında konumlandırma
        nextLevelText.setVisible(false);

        completedGameText.setFont(Font.font("Arial", FontWeight.BOLD, 16 * DuckHunt.SCALE));
        completedGameText.setFill(Color.ORANGE);
        completedGameText.setX((DuckHunt.SCREEN_WIDTH - completedGameText.getLayoutBounds().getWidth()) / 2);
        completedGameText.setY((DuckHunt.SCREEN_HEIGHT + completedGameText.getLayoutBounds().getHeight()) / 2 - 18 * DuckHunt.SCALE);
        completedGameText.setVisible(false);

        playAgainText.setFont(Font.font("Arial", FontWeight.BOLD, 16 * DuckHunt.SCALE));
        playAgainText.setFill(Color.ORANGE);
        playAgainText.setX((DuckHunt.SCREEN_WIDTH - playAgainText.getLayoutBounds().getWidth()) / 2);
        playAgainText.setY((DuckHunt.SCREEN_HEIGHT + playAgainText.getLayoutBounds().getHeight()) / 2 - 8 * DuckHunt.SCALE);
        playAgainText.setVisible(false);
    }

    /**
     * Sets the crosshair for the game scene.
     * @param root The root group of the scene.
     * @param gameScene The game scene.
     */
    public static void setCursor(Group root, Scene gameScene) {
        ImageView cursorImageView = new ImageView(new Image(BackgroundManager.crosshairImages[BackgroundManager.currentCrosshairIndex]));
        cursorImageView.setFitWidth(cursorImageView.getLayoutBounds().getWidth() * DuckHunt.SCALE);
        cursorImageView.setFitHeight(cursorImageView.getLayoutBounds().getHeight() * DuckHunt.SCALE);
        cursorImageView.setLayoutX(DuckHunt.SCREEN_WIDTH / 2 - cursorImageView.getLayoutBounds().getWidth() / 2);
        cursorImageView.setLayoutY(DuckHunt.SCREEN_HEIGHT / 2 - cursorImageView.getLayoutBounds().getHeight() / 2);
        root.getChildren().add(cursorImageView);

        gameScene.setOnMouseExited(event -> cursorImageView.setVisible(false));

        gameScene.setOnMouseEntered(event -> cursorImageView.setVisible(true));

        gameScene.setCursor(Cursor.NONE);
        gameScene.setOnMouseMoved(event -> {
            cursorImageView.setLayoutX(event.getSceneX() - cursorImageView.getFitWidth() / 2);
            cursorImageView.setLayoutY(event.getSceneY() - cursorImageView.getFitHeight() / 2);
        });
    }

    /**
     * Shows the all texts which has fade-in animation.
     * @param text The Text object to be shown.
     */
    public static void showText(Text text) {
        text.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), text);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    /**
     * Shows the all texts which has flashing animation.
     * @param text The Text object to be shown.
     */
    public static void showFlashText(Text text) {
        text.setVisible(true);
        Timeline flashingAnimationTimeline = new Timeline(
                new KeyFrame(Duration.millis(500), new KeyValue(text.opacityProperty(), 0.0)),
                new KeyFrame(Duration.millis(1000), new KeyValue(text.opacityProperty(), 1.0))
        );
        flashingAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
        flashingAnimationTimeline.setAutoReverse(true);
        flashingAnimationTimeline.play();
    }

    /**
     * Generates a Text object displaying the level information.
     * @param level The level number.
     * @return The generated Text object.
     */
    public static Text getLevelText(int level) {
        Text levelText = new Text("Level " + level + "/6");
        levelText.setFont(Font.font("Arial", FontWeight.BOLD, 10 * DuckHunt.SCALE));
        levelText.setFill(Color.ORANGE);
        levelText.setX(DuckHunt.SCREEN_WIDTH / 2 - levelText.getLayoutBounds().getWidth() / 2);
        levelText.setY(levelText.getLayoutBounds().getHeight());
        return levelText;
    }

    /**
     * Generates a Text object displaying the AMMO information.
     * @param ammoText The ammo number.
     * @return The generated Text object.
     */
    public static void ammoTextSetting(Text ammoText) {
        ammoText.setFont(Font.font("Arial", FontWeight.BOLD, 10 * DuckHunt.SCALE));
        ammoText.setFill(Color.ORANGE);
        ammoText.setX(DuckHunt.SCREEN_WIDTH - 64 * DuckHunt.SCALE); // Sağ üst köşede konumlandırma
        ammoText.setY(ammoText.getLayoutBounds().getHeight());
    }

    /**
     * Configures the properties of the duck ImageView.
     * @param initialX The initial X position of the duck.
     * @param initialY The initial Y position of the duck.
     * @param duckImageView The ImageView of the duck.
     */
    public static void configureDuckImageView(double initialX, double initialY, ImageView duckImageView) {
        duckImageView.setLayoutX(initialX);
        duckImageView.setLayoutY(initialY);
        duckImageView.setFitWidth(duckImageView.getLayoutBounds().getWidth() * DuckHunt.SCALE);
        duckImageView.setFitHeight(duckImageView.getLayoutBounds().getHeight() * DuckHunt.SCALE);
    }
}