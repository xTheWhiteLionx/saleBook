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
    public void addAsset() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier GmbH", URI.create("D"));
        Asset asset = new Asset(1, "Treasure", supplier, LocalDate.now(), null, BigDecimal.valueOf(200));
        AssetsManager assetsManager = saleBook.getAssetsManager();

        Assert.assertTrue(assetsManager.addAsset(asset));
        Assert.assertEquals(Set.of(asset), assetsManager.getAssets());
    }

    @Test
    public void removeAsset() {
        SaleBook saleBook = new SaleBook(new FakeGui());
        Supplier supplier = new Supplier("supplier GmbH", URI.create("D"));
        Asset asset = new Asset(1, "Treasure", supplier, LocalDate.now(), null, BigDecimal.valueOf(200));
        AssetsManager assetsManager = saleBook.getAssetsManager();
        assetsManager.addAsset(asset);

        Assert.assertEquals(asset, assetsManager.removeAsset(1));
        Assert.assertEquals(Collections.emptySet(), assetsManager.getAssets());
    }
}