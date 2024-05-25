package logic.saleBook;

import javafx.scene.paint.Color;
import junit.framework.TestCase;
import logic.*;
import logic.order.Order;
import logic.products.item.Item;
import logic.products.item.ItemColor;
import logic.products.position.Position;
import logic.products.position.ShippingCompany;
import logic.sparePart.SparePart;
import org.junit.Assert;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SaleBookTest extends TestCase {


    public void testAddSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);

        Assert.assertTrue(saleBook.getSparePartsManager().addSparePart(sparePart));
        Assert.assertEquals(Set.of(sparePart), saleBook.getSparePartsManager().getSpareParts());
    }


    public void testRemoveSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        SparePart sparePart = new SparePart("Scrow", Condition.NEW, "piece",
                "testCategory", null);
        saleBook.getSparePartsManager().addSparePart(sparePart);

        Assert.assertTrue(saleBook.getSparePartsManager().removeSparePart(sparePart));
        Assert.assertTrue(saleBook.getSparePartsManager().getSpareParts().isEmpty());
    }


    public void testAddPosition() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Position position = new Position(1, "testCategory", LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ZERO);

        Assert.assertTrue(saleBook.getPositionsManager().addPosition(position));
        Assert.assertEquals(BigDecimal.TEN, saleBook.getVariableCosts());
    }


    public void testAddItemToPosition() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Position position = new Position(1, "testCategory", LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ZERO);
        saleBook.getPositionsManager().addPosition(position);
        ItemColor articleColor = ItemColor.getItemColor("testItemColor", Color.BLACK);
        Item item = new Item(1, Condition.NEW, Variant.LEFT, articleColor, "");

        Position expected = new Position(1, "testCategory", LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ZERO);
        expected.addItem(item);

        Assert.assertTrue(saleBook.getPositionsManager().addItemToPosition(1, item));
        Assert.assertEquals(expected, saleBook.getPositionsManager().removePosition(1));
    }


    public void testRemovePosition() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Position position = new Position(1, "testCategory", LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ZERO);
        saleBook.getPositionsManager().addPosition(position);

        Assert.assertEquals(position, saleBook.getPositionsManager().removePosition(1));
        Assert.assertEquals(BigDecimal.ZERO, saleBook.getVariableCosts());
    }


    public void testRemoveItem() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Position position = new Position(1, "testCategory", LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ZERO);
        ItemColor articleColor = ItemColor.getItemColor("testItemColor", Color.BLACK);
        Item item = new Item(1, Condition.NEW, Variant.LEFT, articleColor, "");
        Item item2 = new Item(2, Condition.NEW, Variant.RIGHT, articleColor, "");
        position.addItem(item);
        position.addItem(item2);
        saleBook.getPositionsManager().addPosition(position);

        Assert.assertEquals(item2, saleBook.getPositionsManager().removeItem(1, 2));
    }


    public void testAddCostToPosition() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Position position = new Position(1, "testCategory", LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ZERO);
        saleBook.getPositionsManager().addPosition(position);
        ItemColor articleColor = ItemColor.getItemColor("testItemColor", Color.BLACK);
        Item item = new Item(1, Condition.NEW, Variant.LEFT, articleColor, "");
        position.addItem(item);

        saleBook.getPositionsManager().addCostToPosition(1, BigDecimal.valueOf(15));

        Assert.assertEquals(BigDecimal.valueOf(25), saleBook.getVariableCosts());
    }


    public void testShipped() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Position position = new Position(1, "testCategory", LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ZERO);
        saleBook.getPositionsManager().addPosition(position);
        ItemColor articleColor = ItemColor.getItemColor("testItemColor", Color.BLACK);
        Item item = new Item(1, Condition.NEW, Variant.LEFT, articleColor, "");
        position.addItem(item);

        saleBook.getPositionsManager().shipped(1, ShippingCompany.DHL, "P1", BigDecimal.valueOf(5.49));

        Assert.assertEquals(BigDecimal.valueOf(15.49), saleBook.getVariableCosts());
    }

    public void testAddSupplier() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));

        Assert.assertTrue(saleBook.getSuppliersManager().addSupplier(supplier));
    }


    public void testRemoveSupplier() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        saleBook.getSuppliersManager().addSupplier(supplier);

        Assert.assertEquals(supplier, saleBook.getSuppliersManager().removeSupplier("supplier"));
    }


    public void testRemoveSupplier_noSupplierWithThisName() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        saleBook.getSuppliersManager().addSupplier(supplier);

        Assert.assertNull(saleBook.getSuppliersManager().removeSupplier("supplier GmbH"));
        Assert.assertFalse(saleBook.getSuppliersManager().getSuppliers().isEmpty());
    }


    public void testAddAsset() {
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        Asset asset = new Asset(1, "PC", supplier, LocalDate.now(), LocalDate.now(),
                BigDecimal.valueOf(100));
        SaleBook saleBook = new SaleBook(new FakeGui());

        Assert.assertTrue(saleBook.getAssetsManager().addAsset(asset));
    }


    public void testRemoveAsset() {
        Supplier supplier = new Supplier("supplier", URI.create("D"));
        Asset asset = new Asset(1, "PC", supplier, LocalDate.now(), LocalDate.now(),
                BigDecimal.valueOf(100));
        SaleBook saleBook = new SaleBook(new FakeGui());
        saleBook.getAssetsManager().addAsset(asset);

        Assert.assertEquals(asset, saleBook.getAssetsManager().removeAsset(1));
    }
}