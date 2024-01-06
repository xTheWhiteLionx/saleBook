package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.products.position.Position;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;

public class ReceivedController extends FunctionDialog<LocalDate> implements Initializable {
    /**
     *
     */
    @FXML
    private DatePicker receivedDatePicker;
    /**
     *
     */
    @FXML
    private Button btnApply;
    /**
     *
     */
    private SaleBook saleBook;
    /**
     * The current controlled investment
     */
    private Position currItem;

    /**
     *
     */
    public static @NotNull ReceivedController createReceivedController(@NotNull LocalDate orderDate)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/functions/ReceivedController.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = createStyledStage(scene);
        stage.setTitle("set received date");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        ReceivedController controller = loader.getController();
        controller.initializeDatePicker(orderDate);
        stage.showAndWait();
        return controller;
    }

    private void initializeDatePicker(@NotNull LocalDate orderDate) {
        this.receivedDatePicker.setValue(LocalDate.now());
        this.receivedDatePicker.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean b) {
                super.updateItem(item,b);
                if (item.isBefore(orderDate)) {
                    this.setDisable(true);
                    this.setStyle("-fx-background-color: #7e7e7e;");
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void handleApply() {
        this.result = this.receivedDatePicker.getValue();
        this.handleCancel();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.btnApply.getScene().getWindow();
        stage.close();
    }
}
