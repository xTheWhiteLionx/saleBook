package gui.util;

import org.jetbrains.annotations.NotNull;

/**
 * Some Utility's for string's.
 *
 * @author xthewhitelionx
 */
public class StringUtils {
    /**
     * Returns true if and only if the specified main string contains the specified
     * substring. The upper and lower case of characters are ignored.
     *
     * @param mainString the string in which will be searched
     * @param subString the sub string to search for
     * @return true if the main string contains the sub string, ignoring upper and lower case, false
     * otherwise
     */
    public static boolean containsIgnoreCase(@NotNull String mainString, @NotNull String subString){
        final int length = subString.length();
        if (length == 0) {
            return true; // Empty string is contained
        }

        final char firstLo = Character.toLowerCase(subString.charAt(0));
        final char firstUp = Character.toUpperCase(subString.charAt(0));

        for (int i = mainString.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = mainString.charAt(i);
            if (ch != firstLo && ch != firstUp) {
                continue;
            }

            if (mainString.regionMatches(true, i, subString, 0, length)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Examines if the specified text is a valid number.
     * A number is valid if it has only digits, a dot and/or a plus/minus as prefix.
     *
     * @param text the text which should be examined
     * @return true if the text is a valid number otherwise false
     */
    public static boolean isValidNumber(@NotNull String text) {
        return text.trim().matches(TextFieldUtils.NUMBER_PATTERN);
    }
}
