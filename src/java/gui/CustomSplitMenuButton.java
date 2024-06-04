package gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

//TODO 08.01.2024 JavaDoc
public class CustomSplitMenuButton extends Group {

    private final ButtonBase label;
    private final ButtonBase arrowButton;
    protected ContextMenu popup;
    private ObjectProperty<SplitMode> splitMode;
    private DoubleProperty sizeBinding;
    private DoubleProperty layoutBinding;
    private double oldSizeValue;
    private double oldLayoutValue;
    private PseudoClass layoutClass;

    //
    private static final PseudoClass TOP_PSEUDO_CLASS = PseudoClass.getPseudoClass("top");
    private static final PseudoClass BOTTOM_PSEUDO_CLASS = PseudoClass.getPseudoClass("bottom");
    private static final PseudoClass LEFT_PSEUDO_CLASS = PseudoClass.getPseudoClass("left");
    private static final PseudoClass RIGHT_PSEUDO_CLASS = PseudoClass.getPseudoClass("right");
    private static final PseudoClass HIDDEN_PSEUDO_CLASS = PseudoClass.getPseudoClass("hidden");

    public enum SplitMode {
        SPLIT_TOP, // put arrow buton on top
        SPLIT_RIGHT, // put arrow button on right
        SPLIT_BOTTOM, // bottom
        SPLIT_LEFT, // left
        HIDDEN  // hides the arrow button regardless of visibility
    }

    private void changeToPseudoClass(PseudoClass newClass) {
        this.pseudoClassStateChanged(this.layoutClass, false);
        this.pseudoClassStateChanged(newClass, true);
        this.layoutClass = newClass;
    }

    private void bindHidden() {
        if (this.sizeBinding != null) {
            this.sizeBinding.unbind();
            this.sizeBinding.set(this.oldSizeValue);
        }
        if (this.layoutBinding != null) {
            this.layoutBinding.unbind();
            this.layoutBinding.set(this.oldLayoutValue);
        }
        this.arrowButton.setVisible(false);
        this.changeToPseudoClass(HIDDEN_PSEUDO_CLASS);
    }

    private void bindSizeAndLayout(DoubleProperty sizeFrom, ReadOnlyDoubleProperty sizeTo,
                                   DoubleProperty layoutFrom, ReadOnlyDoubleProperty layoutTo,
                                   PseudoClass newPseudoClass) {

        if (this.sizeBinding != null) {
            this.sizeBinding.unbind();
            this.sizeBinding.set(this.oldSizeValue);
        }
        if (this.layoutBinding != null) {
            this.layoutBinding.unbind();
            this.layoutBinding.set(this.oldLayoutValue);
        }
        this.oldSizeValue = sizeFrom.get();
        this.sizeBinding = sizeFrom;
        this.oldLayoutValue = layoutFrom.get();
        this.layoutBinding = layoutFrom;
        sizeFrom.bind(sizeTo);
        layoutFrom.bind(layoutTo);
        this.changeToPseudoClass(newPseudoClass);
        this.arrowButton.setVisible(true);
    }

    public void setGraphic(ImageView imageView) {
        this.label.setGraphic(imageView);
    }

    public void setOnAction(EventHandler<ActionEvent> actionEvent) {
        this.label.setOnAction(actionEvent);
    }

    public void setSplitMode(SplitMode mode) {
        if (this.splitMode == null) {
            this.splitMode = new SimpleObjectProperty<>();
        }
        if (this.splitMode.get() == mode) {
            return;
        } // no changes needed
        this.splitMode.set(mode);

        // set up new bindings
        switch (mode) {
            case SPLIT_BOTTOM:
                // bind arrowbutton width to label width
                // bind arrowbutton starting position to bottom of label
                this.bindSizeAndLayout(this.arrowButton.prefWidthProperty(), this.label.widthProperty(),
                        this.arrowButton.layoutYProperty(), this.label.heightProperty(),
                        BOTTOM_PSEUDO_CLASS);
                break;
            case SPLIT_RIGHT:
                // bind arrowbutton height to label height
                this.bindSizeAndLayout(this.arrowButton.prefHeightProperty(), this.label.heightProperty(),
                        this.arrowButton.layoutXProperty(), this.label.widthProperty(),
                        RIGHT_PSEUDO_CLASS);
                break;
            case SPLIT_LEFT:
                // bind arrowbutton height to label height
                this.bindSizeAndLayout(this.arrowButton.prefHeightProperty(), this.label.heightProperty(),
                        this.label.layoutXProperty(), this.arrowButton.widthProperty(),
                        LEFT_PSEUDO_CLASS);
                break;
            case SPLIT_TOP:
                // bind arrowbutton width to label height
                this.bindSizeAndLayout(this.arrowButton.prefWidthProperty(), this.label.widthProperty(),
                        this.label.layoutYProperty(), this.arrowButton.heightProperty(),
                        TOP_PSEUDO_CLASS);
                break;
            case HIDDEN:
                // unbind all and hide button
                this.bindHidden();
                break;
        }

    }

    public SplitMode getSplitMode() {
        return (this.splitMode == null) ? SplitMode.HIDDEN : this.splitMode.get();
    }

    public ObjectProperty<SplitMode> splitModeProperty() {
        return this.splitMode;
    }

    public ButtonBase getButton() {
        return this.label;
    }

    public ButtonBase getArrowButton() {
        return this.arrowButton;
    }

    public CustomSplitMenuButton(String text, SplitMode mode, MenuItem... items) {
        this.label = new Button(text);
        this.label.getStyleClass().setAll("label");
        this.arrowButton = new Button();
        // bind the managed property to visibility.
        // we dont want to manage an invisible button.
        this.arrowButton.managedProperty().bind(this.arrowButton.visibleProperty());
        this.arrowButton.getStyleClass().setAll("arrow-button");
        this.getStyleClass().setAll("split-menu-button");
        this.getChildren().setAll(this.label, this.arrowButton);
        this.getStyleClass().add("big");
        this.setSplitMode(mode);

        this.popup = new ContextMenu(items);

        this.arrowButton.setOnAction(new EventHandler<>() {
            boolean isOpen = false;

            @Override
            public void handle(ActionEvent actionEvent) {
                this.isOpen = !this.isOpen;
                if (this.isOpen) {
                    CustomSplitMenuButton.this.popup.show(CustomSplitMenuButton.this.arrowButton, Side.BOTTOM, 0, 0);
                } else {
                    CustomSplitMenuButton.this.popup.hide();
                }
            }
        });
    }
}
