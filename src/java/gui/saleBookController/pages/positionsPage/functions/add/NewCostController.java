package gui.saleBookController.pages.positionsPage.functions.add;

import gui.ApplicationMain;
import gui.FXutils.TextInputControlUtils;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.LabelUtils;
import utils.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.FXutils.StageUtils.createStyledStage;

/**
 * Controller to add new costs to a specific position
 *
 * @see gui.saleBookController.pages.FunctionDialog
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

    /**
     * Initializes this controller and his controls
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LabelUtils.setCurrencies(this.costCurrencyLbl);
        this.costTxtFld.setText("0");
        TextInputControlUtils.installTouch(this.costTxtFld);
        this.costTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
            this.applyBtn.setDisable(!StringUtils.isValidNumber(newText) && newText.startsWith("-"));
        });
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
