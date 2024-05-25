package gui.saleBookController.pages.sparePartsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.ChoiceBoxUtils;
import javafx.beans.binding.BooleanBinding;
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
import logic.Condition;
import logic.sparePart.SparePart;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.FXutils.StageUtils.createStyledStage;
import static gui.FXutils.TextFieldUtils.isPositive;

/**
 * EditSparePartController is a controller to edit a specific sparePart
 *
 * @see gui.saleBookController.pages.FunctionDialog
 * @author xthe_white_lionx
 */
public class EditSparePartController extends FunctionDialog<Boolean> implements Initializable {

    /**
     * TextField to edit the name
     */
    @FXML
    public TextField nameTxtFld;

    /**
     * TextField to edit the unit
     */
    @FXML
    public TextField unitTxtFld;

    /**
     * Label to display the Unit of the quantity
     */
    @FXML
    public Label quantityUnitLbl;

    /**
     * ChoiceBox to choose the condition of the spare part
     */
    @FXML
    public ChoiceBox<Condition> conditionChcBx;

    /**
     * TextField to edit the quantity
     */
    @FXML
    public TextField quantityTxtFld;

    /**
     * ChoiceBox to choose the category of the spare part
     */
    @FXML
    private ChoiceBox<String> categoryChcBx;

    /**
     * Button to apply the editing
     */
    @FXML
    public Button btnApply;

    /**
     * Button to cancel the editing
     */
    @FXML
    public Button btnCancel;

    /**
     * The sparePart which should be editing
     */
    private SparePart sparePart;

    /**
     * Creates and loads a new EditSparePartController
     *
     * @param sparePart the sparePart which should be editing
     * @param categories the possible categories that can be chosen
     * @return the new created EditSparePartController
     * @throws IOException if the fxml file could not be loaded
     */
    public static @NotNull EditSparePartController createEditSparePartController(
            @NotNull SparePart sparePart, @NotNull Set<String> categories) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/sparePartsPage" +
                        "/functions/EditSparePartController.fxml"));

        Stage stage = createStyledStage(new Scene(loader.load()));
        stage.setTitle("Edit spare part");
        stage.setMinWidth(400D);
        stage.setMinHeight(300D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        EditSparePartController editSparePartController = loader.getController();
        editSparePartController.initialize(sparePart, categories);
        stage.showAndWait();
        return editSparePartController;
    }

    /**
     * Initializes this EditSparePartController.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxUtils.addItems(this.conditionChcBx, Condition.class);
        this.unitTxtFld.textProperty().addListener((observableValue, s, t1) ->
                this.quantityUnitLbl.setText(t1));
        this.initializeBooleanBinding();
    }

    /**
     * Initializes controls of this controller
     *
     * @param sparePart the sparePart which should be editing
     * @param categories the possible categories that can be chosen
     */
    private void initialize(@NotNull SparePart sparePart, @NotNull Set<String> categories) {
        this.sparePart = sparePart;

        this.nameTxtFld.setText(sparePart.getName());
        this.conditionChcBx.setValue(sparePart.getCondition());
        this.unitTxtFld.setText(sparePart.getUnit());
//        this.quantityTxtFld.setText(String.valueOf(sparePart.getQuantity()));
        ChoiceBoxUtils.addItems(this.categoryChcBx, categories);
        this.categoryChcBx.setValue(sparePart.getCategory());
    }

    /**
     * Creates and adds a changeListener to the controller items
     * to regular the accessibility of the apply button
     */
    private void initializeBooleanBinding() {
        BooleanBinding inputsValid = new BooleanBinding() {
            {
                this.bind(EditSparePartController.this.nameTxtFld.textProperty(), EditSparePartController.this.unitTxtFld.textProperty(),
                        EditSparePartController.this.quantityTxtFld.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return !EditSparePartController.this.unitTxtFld.getText().isEmpty() && !EditSparePartController.this.nameTxtFld.getText().isEmpty() &&
                        isPositive(EditSparePartController.this.quantityTxtFld);
            }
        };
        this.btnApply.disableProperty().bind(inputsValid.not());
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
     * Handles the "Apply" Button, resets the fields of the spareParts, sets the result and
     * closes the window
     */
    @FXML
    private void handleApply() {
        this.sparePart.setName(this.nameTxtFld.getText());
        this.sparePart.setCondition(this.conditionChcBx.getValue());
        this.sparePart.setUnit(this.unitTxtFld.getText());
//        this.sparePart.setQuantity(Integer.parseInt(this.quantityTxtFld.getText()));
        this.sparePart.setCategory(this.categoryChcBx.getValue());
        this.result = true;
        this.handleCancel();
    }
}
