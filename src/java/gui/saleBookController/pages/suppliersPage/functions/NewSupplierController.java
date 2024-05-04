package gui.saleBookController.pages.suppliersPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.FXutils.StageUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Supplier;
import utils.URLUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.FXutils.StageUtils.createStyledStage;

/**
 * Controller to create a new supplier
 *
 * @see gui.saleBookController.pages.FunctionDialog
 * @author xthe_white_lionx
 */
public class NewSupplierController extends FunctionDialog<Supplier> implements Initializable {

    /**
     * TextField to set the name of the new supplier
     */
    @FXML
    private TextField nameTxtFld;

    /**
     * TextField to set the order web page of the new supplier
     */
    @FXML
    private TextField orderWebPageTxtFld;

    /**
     * Button to apply the new supplier
     */
    @FXML
    private Button applyBtn;

    /**
     * Name of the suppliers of the saleBook
     */
    private Set<String> nameOfSuppliers;

    /**
     * Creates and loads a new created NewSupplierController
     *
     * @param nameOfSuppliers the name of the already existing suppliers of the saleBook
     * @return the new created NewSupplierController
     * @throws IOException if the fxml file cannot be loaded
     */
    public static @NotNull NewSupplierController createNewSupplierController(
            @NotNull Set<String> nameOfSuppliers) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/suppliersPage/" +
                        "functions/NewSupplierController.fxml"));
        Stage newStage = createStyledStage(new Scene(loader.load()));
        newStage.setTitle("new supplier");
        newStage.initModality(Modality.APPLICATION_MODAL);
        NewSupplierController controller = loader.getController();
        controller.nameOfSuppliers = nameOfSuppliers;
        newStage.showAndWait();
        return controller;
    }

    /**
     * Initializes this controller
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BooleanBinding hasInvalidInput = new BooleanBinding() {
            {
                this.bind(NewSupplierController.this.nameTxtFld.textProperty(), NewSupplierController.this.orderWebPageTxtFld.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return NewSupplierController.this.nameTxtFld.getText().isEmpty() &&
                        URLUtils.isValidURL(NewSupplierController.this.orderWebPageTxtFld.getText());
            }
        };
        this.applyBtn.disableProperty().bind(hasInvalidInput);
    }

    /**
     * Handles the press of the apply button
     */
    @FXML
    private void handleApply() {
        String name = this.nameTxtFld.getText();
        if (this.nameOfSuppliers.contains(name)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            StageUtils.styleStage(stage);
            alert.setContentText("There already exist a Supplier with the name " + name);
            alert.showAndWait();
        } else {
            this.result = new Supplier(name, URI.create(this.orderWebPageTxtFld.getText()));
            this.handleCancel();
        }
    }

    /**
     * Closes the window
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }
}
