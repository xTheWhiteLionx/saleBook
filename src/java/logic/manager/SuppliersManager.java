package logic.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import logic.Supplier;
import gui.FXutils.FXCollectionsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 */
public class SuppliersManager {

    /**
     * ObservableMap of suppliers mapped their matching name
     */
    private final ObservableMap<String, Supplier> nameToSupplierObsMap;

    public SuppliersManager() {
        this.nameToSupplierObsMap = FXCollections.observableMap(new TreeMap<>());
    }

    public SuppliersManager(Supplier[] suppliers){
        this.nameToSupplierObsMap = FXCollectionsUtils.toObservableMap(suppliers, Supplier::getName);
    }

    /**
     *
     * @return
     */
    public @NotNull ObservableMap<String, Supplier> getNameToSupplierObsMap() {
        return this.nameToSupplierObsMap;
    }

    /**
     * Returns the suppliers of this suppliersManager
     *
     * @return the suppliers of this suppliersManager
     */
    public @NotNull Collection<Supplier> getSuppliers() {
        return this.nameToSupplierObsMap.values();
    }

    /**
     *
     * @return
     */
    public @NotNull Set<String> getSupplierNames() {
        return this.nameToSupplierObsMap.keySet();
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
     */
    public boolean addSupplier(@NotNull Supplier supplier) {
        return this.nameToSupplierObsMap.put(supplier.getName(), supplier) == null;
    }

    /**
     * Deletes the supplier with the specified supplierName, if the supplier name is not empty.
     *
     * @param supplierName the name of the supplier which should be deleted
     */
    public Supplier removeSupplier(@NotNull String supplierName) {
        return this.nameToSupplierObsMap.remove(supplierName);
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
