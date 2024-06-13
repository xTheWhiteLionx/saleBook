package gui.saleBookController.pages.sparePartsPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.FXutils.TextInputControlUtils;
import costumeClasses.FXClasses.ImageButton;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.sparePartsPage.functions.NewSparePartController;
import gui.saleBookController.pages.sparePartsPage.functions.EditSparePartController;
import gui.FXutils.RibbonTabUtils;
import javafx.scene.image.Image;
import logic.manager.SparePartsManager;
import utils.StringUtils;
import gui.FXutils.TableViewUtils;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.sparePart.SparePart;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.*;
import static gui.Images.*;
import static gui.saleBookController.pages.sparePartsPage.functions.NewSparePartController.createSparePartController;
import static gui.saleBookController.pages.sparePartsPage.functions.EditSparePartController.createEditSparePartController;
import static gui.FXutils.RibbonGroupUtils.createRibbonGroup;

/**
 * This Page shows the spareParts of the saleBook and handles the possible controls for it
 *
 * @author xthe_white_lionx
 * @see gui.saleBookController.pages.Page
 */
public class SparePartsPage implements Initializable, Page {

    /**
     * The base pane
     */
    @FXML
    private Pane basePane;

    /**
     * Label to display the category of the current spare part
     */
    @FXML
    public Label categoryLbl;

    /**
     * Label to display the name of the current spare part
     */
    @FXML
    public Label nameLbl;

    /**
     * Label to display the condition of the current spare part
     */
    @FXML
    public Label conditionLbl;

    @FXML
    public Label minimumStockLbl;

    @FXML
    public Label stockLbl;

    /**
     * Label to display the unit of the current spare part 
     */
    @FXML
    public Label unitLbl;

    /**
     * TextField to search a spare part by name
     */
    @FXML
    public TextField searchBar;

    /**
     * Button to clean the {@link #searchBar}
     */
    @FXML
    private Button cleanSearchBarBtn;

    /**
     * VBox to wrap the {@link #searchBar} and the {@link #sparePartTblVw}
     */
    @FXML
    private VBox wrapVBox;
    
    /**
     * TableView to display all spare parts of the saleBook
     */
    @FXML
    private TableView<SparePart> sparePartTblVw;

    /**
     * FilteredList item of the {@link #sparePartTblVw} to make the tableView filterable
     */
    private FilteredList<SparePart> sparePartFilteredList;

    /**
     * The saleBook to which operate on
     */
    private SaleBook saleBook;

    /**
     * The current selected spare part
     */
    private SparePart selectedSparePart;

    /**
     * The ribbonTab of this SparePartPage
     */
    private RibbonTab ribbonTab;

    /**
     * Button to delete the selected spare part
     */
    private Button deleteSparePartBtn;

    /**
     * Button to edit the selected spare part
     */
    private Button editBtn;

    private SparePartsManager sparePartsManager;

    /**
     * Creates and loads a new SparePartsPage
     *
     * @return the new created SparePartPage
     * @throws IOException if the fxml could not be loaded
     */
    public static @NotNull SparePartsPage createSparePartsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/sparePartsPage/SparePartsPage.fxml"));

        loader.load();
        return loader.getController();
    }

    /**
     * Sets the spare parts of this SparePartsPage as ObservableList for filtering
     *
     * @param sparePartObservableList the observableList of spare parts which should be displayed
     */
    public void setSpareParts(@NotNull ObservableList<SparePart> sparePartObservableList) {
        this.sparePartFilteredList = new FilteredList<>(sparePartObservableList);
        this.sparePartTblVw.setItems(this.sparePartFilteredList);
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
        this.sparePartsManager = this.saleBook.getSparePartsManager();
    }

    /**
     * Initializes this SparePartsPage.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeRibbonTab();
        this.initializeTblVw();
        this.initializeSearchBar();
        this.detailsToDefault();
    }


    /**
     * Handles the press of the edit button
     */
    @FXML
    private void handleEditSparePart() {
        if (this.selectedSparePart != null) {
            try {
                EditSparePartController editSparePartController =
                        createEditSparePartController(this.sparePartsManager,
                                this.selectedSparePart,
                                this.saleBook.getCategories());
                editSparePartController.getResult().ifPresent(dirty -> {
                    if (dirty) {
                        this.updateTableViewAndDetail();
                    }
                });
            } catch (IOException e) {
                displayError("failed to load editSparePartController", e);
            }
        }
    }

    /**
     * Handles the cleaning of the searchbar and set it invisible
     */
    @FXML
    public void handleCleanSearchBar() {
        this.searchBar.setText("");
        this.cleanSearchBarBtn.setVisible(false);
    }

    /**
     * Handles the "new" Button and opens a new newSparePartController
     */
    @FXML
    public void handleNewSparePart() {
        try {
            SparePartsManager sparePartsManager = this.sparePartsManager;
            NewSparePartController newSparePartController =
                    createSparePartController(sparePartsManager.getSparePartNames(),
                            sparePartsManager.getSparePartUnits(),
                            this.saleBook.getCategories());
            newSparePartController.getResult().ifPresent(sparePartsManager::addSparePart);
        } catch (IOException e) {
            displayError("failed to load newSparePartController", e);
        }
    }

    /**
     * Handles the "delete" Button and deletes the selected spare part.
     */
    @FXML
    public void handleDeleteSparePart() {
        if (acceptedDeleteAlert()) {
            this.sparePartsManager.removeSparePart(this.selectedSparePart);
        }
    }

    /**
     * Updates the sparePartTblVw and the detail view of the spare part
     */
    public void updateTableViewAndDetail() {
        this.updateDetail();
        this.sparePartTblVw.refresh();
    }

    /**
     * Initializes the RibbonTab
     */
    private void initializeRibbonTab() {
        Button newSparePartBtn = new ImageButton("new", ADD_IMAGE,
                actionEvent -> this.handleNewSparePart());

        this.deleteSparePartBtn = new ImageButton("delete", DELETE_IMAGE,
                actionEvent -> this.handleDeleteSparePart());
        this.deleteSparePartBtn.setDisable(true);
        this.editBtn = new ImageButton("edit", EDIT_IMAGE,
                actionEvent -> this.handleEditSparePart());
        this.editBtn.setDisable(true);

        RibbonGroup ribbonGroup = createRibbonGroup("organisation", newSparePartBtn,
                this.editBtn, this.deleteSparePartBtn);
        this.ribbonTab = RibbonTabUtils.createRibbonTab("Spare Parts", ribbonGroup);
    }

    /**
     * Updates the detail view of the selected spare part
     */
    private void updateDetail() {
        if (this.selectedSparePart != null) {
            this.categoryLbl.setText(this.selectedSparePart.getCategory());
            this.nameLbl.setText(this.selectedSparePart.getName());
            this.conditionLbl.setText(this.selectedSparePart.getCondition().name());
            this.unitLbl.setText(this.selectedSparePart.getUnit());
            Integer minimumStock = this.selectedSparePart.getMinimumStock();
            this.minimumStockLbl.setText(String.valueOf(minimumStock));
            this.stockLbl.setText(String.valueOf(this.sparePartsManager.getQuantity(this.selectedSparePart
            )));
        } else {
            this.detailsToDefault();
        }
    }

    /**
     * Initializes sparePartTblVw
     */
    private void initializeTblVw() {
        TableViewUtils.addColumn(this.sparePartTblVw, "", (sparePart) -> {
            int minimumStock = sparePart.getMinimumStock();
            if (minimumStock > 0) {
                Integer quantity = this.sparePartsManager.getQuantity(sparePart);
                if (quantity != null && quantity < minimumStock) {
                    Image image = WARNING_IMAGE;
                    String toolTipText = "below minimum stock";
                    if (quantity == 0) {
                        image = ERROR_IMAGE;
                        toolTipText = "out of stock";
                    }
                    ImageView imageView = createImageView(image,20);
                    imageView.setPickOnBounds(true);
                    Tooltip tooltip = new Tooltip(toolTipText);
                    VBox vBox = new VBox(imageView);
                    Tooltip.install(vBox, tooltip);
                    return vBox;
                }
            } else {
                return null;
            }
            return null;
        });
        TableViewUtils.addColumn(this.sparePartTblVw, "category", SparePart::getCategory);
        TableViewUtils.addColumn(this.sparePartTblVw, "name", SparePart::getName);
        TableViewUtils.addColumn(this.sparePartTblVw, "condition",
                sparePart -> sparePart.getCondition().name());
        TableViewUtils.addColumn(this.sparePartTblVw, "in stock",
                sparePart -> this.sparePartsManager.getQuantity(sparePart) + " " + sparePart.getUnit());
        TableViewUtils.addColumn(this.sparePartTblVw, "min stock",
                sparePart -> sparePart.getMinimumStock() + " " + sparePart.getUnit());

        this.sparePartTblVw.prefHeightProperty().bind(this.wrapVBox.heightProperty());

        this.sparePartTblVw.getSelectionModel().selectedItemProperty().addListener(
                ((observableValue, oldSparePart, newSparePart) -> {
            this.selectedSparePart = newSparePart;
            this.updateDetail();
            boolean isNull = newSparePart == null;
            this.editBtn.setDisable(isNull);
            this.deleteSparePartBtn.setDisable(isNull);
        }));
    }

    /**
     * Initializes the search bar
     */
    private void initializeSearchBar() {
        TextInputControlUtils.installTouch(this.searchBar);
        this.searchBar.textProperty().addListener((observableValue, oldText, newText) -> {
            if (newText.isEmpty()) {
                this.cleanSearchBarBtn.setVisible(false);
                this.sparePartFilteredList.setPredicate(sparePart -> true);
            } else {
                this.cleanSearchBarBtn.setVisible(true);
                this.sparePartFilteredList.setPredicate(sparePart ->
                        StringUtils.containsIgnoreCase(sparePart.getName(), newText));
            }
        });
    }

    /**
     * Sets the detail to the default values
     */
    private void detailsToDefault() {
        this.nameLbl.setText("");
        this.conditionLbl.setText("");
        this.unitLbl.setText("");
        this.categoryLbl.setText("");
        this.minimumStockLbl.setText("");
        this.stockLbl.setText("");
    }
}
