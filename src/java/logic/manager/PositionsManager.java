package logic.manager;

import gui.FXutils.LabelUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import logic.GUIConnector;
import logic.products.item.Item;
import logic.products.position.Position;
import logic.products.position.AbstractPosition;
import logic.products.position.ShippingCompany;
import logic.products.position.State;
import gui.FXutils.FXCollectionsUtils;
import logic.saleBook.SaleBook;
import logic.sparePart.SparePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * @author xthe_white_lionx
 */
public class PositionsManager {
    /**
     * ObservableMap of positions mapped to their matching id
     */
    private final ObservableMap<Integer, Position> idToPositionObsMap;

    /**
     * the id for the next creation of a position
     */
    private int nextPosId;

    /**
     * Connection to the gui
     */
    private final GUIConnector gui;

    /**
     * The saleBook
     */
    private final SaleBook saleBook;

    /**
     * Constructor
     *
     * @param saleBook the saleBook
     * @param gui      connection to the gui
     */
    public PositionsManager(@NotNull SaleBook saleBook, @NotNull GUIConnector gui) {
        this.saleBook = saleBook;
        this.gui = gui;
        this.idToPositionObsMap = FXCollections.observableMap(new TreeMap<>());
        this.nextPosId = 1;
    }

    /**
     * Constructor
     *
     * @param saleBook  the saleBook
     * @param positions the positions which should be managed
     * @param nextPosId the id for the next position
     * @param gui       connection to the gui
     */
    public PositionsManager(@NotNull SaleBook saleBook, @NotNull Position[] positions,
                            int nextPosId, @NotNull GUIConnector gui) {
        this.saleBook = saleBook;
        this.gui = gui;
        this.idToPositionObsMap = FXCollectionsUtils.toObservableMap(positions, AbstractPosition::getId);
        this.nextPosId = nextPosId;
    }

    /**
     * @return
     */
    //TODO 27.05.2024 needed?
    public ObservableMap<Integer, Position> getIdToPositionObsMap() {
        return this.idToPositionObsMap;
    }

    /**
     * Returns the positions of this positionsManager
     *
     * @return the positions of this positionsManager
     */
    public @NotNull Collection<Position> getPositions() {
        return new HashSet<>(this.idToPositionObsMap.values());
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
     * Adds the specified {@code position} to this positionsManager
     *
     * @param position the position which should be added
     * @return {@code true} if the position was successfully added, otherwise {@code false}
     * @throws IllegalArgumentException if the id of the specified position is already used
     */
    public boolean addPosition(@NotNull Position position) {
        int id = position.getId();
        if (this.nextPosId != id) {
            throw new IllegalArgumentException("expected id is %d but is %d".formatted(this.nextPosId, id));
        }
        Position oldValue = this.idToPositionObsMap.putIfAbsent(position.getId(), position);
        if (oldValue == null) {
            this.saleBook.addCategory(position.getCategory());
            this.saleBook.addVariableCosts(position.getTotalCost());
            this.nextPosId++;
            if (position.isSold()) {
                this.saleBook.addSale(position.getSellingPrice());
            }
            this.gui.updateStatus(String.format("position %d successfully added", position.getId()));
            return true;
        }
        return false;
    }

    /**
     * Adds the specified item to the position with the specified posId
     *
     * @param posId id of the position to which the item should be added
     * @param item  the item which should be added
     * @return {@code true} if the item was successfully added, otherwise {@code false}
     * @throws IllegalArgumentException if no position with the specified posId exist
     */
    public boolean addItemToPosition(int posId, @NotNull Item item) {
        Position position = this.idToPositionObsMap.get(posId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + posId);
        }
        boolean added = position.addItem(item);
        if (added) {
            this.gui.updateStatus(String.format("item %d of position %d successfully added", item.getId(), posId));
        }

        return added;
    }

    /**
     * Removes the position with the specified id and returns it.
     *
     * @param id the id of the searched position
     * @return the removed position
     */
    public @Nullable Position removePosition(int id) {
        Position position = this.idToPositionObsMap.remove(id);
        if (position != null) {
            this.saleBook.subtractVariableCosts(position.getTotalCost());
            if (position.isSold()) {
                this.saleBook.subtractSale(position.getSellingPrice());
            }
            this.gui.updateStatus(String.format("position %d successfully deleted", id));
        }
        return position;
    }

    /**
     * Removes the item with the specified itemId of the position with the specified positionId
     *
     * @param positionId the id of the position which inherits the item
     * @param itemId     the id of the item which should be removed
     * @return the removed item
     * @throws IllegalArgumentException if there is no position with the specified positionId or
     *                                  if the target position doesn't contain the item with the specified itemId
     */
    public @Nullable Item removeItem(int positionId, int itemId) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        if (position.itemCount() <= 1) {
            throw new IllegalArgumentException("a position must have at least 1 item");
        }

        Item removedItem = position.removeItemById(itemId);
        if (removedItem != null) {
            this.gui.displayPositions(this.getPositions());
            this.gui.updateStatus(String.format("item %d of position %d successfully deleted", itemId, positionId));
        }

        return removedItem;
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
        this.gui.refreshPosition();
        this.gui.updateStatus(String.format("position %d set on received", positionId));
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
        this.saleBook.addVariableCosts(newCost);
        this.gui.refreshPosition();
        this.gui.updateStatus(String.format("cost %.2f %s add to position %d", newCost, LabelUtils.SYMBOL_OF_CURRENCY, positionId));
    }

    /**
     * Sets the position with the specified positionId to the state repaired and adjusts the stock
     * of the spareParts depending on the specified sparePartsToCount map.
     * The sparePartToCount map represents the number of used spareParts.
     *
     * @param positionId        the id of the position which should be repaired
     * @param sparePartsToCount the used spare parts mapped to their amount
     * @throws IllegalArgumentException if there is no position with the specified positionId or
     *                                  sparePartsToCount is null
     */
    public void repairPosition(int positionId, @NotNull Map<SparePart, Integer> sparePartsToCount) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }

        this.saleBook.getSparePartsManager().useSparParts(sparePartsToCount);
        position.setState(State.REPAIRED);
        this.gui.refreshPosition();
        this.gui.updateStatus(String.format("position %d repaired", positionId));
    }

    /**
     * Sales the position with the specified positionId.
     *
     * @param positionId   the id of the position which should be sale
     * @param sellingDate  date on which the position is sold
     * @param sellingPrice price on which the position is sold
     * @throws IllegalArgumentException if there is no position with the specified positionId
     */
    public void sale(int positionId, @NotNull LocalDate sellingDate, @NotNull BigDecimal sellingPrice) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        position.sale(sellingDate, sellingPrice);
        this.gui.refreshPosition();
        this.gui.updateStatus(String.format("position %d sold", positionId));
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
    public void shipped(int positionId, @NotNull ShippingCompany shippingCompany, @NotNull String trackingNumber, @NotNull BigDecimal shippingCost) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        position.send(shippingCompany, trackingNumber, shippingCost);
        this.saleBook.addVariableCosts(shippingCost);
        this.gui.refreshPosition();
        this.gui.updateStatus(String.format("position %d shipped", positionId));
    }

    /**
     * Divides the position with the specified positionId. For Each item of the position, a new
     * position will be created.
     *
     * @param positionId the id of the position which should be divided
     * @return the new created positions
     * @throws IllegalArgumentException if there is no position with the specified positionId
     */
    public @Nullable Position[] dividePosition(int positionId) {
        Position oldPosition = this.idToPositionObsMap.get(positionId);
        if (oldPosition == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }

        int[] positionIds = new int[oldPosition.itemCount() - 1];
        for (int i = 0, positionIdsLength = positionIds.length; i < positionIdsLength; i++) {
            positionIds[i] = this.nextPosId++;
        }

        Position[] positions = oldPosition.divide(positionIds);
        if (positions.length > 0) {
            StringBuilder builder = new StringBuilder("position %d divided in ");
            builder.append(positionId);

            for (int i = 0, positionsLength = positions.length; i < positionsLength; i++) {
                builder.append(" ");
                Position position = positions[i];
                int id = position.getId();
                this.idToPositionObsMap.put(id, position);
                builder.append(id);
                if (i < positionsLength - 1) {
                    builder.append(",");
                }
            }
            this.gui.refreshPosition();
            this.gui.updateStatus(builder.toString());
        }

        return positions;
    }

    /**
     * Combines the position with the specified positionId with the positions with the specified positionIds.
     * All positions have to be at least at the state received
     *
     * @param positionId  the id of one position which should be combined
     * @param positionIds the id of the other position which should be combined with
     * @return
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
        this.gui.refreshPosition();
        this.gui.updateStatus("combined to the new position " + this.nextPosId);
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
        return this.nextPosId == that.nextPosId && Objects.equals(this.idToPositionObsMap, that.idToPositionObsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idToPositionObsMap, this.nextPosId);
    }

    @Override
    public String toString() {
        return "PositionsManager{" + "idToPositionObsMap=" + this.idToPositionObsMap + ", nextPosId=" + this.nextPosId + '}';
    }
}
