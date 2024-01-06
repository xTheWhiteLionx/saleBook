package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.util.LabelUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.utils.BigDecimalUtils;
import logic.products.position.Position;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;
import static gui.util.StringUtils.isValidNumber;

/**
 *
 */
public class SaleController implements Initializable {

    /**
     *
     */
    @FXML
    private TextField sellingPriceTxtField;
    /**
     *
     */
    @FXML
    private Label sellingPriceLblCurrency;
    /**
     *
     */
    @FXML
    private DatePicker sellingDatePicker;
    /**
     *
     */
    @FXML
    private Button btnApply;
    /**
     *
     */
    @FXML
    private Button btnCancel;
    /**
     *
     */
    private SaleBook saleBook;
    /**
     * The current controlled investment
     */
    private Position position;

    /**
     *
     * @param position
     */
    private void loadSaleController(@NotNull Position position, @NotNull SaleBook saleBook){
        this.position = position;
        this.saleBook = saleBook;
    }

    /**
     *
     * @return
     */
    public static @NotNull SaleController createSaleController(@NotNull Position position,
                                                               @NotNull SaleBook saleBook)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/functions/SaleController.fxml"));

        Stage stage = createStyledStage(new Scene(loader.load()));
        stage.setTitle("sale");
        stage.setMinWidth(350D);
        stage.setMinHeight(200D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        SaleController controller = loader.getController();
        controller.loadSaleController(position, saleBook);
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
        LabelUtils.setCurrencies(this.sellingPriceLblCurrency);
        this.btnApply.setDisable(true);
        this.sellingDatePicker.setValue(LocalDate.now());
        this.sellingDatePicker.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                this.setDisable(item.isBefore(SaleController.this.position.getOrderDate()));
            }
        });
        this.sellingPriceTxtField.textProperty().addListener(
                (observableValue, oldValue, newValue) -> this.btnApply.setDisable(
                        !isValidNumber(newValue)));
    }

    /**
     *
     */
    @FXML
    private void handleApply() {
        BigDecimal sellingPrice = BigDecimalUtils.parse(this.sellingPriceTxtField.getText());
        this.saleBook.sale(this.position.getId(), this.sellingDatePicker.getValue(), sellingPrice);
        this.handleCancel();
    }

    /**
     *
     */
    @FXML
    private void handleCancel() {
     Stage stage = (Stage) this.btnCancel.getScene().getWindow();
     stage.close();
    }
}
