package gui;

import gui.util.StageUtils;
import javafx.scene.Scene;
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
    public static final String DIRECTORY = "books";

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
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        fileChooser.setInitialDirectory(theDir);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );
        return fileChooser;
    }

    /**
     * creates a confirmation alert with the given content
     *
     * @return true if deleting were accepted
     */
    public static boolean acceptedDeleteAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        StageUtils.styleScene(alert.getDialogPane().getScene());
        alert.setContentText("Are your sure you want to delete the selected item?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Displays an alert with the specified exception as near content
     *
     * @param headerText
     * @param ex exception which should be displayed
     * @throws NullPointerException if the specified element is null
     */
    public static void displayError(@NotNull String headerText, @NotNull Exception ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(ex.getMessage());
        DialogPane dialogPane = alert.getDialogPane();

        StageUtils.styleScene(dialogPane.getScene());

        ex.printStackTrace(printWriter);
        printWriter.close();

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Exception stacktrace:"), 0, 0);
        gridPane.add(new TextArea(stringWriter.toString()), 0, 1);

        dialogPane.setExpandableContent(gridPane);
        alert.showAndWait();
    }

    /**
     * Displays an alert with the specified exception as near content
     *
     * @param ex exception which should be displayed
     * @throws NullPointerException if the specified element is null
     */
    public static void displayError(@NotNull Exception ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(ex.getMessage());
        DialogPane dialogPane = alert.getDialogPane();

        Scene scene = dialogPane.getScene();
        StageUtils.styleScene(scene);

        ex.printStackTrace(printWriter);
        printWriter.close();

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Exception stacktrace:"), 0, 0);
        gridPane.add(new TextArea(stringWriter.toString()), 0, 1);

        dialogPane.setExpandableContent(gridPane);
        alert.showAndWait();
    }

    /**
     * Displays an alert with the specified exception as near content
     *
     * @param ex exception which should be displayed
     * @throws NullPointerException if the specified element is null
     */
    public static void displayInvalidFile(Exception ex) {
        if (ex != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Scene scene = alert.getDialogPane().getScene();
            StageUtils.styleScene(scene);

            alert.setHeaderText("Die ausgewählte Datei ist fehlerhaft!");

            alert.setContentText(ex.getMessage());

            ex.printStackTrace(printWriter);
            printWriter.close();

            GridPane gridPane = new GridPane();
            gridPane.add(new Label("Exception stacktrace:"), 0, 0);
            gridPane.add(new TextArea(stringWriter.toString()), 0, 1);

            alert.getDialogPane().setExpandableContent(gridPane);

            alert.showAndWait();
        } else {
            throw new NullPointerException("exception == null");
        }
    }
}
