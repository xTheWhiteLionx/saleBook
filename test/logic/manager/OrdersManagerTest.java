package logic.manager;

import logic.Condition;
import logic.FakeGui;
import logic.Supplier;
import logic.order.Order;
import logic.saleBook.SaleBook;
import logic.sparePart.SparePart;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("MissingJavadoc")
public class OrdersManagerTest {

    @Test
    public void testAddOrder() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece", "testCategory", null);
        sparePartToOrderQuantity.put(sparePart, 10);
        Order order = new Order(1, LocalDate.now(), supplier, sparePartToOrderQuantity, BigDecimal.valueOf(25));

        Assert.assertTrue(saleBook.getOrdersManager().addOrder(order));
        Assert.assertEquals(BigDecimal.valueOf(0), saleBook.getFixedCosts());
    }

    @Test
    public void testOrderReceived_fullOrder() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);
        sparePartToOrderQuantity.put(sparePart, 10);
        SparePart sparePart2 = new SparePart("batterie", Condition.NEW, "piece",
                "testCategory", null);
        sparePartToOrderQuantity.put(sparePart2, 15);
        Order order = new Order(1, LocalDate.now(), supplier, sparePartToOrderQuantity, BigDecimal.valueOf(25));
        saleBook.getOrdersManager().addOrder(order);

        saleBook.getOrdersManager().orderReceived(1);

        Assert.assertEquals(Order.OrderState.RECEIVED ,order.getState());
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        Assert.assertEquals(Set.of(sparePart, sparePart2), sparePartsManager.getSpareParts());
        Assert.assertEquals(Integer.valueOf(10), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Integer.valueOf(15), sparePartsManager.getQuantity(sparePart2));
        Assert.assertEquals(BigDecimal.valueOf(25), saleBook.getFixedCosts());
    }

    @Test
    public void testOrderReceived_halfOrder() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);
        sparePartToOrderQuantity.put(sparePart, 10);
        SparePart sparePart2 = new SparePart("batterie", Condition.NEW, "piece",
                "testCategory", null);
        sparePartToOrderQuantity.put(sparePart2, 15);
        Order order = new Order(1, LocalDate.now(), supplier, sparePartToOrderQuantity, BigDecimal.valueOf(25));
        saleBook.getOrdersManager().addOrder(order);
        saleBook.getOrdersManager().sparePartReceived(1, sparePart);
        saleBook.getOrdersManager().orderReceived(1);

        Assert.assertEquals(Order.OrderState.RECEIVED ,order.getState());
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        Assert.assertEquals(Set.of(sparePart, sparePart2), sparePartsManager.getSpareParts());
        Assert.assertEquals(Integer.valueOf(10), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Integer.valueOf(15), sparePartsManager.getQuantity(sparePart2));
        Assert.assertEquals(BigDecimal.valueOf(25), saleBook.getFixedCosts());
    }

    @Test
    public void testSparePartReceived_isNewSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);
        sparePartToOrderQuantity.put(sparePart, 10);
        Order order = new Order(1, LocalDate.now(), supplier, sparePartToOrderQuantity,
                BigDecimal.valueOf(25));
        saleBook.getOrdersManager().addOrder(order);
        SparePart expected = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);

        saleBook.getOrdersManager().sparePartReceived(1, sparePart);

        Assert.assertEquals(Order.OrderState.RECEIVED ,order.getState());
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        Assert.assertEquals(Set.of(expected), sparePartsManager.getSpareParts());
        Assert.assertEquals(Integer.valueOf(10), sparePartsManager.getQuantity(sparePart));
    }

    @Test
    public void testSparePartReceived_alreadyExistingSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);
        saleBook.getSparePartsManager().addSparePart(sparePart);
        sparePartToOrderQuantity.put(sparePart, 10);
        Order order = new Order(1, LocalDate.now(), supplier, sparePartToOrderQuantity,
                BigDecimal.valueOf(25));
        saleBook.getOrdersManager().addOrder(order);
        SparePart expected = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);

        saleBook.getOrdersManager().sparePartReceived(1, sparePart);

        Assert.assertEquals(Order.OrderState.RECEIVED ,order.getState());
        Assert.assertEquals(Set.of(expected), saleBook.getSparePartsManager().getSpareParts());
        Assert.assertEquals(Integer.valueOf(10), saleBook.getSparePartsManager().getQuantity(sparePart));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSparePartReceived_notOrderedSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        SparePart sparePart = new SparePart("scrow", Condition.NEW, "piece",
                "testCategory", null);
        SparePart sparePart2 = new SparePart("wrench", Condition.NEW, "piece",
                "testCategory", null);
        Map<SparePart, Integer> sparePartToOrderQuantity = Map.of(sparePart, 10);
        Order order = new Order(1, LocalDate.now(), supplier, sparePartToOrderQuantity, BigDecimal.valueOf(25));
        saleBook.getOrdersManager().addOrder(order);

        saleBook.getOrdersManager().sparePartReceived(1, sparePart2);
    }

    @Test
    public void testCancelOrder() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        Map<SparePart, Integer> sparePartToOrderQuantity = new HashMap<>();
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);
        sparePartToOrderQuantity.put(sparePart, 10);
        Order order = new Order(1, LocalDate.now(), supplier, sparePartToOrderQuantity, BigDecimal.valueOf(25));
        saleBook.getOrdersManager().addOrder(order);
        saleBook.getOrdersManager().cancelOrder(1);

        Assert.assertEquals(BigDecimal.ZERO, saleBook.getFixedCosts());
    }
}