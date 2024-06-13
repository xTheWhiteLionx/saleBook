package costumeClasses.FXClasses;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import logic.sparePart.SparePart;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A special TableCell from SparePart to Integer
 *
 * @author xthe_white_lionx
 */
public class SpinnerTableCell extends TableCell<SparePart, Integer> {



    /**
     * Enum of the type of the max value for the spinner of this SpinnerTableCell
     */
    public enum MaxValueType {
        /**
         * Use the stock of the spare part as the maximal value of the SpinnerTableCell
         */
        MAX_STOCK,
        /**
         * Use the "unlimited" as the maximal value of the SpinnerTableCell
         */
        UNLIMITED;
    }

    /**
     * Spinner of this spinnerTableCell, controller for editing
     */
    private Spinner<Integer> spinner;

    /**
     * IntegerSpinnerValueFactory of the spinner
     */
    private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory;

    private final Function<SparePart, Integer> getMaxValueOf;

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
     * @param maxValueType Type to get the maximal value for the spinner
     * @param updateMap BiConsumer to update the order quantity of a spare part
     * @param getValueOf Function to get the current order quantity of a spare part
     */
    public SpinnerTableCell(Function<SparePart, Integer> getMaxValueOf,
                            BiConsumer<SparePart, Integer> updateMap,
                            Function<SparePart, Integer> getValueOf) {
        this.getMaxValueOf = getMaxValueOf;
        this.updateMap = updateMap;
        this.getValueOf = getValueOf;
    }

    @Override
    public void startEdit() {
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

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            this.setGraphic(this.spinner);
        } else {
            if (this.isEditing()) {
                if (this.spinner != null) {
                    SparePart sparePart = this.getTableView().getItems().get(this.getIndex());
                    this.spinnerValueFactory.setMax(this.getMaxValueOf.apply(sparePart));
                    this.spinnerValueFactory.setValue(this.getItem());
                }
                this.setGraphic(this.spinner);
                this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                SparePart sparePart = this.getTableView().getItems().get(this.getIndex());
                Integer value = this.getValueOf.apply(sparePart);
                if (value == null) {
                    value = 0;
                }
                this.setText(String.valueOf(value));
                this.setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    /**
     * Initializes the spinner
     */
    private void initializeSpinner() {
        SparePart sparePart = this.getTableView().getItems().get(this.getIndex());
        this.spinner = new Spinner<>(0, this.getMaxValueOf.apply(sparePart), this.getItem());
        this.spinnerValueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) this.spinner.getValueFactory();
        this.spinner.setEditable(true);
        this.spinner.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 4);
        this.spinner.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                Integer value = this.spinner.getValue();
                this.commitEdit(value);
                this.cancelEdit();
                this.updateItem(value, false);
            } else if (t.getCode() == KeyCode.ESCAPE) {
                this.cancelEdit();
            }
        });
    }
}
