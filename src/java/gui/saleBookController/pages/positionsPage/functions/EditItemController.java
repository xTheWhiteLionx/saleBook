package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Condition;
import logic.ItemColor;
import logic.Variant;
import logic.products.Item;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static gui.util.ChoiceBoxUtils.addItems;
import static gui.util.StageUtils.createStyledStage;

public class EditItemController extends FunctionDialog<Boolean> implements Initializable {
    @FXML
    private Label idLbl;
    @FXML
    private ChoiceBox<Condition> conditionChcBx;
    @FXML
    private ChoiceBox<Variant> variantChcBx;
    @FXML
    private TextField colorNameTxtFld;
    @FXML
    private ColorPicker colorpicker;
    @FXML
    private TextArea errorDescriptionTxtArea;
    /**
     *
     */
    @FXML
    private Button applyBtn;
    /**
     *
     */
    @FXML
    private Button btnCancel;

    /**
     *
     */
    private Item item;

    /**
     *
     * @param item
     * @param nameToItemColor
     * @return
     * @throws IllegalArgumentException if item or nameToÍtemColor is null
     */
    public static EditItemController createEditItemController(@NotNull Item item,
                                                            @NotNull Map<String,
                                                              ItemColor> nameToItemColor) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage" +
                        "/functions/EditItemController.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("edit Item");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        EditItemController controller = loader.getController();
        controller.initializeController(item, nameToItemColor);
        stage.showAndWait();
        return controller;
    }

    /**
     *
     * @param item
     * @param nameToItemColor
     */
    private void initializeController(Item item, Map<String, ItemColor> nameToItemColor) {
        this.item = item;

        this.idLbl.setText(String.valueOf(item.getId()));
        this.conditionChcBx.setValue(item.getCondition());
        this.variantChcBx.setValue(item.getVariant());
        ItemColor itemColor = item.getItemColor();
        this.colorNameTxtFld.setText(itemColor.getName());
        this.colorpicker.setValue(itemColor.getColor());
        this.errorDescriptionTxtArea.setText(item.getErrorDescription());

        TextFields.bindAutoCompletion(this.colorNameTxtFld, nameToItemColor.keySet());
        this.colorNameTxtFld.textProperty().addListener((observableValue, oldValue, newValue) -> {
            ItemColor searchedItemColor = nameToItemColor.get(newValue);
            if (searchedItemColor != null) {
                this.colorpicker.setValue(searchedItemColor.getColor());
            }
            this.applyBtn.setDisable(newValue.isEmpty());
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
        addItems(this.conditionChcBx, Condition.class);
        addItems(this.variantChcBx, Variant.class);
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
     * the (new) {@link Item} attributes
     */
    @FXML
    private void handleApply() {
        this.item.setCondition(this.conditionChcBx.getValue());
        this.item.setVariant(this.variantChcBx.getValue());
        this.item.setItemColor(new ItemColor(this.colorNameTxtFld.getText(),
                this.colorpicker.getValue()));
        this.item.setErrorDescription(this.errorDescriptionTxtArea.getText());
        this.result = true;
        this.handleCancel();
    }
}
