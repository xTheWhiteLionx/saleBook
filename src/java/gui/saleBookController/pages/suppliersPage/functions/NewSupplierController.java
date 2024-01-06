package gui.saleBookController.pages.suppliersPage.functions;

import gui.ApplicationMain;
import gui.saleBookController.pages.FunctionDialog;
import gui.util.StageUtils;
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
import logic.order.Supplier;
import logic.utils.URLUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DialogWindow.displayError;
import static gui.util.StageUtils.createStyledStage;

public class NewSupplierController extends FunctionDialog<Supplier> implements Initializable {

    @FXML
    private TextField nameTxtFld;
    @FXML
    private TextField orderWebPageTxtFld;
    @FXML
    private Button applyBtn;

    private Set<String> nameOfSuppliers;

    public static @NotNull NewSupplierController createSupplierController(Set<String> nameOfSuppliers) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/suppliersPage/" +
                        "functions/NewSupplierController.fxml"));
        Stage newStage = createStyledStage(new Scene(loader.load()));
        newStage.setTitle("new supplier");
        newStage.initModality(Modality.APPLICATION_MODAL);
        NewSupplierController controller = loader.getController();
        controller.setNameOfSuppliers(nameOfSuppliers);
        newStage.showAndWait();
        return controller;
    }

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

    @FXML
    private void handleApply() {
        String name = this.nameTxtFld.getText();
        if (this.nameOfSuppliers.contains(name)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Scene scene = alert.getDialogPane().getScene();
            StageUtils.styleScene(scene);
            alert.setContentText("There already exist a Supplier with the name " + name);
            alert.showAndWait();
        } else {
            this.result = new Supplier(name, URI.create(this.orderWebPageTxtFld.getText()));
            this.handleCancel();
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) this.applyBtn.getScene().getWindow();
        stage.close();
    }

    private void setNameOfSuppliers(Set<String> nameOfSuppliers) {
        this.nameOfSuppliers = nameOfSuppliers;
    }
}
