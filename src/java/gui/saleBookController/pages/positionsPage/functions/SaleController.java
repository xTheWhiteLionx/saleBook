package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import costumeClasses.FXClasses.BoundedDateCell;
import gui.FXutils.LabelUtils;
import gui.FXutils.TextInputControlUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.BigDecimalUtils;
import logic.products.position.Position;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.FXutils.StageUtils.createStyledStage;
import static utils.StringUtils.isValidNumber;

/**
 * Controller to sale a specified position
 *
 * @author xthe_white_lionx
 */
public class SaleController implements Initializable {

    /**
     * TextField to set the selling price
     */
    @FXML
    private TextField sellingPriceTxtField;

    /**
     * Label to display the currency of the selling price
     */
    @FXML
    private Label sellingPriceLblCurrency;

    /**
     * DatePicker to pick the selling date
     */
    @FXML
    private DatePicker sellingDatePicker;

    /**
     * Button to apply the selling data
     */
    @FXML
    private Button btnApply;

    /**
     * Button to cancel the selling
     */
    @FXML
    private Button btnCancel;

    /**
     *
     */
    private SaleBook saleBook;

    /**
     * The current position
     */
    private Position position;

    /**
     *
     *
     * @param position
     */
    private void loadSaleController(@NotNull Position position, @NotNull SaleBook saleBook) {
        this.position = position;
        this.sellingDatePicker.setDayCellFactory(cf -> new BoundedDateCell(
                this.position.getReceivedDate(), null));
        this.saleBook = saleBook;
    }

    /**
     * Creates a new SaleController
     *
     * @param position the position, which should be sold
     * @param saleBook saleBook to overhand the input
     * @return the new created saleController
     * @throws IOException if the fxml file cannot be loaded
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
        controller.saleBook = saleBook;
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
        TextInputControlUtils.installTouch(this.sellingPriceTxtField);
        LabelUtils.setCurrencies(this.sellingPriceLblCurrency);
        this.btnApply.setDisable(true);
        this.sellingDatePicker.setValue(LocalDate.now());
        this.sellingPriceTxtField.textProperty().addListener(
                (observableValue, oldText, newText) -> this.btnApply.setDisable(
                        !isValidNumber(newText))
        );
    }

    /**
     *
     */
    @FXML
    private void handleApply() {
        BigDecimal sellingPrice = BigDecimalUtils.parse(this.sellingPriceTxtField.getText());
        this.saleBook.getPositionsManager().sale(this.position.getId(), this.sellingDatePicker.getValue(), sellingPrice);
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
