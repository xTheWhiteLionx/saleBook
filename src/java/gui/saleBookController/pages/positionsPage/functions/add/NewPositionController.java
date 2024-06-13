package gui.saleBookController.pages.positionsPage.functions.add;

import gui.ApplicationMain;
import costumeClasses.FXClasses.BindedBoundedDateCell;
import gui.FXutils.TextInputControlUtils;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.ChoiceBoxUtils;
import gui.FXutils.LabelUtils;
import gui.FXutils.TextFieldUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import logic.products.position.Position;
import logic.products.position.ShippingCompany;
import logic.products.position.State;
import utils.BigDecimalUtils;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * This class represents a Controller to create a new position
 *
 * @see gui.saleBookController.pages.FunctionDialog
 * @author xthe_white_lionx
 */
public class NewPositionController extends FunctionDialog<Position> implements Initializable {
    /**
     * The base pane of this controller
     */
    @FXML
    private BorderPane basePane;
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
     * ComboBox for the category of the position
     */
    @FXML
    private ComboBox<String> categoryComboBox;
    /**
     * TextField for the purchasing price of the position
     */
    @FXML
    private TextField purchasingPriceTxtFld;
    /**
     * TextField for the cost of the position (excluding the purchasing price)
     */
    @FXML
    private TextField costTxtFld;
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
        newPositionController.masterController = masterController;
        newPositionController.initialize(id, categories);
        return newPositionController;
    }

    /**
     * Returns the base pane of this controller
     *
     * @return the base pane of this controller
     */
    public Pane getBasePane() {
        return this.basePane;
    }

    /**
     * Initializes the fields of this controller.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextInputControlUtils.installTouch(this.categoryComboBox.getEditor());
        TextInputControlUtils.installTouch(this.purchasingPriceTxtFld);
        TextInputControlUtils.installTouch(this.costTxtFld);
        TextInputControlUtils.installTouch(this.sellingPriceTxtFld);
        TextInputControlUtils.installTouch(this.trackingNumberTxtFld);
        TextInputControlUtils.installTouch(this.trackingNumberTxtFld);

        this.costTxtFld.setText("0");
        LabelUtils.setCurrencies(this.purchasingPriceCurrencyLbl, this.costCurrencyLbl,
                this.sellingPriceCurrencyLbl);
        ChoiceBoxUtils.addItems(this.stateChcBx, State.class);
        ChoiceBoxUtils.addItems(this.shippingCompanyChcBx, ShippingCompany.class);
        this.orderDatePicker.setValue(LocalDate.now());
        this.sellingDatePicker.setDayCellFactory(cf -> new BindedBoundedDateCell(this.arrivalDatePicker.valueProperty(), null));
        this.arrivalDatePicker.setDayCellFactory(cf -> new BindedBoundedDateCell(this.orderDatePicker.valueProperty(),
                this.sellingDatePicker.valueProperty()));
        this.initializeBooleanBinding();
        this.initializeListener();

        this.costTxtFld.setDisable(true);
        this.arrivalDatePicker.setDisable(true);
        this.sellingDatePicker.setDisable(true);
        this.sellingDatePicker.setDisable(true);
        this.sellingPriceTxtFld.setDisable(true);
        this.shippingCompanyChcBx.setDisable(true);
        this.trackingNumberTxtFld.setDisable(true);
    }

    /**
     * Handles the "Next" Button and sets the {@link #result} to the new created position from the inputs
     */
    @FXML
    private void handleNext() {
        this.result = new Position(this.id, this.categoryComboBox.getValue(),
                this.orderDatePicker.getValue(),
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

    /**
     * Creates and adds the needed changeListener to this controller to regular
     * the accessibility of the controller and their values
     */
    private void initializeListener() {
        this.stateChcBx.valueProperty().addListener((observableValue, state, t1) -> {
            this.costTxtFld.setDisable(t1.compareTo(State.RECEIVED) < 0);
            this.arrivalDatePicker.setDisable(t1.compareTo(State.RECEIVED) < 0);
            this.arrivalDatePicker.setValue(t1.compareTo(State.RECEIVED) < 0 ? null : this.orderDatePicker.getValue());
            this.sellingDatePicker.setDisable(t1.compareTo(State.SOLD) < 0);
            this.sellingPriceTxtFld.setDisable(t1.compareTo(State.SOLD) < 0);
            this.sellingDatePicker.setValue(t1.compareTo(State.SOLD) < 0 ? null : this.arrivalDatePicker.getValue());
            this.shippingCompanyChcBx.setDisable(t1.compareTo(State.SHIPPED) < 0);
            this.trackingNumberTxtFld.setDisable(t1.compareTo(State.SHIPPED) < 0);
        });

        this.orderDatePicker.valueProperty().addListener((observableValue, oldDate, newDate) -> {
            if (newDate != null && this.stateChcBx.getValue().compareTo(State.RECEIVED) >= 0) {
                this.arrivalDatePicker.setValue(newDate);
                this.sellingDatePicker.setValue(newDate);
            }
        });

        this.arrivalDatePicker.valueProperty().addListener((observableValue, oldDate, newDate) -> {
            if (newDate != null && this.stateChcBx.getValue().compareTo(State.SOLD) >= 0) {
                this.sellingDatePicker.setValue(newDate);
            }
        });
    }

    /**
     * Creates and binds the needed booleanBinding to the apply button to regular the accessibility
     */
    private void initializeBooleanBinding() {
        BooleanBinding inputsFull = new BooleanBinding() {
            {
                this.bind(NewPositionController.this.categoryComboBox.editorProperty(),
                        NewPositionController.this.orderDatePicker.valueProperty(),
                        NewPositionController.this.stateChcBx.valueProperty(),
                        NewPositionController.this.purchasingPriceTxtFld.textProperty(),
                        NewPositionController.this.arrivalDatePicker.valueProperty(),
                        NewPositionController.this.sellingDatePicker.valueProperty(),
                        NewPositionController.this.sellingPriceTxtFld.textProperty());
            }

            @Override
            protected boolean computeValue() {
                State state = NewPositionController.this.stateChcBx.getValue();
                return NewPositionController.this.categoryComboBox.getEditor().getText().isEmpty() ||
                        NewPositionController.this.orderDatePicker.getValue() == null ||
                        !TextFieldUtils.isValidNumber(NewPositionController.this.purchasingPriceTxtFld) ||
                        (state.compareTo(State.RECEIVED) < 0 ^ NewPositionController.this.arrivalDatePicker.getValue() == null) ||
                        (state.compareTo(State.SOLD) >= 0 &&
                                (NewPositionController.this.sellingDatePicker.getValue() == null ^ !TextFieldUtils.isValidNumber(NewPositionController.this.sellingPriceTxtFld)));
            }
        };
        this.nextBtn.disableProperty().bind(inputsFull);
    }

    /**
     * Initializes this AddPositionController with the specified parameters
     *
     * @param id the id of the position
     * @param categories the possible categories that the position could have
     */
    private void initialize(int id, Set<String> categories) {
        this.id = id;
        this.idLbl.setText(String.valueOf(id));
        this.categoryComboBox.getItems().setAll(categories);
        TextFields.bindAutoCompletion(this.categoryComboBox.getEditor(), categories);
    }
}
