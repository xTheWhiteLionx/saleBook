package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.SpinnerTableCell;
import gui.SpinnerTableColumn;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.TableViewUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.manager.SparePartsManager;
import logic.sparePart.SparePart;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.FXutils.StageUtils.createStyledStage;

/**
 * Controller to choose the used spareParts for a repair
 *
 * @author xthe_white_lionx
 */
public class RepairedController extends FunctionDialog<Map<SparePart, Integer>> implements Initializable {

    /**
     * Button to apply the changes
     */
    @FXML
    private Button applyBtn;

    /**
     * TableView to display the spare parts that could be used for the repair
     */
    @FXML
    private TableView<SparePart> sparePartsTblVw;

    /**
     * SpinnerTableColumn needed to display a spinner for each row of the tableView
     */
    private SpinnerTableColumn spinnerTableColumn;

    /**
     *
     */
    private SparePartsManager sparePartsManager;

    /**
     * Creates and loads a new RepairedController
     *
     * @param category
     * @param sparePartsManager
     * @return the new created RepairedController
     */
    public static @NotNull RepairedController createRepairedController(
            @NotNull String category, @NotNull SparePartsManager sparePartsManager) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage" +
                        "/functions/RepairedController.fxml"));

        Scene scene = new Scene(loader.load(), 550, 400);
        Stage stage = createStyledStage(scene);
        stage.setTitle("repaired");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        RepairedController controller = loader.getController();
        controller.initialize(category, sparePartsManager);
        stage.showAndWait();
        return controller;
    }

    /**
     * Initializes this controller
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableViewUtils.addColumn(this.sparePartsTblVw, "name", SparePart::getName);
        TableViewUtils.addColumn(this.sparePartsTblVw, "condition", SparePart::getCondition);

    }

    /**
     * Sets the chose able spareParts of the TableView to the specified spareParts and filters them if needed
     *
     * @param category
     * @param sparePartsManager
     */
    private void initialize(@NotNull String category,
                            @NotNull SparePartsManager sparePartsManager) {
        this.sparePartsManager = sparePartsManager;
        TableViewUtils.addColumn(this.sparePartsTblVw, "in stock", this.sparePartsManager::getQuantity);
        this.spinnerTableColumn = new SpinnerTableColumn("used", SpinnerTableCell.MaxValueType.MAX_STOCK);
        this.sparePartsTblVw.getColumns().add(this.spinnerTableColumn);

        ObservableList<SparePart> items = this.sparePartsTblVw.getItems();
        Set<SparePart> spareParts = sparePartsManager.getSparePartsOfCategory(category);
        if (spareParts != null) {
            for (SparePart sparePart : spareParts) {
                if (sparePart.getCondition().isUsable() && sparePartsManager.inStock(sparePart)) {
                    items.add(sparePart);
                }
            }
        }
    }

    /**
     * Handles the apply button and sets the result
     */
    @FXML
    private void handleApply() {
        this.result = this.spinnerTableColumn.getSparePartToSpinnerValue();
        this.handleCancel();
    }

    /**
     * Handles the cancel button and closes the window
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }
}
