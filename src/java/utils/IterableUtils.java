package utils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
     * @param iterable the iterable which supplies the values
     * @param predicate the predicate which every value should fit
     * @param <T> type of the elements of the iterable
     * @return {@code true} if each element of the iterable fits the specified predicate, otherwise {@code false}
     */
    public static <T> boolean areValid(@NotNull Iterable<T> iterable,@NotNull Predicate<?
            super T> predicate) {
        boolean areValid = true;
        Iterator<T> iterator = iterable.iterator();
        while (areValid && iterator.hasNext()) {
            T value = iterator.next();
            if (!predicate.test(value)) {
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
     * @param accu the current value of the accumulation
     * @param combiner the combiner to combine the values
     * @return the reduced value of the iterable values
     * @param <E> Type of the values of the iterable
     * @param <R> Type of the accu and the result
     */
    public static <E, R> @NotNull R reduce(@NotNull Iterable<E> iterable, @NotNull R accu,
                                           @NotNull BiFunction<? super E, ? super R,
                                           ? extends R> combiner) {
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
    public static <E,T> void collect(@NotNull Iterable<E> iterable,
                                     @NotNull Function<? super E, ? extends T> function,
                                    @NotNull Collection<? super T> result) {
        for (E element : iterable) {
            result.add(function.apply(element));
        }
    }
}
