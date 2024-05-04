package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

/**
 * A special type of Button that has an image and displays the image above
 * the text
 *
 * @author xthe_white_lionx
 */
public class ImageButton extends Button {

    /**
     * Creates a new ImageButton
     *
     * @param text the new text of the button
     * @param image the graphic of the button
     * @param actionEvent event that should happen if the button is on action
     */
    public ImageButton(@NotNull String text, @NotNull Image image,
                       @NotNull EventHandler<ActionEvent> actionEvent) {
        super(text, new ImageView(image));
        this.setContentDisplay(ContentDisplay.TOP);
        this.setOnAction(actionEvent);
        this.setAlignment(Pos.CENTER);
        this.setWrapText(true);
        this.getStyleClass().add("big");
    }
}
