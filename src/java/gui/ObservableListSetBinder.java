package gui;

import javafx.collections.*;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper class that binds the elements of an {@link ObservableSet} to an {@link ObservableList}.
 * The items in the list will match the elements in the set.
 * This class is not synchronized.
 *
 * @param <T> the type used for the elements of the ObservableSet
 */
public class ObservableListSetBinder<T> implements SetChangeListener<T> {

    /**
     * ObservableList bound to the values of the ObservableSet
     */
    private final ObservableList<T> observableList;

    /**
     * Creates an ObservableListSetBinder bounded to the specified set
     *
     * @param set from which the values should be bound
     */
    public ObservableListSetBinder(@NotNull ObservableSet<T> set) {
        // initialise the list
        this.observableList = FXCollections.observableArrayList(set);

        // listen for changes
        set.addListener(this);
    }

    /**
     * Returns an unmodifiable version of the ObservableList.
     *
     * @return unmodifiable ObservableList
     */
    public @NotNull ObservableList<T> getObservableList() {
        return FXCollections.unmodifiableObservableList(this.observableList);
    }

    @Override
    public void onChanged(Change<? extends T> change) {
        if (change.wasRemoved()) {
            this.observableList.remove(change.getElementRemoved());
        }
        if (change.wasAdded()) {
            this.observableList.add(change.getElementAdded());
        }
    }
}
