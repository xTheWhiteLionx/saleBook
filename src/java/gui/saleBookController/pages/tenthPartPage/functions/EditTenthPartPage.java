package gui.saleBookController.pages.tenthPartPage.functions;

import gui.ApplicationMain;
import gui.util.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.saleBook.SaleBook;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static gui.util.StageUtils.createStyledStage;

public class EditTenthPartPage {
    @FXML
    private TextField repairServiceSalesTxtFld;
    @FXML
    private TextField extraordinaryIncomeTxtFld;
    @FXML
    private TextField paidTxtFld;
    @FXML
    public Label salesLbl;
    @FXML
    public Label tenthPartTotalSalesLbl;
    @FXML
    public Label balanceLbl;
    @FXML
    private Button applyBtn;

    private BigDecimal repairServiceSales;
    private BigDecimal extraordinaryIncome;
    private BigDecimal paid;
    private BigDecimal sales;
    private SaleBook saleBook;

    /**
     *
     * @return
     * @throws IOException
     */
    public static EditTenthPartPage createEditTenthPartPage(SaleBook saleBook) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/tenthPartPage" +
                        "/functions/EditTenthPartPage.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("edit tenth part page");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        EditTenthPartPage controller = loader.getController();
        controller.initializeController(saleBook);
        stage.showAndWait();
        return controller;
    }

    /**
     *
     *
     * @param repairServiceSales
     * @param extraordinaryIncome
     * @param sales
     * @param tenthPartTotalSales
     * @param paid
     * @param balance
     */
    private void initializeController(BigDecimal repairServiceSales,
                                      BigDecimal extraordinaryIncome,
                                      BigDecimal sales,
                                      BigDecimal tenthPartTotalSales,
                                      BigDecimal paid,
                                      BigDecimal balance) {
        this.repairServiceSalesTxtFld.setText(String.valueOf(repairServiceSales));
        this.extraordinaryIncomeTxtFld.setText(String.valueOf(extraordinaryIncome));
        this.paidTxtFld.setText(String.valueOf(paid));

        this.salesLbl.setText(String.valueOf(sales));
        this.tenthPartTotalSalesLbl.setText(String.valueOf(tenthPartTotalSales));
        this.balanceLbl.setText(String.valueOf(balance));

        // TODO: 06.12.2023
    }

    private void initializeController(SaleBook saleBook) {
        this.saleBook = saleBook;

        this.repairServiceSales = saleBook.getRepairServiceSales();
        this.extraordinaryIncome = saleBook.getExtraordinaryIncome();
        this.paid = saleBook.getPaid();
        this.sales = saleBook.getSalesVolume();
        BigDecimal tenthPartTotalIncome = this.repairServiceSales.add(this.extraordinaryIncome)
                .add(this.sales).divide(BigDecimal.TEN, RoundingMode.HALF_UP);

        this.repairServiceSalesTxtFld.setText(String.valueOf(this.repairServiceSales));
        this.repairServiceSalesTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
            if (StringUtils.isValidNumber(newText)){
                this.tenthPartTotalSalesLbl.setText(String.valueOf(this.repairServiceSales.add(this.extraordinaryIncome)
                        .add(this.sales).divide(BigDecimal.TEN, RoundingMode.HALF_UP)));
            }
        });
        this.extraordinaryIncomeTxtFld.setText(String.valueOf(this.extraordinaryIncome));
        this.paidTxtFld.setText(String.valueOf(this.paid));

        this.salesLbl.setText(String.valueOf(this.sales));
        this.tenthPartTotalSalesLbl.setText(String.valueOf(tenthPartTotalIncome));
        this.balanceLbl.setText(String.valueOf(this.paid.subtract(tenthPartTotalIncome)));
    }

    @FXML
    private void handleApply() {
        // TODO: 06.12.2023
        this.saleBook.recalculateTenthPartPage(this.repairServiceSales, this.extraordinaryIncome, this.paid);
        this.handleCancel();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }
}
