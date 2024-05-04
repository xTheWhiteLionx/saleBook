package logic.products.position;

import logic.products.item.Item;
import logic.products.item.ItemData;
import utils.CollectionsUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the data of a {@link Position} and is needed for the compatibility for saving.
 *
 * @author xthe_white_lionx
 */
public class PositionData extends AbstractPosition {
    /**
     * The itemData of this positionData
     */
    private final ItemData[] itemData;

    /**
     * Constructor for a positionData
     *
     * @param position the position to get the data from to create this positionData
     */
    public PositionData(Position position) {
        super(position.id, position.category, position.orderDate, position.purchasingPrice,
                position.state, position.cost, position.receivedDate, position.sellingDate,
                position.sellingPrice, position.shippingCompany, position.trackingNumber);
        this.itemData = CollectionsUtils.toArray(position.getItems(), ItemData::new,
                new ItemData[0]);
        this.nextItemId = position.nextItemId;
    }

    /**
     * Returns the itemData of this positionData
     *
     * @return the itemData of this positionData
     */
    public @NotNull ItemData[] getItemData() {
        return this.itemData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        PositionData that = (PositionData) o;
        return Objects.deepEquals(this.itemData, that.itemData);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.itemData);
    }

    @Override
    public String toString() {
        return "PositionData{" +
               "items=" + Arrays.toString(this.itemData) +
               ", id=" + this.id +
               ", category='" + this.category + '\'' +
               ", state=" + this.state +
               ", purchasingPrice=" + this.purchasingPrice +
               ", orderDate=" + this.orderDate +
               ", cost=" + this.cost +
               ", receivedDate=" + this.receivedDate +
               ", sellingDate=" + this.sellingDate +
               ", sellingPrice=" + this.sellingPrice +
               ", shippingCompany=" + this.shippingCompany +
               ", trackingNumber='" + this.trackingNumber + '\'' +
               ", nextItemId=" + this.nextItemId +
               '}';
    }
}
