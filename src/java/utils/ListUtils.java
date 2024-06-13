package utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public static <E extends Comparable<E>> void sortedAdd(List<E> list, E element) {
        int index = Collections.binarySearch(list, element);
        if (index < 0) {
            index = -index - 1;
        }
        list.add(index, element);
    }

    /**
     * @param list
     * @param element
     * @param <E>
     */
    public static <E extends Comparable<E>> boolean sortedRemove(List<? extends E> list,
                                                                     E element) {
        int index = Collections.binarySearch(list, element);
        if (index >= 0) {
            list.remove(index);
            return true;
        }
        return false;
    }
}
