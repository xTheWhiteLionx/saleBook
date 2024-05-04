package gui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;
import logic.products.Product;
import logic.products.position.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * A TreeItem class, that likewise works as wrapper and binds the values of an {@link ObservableMap}
 * to an {@link ObservableList}.
 * The items in the list will match the values in the map.
 * This class is not synchronized.
 *
 * @param <K> the type used for keys of the ObservableMap
 */
public class ObservableTreeItemMapBinder<K> extends TreeItem<Product>
        implements MapChangeListener<K, Position> {

    /**
     * ObservableList of the direct children of this TreeItem, which are bound to the values of the ObservableMap
     */
    private final ObservableList<TreeItem<Product>> children;

    /**
     * The FilteredList of the children of this TreeItem
     */
    private final FilteredList<TreeItem<Product>> filteredList;

    /**
     * Creates a new ObservableTreeItemMapBinder
     *
     * @param map from which the values should be bound
     */
    public ObservableTreeItemMapBinder(@NotNull ObservableMap<K, Position> map) {
        super();
        this.children = FXCollections.observableArrayList();
        Collection<Position> values = map.values();
        for (Position position : values) {
            this.children.add(new PositionTreeItem(position));
        }
        this.filteredList = new FilteredList<>(this.children);
        Bindings.bindContent(this.getChildren(), this.filteredList);
        map.addListener(this);
    }

    /**
     * Sets the predicate of the filter of the filterList
     *
     * @param filter the filter to which the elements of the filterList should be tested
     */
    public void setFilter(@NotNull Predicate<Position> filter) {
        this.filteredList.setPredicate(item -> {
            if (item.getValue() instanceof Position position){
                return filter.test(position);
            } else {
                return true;
            }
        });
    }

    /**
     * Sets the filter of the filtered List to false for all items, so that no item will be shown
     */
    public void filterAll(){
        this.filteredList.setPredicate(item -> false);
    }

    @Override
    public void onChanged(Change<? extends K, ? extends Position> change) {
        if (change.wasRemoved()) {
            for (int i = 0; i < this.children.size(); i++) {
                TreeItem<Product> productTreeItem = this.children.get(i);
                if (productTreeItem.getValue().equals(change.getValueRemoved())) {
                    this.children.remove(i);
                    break;
                }
            }
        }
        if (change.wasAdded()) {
            Position valueAdded = change.getValueAdded();
            this.children.add(new PositionTreeItem(valueAdded));
        }
    }
}
