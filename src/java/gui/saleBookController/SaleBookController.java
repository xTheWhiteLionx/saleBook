package gui.saleBookController;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import gui.*;
import gui.saleBookController.pages.Page;
import gui.saleBookController.pages.assetsPage.AssetsPage;
import gui.saleBookController.pages.ordersPage.OrdersPage;
import gui.saleBookController.pages.positionsPage.PositionsPage;
import gui.saleBookController.pages.profitAndLossAccountPage.ProfitAndLossAccountPage;
import gui.saleBookController.pages.sparePartsPage.SparePartsPage;
import gui.saleBookController.pages.suppliersPage.SuppliersPage;
import gui.saleBookController.pages.tenthPartPage.TenthPartPage;
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
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static gui.CustomSplitMenuButton.SplitMode.SPLIT_BOTTOM;
import static gui.DialogWindow.*;
import static gui.Images.*;
import static gui.FXutils.StageUtils.createStyledStage;

/**
 * Controller of the graphical user interface of the saleBook.
 *
 * @author xthe_white_lionx
 */
public class SaleBookController implements Initializable {

    /**
     *
     */
    //TODO 06.01.2024 JavaDoc
    public static final String THEMES_DIR_PATH = "gui/css/themes";

    /**
     * The name of the default file if a new saleBook wil be created
     */
    public static final String DEFAULT_FILE_NAME = "newBook.json";

    /**
     * Label to display the current status
     */
    @FXML
    private Label status;

    /**
     * ProgressBar to display the progress of a process like saving a file
     */
    @FXML
    private ProgressBar progressBar;

    /**
     * The RibbonBand of this SaleBookController
     */
    @FXML
    private Ribbon ribbonBand;

    /**
     * RibbonGroup for file controls
     */
    @FXML
    public RibbonGroup fileRibbonGroup;

    /**
     * The base pane of this SaleBookController
     */
    @FXML
    private BorderPane basePane;

    /**
     * CustomSplitMenuButton for saving options
     */
    @FXML
    private CustomSplitMenuButton saveBtn;

    /**
     * ComboBox to display/choose the theme
     */
    @FXML
    public ComboBox<String> themeCmbBox;

    /**
     * PositionsPage of this SaleBookController
     */
    private PositionsPage positionsPage;

    /**
     * SuppliersPage of this SaleBookController
     */
    private SuppliersPage suppliersPage;

    /**
     * OrdersPage of this SaleBookController
     */
    private OrdersPage ordersPage;

    /**
     *
     */
    private AssetsPage assetsPage;

    /**
     * SparePartsPage of this SaleBookController
     */
    private SparePartsPage sparePartsPage;

    /**
     * TenthPartPage of this SaleBookController
     */
    private TenthPartPage tenthPartPage;

    /**
     * ProfitAndLossAccountPage of this SaleBookController
     */
    private ProfitAndLossAccountPage profitAndLossAccountPage;

    /**
     * Map from the title of a RibbonBand to the matching Page
     */
    private Map<String, Page> ribbonBandTitleToPage;

    /**
     * The current file
     */
    private File currentFile;

    /**
     * The current SaleBook
     */
    private SaleBook saleBook;

    /**
     * Initializes a new SaleBookController and creates a new file to work on
     */
    public static void initializeSaleBookController() {
        SaleBookController controller = loadSaleBookController(DEFAULT_FILE_NAME);
        if (controller != null) {
            controller.handleNewBook();
        }
    }

    /**
     * Initializes a new SaleBookController with the data from the specified file
     *
     * @param file the file from which the data will be read
     * @throws IOException if the file cannot be found, an error occurs by reading the file
     */
    public static void initializeSaleBookController(@NotNull File file) throws IOException {
        SaleBookController controller = loadSaleBookController(file.getName());
        if (controller != null) {
            SaleBookData sealBookData = SaleBookData.fromJson(file, controller.progressBar::setProgress);
            if (sealBookData != null) {
                controller.setSaleBook(new SaleBook(sealBookData, controller.createJavaFXGUI()));
                controller.setCurrentFile(file);
            }
        }
    }

    /**
     * Creates and loads a new SaleBookController
     *
     * @param fileName the name of the current file to display as title
     */
    private static @Nullable SaleBookController loadSaleBookController(@NotNull String fileName) {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/SaleBookController.fxml"));

        try {
            Scene scene = new Scene(loader.load());
            Stage stage = createStyledStage(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(fileName);
            stage.setMinWidth(1250D);
            stage.setMinHeight(900D);
            stage.setMaximized(true);
            SaleBookController controller = loader.getController();
            controller.initializeShortCuts(scene);
            controller.initializeCloseRequestHandler(stage);
            stage.show();
            return controller;
        } catch (IOException e) {
            displayError("fail to load SaleBookController.fxml", e);
        }
        return null;
    }

    /**
     * Initializes this SaleBookController.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeThemeComboBox();
        this.initializePages();
        this.initializeRibbonBandTitleToPageMap();
        this.initializeRibbonBand();
    }

    /**
     * Handles the help button
     */
    @FXML
    public void handleHelp() {
        //TODO 23.01.2024 implement
    }

    /**
     * Opens a new ShortcutsController
     */
    @FXML
    public void handleOpenShortcuts() {
        try {
            ShortcutsController.loadShortcutsController();
        } catch (IOException e) {
            displayError("failed to load shortcut.fxml", e);
        }
    }

    /**
     * Handles the "new" button and creates a
     * new file and {@link SaleBook}
     */
    @FXML
    public void handleNewBook() {
        Stage stage = (Stage) this.progressBar.getScene().getWindow();
        stage.setTitle(DEFAULT_FILE_NAME);
        this.currentFile = null;
        this.setSaleBook(new SaleBook(this.createJavaFXGUI()));
        this.hideInfobox();
    }

    /**
     * Opens the selected saleBook file
     */
    @FXML
    public void handleOpenBook() {
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
            this.saleBook.updateStatus(String.format("%s successfully loaded", selectedFile.getName()));
            this.progressBar.setVisible(true);
            this.progressBar.progressProperty().bind(loadTask.progressProperty());
            loadTask.setOnSucceeded(workerStateEvent -> {
                this.hideInfobox();
            });
            loadTask.run();
            try {
                this.setSaleBook(new SaleBook(loadTask.get(), this.createJavaFXGUI()));
                this.setCurrentFile(selectedFile);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the "save" button and
     * saves the current {@link SaleBook} in the current file.
     */
    @FXML
    public void handleSaveBook() {
        if (this.currentFile == null) {
            this.handleSaveBookAs();
        } else {
            this.save(this.currentFile);
        }
    }

    /**
     * Handles the "save as" button and opens a save as dialog
     */
    @FXML
    public void handleSaveBookAs() {
        FileChooser fileChooser = createFileChooser();
        if (this.currentFile == null) {
            fileChooser.setInitialFileName(DEFAULT_FILE_NAME);
        }
        File selectedFile = fileChooser.showSaveDialog(this.progressBar.getScene().getWindow());
        if (selectedFile != null) {
            this.save(selectedFile);
            this.setCurrentFile(selectedFile);
        }
    }

    /**
     * Stets the current file to the specified file and
     * resets the title of the stage to the filename
     *
     * @param file the new file
     */
    private void setCurrentFile(@NotNull File file) {
        this.currentFile = file;
        Stage stage = (Stage) this.progressBar.getScene().getWindow();
        stage.setTitle(file.getName());
    }

    /**
     * Initializes the themeComboBox of this SaleBookController
     */
    private void initializeThemeComboBox() {
        //this.themeCmbBox.getItems().addAll("Darkmode","Lightmode");
        this.themeCmbBox.setValue(Config.getTheme());
        this.themeCmbBox.getSelectionModel().selectedItemProperty().addListener(
                (ov, oldTheme, newTheme) -> {
                    Config.setTheme(newTheme);
                    this.progressBar.getScene().getStylesheets().setAll(JarMain.class.getResource(
                            "css/themes/%s.css".formatted(newTheme)).toExternalForm());
                });
    }

    /**
     * Initializes the pages of this SaleBookController
     */
    private void initializePages() {
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
            this.assetsPage = AssetsPage.createAssetsPage();
        } catch (IOException e) {
            displayError("failed to load assetPage", e);
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
    }

    /**
     * Sets the saleBook to the specified saleBook
     *
     * @param saleBook the new saleBook
     */
    private void setSaleBook(@NotNull SaleBook saleBook) {
        this.saleBook = saleBook;
        Collection<Page> pages = this.ribbonBandTitleToPage.values();
        for (Page page : pages) {
            page.setSaleBook(saleBook);
        }
    }

    /**
     *
     */
    private void initializeCloseRequestHandler(@NotNull Stage stage) {
        stage.setOnCloseRequest(windowEvent -> {
            //TODO 27.01.2024 add predicate for existing file but not saved yet
            if (this.currentFile == null){
                windowEvent.consume();
                boolean close = DialogWindow.unsavedDataAlert();
                if (close) {
                    stage.close();
                }
            }
        });
    }

    /**
     *
     */
    private void initializeShortCuts(@NotNull Scene scene) {
        ObservableMap<KeyCombination, Runnable> accelerators = scene.getAccelerators();
        accelerators.put(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN),
                this::handleNextTheme);
        accelerators.put(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
                this::handleOpenBook);
        accelerators.put(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN),
                this::handleNewBook);
        accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                this::handleSaveBook);
        accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,
                KeyCombination.SHIFT_DOWN), this::handleSaveBookAs);
        accelerators.put(new KeyCodeCombination(KeyCode.F1), this::handleHelp);
        accelerators.put(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN), () -> {
            Stage stage = (Stage) scene.getWindow();
            stage.setMaximized(true);
        });
    }

    /**
     * Handles the next theme
     */
    private void handleNextTheme() {
        int length = this.themeCmbBox.getItems().size();
        int index = this.themeCmbBox.getSelectionModel().selectedIndexProperty().get();
        this.themeCmbBox.getSelectionModel().select(++index % length);
    }

    /**
     *
     */
    private void initializeRibbonBand() {
        /**
         *
         */
        MenuItem saveAs = new MenuItem("Save As ");
        saveAs.setGraphic(createImageView(SAVE_AS_IMAGE, 16));
        saveAs.setOnAction(actionEvent -> this.handleSaveBookAs());

        this.saveBtn = new CustomSplitMenuButton("Save", SPLIT_BOTTOM, saveAs);
        this.saveBtn.setGraphic(new ImageView(SAVE_IMAGE));
        this.saveBtn.setOnAction(actionEvent -> this.handleSaveBook());
        this.fileRibbonGroup.getNodes().add(this.saveBtn);

        this.ribbonBand.selectedRibbonTabProperty().addListener((observableValue, oldSimpleObject, newSimpleObject) -> {
            RibbonTab ribbonTab = ((RibbonTab) newSimpleObject);
            Page page = this.ribbonBandTitleToPage.get(ribbonTab.getText());
            if (page != null) {
                this.basePane.setCenter(page.getBasePane());
            }
        });
    }

    /**
     * Initializes the RibbonBandTitleToPage map of this SaleBookController
     */
    private void initializeRibbonBandTitleToPageMap() {
        this.ribbonBandTitleToPage = new HashMap<>();
        this.addRibbonBandTitleToPage(this.assetsPage);
        this.addRibbonBandTitleToPage(this.ordersPage);
        this.addRibbonBandTitleToPage(this.positionsPage);
        this.addRibbonBandTitleToPage(this.profitAndLossAccountPage);
        this.addRibbonBandTitleToPage(this.sparePartsPage);
        this.addRibbonBandTitleToPage(this.suppliersPage);
        this.addRibbonBandTitleToPage(this.tenthPartPage);
    }

    /**
     * Creates a new {@link JavaFXGUI} with the fields of this class
     *
     * @return new JavaFXGUI with the data fields of this class
     */
    private GUIConnector createJavaFXGUI() {
        return new JavaFXGUI(this.positionsPage, this.sparePartsPage,
                this.positionsPage.getTotalPerformanceLbl(), this.tenthPartPage,
                this.profitAndLossAccountPage, this.suppliersPage, this.ordersPage,
                this.assetsPage, this.status);
    }

    /**
     * Adds the title of the ribbonTab of the specified page to the {@link #ribbonBandTitleToPage} map and
     * to the ribbonBand
     *
     * @param page to get the ribbonTab from
     */
    private void addRibbonBandTitleToPage(@NotNull Page page) {
        RibbonTab ribbonTab = page.getRibbonTab();
        this.ribbonBandTitleToPage.put(ribbonTab.getText(), page);
        this.ribbonBand.getTabs().add(ribbonTab);
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
        this.progressBar.setVisible(true);
        this.progressBar.progressProperty().bind(saveTask.progressProperty());
        saveTask.setOnSucceeded(workerStateEvent -> {
            this.hideInfobox();
            this.saleBook.updateStatus(String.format("%s successfully saved", file.getName()));
        });
        saveTask.run();
    }
}