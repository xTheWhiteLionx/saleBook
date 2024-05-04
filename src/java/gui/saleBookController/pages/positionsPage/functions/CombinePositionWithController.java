package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.TableViewUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.products.position.Position;
import logic.products.position.State;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import static gui.FXutils.StageUtils.createStyledStage;

/**
 * This controller collects the selected ids of the positions which should be combined with
 *
 * @see gui.saleBookController.pages.FunctionDialog
 * @author xthe_white_lionx
 */
public class CombinePositionWithController extends FunctionDialog<int[]> implements Initializable {

    /**
     * TableView to display the combinable positions
     */
    @FXML
    private TableView<Position> positionsTblVw;

    /**
     * Creates and loads a new CombinePositionWithController
     *
     * @param posId the id of the position to combine with
     * @param positions all position not filtered yet
     * @return the new created CombinePositionWithController
     * @throws IOException if the fxml file could not be loaded
     */
    public static CombinePositionWithController createCombinePositionWithController(int posId,
                                                                                    @NotNull Collection<Position> positions)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/" +
                        "functions/CombinePositionWithController.fxml"));

        Stage stage = createStyledStage(new Scene(loader.load()));
        stage.setTitle("combine with position " + posId);
        stage.setMinWidth(350D);
        stage.setMinHeight(200D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        CombinePositionWithController controller = loader.getController();
        controller.initialize(posId, positions);
        stage.showAndWait();
        return controller;
    }

    /**
     * Initializes the specified fields of this
     *
     * @param posId the id of the position to combine with
     * @param positions all position not filtered yet
     */
    private void initialize(int posId, @NotNull Collection<Position> positions) {
        ObservableList<Position> tableVwItems = this.positionsTblVw.getItems();
        for (Position position : positions) {
            State state = position.getState();
            if (position.getId() != posId && (state == State.RECEIVED || state == State.REPAIRED)){
                tableVwItems.add(position);
            }
        }
    }

    /**
     * Initializes the fields of this controller.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.positionsTblVw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableViewUtils.addColumn(this.positionsTblVw, "id", Position::getId);
        TableViewUtils.addColumn(this.positionsTblVw, "category", Position::getCategory);
    }

    /**
     * Handles the press of the apply button
     */
    @FXML
    private void handleApply() {
        ObservableList<Position> selectedPositions = this.positionsTblVw.getSelectionModel().getSelectedItems();
        int size = selectedPositions.size();
        int [] selectedIds = new int[size];
        for (int i = 0; i < size; i++) {
            selectedIds[i] = selectedPositions.get(i).getId();
        }
        this.result = selectedIds;
        this.handleCancel();
    }

    /**
     * Handle the cancel button and closes this stage
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.positionsTblVw.getScene().getWindow();
        stage.close();
    }
}
