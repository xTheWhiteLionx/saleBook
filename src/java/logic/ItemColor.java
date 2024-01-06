package logic;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class represents a Color of an item which has a "color name".
 */
public class ItemColor {
    /**
     * The name of this itemColor
     */
    private final String name;
    /**
     * The color of this itemColor
     */
    private final Color color;

    /**
     * Constructor for an itemColor
     *
     * @param name the name of the itemColor
     * @param color the color of this itemColor
     */
    public ItemColor(@NotNull String name, @NotNull Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Copy constructor for an itemColor
     *
     * @param itemColor the itemColor which should be copied
     */
    public ItemColor(@NotNull ItemColor itemColor) {
        this.name = itemColor.name;
        this.color = itemColor.color;
    }

    /**
     * Returns the name of this itemColor
     *
     * @return the name of this itemColor
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Returns the color of this itemColor
     *
     * @return the color of this itemColor
     */
    public @NotNull Color getColor() {
        return this.color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemColor itemColor)) {
            return false;
        }
        return Objects.equals(this.name, itemColor.name) && Objects.equals(this.color, itemColor.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.color);
    }

    @Override
    public String toString() {
        return "ItemColor[" +
                "getName=" + this.name + ", " +
                "getColor=" + this.color + ']';
    }

}
