package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

/**
 * This class contains static images like {@link #SAVE_IMAGE}
 *
 * @author xthe_white_lionx
 */
public final class Images {
    /**
     * Image with "save" icon
     */
    public static final Image SAVE_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-speichern-32.png");

    /**
     * Image with "save as" icon
     */
    public static final Image SAVE_AS_IMAGE = new Image("gui/textures/ribbonBandIcons/icons8" +
            "-speichern-als-32.png");

    /**
     * Image with "add" icon
     */
    public static final Image ADD_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-hinzufügen-32.png");

    /**
     * Image with "delete" icon
     */
    public static final Image DELETE_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-löschen-32.png");

    /**
     * Image with "received" icon
     */
    public static final Image RECEIVED_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-daten-angekommen-32.png");

    /**
     * Image with "cost" icon
     */
    public static final Image COST_IMAGE = new Image("gui/textures/ribbonBandIcons/icons8" +
            "-geldtransfer-zu-initiieren-32.png");

    /**
     * Image with "repaired" icon
     */
    public static final Image REPAIRED_IMAGE = new Image("gui/textures/ribbonBandIcons/icons8" +
            "-wartung-32.png");

    /**
     * Image with "filter" icon
     */
    public static final Image FILTER_IMAGE = new Image( "gui/textures/ribbonBandIcons/icons8" +
            "-filter-32.png");

    /**
     * Image with "edit" icon
     */
    public static final Image EDIT_IMAGE = new Image("gui/textures/ribbonBandIcons/icons8" +
            "-bearbeiten-32.png");

    /**
     * Image with "shipped" icon
     */
    public static final Image SHIPPED_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-versendet-32.png");

    /**
     * Image with "sale" icon
     */
    public static final Image SALE_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-verkauf-32.png");

    /**
     * Image with "calculator" icon
     */
    public static final Image CALCULATOR_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-calculator-32.png");

    /**
     * Image with "add order" icon
     */
    public static final Image ADD_ORDER_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-bestellung-anlegen-32.png");

    /**
     * Image with "order completed" icon
     */
    public static final Image ORDER_COMPLETED_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-order-completed-32.png");

    /**
     * Image with "order cancel" icon
     */
    public static final Image ORDER_CANCEL_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-cancel-order-32.png");

    /**
     * Image with "divide" icon
     */
    public static final Image DIVIDE_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-gabelung-32.png");

    /**
     * Image with "combine" icon
     */
    public static final Image COMBINE_IMAGE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-zusammenführen-32.png");

    /**
     * Image with "warning" icon
     */
    public static final Image WARNING_IMAGE = new Image("gui/textures/"+
            "icons8-allgemeines-warnschild-32.png");

    /**
     * Image with "error" icon
     */
    public static final Image ERROR_IMAGE = new Image("gui/textures/"+
            "icons8-error-32.png");

    /**
     * Image with "new asset" icon
     */
    public static final Image NEW_ASSET_IMAGE = new Image("gui/textures/" +
            "icons8-vermögenswert-new-32.png");

    /**
     * Image with "add picture" icon
     */
    public static final Image ADD_PICTURE = new Image("gui/textures/ribbonBandIcons/" +
            "icons8-add-picture-32.png");

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private Images() {
    }

    /**
     * Creates an imageView with the specified image in the height and width of the specified size
     *
     * @param image the image that should be placed in the imageview
     * @param size the size which the imageView should have
     * @return an imageView with the specified image in the specified size
     */
    public static ImageView createImageView(@NotNull Image image, double size){
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater 0 but is %f".formatted(size));
        }
        ImageView result = new ImageView(image);
        result.setFitHeight(size);
        result.setFitWidth(size);
        return result;
    }
}
