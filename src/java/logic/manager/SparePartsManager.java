package logic.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import logic.GUIConnector;
import logic.saleBook.SaleBook;
import logic.sparePart.SparePart;
import logic.sparePart.SparePartData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 * @author
 */
public class SparePartsManager {
    /**
     * ObservableMap of the spareParts of this saleBook
     */
    private final ObservableMap<SparePart, Integer> sparePartsToQuantityObsMap;

    /**
     * ObservableMap of the spareParts of this saleBook
     */
    private final Map<String, Set<SparePart>> categoryToSpareParts;

    /**
     * Set of the name of the spareParts
     */
    private final Set<String> sparePartNames;

    /**
     * Set of units of the sparParts
     */
    private final Set<String> sparePartUnits;

    /**
     *
     */
    private final SaleBook saleBook;

    /**
     *
     */
    private final GUIConnector gui;

    /**
     * @param saleBook
     * @param gui
     */
    public SparePartsManager(@NotNull SaleBook saleBook, @NotNull GUIConnector gui) {
        this.saleBook = saleBook;
        this.gui = gui;
        this.sparePartNames = new HashSet<>();
        this.sparePartUnits = new HashSet<>();
        this.categoryToSpareParts = new TreeMap<>();
        this.sparePartsToQuantityObsMap = FXCollections.observableMap(new TreeMap<>());
    }

    /**
     * @param saleBook
     * @param sparePartData
     * @param gui
     */
    public SparePartsManager(@NotNull SaleBook saleBook, @NotNull SparePartData[] sparePartData,
                             @NotNull GUIConnector gui) {
        this(saleBook, gui);
        for (SparePartData sparePartDate : sparePartData) {
            SparePart sparePart = new SparePart(sparePartDate);
            this.addSparePart(sparePart, sparePartDate.getQuantity());
        }
    }

    /**
     * @return
     */
    public ObservableMap<SparePart, Integer> getSparePartsToQuantityMap() {
        return this.sparePartsToQuantityObsMap;
    }

    /**
     * Returns a copy of the spareParts of this sparePartsManager
     *
     * @return a copy of the spareParts of this sparePartsManager
     */
    public @NotNull Set<SparePart> getSpareParts() {
        return new TreeSet<>(this.sparePartsToQuantityObsMap.keySet());
    }

    /**
     * @return
     */
    @UnmodifiableView
    public Set<String> getSparePartNames() {
        return Collections.unmodifiableSet(this.sparePartNames);
    }

    /**
     * @return
     */
    @UnmodifiableView
    public Set<String> getSparePartUnits() {
        return Collections.unmodifiableSet(this.sparePartUnits);
    }

    /**
     * @param category
     * @return
     */
    public @Nullable Set<SparePart> getSparePartsOfCategory(String category) {
        return new HashSet<>(this.categoryToSpareParts.get(category));
    }

    /**
     * @return
     */
    public @NotNull SparePartData[] toSparePartData() {
        SparePartData[] sparePartData = new SparePartData[this.sparePartsToQuantityObsMap.size()];
        int i = 0;
        for (Map.Entry<SparePart, Integer> sparePartIntegerEntry : this.sparePartsToQuantityObsMap.entrySet()) {
            SparePart key = sparePartIntegerEntry.getKey();
            Integer value = sparePartIntegerEntry.getValue();
            sparePartData[i++] = new SparePartData(key, value);
        }
        return sparePartData;
    }

    /**
     * Returns the quantity of the specified spare part
     *
     * @param sparePart the sparePart of which the quantity should be found
     * @return the quantity of the specified spare part or null if the spare part is not
     * registered
     */
    public @Nullable Integer getQuantity(@NotNull SparePart sparePart) {
        return this.sparePartsToQuantityObsMap.get(sparePart);
    }

    /**
     * Adds the specified sparePart to this sparePartManager
     *
     * @param sparePart the sparePart which should be added
     * @return true if the ...
     */
    //TODO 20.04.2024
    public boolean addSparePart(@NotNull SparePart sparePart) {
        return this.addSparePart(sparePart, 0);
    }

    /**
     * Adds the specified newSparePart to this sparePartManager
     *
     * @param newSparePart the newSparePart which should be added
     * @return true if the ...
     */
    //TODO 20.04.2024
    public boolean addSparePart(@NotNull SparePart newSparePart, int quantity) {
        Integer oldQuantity = this.sparePartsToQuantityObsMap.get(newSparePart);
        boolean added = this.add(newSparePart, quantity);
        //checks if the sparePart was already stored if not there is no matching quantity
        if (oldQuantity == null) {
            this.gui.displaySparePartNames(this.sparePartNames);
            this.gui.updateStatus(String.format("spare part %s successfully added", newSparePart.getName()));
        } else {
            this.gui.refreshSpareParts();
            this.gui.updateStatus(String.format("quantity %d successfully added", quantity));
        }
        return added;
    }

    /**
     * @param sparePartsToQuantity
     */
    //TODO 20.04.2024
    public void addSpareParts(Map<SparePart, Integer> sparePartsToQuantity) {
        for (Map.Entry<SparePart, Integer> entry : sparePartsToQuantity.entrySet()) {
            this.add(entry.getKey(), entry.getValue());
        }
        this.gui.refreshSpareParts();
    }

    /**
     * @param sparePartsToUseCount
     * @throws IllegalArgumentException if a specific spare part is unknown
     */
    //TODO 20.04.2024
    public boolean useSparParts(@NotNull Map<SparePart, Integer> sparePartsToUseCount) {
        boolean allUsed = true;

        for (Map.Entry<SparePart, Integer> entry : sparePartsToUseCount.entrySet()) {
            SparePart sparePart = entry.getKey();
            Integer value = entry.getValue();
            if (value != null) {
                Integer stock = this.sparePartsToQuantityObsMap.get(sparePart);
                if (stock != null && stock >= value) {
                    this.sparePartsToQuantityObsMap.put(sparePart, stock - value);
                } else {
                    allUsed = false;
                }
            }
        }
        this.gui.refreshSpareParts();
        return allUsed;
    }

    /**
     * Removes the specified sparePart from this sparePartManager if it contains the specified sparePart.
     *
     * @param sparePart the sparePart which should be removed
     * @return {@code true} if the sparePart was successfully removed,
     * otherwise {@code false} if this sparePartManager does not contain the specified sparePart
     */
    public boolean removeSparePart(@NotNull SparePart sparePart) {
        boolean removed = this.sparePartsToQuantityObsMap.remove(sparePart) != null;
        if (removed) {
            String deletedName = sparePart.getName();
            String deletedUnit = sparePart.getUnit();
            boolean containsName = false;
            boolean containsUnit = false;
            Iterator<SparePart> iterator = this.sparePartsToQuantityObsMap.keySet().iterator();
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
                this.gui.displaySparePartNames(this.sparePartNames);
            }
            if (!containsUnit) {
                this.sparePartUnits.remove(deletedUnit);
            }
            this.categoryToSpareParts.get(sparePart.getCategory()).remove(sparePart);

            this.gui.updateStatus(String.format("spare part %s successfully deleted",
                    sparePart.getName()));
        }
        return removed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SparePartsManager that)) {
            return false;
        }
        return Objects.equals(this.sparePartsToQuantityObsMap, that.sparePartsToQuantityObsMap) &&
                Objects.equals(this.sparePartNames, that.sparePartNames) &&
                Objects.equals(this.sparePartUnits, that.sparePartUnits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sparePartsToQuantityObsMap, this.sparePartNames, this.sparePartUnits);
    }

    @Override
    public String toString() {
        return "SparePartsManager{" +
                "sparePartsObsSet=" + this.sparePartsToQuantityObsMap +
                ", sparePartNames=" + this.sparePartNames +
                ", sparePartUnits=" + this.sparePartUnits +
                '}';
    }

    /**
     * @param newSparePart
     * @param quantity
     * @return
     */
    private boolean add(@NotNull SparePart newSparePart, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater equals 0 but is %d".formatted(quantity));
        }

        Integer oldValue = this.sparePartsToQuantityObsMap.get(newSparePart);
        this.sparePartsToQuantityObsMap.merge(newSparePart, quantity, Integer::sum);
        if (oldValue == null) {
            this.sparePartUnits.add(newSparePart.getUnit());
            this.sparePartNames.add(newSparePart.getName());
            Set<SparePart> spareParts = this.categoryToSpareParts.computeIfAbsent(newSparePart.getCategory(), k -> new HashSet<>());
            spareParts.add(newSparePart);
        }

        return true;
    }
}
