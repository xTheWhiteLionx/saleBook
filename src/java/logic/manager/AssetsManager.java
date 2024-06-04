package logic.manager;

import gui.ObservableListMapBinder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import logic.Asset;
import gui.FXutils.FXCollectionsUtils;
import logic.GUIConnector;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.IterableUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;

/**
 * This class manages assets
 *
 * @author xthe_white_lionx
 */
public class AssetsManager extends AbstractManager implements ObservableListable<Asset> {

    /**
     * ObservableMap of assets mapped to their matching id
     */
    private final ObservableMap<Integer, Asset> idToAssetObsMap;

    /**
     * The id for the next asset
     */
    private int nextAssetId;

    /**
     *
     */
    //TODO 01.06.2024 JavaDoc
    private BigDecimal sumValue;

    /**
     * Constructor
     *
     * @param saleBook
     * @param gui      connection to the gui
     */
    public AssetsManager(@NotNull SaleBook saleBook, @NotNull GUIConnector gui) {
        super(saleBook, gui);
        this.idToAssetObsMap = FXCollections.observableMap(new TreeMap<>());
        this.nextAssetId = 1;
        this.setSumValue(BigDecimal.ZERO);
    }

    /**
     * Constructor
     *
     * @param saleBook
     * @param assets      the assets that should be
     * @param nextAssetId the id for the next asset
     * @param gui         connection to the gui
     * @throws IllegalArgumentException if the nextAssetId is negative
     */
    public AssetsManager(@NotNull SaleBook saleBook, @NotNull Asset[] assets, int nextAssetId, @NotNull GUIConnector gui) {
        super(saleBook, gui);
        if (nextAssetId <= 0) {
            throw new IllegalArgumentException(("nextAssetId must be greater equals 0 " +
                    "but is %d").formatted(nextAssetId));
        }

        this.idToAssetObsMap = FXCollectionsUtils.toObservableMap(assets, Asset::getId);
        this.nextAssetId = nextAssetId;
        BigDecimal sum = IterableUtils.reduce(this.idToAssetObsMap.values(), BigDecimal.ZERO,
                (asset, accu) -> accu.add(BigDecimal.valueOf(asset.getValue())));
        this.setSumValue(sum);
    }

    @Override
    public ObservableList<Asset> getObservableList() {
        return new ObservableListMapBinder<>(this.idToAssetObsMap).getObservableValuesList();
    }

    /**
     * Returns the assets of this assetManager
     *
     * @return the assets of this assetManager
     */
    public @NotNull Collection<Asset> getAssets() {
        return new TreeSet<>(this.idToAssetObsMap.values());
    }

    /**
     *
     * @return
     */
    public @NotNull BigDecimal getSumValue() {
        return this.sumValue;
    }

    /**
     * Returns the id for the next asset
     *
     * @return the id for the next asset
     */
    public int getNextAssetId() {
        return this.nextAssetId;
    }

    /**
     * Adds the specified asset to this assetManager if the asset with the id is not added yet.
     *
     * @param asset that should be added
     * @return true if the asset were added otherwise false
     */
    public boolean addAsset(@NotNull Asset asset) {
        Asset oldAsset = this.idToAssetObsMap.putIfAbsent(asset.getId(), asset);
        if (oldAsset == null) {
            this.setSumValue(BigDecimal.valueOf(asset.getValue()), BigDecimal::add);
            this.gui.updateStatus(String.format("asset %d added", asset.getId()));
            this.nextAssetId++;
            return true;
        }
        return false;
    }

    /**
     * Removes the asset with the specified assetId
     *
     * @param assetId the id of the asset that should be removed
     * @return the removed asset or null if no asset with the specified asset id exist.
     */
    public @Nullable Asset removeAsset(int assetId) {
        Asset removedAsset = this.idToAssetObsMap.remove(assetId);
        if (removedAsset != null) {
            this.setSumValue(BigDecimal.valueOf(removedAsset.getValue()), BigDecimal::subtract);
            this.gui.updateStatus(String.format("asset %d deleted", assetId));
        }
        return removedAsset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetsManager that)) return false;
        return this.nextAssetId == that.nextAssetId
                && Objects.equals(this.idToAssetObsMap, that.idToAssetObsMap)
                && (this.sumValue == null ? that.sumValue == null : this.sumValue.compareTo(that.sumValue) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idToAssetObsMap, this.nextAssetId, this.sumValue);
    }

    @Override
    public String toString() {
        return "AssetsManager{" +
                "idToAssetObsMap=" + this.idToAssetObsMap +
                ", nextAssetId=" + this.nextAssetId +
                ", sumValue=" + this.sumValue +
                '}';
    }

    /**
     *
     * @param sumValue
     */
    private void setSumValue(BigDecimal sumValue) {
        this.sumValue = sumValue;
        this.gui.displaySumAssetsValue(sumValue);
    }

    /**
     *
     * @param rightOperand
     */
    private void setSumValue(BigDecimal rightOperand,
                             BiFunction<? super BigDecimal, ? super BigDecimal, ? extends BigDecimal> biFunction) {
        this.setSumValue(biFunction.apply(this.sumValue, rightOperand));
    }
}
