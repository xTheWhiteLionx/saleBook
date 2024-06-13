package logic.manager;

import javafx.collections.ObservableList;

/**
 * This is in interface to get an observableList of the concrete class
 *
 * @author xThe_white_Lionx
 * @Date 29.05.2024
 * @param <T> Type of the elements of the observableList
 */
public interface ObservableListable<T> {

    /**
     * Returns an observableList of the concrete class
     *
     * @return an observableList of the concrete class
     */
    ObservableList<T> getObservableList();
}
