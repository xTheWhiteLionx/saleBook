package gui;

import gui.util.StageUtils;
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
     * Creating the stage and showing it. This is where the initial size and the
     * title of the window are set.
     *
     * @param primaryStage the stage to be shown
     */
    @Override
    public void start(@NotNull Stage primaryStage) {
        double width = 300D;
        double height = 300D;

        FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource("LoginController" +
                ".fxml"));

        try {
            Scene scene = new Scene(loader.load(), width, height);
            primaryStage.setScene(scene);
            StageUtils.styleScene(scene);
            primaryStage.setTitle("welcome");
            primaryStage.setMinWidth(width);
            primaryStage.setMinHeight(height);
            primaryStage.show();
        } catch (IOException e) {
            displayError("failed to load loginController",e);
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
