package gui;

import javafx.collections.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A wrapper class that binds the values of an {@link ObservableMap} to an {@link ObservableList}. The items in the list
 * will match the values in the map. This class is not synchronized.
 *
 * @param <K> the type used for keys of the ObservableMap
 * @param <V> the type used for values of the ObservableMap
 */
public class ObservableListSetBinder<T> implements SetChangeListener<T> {

    /**
     *
     */
    private final ObservableList<T> list;

    /**
     *
     * @param set
     */
    public ObservableListSetBinder(@NotNull ObservableSet<T> set) {
        // initialise the list
        this.list = FXCollections.observableArrayList(set);

        // listen for changes
        set.addListener(this);
    }

    /**
     * Returns an unmodifiable version of the ObservableList.
     *
     * @return unmodifiable ObservableList
     */
    public @NotNull ObservableList<T> getList() {
        return FXCollections.unmodifiableObservableList(this.list);
    }

    @Override
    public void onChanged(Change<? extends T> change) {
        if (change.wasRemoved()) {
            this.list.remove(change.getElementRemoved());
        }
        if (change.wasAdded()) {
            this.list.add(change.getElementAdded());
        }
    }
}
