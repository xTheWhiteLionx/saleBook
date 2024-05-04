package gui.saleBookController.pages.tenthPartPage;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.DialogWindow;
import gui.ImageButton;
import gui.Images;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.tenthPartPage.functions.EditTenthPartController;
import gui.TextFieldDialog;
import gui.FXutils.RibbonGroupUtils;
import gui.FXutils.RibbonTabUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This Page shows the taxes of the saleBook and handles the possible controls for it
 *
 * @author xthe_white_lionx
 * @see gui.saleBookController.pages.Page
 */
public class TenthPartPage implements Initializable, Page {

    /**
     * Label to display the service sales
     */
    @FXML
    public Label repairServiceSalesLbl;

    /**
     * Label to display the extraordinary income
     */
    @FXML
    public Label extraordinaryIncomeLbl;

    /**
     * Label to display the service sales
     */
    @FXML
    public Label salesLbl;

    /**
     * Label to display the tenth part of the total sales
     */
    @FXML
    public Label tenthPartTotalSalesLbl;

    /**
     * Label to display the paid of tenth part of the total sales
     */
    @FXML
    public Label paidLbl;

    /**
     * Label to display the balance
     */
    @FXML
    public Label balanceLbl;

    /**
     * The base pane of this TenthPartPage
     */
    @FXML
    private Pane basePane;

    /**
     * The saleBook of this TenthPartPage
     */
    private SaleBook saleBook;

    /**
     * The RibbonTab of this TenthPartPage
     */
    private RibbonTab ribbonTab;

    /**
     * Creates and loads a new TenthPartPage
     *
     * @return the new created TenthPartPage
     * @throws IOException if the fxml cannot be loaded
     */
    public static @NotNull TenthPartPage createTenthPartPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/tenthPartPage/TenthPartPage.fxml"));

        loader.load();
        return loader.getController();
    }

    @Override
    public void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
    }

    @Override
    public @NotNull Pane getBasePane() {
        return this.basePane;
    }

    @Override
    public @NotNull RibbonTab getRibbonTab() {
        return this.ribbonTab;
    }

    /**
     * Initializes this page
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Button addRepairServiceSaleBtn = new ImageButton("add repair service sale", Images.ADD_IMAGE,
                actionEvent -> this.addRepairServiceSale());
        Button addExtraordinaryIncome = new ImageButton("add extraordinary income" , Images.ADD_IMAGE,
                actionEvent -> this.addExtraordinaryIncome());
        Button addPayment = new ImageButton("add payment" , Images.ADD_IMAGE,
                actionEvent -> this.addPayment());
        Button editBtn = new ImageButton("edit", Images.EDIT_IMAGE, actionEvent -> this.edit());
        RibbonGroup functionRibbonGroup = RibbonGroupUtils.createRibbonGroup("functions", addRepairServiceSaleBtn
                , addExtraordinaryIncome, addPayment);
        RibbonGroup organisationRibbonGroup = RibbonGroupUtils.createRibbonGroup("organisation", editBtn);
        this.ribbonTab = RibbonTabUtils.createRibbonTab("Tenth part",
                functionRibbonGroup, organisationRibbonGroup);
    }

    /**
     * Opens a new TextFieldDialog to add the new repair service sale
     */
    private void addRepairServiceSale() {
        try {
            TextFieldDialog textFieldDialog = TextFieldDialog.createTextFieldDialog("repair service sale");
            textFieldDialog.getResult().ifPresent(bigDecimal -> this.saleBook.addRepairServiceSale(bigDecimal));
        } catch (IOException e) {
            DialogWindow.displayError("failed to load textFieldDialog", e);
        }
    }

    /**
     * Opens a new TextFieldDialog to add the new extraordinary income
     */
    private void addExtraordinaryIncome() {
        try {
            TextFieldDialog textFieldDialog = TextFieldDialog.createTextFieldDialog("extraordinary income");
            textFieldDialog.getResult().ifPresent(bigDecimal -> this.saleBook.addExtraordinaryIncome(bigDecimal));
        } catch (IOException e) {
            DialogWindow.displayError("failed to load textFieldDialog", e);
        }
    }

    /**
     * Opens a new TextFieldDialog to add the new payment
     */
   private void addPayment() {
        try {
            TextFieldDialog textFieldDialog = TextFieldDialog.createTextFieldDialog("payment");
            textFieldDialog.getResult().ifPresent(bigDecimal -> this.saleBook.addPayment(bigDecimal));
        } catch (IOException e) {
            DialogWindow.displayError("failed to load textFieldDialog", e);
        }
    }

    /**
     * Opens a new EditTenthPartController
     */
    private void edit() {
        try {
            EditTenthPartController.createEditTenthPartController(this.saleBook);
        } catch (IOException e) {
            DialogWindow.displayError("failed to load EditTenthPartController", e);
        }
    }
}
