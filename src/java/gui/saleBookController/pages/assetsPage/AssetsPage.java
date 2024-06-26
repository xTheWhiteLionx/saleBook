package gui.saleBookController.pages.assetsPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.DialogWindow;
import gui.FXutils.LabelUtils;
import gui.FXutils.TextInputControlUtils;
import costumeClasses.FXClasses.ImageButton;
import gui.JavaFXGUI;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.assetsPage.functions.EditAssetController;
import gui.saleBookController.pages.assetsPage.functions.NewAssetController;
import gui.FXutils.StageUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import utils.StringUtils;
import gui.FXutils.TableViewUtils;
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
import logic.Asset;
import logic.saleBook.SaleBook;
import utils.LocalDateUtils;
import org.controlsfx.control.textfield.CustomTextField;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static gui.DialogWindow.acceptedDeleteAlert;
import static gui.Images.*;
import static gui.saleBookController.pages.assetsPage.functions.NewAssetController.createAssetController;
import static gui.FXutils.RibbonGroupUtils.createRibbonGroup;

/**
 * This class displays the assets of the saleBook and has some controls to interact with.
 *
 * @author xThe_white_Lionx
 */
public class AssetsPage implements Initializable, Page {
    /**
     * The main pane
     */
    @FXML
    private Pane pane;

    /**
     * Label to display the sum value of all assets
     */
    @FXML
    public Label sumValueLbl;

    /**
     * TableView to display the assets
     */
    @FXML
    private TableView<Asset> assetTblVw;


    /**
     * Searchbar TextField to search a specific assets by id
     */
    @FXML
    private CustomTextField assetSearchbarTxtFld;

    /**
     * Button to clean the sparePart searchbar
     */
    @FXML
    private Button cleanAssetSearchBarBtn;

    /**
     * FilteredList of the assets
     */
    private FilteredList<Asset> assetsFilteredList;

    /**
     * RibbonTab with controls
     */
    private RibbonTab assetRibbonTab;

    /**
     * Button to set the selected asset to receive
     */
    private ImageButton receivedAssetBtn;

    /**
     * Button to edit the selected asset
     */
    private ImageButton editBtn;

    /**
     * Button to delete the selected asset
     */
    private ImageButton deleteAssetBtn;
    /**
     * The current saleBook
     */
    private SaleBook saleBook;
    /**
     * The selected asset
     */
    private Asset currAsset;

    /**
     * Creates and returns a new AssetPage
     *
     * @return a new AssetPage
     * @throws IOException
     */
    public static @NotNull AssetsPage createAssetsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/assetsPage" +
                        "/AssetsPage.fxml"));
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

        TableViewUtils.addColumn(this.assetTblVw, "id", Asset::getId);
        TableViewUtils.addColumn(this.assetTblVw, "name", Asset::getName);
        TableViewUtils.addColumn(this.assetTblVw, "supplier", asset ->
                asset.getSupplier().getName());
        TableViewUtils.addColumn(this.assetTblVw, "purchasing date", asset ->
                LocalDateUtils.format(asset.getPurchasingDate()));
        TableViewUtils.addColumn(this.assetTblVw, "arrival date", asset -> {
            LocalDate arrivalDate = asset.getArrivalDate();
            if (arrivalDate != null) {
                return LocalDateUtils.format(arrivalDate);
            }
            return null;
        });
        TableViewUtils.addColumn(this.assetTblVw, "value", asset ->
                JavaFXGUI.formatMoney(asset.getValue()));
        TextInputControlUtils.installTouch(this.assetSearchbarTxtFld);
        this.assetSearchbarTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
            if (newText.isEmpty()) {
                this.assetsFilteredList.setPredicate(asset -> true);
                this.cleanAssetSearchBarBtn.setVisible(false);
            } else {
                if (!newText.matches("[,.]")){
                    //filters the assets by id or name
                    this.assetsFilteredList.setPredicate(asset ->
                            StringUtils.containsIgnoreCase(String.valueOf(asset.getId()), newText)
                                    || StringUtils.containsIgnoreCase(asset.getName(), newText));
                } else {
                    this.assetsFilteredList.setPredicate(asset -> false);
                }
                this.cleanAssetSearchBarBtn.setVisible(true);
            }
        });
        this.assetTblVw.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldAsset, newAsset) -> {
                    boolean isNull = newAsset == null;
                    if (!isNull) {
                        this.currAsset = newAsset;
                    }
                    this.editBtn.setDisable(isNull);
                    this.receivedAssetBtn.setDisable(isNull || newAsset.isReceived());
                    this.deleteAssetBtn.setDisable(isNull);
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
        return this.assetRibbonTab;
    }

    /**
     * Initializes the ribbonTab and his controls
     */
    private void initializeRibbonTab() {
        this.assetRibbonTab = new RibbonTab("Assets");
        Button newAssetBtn = new ImageButton("new", NEW_ASSET_IMAGE, this::handleAddAsset);
        this.editBtn = new ImageButton("edit", EDIT_IMAGE, this::handleEditAsset);
        this.editBtn.disable();
        this.receivedAssetBtn = new ImageButton("received", RECEIVED_IMAGE,
                this::receiveAsset);
        this.receivedAssetBtn.disable();
        this.deleteAssetBtn = new ImageButton("delete", DELETE_IMAGE, this::handleDeleteAsset);
        this.deleteAssetBtn.disable();
        RibbonGroup organisationRibbonGroup = createRibbonGroup("organisation", newAssetBtn,
                this.editBtn, this.receivedAssetBtn, this.deleteAssetBtn);

        this.assetRibbonTab.getRibbonGroups().addAll(organisationRibbonGroup);
    }

    /**
     *
     * @param event
     */
    private void receiveAsset(ActionEvent event) {
        this.currAsset.setArrivalDate(LocalDate.now());
    }

    /**
     *
     *
     * @param event unused
     */
    //TODO 28.05.2024 JavaDoc
    private void handleEditAsset(ActionEvent event) {
        try {
            EditAssetController editAssetController =
                    EditAssetController.createEditAssetController(this.currAsset, this.saleBook);
            editAssetController.getResult().ifPresent(dirty -> {
                if (dirty) {
                    this.saleBook.updateStatus(
                            "asset with id %d updated".formatted(this.currAsset.getId()));
                }
            });
        } catch (IOException e) {
            DialogWindow.displayError("fail to load edit asset controller", e);
        }
    }

    /**
     *
     * @param assets
     */
    public void setAssets(@NotNull ObservableList<Asset> assets) {
        this.assetsFilteredList = new FilteredList<>(assets);
        this.assetTblVw.setItems(this.assetsFilteredList);
    }

    /**
     * Sets the text of the sumValueLbl to the specified sumValue
     *
     * @param sumValue the new sumValue
     */
    public void setSumAssetsValue(BigDecimal sumValue) {
        LabelUtils.setMoneyAndColor(this.sumValueLbl, sumValue);
    }

    /**
     * Cleans the sparePart search bar and displays the unfiltered list of spareParts
     */
    @FXML
    private void handleCleanAssetSearchBar() {
        this.assetSearchbarTxtFld.setText("");
        this.assetsFilteredList.setPredicate(asset -> true);
        this.cleanAssetSearchBarBtn.setVisible(false);
    }

    /**
     * Handles the cancelOrder button
     *
     * @param event unused
     */
    private void handleDeleteAsset(ActionEvent event) {
        if (acceptedDeleteAlert()){
            this.saleBook.getAssetsManager().removeAsset(this.currAsset.getId());
        }
    }

    /**
     * Handles the "new" order button.
     */
    private void handleAddAsset(ActionEvent event) {
        if (this.saleBook.getSuppliersManager().getSuppliers().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            StageUtils.styleStage(stage);
            alert.setContentText("Please add a supplier first");
            alert.showAndWait();
        } else {
            try {
                NewAssetController newAssetController = createAssetController(this.saleBook);
                newAssetController.getResult().ifPresent(asset -> this.saleBook.getAssetsManager().addAsset(asset));
            } catch (IOException e) {
                DialogWindow.displayError("fail to load new asset controller", e);
            }
        }
    }

}
