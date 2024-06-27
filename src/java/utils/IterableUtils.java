package utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class inherits some Utilities for {@link Iterable} classes.
 *
 * @author xthe_white_lionx
 */
public class IterableUtils {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private IterableUtils() {
    }

    /**
     * Checks if each value of the specified iterable fulfills he specified predicate
     *
     * @param iterable  the iterable which supplies the values
     * @param predicate the predicate which every value should fit
     * @param <T>       type of the elements of the iterable
     * @return {@code true} if each element of the iterable fits the specified predicate, otherwise {@code false}
     */
    public static <T> boolean areValid(@NotNull Iterable<T> iterable,
                                       @NotNull Predicate<? super T> predicate) {
        boolean areValid = true;
        Iterator<T> iterator = iterable.iterator();
        while (areValid && iterator.hasNext()) {
            T value = iterator.next();
            if (! predicate.test(value)) {
                areValid = false;
            }
        }
        return areValid;
    }

    /**
     * Reduces the values of the iterable to one value starting with the specified accu.
     * The values will be reduced by the specified combiner
     *
     * @param iterable the iterable with his values
     * @param accu     the current value of the accumulation
     * @param combiner the combiner to combine the values
     * @param <E>      Type of the values of the iterable
     * @param <R>      Type of the accu and the result
     * @return the reduced value of the iterable values
     */
    public static <E, R> @NotNull R reduce(@NotNull Iterable<E> iterable,
                                           @NotNull R accu,
                                           @NotNull BiFunction<? super E, ? super R, ? extends R>
                                                   combiner) {
        for (E element : iterable) {
            accu = combiner.apply(element, accu);
        }
        return accu;
    }

    /**
     * Collects the
     *
     * @param iterable
     * @param function
     * @param result
     * @param <E>
     * @param <T>
     */
    public static <E, T> void map(@NotNull Iterable<E> iterable,
                                  @NotNull Function<? super E, ? extends T> function,
                                  @NotNull Collection<? super T> result) {
        for (E element : iterable) {
            result.add(function.apply(element));
        }
    }

    /**
     * Collects the
     *
     * @param iterable
     * @param predicate
     * @param result
     * @param <E>
     */
    public static <E> void filter(@NotNull Iterable<E> iterable,
                                  @NotNull Predicate<? super E> predicate,
                                  @NotNull Collection<? super E> result) {
        for (E element : iterable) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
    }

    /**
     * Collects the
     *
     * @param iterable
     * @param predicate
     * @param <E>
     */
    public static <E> @Nullable E lookUp(@NotNull Iterable<E> iterable,
                                         @NotNull Predicate<? super E> predicate) {
        E result = null;
        Iterator<E> iterator = iterable.iterator();
        while (iterator.hasNext() && result == null) {
            if (predicate.test(iterator.next())) {
                result = iterator.next();
            }
        }
        return result;
    }
}
