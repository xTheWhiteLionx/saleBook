package logic.saleBook;

import gui.Message;
import gui.ObservableListMapBinder;
import gui.ObservableListSetBinder;
import gui.ObservableTreeItemMapBinder;
import gui.util.LabelUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import logic.GUIConnector;
import logic.ItemColor;
import logic.SparePart;
import logic.order.Order;
import logic.order.Supplier;
import logic.products.Item;
import logic.products.position.Position;
import logic.products.position.PositionData;
import logic.products.position.ShippingCompany;
import logic.products.position.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

/**
 * This class contains the program's logic.
 *
 * @author xthe_white_lionx
 */
public class SaleBook extends SaleBookImpl {

    /**
     * Set of the name of the spareParts
     */
    private final Set<String> sparePartNames = new HashSet<>();
    /**
     * Set of units of the sparParts
     */
    private final Set<String> sparePartUnits = new HashSet<>();
    /**
     * ObservableSet of the categories of this saleBook
     */
    private ObservableSet<String> categoryObsSet;

    /**
     * ObservableSet of the spareParts of this saleBook
     */
    private final ObservableSet<SparePart> sparePartsObsSet;

    /**
     * ObservableMap of positions mapped to their matching id
     */
    private ObservableMap<Integer, Position> idToPositionObsMap;
    /**
     * ObservableMap of suppliers mapped their matching name
     */
    private final ObservableMap<String, Supplier> nameToSupplierObsMap;
    /**
     * ObservableMap of orders mapped to their matching id
     */
    private final ObservableMap<Integer, Order> idToOrderObsMap;
    /**
     * Map of itemColors mapped to their occurrence in this saleBook
     */
    private final Map<ItemColor, Integer> itemColorOccurrences = new HashMap<>();
    /**
     * Map of ItemColors mapped to their names
     */
    private final Map<String, ItemColor> nameToItemColor = new HashMap<>();
    /**
     * Volume of the sales
     */
    private BigDecimal salesVolume = BigDecimal.ZERO;

    /**
     * The sum of all the variable costs
     */
    private BigDecimal variableCosts = BigDecimal.ZERO;

    /**
     * Connection to the gui.
     */
    private final GUIConnector gui;

    /**
     * Constructor for a sale book.
     *
     * @param gui Connection to the gui
     */
    public SaleBook(@NotNull GUIConnector gui) {
        super();
        this.sparePartsObsSet = FXCollections.observableSet(new TreeSet<>());
        this.idToPositionObsMap = FXCollections.observableMap(new TreeMap<>());
        this.categoryObsSet = FXCollections.observableSet(new TreeSet<>());
        this.nameToSupplierObsMap = FXCollections.observableMap(new TreeMap<>());
        this.idToOrderObsMap = FXCollections.observableMap(new TreeMap<>());
        this.gui = gui;

        this.displaySaleBook();
    }

    /**
     * Constructor for an investment book.
     *
     * @param saleBookData the data of a saleBook
     * @param gui          Connection to the gui
     */
    public SaleBook(@NotNull SaleBookData saleBookData, @NotNull GUIConnector gui) {
        super(saleBookData.repairServiceSales, saleBookData.extraordinaryIncome,
                saleBookData.paid, saleBookData.fixedCosts, saleBookData.nextPosId,
                saleBookData.nextOrderId);

        Set<SparePart> spareParts = new TreeSet<>();
        for (SparePart sparePart : saleBookData.getSpareParts()) {
            this.sparePartNames.add(sparePart.getName());
            this.sparePartUnits.add(sparePart.getUnit());
            spareParts.add(sparePart);
        }
        this.sparePartsObsSet = FXCollections.observableSet(spareParts);
        this.initializeIdToPositionMap(saleBookData.getPositionData());

        Map<String, Supplier> nameToSupplier = new TreeMap<>();
        for (Supplier supplier : saleBookData.getSuppliers()) {
            nameToSupplier.put(supplier.getName(), supplier);
        }
        this.nameToSupplierObsMap = FXCollections.observableMap(nameToSupplier);

        Map<Integer, Order> idToOrder = new TreeMap<>();
        for (Order order : saleBookData.getOrders()) {
            idToOrder.put(order.getId(), order);
        }
        this.idToOrderObsMap = FXCollections.observableMap(idToOrder);

        this.gui = gui;
        this.displaySaleBook();
    }

    /**
     * Initializes the idToPositionObsMap.
     *
     * @param positionData the positions data
     */
    private void initializeIdToPositionMap(PositionData[] positionData) {
        Map<Integer, Position> idToPosition = new TreeMap<>();
        Set<String> categorySet = new TreeSet<>();

        for (PositionData positionDatum : positionData) {
            Position position = new Position(positionDatum);
            idToPosition.put(position.getId(), position);
            categorySet.add(position.getCategory());
            this.variableCosts = this.variableCosts.add(position.getPurchasingPrice()).add(position.getCost());
            if (position.isSold()) {
                this.salesVolume = this.salesVolume.add(position.getSellingPrice());
            }
            for (Item item : position.getItems()) {
                ItemColor itemColor = item.getItemColor();
                this.itemColorOccurrences.merge(itemColor, 1, Integer::sum);
                this.nameToItemColor.put(itemColor.getName(), itemColor);
            }
        }
        this.idToPositionObsMap = FXCollections.observableMap(idToPosition);
        this.categoryObsSet = FXCollections.observableSet(categorySet);
    }

    /**
     * Returns the names of the spareParts.
     *
     * @return sparePart names
     */
    public @NotNull Set<String> getSparePartNames() {
        return this.sparePartNames;
    }

    /**
     * Returns the units of the spareParts.
     *
     * @return spareParts units
     */
    public @NotNull Set<String> getSparePartUnits() {
        return this.sparePartUnits;
    }

    /**
     * Returns the categories of the positions
     *
     * @return positions categories
     */
    public @NotNull Set<String> getCategories() {
        return this.categoryObsSet;
    }

    /**
     * Returns the spareParts of this saleBook
     *
     * @return spareParts
     */
    public @NotNull Set<SparePart> getSpareParts() {
        return new TreeSet<>(this.sparePartsObsSet);
    }

    /**
     * Returns the positions of this saleBook
     *
     * @return the positions of this saleBook
     */
    public @NotNull Collection<Position> getPositions() {
        return this.idToPositionObsMap.values();
    }

    /**
     * Returns the orders of this saleBook
     *
     * @return the orders of this saleBook
     */
    public @NotNull Collection<Order> getOrders() {
        return this.idToOrderObsMap.values();
    }

    /**
     * Returns the suppliers of this saleBook
     *
     * @return the suppliers of this saleBook
     */
    public @NotNull Collection<Supplier> getSuppliers() {
        return this.nameToSupplierObsMap.values();
    }

    /**
     * Returns a map which mapped the ItemColor to their name
     *
     * @return ItemColor mapped to their name
     */
    public @NotNull Map<String, ItemColor> getNameToItemColor() {
        return new HashMap<>(this.nameToItemColor);
    }

    /**
     * Returns the salesVolume of this saleBook
     *
     * @return the salesVolume of this saleBook
     */
    public @NotNull BigDecimal getSalesVolume(){
        return this.salesVolume;
    }

    /**
     * Adds the specified sparePart to this saleBook
     *
     * @param sparePart the sparePart which should be added
     */
    public void addSparePart(@NotNull SparePart sparePart) {
        boolean added = this.sparePartsObsSet.add(sparePart);
        if (added) {
            this.sparePartNames.add(sparePart.getName());
            this.sparePartUnits.add(sparePart.getUnit());
            this.gui.updateStatus(String.format("spare part %s successfully added",
                    sparePart.getName()));
            this.gui.displaySparePartNames(this.sparePartNames);
        }
    }

    /**
     * Removes the specified sparePart from this saleBook
     *
     * @param sparePart the sparePart which should be removed
     */
    public void removeSparePart(@NotNull SparePart sparePart) {
        boolean removed = this.sparePartsObsSet.remove(sparePart);
        if (removed) {
            String deletedName = sparePart.getName();
            String deletedUnit = sparePart.getUnit();
            boolean containsName = false;
            boolean containsUnit = false;
            Iterator<SparePart> iterator = this.sparePartsObsSet.iterator();
            while (iterator.hasNext() && (!containsName || !containsUnit)) {
                SparePart part = iterator.next();
                if (part.getName().equals(deletedName)) {
                    containsName = true;
                }
                if (sparePart.getUnit().equals(deletedUnit)) {
                    containsUnit = true;
                }
            }
            if (!containsName) {
                this.sparePartNames.remove(deletedName);
                this.gui.displaySparePartNames(this.sparePartNames);
            }
            if (!containsUnit) {
                this.sparePartUnits.remove(deletedUnit);
            }
            this.gui.updateStatus(String.format("spare part %s successfully deleted",
                    sparePart.getName()));
        }
    }

    /**
     * Adds the specified position to this saleBook
     *
     * @param position the position which should be added
     * @throws IllegalArgumentException if the id of the specified position is already used
     */
    public void addPosition(@NotNull Position position) {
        int id = position.getId();
        if (this.nextPosId != id){
            throw new IllegalArgumentException("expected id is %d but is %d".formatted(
                    this.nextPosId, id));
        }

        this.idToPositionObsMap.put(id, position);
        this.categoryObsSet.add(position.getCategory());
        this.gui.displayCategories(this.categoryObsSet);
        if (position.getSellingPrice() != null) {
            this.addSale(position.getSellingPrice());
        }
        List<Item> items = position.getItems();
        for (Item item : items) {
            ItemColor itemColor = item.getItemColor();
            this.itemColorOccurrences.merge(itemColor, 1, Integer::sum);
            this.nameToItemColor.put(itemColor.getName(), itemColor);
        }
        this.gui.updateStatus(Message.added.formatMessage("position %d".formatted(id)));
        this.nextPosId++;
    }

    /**
     * Adds the specified item to the position with the specified posId
     *
     * @param posId id of the position to which the item should be added
     * @param item  the item which should be added
     * @throws IllegalArgumentException if no position with the specified posId exist
     */
    public void addItemToPosition(int posId, @NotNull Item item) {
        Position position = this.idToPositionObsMap.get(posId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + posId);
        }

        position.addItem(item);
        ItemColor itemColor = item.getItemColor();
        this.itemColorOccurrences.merge(itemColor, 1, Integer::sum);
        this.nameToItemColor.put(itemColor.getName(), itemColor);
        this.gui.displayPositions(this.idToPositionObsMap.values());
        this.gui.updateStatus(String.format("item %d of position %d successfully added",
                item.getId(), posId));
    }

    /**
     * Removes the position with the specified id and returns it.
     *
     * @param id the id of the searched position
     * @throws IllegalArgumentException if there is no position with the specified, id
     */
    public @NotNull Position removePosition(int id) {
        Position position = this.idToPositionObsMap.remove(id);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + id);
        }

        this.removePosition(position);
        this.gui.updateStatus(String.format("position %d successfully deleted", id));
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
    public void removeItem(int positionId, int itemId) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        if (position.getItems().size() <= 1) {
            throw new IllegalArgumentException("a position must have at least 1 item");
        }

        Item item = position.removeItemById(itemId);
        if (item == null) {
            throw new IllegalArgumentException("no item in the position for id " + itemId);
        }

        ItemColor itemColor = item.getItemColor();
        Integer occurrence = this.itemColorOccurrences.merge(itemColor, -1, Integer::sum);
        if (occurrence == 0) {
            this.itemColorOccurrences.remove(itemColor);
            this.nameToItemColor.remove(itemColor.getName());
        }
        this.gui.displayPositions(this.idToPositionObsMap.values());
        this.gui.updateStatus(String.format("item %d of position %d successfully deleted", itemId
                , positionId));
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
        this.variableCosts = this.variableCosts.add(newCost);
        this.gui.refreshPosition();
        this.gui.updateStatus(String.format("cost %.2f %s add to position %d", newCost,
                LabelUtils.SYMBOL_OF_CURRENCY, positionId));
    }

    /**
     * Sets the position with the specified positionId to the state repaired and adjusts the stock
     * of the spareParts depending on the specified sparePartsToCount map.
     * The sparePartToCount map represents the number of used spareParts.
     *
     * @param positionId        the id of the position which should be repaired
     * @param sparePartsToCount map of number of used spareParts
     * @throws IllegalArgumentException if there is no position with the specified positionId or
     *                                  sparePartsToCount is null
     */
    public void repairPosition(int positionId, @NotNull Map<SparePart, Integer> sparePartsToCount) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }

        position.setState(State.REPAIRED);
        for (SparePart sparePart : this.sparePartsObsSet) {
            Integer count = sparePartsToCount.get(sparePart);
            if (count != null && count > 0) {
                sparePart.use(count);
            }
        }
        this.gui.refreshPosition();
        this.gui.refreshSpareParts();
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
    public void sale(int positionId, @NotNull LocalDate sellingDate,
                     @NotNull BigDecimal sellingPrice) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        position.sale(sellingDate, sellingPrice);
        this.addSale(sellingPrice);
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
    public void shipped(int positionId, @NotNull ShippingCompany shippingCompany,
                        @NotNull String trackingNumber, @NotNull BigDecimal shippingCost) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }
        position.send(shippingCompany, trackingNumber, shippingCost);
        this.variableCosts = this.variableCosts.add(shippingCost);
        this.gui.refreshPosition();
        this.gui.updateStatus(String.format("position %d shipped", positionId));
    }

    /**
     * Adds the specified sale to the repair service sales
     *
     * @param sale the sale which should be added
     */
    public void addRepairServiceSale(@NotNull BigDecimal sale) {
        this.repairServiceSales = this.repairServiceSales.add(sale);
        this.gui.displayRepairServiceSale(this.repairServiceSales);
        this.displayTenthPart();
        this.gui.updateStatus(String.format("repair service sale added %.2f %s add to position ",
                sale, LabelUtils.SYMBOL_OF_CURRENCY));
    }

    /**
     * Adds the specified income to the extraordinary income
     *
     * @param income the income which should be added
     */
    public void addExtraordinaryIncome(@NotNull BigDecimal income) {
        this.extraordinaryIncome = this.extraordinaryIncome.add(income);
        this.gui.displayExtraordinaryIncome(this.extraordinaryIncome);
        this.displayTenthPart();
        this.gui.updateStatus(String.format("extraordinary income added %.2f %s",
                income, LabelUtils.SYMBOL_OF_CURRENCY));
    }

    /**
     * Adds the specified payment to the paid
     *
     * @param payment the payment which should be added
     */
    public void addPayment(@NotNull BigDecimal payment) {
        this.paid = this.paid.add(payment);
        this.gui.displayPaid(this.paid);
        BigDecimal tenthPartTotalSales = this.repairServiceSales.add(this.salesVolume.add(this.extraordinaryIncome)).divide(BigDecimal.TEN, RoundingMode.HALF_UP);
        this.gui.displayTenthPartBalance(this.paid.subtract(tenthPartTotalSales));
        this.gui.updateStatus(String.format("payment %.2f %s added",
                payment, LabelUtils.SYMBOL_OF_CURRENCY));
    }

    /**
     * Divides the position with the specified positionId. For Each item of the position, a new
     * position will be created.
     *
     * @param positionId the id of the position which should be divided
     * @throws IllegalArgumentException if there is no position with the specified positionId
     */
    public void dividePosition(int positionId) {
        Position position = this.idToPositionObsMap.get(positionId);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + positionId);
        }

        int[] positionIds = new int[position.getItems().size() - 1];
        for (int i = 0, positionIdsLength = positionIds.length; i < positionIdsLength; i++) {
            positionIds[i] = this.nextPosId++;
        }

        Position[] newPositions = position.divide(positionIds);

        if (newPositions != null && newPositions.length > 0) {
            StringBuilder builder =
                    new StringBuilder(String.format("position %d divided in ", positionId));
            for (int i = 0, newPositionsLength = newPositions.length; i < newPositionsLength; i++) {
                Position currentPosition = newPositions[i];
                int currentId = currentPosition.getId();
                this.idToPositionObsMap.put(currentId, currentPosition);
                builder.append(" ");
                builder.append(currentId);
                if (i < positionIds.length - 1) {
                    builder.append(",");
                }
            }
            this.gui.updateStatus(builder.toString());
        }
    }

    /**
     * Adds the specified order and adds the cost of the order to the fixed cost.
     *
     * @param order the order which should be added
     */
    public void addOrder(@NotNull Order order) {
        int orderId = order.getId();
        this.idToOrderObsMap.put(orderId, order);
        this.addFixedCost(order.getCost());
        this.gui.updateStatus(String.format("order %d added", orderId));
        this.nextOrderId++;
    }

    /**
     * Consumes the order with the specified orderId and stores the spare parts of the order
     *
     * @param orderId the id of the searched order
     * @throws IllegalArgumentException if there is no order with the specified orderId
     */
    public void orderReceived(int orderId) {
        Order order = this.idToOrderObsMap.remove(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        for (SparePart orderedSparePart : order.getSpareParts()) {
            this.storeReceivedSparePart(order, orderedSparePart);
        }
        this.gui.displayOrderedSpareParts(order.getSpareParts());
        this.gui.refreshSpareParts();
        this.gui.updateStatus(String.format("order %d received", orderId));
    }

    /**
     * Cancels the order with the specified orderId
     *
     * @param orderId the id of the order which should be canceled
     * @throws IllegalArgumentException if there is no order with the specified orderId
     */
    public void cancelOrder(int orderId) {
        Order order = this.idToOrderObsMap.remove(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        this.addFixedCost(order.getCost().negate());
        this.gui.displayOrderedSpareParts(Set.of());
        this.gui.updateStatus(String.format("order %d canceled", orderId));
    }

    /**
     * Consumes a spare part of the order with the specified orderId
     *
     * @param orderId the id of the order from which the spare part should be consumed
     * @param orderedSparePart the spare part which was received
     * @throws IllegalArgumentException if there is no order with the specified orderId
     */
    public void sparePartReceived(int orderId, @NotNull SparePart orderedSparePart) {
        Order order = this.idToOrderObsMap.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        this.storeReceivedSparePart(order, orderedSparePart);
        this.gui.displayOrderedSpareParts(order.getSpareParts());
        this.gui.refreshSpareParts();
        this.gui.updateStatus(String.format("spare part %s of order %d received",
                orderedSparePart.getName(), orderId));
    }

    /**
     * Adds the specified supplier to this saleBook
     *
     * @param supplier the supplier which should be added
     */
    public void addSupplier(@NotNull Supplier supplier) {
        String supplierName = supplier.getName();
        this.nameToSupplierObsMap.put(supplierName, supplier);
        this.gui.displaySupplierNames(this.nameToSupplierObsMap.keySet());
        this.gui.updateStatus(String.format("supplier %s added", supplierName));
    }

    /**
     * Deletes the supplier with the specified supplierName, if the supplier name is not empty.
     *
     * @param supplierName the name of the supplier which should be deleted
     */
    public void removeSupplier(@NotNull String supplierName) {
        if (!supplierName.isEmpty()) {
            this.nameToSupplierObsMap.remove(supplierName);
            this.gui.displaySupplierNames(this.nameToSupplierObsMap.keySet());
            this.gui.updateStatus(String.format("supplier %s deleted", supplierName));
        }
    }

    /**
     * Returns the supplier with the specified supplierName
     *
     * @param supplierName the name of the searched supplier
     * @return the supplier
     */
    public @Nullable Supplier getSupplierByName(@NotNull String supplierName) {
        return this.nameToSupplierObsMap.get(supplierName);
    }

    /**
     *
     *
     * @param repairServiceSales
     * @param extraordinaryIncome
     * @param paid
     */
    public void recalculateTenthPartPage(BigDecimal repairServiceSales, BigDecimal extraordinaryIncome,
                                         BigDecimal paid) {
        this.repairServiceSales = repairServiceSales;
        this.extraordinaryIncome = extraordinaryIncome;
        this.paid = paid;
        this.displayTenthPart();
    }

    /**
     * Returns the names of the supplier
     *
     * @return name of the suppliers
     */
    public @NotNull Set<String> getSupplierNames() {
        return this.nameToSupplierObsMap.keySet();
    }

    /**
     * Combines the position with the specified positionId with the positions with the specified positionIds.
     * All positions have to be at least at the state received
     *
     * @param positionId the id of one position which should be combined
     * @param positionIds the id of the other position which should be combined with
     * @throws IllegalArgumentException if no positionId to combine with were given or
     * some positionId does not match to any position
     */
    public void combinePositions(int positionId, int @NotNull ... positionIds) {
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
        this.idToPositionObsMap.put(combindPosition.getId(), combindPosition);
        this.gui.refreshPosition();
        this.gui.updateStatus("combined to the new position " + this.nextPosId);
        this.nextPosId++;
    }

    public void updateStatus(String message) {
        this.gui.updateStatus(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaleBook saleBook)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(this.sparePartNames, saleBook.sparePartNames) &&
                Objects.equals(this.sparePartUnits, saleBook.sparePartUnits) && Objects.equals(this.categoryObsSet, saleBook.categoryObsSet) &&
                Objects.equals(this.sparePartsObsSet, saleBook.sparePartsObsSet) &&
                Objects.equals(this.idToPositionObsMap, saleBook.idToPositionObsMap) &&
                Objects.equals(this.nameToSupplierObsMap, saleBook.nameToSupplierObsMap) &&
                Objects.equals(this.idToOrderObsMap, saleBook.idToOrderObsMap) &&
                Objects.equals(this.itemColorOccurrences, saleBook.itemColorOccurrences) &&
                Objects.equals(this.nameToItemColor, saleBook.nameToItemColor) &&
                Objects.equals(this.salesVolume, saleBook.salesVolume) &&
                Objects.equals(this.variableCosts, saleBook.variableCosts) &&
                Objects.equals(this.gui, saleBook.gui);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.sparePartNames, this.sparePartUnits, this.categoryObsSet, this.sparePartsObsSet,
                this.idToPositionObsMap, this.nameToSupplierObsMap, this.idToOrderObsMap, this.itemColorOccurrences,
                this.nameToItemColor, this.salesVolume, this.variableCosts, this.gui);
    }

    @Override
    public String toString() {
        return "SaleBook{" +
                "nameOfSpareParts=" + this.sparePartNames +
                ", units=" + this.sparePartUnits +
                ", models=" + this.categoryObsSet +
                ", sparePartsObsSet=" + this.sparePartsObsSet +
                ", idToPositionObsMap=" + this.idToPositionObsMap +
                ", nameToSupplierObsMap=" + this.nameToSupplierObsMap +
                ", idToOrderObsMap=" + this.idToOrderObsMap +
                ", itemColorOccurrences=" + this.itemColorOccurrences +
                ", nameToItemColor=" + this.nameToItemColor +
                ", salesVolume=" + this.salesVolume +
                ", variableCost=" + this.variableCosts +
                ", gui=" + this.gui +
                ", repairServiceSales=" + this.repairServiceSales +
                ", extraordinaryIncome=" + this.extraordinaryIncome +
                ", paid=" + this.paid +
                ", fixedCosts=" + this.fixedCosts +
                ", nextPosId=" + this.nextPosId +
                ", nextOrderId=" + this.nextOrderId +
                '}';
    }

    /**
     * Recalculates the totalPerformance and displays the new calculated totalPerformance
     */
    private void updateTotalPerformance() {
        BigDecimal totalPerformance = this.salesVolume.subtract(this.variableCosts);
        this.gui.displayTotalPerformance(totalPerformance);
    }

    /**
     * Displays the components of this saleBook
     */
    private void displaySaleBook() {
        this.gui.displaySpareParts(new ObservableListSetBinder<>(this.sparePartsObsSet).getList());
        this.gui.displaySparePartNames(this.sparePartNames);
        ObservableTreeItemMapBinder<Integer> root = new ObservableTreeItemMapBinder<>(this.idToPositionObsMap);
        this.gui.displayPositions(root);
        this.gui.displaySuppliers(new ObservableListMapBinder<>(this.nameToSupplierObsMap).getList());
        this.gui.displaySupplierNames(this.nameToSupplierObsMap.keySet());
        this.gui.displayOrders(new ObservableListMapBinder<>(this.idToOrderObsMap).getList());
        this.gui.displayCategories(new ObservableListSetBinder<>(this.categoryObsSet).getList());
        this.gui.displaySales(this.salesVolume);
        this.gui.displayRepairServiceSale(this.repairServiceSales);
        this.gui.displayExtraordinaryIncome(this.extraordinaryIncome);
        BigDecimal tenthPartTotalIncome = this.repairServiceSales.add(this.extraordinaryIncome)
                .add(this.salesVolume).divide(BigDecimal.TEN, RoundingMode.HALF_UP);
        this.gui.displayTenthPartTotalIncome(tenthPartTotalIncome);
        this.gui.displayPaid(this.paid);
        this.gui.displayTenthPartBalance(this.paid.subtract(tenthPartTotalIncome));
        this.gui.displayVariableCosts(this.variableCosts);
        this.gui.displayFixedCosts(this.fixedCosts);
        this.gui.displayProfitAndLossAccountBalance(this.salesVolume.subtract(this.variableCosts)
                .subtract(this.fixedCosts));
        this.updateTotalPerformance();
    }

    /**
     * Adds the specified cost to the fixedCost, recalculates the balance and displays the updated numbers.
     *
     * @param cost the cost which should be added
     */
    private void addFixedCost(@NotNull BigDecimal cost) {
        this.fixedCosts = this.fixedCosts.add(cost);
        this.gui.displayFixedCosts(this.fixedCosts);
        this.gui.displayProfitAndLossAccountBalance(this.salesVolume.subtract(this.variableCosts)
                .subtract(this.fixedCosts));
    }

    /**
     * Stores the order quantity of the specified receivedSparePart of the specified order
     *
     * @param order the order in which the spare part is
     * @param receivedSparePart the sparePart which was received
     */
    private void storeReceivedSparePart(@NotNull Order order, @NotNull SparePart receivedSparePart) {
        Integer orderQuantity = order.removeSparePart(receivedSparePart);
        if (orderQuantity != null) {
            for (SparePart sparePart : this.sparePartsObsSet) {
                if (sparePart.equals(receivedSparePart)) {
                    sparePart.addQuantity(orderQuantity);
                    break;
                }
            }
        }
    }


    /**
     * Displays the tenth part, which includes the sale volume, tenth part of the income and the balance
     */
    private void displayTenthPart() {
        BigDecimal tenthPartTotalIncome = this.repairServiceSales.add(this.extraordinaryIncome)
                .add(this.salesVolume).divide(BigDecimal.TEN, RoundingMode.HALF_UP);

        this.gui.displaySales(this.salesVolume);
        this.gui.displayTenthPartTotalIncome(tenthPartTotalIncome);
        this.gui.displayTenthPartBalance(this.paid.subtract(tenthPartTotalIncome));
    }

    /**
     * Adds the specified sale to the sale volume and displays the updated numbers
     *
     * @param sale the sale which should be added
     */
    private void addSale(@NotNull BigDecimal sale) {
        if (!sale.equals(BigDecimal.ZERO)) {
            this.salesVolume = this.salesVolume.add(sale);
            this.displayTenthPart();
        }
    }

    /**
     * Subtract the specified sale from the sale volume and displays the updated numbers
     *
     * @param sale the sale which should be subtracted
     */
    private void subtractSale(@NotNull BigDecimal sale) {
        if (!sale.equals(BigDecimal.ZERO)) {
            this.salesVolume = this.salesVolume.subtract(sale);
            this.displayTenthPart();
        }
    }

    /**
     * Removes the specified position and updates the performance of this saleBook
     *
     * @param position the position which should be removed
     */
    private void removePosition(@NotNull Position position) {
        if (position.getSellingPrice() != null) {
            this.subtractSale(position.getSellingPrice());
        }
        this.variableCosts = this.variableCosts.subtract(position.getCost());
        this.updateTotalPerformance();
        String deletedModel = position.getCategory();
        boolean containsModel = false;
        for (Position pos : this.idToPositionObsMap.values()) {
            if (pos.getCategory().equals(deletedModel)) {
                containsModel = true;
                break;
            }
        }
        if (!containsModel) {
            this.categoryObsSet.remove(deletedModel);
        }
        List<Item> items = position.getItems();
        for (Item item : items) {
            ItemColor color = item.getItemColor();
            Integer occurrence = this.itemColorOccurrences.merge(color, -1, Integer::sum);
            if (occurrence == 0) {
                this.itemColorOccurrences.remove(color);
            }
        }
    }
}