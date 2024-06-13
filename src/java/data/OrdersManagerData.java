package data;

import javafx.collections.ObservableMap;
import logic.manager.OrdersManager;
import logic.order.Order;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author xThe_white_Lionx
 * @Date 08.06.2024
 */
public class OrdersManagerData {

    private final Order[] orders;

    /**
     * The id for the next order
     */
    private final int nextOrderId;

    public OrdersManagerData(@NotNull OrdersManager ordersManager) {
        this.orders = ordersManager.getOrders().toArray(new Order[0]);
        this.nextOrderId = ordersManager.getNextOrderId();
    }

    public Order[] getOrders() {
        return this.orders;
    }

    public int getNextOrderId() {
        return this.nextOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof OrdersManagerData that)) return false;
        return this.nextOrderId == that.nextOrderId && Objects.deepEquals(this.orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(this.orders), this.nextOrderId);
    }

    @Override
    public String toString() {
        return "OrdersManagerData{" +
                "orders=" + Arrays.toString(this.orders) +
                ", nextOrderId=" + this.nextOrderId +
                '}';
    }
}
