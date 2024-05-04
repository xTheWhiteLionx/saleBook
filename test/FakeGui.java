import gui.ObservableTreeItemMapBinder;
import javafx.collections.ObservableList;
import logic.Asset;
import logic.GUIConnector;
import logic.SparePart;
import logic.order.Order;
import logic.Supplier;
import logic.products.position.Position;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

public class FakeGui implements GUIConnector {

    @Override
    public void displayPositions(@NotNull Collection<Position> positions) {

    }

    @Override
    public void displayCategories(@NotNull Collection<String> categories) {

    }

    @Override
    public void displaySpareParts(@NotNull ObservableList<SparePart> spareParts) {

    }

    @Override
    public void displaySparePartNames(@NotNull Collection<String> nameOfSpareParts) {

    }

    @Override
    public void displaySuppliers(@NotNull ObservableList<Supplier> suppliers) {

    }

    @Override
    public void displaySupplierNames(@NotNull Set<String> supplierNames) {

    }

    @Override
    public void displayOrders(@NotNull ObservableList<Order> orders) {

    }

    /**
     * @param assets
     */
    @Override
    public void displayAssets(@NotNull ObservableList<Asset> assets) {

    }

    @Override
    public void displayRepairServiceSale(@NotNull BigDecimal repairServiceSales) {

    }

    @Override
    public void displayExtraordinaryIncome(@NotNull BigDecimal extraordinaryIncome) {

    }

    @Override
    public void displaySales(@NotNull BigDecimal sales) {

    }

    @Override
    public void displayPaid(@NotNull BigDecimal paid) {

    }

    @Override
    public void displayTenthPartBalance(@NotNull BigDecimal balance) {

    }

    @Override
    public void displayTenthPartTotalIncome(@NotNull BigDecimal tenthPartTotalSales) {

    }

    @Override
    public void displayTotalPerformance(@NotNull BigDecimal totalPerformance) {

    }

    @Override
    public void displayVariableCosts(@NotNull BigDecimal variableCosts) {

    }

    @Override
    public void displayFixedCosts(@NotNull BigDecimal fixedCosts) {

    }

    @Override
    public void displayProfitAndLossAccountBalance(@NotNull BigDecimal balance) {

    }

    @Override
    public void displayOrderedSpareParts(@NotNull Set<SparePart> spareParts) {

    }

    @Override
    public void refreshPosition() {

    }

    @Override
    public void displayPositions(@NotNull ObservableTreeItemMapBinder<Integer> root) {

    }

    @Override
    public void refreshSpareParts() {

    }

    @Override
    public void updateStatus(@NotNull String message) {

    }
}
