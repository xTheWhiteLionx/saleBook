package logic.manager;

import logic.FakeGui;
import logic.Supplier;
import logic.saleBook.SaleBook;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;
@SuppressWarnings("MissingJavadoc")
public class SuppliersManagerTest {

    @Test
    public void addSupplier() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier GmbH", URI.create("D"));
        SuppliersManager suppliersManager = saleBook.getSuppliersManager();

        Assert.assertTrue(suppliersManager.addSupplier(supplier));
        Assert.assertEquals(Set.of(supplier), suppliersManager.getSuppliers());
    }

    @Test
    public void removeSupplier() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier GmbH", URI.create("D"));
        SuppliersManager suppliersManager = saleBook.getSuppliersManager();

        suppliersManager.addSupplier(supplier);
        Assert.assertEquals(supplier, suppliersManager.removeSupplier(supplier.getName()));
        Assert.assertEquals(Collections.emptySet(), suppliersManager.getSuppliers());
    }
}