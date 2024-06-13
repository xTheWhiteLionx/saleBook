package logic.sparePart;

import logic.Condition;
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
     * The category of this spare part
     */
    private String category;
    /**
     * The name of this spare part
     */
    private String name;
    /**
     * The condition of this spare part
     */
    private Condition condition;
    /**
     * The quantity unit of this spare part
     */
    private String unit;

    /**
     * The minimum stock which is required for this spare part
     */
    private int minimumStock;

    /**
     * Constructor for a spare part
     *
     * @param name         the name of the spare part
     * @param condition    the condition of the spare part
     * @param unit         the unit of the quantity
     * @param category     the category for which this spare part can be used
     * @param minimumStock the minimum stock which is required for this spare part
     * @throws IllegalArgumentException if the name, the unit or the category is empty or the
     *                                  quantity is less or equals 0
     */
    public SparePart(@NotNull String name, @NotNull Condition condition, @NotNull String unit,
                     @NotNull String category, int minimumStock) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        if (unit.isEmpty()) {
            throw new IllegalArgumentException("unit is empty");
        }
        if (category.isEmpty()) {
            throw new IllegalArgumentException("category ist empty");
        }

        this.name = name;
        this.condition = condition;
        this.category = category;
        this.unit = unit;
        this.minimumStock = minimumStock;
    }

    /**
     * Copy constructor
     *
     * @param sparePart the spare part which should be copied
     */
    public SparePart(SparePart sparePart) {
        this.name = sparePart.name;
        this.condition = sparePart.condition;
        this.category = sparePart.category;
        this.unit = sparePart.unit;
        this.minimumStock = sparePart.minimumStock;
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
     * Returns the minimum stock which is required for this spare part
     *
     * @return the minimum stock which is required for this spare part
     */
    public int getMinimumStock() {
        return this.minimumStock;
    }

    /**
     * Sets the minimum stock which is required for this spare part
     *
     * @param minimumStock the new required minimum stock
     * @throws IllegalArgumentException if the new minimum stock is negative (x < 0)
     */
    public void setMinimumStock(int minimumStock) {
        if (minimumStock < 0) {
            throw new IllegalArgumentException("minimumStock is negative");
        }

        this.minimumStock = minimumStock;
    }

    @Override
    public int compareTo(@NotNull SparePart that) {
        int result = this.name.compareTo(that.name);
        if (result == 0) {
            result = this.condition.compareTo(that.condition);
        }
        if (result == 0) {
            result = this.category.compareTo(that.category);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof SparePart sparePart)) {
            return false;
        }
        return Objects.equals(this.name, sparePart.name)
                && this.condition == sparePart.condition
                && Objects.equals(this.category, sparePart.category)
                && Objects.equals(this.unit, sparePart.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.condition, this.category, this.unit);
    }

    @Override
    public String toString() {
        return "SparePart{" +
                "category='" + this.category + '\'' +
                ", name='" + this.name + '\'' +
                ", condition=" + this.condition +
                ", unit='" + this.unit + '\'' +
                ", minimumStock=" + this.minimumStock +
                '}';
    }
}
