package logic.saleBook;

import data.SaleBookData;
import gui.FXutils.LabelUtils;
import logic.Asset;
import data.Dataable;
import logic.GUIConnector;
import logic.Supplier;
import logic.order.Order;
import logic.products.item.ItemColor;
import logic.manager.AssetsManager;
import logic.manager.OrdersManager;
import logic.manager.PositionsManager;
import logic.manager.SparePartsManager;
import logic.manager.SuppliersManager;
import logic.products.position.Position;
import logic.products.position.PositionData;
import org.jetbrains.annotations.UnmodifiableView;
import utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * This class contains the program's logic.
 *
 * @author xthe_white_lionx
 */
public class SaleBook extends AbstractSaleBook implements Dataable<data.SaleBookData> {
    /**
     * Manager for the spare parts of this saleBook
     */
    private final SparePartsManager sparePartsManager;

    /**
     * Manager for the positions of this saleBook
     */
    private final PositionsManager positionsManager;

    /**
     * Manager for the suppliers of this saleBook
     */
    private final SuppliersManager suppliersManager;

    /**
     * Manager for the orders of this saleBook
     */
    private final OrdersManager ordersManager;

    /**
     * Manager for the asserts of this saleBook
     */
    private final AssetsManager assetsManager;

    /**
     * The categories of this saleBook
     */
    private final Set<String> categories;

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
        this.sparePartsManager = new SparePartsManager(this, gui);
        this.positionsManager = new PositionsManager(this, gui);
        this.suppliersManager = new SuppliersManager(this, gui);
        this.ordersManager = new OrdersManager(this, gui);
        this.assetsManager = new AssetsManager(this, gui);
        this.categories = new TreeSet<>();
        this.gui = gui;

        this.displaySaleBook();
    }

    /**
     * Constructor for an investment book.
     *
     * @param saleBookData the data of a saleBook
     * @param gui          Connection to the gui
     */
    public SaleBook(@NotNull data.SaleBookData saleBookData, @NotNull GUIConnector gui) {
        super(saleBookData.repairServiceSales, saleBookData.extraordinaryIncome,
                saleBookData.paid, saleBookData.fixedCosts);

        this.categories = new TreeSet<>();
        this.sparePartsManager = new SparePartsManager(this, gui,
                saleBookData.getSparePartsManagerData());
        ItemColor.setItemColors(saleBookData.getItemColors());
        Position[] positions = this.createPositions(saleBookData.getPositionsManagerData().getPositionsData());
        this.positionsManager = new PositionsManager(this, positions,
                saleBookData.getPositionsManagerData().getNextPosId(), gui);
        this.suppliersManager = new SuppliersManager(this, saleBookData.getSuppliers(), gui);
        this.ordersManager = new OrdersManager(this, gui,
                saleBookData.getOrdersManagerData());
        this.assetsManager = new AssetsManager(this, saleBookData.getAssetsManagerData(), gui);
        this.gui = gui;
        this.displaySaleBook();
    }

    /**
     * Returns the sparePartsManager of this saleBook
     *
     * @return the sparePartsManager of this saleBook
     */
    public @NotNull SparePartsManager getSparePartsManager() {
        return this.sparePartsManager;
    }

    /**
     * Returns the positionsManager of this saleBook
     *
     * @return the positionsManager of this saleBook
     */
    public @NotNull PositionsManager getPositionsManager() {
        return this.positionsManager;
    }

    /**
     * Returns the suppliersManager of this saleBook
     *
     * @return the suppliersManager of this saleBook
     */
    public @NotNull SuppliersManager getSuppliersManager() {
        return this.suppliersManager;
    }

    /**
     * Returns the ordersManager of this saleBook
     *
     * @return the ordersManager of this saleBook
     */
    public @NotNull OrdersManager getOrdersManager() {
        return this.ordersManager;
    }

    /**
     * Returns the assetsManager of this saleBook
     *
     * @return the assetsManager of this saleBook
     */
    public @NotNull AssetsManager getAssetsManager() {
        return this.assetsManager;
    }

    /**
     * Returns the categories of the positions
     *
     * @return positions categories
     */
    @UnmodifiableView
    public @NotNull Set<String> getCategories() {
        return Collections.unmodifiableSet(this.categories);
    }

    /**
     *
     * @param category
     */
    public boolean addCategory(String category) {
        if (this.categories.add(category)) {
            this.gui.displayCategories(this.categories);
            return true;
        }
        return false;
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
     *
     * @return
     */
    public BigDecimal getVariableCosts() {
        return this.variableCosts;
    }

    /**
     * @param cost
     */
    public void subtractVariableCosts(@NotNull BigDecimal cost) {
        if (cost.compareTo(BigDecimal.ZERO) > 0) {
            this.variableCosts = this.variableCosts.subtract(cost);
            this.updateTotalPerformance();
        }
    }

    /**
     * @param cost
     */
    public void addVariableCosts(@NotNull BigDecimal cost) {
        if (cost.compareTo(BigDecimal.ZERO) > 0) {
            this.variableCosts = this.variableCosts.add(cost);
            this.updateTotalPerformance();
        }
    }

    /**
     * Adds the specified sale to the repair service sales
     *
     * @param sale the sale which should be added
     */
    public void addRepairServiceSale(@NotNull BigDecimal sale) {
        this.repairServiceSales = this.repairServiceSales.add(sale);
        this.gui.displayRepairServiceSale(this.repairServiceSales);
        this.updateTenthPart();
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
        this.updateTenthPart();
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
     * Adds the specified sale to the sale volume and displays the updated numbers
     *
     * @param sale the sale which should be added
     */
    public void addSale(@NotNull BigDecimal sale) {
        if (sale.compareTo(BigDecimal.ZERO) > 0) {
            this.salesVolume = this.salesVolume.add(sale);
            this.updateTenthPart();
        }
    }

    /**
     * Subtract the specified sale from the sale volume and displays the updated numbers
     *
     * @param sale the sale which should be subtracted
     */
    public void subtractSale(@NotNull BigDecimal sale) {
        if (sale.compareTo(BigDecimal.ZERO) > 0) {
            this.salesVolume = this.salesVolume.subtract(sale);
            this.updateTenthPart();
        }
    }

    /**
     * Adds the specified cost to the fixedCost, recalculates the balance and
     * displays the updated numbers.
     *
     * @param cost the cost which should be added
     */
    public void addFixedCost(double cost) {
        if (cost > 0) {
            this.fixedCosts = this.fixedCosts.add(new BigDecimal(cost));
            this.updateProfitAndLossAccountBalance();
        }
    }

    /**
     *
     * @param category
     * @return
     */
    public boolean removeCategory(String category) {
        //TODO 27.05.2024 check for category use in positionManager
        if (this.categories.contains(category)
                && this.sparePartsManager.getSparePartsOfCategory(category).isEmpty()) {
            if (this.categories.remove(category)) {
                this.gui.displayCategories(this.categories);
                return true;
            }
        }
        return false;
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
        this.updateTenthPart();
    }

    /**
     * @param message
     */
    //TODO 18.04.2024
    public void updateStatus(String message) {
        this.gui.updateStatus(message);
    }

    @Override
    public SaleBookData toData() {
        return new SaleBookData(this.repairServiceSales, this.extraordinaryIncome, this.paid,
                this.fixedCosts, this.sparePartsManager.toData(), this.positionsManager.toData(),
                ItemColor.getItemColors().toArray(new ItemColor[0]),
                this.suppliersManager.getSuppliers().toArray(new Supplier[0]),
                this.ordersManager.toData(),
                this.assetsManager.toData());
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
                Objects.equals(this.assetsManager, saleBook.assetsManager) &&
                Objects.equals(this.salesVolume, saleBook.salesVolume) &&
                Objects.equals(this.variableCosts, saleBook.variableCosts) &&
                Objects.equals(this.gui, saleBook.gui);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.sparePartsManager, this.positionsManager,
                this.suppliersManager, this.ordersManager, this.assetsManager, this.salesVolume,
                this.variableCosts, this.gui);
    }

    @Override
    public String toString() {
        return "SaleBook{" +
                "sparePartsManager=" + this.sparePartsManager +
                ", positionsManager=" + this.positionsManager +
                ", supplierManager=" + this.suppliersManager +
                ", ordersManager=" + this.ordersManager +
                ", assetManager=" + this.assetsManager +
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
        int length = positionData.length;
        Position[] result = new Position[length];

        for (int i = 0; i < length; i++) {
            Position position = new Position(positionData[i], ItemColor.getItemColorMap());
            result[i] = position;
            this.variableCosts = this.variableCosts.add(position.getTotalCost());
            this.categories.add(position.getCategory());
            if (position.isSold()) {
                this.salesVolume = this.salesVolume.add(position.getSellingPrice());
            }
        }
        return result;
    }

    /**
     *
     */
    //TODO 20.05.2024 rename
    private void updateProfitAndLossAccountBalance() {
        this.gui.displayFixedCosts(this.fixedCosts);
        this.gui.displayProfitAndLossAccountBalance(this.salesVolume.subtract(this.variableCosts)
                .subtract(this.fixedCosts));
    }

    /**
     * Recalculates the totalPerformance and displays the new calculated totalPerformance
     */
    private void updateTotalPerformance() {
        BigDecimal totalPerformance = this.salesVolume.subtract(this.variableCosts);
        this.gui.displayTotalPerformance(totalPerformance);
    }

    /**
     * Displays the tenth part, which includes the sale volume, tenth part of the income and the balance
     */
    private void updateTenthPart() {
        BigDecimal tenthPartTotalIncome = this.repairServiceSales.add(this.extraordinaryIncome)
                .add(this.salesVolume).divide(BigDecimal.TEN, RoundingMode.HALF_UP);

        this.gui.displaySales(this.salesVolume);
        this.gui.displayTenthPartTotalIncome(tenthPartTotalIncome);
        this.gui.displayTenthPartBalance(this.paid.subtract(tenthPartTotalIncome));
    }

    /**
     * Displays the components of this saleBook
     */
    private void displaySaleBook() {
        this.gui.displaySpareParts(this.sparePartsManager.getObservableList());
        this.gui.displaySparePartNames(this.sparePartsManager.getSparePartNames());
        this.gui.displayPositions(this.positionsManager.toFilteredTreeItem());
        this.gui.displaySuppliers(this.suppliersManager.getObservableList());
        this.gui.displaySupplierNames(this.suppliersManager.getSupplierNames());
        this.gui.displayOrders(this.ordersManager.getObservableList());
        this.gui.displayAssets(this.assetsManager.getObservableList());
        this.gui.displayCategories(this.categories);
        this.gui.displayRepairServiceSale(this.repairServiceSales);
        this.gui.displayExtraordinaryIncome(this.extraordinaryIncome);
        this.gui.displayPaid(this.paid);
        this.gui.displayVariableCosts(this.variableCosts);
        this.updateTenthPart();
        this.updateProfitAndLossAccountBalance();
        this.updateTotalPerformance();
    }
}