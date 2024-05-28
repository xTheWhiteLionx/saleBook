package utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

/**
 * This class inherits some Utilities for {@link Collection}.
 *
 * @author xthe_white_lionx
 */
public class CollectionsUtils {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private CollectionsUtils() {
    }

    /**
     * Returns an array containing all of the mapped values of the values of the values;
     * the runtime type of the returned array is that of the specified array.
     * If the values fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this values.
     *
     * @param values collection of values which should be copied
     * @param mapper function to map a value of the collection to a value of the
     * @param array  the array into which the elements of the values are to
     *               be stored, if it is big enough; otherwise, a new array of the
     *               same runtime type is allocated for this purpose.
     * @param <T>    Type of the values of the collection
     * @param <R>    Type of the elements of the result array
     * @return an array containing all of the elements in of the mapped values of the collection
     */
    public static <T, R> R[] toArray(Collection<T> values, Function<? super T,
            ? extends R> mapper, R[] array) {
        int size = values.size();
        R[] result = array.length < size ? Arrays.copyOf(array, size) : array;
        int i = 0;
        for (T value : values) {
            result[i++] = mapper.apply(value);
        }
        return result;
    }
}
