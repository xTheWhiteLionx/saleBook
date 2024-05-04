package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.saleBook.SaleBook;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static gui.FXutils.StageUtils.createStyledStage;

/**
 *
 */
public class AddPicturesController implements Initializable {
    /**
     * Button to upload the pictures for the shipping
     */
    @FXML
    private Button picturesUploadButton;

    /**
     * Target for the drag of the pictures
     */
    @FXML
    private HBox picturesDragTarget;

    /**
     * Label in the pictures drag zone
     */
    @FXML
    private Label picturesDragLabel;

    /**
     * Button to apply the input
     */
    @FXML
    private Button applyBtn;

    /**
     * ID of the position, which should be shipped
     */
    private int posId;

    /**
     * pictures file
     */
    private List<File> pictures;

    /**
     * the saleBook
     */
    private SaleBook saleBook;

    /**
     * Loads a AddPicturesController
     *
     * @param posId    ID of the position which should be shipped
     * @param saleBook the saleBook
     * @throws IOException if the fxml file cannot be loaded
     */
    public static void loadAddPicturesController(int posId, SaleBook saleBook)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/functions/" +
                        "AddPicturesInterface.fxml"));

        Stage stage = createStyledStage(new Scene(loader.load()));
        stage.setTitle("add pictures");
        stage.setMinWidth(200D);
        stage.setMinHeight(200D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        AddPicturesController controller = loader.getController();
        controller.posId = posId;
        controller.saleBook = saleBook;
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.picturesDragTarget.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != this.picturesDragTarget
                    && dragEvent.getDragboard().hasFiles()) {
                dragEvent.acceptTransferModes(TransferMode.ANY);
            }
            dragEvent.consume();
        });

        this.picturesDragTarget.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = db.hasFiles();
            if (success) {
                this.pictures = db.getFiles();
                this.picturesDragLabel.setText("pictures dragged");
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }

    /**
     * Saves the chosen file from the showOpenDialog as pictures
     */
    @FXML
    private void uploadPictures() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("positions"));
        this.pictures =
                fileChooser.showOpenMultipleDialog(this.picturesUploadButton.getScene().getWindow());
        this.picturesDragLabel.setText("pictures loaded");
    }

    /**
     * Closes the window
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Applies the input to the saleBook and closes the window
     */
    @FXML
    private void handleApply() {
        File parentDir = new File("positions/" + this.posId);
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        File dir = new File(parentDir, "pictures");
        if (!dir.exists()) {
            dir.mkdir();
        }
        boolean success = false;
        int filesSize = this.pictures.size();
        for (int i = 0; i < filesSize; i++) {
            File picture = this.pictures.get(i);
            if (picture != null) {
                String extension = FileUtils.getExtension(picture);
                success = picture.renameTo(new File(dir, "picture " + (i + 1) + "." + extension));
            }
        }
        if (success) {
            this.saleBook.updateStatus(filesSize + " pictures successfully added");
        }
        this.handleClose();
    }
}
