package logic.sparePart;

import logic.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SparePartData extends SparePart {
    /**
     * The quantity of this spare parts in the stock
     */
    private int quantity;

    /**
     * Constructor for a spare part
     *
     * @param name      the name of the spare part
     * @param condition the condition of the spare part
     * @param unit      the unit of the quantity
     * @param category
     * @param minimumStock
     * @param quantity  the quantity of the spare parts in the stock
     * @throws IllegalArgumentException if the name, the unit or the category is empty or the
     *                                  quantity is less or equals 0
     */
    public SparePartData(@NotNull String name, @NotNull Condition condition, @NotNull String unit,
                         @NotNull String category, @Nullable Integer minimumStock, int quantity) {
        super(name, condition, unit, category, minimumStock);
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater equals 0 but is "
                    + quantity);
        }

        this.quantity = quantity;
    }

    /**
     *
     * @param sparePart
     * @param quantity
     */
    public SparePartData(@NotNull SparePart sparePart, int quantity) {
        super(sparePart);
        this.quantity = quantity;
    }

    /**
     * Returns the quantity of this spare part
     *
     * @return the quantity of this spare part
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Sets the quantity of this spare part to the specified newQuantity
     *
     * @param newQuantity the new quantity
     * @throws IllegalArgumentException if the specified newQuantity is less or equals 0
     */
    public void setQuantity(int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("quantity must be greater equals 0 but is "
                    + newQuantity);
        }
        this.quantity = newQuantity;
    }
}
