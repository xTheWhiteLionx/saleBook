package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.FXutils.ChoiceBoxUtils;
import gui.FXutils.LabelUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.products.position.ShippingCompany;
import logic.saleBook.SaleBook;
import utils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.FXutils.StageUtils.createStyledStage;
import static utils.StringUtils.isValidNumber;

/**
 * Controller to set the state of a position to ship
 *
 * @author xthe_white_lionx
 */
public class ToShipController implements Initializable {
    /**
     * ChoiceBox to choose the shipping company for the shipping
     */
    @FXML
    private ChoiceBox<ShippingCompany> shippingCompaniesChcBx;

    /**
     * TextField to set the shipping cost
     */
    @FXML
    private TextField costTxtFld;

    /**
     * Label to display the currency of the cost
     */
    @FXML
    private Label costCurrencyLbl;

    /**
     * TextField to set the tracking number of the shipping
     */
    @FXML
    private TextField trackingNumberTxtFld;

    /**
     * Button to upload the invoice for the shipping
     */
    @FXML
    private Button invoiceUploadButton;

    /**
     * Target for the drag of the invoice
     */
    @FXML
    private HBox invoiceDragTarget;

    /**
     * Label in the invoice drag zone
     */
    @FXML
    private Label invoiceDragLabel;

    /**
     * Button to upload the shipping mark for the shipping
     */
    @FXML
    private Button shippingMarkUploadButton;

    /**
     * Target for the drag of the shipping mark
     */
    @FXML
    private HBox shippingMarkDragTarget;

    /**
     * Label in the shipping mark drag zone
     */
    @FXML
    private Label shippingMarkDragLabel;

    /**
     * Button to apply the input
     */
    @FXML
    private Button applyBtn;

    /**
     * ID of the position, which should be shipped
     */
    private int posId;

    /**
     * SaleBook
     */
    private SaleBook saleBook;

    /**
     * Invoice file
     */
    private File invoice;

    /**
     * Shipping mark file
     */
    private File shippingMark;

    /**
     * Loads a ToShipController with
     *
     * @param posId ID of the position which should be shipped
     * @param saleBook saleBook to overhand the input
     * @throws IOException if the fxml file cannot be loaded
     */
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
        ToShipController controller = loader.getController();
        controller.posId = posId;
        controller.saleBook = saleBook;
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxUtils.addItems(this.shippingCompaniesChcBx, ShippingCompany.class);
        LabelUtils.setCurrencies(this.costCurrencyLbl);

        this.costTxtFld.textProperty().addListener((observableValue, s, t1) ->
                this.applyBtn.setDisable(!isValidNumber(t1)));

        this.invoiceDragTarget.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != this.invoiceDragTarget
            && dragEvent.getDragboard().hasFiles()){
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
            dragEvent.consume();
        });

        this.invoiceDragTarget.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = db.hasFiles();
            if (success){
                this.invoice = db.getFiles().get(0);
                this.invoiceDragLabel.setText(this.invoice.getName());
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });

        this.shippingMarkDragTarget.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != this.shippingMarkDragTarget
            && dragEvent.getDragboard().hasFiles()){
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
            dragEvent.consume();
        });

        this.shippingMarkDragTarget.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = db.hasFiles();
            if (success){
                this.shippingMark = db.getFiles().get(0);
                this.shippingMarkDragLabel.setText(this.shippingMark.getName());
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }

    /**
     * Saves the chosen file from the showOpenDialog as invoice
     */
    @FXML
    private void uploadInvoice(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("positions"));
        this.invoice =
                fileChooser.showOpenDialog(this.invoiceUploadButton.getScene().getWindow());
        this.invoiceDragLabel.setText(this.invoice.getName());
    }

    /**
     * Saves the chosen file from the showOpenDialog as shipping mark
     */
    @FXML
    private void uploadShippingMark(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("positions"));
        this.shippingMark =
                fileChooser.showOpenDialog(this.shippingMarkUploadButton.getScene().getWindow());
        this.shippingMarkDragLabel.setText(this.shippingMark.getName());
    }

    /**
     * Closes the window
     */
    @FXML
    private void handleClose(){
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Applies the input to the saleBook and closes the window
     */
    @FXML
    private void handleApply() {
        this.saleBook.getPositionsManager().shipped(this.posId, this.shippingCompaniesChcBx.getValue(),
                this.trackingNumberTxtFld.getText(),
                BigDecimalUtils.parse(this.costTxtFld.getText()));

        String dirName = "positions/" + this.posId;
        if (this.invoice != null) {
            String extension = FileUtils.getExtension(this.invoice);
            this.invoice.renameTo(new File(dirName, "invoice P" + this.posId + "." + extension));
        }
        if (this.shippingMark != null) {
            String extension = FileUtils.getExtension(this.shippingMark);
            this.shippingMark.renameTo(new File(dirName,
                    "shipping mark P" + this.posId + "." + extension));
        }

        this.handleClose();
    }
}
