package gui;

import com.sun.javafx.collections.SortableList;
import costumeClasses.SortedList;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import utils.ListUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
            int index = Collections.binarySearch(this.observableKeyList, change.getKey());
            this.observableKeyList.remove(index);
            V removedValue = change.getValueRemoved();
            if (removedValue != null) {
                index = Collections.binarySearch(this.observableValueList, removedValue);
                this.observableValueList.remove(index);
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
