package gui;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A wrapper class that binds the values of an {@link ObservableMap} to an {@link ObservableList}. The items in the list
 * will match the values in the map. This class is not synchronized.
 *
 * @param <K> the type used for keys of the ObservableMap
 * @param <V> the type used for values of the ObservableMap
 */
public class ObservableListMapBinder<K, V> implements MapChangeListener<K, V> {

    private final ObservableList<V> list;

    /**
     *
     * @param map
     */
    public ObservableListMapBinder(@NotNull ObservableMap<K, V> map) {
        // initialise the list
        this.list = FXCollections.observableArrayList(map.values());

        // listen for changes
        map.addListener(this);
    }

    /**
     * Returns an unmodifiable version of the ObservableList.
     *
     * @return unmodifiable ObservableList
     */
    public @NotNull ObservableList<V> getList() {
        return FXCollections.unmodifiableObservableList(this.list);
    }

    @Override
    public void onChanged(Change<? extends K, ? extends V> change) {
        if (change.wasRemoved()) {
            this.list.remove(change.getValueRemoved());
        }
        if (change.wasAdded()) {
            this.list.add(change.getValueAdded());
        }
    }
}
