package gui.saleBookController;

import gui.ApplicationMain;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.FXutils.StageUtils.createStyledStage;

/**
 * Controller to display the existing shortcuts for the application
 *
 * @author xthe_white_lionx
 */
public class ShortcutsController implements Initializable {

    /**
     * The base pane
     */
    @javafx.fxml.FXML
    private Pane basePane;

    /**
     * Loads a new ShortcutsController
     *
     * @throws IOException if the fxml cannot be loaded
     */
    public static void loadShortcutsController() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/Shortcuts.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("Shortcuts");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * Initializes this controller
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Closes the window
     */
    public void handleCancel() {
        Stage stage = (Stage) this.basePane.getScene().getWindow();
        stage.close();
    }
}
