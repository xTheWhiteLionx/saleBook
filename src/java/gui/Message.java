package gui;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public enum Message {
    added("successfully added"),
    deleted("successfully deleted"),
    loaded("successfully loaded"),
    saved("successfully saved");

    /**
     *
     */
    private final String text;

    /**
     *
     * @param text
     */
    Message(@NotNull String text) {
        this.text = text;
    }

    /**
     *
     * @param prefix
     * @return
     */
    public @NotNull String formatMessage(@NotNull String prefix) {
        return prefix + " " + this.text;
    }
}
