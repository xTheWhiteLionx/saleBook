package utils;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
/**
 * This class inherits a Utility methode for URLs to validate if a string is a valid URL.
 *
 * @author xthe_white_lionx
 */
public class URLUtils {
    /**
     * Checks if the specified urlString is a valid URL.
     * A string is a valid url if it can be converted to one.
     *
     * @param urlString the urlString which should be checked
     * @return true if the specified urlString is a valid URL, otherwise false
     */
    public static boolean isValidURL(@NotNull String urlString) {
        if (urlString.isEmpty()) {
            return false;
        }
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
