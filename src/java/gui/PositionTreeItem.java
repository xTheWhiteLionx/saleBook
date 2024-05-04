package gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import logic.products.item.Item;
import logic.products.Product;
import logic.products.position.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A PositionTreeItem class.
 *
 * @author xthe_white_lionx
 */
public class PositionTreeItem extends TreeItem<Product> implements ListChangeListener<Item> {

    /**
     * ObservableList of the direct children of this PositionTreeItem
     */
    ObservableList<TreeItem<Product>> children;

    /**
     * Creates a new PositionTreeItem with the specified position
     *
     * @param position value of this PositionTreeItem
     */
    public PositionTreeItem(Position position) {
        super(position);
        this.children = this.getChildren();
        ObservableList<Item> itemObservableList = position.getItemObservableList();
        for (Item item : itemObservableList) {
            this.children.add(new TreeItem<>(item));
        }
        itemObservableList.addListener(this);
    }

    @Override
    public void onChanged(Change<? extends Item> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                List<? extends Item> addedSubList = change.getAddedSubList();
                for (Item item : addedSubList) {
                    this.children.add(new TreeItem<>(item));
                }
            }

            if (change.wasRemoved()) {
                List<? extends Item> removed = change.getRemoved();

                Set<Integer> ids = new HashSet<>();
                for (Item item : removed) {
                    ids.add(item.getId());
                }
                this.children.removeIf(
                        productTreeItem -> ids.contains(productTreeItem.getValue().getId()));
            }
        }
    }
}
