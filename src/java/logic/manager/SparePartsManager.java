package logic.manager;

import data.SparePartsManagerData;
import costumeClasses.FXClasses.ObservableListMapBinder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import data.Dataable;
import logic.GUIConnector;
import logic.saleBook.SaleBook;
import logic.sparePart.SparePart;
import data.SparePartData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 *
 *
 * @author xThe_white_Lionx
 */
public class SparePartsManager extends AbstractManager implements Dataable<SparePartsManagerData>,
        ObservableListable<SparePart> {
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
     * Constructor
     *
     * @param saleBook the connection to the current saleBook
     * @param gui the connection to the current gui
     */
    public SparePartsManager(@NotNull SaleBook saleBook, @NotNull GUIConnector gui) {
        super(saleBook, gui);
        this.sparePartNames = new HashSet<>();
        this.sparePartUnits = new HashSet<>();
        this.categoryToSpareParts = new TreeMap<>();
        this.sparePartsToQuantityObsMap = FXCollections.observableMap(new TreeMap<>());
    }

    /**
     * Constructor
     *
     * @param saleBook the connection to the current saleBook
     * @param sparePartData the data of the spare parts
     * @param gui the connection to the current gui
     */
    public SparePartsManager(@NotNull SaleBook saleBook,
                             @NotNull GUIConnector gui, @NotNull SparePartData[] sparePartData) {
        this(saleBook, gui);
        for (SparePartData sparePartDate : sparePartData) {
            SparePart sparePart = new SparePart(sparePartDate);
            this.add(sparePart, sparePartDate.getQuantity());
        }

    }

    /**
     * Constructor
     *
     * @param saleBook the connection to the current saleBook
     * @param gui the connection to the current gui
     */
    public SparePartsManager(@NotNull SaleBook saleBook,
                             @NotNull GUIConnector gui,
                             @NotNull SparePartsManagerData sparePartsManagerData) {
        this(saleBook, gui);
        for (SparePartData sparePartDate : sparePartsManagerData.getSparePartData()) {
            SparePart sparePart = new SparePart(sparePartDate);
            this.add(sparePart, sparePartDate.getQuantity());
        }

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
     * Returns the names of the spare parts
     *
     * @return the names of the spare parts
     */
    @UnmodifiableView
    public Set<String> getSparePartNames() {
        return Collections.unmodifiableSet(this.sparePartNames);
    }

    /**
     * Returns the units of the spare parts
     *
     * @return the units of the spare parts
     */
    @UnmodifiableView
    public Set<String> getSparePartUnits() {
        return Collections.unmodifiableSet(this.sparePartUnits);
    }

    /**
     * Returns the spare parts for the specified category
     *
     * @param category the category for which the matching spare parts should be seeked
     * @return the sparePart of the category or null if the category is unknown
     */
    public @Nullable Set<SparePart> getSparePartsOfCategory(String category) {
        Set<SparePart> spareParts = this.categoryToSpareParts.get(category);
        return spareParts != null ?
                new HashSet<>(this.categoryToSpareParts.get(category)) : null;
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
     * Adds the specified sparePart to this sparePartManager with the quantity one, if the spare
     * part is already known to this sparePartManager one will be added to the current quantity
     *
     * @param sparePart the sparePart which should be added
     * @return true if the spare part was successfully added
     */
    public boolean addSparePart(@NotNull SparePart sparePart) {
        return this.addSparePart(sparePart, 1);
    }

    /**
     * Adds the specified spare part to this sparePartManager.
     * If the sparePart is already known the specified quantity will be added to the stock
     * otherwise the new spare part will be added and mapped to the specified quantity.
     * Updates the gui.
     *
     * @param newSparePart the "new" spare part which should be added
     * @param quantity     the quantity which should be added
     * @return {@code true} if the specified spare part was successfully added,
     * otherwise {@code false}
     * @throws IllegalArgumentException if the quantity is less than 0
     */
    public boolean addSparePart(@NotNull SparePart newSparePart, int quantity) {
        Integer oldQuantity = this.sparePartsToQuantityObsMap.get(newSparePart);
        boolean added = this.add(newSparePart, quantity);
        //checks if the sparePart was already stored if not there is no matching quantity and it
        // is a new spare part to this sparePartManager
        if (oldQuantity == null) {
            this.gui.displaySparePartNames(this.sparePartNames);
            this.gui.updateStatus(String.format("spare part %s successfully added",
                    newSparePart.getName()));
        } else {
            this.gui.refreshSpareParts();
            this.gui.updateStatus(String.format("quantity %d successfully added", quantity));
        }
        return added;
    }

    /**
     * Adds the specified map of spare part to their quantity to this sparePartManager.
     * If the sparePart is already known the specified quantity will be added to the stock
     * otherwise the new spare part will be added and mapped to the specified mapped quantity
     *
     * @param sparePartsToQuantity map from a spare part to his quantity
     * @return true if each
     * @throws IllegalArgumentException if any quantity is less than 0
     */
    //TODO 30.05.2024 what if an Integer is less than or equals 0?
    public boolean addSpareParts(Map<SparePart, Integer> sparePartsToQuantity) {
        boolean addedAll = true;
        for (Map.Entry<SparePart, Integer> entry : sparePartsToQuantity.entrySet()) {
            if (!this.add(entry.getKey(), entry.getValue())) {
                addedAll = false;
            }
        }
        this.gui.refreshSpareParts();
        return addedAll;
    }

    /**
     * Consumes the specified amount of the specified sparePart. Returns true
     * if the spareParts could be used otherwise false
     *
     * @param sparePart the sparPart which should be used
     * @param amount the amount of the sparePart which should be used
     * @return {@code true} if the spareParts were used; otherwise {@code false}
     */
    public boolean useSparParts(@NotNull SparePart sparePart, int amount) {
        Integer stock = this.sparePartsToQuantityObsMap.get(sparePart);
        if (stock != null && stock >= amount) {
            this.sparePartsToQuantityObsMap.put(sparePart, stock - amount);
            this.gui.refreshSpareParts();
            return true;
        }
        return false;
    }

    /**
     * @param sparePartsToUseCount
     * @throws IllegalArgumentException if a specific spare part is unknown
     * @return
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
            while (iterator.hasNext() && (! containsName || ! containsUnit)) {
                SparePart part = iterator.next();
                if (part.getName().equals(deletedName)) {
                    containsName = true;
                }
                if (sparePart.getUnit().equals(deletedUnit)) {
                    containsUnit = true;
                }
            }
            if (! containsName) {
                this.sparePartNames.remove(deletedName);
                this.gui.displaySparePartNames(this.sparePartNames);
            }
            if (! containsUnit) {
                this.sparePartUnits.remove(deletedUnit);
            }
            this.categoryToSpareParts.get(sparePart.getCategory()).remove(sparePart);

            this.gui.updateStatus(String.format("spare part %s successfully deleted",
                    sparePart.getName()));
        }
        return removed;
    }

    /**
     *
     * @param sparePart
     * @return
     */
    public boolean inStock(@NotNull SparePart sparePart) {
        Integer quantity = this.sparePartsToQuantityObsMap.get(sparePart);
        if (quantity == null) {
            return false;
        }
        return quantity > 0;
    }

    /**
     * @return
     */
    @Override
    public @NotNull SparePartsManagerData toData() {
        return new SparePartsManagerData(this);
    }

    @Override
    public ObservableList<SparePart> getObservableList() {
        return new ObservableListMapBinder<>(this.sparePartsToQuantityObsMap).getObservableKeyList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof SparePartsManager that)) {
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
                ", categoryToSpareParts=" + this.categoryToSpareParts +
                '}';
    }

    /**
     * Adds the specified spare part to this sparePartManager.
     * If the sparePart is already known the specified quantity will be added to the stock
     * otherwise the new spare part will be added and mapped to the specified quantity
     *
     * @param newSparePart the "new" spare part which should be added
     * @param quantity     the quantity which should be added
     * @return {@code true} if the specified spare part was successfully added,
     * otherwise {@code false}
     * @throws IllegalArgumentException if the quantity is less than 0
     */
    private boolean add(@NotNull SparePart newSparePart, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater or equals 0 but is %d".formatted(quantity));
        }

        Integer oldValue = this.sparePartsToQuantityObsMap.get(newSparePart);
        this.sparePartsToQuantityObsMap.merge(newSparePart, quantity, Integer::sum);
        if (oldValue == null) {
            this.sparePartUnits.add(newSparePart.getUnit());
            this.sparePartNames.add(newSparePart.getName());
            Set<SparePart> spareParts = this.categoryToSpareParts.computeIfAbsent(
                    newSparePart.getCategory(), category -> new HashSet<>()
            );
            spareParts.add(newSparePart);
        }
        return true;
    }
}
