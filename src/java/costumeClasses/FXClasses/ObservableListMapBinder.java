package costumeClasses.FXClasses;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import utils.ListUtils;

import java.util.Collections;

/**
 * A wrapper class that binds the keys and values of an {@link ObservableMap} to an
 * {@link ObservableList}. The keys of the map will match the items of the
 * {@link #observableKeyList}. The values of the map will match the items
 * of the {@link #observableValueList}. This class is not synchronized.
 *
 * @param <K> the type used for keys of the ObservableMap
 * @param <V> the type used for values of the ObservableMap
 * @author xThe_white_Lionx
 * @date 02.06.2024
 */
public class ObservableListMapBinder<K extends Comparable<K>, V extends Comparable<V>> implements
        MapChangeListener<K, V> {

    /**
     * ObservableList bound to the keys of the ObservableMap
     */
    private final ObservableList<K> observableKeyList;

    /**
     * ObservableList bound to the values of the ObservableMap
     */
    private final ObservableList<V> observableValueList;

    /**
     * Creates an observableListMapBinder bound to the keys and values of the specified map
     *
     * @param map from which the keys and values will be bound
     */
    public ObservableListMapBinder(@NotNull ObservableMap<K, V> map) {
        // initialise the list
        this.observableKeyList = FXCollections.observableArrayList(map.keySet());
        this.observableValueList = FXCollections.observableArrayList(map.values());
        Collections.sort(this.observableKeyList);
        Collections.sort(this.observableValueList);

        // listen for changes
        map.addListener(this);
    }

    /**
     * Returns an unmodifiable view of the ObservableKeyList.
     *
     * @return unmodifiable ObservableKeyList
     */
    @UnmodifiableView
    public @NotNull ObservableList<K> getObservableKeyList() {
        return FXCollections.unmodifiableObservableList(this.observableKeyList);
    }

    /**
     * Returns an unmodifiable view of the ObservableKeyList.
     *
     * @return unmodifiable ObservableKeyList
     */
    @UnmodifiableView
    public @NotNull ObservableList<V> getObservableValuesList() {
        return FXCollections.unmodifiableObservableList(this.observableValueList);
    }

    @Override
    public void onChanged(Change<? extends K, ? extends V> change) {
        if (change.wasRemoved()) {
            ListUtils.sortedRemove(this.observableKeyList, change.getKey());
            V removedValue = change.getValueRemoved();
            if (removedValue != null) {
                ListUtils.sortedRemove(this.observableValueList, removedValue);
            }
        }

        if (change.wasAdded()) {
            ListUtils.sortedAdd(this.observableKeyList, change.getKey());
            V valueAdded = change.getValueAdded();
            if (valueAdded != null) {
                ListUtils.sortedAdd(this.observableValueList, valueAdded);
            }
        }
    }

}
