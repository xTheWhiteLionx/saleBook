package gui;

import gui.FXutils.StageUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * This class contains the dialog window methods.
 *
 * @author xthe_white_lionx
 */
public class DialogWindow {

    /**
     * The directory of the package of the json files
     */
    private static final String DIRECTORY = "books";

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private DialogWindow() {
    }

    /**
     * Returns a json filtered FileChooser on initialized to this {@link #DIRECTORY}.
     * Creates the DIRECTORY if it does not exist.
     *
     * @return a json filtered FileChooser to this {@link #DIRECTORY}
     */
    public static @NotNull FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        //ensure the dialog opens in the correct directory
        File theDir = new File(DIRECTORY);
        if (!theDir.isDirectory()) {
            theDir.mkdir();
        }
        fileChooser.setInitialDirectory(theDir);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );
        return fileChooser;
    }

    /**
     * creates a confirmation alert
     *
     * @return true if deleting were accepted
     */
    public static boolean acceptedDeleteAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        StageUtils.styleStage(stage);
        alert.setContentText("Are your sure you want to delete the selected item?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * creates a warning alert
     *
     * @return true if deleting were accepted
     */
    public static boolean unsavedDataAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        StageUtils.styleStage(stage);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.setHeaderText("You have unsaved data");
        alert.setContentText("Unsaved data will get lost. Should the app still be closed?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Displays an alert with the specified exception as near content
     *
     * @param headerText the headerText of the alert
     * @param ex exception which should be displayed
     * @throws NullPointerException if the specified element is null
     */
    public static void displayError(@NotNull String headerText, @NotNull Exception ex) {
        Alert alert = createErrorAlert(ex);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    /**
     * Displays an alert with the specified exception as near content
     *
     * @param ex exception which should be displayed
     * @throws NullPointerException if the specified element is null
     */
    public static void displayError(@NotNull Exception ex) {
        createErrorAlert(ex).showAndWait();
    }

    /**
     *
     * @param ex
     * @return
     */
    private static Alert createErrorAlert(@NotNull Exception ex){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(ex.getMessage());
        DialogPane dialogPane = alert.getDialogPane();
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        StageUtils.styleStage(stage);

        ex.printStackTrace(printWriter);
        printWriter.close();

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Exception stacktrace:"), 0, 0);
        gridPane.add(new TextArea(stringWriter.toString()), 0, 1);

        dialogPane.setExpandableContent(gridPane);
        return alert;
    }
}
