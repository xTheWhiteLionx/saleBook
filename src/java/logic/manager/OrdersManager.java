package logic.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import logic.order.Order;
import gui.FXutils.FXCollectionsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.TreeMap;

/**
 *
 * @author xthe_white_lionx
 */
public class OrdersManager {

    /**
     * ObservableMap of orders mapped to their matching id
     */
    private final ObservableMap<Integer, Order> idToOrderObsMap;

    /**
     * The id for the next order
     */
    private int nextOrderId;

    /**
     * Constructor
     */
    public OrdersManager() {
        this.idToOrderObsMap = FXCollections.observableMap(new TreeMap<>());
        this.nextOrderId = 1;
    }

    /**
     * Constructor
     *
     * @param orders the orders that should be managed
     * @param nextOrderId the id for the next order
     * @throws IllegalArgumentException if the nextOrderId is negative
     */
    public OrdersManager(Order[] orders, int nextOrderId){
        if (nextOrderId < 0) {
            throw new IllegalArgumentException("nextOrderId must be greater equals 0 but is %d".formatted(nextOrderId));
        }

        this.idToOrderObsMap = FXCollectionsUtils.toObservableMap(orders, Order::getId);
        this.nextOrderId = nextOrderId;
    }

    /**
     *
     * @param orderId
     * @return
     */
    public @Nullable Order getOrder(int orderId){
        return this.idToOrderObsMap.get(orderId);
    }

    /**
     *
     * @return
     */
    public @NotNull ObservableMap<Integer, Order> getIdToOrderObsMap() {
        return this.idToOrderObsMap;
    }

    /**
     * Returns the orders of this ordersManager
     *
     * @return the orders of this ordersManager
     */
    public @NotNull Collection<Order> getOrders() {
        return this.idToOrderObsMap.values();
    }

    /***
     * Returns the id for the next creation of an order
     *
     * @return the id for the next creation of an order
     */
    public int getNextOrderId() {
        return this.nextOrderId;
    }

    /**
     * Adds the specified order
     *
     * @param order the order which should be added
     */
    public boolean addOrder(@NotNull Order order) {
        Order oldOrder = this.idToOrderObsMap.putIfAbsent(order.getId(), order);
        if (oldOrder == null) {
            this.nextOrderId++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Consumes the order with the specified orderId
     *
     * @param orderId the id of the searched order
     */
    public @Nullable Order removeOrder(int orderId) {
        return this.idToOrderObsMap.remove(orderId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdersManager that)) {
            return false;
        }
        return Objects.equals(this.idToOrderObsMap, that.idToOrderObsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idToOrderObsMap);
    }

    @Override
    public String toString() {
        return "OrdersManager{" +
                "idToOrderObsMap=" + this.idToOrderObsMap +
                '}';
    }
}
