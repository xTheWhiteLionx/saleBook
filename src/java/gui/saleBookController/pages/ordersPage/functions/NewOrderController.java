package gui.saleBookController.pages.ordersPage.functions;

import gui.ApplicationMain;
import gui.BoundedDateCell;
import gui.DialogWindow;
import gui.SpinnerTableCell;
import gui.SpinnerTableColumn;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.ChoiceBoxUtils;
import gui.FXutils.LabelUtils;
import gui.saleBookController.pages.sparePartsPage.functions.NewSparePartController;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import logic.manager.SparePartsManager;
import utils.DoubleUtils;
import utils.StringUtils;
import gui.FXutils.TableViewUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Condition;
import logic.sparePart.SparePart;
import logic.order.Order;
import logic.Supplier;
import logic.saleBook.SaleBook;
import utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static gui.FXutils.StageUtils.createStyledStage;

/**
 * This class represents a controller to create a new order.
 */
public class NewOrderController extends FunctionDialog<Order> implements Initializable {

    /**
     * TableView to display order able spare parts
     */
    @FXML
    private TableView<SparePart> sparePartsTblVw;
    /**
     * ChoiceBox to choose a supplier for the order
     */
    @FXML
    private ChoiceBox<String> supplierChcBx;
    /**
     * TextField for the cost of the order
     */
    @FXML
    private TextField orderCostTxtFld;
    /**
     * Label to display the currency, of the cost
     */
    @FXML
    private Label orderCostCurrencyLbl;

    /**
     *
     */
    @FXML
    public DatePicker orderDatePicker;

    /**
     * Button to apply the input
     */
    @FXML
    private Button applyBtn;

    /**
     * The id of the new order
     */
    private int orderId;

    /**
     * The SaleBook to which the new order should belong
     */
    private SaleBook saleBook;

    /**
     * SpinnerTableColumn needed to display a spinner for each row of the tableView
     */
    private SpinnerTableColumn spinnerTableColumn;

    /**
     * Factory to create a NewOrderController
     *
     * @param saleBook the saleBook to which the order should be added
     * @return a new NewOrderController
     * @throws IOException if the loading of the fxml file fails to load
     */
    public static @NotNull NewOrderController createNewOrderController(@NotNull SaleBook saleBook)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/ordersPage/" +
                        "functions/NewOrderController.fxml"));

        Scene scene = new Scene(loader.load(), 600, 600);
        Stage stage = createStyledStage(scene);
        stage.setTitle("new order");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        NewOrderController controller = loader.getController();
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
        TableViewUtils.addColumn(this.sparePartsTblVw, "name", SparePart::getName);
        TableViewUtils.addColumn(this.sparePartsTblVw, "condition", SparePart::getCondition);
        this.spinnerTableColumn = new SpinnerTableColumn("ordered",
                SpinnerTableCell.MaxValueType.UNLIMITED);
        this.sparePartsTblVw.getColumns().add(this.spinnerTableColumn);

        this.orderDatePicker.setValue(LocalDate.now());
        this.orderDatePicker.setDayCellFactory(cf -> new BoundedDateCell(null, LocalDate.now()));
        LabelUtils.setCurrencies(this.orderCostCurrencyLbl);
        this.orderCostTxtFld.textProperty().addListener((observableValue, oldValue, newValue) ->
                this.applyBtn.setDisable(!StringUtils.isValidNumber(newValue)));
    }

    /**
     * Handles the apply button and sets the result to the new created order.
     */
    @FXML
    public void handleApply() {
        //TODO 31.05.2024 Does not check if no spare part were chosen. No Order without spare part!
        //this.spinnerTableColumn.getSparePartToSpinnerValue();

        Supplier supplier = this.saleBook.getSuppliersManager().getSupplier(this.supplierChcBx.getValue());
        if (supplier != null) {
            this.result = new Order(this.orderId, this.orderDatePicker.getValue(), supplier,
                    this.spinnerTableColumn.getSparePartToSpinnerValue(),
                    DoubleUtils.parse(this.orderCostTxtFld.getText()));
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
        this.orderId = saleBook.getOrdersManager().getNextOrderId();
        Set<SparePart> spareParts = saleBook.getSparePartsManager().getSpareParts();
        ObservableList<SparePart> tblVwItems = this.sparePartsTblVw.getItems();
        for (SparePart sparePart : spareParts) {
            if(sparePart.getCondition() == Condition.NEW){
                tblVwItems.add(sparePart);
            }
        }
        ChoiceBoxUtils.addItems(this.supplierChcBx, saleBook.getSuppliersManager().getSupplierNames());
    }

    /**
     *
     * @param actionEvent
     */
    @FXML
    private void addNewSparePart(ActionEvent actionEvent) {
        try {
            SparePartsManager sparePartsManager = this.saleBook.getSparePartsManager();
            NewSparePartController sparePartController =
                    NewSparePartController.createSparePartController(sparePartsManager.getSparePartNames(),
                            sparePartsManager.getSparePartUnits(),
                            this.saleBook.getCategories());
            sparePartController.getResult().ifPresent(sparePart -> {
                //TODO 22.05.2024 check if sparePart already exists
                this.sparePartsTblVw.getItems().add(sparePart);
            });
        } catch (IOException e) {
            DialogWindow.displayError(e);
        }
    }
}
