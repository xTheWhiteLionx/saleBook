package gui.FXutils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Some Utility's for TableView's.
 *
 * @author xthe_white_lionx
 */
public class TableViewUtils {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private TableViewUtils() {
    }

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
                                        @NotNull Function<? super S, T> valueExtractor) {
        TableColumn<S, T> typColumn = new TableColumn<>(columnTitle);
        typColumn.setCellValueFactory(f -> new SimpleObjectProperty<>(valueExtractor.apply(f.getValue())));
        tableView.getColumns().add(typColumn);
    }
}
