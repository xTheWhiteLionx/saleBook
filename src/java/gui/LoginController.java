package gui;

import gui.saleBookController.SaleBookController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.createFileChooser;
import static gui.DialogWindow.displayInvalidFile;
import static gui.saleBookController.SaleBookController.initializeSaleBookController;

/**
 * MasterController of the graphical file interface.
 *
 * @author xthe_white_lionx
 */
public class LoginController implements Initializable {

    /**
     *
     */
    @FXML
    public Button loadBook;

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * handles the create new Book button
     */
    public void handleCreateNewBook() {
        SaleBookController.initializeSaleBookController();
        this.closeWindow();
    }

    /**
     * handles the create new Book button
     */
    public void handleLoadBook() {
        File selectedFile = createFileChooser().showOpenDialog(this.loadBook.getScene().getWindow());
        if (selectedFile != null) {
            try {
                initializeSaleBookController(selectedFile);
                this.closeWindow();
            } catch (IOException e) {
                displayInvalidFile(e);
            }
        }
    }

    /**
     * Handles the "exit" button.
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) this.loadBook.getScene().getWindow();
        stage.close();
    }
}
