package utils;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class inherits some Utilities for BigDecimals.
 *
 * @author xthe_white_lionx
 */
public class BigDecimalUtils {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private BigDecimalUtils() {
    }

    /**
     * Checks if the specified number is positive.
     * A Number is positive if it is &ge 0
     *
     * @param number the number which should be checked
     * @return true if the number is positive, otherwise false
     */
    public static boolean isPositive(@NotNull BigDecimal number){
        return number.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Returns the calculated percentage of the specified pct based on the specified base
     *
     * @param base the base for the percentage calculation
     * @param pct the part of the base which the percentage should be calculated
     * @return the calculated percentage of the specified pct based on the specified base
     */
    public static @NotNull BigDecimal calcPercent(@NotNull BigDecimal base, @NotNull BigDecimal pct){
        return new BigDecimal(100).divide(base,2, RoundingMode.HALF_UP).multiply(pct)
                .setScale(2,RoundingMode.HALF_UP);
    }

    /**
     * Converts the specified text in to a BigDecimal
     *
     * @param text the text which should be converted
     * @return the BigDecimal representation of the specified text
     * @throws IllegalArgumentException if the specified text is empty
     */
    public static @NotNull BigDecimal parse(@NotNull String text) {
        if (text.isEmpty()) {
            throw new IllegalArgumentException("the text is empty");
        }
        return new BigDecimal(text.trim().replace(",", "."));
    }

}
