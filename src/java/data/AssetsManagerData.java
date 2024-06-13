package data;

import logic.Asset;
import logic.manager.AssetsManager;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author xThe_white_Lionx
 * @Date 08.06.2024
 */
public class AssetsManagerData {

    private final Asset[] assets;

    private final int nextAssetId;

    public AssetsManagerData(AssetsManager assetsManager) {
        this.assets = assetsManager.getAssets().toArray(new Asset[0]);
        this.nextAssetId = assetsManager.getNextAssetId();
    }

    public Asset[] getAssets() {
        return this.assets.clone();
    }

    /**
     * Returns the id for the next asset
     *
     * @return the id for the next asset
     */
    public int getNextAssetId() {
        return this.nextAssetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof AssetsManagerData that)) return false;
        return this.nextAssetId == that.nextAssetId && Arrays.deepEquals(this.assets, that.assets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(this.assets), this.nextAssetId);
    }

    @Override
    public String toString() {
        return "AssetsManagerData{" +
                "assets=" + Arrays.toString(this.assets) +
                ", nextAssetId=" + this.nextAssetId +
                '}';
    }
}
