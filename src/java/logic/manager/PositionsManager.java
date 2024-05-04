package logic.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import logic.products.item.Item;
import logic.products.position.Position;
import logic.products.position.AbstractPosition;
import logic.products.position.ShippingCompany;
import logic.products.position.State;
import gui.FXutils.FXCollectionsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class PositionsManager {
    /**
     * ObservableMap of positions mapped to their matching id
     */
    private final ObservableMap<Integer, Position> idToPositionObsMap;

    /**
     * ObservableSet of the categories of this positionsManager
     */
    private final Set<String> categories;

    /**
     * the id for the next creation of a position
     */
    private int nextPosId;

    /**
     *
     */
    public PositionsManager() {
        this.idToPositionObsMap = FXCollections.observableMap(new TreeMap<>());
        this.categories = new TreeSet<>();
        this.nextPosId = 1;
    }

    /**
     *
     * @param positions
     * @param nextPosId
     */
    public PositionsManager(@NotNull Position[] positions, int nextPosId) {
        this.idToPositionObsMap = FXCollectionsUtils.toObservableMap(positions,
                AbstractPosition::getId);

        this.categories = new TreeSet<>();
        for (Position position : positions) {
            this.categories.add(position.getCategory());
        }
        this.nextPosId = nextPosId;
    }

    /**
     *
     * @return
     */
    public ObservableMap<Integer, Position> getIdToPositionObsMap() {
        return this.idToPositionObsMap;
    }

    /**
     * Returns the positions of this positionsManager
     *
     * @return the positions of this positionsManager
     */
    public @NotNull Collection<Position> getPositions() {
        return this.idToPositionObsMap.values();
    }

    /**
     * Returns the categories of the positions
     *
     * @return positions categories
     */
    public @NotNull Set<String> getCategories() {
        return this.categories;
    }

    /**
     * Returns the next id for a position
     *
     * @return the next id for a position
     */
    public int getNextPosId() {
        return this.nextPosId;
    }

    /**
     * Adds the specified position to this positionsManager
     *
     * @param position the position which should be added
     * @throws IllegalArgumentException if the id of the specified position is already used
     */
    public boolean addPosition(@NotNull Position position) {
        int id = position.getId();
        if (this.nextPosId != id) {
            throw new IllegalArgumentException("expected id is %d but is %d".formatted(
                    this.nextPosId, id));
        }
        Position oldValue = this.idToPositionObsMap.putIfAbsent(position.getId(), position);
        if (oldValue == null){
            this.categories.add(position.getCategory());
            this.nextPosId++;
            return true;
        }
        return false;
    }

    /**
     * Adds the specified item to the position with the specified posId
     *
     * @param posId id of the position to which the item should be added
     * @param item  the item which should be added
     * @throws IllegalArgumentException if no position with the specified posId exist
     */
    public boolean addItemToPosition(int posId, @NotNull Item item) {
        Position position = this.idToPositionObsMap.get(posId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + posId);
        }

        return position.addItem(item);
    }

    /**
     * Removes the position with the specified id and returns it.
     *
     * @param id the id of the searched position
     */
    public @Nullable Position removePosition(int id) {
        Position position = this.idToPositionObsMap.remove(id);

        if (position != null) {
            String deletedModel = position.getCategory();
            boolean containsModel = false;
            for (Position pos : this.idToPositionObsMap.values()) {
                if (pos.getCategory().equals(deletedModel)) {
                    containsModel = true;
                    break;
                }
            }
            if (!containsModel) {
                this.categories.remove(deletedModel);
            }
        }
        return position;
    }

    /**
     * Removes the item with the specified itemId of the position with the specified positionId
     *
     * @param positionId the id of the position which inherits the item
     * @param itemId     the id of the item which should be removed
     * @throws IllegalArgumentException if there is no position with the specified positionId or
     *                                  if the target position doesn't contain the item with the specified itemId
     */
    public @Nullable Item removeItem(int positionId, int itemId) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        if (position.getItems().size() <= 1) {
            throw new IllegalArgumentException("a position must have at least 1 item");
        }

        return position.removeItemById(itemId);
    }

    /**
     * Sets the position with the specified positionId to the state received and the received date of this position
     * to the receivedDate
     *
     * @param positionId   the id of the searched position
     * @param receivedDate the date on which the position was received
     * @throws IllegalArgumentException if there is no position with the specified positionId
     */
    public void setReceived(int positionId, @NotNull LocalDate receivedDate) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        position.setReceived(receivedDate);
    }

    /**
     * Adds the specified newCost to the position with the specified positionId
     *
     * @param positionId the id of the searched position
     * @param newCost    the cost which should be added
     * @throws IllegalArgumentException if there is no position with the specified positionId
     */
    public void addCostToPosition(int positionId, @NotNull BigDecimal newCost) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        position.addCost(newCost);
    }

    /**
     * Sets the position with the specified positionId to the state repaired and adjusts the stock
     * of the spareParts depending on the specified sparePartsToCount map.
     * The sparePartToCount map represents the number of used spareParts.
     *
     * @param positionId        the id of the position which should be repaired
     * @throws IllegalArgumentException if there is no position with the specified positionId or
     *                                  sparePartsToCount is null
     */
    public void repairPosition(int positionId) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }

        position.setState(State.REPAIRED);
    }

    /**
     * Sales the position with the specified positionId.
     *
     * @param positionId   the id of the position which should be sale
     * @param sellingDate  date on which the position is sold
     * @param sellingPrice price on which the position is sold
     * @throws IllegalArgumentException if there is no position with the specified positionId
     */
    public void sale(int positionId, @NotNull LocalDate sellingDate,
                     @NotNull BigDecimal sellingPrice) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        position.sale(sellingDate, sellingPrice);
    }

    /**
     * Sets the position with the specified positionId to the state shipped and adds the
     * shipping data.
     *
     * @param positionId      the id of the position which should be shipped
     * @param shippingCompany the shipping company of the shipped position
     * @param trackingNumber  tracking number of the shipped position in the shipping company,
     *                        choose empty if the position has no tracking number
     * @param shippingCost    cost of the shipping for the position
     * @throws IllegalArgumentException if there is no position with the specified positionId
     */
    public void shipped(int positionId, @NotNull ShippingCompany shippingCompany,
                        @NotNull String trackingNumber, @NotNull BigDecimal shippingCost) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        position.send(shippingCompany, trackingNumber, shippingCost);
    }

    /**
     * Divides the position with the specified positionId. For Each item of the position, a new
     * position will be created.
     *
     * @param positionId the id of the position which should be divided
     * @throws IllegalArgumentException if there is no position with the specified positionId
     */
    public @Nullable Position[] dividePosition(int positionId) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }

        int[] positionIds = new int[position.getItems().size() - 1];
        for (int i = 0, positionIdsLength = positionIds.length; i < positionIdsLength; i++) {
            positionIds[i] = this.nextPosId++;
        }

        return position.divide(positionIds);
    }

    /**
     * Combines the position with the specified positionId with the positions with the specified positionIds.
     * All positions have to be at least at the state received
     *
     * @param positionId  the id of one position which should be combined
     * @param positionIds the id of the other position which should be combined with
     * @throws IllegalArgumentException if no positionId to combine with were given or
     *                                  some positionId does not match to any position
     */
    public @NotNull Position combinePositions(int positionId, int @NotNull ... positionIds) {
        if (positionIds.length < 1) {
            throw new IllegalArgumentException("");
        }

        for (int currId : positionIds) {
            if (!this.idToPositionObsMap.containsKey(currId)) {
                throw new IllegalArgumentException("no position found for id " + currId);
            }
        }

        Position combindPosition = this.removePosition(positionId);

        for (int id : positionIds) {
            Position currPosition = this.removePosition(id);
            combindPosition = combindPosition.combine(this.nextPosId, currPosition);
        }
        this.idToPositionObsMap.put(this.nextPosId, combindPosition);
        this.nextPosId++;
        return combindPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionsManager that)) {
            return false;
        }
        return this.nextPosId == that.nextPosId &&
                Objects.equals(this.idToPositionObsMap, that.idToPositionObsMap) &&
                Objects.equals(this.categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idToPositionObsMap,
                this.categories,
                this.nextPosId);
    }

    @Override
    public String toString() {
        return "PositionsManager{" +
                "idToPositionObsMap=" + this.idToPositionObsMap +
                ", categoriesObsSet=" + this.categories +
                ", nextPosId=" + this.nextPosId +
                '}';
    }
}
