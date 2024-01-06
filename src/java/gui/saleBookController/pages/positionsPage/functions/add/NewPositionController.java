package gui.saleBookController.pages.positionsPage.functions.add;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.util.ChoiceBoxUtils;
import gui.util.LabelUtils;
import gui.util.TextFieldUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import logic.products.Item;
import logic.products.position.Position;
import logic.products.position.ShippingCompany;
import logic.products.position.State;
import logic.utils.BigDecimalUtils;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 */
public class NewPositionController extends FunctionDialog<Position> implements Initializable {
    /**
     * The base pane of this controller
     */
    @FXML
    private BorderPane borderPane;
    /**
     * DatePicker to pick the order date of the position
     */
    @FXML
    private DatePicker orderDatePicker;
    /**
     * ChoiceBox to choose the state of the position
     */
    @FXML
    public ChoiceBox<State> stateChcBx;
    /**
     * Label to display the purchasing price of the position
     */
    @FXML
    private Label purchasingPriceCurrencyLbl;
    /**
     * Label to display the cost of the position
     */
    @FXML
    private Label costCurrencyLbl;
    /**
     * TextField to display the selling price of the position
     */
    @FXML
    private TextField sellingPriceTxtFld;
    /**
     * Label to display the selling price of the position
     */
    @FXML
    private Label sellingPriceCurrencyLbl;
    /**
     * DatePicker to pick the selling date of the position
     */
    @FXML
    private DatePicker sellingDatePicker;
    /**
     * Label to display the id of the position
     */
    @FXML
    private Label idLbl;
    /**
     * TextField for the cost of the position (excluding the purchasing price)
     */
    @FXML
    private TextField costTxtFld;
    /**
     * TextField for the category of the position
     */
    @FXML
    private TextField categoryTxtFld;
    /**
     * TextField for the purchasing price of the position
     */
    @FXML
    private TextField purchasingPriceTxtFld;
    /**
     * DatePicker for the date that the position has arrived
     */
    @FXML
    private DatePicker arrivalDatePicker;
    /**
     * TextField for the tracking number of the position if it is shipped
     */
    @FXML
    private TextField trackingNumberTxtFld;
    /**
     * ChoiceBox to choose the shipping company of the position if it is shipped
     */
    @FXML
    private ChoiceBox<ShippingCompany> shippingCompanyChcBx;
    /**
     * Button to get the next step
     */
    @FXML
    private Button nextBtn;
    /**
     * The id of the position
     */
    private int id;
    /**
     * The MasterController, needed to notify that the next step should be shown
     */
    private MasterController masterController;

    /**
     * Creates and loads a new AddPositionController
     *
     * @param masterController needed to notify that the next step should be shown
     * @param id the id of the position
     * @param categories the possible categories that the position could have
     * @return the new AddPositionController
     * @throws IOException If the matching fxml could not be loaded
     */
    public static @NotNull NewPositionController createAddPositionController(@NotNull MasterController masterController,
                                                                             int id,
                                                                             @NotNull Set<String> categories) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/" +
                        "functions/add/NewPositionController.fxml"));

        loader.load();
        NewPositionController newPositionController = loader.getController();
        newPositionController.initialize(masterController, id, categories);
        return newPositionController;
    }

    /**
     * Returns the base pane of this controller
     *
     * @return the base pane
     */
    public Pane getPane() {
        return this.borderPane;
    }

    /**
     * Initializes this AddPositionController with the specified parameters
     *
     * @param masterController needed to notify that the next step should be shown
     * @param id the id of the position
     * @param categories the possible categories that the position could have
     */
    private void initialize(MasterController masterController, int id, Set<String> categories) {
        this.masterController = masterController;
        this.id = id;
        TextFields.bindAutoCompletion(this.categoryTxtFld, categories);
        ChoiceBoxUtils.addItems(this.stateChcBx, State.class);
        ChoiceBoxUtils.addItems(this.shippingCompanyChcBx, ShippingCompany.class);

        this.idLbl.setText(String.valueOf(id));
        this.orderDatePicker.setValue(LocalDate.now());
        this.initializeApplyListener();
    }

    /**
     * Creates and adds a changeListener to the masterController
     * items of the investment to regular the accessibility of the apply button
     */
    //TODO 03.01.2024 JavaDoc
    private void initializeApplyListener() {
        BooleanBinding inputsFull = new BooleanBinding() {
            {
                this.bind(NewPositionController.this.categoryTxtFld.textProperty(), NewPositionController.this.orderDatePicker.valueProperty(),
                        NewPositionController.this.stateChcBx.valueProperty(), NewPositionController.this.purchasingPriceTxtFld.textProperty(),
                        NewPositionController.this.arrivalDatePicker.valueProperty(), NewPositionController.this.sellingDatePicker.valueProperty(),
                        NewPositionController.this.sellingPriceTxtFld.textProperty());
            }

            @Override
            protected boolean computeValue() {
                State state = NewPositionController.this.stateChcBx.getValue();
                return NewPositionController.this.categoryTxtFld.getText().isEmpty() || NewPositionController.this.orderDatePicker.getValue() == null ||
                        !TextFieldUtils.isValidNumber(NewPositionController.this.purchasingPriceTxtFld) ||
                        (state.compareTo(State.RECEIVED) < 0 ^ NewPositionController.this.arrivalDatePicker.getValue() == null) ||
                        (state.compareTo(State.SOLD) >= 0 && (NewPositionController.this.sellingDatePicker.getValue() == null ^ !TextFieldUtils.isValidNumber(NewPositionController.this.sellingPriceTxtFld)));
            }
        };
        this.nextBtn.disableProperty().bind(inputsFull);

        this.stateChcBx.valueProperty().addListener((observableValue, state, t1) -> {
            this.costTxtFld.setDisable(t1.compareTo(State.RECEIVED) < 0);
            this.arrivalDatePicker.setDisable(t1.compareTo(State.RECEIVED) < 0);
            this.arrivalDatePicker.setValue(t1.compareTo(State.RECEIVED) < 0 ? null : this.orderDatePicker.getValue());
            this.sellingDatePicker.setDisable(t1.compareTo(State.SOLD) < 0);
            this.sellingPriceTxtFld.setDisable(t1.compareTo(State.SOLD) < 0);
            this.sellingDatePicker.setValue(t1.compareTo(State.RECEIVED) < 0 ? null : this.arrivalDatePicker.getValue());
            this.shippingCompanyChcBx.setDisable(t1.compareTo(State.SHIPPED) < 0);
            this.trackingNumberTxtFld.setDisable(t1.compareTo(State.SHIPPED) < 0);
        });

        this.orderDatePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (t1 != null && this.stateChcBx.getValue().compareTo(State.RECEIVED) >= 0) {
                this.arrivalDatePicker.setValue(t1);
                this.arrivalDatePicker.setDayCellFactory(d -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(t1)) {
                            this.setDisable(true);
                            // used a different coloring to see which are disabled.
                            this.setStyle("-fx-background-color: #7e7e7e;");
                        }
                    }
                });
            }
        });

        this.arrivalDatePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (t1 != null && this.stateChcBx.getValue().compareTo(State.SOLD) >= 0) {
                this.sellingDatePicker.setValue(t1);
                this.sellingDatePicker.setDayCellFactory(d -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(t1)) {
                            this.setDisable(true);
                            // used a different coloring to see which are disabled.
                            this.setStyle("-fx-background-color: #7e7e7e;");
                        }
                    }
                });
            }
        });
    }

    /**
     * Initializes the fields of this controller.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LabelUtils.setCurrencies(this.purchasingPriceCurrencyLbl, this.costCurrencyLbl,
                this.sellingPriceCurrencyLbl);

        this.costTxtFld.setText("0");
        this.costTxtFld.setDisable(true);
        this.arrivalDatePicker.setDisable(true);
        this.sellingDatePicker.setDisable(true);
        this.sellingDatePicker.setDisable(true);
        this.sellingPriceTxtFld.setDisable(true);
        this.shippingCompanyChcBx.setDisable(true);
        this.trackingNumberTxtFld.setDisable(true);
    }

    /**
     * Handles the "Apply" Button and sets the {@link #result} to the new created position from the inputs
     */
    @FXML
    private void handleNext() {
        this.result = new Position(this.id, this.categoryTxtFld.getText(), this.orderDatePicker.getValue(),
                BigDecimalUtils.parse(this.purchasingPriceTxtFld.getText()),
                BigDecimalUtils.parse(this.costTxtFld.getText()));
        State state = this.stateChcBx.getValue();
        if (state != State.ORDERED) {
            switch (state) {
                case DELIVERED:
                case SHIPPED:
                    this.result.send(this.shippingCompanyChcBx.getValue(),
                            this.trackingNumberTxtFld.getText(), BigDecimal.ZERO);
                case SOLD:
                    this.result.sale(this.sellingDatePicker.getValue(), BigDecimalUtils.parse(this.sellingPriceTxtFld.getText()));
                case REPAIRED:
                case RECEIVED:
                    this.result.setReceived(this.arrivalDatePicker.getValue());
            }
            this.result.setState(state);
        }
        this.masterController.showNewItemsController();
    }
}
