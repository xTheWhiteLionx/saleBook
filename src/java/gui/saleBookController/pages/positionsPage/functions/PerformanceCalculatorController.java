package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.util.LabelUtils;
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
import logic.products.position.Position;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;
import static gui.util.StringUtils.isValidNumber;

/**
 *
 */
//TODO JavaDoc
public class PerformanceCalculatorController implements Initializable {
    /**
     *
     */
    @FXML
    private TextField sellingPriceTxtField;
    /**
     *
     */
    @FXML
    private Label sellingPriceCurrencyLbl;
    /**
     *
     */
    @FXML
    private Label performanceLbl;
    /**
     *
     */
    @FXML
    private Button btnCalculate;
    /**
     *
     */
    @FXML
    private Button btnCancel;

    /**
     * The current controlled investment
     */
    private Position currPosition;

    /**
     *
     * @param position
     */
    public static void loadPerformanceCalculatorController(Position position) throws IOException {
        PerformanceCalculatorController performanceCalculatorController =
                createPerformanceCalculatorController();
        performanceCalculatorController.currPosition = position;
    }

    /**
     *
     * @return
     */
    private static PerformanceCalculatorController createPerformanceCalculatorController() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/functions/" +
                        "PerformanceCalculatorController.fxml"));

        Stage stage = createStyledStage(new Scene(loader.load()));
        stage.setTitle("Performance Calculator");
        stage.setMinWidth(350D);
        stage.setMinHeight(200D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        return loader.getController();
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LabelUtils.setCurrencies(sellingPriceCurrencyLbl);
        btnCalculate.setDisable(true);
        sellingPriceTxtField.textProperty().addListener(
                (observableValue, oldValue, newValue) -> btnCalculate.setDisable(
                        !isValidNumber(newValue)));
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleCalculate() {
        BigDecimal sellingPrice = BigDecimalUtils.parse(sellingPriceTxtField.getText());
        BigDecimal performance = currPosition.calcPerformance(sellingPrice);
        LabelUtils.setMoneyAndColor(performanceLbl, performance);
    }

    /**
     * Closes the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
