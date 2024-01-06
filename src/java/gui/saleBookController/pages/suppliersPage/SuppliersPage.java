package gui.saleBookController.pages.suppliersPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.ImageButton;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.suppliersPage.functions.EditSupplierController;
import gui.saleBookController.pages.suppliersPage.functions.NewSupplierController;
import gui.util.StringUtils;
import gui.util.TableViewUtils;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.order.Supplier;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import static gui.DialogWindow.acceptedDeleteAlert;
import static gui.DialogWindow.displayError;
import static gui.Images.*;
import static gui.saleBookController.pages.suppliersPage.functions.NewSupplierController.createSupplierController;
import static gui.util.RibbonGroupUtil.createRibbonGroup;

public class SuppliersPage implements Initializable, Page {

    @FXML
    private Pane borderPane;
    @FXML
    public TextField nameSearchbarTxtFld;
    @FXML
    private Button cleanSearchBarBtn;
    @FXML
    public TableView<Supplier> supplierTblVw;
    @FXML
    private VBox wrapVBox;

    /**
     * The current {@link SaleBook}
     */
    private SaleBook saleBook;
    /**
     *
     */
    private Supplier currentSupplier;
    private FilteredList<Supplier> suppliersFilteredList;
    private RibbonTab suppliersTab;
    private Button deleteSupplierBtn;
    private Button editSupplierBtn;


    /**
     * @return
     */
    public static @NotNull SuppliersPage createSuppliersPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/suppliersPage" +
                        "/SuppliersPage.fxml"));
        loader.load();
        return loader.getController();
    }

    /**
     * @return
     */
    @Override
    public @NotNull Pane getPane() {
        return this.borderPane;
    }

    @Override
    public @NotNull RibbonTab getRibbonTab() {
        return this.suppliersTab;
    }

    /**
     * @param saleBook
     */
    @Override
    public void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
        this.initializeSearchBar();
    }

    /**
     * Initializes the SuppliersPane.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeRibbonTab();
        this.initializeTblVw();
    }

    /**
     *
     * @param suppliers
     */
    public void setSuppliers(@NotNull ObservableList<Supplier> suppliers) {
        this.suppliersFilteredList = new FilteredList<>(suppliers);
        this.supplierTblVw.setItems(this.suppliersFilteredList);
    }

    /**
     *
     */
    private void initializeRibbonTab() {
        this.suppliersTab = new RibbonTab("Suppliers");
        Button newSupplierBtn = new ImageButton("new", ADD_IMAGE,
                actionEvent -> this.handleAddSupplier());
        this.deleteSupplierBtn = new ImageButton("delete", DELETE_IMAGE, actionEvent -> this.handleDeleteSupplier());
        this.deleteSupplierBtn.setDisable(true);
        this.editSupplierBtn = new ImageButton("edit", EDIT_IMAGE, actionEvent -> this.handleEditSupplier());
        this.editSupplierBtn.setDisable(true);
        RibbonGroup ribbonGroup = createRibbonGroup("organisation", newSupplierBtn,
                this.editSupplierBtn, this.deleteSupplierBtn);
        this.suppliersTab.getRibbonGroups().add(ribbonGroup);
    }

    private Function<Supplier, Hyperlink> labelWebPage(){
        return supplier -> {
            Hyperlink link = new Hyperlink(supplier.getName());
            link.setOnAction(actionEvent -> {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(supplier.getOrderWebpage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return link;
        };
    }

    /**
     * Initialize the investment tab, the Tableview and
     * sets the default values
     */
    private void initializeTblVw() {
        TableViewUtils.addColumn(this.supplierTblVw, "name", this.labelWebPage());
        this.supplierTblVw.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.supplierTblVw.prefHeightProperty().bind(this.wrapVBox.heightProperty());

        this.supplierTblVw.getSelectionModel().selectedItemProperty().addListener(((observableValue,
                                                                                    supplier, t1) -> {
            this.currentSupplier = t1;
            boolean isNull = t1 == null;
            this.editSupplierBtn.setDisable(isNull);
            this.deleteSupplierBtn.setDisable(isNull);
        }));
    }

    /**
     *
     */
    private void initializeSearchBar() {
        this.nameSearchbarTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Compare name of every investment with filter text.
                this.suppliersFilteredList.setPredicate(supplier -> true);
                this.cleanSearchBarBtn.setVisible(false);
            } else {
                // If filter text is empty, display all platforms.
                this.suppliersFilteredList.setPredicate(supplier ->
                        StringUtils.containsIgnoreCase(supplier.getName(), newValue));
                this.cleanSearchBarBtn.setVisible(true);
            }
        });
    }

    /**
     *
     */
    @FXML
    public void handleEditSupplier() {
        if (this.currentSupplier != null) {
            try {
                EditSupplierController editSupplierController =
                        EditSupplierController.createEditSupplierController(this.currentSupplier,
                        this.saleBook.getSupplierNames());
                editSupplierController.getResult().ifPresent(dirty -> {
                    if (dirty) {
                        this.supplierTblVw.refresh();
                    }
                });
            } catch (IOException e) {
                displayError("failed to load editSupplierController", e);
            }
        }
    }

    /**
     * Handles the cleaning of the searchbar and
     */
    @FXML
    private void handleCleanSearchBar() {
        this.nameSearchbarTxtFld.setText("");
        this.suppliersFilteredList.setPredicate(supplier -> true);
        this.cleanSearchBarBtn.setVisible(false);
    }

    /**
     * Handles the "Add" Button and hands over the value for
     * a new Platform.
     */
    @FXML
    private void handleAddSupplier() {
        try {
            NewSupplierController newSupplierController =
                    createSupplierController(this.saleBook.getSupplierNames());
            newSupplierController.getResult().ifPresent(supplier -> this.saleBook.addSupplier(supplier));
        } catch (IOException e) {
            displayError("failed to load newSupplierController", e);
        }
    }

    /**
     * Handles the "delete selected row" Button and
     * deletes the selected platform.
     */
    @FXML
    private void handleDeleteSupplier() {
        if (acceptedDeleteAlert()) {
            this.saleBook.removeSupplier(this.currentSupplier.getName());
        }
    }
}
