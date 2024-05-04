package gui;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import logic.SparePart;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

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
        UNLIMITED
    }

    /**
     * Spinner of this spinnerTableCell, controller for editing
     */
    private Spinner<Integer> spinner;

    /**
     * IntegerSpinnerValueFactory of the spinner
     */
    private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory;

    /**
     * Type to get the maximal value for the spinner
     */
    private final MaxValueType maxValueType;

    /**
     * BiConsumer to updating the order quantity of a spare part
     */
    private final BiConsumer<SparePart, Integer> updateMap;

    /**
     * Function to get the current order quantity of a spare part
     */
    private final BiFunction<SparePart, Integer, Integer> getValueOf;

    /**
     * Constructor for an SpinnerTableCell
     *
     * @param maxValueType Type to get the maximal value for the spinner
     * @param updateMap BiConsumer to update the order quantity of a spare part
     * @param getValueOf Function to get the current order quantity of a spare part
     */
    public SpinnerTableCell(MaxValueType maxValueType, BiConsumer<SparePart, Integer> updateMap,
                            BiFunction<SparePart, Integer, Integer> getValueOf) {
        this.maxValueType = maxValueType;
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
                    this.spinnerValueFactory.setMax(this.getMaxValue());
                    this.spinnerValueFactory.setValue(this.getItem());
                }
                this.setGraphic(this.spinner);
                this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                SparePart sparePart = this.getTableView().getItems().get(this.getIndex());
                this.setText(String.valueOf(this.getValueOf.apply(sparePart, 0)));
                this.setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    /**
     * Initializes the spinner
     */
    private void initializeSpinner() {
        this.spinner = new Spinner<>(0, this.getMaxValue(), this.getItem());
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

    /**
     * Returns the maximal value depending on the maxValueType of this SpinnerTableCell
     *
     * @return the maximal value depending on the maxValueType
     */
    private int getMaxValue() {
        switch (this.maxValueType) {
            case MAX_STOCK -> {
                return this.getTableView().getItems().get(this.getIndex()).getQuantity();
            }
            case UNLIMITED -> {
                return Integer.MAX_VALUE;
            }
            default -> throw new IllegalStateException("Unexpected value: " + this.maxValueType);
        }
    }
}
