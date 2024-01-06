package logic.products.position;

import logic.products.Item;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the data of a {@link Position} and is needed for the compatibility for saving.
 *
 */
public class PositionData extends PositionImpl{
    /**
     * The items of this positionData
     */
    private final Item[] items;

    /**
     * Constructor for a positionData
     *
     * @param position the position to get the data from to create this positionData
     */
    public PositionData(Position position) {
        super(position.id, position.category, position.orderDate, position.purchasingPrice,
                position.state, position.cost, position.receivedDate, position.sellingDate,
                position.sellingPrice, position.shippingCompany, position.trackingNumber);
        this.items = position.getItems().toArray(new Item[0]);
        this.nextItemId = position.nextItemId;
    }

    /**
     * Returns the items of this positionData
     *
     * @return the items of this positionData
     */
    public @NotNull Item[] getItems() {
        return this.items;
    }
}
