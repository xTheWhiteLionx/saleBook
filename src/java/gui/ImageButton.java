package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends Button {

    /**
     *
     * @param label
     * @param image
     * @param actionEvent
     */
    public ImageButton(String label, Image image, EventHandler<ActionEvent> actionEvent) {
        super(label, new ImageView(image));
        this.setContentDisplay(ContentDisplay.TOP);
        this.setOnAction(actionEvent);
        this.setAlignment(Pos.CENTER);
        this.setWrapText(true);
        this.getStyleClass().add("big");
    }
}
