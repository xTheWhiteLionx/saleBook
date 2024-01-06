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

public class ObservableTreeItemMapBinder<K> extends TreeItem<Product>
        implements MapChangeListener<K, Position> {
    private final ObservableList<TreeItem<Product>> children;
    private final FilteredList<TreeItem<Product>> filteredList;

    /**
     * @param map
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
     *
     * @param filter
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
     *
     */
    public void resetFilter(){
        this.filteredList.setPredicate(item -> true);
    }

    public @NotNull FilteredList<TreeItem<Product>> getFilteredList() {
        return this.filteredList;
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
