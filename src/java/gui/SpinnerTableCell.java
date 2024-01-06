package gui;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import logic.SparePart;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 *
 */
//TODO 25.12.2023 JavaDoc
public class SpinnerTableCell extends TableCell<SparePart, Integer> {

    /**
     * Spinner of this spinnerTableCell, controller for editing
     */
    private Spinner<Integer> spinner;
    /**
     *
     */
    private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory;
    /**
     * BiConsumer to updating the order quantity of a spare part
     */
    private final BiConsumer<SparePart, Integer> updateMap;
    /**
     * Function to get the current order quantity of a spare part
     */
    private final Function<SparePart, Integer> getValueOf;


    /**
     * Constructor for an SpinnerTableCell
     *
     * @param updateMap BiConsumer to update the order quantity of a spare part
     * @param getValueOf Function to get the current order quantity of a spare part
     */
    public SpinnerTableCell(BiConsumer<SparePart, Integer> updateMap,
                            Function<SparePart, Integer> getValueOf) {
        this.updateMap = updateMap;
        this.getValueOf = getValueOf;
    }

    @Override
    public void startEdit(){
        super.startEdit();

        if (this.spinner == null) {
            this.initializeSpinner();
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
     * Initializes the spinner
     */
    private void initializeSpinner() {
        this.spinner = new Spinner<>(0, Integer.MAX_VALUE, this.getItem());
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
