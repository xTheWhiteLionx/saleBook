package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.util.TableViewUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import logic.products.position.Position;
import logic.products.position.State;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import static gui.util.StageUtils.createStyledStage;

public class CombinePositionWithController extends FunctionDialog<int[]> implements Initializable {


    @FXML
    private TableView<Position> positionsTblVw;

    /**
     *
     * @param posId
     * @param positions
     * @return
     * @throws IOException
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
     *
     * @param posId
     * @param positions
     */
    private void initialize(int posId, @NotNull Collection<Position> positions) {
        ObservableList<Position> items = this.positionsTblVw.getItems();
        for (Position position : positions) {
            State state = position.getState();
            if (position.getId() != posId && (state == State.RECEIVED || state == State.REPAIRED)){
                items.add(position);
            }
        }
    }

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
        ObservableList<Position> selectedItems = this.positionsTblVw.getSelectionModel().getSelectedItems();
        int size = selectedItems.size();
        int [] selectedIds = new int[size];
        for (int i = 0; i < size; i++) {
            selectedIds[i] = selectedItems.get(i).getId();
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
