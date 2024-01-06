package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.util.ChoiceBoxUtils;
import gui.util.LabelUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.products.position.ShippingCompany;
import logic.saleBook.SaleBook;
import logic.utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;
import static gui.util.StringUtils.isValidNumber;

public class ToShipController implements Initializable {
    @FXML
    private ChoiceBox<ShippingCompany> shippingCompaniesChcBx;
    @FXML
    private TextField costTxtFld;
    @FXML
    private Label costCurrencyLbl;
    @FXML
    private TextField trackingNumberTxtFld;
    @FXML
    private Button applyBtn;

    private int posId;

    private SaleBook saleBook;

    public static void loadToShipController(int posId, @NotNull SaleBook saleBook) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/functions/" +
                        "ToShipController.fxml"));

        Stage stage = createStyledStage(new Scene(loader.load()));
        stage.setTitle("to ship");
        stage.setMinWidth(350D);
        stage.setMinHeight(200D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        ToShipController controller = loader.getController();
        controller.initialize(posId, saleBook);
    }

    public void initialize(int posId, SaleBook saleBook) {
        this.posId = posId;
        this.saleBook = saleBook;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxUtils.addItems(this.shippingCompaniesChcBx, ShippingCompany.class);
        this.costTxtFld.setText("0");
        LabelUtils.setCurrencies(this.costCurrencyLbl);

        this.costTxtFld.textProperty().addListener((observableValue, s, t1) ->
                this.applyBtn.setDisable(!isValidNumber(t1)));
    }

    @FXML
    private void handleCancel(){
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleApply() {
        this.saleBook.shipped(this.posId, this.shippingCompaniesChcBx.getValue(), this.trackingNumberTxtFld.getText(),
                BigDecimalUtils.parse(this.costTxtFld.getText()));
        this.handleCancel();
    }
}
