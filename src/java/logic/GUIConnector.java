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
 * Interface used for the logic of the saleBook to communicate with the
 * gui.
 *
 * @author xthe_white_lionx
 */
public interface GUIConnector {

    /**
     * Displays the specified positions
     *
     * @param positions the positions which should be displayed
     */
    void displayPositions(@NotNull Collection<Position> positions);

    /**
     * Displays the specified categories in the filter, to filter the positions.
     *
     * @param categories the categories of the positions
     */
    void displayCategories(@NotNull Collection<String> categories);

    /**
     * Displays the specified spareParts
     *
     * @param spareParts the spareParts which should be displayed
     */
    void displaySpareParts(@NotNull ObservableList<logic.sparePart.SparePart> spareParts);

    /**
     * Displays the specified sparePartNames for the autocompletion of the searchbar
     *
     * @param nameOfSpareParts the name of the spare parts which should be used for autocompletion
     */
    void displaySparePartNames(@NotNull Collection<String> nameOfSpareParts);

    /**
     * Displays the specified suppliers
     *
     * @param suppliers the suppliers which should be displayed
     */
    void displaySuppliers(@NotNull ObservableList<Supplier> suppliers);

    /**
     * Displays the specified supplier names for the autocompletion of the searchbar
     *
     * @param supplierNames the name of the suppliers which should be used for autocompletion
     */
    void displaySupplierNames(@NotNull Set<String> supplierNames);

    /**
     * Displays the specified orders
     *
     * @param orders the orders which should be displayed
     */
    void displayOrders(@NotNull ObservableList<Order> orders);

    /**
     * Displays the specified assets
     *
     * @param assets the assets which should be displayed
     */
    void displayAssets(@NotNull ObservableList<Asset> assets);

    /**
     * Displays the specified repairServiceSales
     *
     * @param repairServiceSales the sales of the repair service sales
     *                           which should be displayed
     */
    void displayRepairServiceSale(@NotNull BigDecimal repairServiceSales);

    /**
     * Displays the specified extraordinaryIncome
     *
     * @param extraordinaryIncome the extraordinary income which should be displayed
     */
    void displayExtraordinaryIncome(@NotNull BigDecimal extraordinaryIncome);

    /**
     * Displays the specified sales
     *
     * @param sales the sales which should be displayed
     */
    void displaySales(@NotNull BigDecimal sales);

    /**
     * Displays the specified paid, which represents the number of taxes which were paid
     *
     * @param paid the number of taxes which were paid
     */
    void displayPaid(@NotNull BigDecimal paid);

    /**
     * Displays the specified balance of the tenthPart page
     *
     * @param balance the balance of the tenthPart page which should be displayed
     */
    void displayTenthPartBalance(@NotNull BigDecimal balance);

    /**
     * Displays the specified total sales for the tenthPart page
     *
     * @param tenthPartTotalSales the total sales for the tenthPart page which should be displayed
     */
    void displayTenthPartTotalIncome(@NotNull BigDecimal tenthPartTotalSales);

    /**
     * Displays the specified totalPerformance
     *
     * @param totalPerformance the total performance of the saleBook
     */
    void displayTotalPerformance(@NotNull BigDecimal totalPerformance);

    /**
     * Displays the specified variableCosts
     *
     * @param variableCosts the variable costs of the saleBook
     */
    void displayVariableCosts(@NotNull BigDecimal variableCosts);

    /**
     * Displays the specified fixedCosts
     *
     * @param fixedCosts the fixed costs of the saleBook
     */
    void displayFixedCosts(@NotNull BigDecimal fixedCosts);

    /**
     * Displays the specified balance of the profit and loss account
     *
     * @param balance the balance of the profit and loss account
     */
    void displayProfitAndLossAccountBalance(@NotNull BigDecimal balance);

    /**
     * Displays the spareParts of the current order
     *
     * @param spareParts the spare parts of the current order
     */
    void displayOrderedSpareParts(@NotNull Set<SparePart> spareParts);

    /**
     * Refreshes the displayed information of the current displayed position
     */
    void refreshPosition();

    /**
     * Displays the positions of the specified root
     *
     * @param root of the positions which should be displayed
     */
    void displayPositions(@NotNull ObservableTreeItemMapBinder<Integer> root);

    /**
     * Refreshes the displayed spareParts and the current displayed sparePart if needed
     */
    void refreshSpareParts();

    /**
     * Updates the current status to the specified message
     *
     * @param message the message which should be displayed
     */
    void updateStatus(@NotNull String message);

    void refreshOrders();
}
