package gui.saleBookController.pages.positionsPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.CustomSplitMenuButton;
import gui.CustomSplitMenuButton.SplitMode;
import gui.ImageButton;
import gui.Images;
import gui.ObservableTreeItemMapBinder;
import gui.saleBookController.pages.FunctionDialog;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.positionsPage.functions.*;
import gui.saleBookController.pages.positionsPage.functions.add.NewCostController;
import gui.saleBookController.pages.positionsPage.functions.add.NewItemController;
import gui.saleBookController.pages.positionsPage.functions.add.MasterController;
import gui.util.StringUtils;
import gui.util.TreeColumnUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import logic.SparePart;
import logic.products.position.State;
import logic.products.Item;
import logic.products.position.Position;
import logic.products.Product;
import logic.saleBook.SaleBook;
import org.controlsfx.control.textfield.CustomTextField;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
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
import static gui.util.RibbonGroupUtil.createRibbonGroup;

public class PositionsPage implements Initializable, Page {

    public Button cleanSearchBarBtn;

    public CustomTextField idSearchbarTxtFld;
    //Tab Item
    @FXML
    private TreeTableView<Product> trTblVw;

    private ObservableTreeItemMapBinder<Integer> root;

    @FXML
    private VBox wrapVBoxInvestments;
    @FXML
    private Label totalPerformanceLbl;
    @FXML
    private ScrollPane detailPane;

    //function Buttons
    private CustomSplitMenuButton btnSellingPriceCalculator;
    private MenuItem btnPerformanceCalculator;
    /**
     * The current {@link SaleBook}
     */
    private SaleBook saleBook;

    /**
     * The current {@link SaleBook}
     */
    private Position currPos;

    private FilterPositionsController filterPositionsController;
    /**
     *
     */
    private BorderPane borderPane;

    private RibbonTab positionsTab;
    private Button combinePositionWithBtn;
    private Button dividePositionBtn;
    private Button setReceivedBtn;
    private Button addCostBtn;
    private Button setRepairedBtn;
    private ImageButton addItemBtn;
    private Button toShipBtn;
    private Button saleBtn;

    private Button editBtn;
    private Button deleteBtn;
    private DetailPositionPane detailPositionPane;
    private DetailItemPane detailItemPane;

    /**
     * @return
     */
    public static @NotNull PositionsPage createPositionsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/PositionsPage.fxml"));

        {
            BorderPane borderPane = loader.load();
            PositionsPage positionsPage = loader.getController();
            positionsPage.borderPane = borderPane;
            return positionsPage;
        }
    }

    /**
     * Initializes the PositionsPane.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeRibbonTab();
        this.initializeTblVw();
    }

    public @NotNull TreeTableView<Product> getTrTblVw() {
        return this.trTblVw;
    }

    /**
     * @return
     */
    public @NotNull Label getTotalPerformanceLbl() {
        return this.totalPerformanceLbl;
    }

    @Override
    public @NotNull Pane getPane() {
        return this.borderPane;
    }

    @Override
    public @NotNull RibbonTab getRibbonTab() {
        return this.positionsTab;
    }

    /**
     * @param saleBook
     */
    @Override
    public void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
        this.filterPositionsController.setCategories(saleBook.getCategories());
    }

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
     * @param categories
     */
    public void setCategories(@NotNull Collection<String> categories) {
        this.filterPositionsController.setCategories(categories);
    }

    /**
     *
     */
    private void initializeRibbonTab() {
        this.positionsTab = new RibbonTab("Positions");

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
                actionEvent -> this.handleCombine());
        this.combinePositionWithBtn.setDisable(true);

        this.setReceivedBtn = new ImageButton("received", RECEIVED_IMAGE, actionEvent ->
                this.handleReceived());
        this.setReceivedBtn.setDisable(true);

        this.addCostBtn = new ImageButton("add cost", COST_IMAGE, actionEvent -> this.handleAddCost());
        this.addCostBtn.setDisable(true);

        this.setRepairedBtn = new ImageButton("repaired", REPAIRED_IMAGE, actionEvent ->
                this.handleRepair()
        );
        this.setRepairedBtn.setDisable(true);

        this.saleBtn = new ImageButton("sale", SALE_IMAGE,
                actionEvent -> {
                    try {
                        createSaleController(this.currPos, this.saleBook);
                    } catch (IOException e) {
                        displayError("failed to load saleController", e);
                    }
                });
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
                createImageView(CALCULATER_IMAGE, 16));
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
        this.btnSellingPriceCalculator.setGraphic(new ImageView(CALCULATER_IMAGE));
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
                this.addCostBtn, this.setRepairedBtn, this.saleBtn, this.toShipBtn,
                this.btnSellingPriceCalculator);

        Button filterBtn = new ImageButton("filter", FILTER_IMAGE, actionEvent -> this.handleFilter());
        this.editBtn = new ImageButton("edit", EDIT_IMAGE, actionEvent -> this.handleEdit());
        this.editBtn.setDisable(true);
        this.deleteBtn = new ImageButton("delete", DELETE_IMAGE,
                actionEvent -> this.handleDelete());
        this.deleteBtn.setDisable(true);

        RibbonGroup ribbonGroupOrganisation = createRibbonGroup("organisation", filterBtn, this.editBtn,
                this.deleteBtn);

        this.positionsTab.getRibbonGroups().addAll(ribbonGroupNew, ribbonGroupPositionFunctions,
                ribbonGroupOrganisation);
    }

    private void handleFilter() {
        this.filterPositionsController.showAndWait();
    }

    /**
     *
     */
    private void handleDivide() {
        this.saleBook.dividePosition(this.currPos.getId());
    }

    /**
     *
     */
    private void handleCombine() {
        try {
            int currPosId = this.currPos.getId();
            CombinePositionWithController controller =
                    CombinePositionWithController.createCombinePositionWithController(
                            currPosId, this.saleBook.getPositions());
            controller.getResult().ifPresent(result ->
                    this.saleBook.combinePositions(currPosId, result));
        } catch (IOException e) {
            displayError("failed to load combinePositionWithController", e);
        }
    }

    /**
     *
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
                    functionDialog = createEditItemController(item, this.saleBook.getNameToItemColor());
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
     * @return
     */
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
//        //Creating a column
//        TreeTableColumn<Product,String> column = new TreeTableColumn<>("Column");
//        column.setPrefWidth(150);
//
//        //Defining cell content
//        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> p) ->
//                new ReadOnlyStringWrapper(p.getValue().getValue()));
//
//
//        // cell factory to display graphic:
//        column.setCellFactory(ttc -> new TreeTableCell<>() {
//            private final ImageView graphic = new ImageView("gui/textures/positionIcons/nintendoSwitchIcon.png");
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty ? null : item);
//                graphic.setFitHeight(25);
//                graphic.setFitWidth(25);
//                setGraphic(empty ? null : graphic);
//            }
//        });
//        trTblVw.getColumns().add(column);

        TreeColumnUtils.addColumn(this.trTblVw, "icon", this.createIcon());
        TreeColumnUtils.addColumn(this.trTblVw, "id", Product::getId);
        TreeColumnUtils.addColumn(this.trTblVw, "type", Product::getSimpleName);
        TreeColumnUtils.addColumn(this.trTblVw, "items", Product -> {
            if (Product instanceof Position position){
                return position.getItems().size();
            }
            return "";
        });
        TreeColumnUtils.addColumn(this.trTblVw, "state", Product -> {
            if (Product instanceof Position position){
                return position.getState();
            }
            return "";
        });

        this.trTblVw.prefHeightProperty().bind(this.wrapVBoxInvestments.heightProperty());
        this.trTblVw.setRoot(new TreeItem<>());

        try {
            this.detailPositionPane = createDetailPositionPane();
        } catch (IOException e) {
            displayError("failed to load detailPositionPane", e);
        }
        this.trTblVw.getSelectionModel().selectedItemProperty().addListener((observableValue, productTreeItem, t1) -> {
            this.detailPane.setContent(null);
            if (t1 != null) {
                Product product = t1.getValue();
                if (product != null) {
                    this.updateControlsAndDetail(product);
                }
            }
        });

        this.idSearchbarTxtFld.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if(!newValue.matches("[,.]") && StringUtils.isValidNumber(newValue)){
                    int searchedId = Integer.parseInt(newValue);
                    this.root.setFilter(position -> position.getId() == searchedId);
                } else {
                    this.root.setFilter(position -> false);
                }
                this.cleanSearchBarBtn.setVisible(true);
            } else {
                this.cleanSearchBarBtn.setVisible(false);
                this.filterPositionsController.handleApply();
            }
        });
    }

    /**
     * Handles the "add" Button and hands over the value for
     * a new Item.
     */
    private void handleNewPosition() {
        try {
            MasterController masterController =
                    MasterController.createMasterController(this.saleBook.getNextPosId(),
                            this.saleBook.getCategories(), this.saleBook.getNameToItemColor());
            masterController.getResult().ifPresent(position -> this.saleBook.addPosition(position));
        } catch (IOException e) {
            displayError("fail to load masterController", e);
        }
    }

    public void updateControlsAndDetail() {
        this.updateControlsAndDetail(this.trTblVw.getSelectionModel().getSelectedItem().getValue());
    }

    private void updateControlsAndDetail(@NotNull Product product) {
        if (product instanceof Position position) {
            this.currPos = position;
            this.detailPositionPane.setPosition(position);
            this.detailPane.setContent(this.detailPositionPane.getVbox());

            State state = position.getState();
            this.btnSellingPriceCalculator.setDisable(false);
            this.dividePositionBtn.setDisable(!((state == State.RECEIVED || state == State.REPAIRED) &&
                    position.getItems().size() > 1));
            this.combinePositionWithBtn.setDisable(!((state == State.RECEIVED || state == State.REPAIRED)));
            this.setReceivedBtn.setDisable(state != State.ORDERED);
            this.addCostBtn.setDisable(state.compareTo(State.RECEIVED) < 0);
            this.setRepairedBtn.setDisable(state != State.RECEIVED);
            this.addItemBtn.setDisable(state.compareTo(State.SOLD) >= 0);
            this.btnPerformanceCalculator.setDisable(false);
            this.btnSellingPriceCalculator.setDisable(false);
            this.saleBtn.setDisable(state != State.REPAIRED);
            this.toShipBtn.setDisable(state != State.SOLD);
        } else if (product instanceof Item item) {
            this.currPos = null;
            try {
                this.detailItemPane = createDetailItemPane(item);
            } catch (IOException e) {
                displayError("failed to load detailItemPane", e);
            }
            this.detailPane.setContent(this.detailItemPane.getVBox());

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
            this.toShipBtn.setDisable(true);
        }
        this.editBtn.setDisable(false);
        this.deleteBtn.setDisable(false);
    }


    private void handleReceived() {
        ReceivedController receivedController = null;
        try {
            receivedController = createReceivedController(this.currPos.getOrderDate());
            receivedController.getResult().ifPresent(receivedDate ->
                    this.saleBook.setReceived(this.currPos.getId(), receivedDate)
            );
        } catch (IOException e) {
            displayError("", e);
        }
    }

    private void handleAddCost() {
        try {
            NewCostController newCostController = createNewCostController();
            newCostController.getResult().ifPresent(value ->
                    this.saleBook.addCostToPosition(this.currPos.getId(), value));
        } catch (IOException e) {
            displayError("failed to load newCostController fxml", e);
        }
    }

    private void handleNewItem() {
        try {
            NewItemController newItemController =
                    createAddItemController(this.currPos.getNextItemId(),
                            this.saleBook.getNameToItemColor());
            newItemController.getResult().ifPresent(item ->
                    this.saleBook.addItemToPosition(this.currPos.getId(), item));
        } catch (IOException e) {
            displayError("failed to load newCostItemController", e);
        }
    }

    private void handleRepair() {
        RepairedController repairedController = null;
        try {
            Collection<SparePart> spareParts = this.saleBook.getSpareParts();
            spareParts.removeIf(sparePart -> !sparePart.getCategory().equals(this.currPos.getCategory()));
            repairedController = createRepairedController(spareParts);
            repairedController.getResult().ifPresent(usedSpareParts ->
                    this.saleBook.repairPosition(this.currPos.getId(), usedSpareParts));
        } catch (IOException e) {
            displayError("failed to load repairedController", e);
        }
    }

    /**
     * Handles the "delete selected row" Button and
     * deletes the selected investment.
     */
    private void handleDelete() {
        if (acceptedDeleteAlert()) {
            TreeItem<Product> selectedItem = this.trTblVw.getSelectionModel().getSelectedItem();
            Product value = selectedItem.getValue();
            if (value instanceof Position position) {
                this.saleBook.removePosition(position.getId());
            } else if (value instanceof Item item) {
                int positionId = selectedItem.getParent().getValue().getId();
                this.saleBook.removeItem(positionId, item.getId());
            }
        }
    }

    public void handleCleanSearchBar(ActionEvent actionEvent) {
        this.idSearchbarTxtFld.setText("");
        this.cleanSearchBarBtn.setVisible(false);
        this.filterPositionsController.handleApply();
    }
}
