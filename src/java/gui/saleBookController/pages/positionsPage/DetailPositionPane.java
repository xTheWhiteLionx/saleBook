package gui.saleBookController.pages.positionsPage;

import gui.ApplicationMain;
import gui.DialogWindow;
import gui.FXutils.LabelUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.products.position.Position;
import logic.products.position.State;
import utils.FileUtils;
import utils.LocalDateUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class displays the details of the current position
 *
 * @author xthe_white_lionx
 */
public class DetailPositionPane implements Initializable {

    /**
     * Days of a commercial month
     */
    public static final long DAYS_OF_COMMERCIAL_MONTH = 30L;

    /**
     * Number of Days of a year
     */
    public static final long DAYS_OF_A_YEAR = 365L;

    /**
     * The base vbox which inherits all components of this pane
     */
    @FXML
    public VBox vbox;
    /**
     * Label to display the id of the current position
     */
    @FXML
    private Label idLbl;
    /**
     * Label to display the model of the current position
     */
    @FXML
    private Label categoryLbl;
    /**
     * Label to display the date of order of the current position
     */
    @FXML
    private Label orderDateLbl;
    /**
     * Label to display the purchasing price of the current position
     */
    @FXML
    private Label purchasingPriceLbl;
    /**
     * Label to display the state of the current position
     */
    @FXML
    private Label stateLbl;
    /**
     * Label to display the cost of the current position
     */
    @FXML
    private Label costLbl;
    /**
     * Label to display the date of arrival of the current position
     */
    @FXML
    private Label arrivalDateLbl;
    /**
     * Label to display the selling date of the current position
     */
    @FXML
    private Label sellingDateLbl;
    /**
     * Label to display the selling price of the current position
     */
    @FXML
    private Label sellingPriceLbl;
    /**
     * Label to display the shipping company of the current position
     */
    @FXML
    private Label shippingCompanyLbl;
    /**
     * Hyperlink to display the tracking number of the current position and links
     * to the tracking page of the position
     */
    @FXML
    private Hyperlink trackingNumberLinkLbl;
    /**
     * Label to display the holding period (days between the receivedDate and the sellingDate)
     * of the current position
     */
    @FXML
    private Label holdingPeriodLbl;
    /**
     * Label to display the performance of the current position
     */
    @FXML
    private Label performanceLbl;
    /**
     * Label to display the performance in percentage of the current position,
     * with the sellingPrice as base for the calculation
     */
    @FXML
    private Label percentPerformanceLbl;

    //TODO 03.06.2024 JavaDoc
    private int id;

    /**
     * Creates and loads a new DetailPositionPane
     *
     * @return the new created DetailPositionPane
     */
    public static @NotNull DetailPositionPane createDetailPositionPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("saleBookController/pages/positionsPage/DetailPositionPane.fxml"));
        loader.load();
        return loader.getController();
    }

    /**
     * Initializes this controller and his controls
     *
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clear();
    }

    /**
     * Sets the details of the specified position only if the detail is available
     *
     * @param position the position which details should be displayed
     */
    public void setPosition(@NotNull Position position) {
        this.clear();
        this.id = position.getId();
        this.idLbl.setText(String.valueOf(this.id));
        this.categoryLbl.setText(position.getCategory());
        this.orderDateLbl.setText(LocalDateUtils.format(position.getOrderDate()));
        LabelUtils.setMoney(this.purchasingPriceLbl, position.getPurchasingPrice());
        LabelUtils.setMoney(this.costLbl, position.getCost());
        Long holdingPeriod = position.calcHoldingPeriod();
        if (holdingPeriod != null) {
            StringBuilder sb = new StringBuilder();
            if (holdingPeriod > DAYS_OF_A_YEAR){
                long years = holdingPeriod / DAYS_OF_A_YEAR;
                sb.append(years).append(" ").append("year");
                if (years != 1){
                    sb.append("s");
                }
            }
            if (holdingPeriod > DAYS_OF_COMMERCIAL_MONTH){
                long months = holdingPeriod / DAYS_OF_COMMERCIAL_MONTH;
                sb.append(" ").append(months).append(" ").append("month");
                if (months != 1){
                    sb.append("s");
                }
                sb.append(" ");
            }
            long days = holdingPeriod % DAYS_OF_COMMERCIAL_MONTH;
            sb.append(days).append(" ").append("day");
            if (days != 1){
                sb.append("s");
            }
            this.holdingPeriodLbl.setText(sb.toString());
        }
        LabelUtils.setMoneyAndColor(this.performanceLbl, position.calcPerformance());
        BigDecimal calcPercentPerformance = position.calcPercentPerformance();
        if (calcPercentPerformance != null) {
            this.percentPerformanceLbl.setText(calcPercentPerformance + " %");
        }
        State state = position.getState();
        this.stateLbl.setText(state.name());
        switch (state) {
            case DELIVERED:
            case SHIPPED:
                if (position.getShippingCompany() != null) {
                    this.shippingCompanyLbl.setText(position.getShippingCompany().name());
                }
                String trackingNumber = position.getTrackingNumber();
                if (!trackingNumber.isEmpty()) {
                    this.trackingNumberLinkLbl.setText("[" + trackingNumber + "]");
                    this.trackingNumberLinkLbl.setOnAction(actionEvent -> {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(position.getShippingCompany().
                                        createTrackingLink(trackingNumber));
                            } catch (IOException | URISyntaxException e) {
                                DialogWindow.displayError(e);
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            case SOLD:
                this.sellingDateLbl.setText(LocalDateUtils.format(position.getSellingDate()));
                LabelUtils.setMoney(this.sellingPriceLbl, position.getSellingPrice());
            case REPAIRED:
            case RECEIVED:
                this.arrivalDateLbl.setText(LocalDateUtils.format(position.getReceivedDate()));
        }
    }

    /**
     * Returns the base pane of this controller
     *
     * @return the base pane of this controller
     */
    public Pane getBasePane() {
        return this.vbox;
    }

    @FXML
    private void openPictureDirectory(ActionEvent actionEvent) {
        File parentDir = new File("positions/" + this.id);
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        File picturesDir = new File(parentDir, "pictures");
        if (!picturesDir.exists()) {
            picturesDir.mkdir();
        }
        try {
            Desktop.getDesktop().open(picturesDir);
        } catch (IOException e) {
            DialogWindow.displayError(e);
        }
    }

    /**
     * Clears the labels
     */
    private void clear() {
        this.idLbl.setText("");
        this.categoryLbl.setText("");
        this.orderDateLbl.setText("");
        this.purchasingPriceLbl.setText("");
        this.costLbl.setText("");
        this.stateLbl.setText("");
        this.arrivalDateLbl.setText("");
        this.sellingDateLbl.setText("");
        this.sellingPriceLbl.setText("");
        this.shippingCompanyLbl.setText("");
        this.trackingNumberLinkLbl.setText("");
        this.holdingPeriodLbl.setText("");
        this.performanceLbl.setText("");
        this.percentPerformanceLbl.setText("");
    }
}
