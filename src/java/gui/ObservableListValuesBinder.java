package gui;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper class that binds the values of an {@link ObservableMap} to an {@link ObservableList}. The items in the list
 * will match the values in the map. This class is not synchronized.
 *
 * @param <K> the type used for keys of the ObservableMap
 * @param <V> the type used for values of the ObservableMap
 */
public class ObservableListValuesBinder<K, V> implements MapChangeListener<K, V> {

    /**
     * ObservableList bound to the values of the ObservableMap
     */
    private final ObservableList<V> observableList;

    /**
     * Creates an observableListMapBinder bound to the values of the specified map
     *
     * @param map from which the values should be bound
     */
    public ObservableListValuesBinder(@NotNull ObservableMap<K, V> map) {
        // initialise the list
        this.observableList = FXCollections.observableArrayList(map.values());

        // listen for changes
        map.addListener(this);
    }

    /**
     * Returns an unmodifiable version of the ObservableList.
     *
     * @return unmodifiable ObservableList
     */
    public @NotNull ObservableList<V> getObservableList() {
        return FXCollections.unmodifiableObservableList(this.observableList);
    }

    @Override
    public void onChanged(Change<? extends K, ? extends V> change) {
        if (change.wasRemoved()) {
            this.observableList.remove(change.getValueRemoved());
        }
        if (change.wasAdded()) {
            this.observableList.add(change.getValueAdded());
        }
    }
}
