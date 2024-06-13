package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import javafx.scene.paint.Color;
import logic.products.item.ItemColor;
import logic.ProgressListener;
import logic.ProgressReader;
import logic.ProgressWriter;
import logic.Supplier;
import logic.saleBook.AbstractSaleBook;
import logic.saleBook.SaleBook;
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
 * @see logic.saleBook.AbstractSaleBook
 * @author xthe_white_lionx
 */
public class SaleBookData extends AbstractSaleBook {

    /**
     * Spare parts of this saleBookData
     */
    private final SparePartsManagerData sparePartsManagerData;

    private final PositionsManagerData positionsManagerData;

    /**
     * ItemColors of this saleBookData
     */
    private final ItemColor[] itemColors;

    /**
     * Suppliers for spare parts of this saleBookData
     */
    private final Supplier[] suppliers;

    private final AssetsManagerData assetsManagerData;

    private final OrdersManagerData ordersManagerData;

    /**
     *
     * @param repairServiceSales
     * @param extraordinaryIncome
     * @param paid
     * @param fixedCosts
     * @param itemColors
     * @param suppliers
     * @param assetsManagerData
     */
    public SaleBookData(@NotNull BigDecimal repairServiceSales,
                        @NotNull BigDecimal extraordinaryIncome,
                        @NotNull BigDecimal paid, @NotNull BigDecimal fixedCosts,
                        SparePartsManagerData sparePartsManagerData,
                        PositionsManagerData positionsManagerData,
                        ItemColor[] itemColors, Supplier[] suppliers,
                        OrdersManagerData ordersManagerData, AssetsManagerData assetsManagerData) {
        super(repairServiceSales, extraordinaryIncome, paid, fixedCosts);
        this.sparePartsManagerData = sparePartsManagerData;
        this.positionsManagerData = positionsManagerData;
        this.itemColors = itemColors;
        this.suppliers = suppliers;
        this.ordersManagerData = ordersManagerData;
        this.assetsManagerData = assetsManagerData;
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
     * Returns the sparePartsManagerData of this saleBookData
     *
     * @return the sparePartsManagerData of this saleBookData
     */
    public @NotNull SparePartsManagerData getSparePartsManagerData() {
        return this.sparePartsManagerData;
    }

    /**
     * Returns the position data of this saleBookData
     *
     * @return the positionData of this saleBookData
     */
    public @NotNull PositionsManagerData getPositionsManagerData() {
        return this.positionsManagerData;
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
     * Returns the suppliers of this saleBookData
     *
     * @return the suppliers of this saleBookData
     */
    public @NotNull Supplier[] getSuppliers() {
        return this.suppliers;
    }

    /**
     * Returns the assetsManagerData of this saleBookData
     *
     * @return the assetsManagerData of this saleBookData
     */
    public @NotNull AssetsManagerData getAssetsManagerData() {
        return this.assetsManagerData;
    }

    /**
     * Returns the ordersManagerData of this saleBookData
     *
     * @return the ordersManagerData of this saleBookData
     */
    public @NotNull OrdersManagerData getOrdersManagerData() {
        return this.ordersManagerData;
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
        if (!"json".equals(extension)) {
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaleBookData that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(this.sparePartsManagerData, that.sparePartsManagerData) && Objects.equals(this.positionsManagerData, that.positionsManagerData) && Objects.deepEquals(this.itemColors, that.itemColors) && Objects.deepEquals(this.suppliers, that.suppliers) && Objects.equals(this.assetsManagerData, that.assetsManagerData) && Objects.equals(this.ordersManagerData, that.ordersManagerData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.sparePartsManagerData, this.positionsManagerData, Arrays.hashCode(this.itemColors), Arrays.hashCode(this.suppliers), this.assetsManagerData, this.ordersManagerData);
    }

    @Override
    public String toString() {
        return "SaleBookData{" +
                "sparePartsManagerData=" + this.sparePartsManagerData +
                ", positionsManagerData=" + this.positionsManagerData +
                ", itemColors=" + Arrays.toString(this.itemColors) +
                ", suppliers=" + Arrays.toString(this.suppliers) +
                ", assetsManagerData=" + this.assetsManagerData +
                ", ordersManagerData=" + this.ordersManagerData +
                '}';
    }


}
