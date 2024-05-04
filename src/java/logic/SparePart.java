package logic;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class represents a spare part for an item. A spare part is unique by
 * the combination of his name and condition and can be identified by them.
 *
 * @author xthe_white_lionx
 */
public class SparePart implements Comparable<SparePart> {

    /**
     * The name of this spare part
     */
    private String name;
    /**
     * The condition of this spare part
     */
    private Condition condition;
    /**
     * The category of this spare part
     */
    private String category;
    /**
     * The quantity unit of this spare part
     */
    private String unit;
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
     * @param quantity  the quantity of the spare parts in the stock
     * @throws IllegalArgumentException if the name, the unit or the category is empty or the
     *                                  quantity is less or equals 0
     */
    public SparePart(@NotNull String name, @NotNull Condition condition, @NotNull String unit,
                     @NotNull String category, int quantity) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        if (unit.isEmpty()) {
            throw new IllegalArgumentException("unit is empty");
        }
        if (category.isEmpty()) {
            throw new IllegalArgumentException("category ist empty");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater equals 0 but is "
                    + quantity);
        }

        this.name = name;
        this.condition = condition;
        this.category = category;
        this.unit = unit;
        this.quantity = quantity;
    }

    /**
     * Returns the name of this spare part
     *
     * @return the name of this spare part
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Sets the name of this spare part to the specified newName
     *
     * @param newName the new name of this spare part
     * @throws IllegalArgumentException if the specified newName is empty
     */
    public void setName(@NotNull String newName) {
        if (newName.isEmpty()) {
            throw new IllegalArgumentException("newName is empty");
        }
        this.name = newName;
    }

    /**
     * Returns the condition of this spare part
     *
     * @return the condition of this spare part
     */
    public @NotNull Condition getCondition() {
        return this.condition;
    }

    /**
     * Sets the condition of this spare part to the specified newCondition
     *
     * @param newCondition the new condition of this spare part
     */
    public void setCondition(@NotNull Condition newCondition) {
        this.condition = newCondition;
    }

    /**
     * Returns the category for which this spare part is
     *
     * @return the category for which this spare part is
     */
    public @NotNull String getCategory() {
        return this.category;
    }

    /**
     * Sets the category for which this spare part is
     *
     * @param category the new model of this spare part
     * @throws IllegalArgumentException if the category is empty
     */
    public void setCategory(@NotNull String category) {
        if (category.isEmpty()) {
            throw new IllegalArgumentException("category ist empty");
        }

        this.category = category;
    }

    /**
     * Returns the quantity unit of this spare part
     *
     * @return the unit of this spare part
     */
    public @NotNull String getUnit() {
        return this.unit;
    }

    /**
     * Sets the quantity unit to the specified newUnit
     *
     * @param newUnit the new quantity unit
     * @throws IllegalArgumentException if the specified newUnit is empty
     */
    public void setUnit(@NotNull String newUnit) {
        if (newUnit.isEmpty()) {
            throw new IllegalArgumentException("unit is empty");
        }
        this.unit = newUnit;
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

    /**
     * Adds the specified quantity to the quantity of this spare part.
     * To subtract the quantity, use the {@link #use(int)} methode.
     *
     * @param quantity the quantity which should be added to the quantity of this spare part
     * @throws IllegalArgumentException if the specified quantity is less or equals 0
     */
    public void addQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater equals 0 but is "
                    + quantity);
        }
        this.quantity += quantity;
    }

    /**
     * Uses the specified count of the quantity of this spare part
     *
     * @param count the count which should be used of the quantity
     * @throws IllegalArgumentException if the specified count is less or equals 0 or the count
     *                                  is higher than the quantity of this sparePart
     */
    public void use(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be greater equals 0 but is "
                    + count);
        }
        if (count > this.quantity) {
            throw new IllegalArgumentException(String.format("Not enough parts of type %s " +
                    "(requested %d, but we only have %d.)", this.name, count, this.quantity));
        }
        if (count > 0) {
            this.quantity -= count;
        }
    }

    @Override
    public int compareTo(@NotNull SparePart that) {
        int result = this.name.compareTo(that.name);
        if (result == 0) {
            result = this.condition.compareTo(that.condition);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SparePart sparePart)) {
            return false;
        }
        return this.quantity == sparePart.quantity && Objects.equals(this.name, sparePart.name) &&
                this.condition == sparePart.condition && Objects.equals(this.category, sparePart.category) &&
                Objects.equals(this.unit, sparePart.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.condition, this.category, this.unit, this.quantity);
    }

    @Override
    public String toString() {
        return "SparePart{" +
                "name='" + this.name + '\'' +
                ", condition=" + this.condition +
                ", category='" + this.category + '\'' +
                ", unit='" + this.unit + '\'' +
                ", quantity=" + this.quantity +
                '}';
    }
}
