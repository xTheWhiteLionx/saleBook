package gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import logic.products.item.Item;
import logic.products.Product;
import logic.products.position.Position;
import utils.CollectionsUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * A PositionTreeItem class.
 *
 * @author xthe_white_lionx
 */
public class PositionTreeItem extends TreeItem<Product> implements ListChangeListener<Item> {

    /**
     * ObservableList of the direct children of this PositionTreeItem
     */
    private ObservableList<TreeItem<Product>> children;

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
                //TODO 31.05.2024 optimate
//                Iterator<TreeItem<Product>> iterator = this.children.iterator();
//                while (iterator.hasNext()) {
//                    TreeItem<Product> treeItem = iterator.next();
//                    if (ids.contains(treeItem.getValue().getId())) {
//                        iterator.remove();
//                    }
//                }
                this.children.removeIf(
                        productTreeItem -> ids.contains(productTreeItem.getValue().getId()));
            }
        }
    }
}
