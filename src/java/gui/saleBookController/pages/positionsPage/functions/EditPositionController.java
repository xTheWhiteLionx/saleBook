package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.util.LabelUtils;
import gui.util.StageUtils;
import gui.util.TextFieldUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.products.position.ShippingCompany;
import logic.products.position.State;
import logic.products.Item;
import logic.products.position.Position;
import logic.saleBook.SaleBook;
import logic.utils.BigDecimalUtils;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.util.ChoiceBoxUtils.addItems;

public class EditPositionController extends FunctionDialog<Boolean> implements Initializable {
    @FXML
    private Label idLbl;
    @FXML
    private TextField categoryTxtFld;
    /**
     *
     */
    @FXML
    private DatePicker orderDatePicker;
    @FXML
    private TextField purchasingPriceTxtFld;
    /**
     *
     */
    @FXML
    private Label purchasingPriceCurrencyLbl;
    /**
     *
     */
    @FXML
    public ChoiceBox<State> stateChcBx;
    @FXML
    private TextField costTxtFld;
    /**
     *
     */
    @FXML
    private Label costCurrencyLbl;
    @FXML
    private DatePicker receiveDatePicker;
    /**
     *
     */
    @FXML
    private TextField sellingPriceTxtFld;
    /**
     *
     */
    @FXML
    private DatePicker sellingDatePicker;
    /**
     *
     */
    @FXML
    private Label sellingPriceCurrencyLbl;
    @FXML
    private ChoiceBox<ShippingCompany> shippingCompanyChcBx;
    @FXML
    private TextField trackingNumberTxtFld;
    /**
     *
     */
    @FXML
    private Button applyBtn;

    private final ValidationSupport validationSupport = new ValidationSupport();
    private Position position;

    /**
     * @return
     */
    public static EditPositionController createEditPositionController(@NotNull Position position,
                                                                      @NotNull Set<String> models) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/" +
                        "functions/EditPositionController.fxml"));
        Stage stage = StageUtils.createStyledStage(new Scene(loader.load()));
        EditPositionController editPositionController = loader.getController();
        editPositionController.initialize(position, models);
        stage.setTitle("edit position");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return editPositionController;
    }

    /**
     *
     * @param position
     * @param models
     */
    private void initialize(Position position, Set<String> models) {
        this.position = position;

        this.idLbl.setText(String.valueOf(position.getId()));
        this.categoryTxtFld.setText(position.getCategory());
        TextFields.bindAutoCompletion(this.categoryTxtFld, models);
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
        this.initializeApplyListener();
    }

    /**
     * Creates and adds a changeListener to the masterController
     * items of the investment to regular the accessibility of the apply button
     */
    private void initializeApplyListener() {
        // TODO: 22.07.2023
        BooleanBinding inputsFull = new BooleanBinding() {
            {
                this.bind(EditPositionController.this.categoryTxtFld.textProperty(), EditPositionController.this.orderDatePicker.valueProperty(),
                        EditPositionController.this.stateChcBx.valueProperty(), EditPositionController.this.purchasingPriceTxtFld.textProperty(),
                        EditPositionController.this.receiveDatePicker.valueProperty(), EditPositionController.this.sellingDatePicker.valueProperty(),
                        EditPositionController.this.sellingPriceTxtFld.textProperty());
            }

            @Override
            protected boolean computeValue() {
                State state = EditPositionController.this.stateChcBx.getValue();
                return EditPositionController.this.categoryTxtFld.getText().isEmpty() || EditPositionController.this.orderDatePicker.getValue() == null ||
                        !TextFieldUtils.isValidNumber(EditPositionController.this.purchasingPriceTxtFld) ||
                        (state.compareTo(State.RECEIVED) < 0 ^ EditPositionController.this.receiveDatePicker.getValue() == null) ||
                        (state.compareTo(State.SOLD) >= 0 && (EditPositionController.this.sellingDatePicker.getValue() == null ^ !TextFieldUtils.isValidNumber(EditPositionController.this.sellingPriceTxtFld)));
            }
        };
        this.applyBtn.disableProperty().bind(inputsFull);

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

        this.orderDatePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (t1 != null && this.stateChcBx.getValue().compareTo(State.RECEIVED) >= 0) {
                this.receiveDatePicker.setValue(t1);
                this.receiveDatePicker.setDayCellFactory(d -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(t1)) {
                            this.setDisable(true);
                            this.setStyle("-fx-background-color: #7e7e7e;"); // I used a different coloring to see which are disabled.
                        }
                    }
                });
            }
        });

        this.receiveDatePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (t1 != null && this.stateChcBx.getValue().compareTo(State.SOLD) >= 0) {
                this.sellingDatePicker.setValue(t1);
                this.sellingDatePicker.setDayCellFactory(d -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(t1)) {
                            this.setDisable(true);
                            this.setStyle("-fx-background-color: #7e7e7e;"); // I used a different coloring to see which are disabled.
                        }
                    }
                });
            }
        });
    }

    /**
     * Initializes the application.
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
    }

    /**
     * Handles the "Apply" Button and hands over
     * the (new) {@link Item} attributes
     */
    @FXML
    private void handleApply() {
        this.position.setCategory(this.categoryTxtFld.getText());
        this.position.setOrderDate(this.orderDatePicker.getValue());
        this.position.setPurchasingPrice(BigDecimalUtils.parse(this.purchasingPriceTxtFld.getText()));
        this.position.setCost(BigDecimalUtils.parse(this.costTxtFld.getText()));
        //this.position.setErrorDescription(this.errorDescriptionTxtArea.getText());

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
