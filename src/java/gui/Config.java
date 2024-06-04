package gui;

import org.jetbrains.annotations.NotNull;

import java.util.prefs.Preferences;

/**
 * This class contains the configuration
 *
 * @author xthe_white_lionx
 */
public class Config {

    /**
     * The preference node from the calling user's preference tree
     */
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(Config.class);

    /**
     * Key to get the auto save preference
     */
    private static final String AUTO_SAVE = "Autosave";

    /**
     * Key to get the theme preference
     */
    private static final String THEME = "Theme";

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private Config() {
    }

    /**
     * Returns true if auto save is on, otherwise false
     *
     * @return true if auto save is on, otherwise false
     */
    public static boolean isAutoSave() {
        return PREFERENCES.getBoolean(AUTO_SAVE, false);
    }

    /**
     * Sets auto save on or off.
     *
     * @param autoSave sets auto save on or off
     */
    public static void setAutoSave(boolean autoSave) {
        PREFERENCES.putBoolean(AUTO_SAVE, autoSave);
    }

    /**
     * Returns the current Theme or light-mode if no preference is set.
     *
     * @return current Theme or light-mode if no preference is set
     */
    public static @NotNull String getTheme(){
        return PREFERENCES.get(THEME, "Lightmode");
    }

    /**
     * Sets the preferred theme
     *
     * @param theme the preferred theme
     */
    public static void setTheme(@NotNull String theme) {
        PREFERENCES.put(THEME, theme);
    }

}
