package gui.FXutils;

import gui.ObservableListKeyBinder;
import gui.ObservableListValuesBinder;
import gui.ObservableListSetBinder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Some Utility's for FXCollections.
 *
 *  @author xthe_white_lionx
 */
public class FXCollectionsUtils {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private FXCollectionsUtils() {
    }

    /**
     * Creates an observable List bound to the specified observableSet
     *
     * @param observableSet set of the values that should be bound
     * @return a new observable List with the specific values of the observableSet
     * @param <T> Type of the values
     */
    public static <T> ObservableList<T> toObservableList(ObservableSet<T> observableSet) {
        return new ObservableListSetBinder<>(observableSet).getObservableList();
    }

    /**
     * Creates an observable List bound to the specified observableMap
     *
     * @param observableMap Map of the values that should be bound
     * @return a new observable Map with the specific values
     * @param <K>     type of the Keys
     * @param <V>     type of the Values
     */
    public static <K, V> ObservableList<V> toObservableValuesList(ObservableMap<K,V> observableMap) {
        return new ObservableListValuesBinder<>(observableMap).getObservableList();
    }

    /**
     * Creates an observable List bound to the specified observableMap
     *
     * @param observableMap Map of the values that should be bound
     * @return a new observable Map with the specific values
     * @param <K>     type of the Keys
     * @param <V>     type of the Values
     */
    public static <K, V> ObservableList<K> toObservableKeyList(ObservableMap<K,V> observableMap) {
        return new ObservableListKeyBinder<>(observableMap).getObservableList();
    }



    /**
     * Creates an observable Map from the specified values, using the natural ordering of its keys.
     * All keys inserted into the map must implement the {@link Comparable} interface.
     * A Key will be generated with the specified mapping function.
     * The generated Key will be associated with the specific value.
     *
     * @param values  values to be associated with the specified key
     * @param mapping function to generate a mapping key to the specific value
     * @return a new observable Map with the specific values
     * @param <K>     type of the Keys
     * @param <V>     type of the Values
     */
    public static <K extends Comparable<K>, V> ObservableMap<K,V> toObservableMap(
            @NotNull V[] values, @NotNull Function<? super V, ? extends K> mapping) {
        Map<K, V> result = new TreeMap<>();
        for (V value : values) {
            K key = mapping.apply(value);
            result.put(key, value);
        }

        return FXCollections.observableMap(result);
    }
}
