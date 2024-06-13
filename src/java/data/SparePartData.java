package data;

import logic.sparePart.SparePart;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The data representation of a {@link SparePart} and its quantity
 *
 * @see SparePart
 * @author xthe_white_lionx
 * @date 30.05.2024
 */
public class SparePartData extends SparePart {
    /**
     * The quantity of this spare parts in the stock
     */
    private final int quantity;

    /**
     * Constructor
     *
     * @param sparePart the spare part to get the data from
     * @param quantity  the quantity of the spare parts in the stock
     * @throws IllegalArgumentException if the name, the unit or the category is empty or the
     *                                  quantity is less or equals 0
     */
    public SparePartData(@NotNull SparePart sparePart, int quantity) {
        super(sparePart);
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater equals 0 but is "
                    + quantity);
        }

        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SparePartData that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return this.quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.quantity);
    }

    @Override
    public String toString() {
        return "SparePartData{" + "quantity=" + this.quantity +
                '}';
    }

    /**
     * Returns the quantity of this spare part
     *
     * @return the quantity of this spare part
     */
    public int getQuantity() {
        return this.quantity;
    }
}
