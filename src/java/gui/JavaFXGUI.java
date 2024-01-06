package gui;

import gui.saleBookController.pages.ordersPage.OrdersPage;
import gui.saleBookController.pages.positionsPage.PositionsPage;
import gui.saleBookController.pages.profitAndLossAccountPage.ProfitAndLossAccountPage;
import gui.saleBookController.pages.sparePartsPage.SparePartsPage;
import gui.saleBookController.pages.suppliersPage.SuppliersPage;
import gui.saleBookController.pages.tenthPartPage.TenthPartPage;
import gui.util.LabelUtils;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.util.Duration;
import logic.GUIConnector;
import logic.SparePart;
import logic.order.Order;
import logic.order.Supplier;
import logic.products.Item;
import logic.products.Product;
import logic.products.position.Position;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static gui.util.LabelUtils.setMoneyAndColor;


/**
 * This class is responsible for changing the gui when the logic deems it
 * necessary. Created by the gui and then passed as a parameter into the logic.
 *
 * @author xthe_white_lionx
 */
public class JavaFXGUI implements GUIConnector {

    /**
     *
     */
    public static final DecimalFormat df = new DecimalFormat("#.##");
    /**
     *
     */
    private static final Duration SHOW_DURATION = Duration.seconds(5);
    /**
     * All {@link }s in a Tableview
     */
    private final PositionsPage positionsPage;
    /**
     *
     */
    private final SparePartsPage sparePartsPage;
    /**
     *
     */
    private final TenthPartPage tenthPartPage;
    /**
     *
     */
    private final ProfitAndLossAccountPage profitAndLossAccountPage;
    /**
     *
     */
    private final SuppliersPage suppliersPage;
    /**
     *
     */
    private final OrdersPage ordersPage;

    /**
     *
     */
    private final TreeItem<Product> root;

    /**
     * Label to show the total performance
     * of the displayed investments
     */
    private final Label totalPerformanceLabel;

    /**
     * label to show the current status
     */
    private final Label status;

    /**
     * The constructor. Gets past all components of the gui that may change
     * due to actions in the logic.
     *
     * @param sparePartsPage           all platforms in a Tableview
     * @param totalPerformanceLabel    label to change the total performance
     *                                 of the displayed investments
     * @param tenthPartPage
     * @param profitAndLossAccountPage
     * @param suppliersPage
     * @param ordersPage
     * @param status               label to change the current status
     */
    public JavaFXGUI(@NotNull PositionsPage positionsPage, @NotNull SparePartsPage sparePartsPage,
                     @NotNull Label totalPerformanceLabel, @NotNull TenthPartPage tenthPartPage,
                     @NotNull ProfitAndLossAccountPage profitAndLossAccountPage,
                     @NotNull SuppliersPage suppliersPage,
                     @NotNull OrdersPage ordersPage, @NotNull Label status) {
        this.positionsPage = positionsPage;
        this.root = positionsPage.getTrTblVw().getRoot();
        this.tenthPartPage = tenthPartPage;
        this.sparePartsPage = sparePartsPage;
        this.totalPerformanceLabel = totalPerformanceLabel;
        this.profitAndLossAccountPage = profitAndLossAccountPage;
        this.suppliersPage = suppliersPage;
        this.ordersPage = ordersPage;
        this.status = status;
    }

    /**
     * Returns a string out of the given double value and
     * replace commas with dots
     *
     * @param value the given double value
     * @return string out of double value
     * @throws IllegalArgumentException if
     */
    public static @NotNull String format(@NotNull Number value) {
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }

    /**
     * Returns a string out of the given double value and
     * replace commas with dots
     *
     * @param value the given double value
     * @return string out of double value
     */
    public static @NotNull String formatMoney(@NotNull Number value) {
        return format(value) + " " + LabelUtils.SYMBOL_OF_CURRENCY;
    }

    /**
     *
     * @param fn
     * @return
     */
    public static @NotNull Function<Position, String> formatMoney(
            @NotNull Function<Position, Number> fn) {
        return position -> formatMoney(fn.apply(position));
    }

    @Override
    public void displaySpareParts(@NotNull ObservableList<SparePart> spareParts) {
        this.sparePartsPage.setSpareParts(spareParts);
    }

    @Override
    public void displaySparePartNames(@NotNull Collection<String> nameOfSpareParts) {
        TextFields.bindAutoCompletion(this.sparePartsPage.nameSearchbarTxtFld, nameOfSpareParts);
    }

    @Override
    public void displayPositions(@NotNull Collection<Position> positions) {
        this.root.getChildren().clear();
        for (Position position : positions) {
            TreeItem<Product> positionTreeItem = new TreeItem<>(position);
            ObservableList<TreeItem<Product>> children = positionTreeItem.getChildren();
            List<Item> items = position.getItems();
            for (Item item : items) {
                children.add(new TreeItem<>(item));
            }
            this.root.getChildren().add(positionTreeItem);
        }
    }

    @Override
    public void displayCategories(@NotNull Collection<String> models) {
        this.positionsPage.setCategories(models);
    }

    @Override
    public void displaySuppliers(@NotNull ObservableList<Supplier> suppliers) {
        this.suppliersPage.setSuppliers(suppliers);
    }

    @Override
    public void displaySupplierNames(@NotNull Set<String> supplierNames){
        TextFields.bindAutoCompletion(this.suppliersPage.nameSearchbarTxtFld, supplierNames);
    }

    @Override
    public void displayOrders(@NotNull ObservableList<Order> orders) {
        this.ordersPage.setOrders(orders);
    }

    @Override
    public void displayRepairServiceSale(@NotNull BigDecimal repairServiceSales) {
        LabelUtils.setMoney(this.tenthPartPage.repairServiceSalesLbl, repairServiceSales);
    }

    @Override
    public void displayExtraordinaryIncome(@NotNull BigDecimal extraordinaryIncome) {
        LabelUtils.setMoney(this.tenthPartPage.extraordinaryIncomeLbl, extraordinaryIncome);
    }

    @Override
    public void displaySales(@NotNull BigDecimal sales) {
        LabelUtils.setMoney(this.tenthPartPage.salesLbl, sales);
        LabelUtils.setMoney(this.profitAndLossAccountPage.salesLbl, sales);
    }

    @Override
    public void displayPaid(@NotNull BigDecimal paid) {
        LabelUtils.setMoney(this.tenthPartPage.paidLbl, paid);
    }

    @Override
    public void displayTenthPartBalance(@NotNull BigDecimal balance) {
        LabelUtils.setMoneyAndColor(this.tenthPartPage.balanceLbl, balance);
    }

    @Override
    public void displayTenthPartTotalIncome(@NotNull BigDecimal tenthPartTotalSales) {
        LabelUtils.setMoney(this.tenthPartPage.tenthPartTotalSalesLbl, tenthPartTotalSales);
    }

    @Override
    public void displayTotalPerformance(@NotNull BigDecimal totalPerformance) {
        setMoneyAndColor(this.totalPerformanceLabel, totalPerformance);
    }

    @Override
    public void displayVariableCosts(@NotNull BigDecimal variableCosts) {
        LabelUtils.setMoney(this.profitAndLossAccountPage.variableCostsLbl, variableCosts);
    }

    @Override
    public void displayFixedCosts(@NotNull BigDecimal fixedCosts) {
        LabelUtils.setMoney(this.profitAndLossAccountPage.fixedCostsLbl, fixedCosts);
    }

    @Override
    public void displayProfitAndLossAccountBalance(@NotNull BigDecimal balance) {
        setMoneyAndColor(this.profitAndLossAccountPage.balanceLbl, balance);
    }

    @Override
    public void displayOrderedSpareParts(@NotNull Set<SparePart> spareParts) {
        this.ordersPage.setSpareParts(spareParts);
    }

    @Override
    public void refreshPosition() {
        this.positionsPage.updateControlsAndDetail();
    }

    @Override
    public void displayPositions(@NotNull ObservableTreeItemMapBinder<Integer> root) {
        this.positionsPage.setRoot(root);
    }

    @Override
    public void refreshSpareParts() {
        this.sparePartsPage.updateTableViewAndDetail();
    }

    @Override
    public void updateStatus(@NotNull String message) {
        this.status.setVisible(true);
        this.status.setText(message);
        PauseTransition pt = new PauseTransition(SHOW_DURATION);
        pt.onFinishedProperty().set(actionEvent -> {
            this.status.setVisible(false);
            this.status.setText("");
        });
        pt.play();
    }
}
