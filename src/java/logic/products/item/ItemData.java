package logic.products.item;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemData extends AbstractItem {

    /**
     *
     */
    private final String itemColorName;

    /**
     *
     * @param item
     */
    public ItemData(@NotNull Item item) {
        super(item.id, item.condition, item.variant, item.errorDescription);
        this.itemColorName = item.getItemColor().getName();
    }

    /**
     *
     * @return
     */
    public @NotNull String getItemColorName() {
        return this.itemColorName;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemData itemData)) return false;
        return Objects.equals(this.itemColorName, itemData.itemColorName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.itemColorName);
    }

    @Override
    public String toString() {
        return "ItemData{" +
                "itemColorName='" + this.itemColorName + '\'' +
                ", id=" + this.id +
                ", condition=" + this.condition +
                ", variant=" + this.variant +
                ", errorDescription='" + this.errorDescription + '\'' +
                '}';
    }
}
