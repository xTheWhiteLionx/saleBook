package logic.manager;

import data.Dataable;
import data.OrdersManagerData;
import gui.FXutils.FXCollectionsUtils;
import costumeClasses.FXClasses.ObservableListMapBinder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import logic.GUIConnector;
import logic.order.Order;
import logic.saleBook.SaleBook;
import logic.sparePart.SparePart;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This class manages orders
 *
 * @author xthe_white_lionx
 */
public class OrdersManager extends AbstractManager implements Dataable<OrdersManagerData>,
        ObservableListable<Order> {

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
     *
     * @param saleBook
     * @param gui
     */
    public OrdersManager(@NotNull SaleBook saleBook, @NotNull GUIConnector gui) {
        super(saleBook, gui);
        this.idToOrderObsMap = FXCollections.observableMap(new TreeMap<>());
        this.nextOrderId = 1;
    }

//    /**
//     * Constructor
//     *
//     * @param saleBook    connection to the saleBook
//     * @param orders      the orders that should be managed
//     * @param nextOrderId the id for the next order
//     * @param gui         connection to the gui
//     * @throws IllegalArgumentException if the nextOrderId is negative or 0
//     */
//    public OrdersManager(@NotNull SaleBook saleBook, Order[] orders, int nextOrderId,
//                         @NotNull GUIConnector gui) {
//        super(saleBook, gui);
//        if (nextOrderId < 1) {
//            throw new IllegalArgumentException("nextOrderId must be greater equals 1 but is %d".formatted(nextOrderId));
//        }
//
//        this.idToOrderObsMap = FXCollectionsUtils.toObservableMap(orders, Order::getId);
//        this.nextOrderId = nextOrderId;
//    }

    /**
     * Constructor
     *
     * @param saleBook    connection to the saleBook
     * @param gui         connection to the gui
     * @throws IllegalArgumentException if the nextOrderId is negative or 0
     */
    public OrdersManager(@NotNull SaleBook saleBook, @NotNull GUIConnector gui,
                         @NotNull OrdersManagerData ordersManagerData) {
        super(saleBook, gui);
        this.idToOrderObsMap = FXCollectionsUtils.toObservableMap(ordersManagerData.getOrders(),
                Order::getId);
        this.nextOrderId = ordersManagerData.getNextOrderId();
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
     * @return {@code true} if the order was successfully added, otherwise {@code false}
     */
    public boolean addOrder(@NotNull Order order) {
        Order oldOrder = this.idToOrderObsMap.putIfAbsent(order.getId(), order);
        if (oldOrder == null) {
            this.nextOrderId++;
            this.gui.updateStatus(String.format("order %d added", order.getId()));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Consumes the order with the specified orderId and stores its spare parts
     *
     * @param orderId the id of the searched order
     * @throws IllegalArgumentException if there is no order with the specified orderId
     * @throws IllegalStateException    if the order is not receivable
     */
    public void orderReceived(int orderId) {
        Order order = this.idToOrderObsMap.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        this.saleBook.getSparePartsManager().addSpareParts(order.received());
        this.saleBook.addFixedCost(order.getValue());
        this.updateDisplayOrder(order);
        this.gui.updateStatus(String.format("order %d received", orderId));
    }

    /**
     * Consumes a spare part of the order with the specified orderId
     *
     * @param orderId          the id of the order from which the spare part should be consumed
     * @param orderedSparePart the spare part which was received
     * @throws IllegalArgumentException if there is no order with the specified orderId
     */
    public void sparePartReceived(int orderId, @NotNull SparePart orderedSparePart) {
        Order order = this.idToOrderObsMap.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        Integer orderQuantity = order.sparePartReceived(orderedSparePart);
        if (orderQuantity != null && orderQuantity > 0) {
            this.saleBook.getSparePartsManager().addSparePart(orderedSparePart, orderQuantity);

            String updateMessage;
            if (order.getState() == Order.OrderState.RECEIVED) {
                this.updateDisplayOrder(order);
                updateMessage = String.format("order %d received", orderId);
            } else {
                this.gui.displayOrderedSpareParts(order.getSpareParts());
                updateMessage = String.format("%s of order %d received",
                        orderedSparePart.getName(), orderId);
            }
            this.gui.updateStatus(updateMessage);
        }
    }

    /**
     * Cancels the order with the specified orderId
     *
     * @param orderId the id of the order which should be canceled
     * @throws IllegalArgumentException if there is no order with the specified orderId
     */
    public void cancelOrder(int orderId) {
        Order order = this.idToOrderObsMap.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        order.cancel();
        this.updateDisplayOrder(order);
        this.gui.updateStatus(String.format("order %d cancelled", orderId));
    }

    @Override
    public OrdersManagerData toData() {
        return new OrdersManagerData(this);
    }

    @Override
    public ObservableList<Order> getObservableList() {
        return new ObservableListMapBinder<>(this.idToOrderObsMap).getObservableValuesList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof OrdersManager that)) {
            return false;
        }
        return this.nextOrderId == that.nextOrderId &&
                Objects.equals(this.idToOrderObsMap, that.idToOrderObsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idToOrderObsMap, this.nextOrderId);
    }

    @Override
    public String toString() {
        return "OrdersManager{" + "idToOrderObsMap=" + this.idToOrderObsMap +
                ", nextOrderId=" + this.nextOrderId +
                '}';
    }

    /**
     * @param order
     */
    private void updateDisplayOrder(Order order) {
        this.gui.displayOrderedSpareParts(order.getSpareParts());
        this.gui.refreshOrders();
    }
}
