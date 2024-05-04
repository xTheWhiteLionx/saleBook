package logic.order;

import logic.SparePart;
import logic.Supplier;
import utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;

/**
 * This class represents an order.
 * An order has a supplier, a cost and a mapping
 * from spare parts to their order quantity.
 *
 * @author xThe_white_Lionx
 */
public class Order implements Comparable<Order>{

    /**
     * The id of the order
     */
    private final int id;
    /**
     * The supplier of the order
     */
    private final Supplier supplier;
    /**
     * The cost of the order
     */
    private BigDecimal value;

    /**
     * The spare parts of the order mapped to their quantity
     */
    private final Map<SparePart, Integer> sparePartToOrderQuantity;

    /**
     * Constructor to create an order
     *
     * @param id the id of the order
     * @param supplier the supplier of the order
     * @param sparePartToOrderQuantity mapping from the spare part to the ordered quantity
     * @param value the value of the order
     */
    public Order(int id, @NotNull Supplier supplier,
                 @NotNull Map<SparePart, Integer> sparePartToOrderQuantity,
                 @NotNull BigDecimal value) {
        this.id = id;
        this.supplier = supplier;
        this.value = value;
        this.sparePartToOrderQuantity = sparePartToOrderQuantity;
    }

    /**
     * Returns the id of this order
     *
     * @return id of this order
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the supplier of this order
     *
     * @return supplier of this order
     */
    public @NotNull Supplier getSupplier() {
        return this.supplier;
    }

    /**
     * Returns the mapping from the spare parts to their order quantity
     *
     * @return mapping from the spare parts to their order quantity
     */
    public @NotNull Map<SparePart, Integer> getSparePartToOrderQuantity() {
        return new HashMap<>(this.sparePartToOrderQuantity);
    }

    /**
     * Returns a copy of the ordered spare parts
     *
     * @return a copy of the ordered spare parts
     */
    public @NotNull Set<SparePart> getSpareParts(){
        return Collections.unmodifiableSet(this.sparePartToOrderQuantity.keySet());
    }

    /**
     * Returns the order quantity to which the specified {@link SparePart} is mapped,
     * or {@code null} if this order does not contain the sparePart.
     *
     * @param sparePart to which the order quantity is asked
     * @return the order quantity of the specified sparePart
     */
    public @Nullable Integer getOrderQuantity(@NotNull SparePart sparePart){
        return this.sparePartToOrderQuantity.get(sparePart);
    }

    /**
     * Returns the cost of this order
     *
     * @return the cost of this order
     */
    public @NotNull BigDecimal getValue() {
        return this.value;
    }

    /**
     * Sets the cost of this order to the specified cost
     *
     * @param value the new cost of this order
     * @throws IllegalArgumentException if the specified cost is less then 0
     */
    public void setValue(@NotNull BigDecimal value) {
        if (!BigDecimalUtils.isPositive(value)) {
            throw new IllegalArgumentException("cost must be greater equals 0 but is " + value);
        }
        this.value = value;
    }

    /**
     * Returns the order quantity to which the specified {@link SparePart} is mapped,
     * or {@code null} if this order contains no mapping for the orderedSparePart.
     *
     * @param orderedSparePart the spareParts that should be removed
     * @return the ordered amount of the specified orderSparePart of this order
     * @throws IllegalArgumentException if the specified orderedSparePart is null
     */
    public @Nullable Integer removeSparePart(@NotNull SparePart orderedSparePart){
        return this.sparePartToOrderQuantity.remove(orderedSparePart);
    }

    @Override
    public int compareTo(@NotNull Order o) {
        int result = this.id - o.id;
        if (result == 0){
           result = this.supplier.compareTo(o.supplier);
        }
        if (result == 0){
            result = this.value.compareTo(o.value);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order order)) {
            return false;
        }
        return this.id == order.id && Objects.equals(this.supplier, order.supplier)
                && Objects.equals(this.sparePartToOrderQuantity, order.sparePartToOrderQuantity)
                && Objects.equals(this.value, order.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.supplier, this.sparePartToOrderQuantity, this.value);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + this.id +
                ", supplier=" + this.supplier +
                ", sparePartToOrderQuantity=" + this.sparePartToOrderQuantity +
                ", cost=" + this.value +
                '}';
    }
}
