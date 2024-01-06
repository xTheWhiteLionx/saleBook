package gui.saleBookController.pages.positionsPage.functions.add;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.util.LabelUtils;
import gui.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.utils.BigDecimalUtils;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;

/**
 * Controller to add new costs to a specific position
 */
public class NewCostController extends FunctionDialog<BigDecimal> implements Initializable {
    /**
     * TextField for the new cost
     */
    @FXML
    private TextField costTxtFld;
    /**
     * Label to display the currency of the cost
     */
    @FXML
    private Label costCurrencyLbl;
    /**
     * Button to apply the new cost
     */
    @FXML
    private Button applyBtn;

    /**
     *
     */
    private final ValidationSupport validationSupport = new ValidationSupport();

    /**
     * Loads and creates a new newCostController
     *
     * @return a new newCostController
     * @throws IOException If the matching fxml could not be loaded
     */
    public static @NotNull NewCostController createNewCostController() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage" +
                        "/functions/add/NewCostController.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("add cost");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return loader.getController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LabelUtils.setCurrencies(this.costCurrencyLbl);
        this.costTxtFld.setText("0");
        this.costTxtFld.getProperties().put("vkType", "numeric");
        this.validationSupport.registerValidator(this.costTxtFld, (control, o) ->
                ValidationResult.fromErrorIf(this.costTxtFld, "is no valid number",
                        !StringUtils.isValidNumber(this.costTxtFld.getText())));
        this.validationSupport.validationResultProperty().addListener((observableValue, validationResult, t1) ->
                this.applyBtn.setDisable(this.validationSupport.isInvalid()));
    }

    /**
     * Handles the close button and closes the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the apply button, sets the result of this {@link FunctionDialog}
     * and closes the Window.
     */
    @FXML
    private void handleApply() {
        this.result = BigDecimalUtils.parse(this.costTxtFld.getText());
        this.handleCancel();
    }
}
