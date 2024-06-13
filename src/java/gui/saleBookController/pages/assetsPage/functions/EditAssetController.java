package gui.saleBookController.pages.assetsPage.functions;

import gui.ApplicationMain;
import costumeClasses.FXClasses.BindedBoundedDateCell;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.ChoiceBoxUtils;
import gui.FXutils.LabelUtils;
import utils.DoubleUtils;
import utils.StringUtils;
import javafx.beans.binding.BooleanBinding;
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
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.FXutils.StageUtils.createStyledStage;

/**
 * This class represents a controller to create a new order.
 */
public class EditAssetController extends FunctionDialog<Boolean> implements Initializable {

    /**
     *
     */
    @FXML
    private Label idLbl;

    /**
     *
     */
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
    private Asset asset;

    /**
     *
     */
    private SaleBook saleBook;

    /**
     * Factory to create a NewOrderController
     *
     * @param saleBook the saleBook to which the order should be added
     * @return a new NewOrderController
     * @throws IOException if the loading of the fxml file fails to load
     */
    public static @NotNull EditAssetController createEditAssetController(@NotNull Asset asset,
                                                                         @NotNull SaleBook saleBook)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/assetsPage/" +
                        "functions/EditAssetController.fxml"));

        Scene scene = new Scene(loader.load(), 550, 550);
        Stage stage = createStyledStage(scene);
        stage.setTitle("new asset");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        EditAssetController controller = loader.getController();
        controller.setAsset(asset, saleBook);
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

        this.purchasingDatePicker.valueProperty().addListener((observableValue, oldDate, newDate) -> {
            this.arrivalDatePicker.setValue(newDate);
        });

        this.arrivalDatePicker.setDayCellFactory(dateCell ->
                new BindedBoundedDateCell(this.purchasingDatePicker.valueProperty(), null));

        BooleanBinding booleanBinding = new BooleanBinding() {
            { this.bind(EditAssetController.this.nameTxtFld.textProperty(),
                    EditAssetController.this.valueTxtFld.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return EditAssetController.this.nameTxtFld.getText().isEmpty() ||
                        !StringUtils.isValidNumber(EditAssetController.this.valueTxtFld.getText());
            }
        };

        this.applyBtn.disableProperty().bind(booleanBinding);
    }

    /**
     * Handles the apply button and sets the result to the new created order.
     */
    @FXML
    public void handleApply() {
        this.asset.setName(this.nameTxtFld.getText());
        String chosenSupplierName = this.supplierChcBx.getValue();
        this.asset.setSupplier(this.saleBook.getSuppliersManager().getSupplier(chosenSupplierName));
        this.asset.setPurchasingDate(this.purchasingDatePicker.getValue());
        this.asset.setArrivalDate(this.arrivalDatePicker.getValue());
        this.asset.setValue(DoubleUtils.parse(this.valueTxtFld.getText()));
        this.result = true;
        this.handleCancel();
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
    private void setAsset(@NotNull Asset asset, SaleBook saleBook) {
        this.asset = asset;
        this.saleBook = saleBook;
        ChoiceBoxUtils.addItems(this.supplierChcBx, saleBook.getSuppliersManager().getSupplierNames());

        this.idLbl.setText(String.valueOf(asset.getId()));
        this.nameTxtFld.setText(asset.getName());
        this.supplierChcBx.setValue(asset.getSupplier().getName());
        this.purchasingDatePicker.setValue(asset.getPurchasingDate());
        this.arrivalDatePicker.setValue(asset.getArrivalDate());
        this.valueTxtFld.setText(String.valueOf(asset.getValue()));
    }
}
