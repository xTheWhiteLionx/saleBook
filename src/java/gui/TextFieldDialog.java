package gui;

import gui.FXutils.LabelUtils;
import gui.FXutils.TextInputControlUtils;
import gui.saleBookController.pages.FunctionDialog;
import javafx.fxml.FXML;
import utils.StringUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.BigDecimalUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.FXutils.StageUtils.createStyledStage;

/**
 * Controller for an TextFieldDialog which returns an BigDecimal
 *
 * @author xthe_white_lionx
 */
public class TextFieldDialog extends FunctionDialog<BigDecimal> implements Initializable {

    /**
     * Label to describe the TextField
     */
    @FXML
    public Label lbl;

    /**
     * TextField to get the input from
     */
    @FXML
    public TextField textField;

    /**
     * Label to display the currency of the input of the {@link #textField}
     */
    @FXML
    private Label currencyLbl;

    /**
     * Button to apply the text of the TextField
     */
    @FXML
    public Button applyBtn;

    /**
     * Creates and loads a new TextFieldDialog with the specified title
     *
     * @param title title of the TextFieldDialog
     * @return the new created TextFieldDialog
     * @throws IOException if the fxml cannot be loaded
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
        controller.setLabelText("add " + title);
        stage.showAndWait();
        return controller;
    }

    /**
     * Sets the text of the label
     *
     * @param text the text that should be set on the label
     */
    private void setLabelText(String text) {
        this.lbl.setText(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextInputControlUtils.installTouch(this.textField);
        LabelUtils.setCurrencies(this.currencyLbl);
        this.applyBtn.setDisable(true);
        this.textField.textProperty().addListener((observableValue, oldText, newText) ->
                this.applyBtn.setDisable(!StringUtils.isValidNumber(newText)));
    }

    /**
     * Handles the apply button and sets the result
     */
    public void handleApply() {
        this.result = BigDecimalUtils.parse(this.textField.getText());
        this.handleCancel();
    }

    /**
     * Closes the window
     */
    public void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }
}
