package gui.saleBookController.pages.positionsPage.functions.add;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.ItemColor;
import logic.products.position.Position;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;

/**
 * This controller class will be used to manage the switching, at a creation of a position, between
 * the newPositionController and the newItemController
 *
 * @author xthe_white_lionx
 */
public class MasterController extends FunctionDialog<Position> implements Initializable {
    /**
     * The base pane
     */
    @FXML
    public BorderPane borderPane;
    /**
     * Progressbar to show the progress of the creation of the position
     */
    @FXML
    public ProgressBar progressBar;
    /**
     * Circle to display the secondStep of the progress
     */
    @FXML
    public Circle secondStep;

    /**
     * Controller for the input of the new position
     */
    private NewPositionController newPositionController;

    /**
     * Controller for the input of the new item(s) of the new position
     */
    private NewItemsController newItemsController;

    /**
     * Creates a new MasterController, shows it and waits for the user input
     *
     * @param positionId the id of the position which should be created
     * @param categories set of possible categories for the position
     * @param nameToItemColor a mapping from the name to the matching itemColor
     * @return the new created MasterController
     * @throws IOException if the loading of the loader has failed
     */
    public static @NotNull MasterController createMasterController(int positionId, @NotNull Set<String> categories,
                                                          @NotNull Map<String, ItemColor> nameToItemColor)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage" +
                        "/functions/add/MasterController.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("new position");
        double width = 1000D;
        stage.setMinWidth(width);
        stage.setWidth(width);
        stage.setMinHeight(850D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        MasterController masterController = loader.getController();
        masterController.setValues(positionId, categories, nameToItemColor);
        stage.showAndWait();
        return masterController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Displays the newItemsController in the center of the base pane of this controller and adjusts
     * the progressBar
     */
    public void showNewItemsController() {
        this.newPositionController.getResult().ifPresent(position -> {
            this.newItemsController.setPosition(position);
            this.borderPane.setCenter(this.newItemsController.getPane());
            this.progressBar.setProgress(1);
            this.secondStep.setFill(Color.LIMEGREEN);
        });
    }

    /**
     * Displays the newPositionController in the center of the base pane of this controller and adjusts
     * the progressBar
     */
    public void showNewPositionController() {
        if (this.newItemsController != null) {
            this.borderPane.setCenter(this.newPositionController.getPane());
            this.progressBar.setProgress(0.5);
            this.secondStep.setFill(Color.LIGHTGREY);
        }
    }

    /**
     * Handles the finish of the progress of the creation of a position
     */
    public void handleDone() {
        this.newItemsController.getResult().ifPresent(position -> {
            this.result = position;
            Stage stage = (Stage) this.borderPane.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Sets the specified id, categories and nameToItemColor
     *
     * @param id the id for the new position
     * @param categories the categories from which the position can be
     * @param nameToItemColor a mapping from the name to the matching itemColor
     */
    private void setValues(int id, @NotNull Set<String> categories, @NotNull Map<String,
            ItemColor> nameToItemColor) {
        try {
            this.newPositionController = NewPositionController.createAddPositionController(
                    this, id, categories);
            this.borderPane.setCenter(this.newPositionController.getPane());
            this.newItemsController = NewItemsController.createAddItemsController(this,
                    nameToItemColor);
        } catch (IOException e) {
            displayError("failed to load newPositionController", e);
        }

    }
}
