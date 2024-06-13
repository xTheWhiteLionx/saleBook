package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.FXutils.TextInputControlUtils;
import gui.saleBookController.pages.FunctionDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Condition;
import logic.products.item.ItemColor;
import logic.Variant;
import logic.products.item.Item;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.FXutils.ChoiceBoxUtils.addItems;
import static gui.FXutils.StageUtils.createStyledStage;

/**
 * This controller is used to edit the specified item
 *
 * @see gui.saleBookController.pages.FunctionDialog
 * @author xthe_white_lionx
 */
public class EditItemController extends FunctionDialog<Boolean> implements Initializable {
    /**
     * Label to display the id of the item
     */
    @FXML
    private Label idLbl;
    /**
     * ChoiceBox to display the (possible) condition of the item
     */
    @FXML
    private ChoiceBox<Condition> conditionChcBx;
    /**
     * ChoiceBox to display the (possible) variant of the item
     */
    @FXML
    private ChoiceBox<Variant> variantChcBx;
    /**
     * ComboBox to display the name of the itemColor of the item
     */
    @FXML
    private ComboBox<String> colorNameComboBox;
    /**
     * ColorPicker to display the color of the itemColor of the item
     */
    @FXML
    private ColorPicker colorpicker;
    /**
     * TextArea to display the error description of the item
     */
    @FXML
    private TextArea errorDescriptionTxtArea;
    /**
     * Button to handle the apply
     */
    @FXML
    private Button applyBtn;
    /**
     * Button to handle the cancel
     */
    @FXML
    private Button btnCancel;

    /**
     * The item which should be edited
     */
    private Item item;

    /**
     * Creates and loads a new EditItemController
     *
     * @param item the item which should be edited
     * @param nameToItemColor a map from the name of an itemColor to the corresponding color
     * @return the new created EditItemController
     * @throws IllegalArgumentException if item or nameToÍtemColor is null
     */
    public static EditItemController createEditItemController(@NotNull Item item,
                                                            @NotNull Map<String,
                                                              ItemColor> nameToItemColor)
            throws IOException {
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
     * Initializes the controller to the specified fields
     *
     * @param item the item which should be edited
     * @param nameToItemColor a map from the name of an itemColor to the corresponding color
     */
    private void initializeController(Item item, Map<String, ItemColor> nameToItemColor) {
        this.item = item;

        this.idLbl.setText(String.valueOf(item.getId()));
        this.conditionChcBx.setValue(item.getCondition());
        this.variantChcBx.setValue(item.getVariant());
        ItemColor itemColor = item.getItemColor();
        this.colorNameComboBox.setValue(itemColor.getName());
        this.colorpicker.setValue(itemColor.getColor());
        this.errorDescriptionTxtArea.setText(item.getErrorDescription());

        Set<String> possibleSuggestions = nameToItemColor.keySet();
        this.colorNameComboBox.getItems().setAll(possibleSuggestions);
        TextFields.bindAutoCompletion(this.colorNameComboBox.getEditor(), possibleSuggestions);
        this.colorNameComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            ItemColor searchedItemColor = nameToItemColor.get(newValue);
            if (searchedItemColor != null) {
                this.colorpicker.setValue(searchedItemColor.getColor());
            }
            this.applyBtn.setDisable(newValue.isEmpty());
        });
    }

    /**
     * Initializes the controller.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextInputControlUtils.installTouch(this.colorNameComboBox.getEditor());
        TextInputControlUtils.installTouch(this.errorDescriptionTxtArea);
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
     * Handles the "Apply" Button and sets the new values for the item also the result will be set to true
     */
    @FXML
    private void handleApply() {
        this.item.setCondition(this.conditionChcBx.getValue());
        this.item.setVariant(this.variantChcBx.getValue());
        this.item.setItemColor(ItemColor.getItemColor(this.colorNameComboBox.getValue(),
                this.colorpicker.getValue()));
        this.item.setErrorDescription(this.errorDescriptionTxtArea.getText());
        this.result = true;
        this.handleCancel();
    }
}
