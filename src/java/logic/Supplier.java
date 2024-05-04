package logic;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Objects;

/**
 * This class represents an order. An Order has a supplier, a cost and a mapping
 * from spare parts to their order quantity.
 *
 * @author xThe_white_Lionx
 */
public class Supplier implements Comparable<Supplier> {
    /**
     * Name of this supplier
     */
    private String name;
    /**
     * URI of the order webpage of this supplier.
     * By adding the tracking number, the tracking page can be reached.
     */
    private URI orderWebpage;

    /**
     * Constructor for a supplier
     *
     * @param name the supplier name
     * @param orderWebpage the order webpage of the supplier
     * @throws IllegalArgumentException if the specified name is empty
     */
    public Supplier(@NotNull String name, @NotNull URI orderWebpage) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        this.name = name;
        this.orderWebpage = orderWebpage;
    }

    /**
     * Returns the name of this supplier
     *
     * @return name of this supplier
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Sets the name of this supplier
     *
     * @param name the new name of this supplier
     * @throws IllegalArgumentException if the specified name is empty
     */
    public void setName(@NotNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        this.name = name;
    }

    /**
     * Returns the order webpage of this supplier
     *
     * @return the order webpage of this supplier
     */
    public @NotNull URI getOrderWebpage() {
        return this.orderWebpage;
    }

    /**
     * Sets the orderWebPage of this supplier
     *
     * @param orderWebpage the new orderWebpage of this supplier
     */
    public void setOrderWebpage(@NotNull URI orderWebpage) {
        this.orderWebpage = orderWebpage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplier supplier)) {
            return false;
        }
        return Objects.equals(this.name, supplier.name) &&
                Objects.equals(this.orderWebpage, supplier.orderWebpage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.orderWebpage);
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "name='" + this.name + '\'' +
                ", orderWebpage=" + this.orderWebpage +
                '}';
    }

    /**
     * Compares this supplier to the specified other supplier.
     * A supplier is greater than another if the supplier name is lexicographically greater than the another or
     * if the orderWebpage is greater, by the definition of URI, than the other supplier
     *
     * @param other the supplier to be compared.
     * @return  the value {@code 0} if the argument supplier is equal to
     *          this supplier; a value less than {@code 0} if this supplier
     *          is less than the supplier argument; and a
     *          value greater than {@code 0} if this string is
     *          greater than the supplier argument.
     */
    @Override
    public int compareTo(@NotNull Supplier other) {
        int result = this.name.compareTo(other.name);
        if (result == 0) {
            result = this.orderWebpage.compareTo(other.orderWebpage);
        }
        return result;
    }
}
