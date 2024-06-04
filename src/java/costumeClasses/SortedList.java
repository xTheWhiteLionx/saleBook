package costumeClasses;

import com.sun.javafx.collections.SortableList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author xthe_white_lionx
 * @date 02.06.2024
 */
public class SortedList<E extends Comparable<E>> implements List<E> {

    private final List<E> internList;

    private Comparator<? super E> comperator;

    public SortedList() {
        this.internList = new ArrayList<>();
        this.comperator = E::compareTo;
    }

    public SortedList(int initialCapacity) {
        this.internList = new ArrayList<>(initialCapacity);
        this.comperator = E::compareTo;
    }

    public SortedList(Collection<? extends E> c) {
        this.internList = new ArrayList<>(c);
        this.comperator = E::compareTo;
        if (c.size() > 1) {
            this.internList.sort(this.comperator);
        }
    }

    public SortedList(Comparator<? super E> c) {
        this.internList = new ArrayList<>();
        this.comperator = c;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        this.internList.forEach(action);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return this.internList.toArray(generator);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return this.internList.removeIf(filter);
    }

    @Override
    public Stream<E> stream() {
        return this.internList.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return this.internList.parallelStream();
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return this.internList.size();
    }

    /**
     * Returns {@code true} if this list contains no elements.
     *
     * @return {@code true} if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return this.internList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        int index = Collections.binarySearch(this.internList, (E)o, this.comperator);
        return index >= 0;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return this.internList.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return this.internList.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return this.internList.toArray(a);
    }

    @Override
    public boolean add(E e) {
        int index = Collections.binarySearch(this.internList, e, this.comperator);
        if (index < 0) {
            index = -index - 1;
        } else {
            index = ++index % this.internList.size();
        }
        this.internList.add(index, e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = Collections.binarySearch(this.internList, (E)o, this.comperator);
        if (index < 0) {
            return false;
        }
        return this.internList.remove(index) != null;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        boolean contains = true;
        for (Object o : c) {
            int i = Collections.binarySearch(this.internList, (E) o, this.comperator);
            if (i < 0) {
                contains = false;
            }
        }
        return contains;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        boolean addedAll = false;
        for (E e : c) {
         addedAll = this.add(e);
        }
        return addedAll;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean removedAll = false;
        for (Object o : c) {
            removedAll = this.remove(o);
        }
        return removedAll;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.internList.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        this.internList.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        if (!this.comperator.equals(c)) {
            this.comperator = c;
            this.internList.sort(this.comperator);
        }
    }

    @Override
    public void clear() {
        this.internList.clear();
    }

    @Override
    public E get(int index) {
        return this.internList.get(index);
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public E remove(int index) {
        return this.internList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return Collections.binarySearch(this.internList, (E) o, this.comperator);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.internList.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return this.internList.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return this.internList.listIterator(index);
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return this.internList.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return this.internList.spliterator();
    }

    public Comparator<? super E> getComperator() {
        return this.comperator;
    }

    public void setComperator(Comparator<? super E> comperator) {
        this.comperator = comperator;
    }
}
