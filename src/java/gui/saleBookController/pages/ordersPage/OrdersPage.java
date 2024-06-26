package gui.saleBookController.pages.ordersPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.DialogWindow;
import gui.FXutils.TextInputControlUtils;
import costumeClasses.FXClasses.ImageButton;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.ordersPage.functions.NewOrderController;
import gui.FXutils.StageUtils;
import javafx.event.ActionEvent;
import logic.manager.OrdersManager;
import utils.StringUtils;
import gui.FXutils.TableViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.sparePart.SparePart;
import logic.order.Order;
import logic.saleBook.SaleBook;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static gui.DialogWindow.acceptedDeleteAlert;
import static gui.Images.*;
import static gui.JavaFXGUI.formatMoney;
import static gui.saleBookController.pages.ordersPage.functions.NewOrderController.createNewOrderController;
import static gui.FXutils.RibbonGroupUtils.createRibbonGroup;

/**
 * This class displays the orders of the saleBook and has some controls to interact with.
 *
 * @author xthe_white_lionx
 */
public class OrdersPage implements Initializable, Page {
    /**
     * The main pane
     */
    @FXML
    private Pane pane;
    /**
     * TableView to display the orders
     */
    @FXML
    private TableView<Order> orderTblVw;
    /**
     * FilteredList of the orders
     */
    private FilteredList<Order> ordersFilteredList;
    /**
     * Searchbar TextField to search a specific sparePart by name
     */
    @FXML
    private CustomTextField sparePartSearchbarTxtFld;
    /**
     * Searchbar TextField to search a specific order by id
     */
    @FXML
    private CustomTextField orderSearchbarTxtFld;
    /**
     * Button to clean the sparePart searchbar
     */
    @FXML
    private Button cleanOrderSearchBarBtn;
    /**
     * Button to clean the sparePart searchbar
     */
    @FXML
    private Button cleanSparePartSearchBarBtn;
    /**
     * TableView to display the sparePartsFilteredList
     */
    @FXML
    public TableView<SparePart> sparePartTblVw;

    /**
     * Button to set the selected order to receive
     */
    @FXML
    private Button receivedOrderBtn;
    /**
     * Button to delete the selected order
     */
    @FXML
    private Button cancelOrderBtn;
    /**
     * Button to set the current spare part to state received
     */
    @FXML
    private Button receivedSparePartBtn;
    /**
     * FilteredList of the spareParts of the selected order
     */
    @FXML
    private FilteredList<SparePart> sparePartsFilteredList;
    /**
     * RibbonTab with controls
     */
    @FXML
    private RibbonTab orderRibbonTab;
    /**
     * The current saleBook
     */
    private SaleBook saleBook;
    /**
     * The orderManager of the saleBook
     */
    private OrdersManager ordersManager;
    /**
     * The selected order
     */
    private Order selectedOrder;

    /**
     * Creates and returns a new OrderPage
     *
     * @return a new OrderPage
     */
    public static @NotNull OrdersPage createOrderPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/ordersPage" +
                        "/OrdersPage.fxml"));

        loader.load();
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
        this.initializeRibbonTab();

        TableViewUtils.addColumn(this.orderTblVw, "id", Order::getId);
        TableViewUtils.addColumn(this.orderTblVw, "state", order ->
                order.getState().name());
        TableViewUtils.addColumn(this.orderTblVw, "supplier", order ->
                order.getSupplier().getName());
        TableViewUtils.addColumn(this.orderTblVw, "cost", order ->
                formatMoney(order.getValue()));
        this.orderTblVw.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    this.selectedOrder = newValue;
                    this.updateDetail();
                });
        TextInputControlUtils.installTouch(this.orderSearchbarTxtFld);
        this.orderSearchbarTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
            if (newText.isEmpty()){
                this.ordersFilteredList.setPredicate(order -> true);
                this.cleanOrderSearchBarBtn.setVisible(false);
            } else if (!newText.matches("[,.]") && StringUtils.isValidNumber(newText)){
                int searchedId = Integer.parseInt(newText);
                this.ordersFilteredList.setPredicate(order -> order.getId() == searchedId);
                this.cleanOrderSearchBarBtn.setVisible(true);
            } else {
                this.ordersFilteredList.setPredicate(order -> false);
                this.cleanOrderSearchBarBtn.setVisible(true);
            }
        });

        TableViewUtils.addColumn(this.sparePartTblVw, "name", SparePart::getName);
        TableViewUtils.addColumn(this.sparePartTblVw, "state", sparePart -> this.selectedOrder.getState(sparePart).name());
        TableViewUtils.addColumn(this.sparePartTblVw, "order quantity",
                sparePart -> this.selectedOrder.getOrderQuantity(sparePart) + " " + sparePart.getUnit());
        TableViewUtils.addColumn(this.sparePartTblVw, "for", SparePart::getCategory);
        this.sparePartTblVw.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    this.receivedSparePartBtn.setDisable(newValue == null
                            || !this.selectedOrder.isReceivable(newValue));
                });

        TextInputControlUtils.installTouch(this.sparePartSearchbarTxtFld);
        this.sparePartSearchbarTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
            if (newText.isEmpty()){
                this.sparePartsFilteredList.setPredicate(sparePart -> true);
                this.cleanSparePartSearchBarBtn.setVisible(false);
            } else {
                this.sparePartsFilteredList.setPredicate(sparePart ->
                        sparePart.getName().toLowerCase().contains(newText.toLowerCase()));
                this.cleanSparePartSearchBarBtn.setVisible(true);
            }
        });
    }

    /**
     * Sets the saleBook of this OrderPage
     *
     * @param saleBook the saleBook which should be set
     */
    @Override
    public void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
        this.ordersManager = saleBook.getOrdersManager();
    }

    /**
     * Returns the base pane of this OrderPage
     *
     * @return the base pane of this OrderPage
     */
    @Override
    public @NotNull Pane getBasePane() {
        return this.pane;
    }

    /**
     * Returns the RibbonTab of this OrderPage
     *
     * @return the RibbonTab of this OrderPage
     */
    @Override
    public @NotNull RibbonTab getRibbonTab() {
        return this.orderRibbonTab;
    }

    /**
     * Initializes the ribbonTab and his controls
     */
    private void initializeRibbonTab() {
        this.orderRibbonTab = new RibbonTab("Orders");
        Button newOrderBtn = new ImageButton("new", ADD_ORDER_IMAGE, this::handleAddOrder);

        this.receivedOrderBtn = new ImageButton("received", ORDER_COMPLETED_IMAGE,
                actionEvent -> this.ordersManager.orderReceived(this.selectedOrder.getId()));
        this.receivedOrderBtn.setDisable(true);
        this.cancelOrderBtn = new ImageButton("cancel", ORDER_CANCEL_IMAGE,
                actionEvent -> this.handleCancelOrder());
        this.cancelOrderBtn.setDisable(true);
        RibbonGroup orderRibbonGroup = createRibbonGroup("order", newOrderBtn,
                this.receivedOrderBtn, this.cancelOrderBtn);

        this.receivedSparePartBtn = new ImageButton("received", RECEIVED_IMAGE,
                this::handleSparePartReceived);
        this.receivedSparePartBtn.setDisable(true);
        RibbonGroup sparePartRibbonGroup = createRibbonGroup("sparePart", this.receivedSparePartBtn);

        this.orderRibbonTab.getRibbonGroups().addAll(orderRibbonGroup, sparePartRibbonGroup);
    }

    /**
     * Handles the cancelOrder button
     */
    private void handleCancelOrder() {
        if (acceptedDeleteAlert()){
            this.ordersManager.cancelOrder(this.selectedOrder.getId());
        }
    }

    /**
     * Sets the specified orders to the order tableView
     *
     * @param orders orders which should be displayed
     */
    public void setOrders(@NotNull ObservableList<Order> orders) {
        this.ordersFilteredList = new FilteredList<>(orders);
        this.orderTblVw.setItems(this.ordersFilteredList);
        this.setSpareParts(Collections.emptySet());
    }

    /**
     * Sets the specified spare parts to the tableView and initializes the spare part searchbar
     *
     * @param spareParts spare parts which should be displayed
     */
    public void setSpareParts(@NotNull Set<SparePart> spareParts) {
        List<SparePart> sparePartList = new ArrayList<>();
        Set<String> sparePartsName = new TreeSet<>();
        for (SparePart sparePart : spareParts) {
            sparePartList.add(sparePart);
            sparePartsName.add(sparePart.getName());
        }

        this.sparePartsFilteredList =
                new FilteredList<>(FXCollections.observableList(sparePartList));
        this.sparePartTblVw.setItems(this.sparePartsFilteredList);
        TextFields.bindAutoCompletion(this.sparePartSearchbarTxtFld, sparePartsName);
    }

    /**
     * Cleans the sparePart search bar and displays the unfiltered list of spareParts
     */
    @FXML
    private void handleCleanOrderSearchBar() {
        this.orderSearchbarTxtFld.setText("");
        this.ordersFilteredList.setPredicate(order -> true);
        this.cleanOrderSearchBarBtn.setVisible(false);
    }

    /**
     * Cleans the sparePart search bar and displays the unfiltered list of spareParts
     */
    @FXML
    private void handleCleanSparePartSearchBar() {
        this.sparePartSearchbarTxtFld.setText("");
        this.sparePartsFilteredList.setPredicate(sparePart -> true);
        this.cleanSparePartSearchBarBtn.setVisible(false);
    }

    /**
     * Handles the "new" order button.
     *
     * @param actionEvent unused
     */
    private void handleAddOrder(ActionEvent actionEvent) {
        if (this.saleBook.getSuppliersManager().getSuppliers().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            StageUtils.styleStage(stage);
            alert.setContentText("Please add a supplier first");
            alert.showAndWait();
        } else {
            try {
                NewOrderController newOrderController = createNewOrderController(this.saleBook);
                newOrderController.getResult().ifPresent(order -> this.ordersManager.addOrder(order));
            } catch (IOException e) {
                DialogWindow.displayError("fail to load new order controller", e);
            }
        }
    }

    /**
     * Handles if a sparePart of the selected order is
     *
     * @param actionEvent unused
     */
    public void handleSparePartReceived(ActionEvent actionEvent) {
        this.ordersManager.sparePartReceived(this.selectedOrder.getId(),
                this.sparePartTblVw.getSelectionModel().getSelectedItem());
    }

    /**
     *
     */
    public void updateTableViewOrderAndDetail() {
        this.orderTblVw.refresh();
        this.sparePartTblVw.refresh();
        this.updateDetail();
    }

    /**
     * Updates the detail view of the selected spare part
     */
    private void updateDetail() {
        boolean isNull = this.selectedOrder == null;
        if (!isNull) {
            this.setSpareParts(this.selectedOrder.getSpareParts());
        }
        this.receivedOrderBtn.setDisable(this.selectedOrder == null
                || !this.selectedOrder.isReceivable());
        SparePart selectedSparePart = this.sparePartTblVw.getSelectionModel().getSelectedItem();
        this.receivedSparePartBtn.setDisable(selectedSparePart == null
                || !this.selectedOrder.isReceivable(selectedSparePart));
        this.cancelOrderBtn.setDisable(isNull || !this.selectedOrder.isCancellable());
    }
}
