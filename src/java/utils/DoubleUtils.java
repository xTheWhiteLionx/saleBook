package utils;

import java.util.Collection;

/**
 * This class inherits some Utilities for doubles.
 *
 * @author xThe_white_Lionx
 * @Date 28.05.2024
 */
public class DoubleUtils {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private DoubleUtils() {
    }

    /**
     *
     * @param s
     * @return
     */
    public static double parse(String s){
        s = s.replace(',', '.');
        return Double.parseDouble(s);
    }
}
