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
 * Controller for editing the specified supplier
 *
 * @see gui.saleBookController.pages.FunctionDialog
 * @author xthe_white_lionx
 */
public class EditSupplierController extends FunctionDialog<Boolean> implements Initializable {

    /**
     * TextField to edit the name of the current supplier
     */
    @FXML
    private TextField nameTxtFld;

    /**
     * TextField to edit the orderWebPage of the current supplier
     */
    @FXML
    private TextField orderWebPageTxtFld;

    /**
     * Button to apply the editing of the current supplier
     */
    @FXML
    private Button applyBtn;

    /**
     * The current supplier which should be edited
     */
    private Supplier supplier;

    /**
     * Name of the suppliers of the saleBook
     */
    private Set<String> nameOfSuppliers;

    /**
     * Creates and loads a new EditSupplierController
     *
     * @param supplier the supplier which should be edited
     * @param nameOfSuppliers names of the already existing suppliers
     * @return the new created EditSupplierController
     * @throws IOException if the fxml cannot be loaded
     */
    public static @NotNull EditSupplierController createEditSupplierController(
            @NotNull Supplier supplier, @NotNull Set<String> nameOfSuppliers) throws IOException {
            FXMLLoader loader = new FXMLLoader(
                    ApplicationMain.class.getResource("saleBookController/pages/suppliersPage/" +
                            "functions/EditSupplierController.fxml"));
        Stage newStage = createStyledStage(new Scene(loader.load()));
        newStage.setTitle("edit supplier");
        newStage.initModality(Modality.APPLICATION_MODAL);
        EditSupplierController controller = loader.getController();
        controller.setSuppliers(supplier);
        controller.nameOfSuppliers = nameOfSuppliers;
        newStage.showAndWait();
        return controller;
    }

    /**
     * Initializes this EditSupplierController
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BooleanBinding hasInvalidInput = new BooleanBinding() {
            {
                this.bind(EditSupplierController.this.nameTxtFld.textProperty(),
                        EditSupplierController.this.orderWebPageTxtFld.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return EditSupplierController.this.nameTxtFld.getText().isEmpty() &&
                        URLUtils.isValidURL(EditSupplierController.this.orderWebPageTxtFld.getText());
            }
        };
        this.applyBtn.disableProperty().bind(hasInvalidInput);
    }

    /**
     * Handles the press of the apply button and sets the result
     */
    @FXML
    public void handleApply() {
        String name = this.nameTxtFld.getText();
        if (this.nameOfSuppliers.contains(name)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            StageUtils.styleStage(stage);
            alert.setContentText("There already exist a Supplier with the name " + name);
            alert.showAndWait();
        } else {
            this.supplier.setName(name);
            this.supplier.setOrderWebpage(URI.create(this.orderWebPageTxtFld.getText()));
            this.result = true;
            this.handleCancel();
        }
    }

    /**
     * Closes the window
     */
    @FXML
    public void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the supplier which should be edited
     *
     * @param supplier the supplier which should be edited
     */
    private void setSuppliers(@NotNull Supplier supplier) {
        this.supplier = supplier;

        this.nameTxtFld.setText(supplier.getName());
        this.orderWebPageTxtFld.setText(supplier.getOrderWebpage().toString());
    }
}
