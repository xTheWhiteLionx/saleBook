package logic.manager;

import logic.Asset;
import logic.Condition;
import logic.FakeGui;
import logic.Supplier;
import logic.saleBook.SaleBook;
import logic.sparePart.SparePart;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

@SuppressWarnings("MissingJavadoc")
public class AssetsManagerTest {

    @Test
    public void getSumValue() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier GmbH", URI.create("D"));
        Asset asset = new Asset(1, "Treasure", supplier, LocalDate.now(), 13.99);
        Asset asset2 = new Asset(2, "Treasure2", supplier, LocalDate.now(), 14.50);
        AssetsManager assetsManager = saleBook.getAssetsManager();

        Assert.assertEquals(BigDecimal.ZERO, assetsManager.getSumValue());
        assetsManager.addAsset(asset);
        assetsManager.addAsset(asset2);
        Assert.assertEquals(BigDecimal.valueOf(28.49), assetsManager.getSumValue());
    }

    @Test
    public void addAsset() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier GmbH", URI.create("D"));
        Asset asset = new Asset(1, "Treasure", supplier, LocalDate.now(), 200D);
        AssetsManager assetsManager = saleBook.getAssetsManager();

        Assert.assertTrue(assetsManager.addAsset(asset));
        Assert.assertEquals(Set.of(asset), assetsManager.getAssets());
        Assert.assertEquals(BigDecimal.valueOf(200.0), assetsManager.getSumValue());
    }

    @Test
    public void removeAsset() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier GmbH", URI.create("D"));
        Asset asset = new Asset(1, "Treasure", supplier, LocalDate.now(),  200);
        AssetsManager assetsManager = saleBook.getAssetsManager();
        assetsManager.addAsset(asset);

        Assert.assertEquals(asset, assetsManager.removeAsset(1));
        Assert.assertEquals(Collections.emptySet(), assetsManager.getAssets());
        Assert.assertEquals(BigDecimal.valueOf(0.0), assetsManager.getSumValue());
    }
}