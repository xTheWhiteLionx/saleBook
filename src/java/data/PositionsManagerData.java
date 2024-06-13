package data;

import logic.manager.PositionsManager;
import logic.products.position.PositionData;
import utils.CollectionsUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author xthe_white_lionx
 * @date 08.06.2024
 */
public class PositionsManagerData {

    private final PositionData[] positionsData;

    /**
     * the id for the next creation of a position
     */
    private int nextPosId;

    public PositionsManagerData(PositionsManager positionsManager) {
        this.positionsData = CollectionsUtils.toArray(positionsManager.getPositions(),
                PositionData::new, new PositionData[0]);
        this.nextPosId = positionsManager.getNextPosId();
    }

    public PositionData[] getPositionsData() {
        return this.positionsData;
    }

    public int getNextPosId() {
        return this.nextPosId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionsManagerData that)) {
            return false;
        }
        return this.nextPosId == that.nextPosId
                && Arrays.deepEquals(this.positionsData, that.positionsData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(this.positionsData), this.nextPosId);
    }

    @Override
    public String toString() {
        return "PositionsManagerData{" +
                "positionsData=" + Arrays.toString(this.positionsData) +
                ", nextPosId=" + this.nextPosId +
                '}';
    }
}
