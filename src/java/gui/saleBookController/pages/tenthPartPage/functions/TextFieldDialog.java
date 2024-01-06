package gui.saleBookController.pages.tenthPartPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.saleBookController.pages.ordersPage.functions.NewOrderController;
import gui.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.util.StageUtils.createStyledStage;

/**
 *
 */
public class TextFieldDialog extends FunctionDialog<String> implements Initializable {
    /**
     *
     */
    public Label lbl;
    /**
     *
     */
    public TextField textField;
    /**
     *
     */
    public Button applyBtn;

    /**
     *
     * @param title
     * @return
     * @throws IOException
     */
    public static TextFieldDialog createTextFieldDialog(String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/tenthPartPage/" +
                        "functions/TextFieldDialog.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        TextFieldDialog controller = loader.getController();
        controller.setFieldName("add " + title);
        stage.showAndWait();
        return controller;
    }

    /**
     *
     * @param fieldName
     */
    private void setFieldName(String fieldName) {
        this.lbl.setText(fieldName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.applyBtn.setDisable(true);
        this.textField.textProperty().addListener((observableValue, oldText, newText) -> {
            this.applyBtn.setDisable(!StringUtils.isValidNumber(newText));
        });
    }

    /**
     *
     */
    public void handleApply() {
        this.result = this.textField.getText();
        this.handleCancel();
    }

    /**
     *
     */
    public void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }
}
