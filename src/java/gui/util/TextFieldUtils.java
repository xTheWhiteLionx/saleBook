package gui.util;

import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class TextFieldUtils {

    /**
     * Regex for only integers or double values
     * (only two decimal places are guaranteed)
     */
    public static final String NUMBER_PATTERN = "^[-+]?\\d+([,.]\\d\\d)?$";

    /**
     * Examines if the text of the specified textField is a positive number.
     * A number is positive if it is &ge 0
     *
     * @param textField the text field which inherits the text which should be examined
     * @return true if the text is a positive number otherwise false
     */
    public static boolean isPositive(@NotNull TextField textField) {
        String text = textField.getText().trim();
        if (!StringUtils.isValidNumber(text)) {
            return false;
        }
        return Double.parseDouble(text) >= 0;
    }

    /**
     * Examines if the given text field matches the regex for
     * a valid double
     *
     * @param textField to be examined text field
     * @return true if the text, of the text field, is a valid number
     * otherwise false
     */
    public static boolean isValidNumber(@NotNull TextField textField) {
        return StringUtils.isValidNumber(textField.getText());
    }

}
