package logic;

import utils.BigDecimalUtils;
import utils.LocalDateUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * This class represents an asset.
 * An asset has a supplier, purchasing date, arrival date, unique name, unique id and a value.
 * The value of the asset is the price on which the asset was bought.
 *
 * @author xThe_white_Lionx
 */
public class Asset implements Comparable<Asset> {

    /**
     * The id of the asset
     */
    private final int id;

    /**
     * The name of the asset
     */
    private @NotNull String name;

    /**
     * The supplier of the asset
     */
    private @NotNull Supplier supplier;

    /**
     * The purchasing price of the asset
     */
    private @NotNull LocalDate purchasingDate;

    /**
     * The arrival date of the asset
     */
    private @Nullable LocalDate arrivalDate;

    /**
     * The value of the asset
     */
    private double value;

    /**
     * Constructs a new Asset with the specified parameters
     *
     * @param id id of the new asset
     * @param name name id of the new asset
     * @param supplier supplier of the new asset
     * @param purchasingDate purchasing price of the new asset
     * @param value value of the new asset
     * @throws IllegalArgumentException if the name is empty or the value is negative
     */
    public Asset(int id, @NotNull String name,@NotNull Supplier supplier,
                 @NotNull LocalDate purchasingDate, double value) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("value is negative");
        }
        if (purchasingDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("purchasingDate is in the future");
        }

        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.purchasingDate = purchasingDate;
        this.value = value;
    }

    /**
     * Returns the id of this asset
     *
     * @return the id of this asset
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the name of this asset
     *
     * @return the name of this asset
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Sets the name of this asset
     *
     * @param name the new name of this asset
     * @throws IllegalArgumentException if the name is empty
     */
    public void setName(@NotNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }

        this.name = name;
    }

    /**
     * Returns the supplier of this asset
     *
     * @return the supplier of this asset
     */
    public @NotNull Supplier getSupplier() {
        return this.supplier;
    }

    /**
     * Sets the supplier of this asset
     *
     * @param supplier the new supplier of this asset
     */
    public void setSupplier(@NotNull Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     * Returns the purchasing price of this asset
     *
     * @return the purchasing price of this asset
     */
    public @NotNull LocalDate getPurchasingDate() {
        return this.purchasingDate;
    }

    /**
     * Sets the purchasing price of this asset
     *
     * @param purchasingDate the new purchasing price of this asset
     * @throws IllegalArgumentException if the purchasingDate is in the future
     */
    public void setPurchasingDate(@NotNull LocalDate purchasingDate) {
        if (purchasingDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("purchasingDate is in the future");
        }

        this.purchasingDate = purchasingDate;
    }

    /**
     * Returns the arrival date of this asset
     *
     * @return the arrival date of this asset
     */
    public @Nullable LocalDate getArrivalDate() {
        return this.arrivalDate;
    }

    /**
     * Sets the arrival date of this asset
     *
     * @param arrivalDate the new arrival date of this asset
     * @throws IllegalArgumentException if the arrivalDate is before the purchasingDate or
     * the arrivalDate is in the future
     */
    public void setArrivalDate(@NotNull LocalDate arrivalDate) {
        if (!LocalDateUtils.areAcceptableDates(this.purchasingDate, arrivalDate)) {
            throw new IllegalArgumentException("arrivalDate is before purchasingDate");
        }
        if (arrivalDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("arrivalDate is in the future");
        }

        this.arrivalDate = arrivalDate;
    }

    /**
     * Returns the value of this asset
     *
     * @return the value of this asset
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Sets the value of this asset
     *
     * @param value the new value of this asset
     * @throws IllegalArgumentException if the value is negative
     */
    public void setValue(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value is negative");
        }

        this.value = value;
    }

    /**
     * Returns true
     *
     * @return
     */
    public boolean isReceived() {
        return this.arrivalDate != null;
    }

    @Override
    public int compareTo(@NotNull Asset o) {
        int result = o.id - this.id;
        if(result == 0){
            result = this.name.compareTo(o.name);
        }
        if (result == 0){
            result = this.supplier.compareTo(o.supplier);
        }
        if (result == 0){
            result = Double.compare(this.value, o.value);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset asset)) {
            return false;
        }
        return this.id == asset.id &&
                Objects.equals(this.name, asset.name) &&
                Objects.equals(this.supplier, asset.supplier) &&
                Objects.equals(this.purchasingDate, asset.purchasingDate) &&
                Objects.equals(this.arrivalDate, asset.arrivalDate) &&
                this.value == asset.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.supplier,
                this.purchasingDate, this.arrivalDate, this.value);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", supplier=" + this.supplier +
                ", purchasingDate=" + this.purchasingDate +
                ", arrivalDate=" + this.arrivalDate +
                ", value=" + this.value +
                '}';
    }
}
