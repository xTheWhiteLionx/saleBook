package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.BoundedDateCell;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.LabelUtils;
import gui.FXutils.StageUtils;
import gui.FXutils.TextFieldUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.products.position.ShippingCompany;
import logic.products.position.State;
import logic.products.item.Item;
import logic.products.position.Position;
import utils.BigDecimalUtils;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.FXutils.ChoiceBoxUtils.addItems;

/**
 * This controller is used to edit the specified position
 *
 * @author xthe_white_lionx
 * @see gui.saleBookController.pages.FunctionDialog
 */
public class EditPositionController extends FunctionDialog<Boolean> implements Initializable {
    /**
     * Label to display the id of the position
     */
    @FXML
    private Label idLbl;

    /**
     * ComboBox for the category of the position
     */
    @FXML
    private ComboBox<String> categoryComboBox;

    /**
     * DatePicker to pick a new date for the order date of the position
     */
    @FXML
    private DatePicker orderDatePicker;

    /**
     * TextField to
     */
    @FXML
    private TextField purchasingPriceTxtFld;

    /**
     * Label to display the purchasing price currency
     */
    @FXML
    private Label purchasingPriceCurrencyLbl;

    /**
     * ChoiceBox to choose the new state from
     */
    @FXML
    public ChoiceBox<State> stateChcBx;

    /**
     * TextField for editing the cost of the position (excluding the purchasing price)
     */
    @FXML
    private TextField costTxtFld;

    /**
     * Label to display currency of the cost
     */
    @FXML
    private Label costCurrencyLbl;

    /**
     * DatePicker to pick the new receive date for the position
     */
    @FXML
    private DatePicker receiveDatePicker;

    /**
     * TextField to edit the selling price of the position
     */
    @FXML
    private TextField sellingPriceTxtFld;

    /**
     * DatePicker to pick the new selling date for the position
     */
    @FXML
    private DatePicker sellingDatePicker;

    /**
     * Label to display the currency of the selling price
     */
    @FXML
    private Label sellingPriceCurrencyLbl;

    /**
     * ChoiceBox to choose the new shipping company of the position
     */
    @FXML
    private ChoiceBox<ShippingCompany> shippingCompanyChcBx;

    /**
     * TextField to edit the tracking number of the position
     */
    @FXML
    private TextField trackingNumberTxtFld;

    /**
     * Button to apply the editing
     */
    @FXML
    private Button applyBtn;

    /**
     * The position which should be edit
     */
    private Position position;

    /**
     * Creates and loads a new EditPositionController
     *
     * @return the new created EditPositionController
     * @throws IOException if the fxml can not be loaded
     */
    public static EditPositionController createEditPositionController(@NotNull Position position, @NotNull Set<String> categories) throws IOException {
        FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource("saleBookController/pages/positionsPage/" + "functions/EditPositionController.fxml"));
        Stage stage = StageUtils.createStyledStage(new Scene(loader.load()));
        EditPositionController editPositionController = loader.getController();
        editPositionController.initialize(position, categories);
        stage.setTitle("edit position");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return editPositionController;
    }

    /**
     * Initializes the controls of this controller
     *
     * @param position   The position which should be edited
     * @param categories the possible categories that the position could have
     */
    private void initialize(Position position, Set<String> categories) {
        this.position = position;

        this.idLbl.setText(String.valueOf(position.getId()));
        this.categoryComboBox.setValue(position.getCategory());
        this.categoryComboBox.getItems().setAll(categories);
        TextFields.bindAutoCompletion(this.categoryComboBox.getEditor(), categories);
        this.orderDatePicker.setValue(position.getOrderDate());
        this.purchasingPriceTxtFld.setText(String.valueOf(position.getPurchasingPrice()));
        this.costTxtFld.setText(String.valueOf(position.getCost()));

        State state = position.getState();
        if (state != State.ORDERED) {
            switch (state) {
                case DELIVERED:
                case SHIPPED:
                    this.shippingCompanyChcBx.setValue(position.getShippingCompany());
                    this.shippingCompanyChcBx.setDisable(false);
                    this.trackingNumberTxtFld.setText(position.getTrackingNumber());
                    this.trackingNumberTxtFld.setDisable(false);
                case SOLD:
                    this.sellingPriceTxtFld.setText(String.valueOf(position.getSellingPrice()));
                    this.sellingPriceTxtFld.setDisable(false);
                    this.sellingDatePicker.setValue(position.getSellingDate());
                    this.sellingDatePicker.setDisable(false);
                case REPAIRED:
                case RECEIVED:
                    this.receiveDatePicker.setValue(position.getReceivedDate());
                    this.receiveDatePicker.setDisable(false);
            }
            this.stateChcBx.setValue(state);
        }
    }

    /**
     * Creates and adds a changeListener to the masterController
     * items of the investment to regular the accessibility of the apply button
     */
    private void initializeApplyListener() {
        this.stateChcBx.valueProperty().addListener((observableValue, state, t1) -> {
            this.costTxtFld.setDisable(t1.compareTo(State.RECEIVED) < 0);
            this.receiveDatePicker.setDisable(t1.compareTo(State.RECEIVED) < 0);
            this.receiveDatePicker.setValue(t1.compareTo(State.RECEIVED) < 0 ? null : this.orderDatePicker.getValue());
            this.sellingDatePicker.setDisable(t1.compareTo(State.SOLD) < 0);
            this.sellingPriceTxtFld.setDisable(t1.compareTo(State.SOLD) < 0);
            this.sellingDatePicker.setValue(t1.compareTo(State.RECEIVED) < 0 ? null : this.receiveDatePicker.getValue());
            this.shippingCompanyChcBx.setDisable(t1.compareTo(State.SHIPPED) < 0);
            this.trackingNumberTxtFld.setDisable(t1.compareTo(State.SHIPPED) < 0);
        });

        this.orderDatePicker.valueProperty().addListener((observableValue, oldDate, newDate) -> {
            if (newDate != null && this.stateChcBx.getValue().compareTo(State.RECEIVED) >= 0) {
                this.receiveDatePicker.setValue(newDate);
                this.receiveDatePicker.setDayCellFactory(cf -> new BoundedDateCell(newDate, null));
            }
        });

        this.receiveDatePicker.valueProperty().addListener((observableValue, oldDate, newDate) -> {
            if (newDate != null && this.stateChcBx.getValue().compareTo(State.SOLD) >= 0) {
                this.sellingDatePicker.setValue(newDate);
                this.sellingDatePicker.setDayCellFactory(cf -> new BoundedDateCell(newDate, null));
            }
        });
    }

    /**
     * Initializes this controller.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //all the currency labels as array
        LabelUtils.setCurrencies(this.purchasingPriceCurrencyLbl, this.costCurrencyLbl,
                this.sellingPriceCurrencyLbl);

        addItems(this.stateChcBx, State.class);
        addItems(this.shippingCompanyChcBx, ShippingCompany.class);

        this.receiveDatePicker.setDisable(true);
        this.sellingDatePicker.setDisable(true);
        this.sellingDatePicker.setDisable(true);
        this.sellingPriceTxtFld.setDisable(true);
        this.shippingCompanyChcBx.setDisable(true);
        this.trackingNumberTxtFld.setDisable(true);

        this.initializeBooleanBinding();
        this.initializeApplyListener();
    }

    /**
     * Initializes the BooleanBinding and binds the semantic paired fields
     */
    private void initializeBooleanBinding() {
        BooleanBinding inputsFull = new BooleanBinding() {
            {
                this.bind(EditPositionController.this.categoryComboBox.valueProperty(),
                        EditPositionController.this.orderDatePicker.valueProperty(),
                        EditPositionController.this.stateChcBx.valueProperty(),
                        EditPositionController.this.purchasingPriceTxtFld.textProperty(),
                        EditPositionController.this.receiveDatePicker.valueProperty(),
                        EditPositionController.this.sellingDatePicker.valueProperty(),
                        EditPositionController.this.sellingPriceTxtFld.textProperty());
            }

            @Override
            protected boolean computeValue() {
                State state = EditPositionController.this.stateChcBx.getValue();
                String value = EditPositionController.this.categoryComboBox.getValue();
                return value == null || value.isEmpty()
                        || EditPositionController.this.orderDatePicker.getValue() == null ||
                        !TextFieldUtils.isValidNumber(EditPositionController.this.purchasingPriceTxtFld) ||
                        (state.compareTo(State.RECEIVED) < 0 ^ EditPositionController.this.receiveDatePicker.getValue() == null) ||
                        (state.compareTo(State.SOLD) >= 0 && (EditPositionController.this.sellingDatePicker.getValue() == null ^ !TextFieldUtils.isValidNumber(EditPositionController.this.sellingPriceTxtFld)));
            }
        };
        this.applyBtn.disableProperty().bind(inputsFull);
    }

    /**
     * Handles the "Apply" Button and hands over
     * the (new) {@link Item} attributes
     */
    @FXML
    private void handleApply() {
        this.position.setCategory(this.categoryComboBox.getValue());
        this.position.setOrderDate(this.orderDatePicker.getValue());
        this.position.setPurchasingPrice(BigDecimalUtils.parse(this.purchasingPriceTxtFld.getText()));
        this.position.setCost(BigDecimalUtils.parse(this.costTxtFld.getText()));

        State state = this.stateChcBx.getValue();
        if (state != State.ORDERED) {
            switch (state) {
                case DELIVERED:
                case SHIPPED:
                    this.position.setShippingCompany(this.shippingCompanyChcBx.getValue());
                    this.position.setTrackingNumber(this.trackingNumberTxtFld.getText());
                case SOLD:
                    this.position.setSellingPrice(BigDecimalUtils.parse(this.sellingPriceTxtFld.getText()));
                    this.position.setSellingDate(this.sellingDatePicker.getValue());
                case REPAIRED:
                case RECEIVED:
                    this.position.setReceived(this.receiveDatePicker.getValue());
            }
            this.position.setState(this.stateChcBx.getValue());
        }
        this.result = true;
        this.handleCancel();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }
}
