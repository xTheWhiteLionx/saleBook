package costumeClasses.FXClasses;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;

/**
 * This class represents a customSplitMenuButton its split mode can be
 * adjusted.
 *
 * @author xThe_white_Lionx
 * @Date 17.06.2024
 */
public class CustomSplitMenuButton extends Group {
    /**
     * the label of this custom split menu button
     */
    private final ButtonBase mainButtonBase;

    /**
     * the arrow button
     */
    private final ButtonBase arrowButton;

    /**
     * The contextMenu of the arrow button
     */
    protected ContextMenu popup;

    /**
     * The property of splitMode of this customSplitMenuButton
     */
    private ObjectProperty<SplitMode> splitMode;

    /**
     *
     */
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

    /**
     *
     */
    public enum SplitMode {
        /**
         * sets the arrow button on the top
         */
        SPLIT_TOP, // put arrow button on top
        /**
         * sets the arrow button on the right
         */
        SPLIT_RIGHT,
        /**
         * sets the arrow button on the bottom
         */
        SPLIT_BOTTOM,
        /**
         * sets the arrow button on the left
         */
        SPLIT_LEFT,
        /**
         * hides the arrow button
         */
        HIDDEN
    }

    /**
     *
     *
     * @param newClass
     */
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

    /**
     * Sets the graphic for the label of this customSplitMenuButton
     *
     * @param graphic the new graphic
     */
    public void setGraphic(Node graphic) {
        this.mainButtonBase.setGraphic(graphic);
    }

    /**
     * Sets the actionEvent for the mainButtonBase
     *
     * @param actionEvent the new actionEvent
     */
    public void setOnAction(EventHandler<ActionEvent> actionEvent) {
        this.mainButtonBase.setOnAction(actionEvent);
    }

    /**
     * Sets the splitMode of this customSplitMenuButton
     *
     * @param mode the new mode of this customSplitMenuButton
     */
    public void setSplitMode(SplitMode mode) {
        if (this.splitMode == null) {
            this.splitMode = new SimpleObjectProperty<>(mode);
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
                this.bindSizeAndLayout(this.arrowButton.prefWidthProperty(), this.mainButtonBase.widthProperty(),
                        this.arrowButton.layoutYProperty(), this.mainButtonBase.heightProperty(),
                        BOTTOM_PSEUDO_CLASS);
                break;
            case SPLIT_RIGHT:
                // bind arrowbutton height to label height
                this.bindSizeAndLayout(this.arrowButton.prefHeightProperty(), this.mainButtonBase.heightProperty(),
                        this.arrowButton.layoutXProperty(), this.mainButtonBase.widthProperty(),
                        RIGHT_PSEUDO_CLASS);
                break;
            case SPLIT_LEFT:
                // bind arrowbutton height to label height
                this.bindSizeAndLayout(this.arrowButton.prefHeightProperty(), this.mainButtonBase.heightProperty(),
                        this.mainButtonBase.layoutXProperty(), this.arrowButton.widthProperty(),
                        LEFT_PSEUDO_CLASS);
                break;
            case SPLIT_TOP:
                // bind arrowbutton width to label height
                this.bindSizeAndLayout(this.arrowButton.prefWidthProperty(), this.mainButtonBase.widthProperty(),
                        this.mainButtonBase.layoutYProperty(), this.arrowButton.heightProperty(),
                        TOP_PSEUDO_CLASS);
                break;
            case HIDDEN:
                // unbind all and hide button
                this.bindHidden();
                break;
        }

    }

    /**
     * Returns the splitMode of this customSplitMenuButton
     *
     * @return the splitMode
     */
    public SplitMode getSplitMode() {
        return (this.splitMode == null) ? SplitMode.HIDDEN : this.splitMode.get();
    }

    /**
     * Returns the splitModeProperty of this customSplitMenuButton
     *
     * @return the splitModeProperty
     */
    public ObjectProperty<SplitMode> splitModeProperty() {
        return this.splitMode;
    }

    /**
     * Returns the mainButtonBase of this customSplitMenuButton
     *
     * @return the mainButtonBase
     */
    public ButtonBase getMainButton() {
        return this.mainButtonBase;
    }

    /**
     * Returns the arrowButtonBase of this customSplitMenuButton
     *
     * @return the arrowButtonBase
     */
    public ButtonBase getArrowButton() {
        return this.arrowButton;
    }

    /**
     * Constructor
     *
     * @param text the text of the mainButtonBase
     * @param splitMode the splitMode for the mainButtonBase and the arrowButton
     * @param items the items for the popup
     */
    public CustomSplitMenuButton(String text, SplitMode splitMode,
                                 MenuItem... items) {
        this.mainButtonBase = new Button(text);
        this.mainButtonBase.getStyleClass().setAll("label");
        this.arrowButton = new Button();
        // bind the managed property to visibility.
        // we dont want to manage an invisible button.
        this.arrowButton.managedProperty().bind(
                this.arrowButton.visibleProperty());
        this.arrowButton.getStyleClass().setAll("arrow-button");
        this.getStyleClass().setAll("split-menu-button");
        this.getChildren().setAll(this.mainButtonBase, this.arrowButton);
        this.getStyleClass().add("big");
        this.setSplitMode(splitMode);

        this.popup = new ContextMenu(items);

        this.arrowButton.setOnAction(new EventHandler<>() {
            boolean isOpen = false;

            @Override
            public void handle(ActionEvent actionEvent) {
                this.isOpen = !this.isOpen;
                if (this.isOpen) {
                    CustomSplitMenuButton.this.popup.show(
                            CustomSplitMenuButton.this.arrowButton,
                            Side.BOTTOM, 0, 0);
                } else {
                    CustomSplitMenuButton.this.popup.hide();
                }
            }
        });
    }
}
