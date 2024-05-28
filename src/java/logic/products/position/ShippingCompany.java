package logic.products.position;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author xthe_white_lionx
 */
//TODO 08.01.2024 JavaDoc
public enum ShippingCompany {
    /**
     * The DHL shippingCompany
     */
    DHL("https://www.dhl.de/de/privatkunden/pakete-empfangen/verfolgen.html?lang=de&idc="),
    /**
     * The HERMES shippingCompany
     */
    HERMES("https://www.myhermes.de/empfangen/sendungsverfolgung/sendungsinformation#");

    /**
     * The tracking page url of this shippingCompany
     */
    private final String url;

    /**
     * Constructor for a shippingCompany
     *
     * @param url tracking page url of this shippingCompany
     * @throws IllegalArgumentException if the specified url is empty
     */
    ShippingCompany(@NotNull String url) {
        if (url.isEmpty()) {
            throw new IllegalArgumentException("the url must not be empty");
        }
        this.url = url;
    }

    /**
     * Returns the tracking link for the specified trackingNumber.
     *
     * @param trackingNumber the tracking number of the package which wanted to be tracked in
     *                       this shippingCompany
     * @return the tracking link for the specified trackingNumber
     * @throws IllegalArgumentException if the trackingNumber is empty
     * @throws URISyntaxException       indicates that the trackingNumber could not be parsed as a
     *                                  URI reference.
     */
    public @NotNull URI createTrackingLink(@NotNull String trackingNumber) throws URISyntaxException {
        if (trackingNumber.isEmpty()) {
            throw new IllegalArgumentException("tracking number must not be empty");
        }
        return new URI(this.url + trackingNumber.trim());
    }
}
