package logic.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import logic.SparePart;
import logic.order.Order;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class SparePartsManager {
    /**
     * ObservableSet of the spareParts of this saleBook
     */
    private final ObservableSet<SparePart> sparePartsObsSet;

    /**
     * Set of the name of the spareParts
     */
    private final Set<String> sparePartNames = new HashSet<>();
    /**
     * Set of units of the sparParts
     */
    private final Set<String> sparePartUnits = new HashSet<>();

    /**
     *
     */
    public SparePartsManager() {
        this.sparePartsObsSet = FXCollections.observableSet(new TreeSet<>());
    }

    /**
     *
     * @param spareParts
     */
    public SparePartsManager(SparePart[] spareParts) {
        Set<SparePart> sparePartSet = new TreeSet<>();
        for (SparePart sparePart : spareParts) {
            this.sparePartNames.add(sparePart.getName());
            this.sparePartUnits.add(sparePart.getUnit());
            sparePartSet.add(sparePart);
        }
        this.sparePartsObsSet = FXCollections.observableSet(sparePartSet);
    }

    public ObservableSet<SparePart> getSparePartsObsSet() {
        return this.sparePartsObsSet;
    }

    public Set<String> getSparePartNames() {
        return this.sparePartNames;
    }

    public Set<String> getSparePartUnits() {
        return this.sparePartUnits;
    }

    /**
     * Adds the specified sparePart to this sparePartManager
     *
     * @param sparePart the sparePart which should be added
     * @return true if the ...
     */
    //TODO 20.04.2024
    public boolean addSparePart(@NotNull SparePart sparePart) {
        boolean added = this.sparePartsObsSet.add(sparePart);
        if (added) {
            this.sparePartNames.add(sparePart.getName());
            this.sparePartUnits.add(sparePart.getUnit());
            return true;
        }
        return false;
    }

    /**
     * Removes the specified sparePart from this saleBook
     *
     * @param sparePart the sparePart which should be removed
     * @return true if ...
     */
    //TODO 20.04.2024
    public boolean removeSparePart(@NotNull SparePart sparePart) {
        boolean removed = this.sparePartsObsSet.remove(sparePart);
        if (removed) {
            String deletedName = sparePart.getName();
            String deletedUnit = sparePart.getUnit();
            boolean containsName = false;
            boolean containsUnit = false;
            Iterator<SparePart> iterator = this.sparePartsObsSet.iterator();
            while (iterator.hasNext() && (!containsName || !containsUnit)) {
                SparePart part = iterator.next();
                if (part.getName().equals(deletedName)) {
                    containsName = true;
                }
                if (sparePart.getUnit().equals(deletedUnit)) {
                    containsUnit = true;
                }
            }
            if (!containsName) {
                this.sparePartNames.remove(deletedName);
            }
            if (!containsUnit) {
                this.sparePartUnits.remove(deletedUnit);
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @param sparePartsToUseCount
     */
    //TODO 20.04.2024
    public void useSparParts(@NotNull Map<SparePart, Integer> sparePartsToUseCount) {
        sparePartsToUseCount = new HashMap<>(sparePartsToUseCount);
        Iterator<SparePart> iterator = this.sparePartsObsSet.iterator();
        while (!sparePartsToUseCount.isEmpty() && iterator.hasNext()){
            SparePart sparePart = iterator.next();
            Integer count = sparePartsToUseCount.remove(sparePart);
            if (count != null && count > 0) {
                sparePart.use(count);
            }
        }
    }


    /**
     *
     * @param newSparePart
     * @param orderQuantity
     */
    //TODO 20.04.2024
    public void storeSparePart(@NotNull SparePart newSparePart, @NotNull Integer orderQuantity) {
        for (SparePart sparePart : this.sparePartsObsSet) {
            if (sparePart.equals(newSparePart)) {
                sparePart.addQuantity(orderQuantity);
                break;
            }
        }
    }

    /**
     *
     * @param sparePartsToQuantity
     */
    //TODO 20.04.2024
    public void storeSpareParts(Map<SparePart, Integer> sparePartsToQuantity) {
        sparePartsToQuantity = new HashMap<>(sparePartsToQuantity);
        Iterator<SparePart> iterator = this.sparePartsObsSet.iterator();
        while (!sparePartsToQuantity.isEmpty() && iterator.hasNext()){
            SparePart sparePart = iterator.next();
            Integer quantity = sparePartsToQuantity.remove(sparePart);
            if (quantity != null && quantity > 0) {
                sparePart.addQuantity(quantity);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SparePartsManager that)) {
            return false;
        }
        return Objects.equals(this.sparePartsObsSet, that.sparePartsObsSet) &&
                Objects.equals(this.sparePartNames, that.sparePartNames) &&
                Objects.equals(this.sparePartUnits, that.sparePartUnits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sparePartsObsSet, this.sparePartNames, this.sparePartUnits);
    }

    @Override
    public String toString() {
        return "SparePartsManager{" +
                "sparePartsObsSet=" + this.sparePartsObsSet +
                ", sparePartNames=" + this.sparePartNames +
                ", sparePartUnits=" + this.sparePartUnits +
                '}';
    }
}
