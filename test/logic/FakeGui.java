package logic;

import gui.ObservableTreeItemMapBinder;
import javafx.collections.ObservableList;
import logic.order.Order;
import logic.products.position.Position;
import logic.sparePart.SparePart;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

/**
 *
 */
public class FakeGui implements GUIConnector {
    /**
     * @param positions the positions which should be displayed
     */
    @Override
    public void displayPositions(@NotNull Collection<Position> positions) {

    }

    /**
     * @param categories the categories of the positions
     */
    @Override
    public void displayCategories(@NotNull Collection<String> categories) {

    }

    /**
     * @param spareParts the spareParts which should be displayed
     */
    @Override
    public void displaySpareParts(@NotNull ObservableList<SparePart> spareParts) {

    }

    /**
     * @param nameOfSpareParts the name of the spare parts which should be used for autocompletion
     */
    @Override
    public void displaySparePartNames(@NotNull Collection<String> nameOfSpareParts) {

    }

    /**
     * @param suppliers the suppliers which should be displayed
     */
    @Override
    public void displaySuppliers(@NotNull ObservableList<Supplier> suppliers) {

    }

    /**
     * @param supplierNames the name of the suppliers which should be used for autocompletion
     */
    @Override
    public void displaySupplierNames(@NotNull Set<String> supplierNames) {

    }

    /**
     * @param orders the orders which should be displayed
     */
    @Override
    public void displayOrders(@NotNull ObservableList<Order> orders) {

    }

    /**
     * @param assets the assets which should be displayed
     */
    @Override
    public void displayAssets(@NotNull ObservableList<Asset> assets) {

    }

    /**
     * @param repairServiceSales the sales of the repair service sales
     *                           which should be displayed
     */
    @Override
    public void displayRepairServiceSale(@NotNull BigDecimal repairServiceSales) {

    }

    /**
     * @param extraordinaryIncome the extraordinary income which should be displayed
     */
    @Override
    public void displayExtraordinaryIncome(@NotNull BigDecimal extraordinaryIncome) {

    }

    /**
     * @param sales the sales which should be displayed
     */
    @Override
    public void displaySales(@NotNull BigDecimal sales) {

    }

    /**
     * @param paid the number of taxes which were paid
     */
    @Override
    public void displayPaid(@NotNull BigDecimal paid) {

    }

    /**
     * @param balance the balance of the tenthPart page which should be displayed
     */
    @Override
    public void displayTenthPartBalance(@NotNull BigDecimal balance) {

    }

    /**
     * @param tenthPartTotalSales the total sales for the tenthPart page which should be displayed
     */
    @Override
    public void displayTenthPartTotalIncome(@NotNull BigDecimal tenthPartTotalSales) {

    }

    /**
     * @param totalPerformance the total performance of the saleBook
     */
    @Override
    public void displayTotalPerformance(@NotNull BigDecimal totalPerformance) {

    }

    /**
     * @param variableCosts the variable costs of the saleBook
     */
    @Override
    public void displayVariableCosts(@NotNull BigDecimal variableCosts) {

    }

    /**
     * @param fixedCosts the fixed costs of the saleBook
     */
    @Override
    public void displayFixedCosts(@NotNull BigDecimal fixedCosts) {

    }

    /**
     * @param balance the balance of the profit and loss account
     */
    @Override
    public void displayProfitAndLossAccountBalance(@NotNull BigDecimal balance) {

    }

    /**
     * @param spareParts the spare parts of the current order
     */
    @Override
    public void displayOrderedSpareParts(@NotNull Set<SparePart> spareParts) {

    }

    /**
     *
     */
    @Override
    public void refreshPosition() {

    }

    /**
     * @param root of the positions which should be displayed
     */
    @Override
    public void displayPositions(@NotNull ObservableTreeItemMapBinder<Integer> root) {

    }

    /**
     *
     */
    @Override
    public void refreshSpareParts() {

    }

    /**
     * @param message the message which should be displayed
     */
    @Override
    public void updateStatus(@NotNull String message) {

    }

    @Override
    public void refreshOrders() {

    }

    @Override
    public void displaySumAssetsValue(BigDecimal sumValue) {

    }
}
