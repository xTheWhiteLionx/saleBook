package gui.util;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * Some Utility's for choiceBox's. Likewise {@link #addItems(ChoiceBox, Class)}.
 *
 *  @author xthewhitelionx
 */
public class ChoiceBoxUtils {

    /**
     * Creates a new choiceBox filled with the elements of the specified enum class
     *
     * @param enumClass class of the enum
     * @param <T>       type of the elements
     * @throws IllegalArgumentException if the specified choiceBox or the enumClass is null
     */
    public static @NotNull <T extends Enum<T>> ChoiceBox<T> createChoiceBox(
            @NotNull Class<T> enumClass) {
        ChoiceBox<T> result = new ChoiceBox<>();
        addItems(result, enumClass);
        return result;
    }

    /**
     * Adds all the elements of the specified enum class to the specified choiceBox and puts the
     * value to the first element.
     *
     * @param choiceBox the choiceBox witch should be filled
     * @param enumClass class of the enum
     * @param <T>       type of the elements
     */
    public static <T extends Enum<T>> void addItems(@NotNull ChoiceBox<T> choiceBox,
                                                    @NotNull Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        choiceBox.getItems().addAll(values);
        if (values.length >= 1) {
            choiceBox.setValue(values[0]);
        }
    }

    /**
     * Adds all the elements of the specified values to the specified choiceBox and puts the
     * first value of the collection to the first element of the choiceBox. First value of
     * collection means the first returned value of his iterator.
     *
     * @param choiceBox the choiceBox witch should be filled
     * @param values    collection of values which should be added
     * @param <T>       type of the elements
     */
    public static <T> void addItems(@NotNull ChoiceBox<T> choiceBox, @NotNull Collection<T> values) {
        if (!values.isEmpty()) {
            choiceBox.getItems().addAll(values);
            choiceBox.setValue(values.iterator().next());
        }
    }

    /**
     * Sets all the elements of the specified values to the specified choiceBox and puts the
     * first value of the collection to the first element of the choiceBox. First value of
     * collection means the first returned value of his iterator.
     *
     * @param choiceBox the choiceBox witch should be filled
     * @param values    collection of values which should be displayed
     * @param <T>       type of the elements
     */
    public static <T> void setItems(@NotNull ChoiceBox<T> choiceBox, @NotNull Collection<T> values) {
        choiceBox.getItems().setAll(values);
        if (!values.isEmpty()) {
            choiceBox.setValue(values.iterator().next());
        }
    }
}
