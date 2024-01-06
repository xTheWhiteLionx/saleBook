package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.util.TableViewUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.SparePart;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;

public class RepairedController extends FunctionDialog<Map<SparePart, Integer>> implements Initializable {

    @FXML
    public BorderPane borderPane;
    @FXML
    private Button applyBtn;
    @FXML
    private TableView<SparePart> sparePartsTblVw;

    private final Map<SparePart, Integer> sparePartToUsedMap = new HashMap<>();

    /**
     * @param spareParts
     * @return
     */
    public static @NotNull RepairedController createRepairedController(
            @NotNull Collection<SparePart> spareParts) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage" +
                        "/functions/RepairedController.fxml"));

        Scene scene = new Scene(loader.load(), 550, 400);
        Stage stage = createStyledStage(scene);
        stage.setTitle("repaired");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        RepairedController controller = loader.getController();
        controller.setSpareParts(spareParts);
        stage.showAndWait();
        return controller;
    }

    /**
     * @param spareParts
     */
    private void setSpareParts(@NotNull Collection<SparePart> spareParts) {
        ObservableList<SparePart> items = this.sparePartsTblVw.getItems();
        for (SparePart sparePart : spareParts) {
            if (sparePart.getQuantity() > 0) {
                items.add(sparePart);
            }
        }
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableViewUtils.addColumn(this.sparePartsTblVw, "name", SparePart::getName);
        TableViewUtils.addColumn(this.sparePartsTblVw, "condition", SparePart::getCondition);
        TableViewUtils.addColumn(this.sparePartsTblVw, "in stock", SparePart::getQuantity);
        this.sparePartsTblVw.getColumns().add(this.getSparePartIntegerTableColumn());
        this.sparePartsTblVw.setEditable(true);
    }

    @NotNull
    private TableColumn<SparePart, Integer> getSparePartIntegerTableColumn() {
        TableColumn<SparePart, Integer> usedColumn = new TableColumn<>("used");
        usedColumn.setPrefWidth(100);
        usedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(0).asObject());
        BiConsumer<SparePart, Integer> updateMap =
                (key, value) -> {
                    if (value > 0) {
                        this.sparePartToUsedMap.put(key, value);
                    } else {
                        this.sparePartToUsedMap.remove(key);
                    }
                };
        usedColumn.setCellFactory(tc -> new SpinnerTableCell(updateMap,
                (key) -> this.sparePartToUsedMap.getOrDefault(key, 0)));

        usedColumn.setOnEditCommit(
                t -> updateMap.accept(t.getTableView().getItems().get(
                        t.getTablePosition().getRow()), t.getNewValue()));

        return usedColumn;
    }

    /**
     *
     */
    @FXML
    private void handleApply() {
        this.result = this.sparePartToUsedMap;
        this.handleCancel();

    }

    /**
     *
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }

    /**
     *
     */
    public static class SpinnerTableCell extends TableCell<SparePart, Integer> {

        Spinner<Integer> spinner;

        SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory;

        BiConsumer<SparePart, Integer> updateMap;

        Function<SparePart, Integer> getValueOf;


        public SpinnerTableCell(BiConsumer<SparePart, Integer> updateMap, Function<SparePart, Integer> getValueOf) {
            this.updateMap = updateMap;
            this.getValueOf = getValueOf;
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (this.spinner == null) {
                this.createSpinner();
            }

            this.setGraphic(this.spinner);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            this.updateItem(this.spinner.getValue(), false);
            this.updateMap.accept(this.getTableRow().getItem(), this.spinner.getValue());
            this.setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        /**
         *
         */
        private void createSpinner() {
            this.spinner = new Spinner<>(0, this.getTableView().getItems().get(this.getIndex()).getQuantity(),
                    this.getItem());
            this.spinnerValueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) this.spinner.getValueFactory();
            this.spinner.setEditable(true);
            this.spinner.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 4);
            this.spinner.setOnKeyPressed(t -> {
                if (t.getCode() == KeyCode.ENTER) {
                    this.commitEdit(this.spinner.getValue());
                    this.cancelEdit();
                    this.updateItem(this.spinner.getValue(), false);
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    this.cancelEdit();
                }
            });
        }

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                this.setGraphic(this.spinner);
            } else {
                if (this.isEditing()) {
                    if (this.spinner != null) {
                        this.spinnerValueFactory.setMax(this.getTableView().getItems().get(this.getIndex()).getQuantity());
                        this.spinnerValueFactory.setValue(this.getItem());
                    }
                    this.setGraphic(this.spinner);
                    this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    this.setText(String.valueOf(this.getValueOf.apply(this.getTableView().getItems().get(this.getIndex()))));
                    this.setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }
    }
}
