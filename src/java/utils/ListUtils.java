package utils;

import java.util.Collections;
import java.util.List;

/**
 * @author xthe_white_lionx
 * @date 02.06.2024
 */
public class ListUtils {

    private ListUtils() {
    }

    /**
     * @param list
     * @param element
     * @param <E>
     */
    //TODO 01.06.2024 outsource to new class ListUtils
    public static <E extends Comparable<E>> void sortedAdd(List<E> list, E element) {
        int index = Collections.binarySearch(list, element);
        index = -index - 1;
        list.add(index, element);
    }
}
