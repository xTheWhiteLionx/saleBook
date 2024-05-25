package gui.saleBookController.pages.assetsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.ChoiceBoxUtils;
import gui.FXutils.LabelUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Asset;
import logic.Supplier;
import logic.saleBook.SaleBook;
import utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static gui.FXutils.StageUtils.createStyledStage;

/**
 * This class represents a controller to create a new order.
 */
public class NewAssetController extends FunctionDialog<Asset> implements Initializable {
    @FXML
    private TextField nameTxtFld;
    /**
     * ChoiceBox to choose a supplier for the order
     */
    @FXML
    private ChoiceBox<String> supplierChcBx;
    @FXML
    private DatePicker purchasingDatePicker;
    @FXML
    private DatePicker arrivalDatePicker;
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

        double size = 250D;
        Scene scene = new Scene(loader.load(), size, size);
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
        LabelUtils.setCurrencies(this.valueCurrencyLbl);
    }

    /**
     * Handles the apply button and sets the result to the new created order.
     */
    @FXML
    public void handleApply() {
        Supplier supplier = this.saleBook.getSuppliersManager().getSupplier(this.supplierChcBx.getValue());
        if (supplier != null) {
            this.result = new Asset(this.saleBook.getAssetsManager().getNextAssetId(), this.nameTxtFld.getText(),
                    supplier, this.purchasingDatePicker.getValue(),
                    this.arrivalDatePicker.getValue(),
                    BigDecimalUtils.parse(this.valueTxtFld.getText()));
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
        ChoiceBoxUtils.addItems(this.supplierChcBx, saleBook.getSuppliersManager().getSupplierNames());
    }
}
