package gui.saleBookController;

import gui.ApplicationMain;
import gui.saleBookController.pages.tenthPartPage.TenthPartPage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.util.StageUtils.createStyledStage;

/**
 *
 */
public class ShortcutsController implements Initializable {

    /**
     *
     */
    public GridPane gridPane;

    /**
     *
     */
    public static void load() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/Shortcuts.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("Shortcuts");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleCancel() {
        Stage stage = (Stage) this.gridPane.getScene().getWindow();
        stage.close();
    }
}
