package gui;

import gui.FXutils.StageUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static gui.DialogWindow.displayError;

/**
 * This class contains the main.
 *
 * @author xthe_white_lionx
 */
public class ApplicationMain extends Application {

    /**
     * Width of the start scene
     */
    private static final double WIDTH = 300D;

    /**
     * Height of the start scene
     */
    private static final double HEIGHT = 300D;

    /**
     * Creating the stage and showing it. This is where the initial size and the
     * title of the window are set.
     *
     * @param primaryStage the stage to be shown
     */
    @Override
    public void start(@NotNull Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource(
                    "Start.fxml"));
            Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);
            primaryStage.setScene(scene);
            StageUtils.styleStage(primaryStage);
            primaryStage.setTitle("welcome");
            primaryStage.setMinWidth(WIDTH);
            primaryStage.setMinHeight(HEIGHT);
            primaryStage.show();
        } catch (IOException e) {
            displayError("failed to load start.fxml",e);
        }
    }

    /**
     * Main method
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}
