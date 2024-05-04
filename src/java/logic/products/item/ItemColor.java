package logic.products.item;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This class represents a Color of an item which has a "color name".
 */
public class ItemColor implements Comparable<ItemColor> {

    /**
     * Map of name of an itemColor to his reference
     */
    private static final Map<String, ItemColor> ITEM_COLOR_MAP = new TreeMap<>();

    /**
     * The name of this itemColor
     */
    private final String name;
    /**
     * The color of this itemColor
     */
    private final Color color;

    /**
     * @param name
     * @param color
     * @return
     */
    public static @NotNull ItemColor getItemColor(String name, Color color) {
        ItemColor result = ITEM_COLOR_MAP.get(name);
        if (result == null) {
            result = new ItemColor(name, color);
            ITEM_COLOR_MAP.put(name, result);
        }
        return result;
    }

    /**
     *
     * @param itemColors
     */
    public static void setItemColors(@NotNull ItemColor[] itemColors) {
        for (ItemColor itemColor : itemColors) {
            ITEM_COLOR_MAP.put(itemColor.name, itemColor);
        }
    }

    /**
     * @return
     */
    public static Map<String, ItemColor> getItemColorMap() {
        return ITEM_COLOR_MAP;
    }

    /**
     * Constructor for an itemColor
     *
     * @param name  the name of the itemColor
     * @param color the color of this itemColor
     * @throws IllegalArgumentException if the name is empty
     */
    private ItemColor(@NotNull String name, @NotNull Color color) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }

        this.name = name;
        this.color = color;
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
    public int compareTo(@NotNull ItemColor o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemColor itemColor)) {
            return false;
        }
        return Objects.equals(this.name, itemColor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        return "ItemColor{" +
                "name='" + this.name + '\'' +
                ", color=" + this.color +
                '}';
    }
}
