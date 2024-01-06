package gui.saleBookController.pages.tenthPartPage;

import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.DialogWindow;
import gui.ImageButton;
import gui.Images;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.tenthPartPage.functions.EditTenthPartPage;
import gui.saleBookController.pages.tenthPartPage.functions.TextFieldDialog;
import gui.util.RibbonGroupUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import logic.saleBook.SaleBook;
import logic.utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class TenthPartPage implements Initializable, Page {

    @FXML
    public Label repairServiceSalesLbl;
    @FXML
    public Label extraordinaryIncomeLbl;
    @FXML
    public Label salesLbl;
    @FXML
    public Label tenthPartTotalSalesLbl;
    @FXML
    public Label paidLbl;
    @FXML
    public Label balanceLbl;
    @FXML
    private Pane pane;

    private SaleBook saleBook;
    private RibbonTab ribbonTab;

    /**
     * @return
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
    public @NotNull Pane getPane() {
        return this.pane;
    }

    @Override
    public @NotNull RibbonTab getRibbonTab() {
        return this.ribbonTab;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ribbonTab = new RibbonTab("Tenth part");
        Button addRepairServiceSaleBtn = new ImageButton("add repair service sale", Images.ADD_IMAGE,
                actionEvent -> this.addRepairServiceSale());
        Button addExtraordinaryIncome = new ImageButton("add extraordinary income" , Images.ADD_IMAGE,
                actionEvent -> this.addExtraordinaryIncome());
        Button addPayment = new ImageButton("add payment" , Images.ADD_IMAGE,
                actionEvent -> this.addPayment());
        Button editBtn = new ImageButton("edit", Images.EDIT_IMAGE, actionEvent -> this.edit());
        this.ribbonTab.getRibbonGroups().addAll(RibbonGroupUtil.createRibbonGroup("functions", addRepairServiceSaleBtn
        , addExtraordinaryIncome, addPayment), RibbonGroupUtil.createRibbonGroup("organisation", editBtn));
    }

    private void addRepairServiceSale() {
        try {
            TextFieldDialog textFieldDialog = TextFieldDialog.createTextFieldDialog("repair service sale");
            textFieldDialog.getResult().ifPresent(text -> this.saleBook.
                    addRepairServiceSale(BigDecimalUtils.parse(text)));
        } catch (IOException e) {
            DialogWindow.displayError("failed to load textFieldDialog", e);
        }
    }

    private void addExtraordinaryIncome() {
        try {
            TextFieldDialog textFieldDialog = TextFieldDialog.createTextFieldDialog("extraordinary income");
            textFieldDialog.getResult().ifPresent(text -> this.saleBook.
                    addExtraordinaryIncome(BigDecimalUtils.parse(text)));
        } catch (IOException e) {
            DialogWindow.displayError("failed to load textFieldDialog", e);
        }
    }

   private void addPayment() {
        try {
            TextFieldDialog textFieldDialog = TextFieldDialog.createTextFieldDialog("payment");
            textFieldDialog.getResult().ifPresent(text -> this.saleBook.
                    addPayment(BigDecimalUtils.parse(text)));
        } catch (IOException e) {
            DialogWindow.displayError("failed to load textFieldDialog", e);
        }
    }

    /**
     *
     */
    private void edit() {
        // TODO: 01.11.2023 implement
        System.out.println("hier könnte jetzt etwas passieren");
        try {
            EditTenthPartPage editTenthPage = EditTenthPartPage.createEditTenthPartPage(this.saleBook);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
