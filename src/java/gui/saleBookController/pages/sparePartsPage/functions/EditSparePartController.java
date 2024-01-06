package gui.saleBookController.pages.sparePartsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.util.ChoiceBoxUtils;
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
import logic.SparePart;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;
import static gui.util.TextFieldUtils.isPositive;

public class EditSparePartController extends FunctionDialog<Boolean> implements Initializable {

    @FXML
    public TextField nameTxtFld;
    @FXML
    public TextField unitTxtFld;
    @FXML
    public Label amountUnitLbl;
    @FXML
    public ChoiceBox<Condition> conditionChcBx;
    @FXML
    public TextField quantityTxtFld;
    @FXML
    private ChoiceBox<String> categoryChcBx;
    @FXML
    public Button btnApply;
    @FXML
    public Button btnCancel;
    @FXML
    private SparePart sparePart;

    public static @NotNull EditSparePartController createEditSparePartController(
            @NotNull SparePart sparePart, Set<String> models) throws IOException {
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
        editSparePartController.setSparePart(sparePart, models);
        stage.showAndWait();
        return editSparePartController;
    }

    /**
     * @param sparePart
     * @param categories
     */
    private void setSparePart(SparePart sparePart, Set<String> categories) {
        this.sparePart = sparePart;

        this.nameTxtFld.setText(sparePart.getName());
        this.conditionChcBx.setValue(sparePart.getCondition());
        this.unitTxtFld.setText(sparePart.getUnit());
        this.quantityTxtFld.setText(String.valueOf(sparePart.getQuantity()));
        ChoiceBoxUtils.addItems(this.categoryChcBx, categories);
        this.categoryChcBx.setValue(sparePart.getCategory());
    }

    /**
     * Creates and adds a changeListener to the controller items
     * to regular the accessibility of the apply button
     */
    private void initializeListener() {
        this.unitTxtFld.textProperty().addListener((observableValue, s, t1) ->
                this.amountUnitLbl.setText(t1));

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
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxUtils.addItems(this.conditionChcBx, Condition.class);
        this.initializeListener();
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
     * Handles the "Apply" Button and hands over
     * the (new) {@link }
     */
    @FXML
    private void handleApply() {
        this.sparePart.setName(this.nameTxtFld.getText());
        this.sparePart.setCondition(this.conditionChcBx.getValue());
        this.sparePart.setUnit(this.unitTxtFld.getText());
        this.sparePart.setQuantity(Integer.parseInt(this.quantityTxtFld.getText()));
        this.sparePart.setCategory(this.categoryChcBx.getValue());
        this.result = true;
        this.handleCancel();
    }
}
