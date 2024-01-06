package gui.saleBookController;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.*;
import gui.saleBookController.pages.ordersPage.OrdersPage;
import gui.saleBookController.pages.positionsPage.PositionsPage;
import gui.saleBookController.pages.profitAndLossAccountPage.ProfitAndLossAccountPage;
import gui.saleBookController.pages.sparePartsPage.SparePartsPage;
import gui.saleBookController.pages.suppliersPage.SuppliersPage;
import gui.saleBookController.pages.tenthPartPage.TenthPartPage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.GUIConnector;
import logic.saleBook.SaleBook;
import logic.saleBook.SaleBookData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static gui.CustomSplitMenuButton.SplitMode.SPLIT_BOTTOM;
import static gui.DialogWindow.*;
import static gui.Images.*;
import static gui.util.StageUtils.createStyledStage;


/**
 * MasterController of the graphical user interface.
 *
 * @author xthewhitelionx
 */
public class SaleBookController implements Initializable {

    public static final String THEMES_DIR_PATH = "/gui/css/themes";
    /**
     * Label to display the current status
     */
    @FXML
    private Label status;

    /**
     *
     */
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Ribbon ribbonBand;
    @FXML
    public RibbonGroup fileRibbonGroup;

    /**
     *
     */
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    public Button openBookBtn;
    @FXML
    public Button newBookBtn;

    @FXML
    private CustomSplitMenuButton saveBtn;
    @FXML
    public ComboBox<String> themeCmbBox;
    public Button shortCutsBtn;
    public Button helpBtn;

    /**
     * The current {@link SaleBook}
     */
    private SaleBook saleBook;

    /**
     *
     */
    private PositionsPage positionsPage;

    private SuppliersPage suppliersPage;
    private OrdersPage ordersPage;
    /**
     *
     */
    private SparePartsPage sparePartsPage;
    private TenthPartPage tenthPartPage;
    private ProfitAndLossAccountPage profitAndLossAccountPage;
    /**
     * The current file
     */
    private File currFile;

    private MenuItem saveAs;


    /**
     * Initialize a new game with the specified arguments by using the
     * methode {@link #loadGameController()}
     *
     * @param involved    array of {@code boolean}s true if someone plays otherwise false
     * @param names       name of each player in an array
     * @param playerTypes typ of each player in an array
     * @param stage
     * @throws NullPointerException if involved, names or playerTypes is null
     */
    public static void initializeSaleBookController() {
        SaleBookController controller = loadSaleBookController("newBook.json");
        if (controller != null) {
            controller.handleNewBook();
        }
    }

    /**
     * Initialize a new game with the specified arguments by using the
     * methode {@link #loadGameController()}
     *
     * @param involved    array of {@code boolean}s true if someone plays otherwise false
     * @param names       name of each player in an array
     * @param playerTypes typ of each player in an array
     * @throws NullPointerException if involved, names or playerTypes is null
     */
    /**
     * @param selectedFile
     * @throws IOException
     */
    public static void initializeSaleBookController(@NotNull File selectedFile) throws IOException {
        SaleBookController controller = loadSaleBookController(selectedFile.getName());
        if (controller != null) {
            SaleBookData sealBookData = SaleBookData.fromJson(selectedFile, progress -> {
            });
            if (sealBookData != null) {
                controller.setSaleBook(selectedFile, new SaleBook(sealBookData, controller.createJavaFXGUI()));
            }
        }
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO 06.01.2024 files not found
        URL themesDirUrl = JarMain.class.getResource("css/themes");
        if (themesDirUrl != null) {
            File cssDir = new File(themesDirUrl.toExternalForm());
            File[] files = cssDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    String name = file.getName().replace(".css", "");
                    this.themeCmbBox.getItems().add(name);
                }
            }
        }

        this.themeCmbBox.setValue(Config.getTheme());
        this.themeCmbBox.getSelectionModel().selectedItemProperty().addListener((ov, oldVal, newVal) -> {
            Config.setTheme(newVal);
            this.progressBar.getScene().getStylesheets().setAll(JarMain.class.getResource(
                    "css/themes/%s.css".formatted(newVal)).toExternalForm());
        });
        try {
            this.sparePartsPage = SparePartsPage.createSparePartsPage();
        } catch (IOException e) {
            displayError("failed to load sparePartPage", e);
        }
        try {
            this.suppliersPage = SuppliersPage.createSuppliersPage();
        } catch (IOException e) {
            displayError("failed to load suppliersPage", e);
        }
        try {
            this.ordersPage = OrdersPage.createOrderPage();
        } catch (IOException e) {
            displayError("fail to load orderPage", e);
        }
        try {
            this.positionsPage = PositionsPage.createPositionsPage();
        } catch (IOException e) {
            displayError("failed to load positionPage", e);
        }
        try {
            this.tenthPartPage = TenthPartPage.createTenthPartPage();
        } catch (IOException e) {
            displayError("failed to load tenthPartPage", e);
        }
        try {
            this.profitAndLossAccountPage = ProfitAndLossAccountPage.createProfitAndLossAccountPage();
        } catch (IOException e) {
            displayError("failed to load profitAndLossAccountPage", e);
        }
        this.initializeRibbonBand();
        this.hideInfobox();
    }

    /**
     * closes the current stage/window and creates a new {@link SaleBookController}
     * also it hands over the chosen file
     *
     * @param fileName the chosen file which should be
     *                     transmitted to the created UserInterfaceController
     */
    private static @Nullable SaleBookController loadSaleBookController(String fileName) {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/SaleBookController.fxml"));

        try {
            Scene scene = new Scene(loader.load());
            Stage stage = createStyledStage(scene);
            stage.setMaximized(true);
            stage.setTitle(fileName);
            stage.setMinWidth(1250D);
            stage.setMinHeight(900D);
            stage.initModality(Modality.WINDOW_MODAL);
            SaleBookController controller = loader.getController();
            controller.initializeShortCuts(scene);
            stage.show();
            return controller;
        } catch (IOException e) {
            displayError(e);
        }
        return null;
    }

    private void setSaleBook(File currFile, SaleBook saleBook) {
        this.currFile = currFile;
        this.saleBook = saleBook;
        this.sparePartsPage.setSaleBook(saleBook);
        this.positionsPage.setSaleBook(saleBook);
        this.tenthPartPage.setSaleBook(saleBook);
        this.profitAndLossAccountPage.setSaleBook(saleBook);
        this.suppliersPage.setSaleBook(saleBook);
        this.ordersPage.setSaleBook(saleBook);

        Stage stage = (Stage) this.progressBar.getScene().getWindow();
        stage.setTitle(currFile.getName());
    }

    /**
     *
     */
    private void initializeShortCuts(Scene scene) {
        ObservableMap<KeyCombination, Runnable> accelerators = scene.getAccelerators();
        accelerators.put(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN),
                () -> {
                    int length = this.themeCmbBox.getItems().size();
                    int index = this.themeCmbBox.getSelectionModel().selectedIndexProperty().get();
                    this.themeCmbBox.getSelectionModel().select(++index % length);
                });
        accelerators.put(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
                this.openBookBtn::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN),
                this.newBookBtn::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                this.saveBtn.getButton()::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,
                KeyCombination.SHIFT_DOWN), this.saveAs::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.F1), this.helpBtn::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN), () -> {
            Stage stage = (Stage) scene.getWindow();
            stage.setMaximized(true);
        });
    }

    /**
     * Creates a new {@link JavaFXGUI} with the fields of this class
     *
     * @return new JavaFXGUI with the data fields of this class
     */
    private GUIConnector createJavaFXGUI() {
        return new JavaFXGUI(this.positionsPage, this.sparePartsPage,
                this.positionsPage.getTotalPerformanceLbl(), this.tenthPartPage,
                this.profitAndLossAccountPage, this.suppliersPage, this.ordersPage, this.status);
    }

    /**
     *
     */
    private void initializeRibbonBand() {
        this.saveAs = new MenuItem("Save As");
        this.saveAs.setGraphic(createImageView(SAVE_AS_IMAGE, 16));
        this.saveAs.setOnAction(actionEvent -> this.handleSaveBookAs());

        this.saveBtn = new CustomSplitMenuButton("Save", SPLIT_BOTTOM, this.saveAs);
        this.saveBtn.setGraphic(new ImageView(SAVE_IMAGE));
        this.saveBtn.setOnAction(actionEvent -> this.handleSaveBook());
        this.fileRibbonGroup.getNodes().add(this.saveBtn);

        this.shortCutsBtn.setOnAction(actionEvent -> {
            try {
                ShortcutsController.load();
            } catch (IOException e) {
                displayError("failed to load shortcut.fxml", e);
            }
        });


        RibbonTab positionTab = this.positionsPage.getRibbonTab();
        positionTab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                SaleBookController.this.mainBorderPane.setCenter(SaleBookController.this.positionsPage.getPane());
            }
        });
        RibbonTab suppliersTab = this.suppliersPage.getRibbonTab();
        suppliersTab.selectedProperty().addListener(((observableValue, aBoolean, t1) -> {
            if (t1) {
                this.mainBorderPane.setCenter(this.suppliersPage.getPane());
            }
        }));
        RibbonTab sparePartsTab = this.sparePartsPage.getRibbonTab();
        sparePartsTab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                this.mainBorderPane.setCenter(this.sparePartsPage.getPane());
            }
        });
        RibbonTab tenthPartTab = this.tenthPartPage.getRibbonTab();
        tenthPartTab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                this.mainBorderPane.setCenter(this.tenthPartPage.getPane());
            }
        });
        RibbonTab profitAndLossAccountPageRibbonTab = this.profitAndLossAccountPage.getRibbonTab();
        profitAndLossAccountPageRibbonTab.selectedProperty().addListener(((observableValue,
                                                                           aBoolean, t1) -> {
            if (t1) {
                this.mainBorderPane.setCenter(this.profitAndLossAccountPage.getPane());
            }
        }));
        RibbonTab orderPageRibbonTab = this.ordersPage.getRibbonTab();
        orderPageRibbonTab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                this.mainBorderPane.setCenter(this.ordersPage.getPane());
            }
        });

        this.ribbonBand.getTabs().addAll(orderPageRibbonTab, positionTab, profitAndLossAccountPageRibbonTab,
                sparePartsTab, suppliersTab, tenthPartTab);
        this.ribbonBand.setSelectedRibbonTab(positionTab);
    }

    /**
     *
     */
    //TODO JavaDoc
    private void hideInfobox() {
        this.status.setText("");
        this.status.setVisible(false);
        this.progressBar.setVisible(false);
    }

    /**
     * @param file
     * @return
     */
    private void save(@NotNull File file) {
        Task<Void> saveTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    SaleBookData saleBookData = new SaleBookData(SaleBookController.this.saleBook);
                    saleBookData.toJson(file, totalBytes -> this.updateProgress(totalBytes, file.length()));
                } catch (IOException e) {
                    displayError(e);
                }
                return null;
            }
        };
        this.saleBook.updateStatus(Message.saved.formatMessage(file.getName()));
        this.progressBar.setVisible(true);
        this.progressBar.progressProperty().bind(saveTask.progressProperty());
        saveTask.setOnSucceeded(workerStateEvent -> {
            this.hideInfobox();
        });
        saveTask.run();
    }

    /**
     * Handles the "new" button and creates a
     * new file and {@link SaleBook}
     */
    @FXML
    private void handleNewBook() {
        this.setSaleBook(new File(DIRECTORY + "/newBook.json"), new SaleBook(this.createJavaFXGUI()));
        this.hideInfobox();
    }

    /**
     * Handles the "open" button.
     */
    @FXML
    private void handleOpenBook() {
        FileChooser fileChooser = createFileChooser();
        fileChooser.setTitle("Open JSON Graph-File");
        File selectedFile = fileChooser.showOpenDialog(this.progressBar.getScene().getWindow());
        if (selectedFile != null) {
            Task<SaleBookData> loadTask = new Task<>() {
                @Override
                protected SaleBookData call() throws Exception {
                    return SaleBookData.fromJson(selectedFile,
                            totalBytes -> this.updateProgress(totalBytes, selectedFile.length()));
                }
            };
            this.saleBook.updateStatus(Message.loaded.formatMessage(selectedFile.getName()));
            this.progressBar.setVisible(true);
            this.progressBar.progressProperty().bind(loadTask.progressProperty());
            loadTask.setOnSucceeded(workerStateEvent -> {
                this.hideInfobox();
            });
            loadTask.run();
            try {
                this.setSaleBook(selectedFile, new SaleBook(loadTask.get(), this.createJavaFXGUI()));
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the "save" button and
     * saves the current {@link SaleBook}.
     */
    @FXML
    private void handleSaveBook() {
        this.save(this.currFile);
    }

    /**
     * Handles the "save as" button and opens a save dialog
     */
    @FXML
    private void handleSaveBookAs() {
        FileChooser fileChooser = createFileChooser();
        File selectedFile = fileChooser.showSaveDialog(this.progressBar.getScene().getWindow());
        if (selectedFile != null) {
            this.save(selectedFile);
            Stage stage = (Stage) this.progressBar.getScene().getWindow();
            stage.setTitle(selectedFile.getName());
        }
    }

    /**
     * Handles the "exit" button.
     */
    @FXML
    private void handleExit() {
        //TODO AutoSave?
//        if (autoSave.isSelected()) {
//            handleSaveBook();
//        }
        Stage stage = (Stage) this.progressBar.getScene().getWindow();
        stage.close();
    }
}