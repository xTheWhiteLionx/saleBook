package gui.util;

import gui.JarMain;
import gui.Config;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * Some Utility's to style scene's.
 *
 * @author xthewhitelionx
 */
public class StageUtils {

    /**
     * The icon of the application
     */
    private static final Image ICON = new Image("gui/textures/icons8-hauptbuchhaltung-96.png");

    /**
     * Styles the specified scene matching to the current theme. Adds the app icon to the stage of
     * the scene.
     *
     * @param scene the scene which should be styled
     * @throws IllegalArgumentException if the scene has no owner window/stage
     */
    public static void styleScene(@NotNull Scene scene) {
        Stage stage = (Stage) scene.getWindow();
        if (stage == null) {
            throw new IllegalArgumentException("the scene has no window/stage");
        }

        stage.getIcons().add(ICON);
        String path = String.format("css/themes/%s.css", Config.getTheme());
        scene.getStylesheets().setAll(JarMain.class.getResource(path).toExternalForm());
        scene.getStylesheets().add(JarMain.class.getResource("css/Global.css").toExternalForm());
    }

    /**
     * Creates a stage and sets the specified scene, which will be styled.
     * Look at {@link #styleScene(Scene)}
     *
     * @param scene the scene which should be styled
     */
    public static @NotNull Stage createStyledStage(@NotNull Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        styleScene(scene);
        return stage;
    }
}
