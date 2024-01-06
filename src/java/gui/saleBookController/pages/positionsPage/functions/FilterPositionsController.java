package gui.saleBookController.pages.positionsPage.functions;

import gui.ApplicationMain;
import gui.ObservableTreeItemMapBinder;
import gui.util.ChoiceBoxUtils;
import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Quarter;
import logic.products.Product;
import logic.products.position.Position;
import logic.products.position.State;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static gui.util.StageUtils.createStyledStage;
import static logic.Quarter.getQuarterOfMonth;

public class FilterPositionsController implements Initializable {
    @FXML
    private ChoiceBox<State> statusChoiceBox;
    @FXML
    private ChoiceBox<String> categoryChcBx;
    @FXML
    private ChoiceBox<Month> monthChcBox;
    @FXML
    private RadioButton rdBtnFilterMonth;
    @FXML
    private ChoiceBox<Quarter> quarterChcBox;
    @FXML
    private RadioButton rdBtnFilterQuarter;
    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private CheckBox yearCheckBox;
    @FXML
    private Button applyBtn;
    @FXML
    private Button resetBtn;

    private Stage stage;
    private ObservableTreeItemMapBinder<Integer> root;

    public static FilterPositionsController CreateFilterPositionsController(ObservableTreeItemMapBinder<Integer> root) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/" +
                        "functions/FilterPositionsController.fxml"));

        Stage stage = createStyledStage(new Scene(loader.load()));
        stage.setTitle("filter");
        stage.setMinWidth(350D);
        stage.setMinHeight(200D);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        FilterPositionsController controller = loader.getController();
        controller.setStage(stage);
        controller.setRoot(root);
        return controller;
    }

    private void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setRoot(ObservableTreeItemMapBinder<Integer> root) {
        this.root = root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate currentDate = LocalDate.now();
        Month currMonth = currentDate.getMonth();
        int currYear = currentDate.getYear();
        this.yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                currYear, currYear));

        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            this.applyBtn.setDisable(newValue == null);
            this.resetBtn.setDisable(newValue == null);
        };

        this.statusChoiceBox.getItems().addAll(State.values());
        this.statusChoiceBox.valueProperty().addListener(FieldValidityListener);
        this.categoryChcBx.valueProperty().addListener(FieldValidityListener);

        ChangeListener<Object> FieldValidityListener2 = (observable, oldValue, newValue) -> {
            this.applyBtn.setDisable(newValue == null && !this.yearCheckBox.isSelected());
            this.resetBtn.setDisable(newValue == null && !this.yearCheckBox.isSelected());
        };

        this.monthChcBox.getItems().addAll(Month.values());
        this.monthChcBox.valueProperty().addListener(FieldValidityListener2);

        this.quarterChcBox.getItems().addAll(Quarter.values());
        this.quarterChcBox.valueProperty().addListener(FieldValidityListener2);

        this.rdBtnFilterMonth.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (this.rdBtnFilterMonth.isSelected()) {
                this.yearCheckBox.setSelected(true);
                //sets the current month
                this.monthChcBox.setValue(currMonth);
                this.rdBtnFilterQuarter.setSelected(false);
            } else {
                this.monthChcBox.setValue(null);
            }
            this.monthChcBox.setDisable(!this.rdBtnFilterMonth.isSelected());
        });

        this.rdBtnFilterQuarter.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (this.rdBtnFilterQuarter.isSelected()) {
                this.yearCheckBox.setSelected(true);
                //sets the current quarter by the current month
                this.quarterChcBox.setValue(getQuarterOfMonth(currMonth));
                this.rdBtnFilterMonth.setSelected(false);
            } else {
                this.quarterChcBox.setValue(null);
            }
            this.quarterChcBox.setDisable(!this.rdBtnFilterQuarter.isSelected());
        });

        this.yearCheckBox.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
            if (!this.yearCheckBox.isSelected()) {
                this.rdBtnFilterMonth.setSelected(false);
                this.rdBtnFilterQuarter.setSelected(false);
            }
            this.yearSpinner.getValueFactory().setValue(isSelected ? currYear : 0);
            this.yearSpinner.setDisable(!isSelected);

            this.applyBtn.setDisable(!isSelected);
            this.resetBtn.setDisable(!isSelected);
        });
        this.cleanFilter();
    }

    public void showAndWait(){
        this.stage.showAndWait();
    }

    public void setCategories(Collection<String> categories) {
        String currentValue = this.categoryChcBx.getValue();
        ChoiceBoxUtils.setItems(this.categoryChcBx, categories);
        this.categoryChcBx.setValue(currentValue);
    }

    /**
     * Handles the "apply" filter Button
     */
    @FXML
    public void handleApply() {
        Predicate<Position> predicate = position -> true;
        if (this.categoryChcBx.getValue() != null) {
            predicate = predicate.and(x -> x.getCategory().equalsIgnoreCase(this.categoryChcBx.getValue()));
        }
        if (this.statusChoiceBox.getValue() != null) {
            predicate = predicate.and(x -> x.getState() == this.statusChoiceBox.getValue());
        }

        if (this.yearSpinner.getValue() > 0) {
            predicate = predicate.and(x -> x.getOrderDate().getYear() == this.yearSpinner.getValue());
            if (this.monthChcBox.getValue() != null) {
                predicate = predicate.and(
                        x -> x.getOrderDate().getMonth().equals(this.monthChcBox.getValue())
                );
            } else if (this.quarterChcBox.getValue() != null) {
                predicate = predicate.and(
                        x -> this.quarterChcBox.getValue().contains(x.getOrderDate().getMonth())
                );
            }
        }
        this.root.setFilter(predicate);
        this.handleCancel();
    }

    /**
     * Cleans the options of the filter items to their default values
     */
    private void cleanFilter() {
        this.categoryChcBx.setValue(null);
        this.statusChoiceBox.setValue(null);
        this.monthChcBox.setValue(null);
        this.quarterChcBox.setValue(null);
        this.yearSpinner.getValueFactory().setValue(0);

        this.rdBtnFilterMonth.setSelected(false);
        this.rdBtnFilterQuarter.setSelected(false);
        this.yearCheckBox.setSelected(false);
        this.monthChcBox.setDisable(true);
        this.quarterChcBox.setDisable(true);
        this.yearSpinner.setDisable(true);

        this.applyBtn.setDisable(true);
        this.resetBtn.setDisable(true);
    }

    /**
     * Handles the "reset" filter Button and
     * displays the default investments
     */
    @FXML
    private void handleResetFilter() {
        this.cleanFilter();
        this.applyBtn.setDisable(false);
    }

    public void handleCancel() {
        this.stage.close();
    }
}
