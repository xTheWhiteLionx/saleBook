package utils;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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
    public static <T, R> R[] toArray(Collection<T> values, Function<? super T, ?
            extends R> mapper, R[] array) {
        int size = values.size();
        R[] result = array.length < size ? Arrays.copyOf(array, size) : array;
        int i = 0;
        for (T value : values) {
            result[i++] = mapper.apply(value);
        }
        return result;
    }

    /**
     * @param values
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean areValid(Collection<T> values, Predicate<? super T> predicate) {
        boolean areValid = true;
        Iterator<T> iterator = values.iterator();
        while (areValid && iterator.hasNext()) {
            T value = iterator.next();
            if (!predicate.test(value)) {
                areValid = false;
            }
        }
        return areValid;
    }

    /**
     *
     * @param collection
     * @param accu
     * @param combiner
     * @return
     * @param <E>
     * @param <R>
     */
    public static <E, R> @NotNull R reduce(Iterable<E> collection, R accu,
                                   BiFunction<? super E, ? super R, ? extends R> combiner) {
        for (E element : collection) {
            accu = combiner.apply(element, accu);
        }
        return accu;
    }
}
