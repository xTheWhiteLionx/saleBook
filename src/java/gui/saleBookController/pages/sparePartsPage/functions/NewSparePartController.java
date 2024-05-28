package gui.saleBookController.pages.sparePartsPage.functions;

import gui.ApplicationMain;
import gui.FXutils.SpinnerUtils;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.ChoiceBoxUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Condition;
import logic.sparePart.SparePart;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.FXutils.StageUtils.createStyledStage;

/**
 * NewSparePartController is a controller to create a new sparePart
 *
 * @see gui.saleBookController.pages.FunctionDialog
 * @author xthe_white_lionx
 */
public class NewSparePartController extends FunctionDialog<SparePart> implements Initializable {

    /**
     * TextField to set the name
     */
    @FXML
    public TextField nameTxtFld;

    /**
     * ChoiceBox to choose the condition
     */
    @FXML
    public ChoiceBox<Condition> conditionChcBx;

    /**
     * TextField to set the unit
     */
    @FXML
    public TextField unitTxtFld;


    /**
     * CheckBox of the minimum stock of the
     */
    @FXML
    public CheckBox minimumStockChckBx;

    /**
     *
     */
    @FXML
    public Spinner<Integer> minimumStockSpinner;

    /**
     * Label to display the quantity unit
     */
    @FXML
    public Label minStockUnitLbl;

    /**
     * ChoiceBox to choose the category
     */
    @FXML
    private ChoiceBox<String> categoryChcBx;

    /**
     * Button to apply the new created sparePart
     */
    @FXML
    private Button btnApply;

    /**
     * Button to cancel the creation
     */
    @FXML
    private Button btnCancel;


    /**
     * Creates and loads a new NewSparePartController
     *
     * @param nameOfSpareParts name of the already existing spareParts for autocompletion
     * @param units already existing units for autocompletion
     * @param categories already existing categories for autocompletion
     * @return the new created NewSparePartController
     * @throws IOException if the fxml file could not be loaded
     */
    public static @NotNull NewSparePartController createSparePartController(
            @NotNull Set<String> nameOfSpareParts, @NotNull Set<String> units,
            @NotNull Set<String> categories) throws IOException {
        FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource(
                "saleBookController/pages/sparePartsPage/functions/" +
                        "NewSparePartController.fxml"));

        final Stage newStage = createStyledStage(new Scene(loader.load()));
        newStage.setTitle("new spare part");
        newStage.setMinWidth(400D);
        newStage.setMinHeight(300D);
        newStage.initModality(Modality.APPLICATION_MODAL);
        NewSparePartController controller = loader.getController();
        controller.initialize(nameOfSpareParts, units, categories);
        newStage.showAndWait();
        return controller;
    }

    /**
     * Initializes the controls for autocompletion and sets the chose able categories
     *
     * @param nameOfSpareParts name of the already existing spareParts for autocompletion
     * @param units already existing units for autocompletion
     * @param categories already existing categories for autocompletion
     */
    private void initialize(@NotNull Set<String> nameOfSpareParts, @NotNull Set<String> units,
                            @NotNull Set<String> categories){
        TextFields.bindAutoCompletion(this.nameTxtFld, nameOfSpareParts);
        TextFields.bindAutoCompletion(this.unitTxtFld, units);
        ChoiceBoxUtils.addItems(this.categoryChcBx, categories);
    }

    /**
     * Creates and adds a changeListener to the controller items
     * to regular the accessibility of the apply button
     */
    private void initializeBooleanBinding() {
        BooleanBinding inputsValid = new BooleanBinding() {
            {
                this.bind(NewSparePartController.this.nameTxtFld.textProperty(),
                        NewSparePartController.this.unitTxtFld.textProperty(),
                        NewSparePartController.this.minimumStockChckBx.selectedProperty(),
                        NewSparePartController.this.minimumStockSpinner.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return !NewSparePartController.this.unitTxtFld.getText().isEmpty()
                        && !NewSparePartController.this.nameTxtFld.getText().isEmpty();
            }
        };
        this.btnApply.disableProperty().bind(inputsValid.not());
    }

    /**
     * Initializes this controller.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxUtils.addItems(this.conditionChcBx, Condition.class);
        this.minimumStockSpinner.setValueFactory(SpinnerUtils.createValueFactory(1));
        this.minimumStockChckBx.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                this.minimumStockSpinner.setValueFactory(SpinnerUtils.createValueFactory(0));
            } else {
                this.minimumStockSpinner.setValueFactory(SpinnerUtils.createValueFactory(1));
            }
            this.minimumStockSpinner.setDisable(!newValue);
        });
        this.unitTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
            this.minStockUnitLbl.setText(newText);
        });
        this.initializeBooleanBinding();
    }

    /**
     * Closes the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.btnCancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the "Apply" Button, sets the result to the new created sparePart and
     * closes the window
     */
    @FXML
    private void handleApply() {
        this.result = new SparePart(this.nameTxtFld.getText(), this.conditionChcBx.getValue(),
                this.unitTxtFld.getText(), this.categoryChcBx.getValue(), this.minimumStockSpinner.getValue());
        this.handleCancel();
    }
}
