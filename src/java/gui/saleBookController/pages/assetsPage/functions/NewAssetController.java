package gui.saleBookController.pages.assetsPage.functions;

import gui.ApplicationMain;
import costumeClasses.FXClasses.BoundedDateCell;
import gui.FXutils.TextInputControlUtils;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.ChoiceBoxUtils;
import gui.FXutils.LabelUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Asset;
import logic.Supplier;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;
import utils.DoubleUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static gui.FXutils.StageUtils.createStyledStage;

/**
 *
 * @author xThe_white_Lionx
 * @Date 28.05.2024
 */
public class NewAssetController extends FunctionDialog<Asset> implements Initializable {

    @FXML
    private TextField nameTxtFld;
    /**
     * ChoiceBox to choose a supplier for the order
     */
    @FXML
    private ChoiceBox<String> supplierNameChcBx;
    /**
     * DatePicker to pick the purchasing date of the asset
     */
    @FXML
    private DatePicker purchasingDatePicker;
    /**
     * DatePicker to pick the arrival date of the asset
     */
    @FXML
    private DatePicker arrivalDatePicker;
    /**
     * TextField to set the value of the asset
     */
    @FXML
    private TextField valueTxtFld;
    /**
     * Label to display the currency, of the cost
     */
    @FXML
    private Label valueCurrencyLbl;
    /**
     * Button to apply the input
     */
    @FXML
    private Button applyBtn;

    /**
     * The SaleBook to which the new order should belong
     */
    private SaleBook saleBook;

    /**
     * Factory to create a NewOrderController
     *
     * @param saleBook the saleBook to which the order should be added
     * @return a new NewOrderController
     * @throws IOException if the loading of the fxml file fails to load
     */
    public static @NotNull NewAssetController createAssetController(@NotNull SaleBook saleBook)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/assetsPage/" +
                        "functions/NewAssetController.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("new asset");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        NewAssetController controller = loader.getController();
        controller.setSaleBook(saleBook);
        stage.showAndWait();
        return controller;
    }

    /**
     * Initializes this controller and his controls
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextInputControlUtils.installTouch(this.nameTxtFld);
        TextInputControlUtils.installTouch(this.valueTxtFld);

        LabelUtils.setCurrencies(this.valueCurrencyLbl);
        LocalDate now = LocalDate.now();
        this.purchasingDatePicker.setValue(now);
        this.purchasingDatePicker.setDayCellFactory(cf -> new BoundedDateCell(null, now));
        this.arrivalDatePicker.setDayCellFactory(cf -> new BoundedDateCell(this.purchasingDatePicker.getValue(), now));
    }

    /**
     * Handles the apply button and sets the result to the new created order.
     */
    @FXML
    public void handleApply() {
        Supplier supplier = this.saleBook.getSuppliersManager().getSupplier(this.supplierNameChcBx.getValue());
        if (supplier != null) {
            this.result = new Asset(this.saleBook.getAssetsManager().getNextAssetId(),
                    this.nameTxtFld.getText(), supplier, this.purchasingDatePicker.getValue(),
                    DoubleUtils.parse(this.valueTxtFld.getText()));
            LocalDate arrivalDate = this.arrivalDatePicker.getValue();
            if (arrivalDate != null) {
                this.result.setArrivalDate(arrivalDate);
            }
            this.handleCancel();
        }
    }

    /**
     * Handles the cancel button and closes the window
     */
    @FXML
    public void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the specified saleBook, the id of the new order.
     * Furthermore, possible suppliers and spare parts will be set and displayed.
     *
     * @param saleBook the current saleBook to operate on
     */
    private void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
        ChoiceBoxUtils.addItems(this.supplierNameChcBx, saleBook.getSuppliersManager().getSupplierNames());
    }
}
