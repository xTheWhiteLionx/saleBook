package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.FXutils.LabelUtils;
import gui.FXutils.TextInputControlUtils;
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
import logic.products.position.Position;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.FXutils.StageUtils.createStyledStage;
import static utils.StringUtils.isValidNumber;

/**
 *
 * @author xthe_white_lionx
 */
//TODO JavaDoc
public class PerformanceCalculatorController implements Initializable {
    /**
     * TextField for the selling price
     */
    @FXML
    private TextField sellingPriceTxtFld;

    /**
     * Label to display the currency of the selling price
     */
    @FXML
    private Label sellingPriceCurrencyLbl;

    /**
     * Label to display the calculated performance
     */
    @FXML
    private Label performanceLbl;

    /**
     * Button to start the calculation
     */
    @FXML
    private Button btnCalculate;

    /**
     * Button to cancel the calculation and close the window
     */
    @FXML
    private Button btnCancel;

    /**
     * The current position
     */
    private Position currPosition;

    /**
     * Loads and shows a new PerformanceCalculatorController and waits of the input of the user
     *
     * @param position the position needed for calculation
     */
    public static void loadPerformanceCalculatorController(Position position) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/functions/" +
                        "PerformanceCalculatorController.fxml"));

        Stage stage = createStyledStage(new Scene(loader.load()));
        stage.setTitle("Performance calculator");
        stage.setMinWidth(350D);
        stage.setMinHeight(200D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        PerformanceCalculatorController performanceCalculatorController = loader.getController();
        performanceCalculatorController.currPosition = position;
        stage.showAndWait();
    }

    /**
     * Initializes the controls of this controller.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextInputControlUtils.installTouch(this.sellingPriceTxtFld);
        LabelUtils.setCurrencies(this.sellingPriceCurrencyLbl);
        this.btnCalculate.setDisable(true);
        this.sellingPriceTxtFld.textProperty().addListener(
                (observableValue, oldValue, newValue) -> this.btnCalculate.setDisable(
                        !isValidNumber(newValue)));
    }

    /**
     * Handles the "Apply" Button and displays the result of the calculation
     */
    @FXML
    private void handleCalculate() {
        if (this.currPosition != null) {
            BigDecimal sellingPrice = BigDecimalUtils.parse(this.sellingPriceTxtFld.getText());
            BigDecimal performance = this.currPosition.calcPerformance(sellingPrice);
            LabelUtils.setMoneyAndColor(this.performanceLbl, performance);
        }
    }

    /**
     * Handles the close button and closes the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.btnCancel.getScene().getWindow();
        stage.close();
    }
}
