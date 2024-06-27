package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import costumeClasses.FXClasses.BoundedDateCell;
import gui.saleBookController.pages.FunctionDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.FXutils.StageUtils.createStyledStage;

/**
 * Controller to set the receive date for a position
 *
 * @author xthe_white_lionx
 * @see gui.saleBookController.pages.FunctionDialog
 */
public class ReceivedController extends FunctionDialog<LocalDate> implements Initializable {
    /**
     * DatePicker to pick the receive date
     */
    @FXML
    private DatePicker receivedDatePicker;

    /**
     * Button to apply the receive date
     */
    @FXML
    private Button btnApply;

    /**
     * Creates and loads a new ReceivedController
     *
     * @param minDate the minimal date of the DatePicker
     * @return the new created ReceivedController
     * @throws IOException if the fxml could not be loaded
     */
    public static @NotNull ReceivedController createReceivedController(@NotNull LocalDate minDate)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/functions/ReceivedController.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("set receive date");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        ReceivedController controller = loader.getController();
        controller.initializeDatePicker(minDate);
        stage.showAndWait();
        return controller;
    }

    /**
     * Initializes the receivedDatePicker by setting the minimal date
     *
     * @param minDate the minimal reachable date, inclusive this date
     */
    private void initializeDatePicker(@NotNull LocalDate minDate) {
        this.receivedDatePicker.setDayCellFactory(cf -> new BoundedDateCell(minDate, LocalDate.now()));
    }

    /**
     * Initializes the controls of this controller.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.receivedDatePicker.setValue(LocalDate.now());
    }

    /**
     * Handles the apply button and sets the result
     */
    @FXML
    private void handleApply() {
        this.result = this.receivedDatePicker.getValue();
        this.handleCancel();
    }

    /**
     * Handles the cancel button and closes the window
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.btnApply.getScene().getWindow();
        stage.close();
    }
}
