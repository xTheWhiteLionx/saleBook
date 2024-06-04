package gui;

import gui.saleBookController.SaleBookController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.*;
import static gui.saleBookController.SaleBookController.initializeSaleBookController;

/**
 * StartController to choose an existing book/file or create a new one
 *
 * @author xthe_white_lionx
 */
public class StartController implements Initializable {

    /**
     * Button to load the book/file
     */
    @FXML
    public Button loadBook;

    /**
     * Initializes this controller.
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
    @FXML
    public void handleCreateNewBook() {
        SaleBookController.initializeSaleBookController();
        this.closeWindow();
    }

    /**
     * Handles the create new Book button
     */
    @FXML
    public void handleLoadBook() {
        Window window = this.loadBook.getScene().getWindow();
        File selectedFile = createFileChooser().showOpenDialog(window);
        if (selectedFile != null) {
            try {
                initializeSaleBookController(selectedFile);
                this.closeWindow();
            } catch (IOException e) {
                displayError("fail to load the selected file", e);
            }
        }
    }

    /**
     * Closes the window
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) this.loadBook.getScene().getWindow();
        stage.close();
    }
}
