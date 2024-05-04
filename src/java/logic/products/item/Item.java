package logic.products.item;

import logic.Condition;
import logic.Variant;
import logic.products.Product;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class contains the Item logic.
 *
 * @author xthe_white_lionx
 */
public class Item extends AbstractItem implements Product, Comparable<Item> {

    /**
     * The itemColor of this item
     */
    private ItemColor itemColor;

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
        super(id,condition,variant,errorDescription);
        this.itemColor = itemColor;
    }

    /**
     * Constructor for an item
     *
     * @param itemData data for an item
     * @param itemColor the itemColor of the item
     */
    public Item(@NotNull ItemData itemData, @NotNull ItemColor itemColor) {
        super(itemData.id, itemData.condition, itemData.variant, itemData.errorDescription);
        this.itemColor = itemColor;
    }

    /**
     * Returns the itemColor of this item
     *
     * @return the itemColor of this item
     */
    public @NotNull ItemColor getItemColor() {
        return this.itemColor;
    }

    /**
     * Sets the itemColor of this item to the specified itemColor
     *
     * @param itemColor the new itemColor of this item
     */
    public void setItemColor(@NotNull ItemColor itemColor) {
        this.itemColor = itemColor;
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
