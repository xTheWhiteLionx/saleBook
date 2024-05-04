package gui.FXutils;

import javafx.scene.control.SpinnerValueFactory;
import org.jetbrains.annotations.NotNull;

/**
 * This class inherits one Utility methode {@link #createValueFactory(int)} for spinner's.
 *
 * @author xthe_white_lionx
 */
public class SpinnerUtils {
    /**
     * Creates a new SpinnerValueFactory with 0 as minimum value,
     * {@link Integer#MAX_VALUE} as maximum value and the specified initial value
     *
     * @param initValue the initial value of the spinnerValueFactory
     * @return the new created SpinnerValueFactory
     */
    public static @NotNull SpinnerValueFactory<Integer> createValueFactory(int initValue) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,
                initValue);
    }
}
