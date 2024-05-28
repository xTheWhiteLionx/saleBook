package gui.saleBookController.pages.positionsPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.CustomSplitMenuButton;
import gui.CustomSplitMenuButton.SplitMode;
import gui.FXutils.StageUtils;
import gui.ImageButton;
import gui.Images;
import gui.ObservableTreeItemMapBinder;
import gui.saleBookController.pages.FunctionDialog;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.positionsPage.functions.*;
import gui.saleBookController.pages.positionsPage.functions.add.NewCostController;
import gui.saleBookController.pages.positionsPage.functions.add.NewItemController;
import gui.saleBookController.pages.positionsPage.functions.add.MasterController;
import gui.FXutils.RibbonTabUtils;
import javafx.stage.Stage;
import logic.products.item.ItemColor;
import utils.StringUtils;
import gui.FXutils.TreeTableViewUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import logic.sparePart.SparePart;
import logic.products.position.State;
import logic.products.item.Item;
import logic.products.position.Position;
import logic.products.Product;
import logic.saleBook.SaleBook;
import org.controlsfx.control.textfield.CustomTextField;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

import static gui.DialogWindow.acceptedDeleteAlert;
import static gui.DialogWindow.displayError;
import static gui.Images.*;
import static gui.saleBookController.pages.positionsPage.DetailItemPane.createDetailItemPane;
import static gui.saleBookController.pages.positionsPage.DetailPositionPane.createDetailPositionPane;
import static gui.saleBookController.pages.positionsPage.functions.EditItemController.createEditItemController;
import static gui.saleBookController.pages.positionsPage.functions.EditPositionController.createEditPositionController;
import static gui.saleBookController.pages.positionsPage.functions.PerformanceCalculatorController.loadPerformanceCalculatorController;
import static gui.saleBookController.pages.positionsPage.functions.ReceivedController.createReceivedController;
import static gui.saleBookController.pages.positionsPage.functions.RepairedController.createRepairedController;
import static gui.saleBookController.pages.positionsPage.functions.SaleController.createSaleController;
import static gui.saleBookController.pages.positionsPage.functions.SellingPriceCalculatorController.createSellingPriceCalculatorController;
import static gui.saleBookController.pages.positionsPage.functions.add.NewCostController.createNewCostController;
import static gui.saleBookController.pages.positionsPage.functions.add.NewItemController.createAddItemController;
import static gui.FXutils.RibbonGroupUtils.createRibbonGroup;

/**
 * This class represents a PositionPage with his components.
 * The PositionPage displays the positions and their items.
 *
 * @see gui.saleBookController.pages.Page
 * @author xthe_white_lionx
 */
public class PositionsPage implements Initializable, Page {
    /**
     *
     */
    public static String DIR_POSITIONS = "positions";

    /**
     * Button to clean the searchbar
     */
    @FXML
    public Button cleanSearchBarBtn;

    /**
     * TextField to search the position by the id
     */
    @FXML
    public CustomTextField idSearchbarTxtFld;

    /**
     * TreeTableView withe position and their items
     */
    @FXML
    private TreeTableView<Product> trTblVw;

    /**
     * VBox which wraps the TreeTableView and the searchbar
     */
    @FXML
    private VBox wrapVBox;

    /**
     * Label to display the total performance of the positions
     */
    @FXML
    private Label totalPerformanceLbl;

    /**
     * ScrollPane to make the detailPanes scrollable
     */
    @FXML
    private ScrollPane detailScrllPn;

    /**
     * The base pane of this controller
     */
    @FXML
    private BorderPane borderPane;

    /**
     * Root of the TreeTableView
     */
    private ObservableTreeItemMapBinder<Integer> root;

    /**
     * SplitMenuButton for the selling price calculator
     */
    private CustomSplitMenuButton btnSellingPriceCalculator;

    /**
     * MenuItem of the SplitMenuButton {@link #btnSellingPriceCalculator}
     */
    private MenuItem btnPerformanceCalculator;

    /**
     * The current {@link SaleBook}
     */
    private SaleBook saleBook;

    /**
     * The current selected position
     */
    private Position currPos;

    /**
     * FilterPositionsController to filter the positions of the TreeTableView
     */
    private FilterPositionsController filterPositionsController;

    /**
     * RibbonTab of this page with his controls
     */
    private RibbonTab positionsTab;

    /**
     * Button to combine the current position with the specified ones
     */
    private Button combinePositionWithBtn;

    /**
     * Button to divide the current position
     */
    private Button dividePositionBtn;

    /**
     * Button to set the current position to the state received
     */
    private Button setReceivedBtn;

    /**
     * Button to add a cost the current position
     */
    private Button addCostBtn;

    /**
     * Button to divide the current position
     */
    private Button setRepairedBtn;

    /**
     * Button to add pictures to the current position
     */
    private Button addPictures;

    /**
     * Button to add an item to the current position
     */
    private Button addItemBtn;

    /**
     * Button to ship the current position
     */
    private Button toShipBtn;

    /**
     * Button to sale the current position
     */
    private Button saleBtn;

    /**
     * Button to edit the current selected position or item
     */
    private Button editBtn;

    /**
     * Button to delete the current selected position or item
     */
    private Button deleteBtn;

    /**
     * Pane for the details of the current selected position
     */
    private DetailPositionPane detailPositionPane;

    /**
     * Pane for the details of the current selected item
     */
    private DetailItemPane detailItemPane;

    /**
     * Creates a new positionPage
     *
     * @return a new positionPage
     */
    public static @NotNull PositionsPage createPositionsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/PositionsPage.fxml"));
        {
            loader.load();
            return loader.getController();
        }
    }

    /**
     * Initializes the PositionsPage.
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
     * Returns the TreeTableView of this PositionPage
     *
     * @return the TreeTableView of this PositionPage
     */
    public @NotNull TreeTableView<Product> getTrTblVw() {
        return this.trTblVw;
    }

    /**
     * Returns the totalPerformanceLbl of this PositionPage
     *
     * @return the totalPerformanceLbl of this PositionPage
     */
    public @NotNull Label getTotalPerformanceLbl() {
        return this.totalPerformanceLbl;
    }

    /**
     * Returns the base pane of this PositionPage
     *
     * @return the base pane of this PositionPage
     */
    @Override
    public @NotNull Pane getBasePane() {
        return this.borderPane;
    }

    /**
     * Returns the ribbonTab of this PositionPage
     *
     * @return the ribbonTab of this PositionPage
     */
    @Override
    public @NotNull RibbonTab getRibbonTab() {
        return this.positionsTab;
    }

    /**
     * Sets the saleBook of this PositionPage to the specified saleBook
     *
     * @param saleBook the specified saleBook which should be set
     */
    @Override
    public void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
        this.filterPositionsController.setCategories(saleBook.getCategories());
    }

    /**
     * Sets the root of this PositionPage and the root of the TreeTableView to the specified root
     *
     * @param root the specified root which should be set
     */
    public void setRoot(@NotNull ObservableTreeItemMapBinder<Integer> root) {
        this.root = root;
        this.trTblVw.setRoot(this.root);

        try {
            this.filterPositionsController =
                    FilterPositionsController.CreateFilterPositionsController(this.root);
        } catch (IOException e) {
            displayError("failed to load filterPositionsController", e);
        }
    }

    /**
     * Sets the categories of the filter to the specified categories
     *
     * @param categories the new categories which should be set
     */
    public void setCategories(@NotNull Collection<String> categories) {
        this.filterPositionsController.setCategories(categories);
    }

    /**
     * Updates the controls and details
     */
    public void updateControlsAndDetail() {
        this.updateControlsAndDetail(this.trTblVw.getSelectionModel().getSelectedItem().getValue());
    }

    /**
     * Handles the cleanSearchBar button and cleans the searchBar
     *
     * @param actionEvent not used
     */
    @FXML
    public void handleCleanSearchBar(ActionEvent actionEvent) {
        this.idSearchbarTxtFld.setText("");
        this.cleanSearchBarBtn.setVisible(false);
        this.filterPositionsController.handleApply();
    }

    /**
     * Initializes the ribbonTab with the controls
     */
    private void initializeRibbonTab() {
        Button newPositionBtn = new ImageButton("new position", ADD_IMAGE,
                actionEvent -> this.handleNewPosition());
        RibbonGroup ribbonGroupNew = createRibbonGroup("new", newPositionBtn);

        this.addItemBtn = new ImageButton("new item", ADD_IMAGE,
                actionEvent -> this.handleNewItem());
        this.addItemBtn.setDisable(true);

        this.dividePositionBtn = new ImageButton("divide", DIVIDE_IMAGE,
                actionEvent -> this.handleDivide());
        this.dividePositionBtn.setDisable(true);

        this.combinePositionWithBtn = new ImageButton("combine with...", COMBINE_IMAGE,
                actionEvent -> this.handleCombineWith());
        this.combinePositionWithBtn.setDisable(true);

        this.setReceivedBtn = new ImageButton("received", RECEIVED_IMAGE, actionEvent ->
                this.handleSetReceived());
        this.setReceivedBtn.setDisable(true);

        this.addCostBtn = new ImageButton("add cost", COST_IMAGE, actionEvent -> this.handleAddCost());
        this.addCostBtn.setDisable(true);

        this.setRepairedBtn = new ImageButton("repaired", REPAIRED_IMAGE, actionEvent ->
                this.handleRepair()
        );
        this.setRepairedBtn.setDisable(true);

        this.addPictures = new ImageButton("add pictures", ADD_PICTURE,
                actionEvent -> this.handleAddPictures());
        this.addPictures.setDisable(true);
        this.saleBtn = new ImageButton("sale", SALE_IMAGE,
                actionEvent1 -> this.handleSale());
        this.saleBtn.setDisable(true);

        this.toShipBtn = new ImageButton("to ship", SHIPPED_IMAGE,
                actionEvent -> {
                    try {
                        ToShipController.loadToShipController(this.currPos.getId(), this.saleBook);
                    } catch (IOException e) {
                        displayError("failed to load toShipController", e);
                    }
                });
        this.toShipBtn.setDisable(true);

        this.btnPerformanceCalculator = new MenuItem("performance calculator",
                createImageView(CALCULATOR_IMAGE, 16));
        this.btnPerformanceCalculator.setOnAction(actionEvent ->
                {
                    try {
                        loadPerformanceCalculatorController(this.currPos);
                    } catch (IOException e) {
                        displayError("failed to load performance calculator", e);
                    }
                }
        );

        this.btnSellingPriceCalculator = new CustomSplitMenuButton("selling price calculator",
                SplitMode.SPLIT_BOTTOM, this.btnPerformanceCalculator);
        this.btnSellingPriceCalculator.setGraphic(new ImageView(CALCULATOR_IMAGE));
        this.btnSellingPriceCalculator.setOnAction(actionEvent -> {
            try {
                createSellingPriceCalculatorController(this.currPos);
            } catch (IOException e) {
                displayError("failed to load sellingPriceCalculatorController", e);
            }
        });
        this.btnSellingPriceCalculator.setDisable(true);

        RibbonGroup ribbonGroupPositionFunctions = createRibbonGroup("position", this.addItemBtn,
                this.dividePositionBtn, this.combinePositionWithBtn, this.setReceivedBtn,
                this.addCostBtn, this.setRepairedBtn, this.addPictures, this.saleBtn, this.toShipBtn,
                this.btnSellingPriceCalculator);

        Button filterBtn = new ImageButton("filter", FILTER_IMAGE, actionEvent ->
                this.handleFilter());
        this.editBtn = new ImageButton("edit", EDIT_IMAGE, actionEvent -> this.handleEdit());
        this.editBtn.setDisable(true);
        this.deleteBtn = new ImageButton("delete", DELETE_IMAGE,
                actionEvent -> this.handleDelete());
        this.deleteBtn.setDisable(true);

        RibbonGroup ribbonGroupOrganisation = createRibbonGroup("organisation", filterBtn, this.editBtn,
                this.deleteBtn);

        this.positionsTab = RibbonTabUtils.createRibbonTab("Positions", ribbonGroupNew,
                ribbonGroupPositionFunctions, ribbonGroupOrganisation);
    }

    /**
     * Handles the add picture button and adds the pictures to the directory of the current
     * position
     */
    private void handleAddPictures() {
        try {
            AddPicturesController.loadAddPicturesController(this.currPos.getId(), this.saleBook);
        } catch (IOException e) {
            displayError("failed to load AddPicturesController", e);
        }
    }

    /**
     * Handles the filter button and shows the filterController
     */
    private void handleFilter() {
        this.filterPositionsController.showAndWait();
    }

    /**
     * Handles the divide button and divides the current position
     */
    private void handleDivide() {
        this.saleBook.getPositionsManager().dividePosition(this.currPos.getId());
    }

    /**
     * Handles the combine with button
     */
    private void handleCombineWith() {
        try {
            int currPosId = this.currPos.getId();
            CombinePositionWithController controller =
                    CombinePositionWithController.createCombinePositionWithController(
                            currPosId, this.saleBook.getPositionsManager().getPositions());
            controller.getResult().ifPresent(result ->
                    this.saleBook.getPositionsManager().combinePositions(currPosId, result));
        } catch (IOException e) {
            displayError("failed to load combinePositionWithController", e);
        }
    }

    /**
     * Handles the edit button and edits the selected position or item
     */
    private void handleEdit() {
        Product product = this.trTblVw.getSelectionModel().getSelectedItem().getValue();
        if (product != null) {
            FunctionDialog<Boolean> functionDialog = null;
            if (product instanceof Position position) {
                try {
                    functionDialog = createEditPositionController(position, this.saleBook.getCategories());
                } catch (IOException e) {
                    displayError("failed to load editPositionController", e);
                }
            } else if (product instanceof Item item) {
                try {
                    functionDialog = createEditItemController(item, ItemColor.getItemColorMap());
                } catch (IOException e) {
                    displayError("failed to load editItemController", e);
                }
            }
            if (functionDialog != null) {
                functionDialog.getResult().ifPresent(dirty -> {
                    if (dirty) {
                        this.updateControlsAndDetail(product);
                    }
                });
            }
        }
    }

    /**
     *
     *
     * @return
     */
    //TODO 07.01.2024 JavaDoc
    private Function<Product, Node> createIcon() {
        return product -> {
            if (product instanceof Position position) {
                if (position.getCategory().equals("Joy Con")) {
                    return Images.createImageView(new Image("gui/textures/positionIcons" +
                            "/nintendoSwitchIcon.png"), 20);
                }
                if (position.getCategory().equals("PlayStation 4")) {
                    return Images.createImageView(new Image("gui/textures/positionIcons/" +
                            "playStation4Icon.png"), 30);
                }
            } else if (product instanceof Item item) {
                Circle circle = new Circle(0, 0, 10);
                circle.setFill(item.getItemColor().getColor());
                return circle;
            }
            return null;
        };
    }

    /**
     * Initialize the investment tab, the Tableview and
     * sets the default values
     */
    private void initializeTblVw() {
        TreeTableViewUtils.addColumn(this.trTblVw, "icon", this.createIcon());
        TreeTableViewUtils.addColumn(this.trTblVw, "id", Product::getId);
        TreeTableViewUtils.addColumn(this.trTblVw, "type", Product::getSimpleName);
        TreeTableViewUtils.addColumn(this.trTblVw, "items", Product -> {
            if (Product instanceof Position position){
                return position.itemCount();
            }
            return "";
        });
        TreeTableViewUtils.addColumn(this.trTblVw, "state", Product -> {
            if (Product instanceof Position position){
                return position.getState();
            }
            return "";
        });

        this.trTblVw.prefHeightProperty().bind(this.wrapVBox.heightProperty());
        this.trTblVw.setRoot(new TreeItem<>());

        try {
            this.detailPositionPane = createDetailPositionPane();
        } catch (IOException e) {
            displayError("failed to load detailPositionPane", e);
        }
        this.trTblVw.getSelectionModel().selectedItemProperty().addListener((observableValue, productTreeItem, t1) -> {
            this.detailScrllPn.setContent(null);
            if (t1 != null) {
                Product product = t1.getValue();
                if (product != null) {
                    this.updateControlsAndDetail(product);
                }
            }
        });

        this.idSearchbarTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
            if (!newText.isEmpty()) {
                if(!newText.matches("[,.]") && StringUtils.isValidNumber(newText)){
                    this.root.setFilter(position -> String.valueOf(position.getId()).startsWith(newText));
                } else {
                    this.root.filterAll();
                }
                this.cleanSearchBarBtn.setVisible(true);
            } else {
                this.cleanSearchBarBtn.setVisible(false);
                this.filterPositionsController.handleApply();
            }
        });
    }

    /**
     * Handles the "add" Button and creates a new Position.
     */
    private void handleNewPosition() {
        try {
            MasterController masterController =
                    MasterController.createMasterController(this.saleBook.getPositionsManager().getNextPosId(),
                            this.saleBook.getCategories(), ItemColor.getItemColorMap());
            masterController.getResult().ifPresent(position -> {
                this.saleBook.getPositionsManager().addPosition(position);
                File dir = new File(DIR_POSITIONS);
                if (!dir.isDirectory()){
                    dir.mkdir();
                }
                File positionDir = new File(dir, String.valueOf(position.getId()));
                positionDir.mkdir();
                File picturesDir = new File(positionDir, "pictures");
                picturesDir.mkdir();
            });
        } catch (IOException e) {
            displayError("fail to load masterController", e);
        }
    }

    /**
     * Updates the controls and details depending on the specified product
     *
     * @param product the product on which the update will be done
     */
    private void updateControlsAndDetail(@NotNull Product product) {
        if (product instanceof Position position) {
            this.currPos = position;
            this.detailPositionPane.setPosition(position);
            this.detailScrllPn.setContent(this.detailPositionPane.getBasePane());

            State state = position.getState();
            this.btnSellingPriceCalculator.setDisable(false);
            this.dividePositionBtn.setDisable(!((state == State.RECEIVED || state == State.REPAIRED) &&
                    position.itemCount() > 1));
            this.combinePositionWithBtn.setDisable(!((state == State.RECEIVED || state == State.REPAIRED)));
            this.setReceivedBtn.setDisable(state != State.ORDERED);
            this.addCostBtn.setDisable(state.compareTo(State.RECEIVED) < 0);
            this.setRepairedBtn.setDisable(state != State.RECEIVED);
            this.addItemBtn.setDisable(state.compareTo(State.SOLD) >= 0);
            this.btnPerformanceCalculator.setDisable(false);
            this.btnSellingPriceCalculator.setDisable(false);
            this.addPictures.setDisable(false);
            this.saleBtn.setDisable(state != State.RECEIVED && state != State.REPAIRED);
            this.toShipBtn.setDisable(state != State.SOLD);
        } else if (product instanceof Item item) {
            this.currPos = null;
            try {
                this.detailItemPane = createDetailItemPane(item);
            } catch (IOException e) {
                displayError("failed to load detailItemPane", e);
            }
            this.detailScrllPn.setContent(this.detailItemPane.getBasePane());

            this.btnSellingPriceCalculator.setDisable(true);
            this.dividePositionBtn.setDisable(true);
            this.combinePositionWithBtn.setDisable(true);
            this.setReceivedBtn.setDisable(true);
            this.addCostBtn.setDisable(true);
            this.setRepairedBtn.setDisable(true);
            this.addItemBtn.setDisable(true);
            this.btnPerformanceCalculator.setDisable(true);
            this.btnSellingPriceCalculator.setDisable(true);
            this.saleBtn.setDisable(true);
            this.addPictures.setDisable(true);
            this.toShipBtn.setDisable(true);
        }
        this.editBtn.setDisable(false);
        this.deleteBtn.setDisable(false);
    }

    /**
     * Handles the set Received button and sets the current position to the state received
     */
    private void handleSetReceived() {
        try {
            ReceivedController receivedController =
                    createReceivedController(this.currPos.getOrderDate());
            receivedController.getResult().ifPresent(receivedDate ->
                    this.saleBook.getPositionsManager().setReceived(this.currPos.getId(), receivedDate)
            );
        } catch (IOException e) {
            displayError("failed to load ReceivedController fxml", e);
        }
    }

    /**
     * Handles the add cost button and adds the cost to the current position
     */
    private void handleAddCost() {
        try {
            NewCostController newCostController = createNewCostController();
            newCostController.getResult().ifPresent(value ->
                    this.saleBook.getPositionsManager().addCostToPosition(this.currPos.getId(), value));
        } catch (IOException e) {
            displayError("failed to load newCostController fxml", e);
        }
    }

    /**
     * Handles the new item button and adds the new item to the current position
     */
    private void handleNewItem() {
        try {
            NewItemController newItemController =
                    createAddItemController(this.currPos.getNextItemId(),
                            ItemColor.getItemColorMap());
            newItemController.getResult().ifPresent(item ->
                    this.saleBook.getPositionsManager().addItemToPosition(this.currPos.getId(), item));
        } catch (IOException e) {
            displayError("failed to load newCostItemController", e);
        }
    }

    /**
     * Handles the repaired button and repairs the current position
     */
    private void handleRepair() {
        try {
            Collection<SparePart> spareParts = this.saleBook.getSparePartsManager().getSpareParts();
            spareParts.removeIf(sparePart -> !sparePart.getCategory().equals(this.currPos.getCategory()));
            RepairedController repairedController =
                    createRepairedController(this.currPos.getCategory(),
                            this.saleBook.getSparePartsManager());
            repairedController.getResult().ifPresent(usedSpareParts ->
                    this.saleBook.getPositionsManager().repairPosition(this.currPos.getId(), usedSpareParts));
        } catch (IOException e) {
            displayError("failed to load repairedController", e);
        }
    }

    /**
     * Handles the delete Button and deletes the selected position or selected item.
     */
    private void handleDelete() {
        if (acceptedDeleteAlert()) {
            TreeItem<Product> selectedItem = this.trTblVw.getSelectionModel().getSelectedItem();
            Product value = selectedItem.getValue();
            if (value instanceof Position position) {
                this.saleBook.getPositionsManager().removePosition(position.getId());
            } else if (value instanceof Item item) {
                int positionId = selectedItem.getParent().getValue().getId();
                this.saleBook.getPositionsManager().removeItem(positionId, item.getId());
            }
        }
    }

    /**
     * Handles the sale Button and sales the selected position
     */
    private void handleSale() {
        boolean accepted = true;
        if (this.currPos.getState().compareTo(State.REPAIRED) < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            StageUtils.styleStage(stage);
            alert.setHeaderText("items of this position are still broken");
            alert.setContentText("make sure to mark them as broken before selling them");
            alert.getButtonTypes().add(ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            accepted = result.isPresent() && result.get() == ButtonType.OK;
        }
        if (accepted) {
            try {
                createSaleController(this.currPos, this.saleBook);
            } catch (IOException e) {
                displayError("failed to load saleController", e);
            }
        }
    }
}
