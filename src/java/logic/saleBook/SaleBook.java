package logic.saleBook;

import gui.ObservableTreeItemMapBinder;
import gui.FXutils.LabelUtils;
import logic.Asset;
import logic.GUIConnector;
import logic.products.item.ItemColor;
import logic.SparePart;
import logic.manager.AssetManager;
import logic.manager.OrdersManager;
import logic.manager.PositionsManager;
import logic.manager.SparePartsManager;
import logic.manager.SuppliersManager;
import logic.order.Order;
import logic.Supplier;
import logic.products.item.Item;
import logic.products.position.Position;
import logic.products.position.PositionData;
import logic.products.position.ShippingCompany;
import utils.BigDecimalUtils;
import gui.FXutils.FXCollectionsUtils;
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
public class SaleBook extends AbstractSaleBook {
    /**
     *
     */
    //TODO 20.04.2024
    private final SparePartsManager sparePartsManager;

    private final PositionsManager positionsManager;

    private final SuppliersManager suppliersManager;

    private final OrdersManager ordersManager;

    private final AssetManager assetManager;

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
        this.sparePartsManager = new SparePartsManager();
        this.positionsManager = new PositionsManager();
        this.suppliersManager = new SuppliersManager();
        this.ordersManager = new OrdersManager();
        this.assetManager = new AssetManager();
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
                saleBookData.paid, saleBookData.fixedCosts);

        this.sparePartsManager = new SparePartsManager(saleBookData.getSpareParts());
        ItemColor.setItemColors(saleBookData.getItemColors());
        Position[] positions = this.createPositions(saleBookData.getPositionData());
        this.positionsManager = new PositionsManager(positions,
                saleBookData.getNextPosId());
        this.suppliersManager = new SuppliersManager(saleBookData.getSuppliers());
        this.ordersManager = new OrdersManager(saleBookData.getOrders(),
                saleBookData.getNextOrderId());
        this.assetManager = new AssetManager(saleBookData.getAssets(),
                saleBookData.getNextAssetId());
        this.gui = gui;
        this.displaySaleBook();
    }

    /**
     * Returns the names of the spareParts.
     *
     * @return sparePart names
     */
    public @NotNull Set<String> getSparePartNames() {
        return this.sparePartsManager.getSparePartNames();
    }

    /**
     * Returns the units of the spareParts.
     *
     * @return spareParts units
     */
    public @NotNull Set<String> getSparePartUnits() {
        return this.sparePartsManager.getSparePartUnits();
    }

    /**
     * Returns the categories of the positions
     *
     * @return positions categories
     */
    public @NotNull Set<String> getCategories() {
        return this.positionsManager.getCategories();
    }

    /**
     * Returns the spareParts of this saleBook
     *
     * @return spareParts
     */
    public @NotNull Set<SparePart> getSpareParts() {
        return new TreeSet<>(this.sparePartsManager.getSparePartsObsSet());
    }

    /**
     * Returns the positions of this saleBook
     *
     * @return the positions of this saleBook
     */
    public @NotNull Collection<Position> getPositions() {
        return this.positionsManager.getPositions();
    }

    /**
     * Returns the orders of this saleBook
     *
     * @return the orders of this saleBook
     */
    public @NotNull Collection<Order> getOrders() {
        return this.ordersManager.getOrders();
    }

    /**
     * Returns the assets of this saleBook
     *
     * @return the assets of this saleBook
     */
    public @NotNull Collection<Asset> getAssets() {
        return this.assetManager.getAssets();
    }

    /**
     * Returns the suppliers of this saleBook
     *
     * @return the suppliers of this saleBook
     */
    public @NotNull Collection<Supplier> getSuppliers() {
        return this.suppliersManager.getSuppliers();
    }

    /**
     * Returns a map which mapped the ItemColor to their name
     *
     * @return ItemColor mapped to their name
     */
    public @NotNull Map<String, ItemColor> getNameToItemColor() {
        return ItemColor.getItemColorMap();
    }

    /**
     * Returns the salesVolume of this saleBook
     *
     * @return the salesVolume of this saleBook
     */
    public @NotNull BigDecimal getSalesVolume() {
        return this.salesVolume;
    }

    /**
     * Returns the id for the next creation of a position
     *
     * @return the id for the next creation of a position
     */
    public int getNextPosId() {
        return this.positionsManager.getNextPosId();
    }

    /***
     * Returns the id for the next creation of an order
     *
     * @return the id for the next creation of an order
     */
    public int getNextOrderId() {
        return this.ordersManager.getNextOrderId();
    }

    /**
     *
     * @return
     */
    public int getNextAssetId() {
        return this.assetManager.getNextAssetId();
    }

    /**
     * Adds the specified sparePart to this saleBook
     *
     * @param sparePart the sparePart which should be added
     */
    public void addSparePart(@NotNull SparePart sparePart) {
        boolean added = this.sparePartsManager.addSparePart(sparePart);
        if (added) {
            this.gui.displaySparePartNames(this.sparePartsManager.getSparePartNames());
            this.gui.updateStatus(String.format("spare part %s successfully added",
                    sparePart.getName()));
        }
    }

    /**
     * Removes the specified sparePart from this saleBook
     *
     * @param sparePart the sparePart which should be removed
     */
    public boolean removeSparePart(@NotNull SparePart sparePart) {
        boolean removed = this.sparePartsManager.removeSparePart(sparePart);
        if (removed) {
            this.gui.displaySparePartNames(this.sparePartsManager.getSparePartNames());
            this.gui.updateStatus(String.format("spare part %s successfully deleted",
                    sparePart.getName()));
            return true;
        }
        return false;
    }

    /**
     * Adds the specified position to this saleBook
     *
     * @param position the position which should be added
     * @return
     * @throws IllegalArgumentException if the id of the specified position is already used
     */
    public boolean addPosition(@NotNull Position position) {
        boolean added = this.positionsManager.addPosition(position);
        if (added) {
            this.gui.displayCategories(this.positionsManager.getCategories());
            if (position.getSellingPrice() != null) {
                this.addSale(position.getSellingPrice());
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
     * @return
     * @throws IllegalArgumentException if no position with the specified posId exist
     */
    public boolean addItemToPosition(int posId, @NotNull Item item) {
        boolean added = this.positionsManager.addItemToPosition(posId, item);
        if (added) {
            this.gui.displayPositions(this.positionsManager.getPositions());
            this.gui.updateStatus(String.format("item %d of position %d successfully added",
                    item.getId(), posId));
            return true;
        }
        return false;
    }

    /**
     * Removes the position with the specified id and returns it.
     *
     * @param id the id of the searched position
     * @throws IllegalArgumentException if there is no position with the specified, id
     */
    public @NotNull Position removePosition(int id) {
        Position position = this.positionsManager.removePosition(id);
        if (position == null) {
            throw new IllegalArgumentException("no position for id " + id);
        }

        if (position.getSellingPrice() != null) {
            this.subtractSale(position.getSellingPrice());
        }
        this.variableCosts = this.variableCosts.subtract(position.getCost())
                .subtract(position.getPurchasingPrice());
        this.updateTotalPerformance();
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
    public @NotNull Item removeItem(int positionId, int itemId) {
        Item item = this.positionsManager.removeItem(positionId, itemId);
        if (item == null) {
            throw new IllegalArgumentException("no item in the position for id " + itemId);
        }
        this.gui.displayPositions(this.positionsManager.getPositions());
        this.gui.updateStatus(String.format("item %d of position %d successfully deleted", itemId
                , positionId));
        return item;
    }

    /**
     * Sets the position with the specified positionId to the state received and the received date of this position
     * to the receivedDate
     *
     * @param positionId   the id of the searched position
     * @param receivedDate the date on which the position was received
     */
    public void setReceived(int positionId, @NotNull LocalDate receivedDate) {
        this.positionsManager.setReceived(positionId, receivedDate);
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
        this.positionsManager.addCostToPosition(positionId, newCost);
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
        this.positionsManager.repairPosition(positionId);
        this.sparePartsManager.useSparParts(sparePartsToCount);
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
        this.positionsManager.sale(positionId, sellingDate, sellingPrice);
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
        this.positionsManager.shipped(positionId, shippingCompany, trackingNumber, shippingCost);
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
     * @throws IllegalArgumentException if the payment is negative
     */
    public void addPayment(@NotNull BigDecimal payment) {
        if (!BigDecimalUtils.isPositive(payment)) {
            throw new IllegalArgumentException("payment is negative");
        }

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
        Position[] positions = this.positionsManager.dividePosition(positionId);
        if (positions != null && positions.length > 0) {
            StringBuilder builder =
                    new StringBuilder(String.format("position %d divided in ", positionId));
            for (int i = 0, positionsLength = positions.length; i < positionsLength; i++) {
                builder.append(" ");
                builder.append(positions[i].getId());
                if (i < positionsLength - 1) {
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
        boolean added = this.ordersManager.addOrder(order);
        if (added) {
            this.addFixedCost(order.getValue());
            this.gui.updateStatus(String.format("order %d added", order.getId()));
        }
    }

    /**
     * Consumes the order with the specified orderId and stores the spare parts of the order
     *
     * @param orderId the id of the searched order
     * @throws IllegalArgumentException if there is no order with the specified orderId
     */
    public void orderReceived(int orderId) {
        Order order = this.ordersManager.removeOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        this.sparePartsManager.storeSpareParts(order.getSparePartToOrderQuantity());
        this.updateDisplayOrder(order);
        this.gui.updateStatus(String.format("order %d received", orderId));
    }

    /**
     * Cancels the order with the specified orderId
     *
     * @param orderId the id of the order which should be canceled
     * @throws IllegalArgumentException if there is no order with the specified orderId
     */
    public void cancelOrder(int orderId) {
        Order order = this.ordersManager.removeOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        this.addFixedCost(order.getValue().negate());
        this.gui.displayOrderedSpareParts(Set.of());
        this.gui.updateStatus(String.format("order %d canceled", orderId));
    }

    /**
     * Consumes a spare part of the order with the specified orderId
     *
     * @param orderId          the id of the order from which the spare part should be consumed
     * @param orderedSparePart the spare part which was received
     * @throws IllegalArgumentException if there is no order with the specified orderId
     */
    public void sparePartReceived(int orderId, @NotNull SparePart orderedSparePart) {
        Order order = this.ordersManager.getOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("no order for id " + orderId);
        }
        Integer orderQuantity = order.getOrderQuantity(orderedSparePart);
        if (orderQuantity != null && orderQuantity > 0) {
            this.sparePartsManager.storeSparePart(orderedSparePart, orderQuantity);

            this.updateDisplayOrder(order);
            this.gui.updateStatus(String.format("spare part %s of order %d received",
                    orderedSparePart.getName(), orderId));
        }
    }

    /**
     * Adds the specified supplier to this saleBook
     *
     * @param supplier the supplier which should be added
     */
    public void addSupplier(@NotNull Supplier supplier) {
        boolean added = this.suppliersManager.addSupplier(supplier);
        if (added) {
            this.gui.displaySupplierNames(this.suppliersManager.getSupplierNames());
            this.gui.updateStatus(String.format("supplier %s added", supplier.getName()));
        }
    }

    /**
     * Deletes the supplier with the specified supplierName, if the supplier name is not empty.
     *
     * @param supplierName the name of the supplier which should be deleted
     */
    public void removeSupplier(@NotNull String supplierName) {
        Supplier removedSupplier = this.suppliersManager.removeSupplier(supplierName);
        if (removedSupplier != null) {
            this.gui.displaySupplierNames(this.suppliersManager.getSupplierNames());
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
        return this.suppliersManager.getSupplier(supplierName);
    }

    /**
     * @param repairServiceSales
     * @param extraordinaryIncome
     * @param paid
     */
    //TODO 08.01.2024 JavaDoc
    public void recalculateTenthPartPage(BigDecimal repairServiceSales, BigDecimal extraordinaryIncome,
                                         BigDecimal paid) {
        this.repairServiceSales = repairServiceSales;
        this.gui.displayRepairServiceSale(repairServiceSales);
        this.extraordinaryIncome = extraordinaryIncome;
        this.gui.displayExtraordinaryIncome(extraordinaryIncome);
        this.paid = paid;
        this.gui.displayPaid(paid);
        this.displayTenthPart();
    }

    /**
     * Returns the names of the supplier
     *
     * @return name of the suppliers
     */
    public @NotNull Set<String> getSupplierNames() {
        return this.suppliersManager.getSupplierNames();
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
    public void combinePositions(int positionId, int @NotNull ... positionIds) {
        Position position = this.positionsManager.combinePositions(positionId, positionIds);
        this.gui.refreshPosition();
        this.gui.updateStatus("combined to the new position " + position.getId());
    }

    /**
     * Adds the specified asset to this saleBook if the asset with the id is not added yet.
     *
     * @param asset that should be added
     * @return true if the asset were added otherwise false
     */
    public boolean addAsset(@NotNull Asset asset) {
        boolean added = this.assetManager.addAsset(asset);
        if (added) {
            this.gui.updateStatus(String.format("asset %d added", asset.getId()));
            return true;
        }
        return false;
    }

    /**
     * Removes the asset with the specified assetId
     *
     * @param assetId the id of the asset that should be removed
     * @throws IllegalArgumentException if no asset with the specified id exist
     */
    public void removeAsset(int assetId) {
        Asset asset = this.assetManager.removeAsset(assetId);

        if (asset == null) {
            throw new IllegalArgumentException("no asset for ID " + assetId);
        }
        this.gui.updateStatus(String.format("asset %d deleted", assetId));
    }

    /**
     * @param message
     */
    //TODO 18.04.2024
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
        return Objects.equals(this.sparePartsManager, saleBook.sparePartsManager) &&
                Objects.equals(this.positionsManager, saleBook.positionsManager) &&
                Objects.equals(this.suppliersManager, saleBook.suppliersManager) &&
                Objects.equals(this.ordersManager, saleBook.ordersManager) &&
                Objects.equals(this.assetManager, saleBook.assetManager) &&
                Objects.equals(this.salesVolume, saleBook.salesVolume) &&
                Objects.equals(this.variableCosts, saleBook.variableCosts) &&
                Objects.equals(this.gui, saleBook.gui);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.sparePartsManager, this.positionsManager,
                this.suppliersManager, this.ordersManager, this.assetManager, this.salesVolume,
                this.variableCosts, this.gui);
    }

    @Override
    public String toString() {
        return "SaleBook{" +
                "sparePartsManager=" + this.sparePartsManager +
                ", positionsManager=" + this.positionsManager +
                ", supplierManager=" + this.suppliersManager +
                ", ordersManager=" + this.ordersManager +
                ", assetManager=" + this.assetManager +
                ", salesVolume=" + this.salesVolume +
                ", variableCosts=" + this.variableCosts +
                ", gui=" + this.gui +
                '}';
    }

    /**
     * Initializes the positionsManager.
     *
     * @param positionData the positions data
     */
    private Position[] createPositions(@NotNull PositionData[] positionData) {
        Position[] result = new Position[positionData.length];

        for (int i = 0; i < positionData.length; i++) {
            Position position = new Position(positionData[i], ItemColor.getItemColorMap());
            result[i] = position;
            this.variableCosts = this.variableCosts.add(position.getPurchasingPrice()).add(position.getCost());
            if (position.isSold()) {
                this.salesVolume = this.salesVolume.add(position.getSellingPrice());
            }
        }
        return result;
    }

    /**
     * @param order
     */
    //TODO 20.04.2024
    private void updateDisplayOrder(Order order) {
        this.gui.displayOrderedSpareParts(order.getSpareParts());
        this.gui.refreshSpareParts();
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
        this.gui.displaySpareParts(FXCollectionsUtils.toObservableList(this.sparePartsManager.getSparePartsObsSet()));
        this.gui.displaySparePartNames(this.sparePartsManager.getSparePartNames());
        ObservableTreeItemMapBinder<Integer> root =
                new ObservableTreeItemMapBinder<>(this.positionsManager.getIdToPositionObsMap());
        this.gui.displayPositions(root);
        this.gui.displaySuppliers(FXCollectionsUtils.toObservableList(this.suppliersManager.getNameToSupplierObsMap()));
        this.gui.displaySupplierNames(this.getSupplierNames());
        this.gui.displayOrders(FXCollectionsUtils.toObservableList(this.ordersManager.getIdToOrderObsMap()));
        this.gui.displayAssets(FXCollectionsUtils.toObservableList(this.assetManager.getIdToAssetObsMap()));
        this.gui.displayCategories(this.positionsManager.getCategories());
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
}