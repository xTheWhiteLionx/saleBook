package gui.saleBookController.pages.positionsPage.functions.add;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import logic.Condition;
import logic.products.item.ItemColor;
import logic.Variant;
import logic.products.item.Item;
import logic.products.position.Position;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import static gui.DialogWindow.displayError;
import static gui.FXutils.ChoiceBoxUtils.createChoiceBox;

/**
 * This class represents a Controller to create some items and adds them to the position
 *
 * @author xthe_white_lionx
 * @see gui.saleBookController.pages.FunctionDialog
 */
public class NewItemsController extends FunctionDialog<Position> implements Initializable {
    /**
     * Button to apply the new items
     */
    public Button applyBtn;
    /**
     * Button to get the previous controller
     */
    public Button previousBtn;
    /**
     * The main pane of this controller
     */
    @FXML
    private BorderPane basePane;
    /**
     * GridPane to display the input for the items
     */
    @FXML
    public GridPane itemsGridPane;
    /**
     * The ChoiceBox of the variant of each item stored in a list for easier access
     */
    public final List<ChoiceBox<Variant>> variantChcBxs = new ArrayList<>();
    /**
     * The ChoiceBox of the condition of each item stored in a list for easier access
     */
    public final List<ChoiceBox<Condition>> conditionChcBxs = new ArrayList<>();
    /**
     * The ComboBox for the name of the itemColor of each item stored in a list for easier access
     */
    public final List<ComboBox<String>> itemColorNameComboBoxes = new ArrayList<>();
    /**
     * The ColorPicker for the color of the itemColor of each item stored in a list for easier access
     */
    public final List<ColorPicker> itemColorPickers = new ArrayList<>();
    /**
     * The TextArea of the error description of each item stored in a list for easier access
     */
    public final List<TextArea> errorDescriptionTxtAreas = new ArrayList<>();

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
        this.addItemControls(true);
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
     * Handles the addItem button and adds the item controls
     */
    @FXML
    public void handleAddItem() {
        this.addItemControls(false);
    }

    /**
     * Adds the needed Controls for a new Item depending on the specified isFirstItem. If it is
     * not the first item, a delete button will be added, to delete the controls for the item
     *
     * @param isFirstItem true if the item is the first item for the position
     */
    private void addItemControls(boolean isFirstItem) {
        ChoiceBox<Condition> conditionChoiceBox = createChoiceBox(Condition.class);
        this.conditionChcBxs.add(conditionChoiceBox);
        GridPane.setMargin(conditionChoiceBox, new Insets(5));
        ChoiceBox<Variant> variantChoiceBox = createChoiceBox(Variant.class);
        this.variantChcBxs.add(variantChoiceBox);
        GridPane.setMargin(variantChoiceBox, new Insets(5));
        ComboBox<String> itemColorNameComboBox = new ComboBox<>();
        itemColorNameComboBox.setEditable(true);
        itemColorNameComboBox.setPromptText("color name");
        this.itemColorNameComboBoxes.add(itemColorNameComboBox);
        TextArea errorDescriptionTxtArea = new TextArea();
        errorDescriptionTxtArea.setPromptText("error description");
        errorDescriptionTxtArea.setPrefHeight(80);
        errorDescriptionTxtArea.setPrefWidth(300);
        GridPane.setMargin(errorDescriptionTxtArea, new Insets(5));
        this.errorDescriptionTxtAreas.add(errorDescriptionTxtArea);

        this.applyBtn.setDisable(this.isAnyInputInvalid());
        itemColorNameComboBox.valueProperty().addListener((observableValue, s, t1) ->
                this.applyBtn.setDisable(this.isAnyInputInvalid()));

        ColorPicker itemColorPicker = new ColorPicker();
        this.itemColorPickers.add(itemColorPicker);
        this.bind(itemColorNameComboBox, itemColorPicker);
        HBox hBox = new HBox(itemColorNameComboBox, itemColorPicker);
        hBox.setAlignment(Pos.CENTER);
        HBox.setMargin(itemColorNameComboBox, new Insets(5));
        HBox.setMargin(itemColorPicker, new Insets(5));
        int index = this.itemsGridPane.getRowCount();

        this.itemsGridPane.addRow(index, conditionChoiceBox, variantChoiceBox, hBox, errorDescriptionTxtArea);
        if (!isFirstItem) {
            Button deleteButton = new Button("delete");
            deleteButton.setOnAction(actionEvent -> {
                this.conditionChcBxs.remove(conditionChoiceBox);
                this.variantChcBxs.remove(variantChoiceBox);
                this.itemColorNameComboBoxes.remove(itemColorNameComboBox);
                this.errorDescriptionTxtAreas.remove(errorDescriptionTxtArea);
                this.applyBtn.setDisable(this.isAnyInputInvalid());

                this.itemColorPickers.remove(itemColorPicker);
                this.itemsGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == index);
            });

            this.itemsGridPane.addRow(index, deleteButton);
        }
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

        for (int i = 0; i < this.itemColorNameComboBoxes.size(); i++) {
            this.bind(this.itemColorNameComboBoxes.get(i), this.itemColorPickers.get(i));
        }
    }

    /**
     * Checks if any input is invalid. An input is invalid if it is empty.
     *
     * @return true, if any input is invalid, otherwise false
     */
    private boolean isAnyInputInvalid() {
        for (ComboBox<String> colorNameComboBox : this.itemColorNameComboBoxes) {
            String value = colorNameComboBox.getValue();
            if (value == null || colorNameComboBox.getValue().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Binds the specified colorPicker to the specified itemColorComboBox
     *
     * @param itemColorComboBox the ComboBox to bind the colorPicker on
     * @param colorPicker         the ColorPicker which should be bound
     */
    private void bind(@NotNull ComboBox<String> itemColorComboBox, @NotNull ColorPicker colorPicker) {
        if (this.nameToItemColor != null) {
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

    /**
     * Handles the apply button, sets the result of this {@link FunctionDialog}
     */
    @FXML
    private void handleApply() {
        int id = 0;
        for (int i = 0; i < this.conditionChcBxs.size(); i++) {
            id = this.position.getNextItemId();
            Condition condition = this.conditionChcBxs.get(i).getValue();
            Variant variant = this.variantChcBxs.get(i).getValue();
            String itemColorName = this.itemColorNameComboBoxes.get(i).getValue();
            Color itemColor = this.itemColorPickers.get(i).getValue();
            String errorDescription = this.errorDescriptionTxtAreas.get(i).getText();
            Item item = new Item(id, condition, variant, ItemColor.getItemColor(itemColorName, itemColor),
                    errorDescription);
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
