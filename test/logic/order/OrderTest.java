package logic.order;

import logic.Condition;
import logic.Supplier;
import logic.sparePart.SparePart;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"MissingJavadoc", "ClassWithTooManyMethods"})
public class OrderTest {

    private static final Supplier EXAMPLE_SUPPLIER = new Supplier("supplier GmbH", URI.create("D"));

    @Test
    public void testSideEffectFree() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        sparePartToOrderQuantity.put(sparePart, -30);

        Assert.assertEquals(Integer.valueOf(1000), order.getOrderQuantity(sparePart));
    }

    @Test
    public void getState() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        Assert.assertEquals(Order.OrderState.ORDERED, order.getState());
    }

    @Test
    public void getState_receivedOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.received();

        Assert.assertEquals(Order.OrderState.RECEIVED, order.getState());
        Assert.assertEquals(Order.OrderState.RECEIVED, order.getState(sparePart));
    }

    @Test
    public void getState_forSparePart() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        Assert.assertEquals(Order.OrderState.ORDERED, order.getState(sparePart));
    }

    @Test
    public void getState_forReceivedSparePart() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.sparePartReceived(sparePart);

        Assert.assertEquals(Order.OrderState.RECEIVED, order.getState(sparePart));
    }

    @Test
    public void getOrderQuantity() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        Assert.assertEquals(Integer.valueOf(1000), order.getOrderQuantity(sparePart));
    }

    @Test
    public void isReceivable_orderedOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        Assert.assertTrue(order.isReceivable());
    }

    @Test
    public void isReceivable_receivedOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.received();

        Assert.assertFalse(order.isReceivable());
    }

    @Test
    public void isReceivable_allSparePartReceived() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.sparePartReceived(sparePart);

        Assert.assertFalse(order.isReceivable());
    }

    @Test
    public void isReceivable_cancelledOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.cancel();

        Assert.assertFalse(order.isReceivable());
    }

    @Test
    public void sparePartReceived() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        Assert.assertEquals(Integer.valueOf(1000), order.sparePartReceived(scrow));
        Assert.assertEquals(Order.OrderState.ORDERED, order.getState());
    }

    @Test
    public void sparePartReceived_allSparePartReceived() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", 0);
        sparePartToOrderQuantity.put(sparePart, 1000);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        Assert.assertEquals(Integer.valueOf(1000), order.sparePartReceived(sparePart));
        Assert.assertEquals(Order.OrderState.RECEIVED, order.getState());
    }

    @Test (expected = IllegalStateException.class)
    public void sparePartReceived_onCancelledOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.cancel();

        order.sparePartReceived(scrow);
    }

    @Test
    public void received() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        Assert.assertEquals(sparePartToOrderQuantity, order.received());
        Assert.assertEquals(Order.OrderState.RECEIVED, order.getState());
    }

    @Test
    public void received_someSparePartReceived() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.sparePartReceived(scrow);

        Assert.assertEquals(Map.of(wrench, 2), order.received());
        Assert.assertEquals(Order.OrderState.RECEIVED, order.getState());
    }

    @Test (expected = IllegalStateException.class)
    public void receivedOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.received();

        order.received();
    }

    @Test (expected = IllegalStateException.class)
    public void received_onCancelledOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.cancel();

        order.received();
    }

    @Test
    public void isCancellable() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);

        Assert.assertTrue(order.isCancellable());
    }

    @Test
    public void isCancellable_onCancelledOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.cancel();

        Assert.assertFalse(order.isCancellable());
    }

    @Test
    public void isCancellable_onReceivedOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.received();

        Assert.assertFalse(order.isCancellable());
    }

    @Test
    public void cancel() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.cancel();

        Assert.assertEquals(Order.OrderState.CANCELLED, order.getState());
    }

    @Test (expected = IllegalStateException.class)
    public void cancel_onCancelledOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.cancel();

        order.cancel();
    }

    @Test (expected = IllegalStateException.class)
    public void cancel_onReceivedOrder() {
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart scrow = new SparePart("scrow", Condition.NEW, "piece", "testCategory", 0);
        SparePart wrench = new SparePart("wrench", Condition.NEW, "piece", "testCategory",
                0);
        sparePartToOrderQuantity.put(scrow, 1000);
        sparePartToOrderQuantity.put(wrench, 2);
        Order order = new Order(1, LocalDate.now(), EXAMPLE_SUPPLIER, sparePartToOrderQuantity,
                200D);
        order.received();

        order.cancel();
    }
}