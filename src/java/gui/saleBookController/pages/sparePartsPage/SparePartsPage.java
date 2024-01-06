package gui.saleBookController.pages.sparePartsPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.ImageButton;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.sparePartsPage.functions.NewSparePartController;
import gui.saleBookController.pages.sparePartsPage.functions.EditSparePartController;
import gui.util.SpinnerUtils;
import gui.util.StringUtils;
import gui.util.TableViewUtils;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.SparePart;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import static gui.DialogWindow.*;
import static gui.Images.*;
import static gui.saleBookController.pages.sparePartsPage.functions.NewSparePartController.createSparePartController;
import static gui.saleBookController.pages.sparePartsPage.functions.EditSparePartController.createEditSparePartController;
import static gui.util.RibbonGroupUtil.createRibbonGroup;

/**
 *
 */
public class SparePartsPage implements Initializable, Page {
    /**
     *
     */
    @FXML
    private Pane borderPane;
    /**
     *
     */
    @FXML
    public Label nameLbl;
    @FXML
    public Label conditionLbl;
    @FXML
    public Spinner<Integer> quantitySpinner;
    @FXML
    public Label amountUnitLbl;
    @FXML
    public Label categoryLbl;
    @FXML
    public TextField nameSearchbarTxtFld;
    @FXML
    private Button platformCleanSearchBar;
    @FXML
    private TableView<SparePart> sparePartTblVw;
    @FXML
    private VBox wrapVBox;

    private FilteredList<SparePart> sparePartFilteredList;

    /**
     * The current {@link SaleBook}
     */
    private SaleBook saleBook;
    /**
     *
     */
    private SparePart currentSparePart;
    private RibbonTab sparePartsTab;
    private Button deleteSparePartBtn;
    private Button editBtn;


    /**
     * @return
     */
    public static @NotNull SparePartsPage createSparePartsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/sparePartsPage/SparePartsPage.fxml"));

        loader.load();
        return loader.getController();
    }

    /**
     * @param sparePartObservableList
     */
    public void setSpareParts(@NotNull ObservableList<SparePart> sparePartObservableList) {
        this.sparePartFilteredList = new FilteredList<>(sparePartObservableList);
        this.sparePartTblVw.setItems(this.sparePartFilteredList);
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
        return this.sparePartsTab;
    }

    /**
     * @param saleBook
     */
    @Override
    public void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
    }

    /**
     * Initializes the SparePartsPane.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeRibbonTab();
        this.initializeTblVw();
        this.detailToDefault();
        this.initializeSearchBar();
        this.quantitySpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
    }

    /**
     *
     */
    private void initializeRibbonTab() {
        this.sparePartsTab = new RibbonTab("Spare Parts");
        Button newSparePartBtn = new ImageButton("new", ADD_IMAGE,
                actionEvent -> this.handleNewSparePart());

        this.deleteSparePartBtn = new ImageButton("delete", DELETE_IMAGE, actionEvent -> this.handleDeleteSparePart());
        this.deleteSparePartBtn.setDisable(true);
        this.editBtn = new ImageButton("edit", EDIT_IMAGE, actionEvent -> this.handleEditSparePart());
        this.editBtn.setDisable(true);

        RibbonGroup ribbonGroup = createRibbonGroup("organisation", newSparePartBtn,
                this.editBtn, this.deleteSparePartBtn);
        this.sparePartsTab.getRibbonGroups().add(ribbonGroup);
    }


    /**
     * @param f
     * @return
     */
    private Function<SparePart, String> formatUnit(Function<SparePart, Number> f) {
        return s -> f.apply(s) + " " + s.getUnit();
    }

    /**
     *
     */
    private void updateDetail() {
        if (this.currentSparePart != null) {
            this.nameLbl.setText(this.currentSparePart.getName());
            this.conditionLbl.setText(this.currentSparePart.getCondition().name());
            this.quantitySpinner.setValueFactory(
                    SpinnerUtils.createValueFactory(this.currentSparePart.getQuantity()));
            this.amountUnitLbl.setText(this.currentSparePart.getUnit());
            this.categoryLbl.setText(this.currentSparePart.getCategory());
        } else {
            this.detailToDefault();
        }
    }

    /**
     * Initialize the investment tab, the Tableview and
     * sets the default values
     */
    private void initializeTblVw() {
        TableViewUtils.addColumn(this.sparePartTblVw, "", (sparePart -> {
            if (sparePart.getQuantity() == 0) {
                ImageView imageView = createImageView(WARNING_IMAGE, 20);
                imageView.setPickOnBounds(true);
                Tooltip tooltip = new Tooltip("out of stock");
                VBox vBox = new VBox(imageView);
                Tooltip.install(vBox, tooltip);
                return vBox;
            } else {
                return null;
            }
        }));
        TableViewUtils.addColumn(this.sparePartTblVw, "name", SparePart::getName);
        TableViewUtils.addColumn(this.sparePartTblVw, "condition",
                sparePart -> sparePart.getCondition().name());
        TableViewUtils.addColumn(this.sparePartTblVw, "in stock", this.formatUnit(SparePart::getQuantity));
        TableViewUtils.addColumn(this.sparePartTblVw, "for", SparePart::getCategory);

        this.sparePartTblVw.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        this.sparePartTblVw.prefHeightProperty().bind(this.wrapVBox.heightProperty());

        this.sparePartTblVw.getSelectionModel().selectedItemProperty().addListener(((observableValue,
                                                                                     sparePart, t1) -> {
            this.currentSparePart = t1;
            this.updateDetail();
            boolean isNull = t1 == null;
            this.quantitySpinner.setDisable(isNull);
            this.editBtn.setDisable(isNull);
            this.deleteSparePartBtn.setDisable(isNull);
        }));

        this.quantitySpinner.valueProperty().addListener((observableValue, integer, t1) -> {
            if (this.currentSparePart != null) {
                this.currentSparePart.setQuantity(t1);
            }
        });
    }

    /**
     *
     */
    private void initializeSearchBar() {
        this.nameSearchbarTxtFld.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                this.platformCleanSearchBar.setVisible(false);
                this.sparePartFilteredList.setPredicate(sparePart -> true);
            } else {
                this.platformCleanSearchBar.setVisible(true);
                this.sparePartFilteredList.setPredicate(sparePart ->
                        StringUtils.containsIgnoreCase(sparePart.getName(), newValue));
            }
        });
    }

    /**
     *
     */
    private void detailToDefault() {
        this.nameLbl.setText("");
        this.conditionLbl.setText("");
        this.quantitySpinner.setDisable(true);
        this.amountUnitLbl.setText("");
        this.categoryLbl.setText("");
        this.quantitySpinner.setValueFactory(SpinnerUtils.createValueFactory(0));
//        SpinnerUtils.setNewValueFactory(this.quantitySpinner, 0);
    }

    /**
     * Handles the cleaning of the searchbar and
     */
    @FXML
    private void handleCleanSearchBar() {
        this.nameSearchbarTxtFld.setText("");
        this.platformCleanSearchBar.setVisible(false);
    }

    /**
     * Handles the "Add" Button and hands over the value for
     * a new Platform.
     */
    @FXML
    private void handleNewSparePart() {
        try {
            NewSparePartController newSparePartController =
                    createSparePartController(this.saleBook.getSparePartNames(),
                            this.saleBook.getSparePartUnits(), this.saleBook.getCategories());
            newSparePartController.getResult().ifPresent(sparePart ->
                    this.saleBook.addSparePart(sparePart));
        } catch (IOException e) {
            displayError("failed to load newSparePartController", e);
        }
    }

    /**
     *
     */
    @FXML
    public void handleEditSparePart() {
        if (this.currentSparePart != null) {
            try {
                EditSparePartController editSparePartController =
                        createEditSparePartController(this.currentSparePart, this.saleBook.getCategories());
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

    public void updateTableViewAndDetail() {
        this.updateDetail();
        this.sparePartTblVw.refresh();
    }

    /**
     * Handles the "delete selected row" Button and
     * deletes the selected platform.
     */
    @FXML
    private void handleDeleteSparePart() {
        if (acceptedDeleteAlert()) {
            this.saleBook.removeSparePart(this.currentSparePart);
        }
    }
}
