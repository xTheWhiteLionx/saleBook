package gui.saleBookController.pages.positionsPage.functions.add;

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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;

import static gui.FXutils.ChoiceBoxUtils.addItems;
import static gui.FXutils.StageUtils.createStyledStage;

/**
 *  This controller has the new created item as result.
 *
 * @author xthe_white_lionx
 */
public class NewItemController extends FunctionDialog<Item> implements Initializable {
    /**
     * Label to display the id of the item
     */
    @FXML
    private Label idLbl;
    /**
     * ChoiceBox of possible conditions that the item could have
     */
    @FXML
    private ChoiceBox<Condition> conditionChcBx;
    /**
     * ChoiceBox of possible variants that the item could have
     */
    @FXML
    private ChoiceBox<Variant> variantChcBx;
    /**
     * TextField for the name of the item color
     */
    @FXML
    private ComboBox<String> colorNameComboBox;
    /**
     * ColorPicker for the item color
     */
    @FXML
    private ColorPicker colorpicker;
    /**
     * TextArea to write the error description for the item
     */
    @FXML
    public TextArea errorDescriptionTxtArea;
    /**
     * Button to apply the item
     */
    @FXML
    private Button applyBtn;
    /**
     * Button to close the window
     */
    @FXML
    private Button btnCancel;
    /**
     * Id of the item
     */
    private int itemId;

    /**
     * Loads and creates a new AddItemController
     *
     * @param itemId the id of the item that should be created
     * @param nameToItemColor a map from the name of an itemColor to the corresponding color
     * @return a new AddItemController
     * @throws IOException If the matching fxml could not be loaded
     */
    public static @NotNull NewItemController createAddItemController(int itemId,
                                                                      @NotNull Map<String,
                                                            ItemColor> nameToItemColor)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage" +
                        "/functions/add/NewItemController.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("new Item");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        NewItemController controller = loader.getController();
        controller.initializeController(itemId, nameToItemColor);
        stage.showAndWait();
        return controller;
    }

    /**
     * Initializes the components of this controller with the specified parameters
     *
     * @param itemId the id of the item which should be created
     * @param nameToItemColor a map from the name of an itemColor to the corresponding color
     */
    private void initializeController(int itemId, Map<String, ItemColor> nameToItemColor) {
        this.itemId = itemId;
        this.idLbl.setText(String.valueOf(itemId));
        TreeSet<String> possibleSuggestions = new TreeSet<>(nameToItemColor.keySet());
        this.colorNameComboBox.getItems().setAll(possibleSuggestions);
        this.colorNameComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            ItemColor itemColor = nameToItemColor.get(newValue);
            if (itemColor != null) {
                this.colorpicker.setValue(itemColor.getColor());
            }
            this.applyBtn.setDisable(newValue.isEmpty());
        });
        addItems(this.conditionChcBx, Condition.class);
        addItems(this.variantChcBx, Variant.class);
        this.applyBtn.setDisable(true);
    }

    /**
     * Initializes.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextInputControlUtils.installTouch(this.colorNameComboBox.getEditor());
        TextInputControlUtils.installTouch(this.errorDescriptionTxtArea);
    }

    /**
     * Handles the close button and closes the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.btnCancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the "Apply" Button and sets the result to the new item
     */
    @FXML
    private void handleApply() {
        this.result = new Item(this.itemId, this.conditionChcBx.getValue(), this.variantChcBx.getValue(),
                ItemColor.getItemColor(this.colorNameComboBox.getValue(), this.colorpicker.getValue()),
                this.errorDescriptionTxtArea.getText());
        this.handleCancel();
    }

}
