package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.JavaFXGUI;
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
import org.jetbrains.annotations.NotNull;

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
public class SellingPriceCalculatorController implements Initializable {
    /**
     *
     */
    @FXML
    private TextField targetPerformanceTxtField;
    /**
     *
     */
    @FXML
    private Label targetPerformanceCurrency;
    /**
     *
     */
    @FXML
    private Label sellingExchangeRateLbl;
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
     * The current controlled position
     */
    private Position item;

    /**
     *
     * @param position
     */
    private void setPosition(Position position) {
        this.item = position;
    }

    /**
     *
     * @param position
     * @return
     */
    public static @NotNull SellingPriceCalculatorController createSellingPriceCalculatorController(
            @NotNull Position position) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/functions/" +
                        "SellingPriceCalculatorController.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("selling price calculator ");
        stage.setMinWidth(350D);
        stage.setMinHeight(200D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        SellingPriceCalculatorController controller = loader.getController();
        controller.setPosition(position);
        return controller;
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LabelUtils.setCurrencies(this.targetPerformanceCurrency);
        this.btnCalculate.setDisable(true);
        this.targetPerformanceTxtField.textProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    this.btnCalculate.setDisable(!isValidNumber(newValue));
                });
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleCalculate() {
        BigDecimal targetPerformance = BigDecimalUtils.parse(this.targetPerformanceTxtField.getText());
        BigDecimal sellingExchangeRate =
                this.item.calcPerformance(targetPerformance);
        this.sellingExchangeRateLbl.setText(JavaFXGUI.formatMoney(sellingExchangeRate));
    }

    /**
     * Close the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.btnCancel.getScene().getWindow();
        stage.close();
    }
}
