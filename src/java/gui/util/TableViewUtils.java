package gui.util;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 *
 */
public class TableViewUtils {
    /**
     * Adds a new tableColumn with the specified columnTitle to the specified tableView. The table
     * elements will be displayed depending on the specified valueExtractor
     *
     * @param tableView      the tableView to which the new column will be added
     * @param columnTitle    the title of the new added table column
     * @param valueExtractor extracts the table element to the display type
     * @param <S>            type of the table elements
     * @param <T>            type to which the table elements will be extracted (display type)
     */
    public static <S, T> void addColumn(@NotNull TableView<S> tableView, @NotNull String columnTitle,
                                        @NotNull Function<S, T> valueExtractor) {
        TableColumn<S, T> typColumn = new TableColumn<>(columnTitle);
        typColumn.setCellValueFactory(f -> new SimpleObjectProperty<>(valueExtractor.apply(f.getValue())));
        tableView.getColumns().add(typColumn);
    }
}
