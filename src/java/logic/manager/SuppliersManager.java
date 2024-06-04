package logic.manager;

import gui.ObservableListMapBinder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import logic.GUIConnector;
import logic.Supplier;
import gui.FXutils.FXCollectionsUtils;
import logic.order.Order;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 *
 */
public class SuppliersManager extends AbstractManager implements ObservableListable<Supplier> {

    /**
     * ObservableMap of suppliers mapped their matching name
     */
    private final ObservableMap<String, Supplier> nameToSupplierObsMap;

    /**
     * @param saleBook
     * @param gui
     */
    public SuppliersManager(SaleBook saleBook, @NotNull GUIConnector gui) {
        super(saleBook, gui);
        this.nameToSupplierObsMap = FXCollections.observableMap(new TreeMap<>());
    }

    /**
     * @param saleBook
     * @param suppliers
     * @param gui
     */
    public SuppliersManager(SaleBook saleBook, @NotNull Supplier[] suppliers,
                            @NotNull GUIConnector gui) {
        super(saleBook, gui);
        this.nameToSupplierObsMap = FXCollectionsUtils.toObservableMap(suppliers, Supplier::getName);
    }

    /**
     * Returns the suppliers of this suppliersManager
     *
     * @return the suppliers of this suppliersManager
     */
    public @NotNull Collection<Supplier> getSuppliers() {
        return new TreeSet<>(this.nameToSupplierObsMap.values());
    }

    /**
     *
     * @return
     */
    @UnmodifiableView
    public @NotNull Set<String> getSupplierNames() {
        return Collections.unmodifiableSet(this.nameToSupplierObsMap.keySet());
    }

    /**
     * Returns the supplier with the specified supplierName
     *
     * @param supplierName the name of the searched supplier
     * @return the supplier
     */
    public @Nullable Supplier getSupplier(@NotNull String supplierName) {
        return this.nameToSupplierObsMap.get(supplierName);
    }

    /**
     * Adds the specified supplier to this supplierManager
     *
     * @param supplier the supplier which should be added
     * @return
     */
    public boolean addSupplier(@NotNull Supplier supplier) {
        boolean added = this.nameToSupplierObsMap.putIfAbsent(supplier.getName(), supplier) == null;
        if (added) {
            this.gui.displaySupplierNames(this.getSupplierNames());
            this.gui.updateStatus(String.format("supplier %s added", supplier.getName()));
        }
        return added;
    }

    /**
     * Deletes the supplier with the specified supplierName, if the supplier name is not empty.
     *
     * @param supplierName the name of the supplier which should be deleted
     */
    public @Nullable Supplier removeSupplier(@NotNull String supplierName) {
        Supplier removedSupplier = this.nameToSupplierObsMap.remove(supplierName);
        if (removedSupplier != null) {
            this.gui.displaySupplierNames(this.getSupplierNames());
            this.gui.updateStatus(String.format("supplier %s deleted", supplierName));
        }
        return removedSupplier;
    }

    @Override
    public ObservableList<Supplier> getObservableList() {
        return new ObservableListMapBinder<>(this.nameToSupplierObsMap).getObservableValuesList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuppliersManager that)) {
            return false;
        }
        return Objects.equals(this.nameToSupplierObsMap, that.nameToSupplierObsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.nameToSupplierObsMap);
    }

    @Override
    public String toString() {
        return "SuppliersManager{" +
                "nameToSupplierObsMap=" + this.nameToSupplierObsMap +
                '}';
    }
}
