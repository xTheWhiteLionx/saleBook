package costumeClasses.FXClasses;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TableColumn;
import logic.sparePart.SparePart;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * This class represents a spinnerTableColumn.
 * A spinnerTableColumn has a spinnerTableCell for each cell of the column and stores the spinner value of each sparePart.
 * To get the values of the spinners use the {@link #getSparePartToSpinnerValue()} methode.
 *
 * @author xthe_white_lionx
 */
public class SpinnerTableColumn extends TableColumn<SparePart, Integer> {

    /**
     * Map that stores the value of the spinner to the mapping sparePart of the row
     */
    private final Map<SparePart, Integer> sparePartToSpinnerValue;

    /**
     * Constructor for an SpinnerTableColumn
     *
     * @param title the title of the column
     * @param maxValueType the MaxValueType of each SpinnerTableCell
     */
    public SpinnerTableColumn(String title, Function<SparePart, Integer> getMaxValue) {
        super(title);
        this.sparePartToSpinnerValue = new HashMap<>();
        this.setPrefWidth(100);
        this.setCellValueFactory(cellData -> new SimpleIntegerProperty(0).asObject());
        BiConsumer<SparePart, Integer> updateMap =
                (sparePart, value) -> {
                    if (value > 0) {
                        this.sparePartToSpinnerValue.put(sparePart, value);
                    } else {
                        this.sparePartToSpinnerValue.remove(sparePart);
                    }
                };
        this.setCellFactory(tc -> new SpinnerTableCell(getMaxValue, updateMap,
                this.sparePartToSpinnerValue::get));
        this.setOnEditCommit(
                t -> updateMap.accept(t.getTableView().getItems().get(
                        t.getTablePosition().getRow()), t.getNewValue()));
    }

    /**
     * Returns the sparePartToSpinnerValue map of this SpinnerTableColumn
     *
     * @return the sparePartToSpinnerValue map of this SpinnerTableColumn
     */
    public Map<SparePart, Integer> getSparePartToSpinnerValue() {
        return this.sparePartToSpinnerValue;
    }
}