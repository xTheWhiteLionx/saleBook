package data;

import logic.manager.SparePartsManager;
import logic.sparePart.SparePart;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * @author xthe_white_lionx
 * @date 08.06.2024
 */
public class SparePartsManagerData {

    /**
     * Spare parts of this saleBookData
     */
    private final SparePartData[] sparePartData;


    public SparePartsManagerData(@NotNull SparePartsManager sparePartsManager) {

        Set<SparePart> spareParts = sparePartsManager.getSpareParts();
        this.sparePartData = new SparePartData[spareParts.size()];
        int i = 0;
        for (SparePart sparePart : spareParts) {
            this.sparePartData[i++] = new SparePartData(sparePart,
                    sparePartsManager.getQuantity(sparePart));
        }
    }

    public SparePartData[] getSparePartData() {
        return this.sparePartData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SparePartsManagerData that)) {
            return false;
        }
        return Arrays.deepEquals(this.sparePartData, that.sparePartData);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.sparePartData);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SparePartsManagerData{");
        sb.append("sparePartData=").append(Arrays.toString(this.sparePartData));
        sb.append('}');
        return sb.toString();
    }

}
