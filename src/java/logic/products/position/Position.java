package logic.products.position;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.products.item.Item;
import logic.products.Product;
import logic.products.item.ItemColor;
import logic.products.item.ItemData;
import utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static utils.BigDecimalUtils.calcPercent;
import static utils.BigDecimalUtils.isPositive;
import static utils.LocalDateUtils.areAcceptableDates;

/**
 * This class represents a position. Each Position can be identified by his id in the
 * matching {@link logic.saleBook.SaleBook}
 * A position can be in different states. Some information are only available on a specific state.
 * If this is the case, the methode will communicate this by his comment.
 *
 * @author xthe_white_lionx
 */
public class Position extends AbstractPosition implements Product, Comparable<Position> {

    /**
     * Items of this position
     */
    private final ObservableList<Item> items;

    /**
     * Constructor for an ordered position
     *
     * @param id              the id of the position
     * @param category        the category of this position and his items
     * @param orderDate       the date on which the position was ordered
     * @param purchasingPrice the purchasing price of this position assumed by his items
     */
    public Position(int id, @NotNull String category, @NotNull LocalDate orderDate,
                    @NotNull BigDecimal purchasingPrice, @NotNull BigDecimal cost) {
        super(id, category, orderDate, purchasingPrice, cost);
        this.items = FXCollections.observableList(new ArrayList<>());
    }

    /**
     * Constructor for a position
     *
     * @param id              the id of the position
     * @param category        the category of this position and his items
     * @param orderDate       the date on which the position were ordered
     * @param purchasingPrice the purchasing price of this position assumed by his items
     * @param state           the current state of the position
     * @param cost            the common cost of the position
     * @param receivedDate    the date where the position where received
     * @param sellingDate     the selling date of the position
     * @param sellingPrice    the price on which the position were sold
     * @param shippingCompany the company which shipped the position
     * @param trackingNumber  the tracking number of the position in the shipping company
     * @param items           the items of the position
     */
    public Position(int id, @NotNull String category, @NotNull LocalDate orderDate,
                    @NotNull BigDecimal purchasingPrice,
                    @NotNull State state, @NotNull BigDecimal cost, LocalDate receivedDate,
                    LocalDate sellingDate, BigDecimal sellingPrice,
                    ShippingCompany shippingCompany, @NotNull String trackingNumber,
                    @NotNull List<Item> items) {
        super(id, category, orderDate, purchasingPrice, state, cost, receivedDate, sellingDate,
                sellingPrice, shippingCompany, trackingNumber);
        this.items = FXCollections.observableList(items);
    }

    /**
     * Constructor for a position
     *
     * @param positionData the data of one position
     * @param nameToItemColorMap
     */
    public Position(PositionData positionData, Map<String, ItemColor> nameToItemColorMap) {
        super(positionData.id, positionData.category, positionData.orderDate, positionData.purchasingPrice,
                positionData.state, positionData.cost, positionData.receivedDate, positionData.sellingDate,
                positionData.sellingPrice, positionData.shippingCompany, positionData.trackingNumber);

        ItemData[] itemData = positionData.getItemData();
        List<Item> itemList = new ArrayList<>();
        for (ItemData itemDatum : itemData) {
            itemList.add(new Item(itemDatum, nameToItemColorMap.get(itemDatum.getItemColorName())));
        }

        this.items = FXCollections.observableList(itemList);
        this.nextItemId = positionData.nextItemId;
    }

    /**
     * Returns the observableList of the items of this position
     *
     * @return the items of this position as observableList
     */
    public @NotNull ObservableList<Item> getItemObservableList() {
        return this.items;
    }

    /**
     * Returns the items of this position
     *
     * @return the items of this position
     */
    public @NotNull List<Item> getItems() {
        return new ArrayList<>(this.items);
    }

    /**
     * Sets the category of this position
     *
     * @param category the new category of this position
     */
    public void setCategory(@NotNull String category) {
        this.category = category;
    }

    /**
     * Sets the purchasingPrice of this position
     *
     * @param purchasingPrice the new purchasingPrice
     * @throws IllegalArgumentException if the specified purchasingPrice is less than 0
     */
    public void setPurchasingPrice(@NotNull BigDecimal purchasingPrice) {
        if (purchasingPrice.doubleValue() <= 0) {
            throw new IllegalArgumentException(String.format("purchasing price must be greater " +
                    "equals 0 but is %s", purchasingPrice));
        }

        this.purchasingPrice = purchasingPrice;
    }

    /**
     * Sets the orderDate of this position.
     * If the state of this position is not ordered then the receiveDate and sellingDate shall be considered
     *
     * @param orderDate the new orderDate of this position
     * @throws IllegalArgumentException if the orderDate is after the receivedDate or
     *                                  the orderDate is after the sellingDate
     */
    public void setOrderDate(@NotNull LocalDate orderDate) {
        if (this.receivedDate != null && !areAcceptableDates(orderDate, this.receivedDate)) {
            throw new IllegalArgumentException("orderDate is after receivedDate");
        }
        if (this.sellingDate != null && !areAcceptableDates(orderDate, this.sellingDate)) {
            throw new IllegalArgumentException("orderDate is after sellingDate");
        }
        this.orderDate = orderDate;
    }

    /**
     * Sets the cost of this position
     *
     * @param cost the new cost
     * @throws IllegalArgumentException if the cost is negative
     */
    public void setCost(@NotNull BigDecimal cost) {
        if (!BigDecimalUtils.isPositive(cost)) {
            throw new IllegalArgumentException("cost must be greater equals 0 but is " + cost);
        }
        this.cost = cost;
    }

    /**
     * Sets the sellingDate of this position. This methode shall be only called if
     * the state of this position is at least sold otherwise, use the {@link #sale(LocalDate, BigDecimal)} methode.
     *
     * @param sellingDate the new sellingDate
     * @throws IllegalStateException    if the state is not at least sold
     * @throws IllegalArgumentException if the specified sellingDate is before the orderDate or
     *                                  before the receivedDate
     */
    public void setSellingDate(@NotNull LocalDate sellingDate) {
        if (this.state.compareTo(State.SOLD) < 0) {
            throw new IllegalStateException(String.format("current state is %s but must be at " +
                    "least sold", this.state));
        }

        if (!areAcceptableDates(this.orderDate, sellingDate)) {
            throw new IllegalArgumentException("sellingDate is before orderDate");
        }

        if (!areAcceptableDates(this.receivedDate, sellingDate)) {
            throw new IllegalArgumentException("sellingDate is before receivedDate");
        }
        this.sellingDate = sellingDate;
    }

    /**
     * Sets the sellingPrice of this position. This methode shall be only called if
     * the state of this position is at least sold otherwise, use the {@link #sale(LocalDate, BigDecimal)} methode.
     *
     * @param sellingPrice the new sellingPrice
     * @throws IllegalStateException    if the state is not at least sold
     * @throws IllegalArgumentException if the specified sellingPrice is less than 0
     */
    public void setSellingPrice(@NotNull BigDecimal sellingPrice) {
        if (!this.isSold()) {
            throw new IllegalStateException(String.format("position must be at least sold but " +
                    "current state is %s least", this.state));
        }
        if (sellingPrice.doubleValue() < 0) {
            throw new IllegalArgumentException(String.format("selling price must be greater 0 " +
                    "but is %s", sellingPrice));
        }

        this.sellingPrice = sellingPrice;
    }

    /**
     * Sets the shippingCompany of this position. This methode shall be only called if
     * the state of this position is at least shipped otherwise,
     * use the {@link #send(ShippingCompany, String, BigDecimal)} methode.
     *
     * @param shippingCompany the new shippingCompany
     * @throws IllegalStateException if the state of this position is not at least shipped
     */
    public void setShippingCompany(@NotNull ShippingCompany shippingCompany) {
        if (this.state.compareTo(State.SHIPPED) < 0) {
            throw new IllegalStateException(String.format("position must be at least shipped but " +
                    "current state is %s least", this.state));
        }

        this.shippingCompany = shippingCompany;
    }

    /**
     * Sets the trackingNumber of this position. This methode shall be only called if
     * the state of this position is at least shipped otherwise,
     * use the {@link #send(ShippingCompany, String, BigDecimal)} methode.
     *
     * @param trackingNumber the new trackingNumber
     * @throws IllegalStateException if the state of this position is not at least shipped
     */
    public void setTrackingNumber(@NotNull String trackingNumber) {
        if (this.state.compareTo(State.SHIPPED) < 0) {
            throw new IllegalStateException(String.format("position must be at least shipped but " +
                    "current state is %s least", this.state));
        }

        this.trackingNumber = trackingNumber;
    }

    /**
     * Sets the state of this position.
     * Normally the state will changed automatically by using the right methods
     * like {@link #sale(LocalDate, BigDecimal)}. So keep in mind that changing the state can make it
     * necessary to also change the associated fields. For example if the state is set to the state sold
     * the fields sellingDate and sellingPrice should be set too.
     *
     * @param state the new state
     */
    public void setState(@NotNull State state) {
        this.state = state;
    }

    /**
     * Adds the specified newCost to the current cost of this position
     *
     * @param newCost the cost which will be added
     * @throws IllegalArgumentException if the specified newCost is less than 0.
     */
    public void addCost(@NotNull BigDecimal newCost) {
        if (!BigDecimalUtils.isPositive(newCost)) {
            throw new IllegalArgumentException(String.format("new cost must be greater equals " +
                    "0 but is %s", newCost));
        }

        this.cost = this.cost.add(newCost);
    }

    /**
     *
     * @return
     */
    public BigDecimal getTotalCost() {
        return this.purchasingPrice.add(this.cost);
    }

    /**
     * Sets the receivedDate of this position, if this position is not received yet then use
     * the methode {@link #setReceived(LocalDate)}
     *
     * @param receivedDate the date on which the position was received
     * @throws IllegalArgumentException if the specified receivedDate is before the orderDate
     */
    public void setReceivedDate(@NotNull LocalDate receivedDate) {
        if (this.state.compareTo(State.RECEIVED) < 0){
            throw new IllegalStateException(String.format("current state is %s but must be at " +
                    "least received", this.state));
        }
        if (!areAcceptableDates(this.orderDate, receivedDate)) {
            throw new IllegalArgumentException("received date is before order date");
        }
        this.receivedDate = receivedDate;
    }

    /**
     * Sets the position state to received and the specified receivedDate
     *
     * @param receivedDate the date on which the position was received
     */
    public void setReceived(@NotNull LocalDate receivedDate) {
        this.state = State.RECEIVED;
        this.setReceivedDate(receivedDate);
    }

    /**
     * Sets the sellingPrice and the sellingDate of the position.
     * The state will be set to the state sold.
     *
     * @param sellingDate  the specified sellingDate on which the position was sold
     * @param sellingPrice the specified sellingPrice for which the position was sold
     * @throws IllegalArgumentException if the specified sellingPrice is less than 0,
     *                                  the specified sellingDate is before the orderDate or before the receivedDate
     */
    public void sale(@NotNull LocalDate sellingDate, @NotNull BigDecimal sellingPrice) {
        if (sellingPrice.doubleValue() < 0) {
            throw new IllegalArgumentException(String.format("selling price must be greater 0 " +
                    "but is %s", sellingPrice));
        }

        if (!areAcceptableDates(this.orderDate, sellingDate)) {
            throw new IllegalArgumentException("sellingDate is before orderDate");
        }

        if (!areAcceptableDates(this.receivedDate, sellingDate)) {
            throw new IllegalArgumentException("sellingDate is before receivedDate");
        }

        this.state = State.SOLD;
        this.sellingDate = sellingDate;
        this.sellingPrice = sellingPrice;
    }

    /**
     * Sets the shippingCompany, the trackingNumber and adds the shippingCost to this position.
     * The state will be set to the state shipped.
     *
     * @param shippingCompany the company on which the position was shipped
     * @param trackingNumber  the number to track the position on the shippingCompany website
     * @param shippingCost    the cost of shipping the position
     * @throws IllegalArgumentException if the specified newCost is less than 0
     */
    public void send(@NotNull ShippingCompany shippingCompany, @NotNull String trackingNumber,
                     @NotNull BigDecimal shippingCost) {

        this.state = State.SHIPPED;
        this.shippingCompany = shippingCompany;
        this.trackingNumber = trackingNumber;
        this.addCost(shippingCost);
    }

    /**
     * Returns the holding period between the receivedDate and the sellingDate of the position.
     * If the position isn't sold yet, the holding period until today will be returned.
     *
     * @return holding period in days
     */
    public @Nullable Long calcHoldingPeriod() {
        if (this.receivedDate == null) {
            return null;
        }
        return DAYS.between(this.receivedDate,
                Objects.requireNonNullElseGet(this.sellingDate, LocalDate::now));
    }

    /**
     * Returns a calculated performance for this position depending on the specified sellingPrice.
     * The performance is defined as the spread between the specified
     * sellingPrice and all costs, including the purchasingPrice of this position
     *
     * @param sellingPrice the price on which this position should be sold
     * @return the calculated performance for this position if sold
     * @throws IllegalArgumentException if the specified selling price is not positive (&ge; 0)
     */
    public @NotNull BigDecimal calcPerformance(@NotNull BigDecimal sellingPrice) {
        if (!isPositive(sellingPrice)) {
            throw new IllegalArgumentException(String.format("sellingPrice must be greater equals" +
                    " 0 but is %s", sellingPrice));
        }
        return sellingPrice.subtract(this.purchasingPrice.add(this.cost)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns the performance of this position. The performance is defined as the spread between the specified
     * sellingPrice and all costs, including the purchasingPrice of this position.
     * If this position is not sold yet, then all costs will be returned as one negative sum.
     *
     * @return the calculated performance of this position
     */
    public @NotNull BigDecimal calcPerformance() {
        if (!this.isSold()) {
            return this.purchasingPrice.add(this.cost).negate();
        }
        return this.calcPerformance(this.sellingPrice);
    }

    /**
     * Returns the performance of this position in percent. The performance is defined as the spread between the specified
     * sellingPrice and all costs, including the purchasingPrice of this position.
     * For the percent calculation, the sellingPrice will be used as base.
     * If this position is not sold yet, then all costs will be returned as one negative sum.
     *
     * @return the calculated performance of this position in percent or null if this position is not sold yet
     */
    public @Nullable BigDecimal calcPercentPerformance() {
        if (!this.isSold()) {
            return null;
        }
        return calcPercent(this.sellingPrice, this.calcPerformance());
    }

    /**
     * Adds the specified item to this position, if the id of the specified item is already in use in this position.
     * To get an id for an item use the {@link #getNextItemId()} methode.
     *
     * @param item the item to be added to this position
     * @return {@code true} if the item was successfully added, otherwise {@code false}
     * @throws IllegalArgumentException if the id of the specified item is already used
     */
    public boolean addItem(@NotNull Item item) {
        int itemId = item.getId();
        if (this.nextItemId != itemId){
            throw new IllegalArgumentException("expected id is %d but is %d".formatted(this.nextItemId, itemId));
        }
        boolean added = this.items.add(item);
        if (added){
            this.nextItemId++;
        }
        return added;
    }

    /**
     * Removes and returns the item with the matching id or null if there is no item with the specified itemId
     *
     * @param itemId the id of the item that should be removed
     * @return the removed item or null
     */
    public @Nullable Item removeItemById(int itemId) {
        for (int i = 0; i < this.items.size(); i++) {
            Item item = this.items.get(i);
            if (item.getId() == itemId) {
                return this.items.remove(i);
            }
        }
        return null;
    }

    /**
     * Returns true if this position is at least sold
     *
     * @return true if this position is at least sold, otherwise false
     */
    public boolean isSold() {
        return this.state.compareTo(State.SOLD) >= 0;
    }

    /**
     * Combines this position and the specified that position to this position,
     * if both position have at least the state received.
     *
     * @param positionId the id of the combined position
     * @param that       the position to combine with
     * @return this position with the specified positionId and the combined information
     * @throws IllegalStateException if this or that position is not at least received or repaired
     */
    public @NotNull Position combine(int positionId, @NotNull Position that) {
        if (this.state != State.RECEIVED && this.state != State.REPAIRED) {
            throw new IllegalStateException("this position must be at least received or repaired " +
                    "but is " + this.state);
        }
        if (that.state != State.RECEIVED && that.state != State.REPAIRED) {
            throw new IllegalStateException("that position must be at least received or repaired " +
                    "but is " + that.state);
        }

        this.id = positionId;
        if (that.orderDate.isBefore(this.orderDate)) {
            this.orderDate = that.orderDate;
        }
        this.purchasingPrice = this.purchasingPrice.add(that.purchasingPrice);
        this.addCost(that.cost);
        if (that.receivedDate.isBefore(this.receivedDate)) {
            this.receivedDate = that.receivedDate;
        }
        for (Item item : that.items) {
            item.setId(this.nextItemId);
            this.addItem(item);
        }

        return this;
    }

    /**
     * Divides the position with the specified positionId. For Each item of the position, a new
     * position will be created.
     *
     * @param positionIds the id of the position which should be divided
     * @return array of positions, where each position has an item of this position or null,
     * if this position has not enough items to share
     * @throws IllegalArgumentException if to few position ids were given
     */
    public @Nullable Position[] divide(int @NotNull [] positionIds) {
        int itemNumber = this.items.size();
        if (positionIds.length < itemNumber - 1) {
            throw new IllegalArgumentException(String.format("to few ids. %d ids given but %d " +
                            "needed",
                    positionIds.length, itemNumber - 1));
        }

        Position[] positions = new Position[itemNumber - 1];

        if (itemNumber >= 2) {
            this.purchasingPrice = this.purchasingPrice
                    .divide(BigDecimal.valueOf(itemNumber), RoundingMode.HALF_UP);
            this.cost = this.cost
                    .divide(BigDecimal.valueOf(itemNumber), RoundingMode.HALF_UP);
            if (this.sellingPrice != null) {
                this.sellingPrice = this.sellingPrice.divide(BigDecimal.valueOf(itemNumber),
                        RoundingMode.HALF_UP);
            }

            for (int i = 0; i < positionIds.length; i++) {
                Position newPosition = new Position(positionIds[i], this.category,
                        this.orderDate, this.purchasingPrice, this.cost);

                if (this.state != State.ORDERED) {
                    switch (this.state) {
                        case DELIVERED:
                        case SHIPPED:
                            newPosition.send(this.shippingCompany,
                                    this.trackingNumber, BigDecimal.ZERO);
                        case SOLD:
                            newPosition.sale(this.sellingDate, this.sellingPrice);
                        case REPAIRED:
                        case RECEIVED:
                            newPosition.receivedDate = this.receivedDate;
                    }
                    newPosition.state = this.state;
                }

                Item item = this.items.remove(1);
                this.nextItemId--;
                item.setId(newPosition.nextItemId);
                newPosition.addItem(item);
                positions[i] = newPosition;
            }
            return positions;
        }
        return null;
    }

//    @Override
//    public String getSimpleName() {
//        return this.getClass().getSimpleName();
//    }

    /**
     * Compares this position to the specified other position.
     * This position is less than the specified other position if the id is less than the other position.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this position
     * is less than, equal to, or greater than the specified other position.
     */
    @Override
    public int compareTo(@NotNull Position other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return Objects.equals(this.category, position.category) && Objects.equals(this.orderDate, position.orderDate) &&
                Objects.equals(this.purchasingPrice, position.purchasingPrice) &&
                Objects.equals(this.items, position.items) && this.state == position.state &&
                Objects.equals(this.cost, position.cost) &&
                Objects.equals(this.receivedDate, position.receivedDate) &&
                Objects.equals(this.sellingDate, position.sellingDate) &&
                Objects.equals(this.sellingPrice, position.sellingPrice) &&
                this.shippingCompany == position.shippingCompany &&
                Objects.equals(this.trackingNumber, position.trackingNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.category, this.orderDate, this.purchasingPrice, this.items, this.state, this.cost, this.receivedDate, this.sellingDate,
                this.sellingPrice, this.shippingCompany, this.trackingNumber);
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + this.id +
                ", items=" + this.items +
                ", nextItemId=" + this.nextItemId +
                ", category='" + this.category + '\'' +
                ", state=" + this.state +
                ", purchasingPrice=" + this.purchasingPrice +
                ", orderDate=" + this.orderDate +
                ", cost=" + this.cost +
                ", receivedDate=" + this.receivedDate +
                ", sellingDate=" + this.sellingDate +
                ", sellingPrice=" + this.sellingPrice +
                ", shippingCompany=" + this.shippingCompany +
                ", trackingNumber='" + this.trackingNumber + '\'' +
                '}';
    }

}
