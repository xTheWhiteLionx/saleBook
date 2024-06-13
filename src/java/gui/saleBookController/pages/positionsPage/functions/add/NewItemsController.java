package gui.saleBookController.pages.positionsPage.functions.add;

import gui.ApplicationMain;
import gui.FXutils.ChoiceBoxUtils;
import gui.FXutils.TableViewUtils;
import gui.FXutils.TextInputControlUtils;
import gui.saleBookController.pages.FunctionDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import logic.Condition;
import logic.products.item.ItemColor;
import logic.Variant;
import logic.products.item.Item;
import logic.products.position.Position;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import static gui.DialogWindow.displayError;

/**
 * This class represents a Controller to create some items and adds them to the position
 *
 * @author xthe_white_lionx
 * @see gui.saleBookController.pages.FunctionDialog
 */
public class NewItemsController extends FunctionDialog<Position> implements Initializable {
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

    @FXML
    public Button editButton;
    @FXML
    public Button saveButton;
    @FXML
    public Button deleteButton;
    @FXML
    private TableView<Item> itemTableView;
    /**
     * Button to apply the new items
     */
    @FXML
    public Button applyBtn;
    /**
     * Button to get the previous controller
     */
    @FXML
    public Button previousBtn;
    /**
     * The main pane of this controller
     */
    @FXML
    private BorderPane basePane;

    /**
     * The position to which the items will be added
     */
    public Position position;

    /**
     * Mapping from the name of an ItemColor to the ItemColor needed
     * for binding and autocompletion
     */
    private Map<String, ItemColor> nameToItemColor;

    /**
     * The MasterController needed to notify if the items are created
     */
    private MasterController masterController;

    /**
     * Creates and loads a new AddItemsController
     *
     * @param masterController needed to notify if the items are created
     * @param nameToItemColor  mapping from the name of an ItemColor to the ItemColor needed
     *                         for binding and autocompletion
     * @return the new created AddItemsController
     * @throws IOException If the matching fxml could not be loaded
     */
    public static @NotNull NewItemsController createAddItemsController(@NotNull MasterController masterController,
                                                                       @NotNull Map<String, ItemColor> nameToItemColor)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage" +
                        "/functions/add/NewItemsController.fxml"));

        loader.load();
        NewItemsController newItemsController = loader.getController();
        newItemsController.initialize(masterController, nameToItemColor);
        return newItemsController;
    }

    /**
     * Returns the base pane of this controller
     *
     * @return the base pane of this controller
     */
    public @NotNull Pane getBasePane() {
        return this.basePane;
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
        ChoiceBoxUtils.addItems(this.conditionChcBx, Condition.class);
        ChoiceBoxUtils.addItems(this.variantChcBx, Variant.class);
        this.applyBtn.setDisable(this.invalidInput());
        this.applyBtn.disableProperty().bind(this.colorNameComboBox.valueProperty().isEqualTo(""));

        this.initializeItemTableView();
        this.saveButton.setDisable(true);
//        this.colorNameComboBox.valueProperty().addListener((observableValue, s, t1) ->
//                this.applyBtn.setDisable(this.invalidInput()));
    }

    private void initializeItemTableView() {
        TableViewUtils.addColumn(this.itemTableView, "color", item -> {
            Circle circle = new Circle(0, 0, 10);
            ItemColor itemColor = item.getItemColor();
            circle.setFill(itemColor.getColor());
            Tooltip.install(circle, new Tooltip(itemColor.getName()));
            return circle;
        });
        TableViewUtils.addColumn(this.itemTableView, "condition",
                item -> item.getCondition().name());
        TableViewUtils.addColumn(this.itemTableView, "variant",
                item -> item.getVariant().name());
        TableViewUtils.addColumn(this.itemTableView, "error description", Item::getErrorDescription);

        this.itemTableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldItem, newItem) -> {
                    this.saveButton.setDisable(newItem == null);
                    if (newItem != null) {
                        this.conditionChcBx.setValue(newItem.getCondition());
                        this.variantChcBx.setValue(newItem.getVariant());
                        ItemColor itemColor = newItem.getItemColor();
                        this.colorNameComboBox.setValue(itemColor.getName());
                        this.colorpicker.setValue(itemColor.getColor());
                        this.errorDescriptionTxtArea.setText(newItem.getErrorDescription());
                    } else {
                        this.colorNameComboBox.setValue("");
                        this.colorpicker.setValue(Color.WHITE);
                        this.errorDescriptionTxtArea.setText("");
                    }
                });
    }

    /**
     * Sets the position of this controller to which the new created items will be added
     *
     * @param position the position the new created items will be added
     */
    public void setPosition(@NotNull Position position) {
        this.position = position;
    }

    /**
     * Initializes the specified fields of this controller and binds
     * each itemColorNameTextField to the itemColorPicker of the same row
     *
     * @param masterController needed to notify if the items are created
     * @param nameToItemColor  mapping from the name of an ItemColor to the ItemColor needed
     *                         for binding and autocompletion
     */
    private void initialize(@NotNull MasterController masterController,
                            @NotNull Map<String, ItemColor> nameToItemColor) {
        this.masterController = masterController;
        this.nameToItemColor = nameToItemColor;
        this.bind(this.colorNameComboBox, this.colorpicker);
    }

    /**
     * Checks if any input is invalid. An input is invalid if it is empty.
     *
     * @return true, if any input is invalid, otherwise false
     */
    private boolean invalidInput() {
        String value = this.colorNameComboBox.getValue();
        return value == null || value.isEmpty();
    }

    /**
     * Binds the specified colorPicker to the specified itemColorComboBox
     *
     * @param itemColorComboBox the ComboBox to bind the colorPicker on
     * @param colorPicker         the ColorPicker which should be bound
     */
    private void bind(@NotNull ComboBox<String> itemColorComboBox, @NotNull ColorPicker colorPicker) {
        if (this.nameToItemColor != null) {
            //TODO 31.05.2024 replace by getNames methode of itemcolor
            Set<String> possibleSuggestions = new TreeSet<>(this.nameToItemColor.keySet());
            itemColorComboBox.getItems().setAll(possibleSuggestions);
            itemColorComboBox.valueProperty().addListener((observableValue, oldText, newText) -> {
                ItemColor itemColor = this.nameToItemColor.get(newText);
                if (itemColor != null) {
                    colorPicker.setValue(itemColor.getColor());
                }
            });
        }
    }

    @FXML
    public void handleAdd(ActionEvent actionEvent) {
        int id = this.itemTableView.getItems().size() + 1;
        ItemColor itemColor = this.nameToItemColor.get(this.colorNameComboBox.getValue());
        Item item = new Item(id, this.conditionChcBx.getValue(), this.variantChcBx.getValue(),
                itemColor, this.errorDescriptionTxtArea.getText());
        this.itemTableView.getItems().add(item);
    }

    @FXML
    public void handleSave(ActionEvent actionEvent) {
        int id = this.itemTableView.getSelectionModel().getSelectedIndex();
        ItemColor itemColor = this.nameToItemColor.get(this.colorNameComboBox.getValue());
        Item item = new Item(id, this.conditionChcBx.getValue(), this.variantChcBx.getValue(),
                itemColor, this.errorDescriptionTxtArea.getText());
        this.itemTableView.getItems().set(id, item);
        this.itemTableView.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        int index = this.itemTableView.getSelectionModel().getSelectedIndex();
        this.itemTableView.getItems().remove(index);
        this.itemTableView.getSelectionModel().clearSelection();
    }

    /**
     * Handles the apply button, sets the result of this {@link FunctionDialog}
     */
    @FXML
    private void handleApply() {
        //TODO 31.05.2024 add methode
        for (Item item : this.itemTableView.getItems()) {
            this.position.addItem(item);
        }

        this.result = this.position;
        this.masterController.handleDone();
    }

    /**
     * Handles the previous button and shows the previous controller
     */
    @FXML
    private void handlePrevious() {
        this.masterController.showNewPositionController();
    }
}
