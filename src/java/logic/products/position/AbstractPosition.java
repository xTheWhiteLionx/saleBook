package logic.products.position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This abstract class presents a comment Position
 *
 * @author xthe_white_lionx
 */
public abstract class AbstractPosition {
    /**
     * The id of this positionImpl
     */
    protected int id;
    /**
     * The category of this positionImpl
     */
    protected String category;
    /**
     * The current state of this positionImpl
     */
    protected State state;
    /**
     * The price at which this positionImpl was purchased
     */
    protected BigDecimal purchasingPrice;
    /**
     * The date on which this positionImpl was ordered
     */
    protected LocalDate orderDate;
    /**
     * The cost of this positionImpl excluding the {@link #purchasingPrice}
     */
    protected BigDecimal cost;
    /**
     * The date this positionImpl has been received
     */
    protected LocalDate receivedDate;
    /**
     * The date this positionImpl has been sold
     */
    protected LocalDate sellingDate;
    /**
     * The price on which this positionImpl has been sold
     */
    protected BigDecimal sellingPrice;
    /**
     * The shipping company which shipped this positionImpl
     */
    protected ShippingCompany shippingCompany;
    /**
     * The tracking Number of this positionImpl in the corresponding {@link #shippingCompany}
     */
    protected String trackingNumber;
    /**
     * The id of the next item
     */
    protected int nextItemId = 1;

    /**
     * Constructor for an ordered positionImpl. For other states use
     * the {@link #AbstractPosition(int, String, LocalDate, BigDecimal, State, BigDecimal, LocalDate,
     * LocalDate, BigDecimal, ShippingCompany, String)} constructor
     *
     * @param id               the id of this positionImpl
     * @param category            the model of this positionImpl
     * @param orderDate        the date on which this positionImpl was ordered
     * @param purchasingPrice  the price on which this positionImpl was sold
     * @param cost             the cost of this positionImpl excluding the purchasingPrice
     */
    public AbstractPosition(int id, @NotNull String category, @NotNull LocalDate orderDate,
                            @NotNull BigDecimal purchasingPrice,
                            @NotNull BigDecimal cost) {
        this.id = id;
        this.state = State.ORDERED;
        this.category = category;
        this.orderDate = orderDate;
        this.purchasingPrice = purchasingPrice;
        this.cost = cost;
    }

    /**
     * Constructor for a positionImpl. For an ordered positionImpl use
     * the {@link #AbstractPosition(int, String, LocalDate, BigDecimal, BigDecimal)}
     * constructor instant.
     *
     * @param id               the id of this positionImpl
     * @param category            the model of this positionImpl
     * @param orderDate        the date on which this positionImpl was ordered
     * @param purchasingPrice  the price on which this positionImpl was sold
     * @param state            the current state of this positionImpl
     * @param cost             the cost of this positionImpl excluding the purchasingPrice
     * @param receivedDate     the date on which this positionImpl has been received
     * @param sellingDate      the date of this positionImpl when it was sold
     * @param sellingPrice     the price of this positionImpl
     * @param shippingCompany  the shipping company which shipped this positionImpl
     * @param trackingNumber   the tracking Number of this positionImpl in the corresponding
     *                         {@link #shippingCompany}
     */
    public AbstractPosition(int id, @NotNull String category, @NotNull LocalDate orderDate,
                            @NotNull BigDecimal purchasingPrice,
                            @NotNull State state, @NotNull BigDecimal cost, LocalDate receivedDate,
                            LocalDate sellingDate, BigDecimal sellingPrice,
                            ShippingCompany shippingCompany, String trackingNumber) {
        this.id = id;
        this.category = category;
        this.state = state;
        this.purchasingPrice = purchasingPrice;
        this.orderDate = orderDate;
        this.cost = cost;
        this.receivedDate = receivedDate;
        this.sellingDate = sellingDate;
        this.sellingPrice = sellingPrice;
        this.shippingCompany = shippingCompany;
        this.trackingNumber = trackingNumber;
    }

    /**
     * Returns the id of this positionImpl
     *
     * @return the id of this positionImpl
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the model of this positionImpl
     *
     * @return the model of this positionImpl
     */
    public @NotNull String getCategory() {
        return this.category;
    }

    /**
     * Returns the state of the position
     *
     * @return the state
     */
    public @NotNull State getState() {
        return this.state;
    }

    /**
     * Returns the purchasing price of this positionImpl
     *
     * @return the purchasing price of this positionImpl
     */
    public @NotNull BigDecimal getPurchasingPrice() {
        return this.purchasingPrice;
    }

    /**
     * Returns the order date of this positionImpl
     *
     * @return the order date of this positionImpl
     */
    public @NotNull LocalDate getOrderDate() {
        return this.orderDate;
    }

    /**
     * Returns the cost of this positionImpl excluding the {@link #purchasingPrice}
     *
     * @return the cost of this positionImpl excluding the purchasing price
     */
    public @NotNull BigDecimal getCost() {
        return this.cost;
    }

    /**
     * Returns the date on which this positionImpl has been received or null, if
     * this positionImpl was not received yet. Check the state before by {@link #getState()}
     *
     * @return the date this positionImpl has been received or null
     */
    public @Nullable LocalDate getReceivedDate() {
        return this.receivedDate;
    }

    /**
     * Returns the date of this positionImpl when it was sold
     * Check the state before by {@link #getState()}
     *
     * @return the selling date of this positionImpl
     */
    public @Nullable LocalDate getSellingDate() {
        return this.sellingDate;
    }

    /**
     * Returns the price of this positionImpl if it was sold or null if
     * this positionImpl was not sold yet.
     * Check the state before by {@link #getState()}
     *
     * @return the purchasing price or null
     */
    public @Nullable BigDecimal getSellingPrice() {
        return this.sellingPrice;
    }

    /**
     * Returns the shipping company which shipped this positionImpl or null if this positionImpl was
     * not shipped yet. Check the state before by {@link #getState()}
     *
     * @return the shipping company or null
     */
    public @Nullable ShippingCompany getShippingCompany() {
        return this.shippingCompany;
    }

    /**
     * Returns the tracking Number of this positionImpl in the corresponding
     * {@link #shippingCompany}. If this position can not be tracked, the trackingNumber is empty.
     *
     * @return the trackingNumber
     */
    public @Nullable String getTrackingNumber() {
        return this.trackingNumber;
    }

    /**
     * Returns the next id which should be used for an item. So the item can be added to this
     * positionImpl.
     *
     * @return the next item for an item
     */
    public int getNextItemId() {
        return this.nextItemId;
    }
}
