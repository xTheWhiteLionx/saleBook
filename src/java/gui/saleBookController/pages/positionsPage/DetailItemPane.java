package gui.saleBookController.pages.positionsPage;

import gui.ApplicationMain;
import gui.FXutils.ColorUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import logic.products.item.ItemColor;
import logic.products.item.Item;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class displays the details of the current item
 *
 * @author xthe_white_lionx
 */
public class DetailItemPane implements Initializable {

    /**
     * The base VBox of the controller
     */
    @FXML
    private Pane baseVbx;

    /**
     * Label to display the id of the item
     */
    @FXML
    private Label idLbl;
    /**
     * Label to display the condition of the item
     */
    @FXML
    private Label conditionLbl;
    /**
     * Label to display the variant of the item
     */
    @FXML
    private Label variantLbl;
    /**
     * Circle to display the color of the item
     */
    @FXML
    private Circle circleColor;
    /**
     * Label to display the color name of the item
     */
    @FXML
    private Label colorNameLbl;
    /**
     * Label to display the error description of the item
     */
    @FXML
    private Label errorDescriptionLbl;

    /**
     * Creates a DetailItemPane with the specified item
     *
     * @param item the item to display the details of
     * @return the new created DetailItemPane
     */
    public static @NotNull DetailItemPane createDetailItemPane(@NotNull Item item) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/DetailItemPane.fxml"));

        loader.load();
        DetailItemPane detailItemPane = loader.getController();
        detailItemPane.setItem(item);
        return detailItemPane;
    }

    /**
     * Initializes the DetailItemPane.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Returns the base pane of this DetailItemPane
     *
     * @return the base pane of this DetailItemPane
     */
    public Pane getBasePane() {
        return this.baseVbx;
    }

    /**
     * Displays the details of the specified item
     *
     * @param item the item to display the details of
     */
    private void setItem(@NotNull Item item) {
        this.idLbl.setText(String.valueOf(item.getId()));
        this.conditionLbl.setText(item.getCondition().name());
        this.variantLbl.setText(item.getVariant().name());

        ItemColor itemColor = item.getItemColor();
        this.circleColor.setFill(itemColor.getColor());
        this.colorNameLbl.setText(itemColor.getName());

        this.errorDescriptionLbl.setText(item.getErrorDescription());
    }

    /**
     * Handles the copy hex color button and copies the hexadecimal format of
     * the item to the clipboard of the user
     */
    @FXML
    private void handleCopyHexColor() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        Color color = (Color) this.circleColor.getFill();
        content.putString(ColorUtils.toHexString(color));
        clipboard.setContent(content);
    }
}
