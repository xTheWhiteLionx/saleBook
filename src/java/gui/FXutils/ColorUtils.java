package gui.FXutils;

import javafx.scene.paint.Color;

/**
 * This class inherits one Utility methode {@link #toHexString(Color)} for color's.
 *
 * @author xthe_white_lionx
 */
public class ColorUtils {

    /**
     * Returns the string hexadecimal representation of the specified color without the opacity
     *
     * @param color the color of which the hexadecimal representation should be calculated
     * @return the hexadecimal representation of the specified color
     */
    public static String toHexString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        String hexAndOpacity = String.format("%08x", (r + g + b));
        return String.format(hexAndOpacity).substring(0, hexAndOpacity.length() - 2);
    }
}
