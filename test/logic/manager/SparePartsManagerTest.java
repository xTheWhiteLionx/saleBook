package logic.manager;

import junit.framework.TestCase;
import logic.Condition;
import logic.FakeGui;
import logic.saleBook.SaleBook;
import logic.sparePart.SparePart;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("MissingJavadoc")
public class SparePartsManagerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAddSparePart_negativeQuantity() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();

        sparePartsManager.addSparePart(sparePart, -3);
    }

    @Test
    public void testAddSparePart_addZero() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        sparePartsManager.addSparePart(sparePart,12);

        Assert.assertTrue(sparePartsManager.addSparePart(sparePart, 0));
        Assert.assertEquals(Integer.valueOf(12), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Set.of(sparePart), sparePartsManager.getSparePartsOfCategory(category));
        Assert.assertEquals(Set.of(name), sparePartsManager.getSparePartNames());
    }

    @Test
    public void testAddSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();

        Assert.assertTrue(sparePartsManager.addSparePart(sparePart, 12));
        Assert.assertEquals(Integer.valueOf(12), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Set.of(sparePart), sparePartsManager.getSparePartsOfCategory(category));
        Assert.assertEquals(Set.of(name), sparePartsManager.getSparePartNames());
    }

    @Test
    public void testAddSparePart_sameCategory() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        String name2 = "wrench";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePart sparePart2 = new SparePart(name2, Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();

        Assert.assertTrue(sparePartsManager.addSparePart(sparePart, 12));
        Assert.assertTrue(sparePartsManager.addSparePart(sparePart2, 2));
        Assert.assertEquals(Integer.valueOf(12), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Integer.valueOf(2), sparePartsManager.getQuantity(sparePart2));
        Assert.assertEquals(Set.of(sparePart, sparePart2),
                sparePartsManager.getSparePartsOfCategory(category));
        Assert.assertEquals(Set.of(name, name2), sparePartsManager.getSparePartNames());
    }

    @Test
    public void testAddSparePart_sameSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();

        Assert.assertTrue(sparePartsManager.addSparePart(sparePart, 12));
        Assert.assertTrue(sparePartsManager.addSparePart(sparePart, 10));
        Assert.assertEquals(Integer.valueOf(22), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Set.of(sparePart), sparePartsManager.getSparePartsOfCategory(category));
        Assert.assertEquals(Set.of(name), sparePartsManager.getSparePartNames());
    }

    @Test
    public void testAddSpareParts() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String category2 = "junk";
        String name = "scrow";
        String name2 = "wrench";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePart sparePart2 = new SparePart(name2, Condition.NEW, "piece", category2, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        Map<SparePart,Integer> sparePartIntegerMap = Map.of(sparePart, 12, sparePart2, 2);

        sparePartsManager.addSpareParts(sparePartIntegerMap);
        Assert.assertEquals(Integer.valueOf(12), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Integer.valueOf(2), sparePartsManager.getQuantity(sparePart2));
        Assert.assertEquals(Set.of(sparePart), sparePartsManager.getSparePartsOfCategory(category));
        Assert.assertEquals(Set.of(sparePart2), sparePartsManager.getSparePartsOfCategory(category2));
        Assert.assertEquals(Set.of(name, name2), sparePartsManager.getSparePartNames());
    }

    @Test
    public void testUseSparParts_notEnoughSpareParts() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        sparePartsManager.addSparePart(sparePart,12);
        Map<SparePart,Integer> sparePartIntegerMap = Map.of(sparePart, 20);

        Assert.assertFalse(sparePartsManager.useSparParts(sparePartIntegerMap));
        Assert.assertEquals(Integer.valueOf(12), sparePartsManager.getQuantity(sparePart));
    }

    @Test
    public void testUseSparParts_unknownSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePart sparePart2 = new SparePart("wrench", Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        sparePartsManager.addSparePart(sparePart,12);
        Map<SparePart,Integer> sparePartIntegerMap = Map.of(sparePart2, 12);

        Assert.assertFalse(sparePartsManager.useSparParts(sparePartIntegerMap));
    }

    @Test
    public void testUseSparParts() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePart sparePart2 = new SparePart("wrench", Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        sparePartsManager.addSparePart(sparePart,12);
        sparePartsManager.addSparePart(sparePart2, 7);
        Map<SparePart,Integer> sparePartIntegerMap = Map.of(sparePart,10, sparePart2, 2);

        Assert.assertTrue(sparePartsManager.useSparParts(sparePartIntegerMap));
        Assert.assertEquals(Integer.valueOf(2), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Integer.valueOf(5), sparePartsManager.getQuantity(sparePart2));
    }

    @Test
    public void testUseSparParts_useAllSpareParts() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePart sparePart2 = new SparePart("wrench", Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();
        sparePartsManager.addSparePart(sparePart,12);
        sparePartsManager.addSparePart(sparePart2, 7);
        Map<SparePart,Integer> sparePartIntegerMap = Map.of(sparePart,12, sparePart2, 7);

        Assert.assertTrue(sparePartsManager.useSparParts(sparePartIntegerMap));
        Assert.assertEquals(Integer.valueOf(0), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Integer.valueOf(0), sparePartsManager.getQuantity(sparePart2));
    }

    @Test
    public void testRemoveSparePart() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();

        sparePartsManager.addSparePart(sparePart, 12);

        Assert.assertTrue(sparePartsManager.removeSparePart(sparePart));
        Assert.assertNull(sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Collections.emptySet(), sparePartsManager.getSparePartsOfCategory(category));
        Assert.assertEquals(Collections.emptySet(), sparePartsManager.getSparePartNames());
    }

    @Test
    public void testRemoveSparePart_doesNotContain() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        String category = "testCategory";
        String name = "scrow";
        String name2 = "wrench";
        SparePart sparePart = new SparePart(name, Condition.NEW, "piece", category, null);
        SparePart sparePart2 = new SparePart(name2, Condition.NEW, "piece", category, null);
        SparePartsManager sparePartsManager = saleBook.getSparePartsManager();

        sparePartsManager.addSparePart(sparePart, 12);

        Assert.assertFalse(sparePartsManager.removeSparePart(sparePart2));
        Assert.assertEquals(Integer.valueOf(12), sparePartsManager.getQuantity(sparePart));
        Assert.assertEquals(Set.of(sparePart), sparePartsManager.getSparePartsOfCategory(category));
        Assert.assertEquals(Set.of(name), sparePartsManager.getSparePartNames());
    }
}