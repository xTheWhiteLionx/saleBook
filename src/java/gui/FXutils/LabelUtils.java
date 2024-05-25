package gui.FXutils;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static gui.JavaFXGUI.formatMoney;
import static javafx.scene.paint.Color.*;

/**
 * Some Utility's for label's.
 *
 * @author xthewhitelionx
 */
public class LabelUtils {
    /**
     * Symbol of the local currency
     */
    public static final String SYMBOL_OF_CURRENCY = Currency.getInstance(Locale.getDefault()).getSymbol();

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private LabelUtils() {
    }


    /**
     * Sets the text of the given Labels to the
     * current currency symbol
     *
     * @param labels currency labels
     */
    public static void setCurrencies(@NotNull Label @NotNull ... labels) {
        for (Label lbl : labels) {
            lbl.setText(SYMBOL_OF_CURRENCY);
        }
    }

    /**
     * Sets the specified number as text of the specified label formatted as an amount of money
     *
     * @param label  on which the formatted number will be displayed
     * @param number value which will be formatted as an amount of money
     */
    public static void setMoney(@NotNull Label label, @NotNull Number number) {
        label.setText(formatMoney(number));
    }

    /**
     * Sets the given value as text of the given label formatted as amount of money and
     * colors the text green, black or red as the value is greater, equals or less than 0.
     *
     * @param label on which the formatted number will be displayed
     * @param value value which will be formatted as an amount of money
     */
    public static void setMoneyAndColor(@NotNull Label label, @NotNull BigDecimal value) {
        label.setText(formatMoney(value));
        Paint color = BLACK;
        int compare = value.compareTo(BigDecimal.ZERO);
        if (compare > 0) {
            color = GREEN;
        } else if (compare < 0) {
            color = RED;
        }
        label.setTextFill(color);
    }
}
