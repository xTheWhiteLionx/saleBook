package gui.saleBookController.pages.sparePartsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.util.ChoiceBoxUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Condition;
import logic.SparePart;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;
import static gui.util.TextFieldUtils.isPositive;

public class NewSparePartController extends FunctionDialog<SparePart> implements Initializable {

    @FXML
    public TextField nameTxtFld;
    @FXML
    public ChoiceBox<Condition> conditionChcBx;
    @FXML
    public TextField unitTxtFld;
    @FXML
    private TextField quantityTxtFld;
    @FXML
    public Label amountUnitLbl;
    @FXML
    private ChoiceBox<String> categoryChcBx;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;


    /**
     *
     * @param nameOfSpareParts
     * @param units
     * @return
     * @throws IOException
     */
    public static @NotNull NewSparePartController createSparePartController(@NotNull Set<String> nameOfSpareParts,
                                                                   @NotNull Set<String> units,
                                                                            @NotNull Set<String> models) throws IOException {
        FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource(
                "saleBookController/pages/sparePartsPage/functions/" +
                        "NewSparePartController.fxml"));

        int width = 400;
        int height = 300;
        final Stage newStage = createStyledStage(new Scene(loader.load(), width, height));
        newStage.setTitle("new spare part");
        newStage.setMinWidth(width);
        newStage.setMinHeight(height);
        newStage.initModality(Modality.APPLICATION_MODAL);
        NewSparePartController controller = loader.getController();
        controller.initialize(nameOfSpareParts, units, models);
        newStage.showAndWait();
        return controller;
    }

    /**
     *
     * @param nameOfSpareParts
     * @param units
     * @param models
     */
    private void initialize(@NotNull Set<String> nameOfSpareParts, @NotNull Set<String> units,
                            @NotNull Set<String> models){
        TextFields.bindAutoCompletion(this.nameTxtFld, nameOfSpareParts);
        TextFields.bindAutoCompletion(this.unitTxtFld, units);
        ChoiceBoxUtils.addItems(this.categoryChcBx, models);
    }

    /**
     * Creates and adds a changeListener to the controller items
     * to regular the accessibility of the apply button
     */
    private void initializeListener() {
        this.unitTxtFld.textProperty().addListener((observableValue, s, t1) -> {
            this.amountUnitLbl.setText(t1);
        });

        BooleanBinding inputsValid = new BooleanBinding() {
            {
                this.bind(NewSparePartController.this.nameTxtFld.textProperty(), NewSparePartController.this.unitTxtFld.textProperty(),
                        NewSparePartController.this.quantityTxtFld.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return !NewSparePartController.this.unitTxtFld.getText().isEmpty() && !NewSparePartController.this.nameTxtFld.getText().isEmpty() &&
                        isPositive(NewSparePartController.this.quantityTxtFld);
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
        String defaultUnit = "pieces";
        this.unitTxtFld.setText(defaultUnit);
        this.amountUnitLbl.setText(defaultUnit);
        this.quantityTxtFld.setText("1");
        this.btnApply.setDisable(true);
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
        this.result = new SparePart(this.nameTxtFld.getText(), this.conditionChcBx.getValue(),
                this.unitTxtFld.getText(), this.categoryChcBx.getValue(),
            Integer.parseInt(this.quantityTxtFld.getText()));
        this.handleCancel();
    }
}
