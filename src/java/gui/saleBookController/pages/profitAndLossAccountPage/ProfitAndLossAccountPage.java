package gui.saleBookController.pages.profitAndLossAccountPage;

import com.pixelduke.control.ribbon.RibbonTab;
import gui.ApplicationMain;
import gui.ImageButton;
import gui.Images;
import gui.saleBookController.pages.Page;
import gui.util.RibbonGroupUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfitAndLossAccountPage implements Initializable, Page {

    @FXML
    public Label salesLbl;
    @FXML
    public Label variableCostsLbl;
    @FXML
    public Label fixedCostsLbl;
    @FXML
    public Label balanceLbl;
    @FXML
    private Pane pane;

    private SaleBook saleBook;
    private RibbonTab ribbonTab;

    public static ProfitAndLossAccountPage createProfitAndLossAccountPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages" +
                        "/profitAndLossAccountPage/ProfitAndLossAccountPage.fxml"));

        loader.load();
        return loader.getController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ribbonTab = new RibbonTab("Profit and loss account");
        Button addFixCost = new ImageButton("add fixed cost", Images.ADD_IMAGE,
                actionEvent -> this.addFixedCosts());
        Button editBtn = new ImageButton("edit", Images.EDIT_IMAGE, actionEvent -> this.edit());
        this.ribbonTab.getRibbonGroups().addAll(RibbonGroupUtil.createRibbonGroup("functions", addFixCost),
                RibbonGroupUtil.createRibbonGroup("organisation", editBtn));
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

    private void addFixedCosts() {
        // TODO: 01.11.2023 implement
        this.saleBook.getFixedCosts();
    }

    /**
     *
     */
    private void edit() {
        // TODO: 01.11.2023 implement
        System.out.println("hier könnte jetzt etwas passieren");
    }
}
