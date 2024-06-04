package logic.saleBook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import javafx.scene.paint.Color;
import logic.Asset;
import logic.manager.OrdersManager;
import logic.products.item.ItemColor;
import logic.ProgressListener;
import logic.ProgressReader;
import logic.ProgressWriter;
import logic.order.Order;
import logic.Supplier;
import logic.products.position.PositionData;
import logic.sparePart.SparePartData;
import utils.CollectionsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * This class contains {@code SaleBookData} with all his operations.
 * An instance of the SaleBook class can be created, which contains all the
 * information from a JSON-File and checks it for any errors.
 * <p>
 * Whereupon a new instance of the {@link SaleBook} can be created.
 * This applies to both directions, so this class acts as a transmitter class between
 * JSON-Files (save/load game) and the logic.
 *
 * @author xthe_white_lionx
 */
public class SaleBookData extends AbstractSaleBook {

    /**
     * Spare parts of this saleBookData
     */
    private final SparePartData[] sparePartData;

    /**
     * Position data of this saleBookData
     */
    private final PositionData[] positionData;

    /**
     * ItemColors of this saleBookData
     */
    private final ItemColor[] itemColors;

    /**
     * Suppliers for spare parts of this saleBookData
     */
    private final Supplier[] suppliers;

    /**
     * Orders of this saleBookData
     */
    private final Order[] orders;

    /**
     * Assets of this saleBookData
     */
    private final Asset[] assets;

    /**
     * the id for the next creation of a position
     */
    private final int nextPosId;

    /**
     * the id for the next creation of an order
     */
    private final int nextOrderId;

    /**
     * the id for the next creation of an asset
     */
    private final int nextAssetId;

    /**
     *
     * @param repairServiceSales
     * @param extraordinaryIncome
     * @param paid
     * @param fixedCosts
     * @param sparePartData
     * @param positionData
     * @param itemColors
     * @param suppliers
     * @param orders
     * @param assets
     * @param nextPosId
     * @param nextOrderId
     * @param nextAssetId
     */
    public SaleBookData(@NotNull BigDecimal repairServiceSales,
                        @NotNull BigDecimal extraordinaryIncome,
                        @NotNull BigDecimal paid, @NotNull BigDecimal fixedCosts,
                        SparePartData[] sparePartData, PositionData[] positionData,
                        ItemColor[] itemColors, Supplier[] suppliers, Order[] orders,
                        Asset[] assets, int nextPosId, int nextOrderId, int nextAssetId) {
        super(repairServiceSales, extraordinaryIncome, paid, fixedCosts);
        this.sparePartData = sparePartData;
        this.positionData = positionData;
        this.itemColors = itemColors;
        this.suppliers = suppliers;
        this.orders = orders;
        this.assets = assets;
        this.nextPosId = nextPosId;
        this.nextOrderId = nextOrderId;
        this.nextAssetId = nextAssetId;
    }

    /**
     * Builds a SaleBookData from the data of the specified file
     *
     * @param file the file to get the data from to
     * @param progressListener the listener which will be updated by reading the file
     * @return a new saleBookData
     * @throws FileNotFoundException if the file cannot be found or opened
     * @throws IOException if an I/0 error occurs at the reading
     * @throws IllegalArgumentException if the file is not a json file
     */
    public static @Nullable SaleBookData fromJson(@NotNull File file,
                                                  @NotNull ProgressListener progressListener)
            throws FileNotFoundException, IOException {
        if (!FileUtils.getExtension(file).equals("json")) {
            throw new IllegalArgumentException("the file must be an json file");
        }

        Gson gson = new GsonBuilder()
                //Deserializer to parse the dates as LocalDates
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (json, type, jsonDeserializationContext) -> json == null ? null :
                                LocalDate.ofEpochDay(json.getAsLong()))
                //Deserializer to parse the dates as Color
                .registerTypeAdapter(Color.class, (JsonDeserializer<Color>)
                        (json, type, jsonDeserializationContext) -> json == null ? null :
                                Color.valueOf(json.getAsString()))
                .create();

        try (FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
             ProgressReader progressReader = new ProgressReader(fileInputStream, progressListener)) {
            if (progressReader.ready()) {
                return gson.fromJson(progressReader, SaleBookData.class);
            }
        }
        return null;
    }

    /**
     * Returns the spare parts of this saleBookData
     *
     * @return the spare parts of this saleBookData
     */
    public @NotNull SparePartData[] getSparePartData() {
        return this.sparePartData;
    }

    /**
     * Returns the position data of this saleBookData
     *
     * @return the positionData of this saleBookData
     */
    public @NotNull PositionData[] getPositionData() {
        return this.positionData;
    }

    /**
     * Returns the ItemColors of this saleBookData
     *
     * @return the ItemColors of this saleBookData
     */
    public @NotNull ItemColor[] getItemColors() {
        return this.itemColors;
    }

    /**
     * Returns the orders of this saleBookData
     *
     * @return the orders of this saleBookData
     */
    public @NotNull Order[] getOrders() {
        return this.orders;
    }

    /**
     * Returns the suppliers of this saleBookData
     *
     * @return the suppliers of this saleBookData
     */
    public @NotNull Supplier[] getSuppliers() {
        return this.suppliers;
    }

    /**
     * Returns the assets of this saleBookData
     *
     * @return the assets of this saleBookData
     */
    public @NotNull Asset[] getAssets() {
        return this.assets;
    }

    /**
     * Returns the id for the next creation of a position
     *
     * @return the id for the next creation of a position
     */
    public int getNextPosId() {
        return this.nextPosId;
    }

    /***
     * Returns the id for the next creation of an order
     *
     * @return the id for the next creation of an order
     */
    public int getNextOrderId() {
        return this.nextOrderId;
    }

    /**
     * Returns the id for the next asset
     *
     * @return the id for the next asset
     */
    public int getNextAssetId() {
        return this.nextAssetId;
    }

    /**
     * Writes in to the specified json file the data of this saleBookData
     *
     * @param file the file which should be written with the data
     * @param progressListener the listener which will be updated by writing the file
     * @throws IOException if the file cannot be found or the (writing) access were denied
     * @throws IllegalArgumentException if the file is not a json file
     */
    public void toJson(@NotNull File file, @NotNull ProgressListener progressListener)
            throws IOException, IllegalArgumentException {
        String extension = FileUtils.getExtension(file);
        if (!extension.equals("json")) {
            throw new IllegalArgumentException("the file must be an json file");
        }

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
                //serializer to parse the LocalDates
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, type, jsonSerializationContext) ->
                                src == null ? null : new JsonPrimitive(src.toEpochDay()))
                //serializer to parse the Color
                .registerTypeAdapter(Color.class,
                        (JsonSerializer<Color>) (src, type, jsonSerializationContext) ->
                                src == null ? null : new JsonPrimitive(src.toString()))
                .create();

        try (FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
             ProgressWriter progressWriter = new ProgressWriter(fileOutputStream,
                     progressListener)) {
            gson.toJson(this, progressWriter);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof SaleBookData that)) return false;
        if (! super.equals(o)) return false;
        return this.nextPosId == that.nextPosId && this.nextOrderId == that.nextOrderId
                && this.nextAssetId == that.nextAssetId
                && Objects.deepEquals(this.sparePartData, that.sparePartData)
                && Objects.deepEquals(this.positionData, that.positionData)
                && Objects.deepEquals(this.itemColors, that.itemColors)
                && Objects.deepEquals(this.suppliers, that.suppliers)
                && Objects.deepEquals(this.orders, that.orders)
                && Objects.deepEquals(this.assets, that.assets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Arrays.hashCode(this.sparePartData),
                Arrays.hashCode(this.positionData), Arrays.hashCode(this.itemColors),
                Arrays.hashCode(this.suppliers), Arrays.hashCode(this.orders),
                Arrays.hashCode(this.assets), this.nextPosId, this.nextOrderId, this.nextAssetId);
    }

    @Override
    public String toString() {
        return "SaleBookData{" +
                "spareParts=" + Arrays.toString(this.sparePartData) +
                ", positionData=" + Arrays.toString(this.positionData) +
                ", itemColors=" + Arrays.toString(this.itemColors) +
                ", suppliers=" + Arrays.toString(this.suppliers) +
                ", orders=" + Arrays.toString(this.orders) +
                ", assets=" + Arrays.toString(this.assets) +
                ", nextPosId=" + this.nextPosId +
                ", nextOrderId=" + this.nextOrderId +
                ", nextAssetId=" + this.nextAssetId +
                '}';
    }
}
