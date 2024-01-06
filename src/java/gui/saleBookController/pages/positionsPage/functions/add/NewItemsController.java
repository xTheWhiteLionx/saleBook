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
import logic.ItemColor;
import logic.Variant;
import logic.products.Item;
import logic.products.position.Position;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.util.ChoiceBoxUtils.createChoiceBox;

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
    private BorderPane borderPane;
    /**
     * GridPane
     */
    @FXML
    public GridPane itemsGridPane;
    /**
     *
     */
    public final List<ChoiceBox<Variant>> variantChcBxs = new ArrayList<>();
    /**
     *
     */
    public final List<ChoiceBox<Condition>> conditionChcBxs = new ArrayList<>();
    /**
     *
     */
    public final List<TextField> itemColorNameTxtFlds = new ArrayList<>();
    /**
     *
     */
    public final List<ColorPicker> itemColorPickers = new ArrayList<>();
    /**
     *
     */
    public final List<TextArea> errorDescriptionTxtAreas = new ArrayList<>();

    /**
     * The position to which the items will be added
     */
    public Position position;

    /**
     *
     */
    private Map<String, ItemColor> nameToItemColor;

    /**
     *
     */
    private MasterController masterController;

    /**
     *
     * @param masterController
     * @param nameToItemColor
     * @return
     * @throws IOException If the matching fxml could not be loaded
     */
    public static @NotNull NewItemsController createAddItemsController(@NotNull MasterController  masterController,
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
     * @return
     */
    public @NotNull Pane getPane() {
        return this.borderPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.itemsGridPane = new GridPane();
        Label conditionLbl = new Label("condition");
        GridPane.setMargin(conditionLbl, new Insets(5));
        Label variantLbl = new Label("variant");
        GridPane.setMargin(variantLbl, new Insets(5));
        Label colorLbl = new Label("color");
        GridPane.setMargin(colorLbl, new Insets(5));
        Label errorDescriptionLbl = new Label("error description");
        GridPane.setMargin(errorDescriptionLbl, new Insets(5));
        this.itemsGridPane.addRow(0, conditionLbl, variantLbl, colorLbl, errorDescriptionLbl);

        this.addItemControls(true);
    }

    /**
     * @param position
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
    private void addItemControls(boolean isFirstItem){
        ChoiceBox<Condition> conditionChoiceBox = createChoiceBox(Condition.class);
        this.conditionChcBxs.add(conditionChoiceBox);
        GridPane.setMargin(conditionChoiceBox, new Insets(5));
        ChoiceBox<Variant> variantChoiceBox = createChoiceBox(Variant.class);
        this.variantChcBxs.add(variantChoiceBox);
        GridPane.setMargin(variantChoiceBox, new Insets(5));
        TextField itemColorNameTxtFld = new TextField();
        itemColorNameTxtFld.setPromptText("color name");
        this.itemColorNameTxtFlds.add(itemColorNameTxtFld);
        TextArea errorDescriptionTxtArea = new TextArea();
        errorDescriptionTxtArea.setPromptText("error description");
        errorDescriptionTxtArea.setPrefHeight(80);
        errorDescriptionTxtArea.setPrefWidth(300);
        GridPane.setMargin(errorDescriptionTxtArea, new Insets(5));
        this.errorDescriptionTxtAreas.add(errorDescriptionTxtArea);

        this.applyBtn.setDisable(this.isAnyInputInvalid());
        itemColorNameTxtFld.textProperty().addListener((observableValue, s, t1) ->
                this.applyBtn.setDisable(this.isAnyInputInvalid()));

        ColorPicker itemColorPicker = new ColorPicker();
        this.itemColorPickers.add(itemColorPicker);
        this.bind(itemColorNameTxtFld, itemColorPicker);
        HBox hBox = new HBox(itemColorNameTxtFld, itemColorPicker);
        hBox.setAlignment(Pos.CENTER);
        HBox.setMargin(itemColorNameTxtFld, new Insets(5));
        HBox.setMargin(itemColorPicker, new Insets(5));
        int index = this.itemsGridPane.getRowCount();

        this.itemsGridPane.addRow(index, conditionChoiceBox, variantChoiceBox, hBox, errorDescriptionTxtArea);
        if (!isFirstItem) {
            Button deleteButton = new Button("delete");
            deleteButton.setOnAction(actionEvent -> {
                this.conditionChcBxs.remove(conditionChoiceBox);
                this.variantChcBxs.remove(variantChoiceBox);
                this.itemColorNameTxtFlds.remove(itemColorNameTxtFld);
                this.errorDescriptionTxtAreas.remove(errorDescriptionTxtArea);
                this.applyBtn.setDisable(this.isAnyInputInvalid());

                this.itemColorPickers.remove(itemColorPicker);
                this.itemsGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == index);
            });

            this.itemsGridPane.addRow(index, deleteButton);
        }
    }

    /**
     *
     *
     * @param masterController
     * @param nameToItemColor
     */
    private void initialize(@NotNull MasterController masterController,
                            @NotNull Map<String, ItemColor> nameToItemColor) {
        this.masterController = masterController;
        this.nameToItemColor = nameToItemColor;

        for (int i = 0; i < this.itemColorNameTxtFlds.size(); i++) {
            this.bind(this.itemColorNameTxtFlds.get(i), this.itemColorPickers.get(i));
        }

        this.borderPane.setCenter(new ScrollPane(this.itemsGridPane));
    }

    /**
     * Checks if any input is invalid. An input is invalid if it is empty.
     *
     * @return true, if any input is invalid, otherwise false
     */
    private boolean isAnyInputInvalid(){
        for (TextField colorNameTxtFld : this.itemColorNameTxtFlds) {
            if(colorNameTxtFld.getText().isEmpty()){
                return true;
            }
        }
        return false;
    }

    /**
     * Binds the specified colorPicker to the specified itemColorNameTxtFld
     *
     * @param itemColorNameTxtFld the textField to bind the colorPicker on
     * @param colorPicker the colorPicker which should be bound
     */
    private void bind(@NotNull TextField itemColorNameTxtFld, @NotNull ColorPicker colorPicker) {
        if (this.nameToItemColor != null) {
            TextFields.bindAutoCompletion(itemColorNameTxtFld, this.nameToItemColor.keySet());
            itemColorNameTxtFld.textProperty().addListener((observableValue, oldText, newText) -> {
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
            String itemColorName = this.itemColorNameTxtFlds.get(i).getText();
            Color itemColor = this.itemColorPickers.get(i).getValue();
            String errorDescription = this.errorDescriptionTxtAreas.get(i).getText();
            Item item = new Item(id, condition, variant, new ItemColor(itemColorName, itemColor),
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
