package gui.saleBookController.pages.profitAndLossAccountPage;

import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import costumeClasses.FXClasses.ImageButton;
import gui.Images;
import gui.saleBookController.pages.Page;
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
 * This class represents a ProfitAndLossAccountPage.
 *
 * @author xthe_white_lionx
 * @see gui.saleBookController.pages.Page
 */
public class ProfitAndLossAccountPage implements Initializable, Page {
    /**
     * Label to display the sum of the sales
     */
    @FXML
    public Label salesLbl;

    /**
     * Label to display the variable costs
     */
    @FXML
    public Label variableCostsLbl;

    /**
     * Label to display the fixe costs
     */
    @FXML
    public Label fixedCostsLbl;

    /**
     * Label to display the balance of the profit and loss account
     */
    @FXML
    public Label balanceLbl;

    /**
     * The base pane of this page
     */
    @FXML
    private Pane basePane;

    /**
     * The saleBook to operate on
     */
    private SaleBook saleBook;

    /**
     * The ribbonTab of this ProfitAndLossAccountPage
     */
    private RibbonTab ribbonTab;

    /**
     * Creates and loads a new ProfitAndLossAccountPage
     *
     * @return the new created ProfitAndLossAccountPage
     * @throws IOException if the fxml file cannot be loaded
     */
    public static ProfitAndLossAccountPage createProfitAndLossAccountPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages" +
                        "/profitAndLossAccountPage/ProfitAndLossAccountPage.fxml"));

        loader.load();
        return loader.getController();
    }

    /**
     * Initializes this page
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Button addFixCost = new ImageButton("add fixed cost", Images.ADD_IMAGE,
                actionEvent -> this.addFixedCosts());
        Button editBtn = new ImageButton("edit", Images.EDIT_IMAGE, actionEvent -> this.edit());
        this.ribbonTab = RibbonTabUtils.createRibbonTab("Profit and loss account",
                RibbonGroupUtils.createRibbonGroup("functions", addFixCost),
                RibbonGroupUtils.createRibbonGroup("organisation", editBtn));
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
     * Handles the add fix cost button and opens the matching dialog
     */
    private void addFixedCosts() {
        // TODO: 01.11.2023 implement
        this.saleBook.getFixedCosts();
    }

    /**
     * Handles the edit button and opens the matching dialog
     */
    private void edit() {
        // TODO: 01.11.2023 implement
        System.out.println("hier könnte jetzt etwas passieren");
    }
}
