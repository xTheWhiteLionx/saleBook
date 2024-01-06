package gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import logic.products.Item;
import logic.products.Product;
import logic.products.position.Position;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PositionTreeItem extends TreeItem<Product> implements ListChangeListener<Item> {

    /**
     *
     */
    ObservableList<TreeItem<Product>> children;

    /**
     *
     * @param position
     */
    public PositionTreeItem(Position position) {
        super(position);
        this.children = this.getChildren();
        for (Item item : position.getItems()) {
            this.children.add(new TreeItem<>(item));
        }
        position.getItemObservableList().addListener(this);
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
                int offSet = 0;
                for (Item item : removed) {
                    while (offSet < this.children.size()) {
                        TreeItem<Product> child = this.children.get(offSet);
                        if (child.getValue() instanceof Item itemChild && itemChild.getId() == item.getId()) {
                            this.children.remove(offSet);
                            break;
                        } else {
                            offSet++;
                        }
                    }
                }
            }
        }
    }
}
