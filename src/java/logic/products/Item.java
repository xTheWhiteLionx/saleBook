package logic.products;

import logic.Condition;
import logic.ItemColor;
import logic.Variant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class contains the Item logic.
 *
 * @author xthe_white_lionx
 */
public class Item implements Product, Comparable<Item> {

    /**
     * The id of this item
     */
    private int id;
    /**
     * The condition of this item
     */
    private Condition condition;
    /**
     * The variant of this item
     */
    private Variant variant;
    /**
     * The itemColor of this item
     */
    private ItemColor itemColor;
    /**
     * The error and faulty description of this item
     */
    private String errorDescription;

    /**
     * Constructor for an item
     *
     * @param id the id of this item
     * @param condition the condition of this item
     * @param variant the variant of this item
     * @param itemColor the itemColor of this item
     * @param errorDescription the error and faulty description of this item
     */
    public Item(int id, @NotNull Condition condition, @NotNull Variant variant,
                @NotNull ItemColor itemColor, @NotNull String errorDescription) {
        this.id = id;
        this.condition = condition;
        this.variant = variant;
        this.itemColor = itemColor;
        this.errorDescription = errorDescription;
    }

    /**
     * Returns the id of this item
     *
     * @return the id of this item
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Sets the id of this item
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Return the condition of this item
     *
     * @return the condition of this item
     */
    public @NotNull Condition getCondition() {
        return this.condition;
    }

    /**
     * Sets the condition of this item to the specified condition
     *
     * @param condition the new condition of this item
     */
    public void setCondition(@NotNull Condition condition) {
        this.condition = condition;
    }

    /**
     * Returns the variant of this item
     *
     * @return he variant of this item
     */
    public @NotNull Variant getVariant() {
        return this.variant;
    }

    /**
     * Sets the variant of this item to the specified variant
     *
     * @param variant the new variant of this item
     */
    public void setVariant(@NotNull Variant variant) {
        this.variant = variant;
    }

    /**
     * Returns the itemColor of this item
     *
     * @return the itemColor of this item
     */
    public @NotNull ItemColor getItemColor() {
        return new ItemColor(this.itemColor);
    }

    /**
     * Sets the itemColor of this item to the specified itemColor
     *
     * @param itemColor the new itemColor of this item
     */
    public void setItemColor(@NotNull ItemColor itemColor) {
        this.itemColor = itemColor;
    }

    /**
     * Returns the error and faulty description of this ...
     *
     * @return the error description of this ...
     */
    public @NotNull String getErrorDescription() {
        return this.errorDescription;
    }

    /**
     * Sets the errorDescription of this ... to the specified errorDescription
     *
     * @param errorDescription the errorDescription which will be set
     */
    public void setErrorDescription(@NotNull String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public int compareTo(@NotNull Item o) {
        int result = o.id - this.id;
        if(result == 0){
            result = this.variant.compareTo(o.variant);
        }
        if (result == 0){
            result = this.condition.compareTo(o.condition);
        }
        if (result == 0){
            result = this.itemColor.equals(o.itemColor) ? 0 : -1;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return this.condition == item.condition && this.variant == item.variant &&
                Objects.equals(this.itemColor, item.itemColor) &&
                Objects.equals(this.errorDescription, item.errorDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.condition, this.variant, this.itemColor,
                this.errorDescription);
    }

    @Override
    public String getSimpleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + this.id +
                ", condition=" + this.condition +
                ", variant=" + this.variant +
                ", itemColor=" + this.itemColor +
                ", errorDescription='" + this.errorDescription + '\'' +
                '}';
    }
}
