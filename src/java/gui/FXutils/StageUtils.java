package gui.FXutils;

import gui.JarMain;
import gui.Config;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * Some Utility's to style scene's.
 *
 * @author xthe_white_lionx
 */
public class StageUtils {

    /**
     * The icon of the application
     */
    public static final Image ICON = new Image("gui/textures/icons8-hauptbuchhaltung-96.png");

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private StageUtils() {
    }

    /**
     * Styles the specified stage and his scene matching to the current theme.
     * Adds the app icon to the stage.
     *
     * @param stage the stage and his scene, which should be styled
     * @throws IllegalArgumentException if the stage has no scene
     */
    public static void styleStage(@NotNull Stage stage) {
        Scene scene = stage.getScene();
        if (scene == null) {
            throw new IllegalArgumentException("the stage has no scene");
        }

        stage.getIcons().add(ICON);
        String path = String.format("css/themes/%s.css", Config.getTheme());
        scene.getStylesheets().setAll(JarMain.class.getResource(path).toExternalForm(),
                JarMain.class.getResource("css/Global.css").toExternalForm());
    }

    /**
     * Creates a stage and sets the specified scene, which will be styled.
     * Look at {@link #styleStage(Stage)}
     *
     * @param scene the scene which should be styled
     * @return the newly created styled stage
     */
    public static @NotNull Stage createStyledStage(@NotNull Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        styleStage(stage);
        return stage;
    }
}
