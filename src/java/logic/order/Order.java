package logic.order;

import logic.sparePart.SparePart;
import logic.Supplier;
import org.jetbrains.annotations.UnmodifiableView;
import utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.IterableUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * This class represents an order.
 * An order has an orderState, an orderDate, a supplier, a cost and a mapping
 * from spare parts to their order quantity.
 *
 * @author xThe_white_Lionx
 */
public class Order implements Comparable<Order> {

    /**
     * Enum of status which an {@link Order} or an {@link SparePart} of the order could reach.
     */
    public enum OrderState {
        /**
         * An order is ordered if not all parts are received
         */
        ORDERED,
        /**
         * An order is received if all parts are received
         */
        RECEIVED,
        /**
         * A cancelled order.
         * A spare part is cancelled if his order is cancelled
         */
        CANCELLED;
    }

    /**
     * The id of this order
     */
    private final int id;

    /**
     * The current orderState of this order
     */
    private OrderState orderState = OrderState.ORDERED;

    /**
     * Order date of this order
     */
    private final LocalDate orderDate;

    /**
     * The supplier of this order
     */
    private final Supplier supplier;

    /**
     * The cost of this order
     */
    private double value;

    /**
     * The spare parts of this order mapped to their quantity
     */
    private final Map<SparePart, Integer> sparePartToOrderQuantity;

    /**
     * The spare parts that are not received yet
     */
    private final Set<SparePart> pendingSpareParts;

    /**
     * Constructor to create an order
     *
     * @param id                       the id of the order
     * @param orderDate                the date of order
     * @param supplier                 the supplier of the order
     * @param sparePartToOrderQuantity mapping from the spare part to the ordered quantity
     * @param value                    the value of the order
     * @throws IllegalArgumentException if the sparePartToOrderQuantity is empty
     */
    public Order(int id, @NotNull LocalDate orderDate, @NotNull Supplier supplier,
                 @NotNull Map<SparePart, Integer> sparePartToOrderQuantity, double value) {
        if (sparePartToOrderQuantity.isEmpty()) {
            throw new IllegalArgumentException("sparePartToOrderQuantity is empty");
        }

        boolean areValid = IterableUtils.areValid(sparePartToOrderQuantity.values(),
                (quantity) -> quantity != null && quantity > 0);
        if (!areValid) {
            throw new IllegalArgumentException("each quantity must be greater 0");
        }

        this.id = id;
        this.orderDate = orderDate;
        this.supplier = supplier;
        this.value = value;
        this.sparePartToOrderQuantity = new TreeMap<>();
        this.pendingSpareParts = new HashSet<>();
        sparePartToOrderQuantity.forEach((sparePart, quantity) -> {
            this.sparePartToOrderQuantity.put(sparePart, quantity);
            this.pendingSpareParts.add(sparePart);
        });
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
     * Returns the {@link OrderState} of this order
     *
     * @return the order state of this order
     */
    public @NotNull OrderState getState() {
        return this.orderState;
    }

    /**
     * Returns the {@link OrderState} of the specified spare part in this order or null,
     * if the spare part is not part of this order
     *
     * @param sparePart the spare part which order state should be checked
     * @return the order state of the specified spare part in this order or null
     */
    public @Nullable OrderState getState(@NotNull SparePart sparePart) {
        if (!this.sparePartToOrderQuantity.containsKey(sparePart)) {
            return null;
        }

        if (this.orderState == OrderState.ORDERED){
            return this.pendingSpareParts.contains(sparePart) ? OrderState.ORDERED : OrderState.RECEIVED;
        }
        return this.orderState;
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
     * Returns a copy of the ordered spare parts
     *
     * @return a copy of the ordered spare parts
     */
    @UnmodifiableView
    public @NotNull Set<SparePart> getSpareParts() {
        return Collections.unmodifiableSet(this.sparePartToOrderQuantity.keySet());
    }

    /**
     * Returns the order quantity to which the specified {@link SparePart} is mapped,
     * or {@code null} if this order does not contain the sparePart.
     *
     * @param sparePart to which the order quantity is asked
     * @return the order quantity of the specified sparePart
     */
    public @Nullable Integer getOrderQuantity(@NotNull SparePart sparePart) {
        return this.sparePartToOrderQuantity.get(sparePart);
    }

    /**
     * Returns the cost of this order
     *
     * @return the cost of this order
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Sets the cost of this order to the specified cost
     *
     * @param value the new cost of this order
     * @throws IllegalArgumentException if the specified cost is less then 0
     */
    public void setValue(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("cost must be greater equals 0 but is " + value);
        }
        this.value = value;
    }

    /**
     * Checks if this order can be received
     *
     * @return {@code true} if this order can be received, otherwise {@code false}
     */
    public boolean isReceivable() {
        return this.orderState == OrderState.ORDERED;
    }

    /**
     * Checks if the specified spare part can be received
     *
     * @param sparePart
     * @return {@code true} if specified spare part can be received, otherwise {@code false}
     */
    public boolean isReceivable(@NotNull SparePart sparePart) {
        return this.isReceivable() && this.pendingSpareParts.contains(sparePart);
    }

    /**
     * @param orderedSparePart the spare part which was received
     * @return the quantity of the order of the sparePart or null if the sparePart does not belong to this order
     * @throws IllegalStateException    if this order is already received or cancelled
     * @throws IllegalArgumentException if the specified sparePart were already received
     */
    public @Nullable Integer sparePartReceived(@NotNull SparePart orderedSparePart) {
        if (this.orderState == OrderState.RECEIVED) {
            throw new IllegalStateException("Order has already been received");
        }
        if (this.orderState == OrderState.CANCELLED) {
            throw new IllegalStateException("Order has already been cancelled");
        }
        if (!this.pendingSpareParts.remove(orderedSparePart)) {
            throw new IllegalArgumentException("spare part already received");
        }
        if (this.pendingSpareParts.isEmpty()) {
            this.orderState = OrderState.RECEIVED;
        }

        return this.sparePartToOrderQuantity.get(orderedSparePart);
    }

    /**
     * @return
     * @throws IllegalStateException
     */
    public @NotNull Map<SparePart, Integer> received() {
        if (this.orderState == OrderState.RECEIVED) {
            throw new IllegalStateException("Order has already been received");
        }
        if (this.orderState == OrderState.CANCELLED) {
            throw new IllegalStateException("Order has already been cancelled");
        }

        Map<SparePart, Integer> result = new HashMap<>();
        for (SparePart pendingSparePart : this.pendingSpareParts) {
            result.put(pendingSparePart, this.sparePartToOrderQuantity.get(pendingSparePart));
        }
        this.pendingSpareParts.clear();
        this.orderState = OrderState.RECEIVED;
        return result;
    }

    /**
     * @return
     */
    public boolean isCancellable() {
        return this.orderState == OrderState.ORDERED
                && this.pendingSpareParts.size() == this.sparePartToOrderQuantity.size();
    }

    /**
     *
     *
     * @throws IllegalStateException if the order is not cancellable
     */
    //TODO 24.05.2024 JavaDoc
    public void cancel() {
        if (!this.isCancellable()) {
            throw new IllegalStateException("Order is not cancellable");
        }

        this.orderState = OrderState.CANCELLED;
        this.pendingSpareParts.clear();
    }

    @Override
    public int compareTo(@NotNull Order o) {
        int result = this.id - o.id;
        if (result == 0) {
            result = this.supplier.compareTo(o.supplier);
        }
        if (result == 0) {
            result = Double.compare(this.value, o.value);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order other)) {
            return false;
        }
        return this.id == other.id
                && this.orderState == other.orderState
                && Objects.equals(this.orderDate, other.orderDate)
                && Objects.equals(this.supplier, other.supplier)
                && this.value == other.value
                && Objects.equals(this.sparePartToOrderQuantity, other.sparePartToOrderQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.orderDate, this.orderDate, this.supplier, this.value,
                this.sparePartToOrderQuantity);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + this.id +
                ", state=" + this.orderState +
                ", orderDate=" + this.orderDate +
                ", supplier=" + this.supplier +
                ", value=" + this.value +
                ", sparePartToOrderQuantity=" + this.sparePartToOrderQuantity +
                ", pendingSpareParts=" + this.pendingSpareParts +
                '}';
    }
}
