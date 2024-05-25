package gui.saleBookController.pages.suppliersPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.ImageButton;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.suppliersPage.functions.EditSupplierController;
import gui.saleBookController.pages.suppliersPage.functions.NewSupplierController;
import gui.FXutils.RibbonTabUtils;
import utils.StringUtils;
import gui.FXutils.TableViewUtils;
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
import logic.Supplier;
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
import static gui.saleBookController.pages.suppliersPage.functions.NewSupplierController.createNewSupplierController;
import static gui.FXutils.RibbonGroupUtils.createRibbonGroup;

/**
 * This Page shows the supplier of the saleBook and handles the possible controls for it
 *
 * @author xthe_white_lionx
 * @see gui.saleBookController.pages.Page
 */
public class SuppliersPage implements Initializable, Page {

    /**
     * The base pane
     */
    @FXML
    private Pane basePane;

    /**
     * search bar to search a supplier by its name
     */
    @FXML
    public TextField nameSearchbar;

    /**
     * Button to clean the search bar
     */
    @FXML
    private Button cleanSearchBarBtn;

    /**
     * TableView to display the supplier of the saleBook
     */
    @FXML
    public TableView<Supplier> supplierTblVw;

    /**
     * VBox which wraps the search bar and the tableView
     */
    @FXML
    private VBox wrapVBox;

    /**
     * The current saleBook
     */
    private SaleBook saleBook;

    /**
     * The current selected Supplier
     */
    private Supplier selectedSupplier;

    /**
     * FilteredList of the supplier for easier filtering
     */
    private FilteredList<Supplier> suppliersFilteredList;

    /**
     * The ribbonTab of this SupplierPage with controls
     */
    private RibbonTab ribbonTab;

    /**
     * Button to delete the selected supplier
     */
    private Button deleteSupplierBtn;

    /**
     * Button to edit the selected supplier
     */
    private Button editSupplierBtn;


    /**
     * Create and loads a new SupplierPage
     *
     * @return the new created SupplierPage
     * @throws IOException if the fxml page cannot be loaded
     */
    public static @NotNull SuppliersPage createSuppliersPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/suppliersPage" +
                        "/SuppliersPage.fxml"));
        loader.load();
        return loader.getController();
    }


    @Override
    public @NotNull Pane getBasePane() {
        return this.basePane;
    }

    @Override
    public @NotNull RibbonTab getRibbonTab() {
        return this.ribbonTab;
    }

    @Override
    public void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
    }

    /**
     * Initializes this SuppliersPage.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeRibbonTab();
        this.initializeSearchBar();
        this.initializeTblVw();
    }

    /**
     * Sets the suppliers of this supplierPage as ObservableList for filtering
     *
     * @param suppliers observableList of the supplier which should be displayed
     */
    public void setSuppliers(@NotNull ObservableList<Supplier> suppliers) {
        this.suppliersFilteredList = new FilteredList<>(suppliers);
        this.supplierTblVw.setItems(this.suppliersFilteredList);
    }

    /**
     * Opens a new EditSupplierController with the selected supplier
     */
    @FXML
    public void handleEditSupplier() {
        if (this.selectedSupplier != null) {
            try {
                EditSupplierController editSupplierController =
                        EditSupplierController.createEditSupplierController(this.selectedSupplier,
                                this.saleBook.getSuppliersManager().getSupplierNames());
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
     * Handles the cleaning of the search bar and resets the filtering
     */
    @FXML
    public void handleCleanSearchBar() {
        this.nameSearchbar.setText("");
        this.cleanSearchBarBtn.setVisible(false);
    }

    /**
     * Handles the "Add" Button and opens a newSupplierController
     */
    @FXML
    public void handleAddSupplier() {
        try {
            NewSupplierController newSupplierController =
                    createNewSupplierController(this.saleBook.getSuppliersManager().getSupplierNames());
            newSupplierController.getResult().ifPresent(supplier -> this.saleBook.getSuppliersManager().addSupplier(supplier));
        } catch (IOException e) {
            displayError("failed to load newSupplierController", e);
        }
    }

    /**
     * Handles the "delete" Button and deletes the selected supplier
     */
    @FXML
    public void handleDeleteSupplier() {
        if (acceptedDeleteAlert()) {
            this.saleBook.getSuppliersManager().removeSupplier(this.selectedSupplier.getName());
        }
    }

    /**
     * Initializes the ribbonTab
     */
    private void initializeRibbonTab() {
        Button newSupplierBtn = new ImageButton("new", ADD_IMAGE,
                actionEvent -> this.handleAddSupplier());
        this.deleteSupplierBtn = new ImageButton("delete", DELETE_IMAGE, actionEvent -> this.handleDeleteSupplier());
        this.deleteSupplierBtn.setDisable(true);
        this.editSupplierBtn = new ImageButton("edit", EDIT_IMAGE, actionEvent -> this.handleEditSupplier());
        this.editSupplierBtn.setDisable(true);
        RibbonGroup organisationRibbonGroup = createRibbonGroup("organisation", newSupplierBtn,
                this.editSupplierBtn, this.deleteSupplierBtn);
        this.ribbonTab = RibbonTabUtils.createRibbonTab("Suppliers", organisationRibbonGroup);
    }

    /**
     * Returns a function from a supplier to a Hyperlink with the supplier's name as text and
     * the supplier's order webpage as link
     *
     * @return function from the supplier to a hyperlink
     */
    private Function<Supplier, Hyperlink> labelWebPage(){
        return supplier -> {
            Hyperlink link = new Hyperlink(supplier.getName());
            link.setOnAction(actionEvent -> {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(supplier.getOrderWebpage());
                    } catch (IOException e) {
                        displayError("can not open order webpage in browser", e);
                    }
                }
            });
            return link;
        };
    }

    /**
     * Initializes the Tableview
     */
    private void initializeTblVw() {
        TableViewUtils.addColumn(this.supplierTblVw, "name", this.labelWebPage());
        this.supplierTblVw.prefHeightProperty().bind(this.wrapVBox.heightProperty());
        this.supplierTblVw.getSelectionModel().selectedItemProperty().addListener(
                ((observableValue, oldSupplier, newSupplier) -> {
            this.selectedSupplier = newSupplier;
            boolean isNull = newSupplier == null;
            this.editSupplierBtn.setDisable(isNull);
            this.deleteSupplierBtn.setDisable(isNull);
        }));
    }

    /**
     * Initializes the search bar
     */
    private void initializeSearchBar() {
        this.nameSearchbar.textProperty().addListener((observable, oldText, newText) -> {
            if (newText.isEmpty()) {
                // Compare name of every supplier with filter text.
                this.suppliersFilteredList.setPredicate(supplier -> true);
                this.cleanSearchBarBtn.setVisible(false);
            } else {
                // If filter text is empty, display all supplier.
                this.suppliersFilteredList.setPredicate(supplier ->
                        StringUtils.containsIgnoreCase(supplier.getName(), newText));
                this.cleanSearchBarBtn.setVisible(true);
            }
        });
    }
}
