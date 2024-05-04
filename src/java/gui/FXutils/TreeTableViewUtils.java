package gui.FXutils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Some Utility's for TreeTableView's.
 */
public class TreeTableViewUtils {
    /**
     * Adds a new treeTableColumn with the specified columnTitle to the specified treeTableView. The
     * table elements will be displayed depending on the specified valueExtractor
     *
     * @param treeTableView  the treeTableView to which the new column will be added
     * @param columnTitle    the title of the new added table column
     * @param valueExtractor extracts the table element to the display type
     * @param <S>            type of the table elements
     * @param <T>            type to which the table elements will be extracted (display type)
     */
    public static <S, T> void addColumn(@NotNull TreeTableView<S> treeTableView,
                                        @NotNull String columnTitle,
                                        @NotNull Function<S, T> valueExtractor) {
        TreeTableColumn<S, T> treeTableColumn = new TreeTableColumn<>(columnTitle);
        treeTableColumn.setCellValueFactory(f -> new SimpleObjectProperty<>(valueExtractor.apply(
                f.getValue().getValue())));
        treeTableView.getColumns().add(treeTableColumn);
    }
}

