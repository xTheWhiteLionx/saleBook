package gui.saleBookController.pages.tenthPartPage.functions;

import gui.ApplicationMain;
import gui.FXutils.LabelUtils;
import utils.StringUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.saleBook.SaleBook;
import utils.BigDecimalUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.FXutils.StageUtils.createStyledStage;
import static gui.FXutils.TextFieldUtils.isValidNumber;

/**
 *
 *
 * @author xthe_white_lionx
 */
public class EditTenthPartController implements javafx.fxml.Initializable {

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

    private SaleBook saleBook;

    private BigDecimal sales;

    /**
     *
     * @return
     * @throws IOException
     */
    public static void createEditTenthPartController(SaleBook saleBook) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/tenthPartPage" +
                        "/functions/EditTenthPartPage.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("edit tenth part page");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        EditTenthPartController controller = loader.getController();
        controller.setSaleBook(saleBook);
        stage.showAndWait();
    }

    /**
     * Initializes this controller
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BooleanBinding anyInputIsInvalid = new BooleanBinding() {
            {
                {
                    this.bind(EditTenthPartController.this.repairServiceSalesTxtFld.textProperty(),
                            EditTenthPartController.this.extraordinaryIncomeTxtFld.textProperty(),
                            EditTenthPartController.this.paidTxtFld.textProperty());
                }
            }
            @Override
            protected boolean computeValue() {
                return !isValidNumber(EditTenthPartController.this.repairServiceSalesTxtFld) &&
                        !isValidNumber(EditTenthPartController.this.extraordinaryIncomeTxtFld) &&
                        !isValidNumber(EditTenthPartController.this.paidTxtFld);
            }
        };
        this.applyBtn.disableProperty().bind(anyInputIsInvalid);
    }

    private void setSaleBook(SaleBook saleBook) {
        this.saleBook = saleBook;

        //TODO 25.01.2024 all needed?
        BigDecimal repairServiceSales = saleBook.getRepairServiceSales();
        BigDecimal extraordinaryIncome = saleBook.getExtraordinaryIncome();
        BigDecimal paid = saleBook.getPaid();
        this.sales = saleBook.getSalesVolume();
        BigDecimal tenthPartTotalIncome = repairServiceSales.add(extraordinaryIncome)
                .add(this.sales).divide(BigDecimal.TEN, RoundingMode.HALF_UP);

        this.repairServiceSalesTxtFld.setText(String.valueOf(repairServiceSales));
        this.repairServiceSalesTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
            if (StringUtils.isValidNumber(newText)){
                this.tenthPartTotalSalesLbl.setText(String.valueOf(repairServiceSales.add(extraordinaryIncome)
                        .add(this.sales).divide(BigDecimal.TEN, RoundingMode.HALF_UP)));
            }
        });
        this.extraordinaryIncomeTxtFld.setText(String.valueOf(extraordinaryIncome));
        this.paidTxtFld.setText(String.valueOf(paid));

        LabelUtils.setMoney(this.salesLbl, this.sales);
        this.tenthPartTotalSalesLbl.setText(String.valueOf(tenthPartTotalIncome));
        this.balanceLbl.setText(String.valueOf(paid.subtract(tenthPartTotalIncome)));
    }

    /**
     *
     */
    @FXML
    public void handleApply() {
        BigDecimal repairServiceSales = BigDecimalUtils.parse(this.repairServiceSalesTxtFld.getText());
        BigDecimal extraordinaryIncome = BigDecimalUtils.parse(this.extraordinaryIncomeTxtFld.getText());
        BigDecimal paid = BigDecimalUtils.parse(this.paidTxtFld.getText());

        this.saleBook.recalculateTenthPartPage(repairServiceSales, extraordinaryIncome, paid);
        this.handleCancel();
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }
}
