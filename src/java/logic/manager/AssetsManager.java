package logic.manager;

import data.AssetsManagerData;
import costumeClasses.FXClasses.ObservableListMapBinder;
import data.Dataable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import logic.Asset;
import logic.GUIConnector;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;

/**
 * This class manages {@link Asset assets}.
 *
 * @author xthe_white_lionx
 */
public class AssetsManager extends AbstractManager implements Dataable<AssetsManagerData>,
        ObservableListable<Asset> {

    /**
     * ObservableMap of assets mapped to their matching id
     */
    private final ObservableMap<Integer, Asset> idToAssetObsMap;

    /**
     * The id for the next asset
     */
    private int nextAssetId;

    /**
     * The sum of the value of all assets
     */
    private BigDecimal sumValue;

    /**
     * Constructor
     *
     * @param saleBook connection to the saleBook
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
     * @param saleBook connection to the saleBook
     * @param assetsManagerData
     * @param gui         connection to the gui
     * @throws IllegalArgumentException if the nextAssetId is negative
     */
    public AssetsManager(@NotNull SaleBook saleBook, @NotNull AssetsManagerData assetsManagerData,
                         @NotNull GUIConnector gui) {
        super(saleBook, gui);
        this.idToAssetObsMap = FXCollections.observableMap(new TreeMap<>());
        this.sumValue = BigDecimal.ZERO;
        for (Asset asset : assetsManagerData.getAssets()) {
            this.idToAssetObsMap.put(asset.getId(), asset);
            this.sumValue = BigDecimal.valueOf(asset.getValue()).add(this.sumValue);
        }
        this.nextAssetId = assetsManagerData.getNextAssetId();
        this.gui.displaySumAssetsValue(this.sumValue);
    }

    @Override
    public AssetsManagerData toData() {
        return new AssetsManagerData(this);
    }

    @Override
    public ObservableList<Asset> getObservableList() {
        return new ObservableListMapBinder<>(this.idToAssetObsMap).getObservableValuesList();
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
     * Returns the assets of this assetManager
     *
     * @return the assets of this assetManager
     */
    public @NotNull Collection<Asset> getAssets() {
        return new TreeSet<>(this.idToAssetObsMap.values());
    }

    /**
     * Returns the sum of the values of all assets
     *
     * @return the sum of the values of all assets
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

    /**
     * Sets the sum value and displays it in the gui
     *
     * @param sumValue the new sum value
     */
    private void setSumValue(BigDecimal sumValue) {
        this.sumValue = sumValue;
        this.gui.displaySumAssetsValue(sumValue);
    }

    /**
     * Sets the sum value to the result of the biFunction. As operands for the function the
     * current sumValue and the specified rightOperand will be used.
     *
     * @param rightOperand the right operand for the specified biFunction
     */
    private void setSumValue(BigDecimal rightOperand,
                             BiFunction<? super BigDecimal, ? super BigDecimal, ? extends BigDecimal> biFunction) {
        this.setSumValue(biFunction.apply(this.sumValue, rightOperand));
    }
}
