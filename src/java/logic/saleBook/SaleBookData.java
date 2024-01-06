package logic.saleBook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import javafx.scene.paint.Color;
import logic.ProgressListener;
import logic.ProgressReader;
import logic.ProgressWriter;
import logic.SparePart;
import logic.order.Order;
import logic.order.Supplier;
import logic.products.position.Position;
import logic.products.position.PositionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

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
public class SaleBookData extends SaleBookImpl {

    /**
     * Spare parts of this saleBookData
     */
    private final SparePart[] spareParts;

    /**
     * Position data of this saleBookData
     */
    private final PositionData[] positionData;

    /**
     * Suppliers for spare parts of this saleBookData
     */
    private final Supplier[] suppliers;

    /**
     * Orders of this saleBookData
     */
    private final Order[] orders;

    /**
     * Constructor for an saleBookData
     *
     * @param saleBook the saleBook to get the data from
     */
    public SaleBookData(@NotNull SaleBook saleBook) {
        super(saleBook.repairServiceSales, saleBook.extraordinaryIncome,
                saleBook.paid, saleBook.fixedCosts, saleBook.nextPosId, saleBook.nextOrderId);
        this.spareParts = saleBook.getSpareParts().toArray(new SparePart[0]);
        Collection<Position> positions = saleBook.getPositions();
        this.positionData = new PositionData[positions.size()];
        int index = 0;
        for (Position position : positions) {
            this.positionData[index++] = new PositionData(position);
        }
        this.suppliers = saleBook.getSuppliers().toArray(new Supplier[0]);
        this.orders = saleBook.getOrders().toArray(new Order[0]);
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
        if (!file.getName().endsWith(".json")) {
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
    public @NotNull SparePart[] getSpareParts() {
        return this.spareParts;
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
     * Writes in to the specified json file the data of this saleBookData
     *
     * @param file the file which should be written with the data
     * @param progressListener the listener which will be updated by writing the file
     * @throws IOException if the file cannot be found or the (writing) access were denied
     * @throws IllegalArgumentException if the file is not a json file
     */
    public void toJson(@NotNull File file, @NotNull ProgressListener progressListener)
            throws IOException {
        if (!file.getName().endsWith(".json")) {
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
        return Arrays.equals(this.positionData, that.positionData) &&
                Arrays.equals(this.suppliers, that.suppliers) && Arrays.equals(this.orders, that.orders);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(this.positionData);
        result = 31 * result + Arrays.hashCode(this.suppliers);
        result = 31 * result + Arrays.hashCode(this.orders);
        return result;
    }

    @Override
    public String toString() {
        return "SaleBookData{" +
                "positions=" + Arrays.toString(this.positionData) +
                ", suppliers=" + Arrays.toString(this.suppliers) +
                ", orders=" + Arrays.toString(this.orders) +
                ", spareParts=" + Arrays.toString(this.spareParts) +
                ", repairServiceSales=" + this.repairServiceSales +
                ", extraordinaryIncome=" + this.extraordinaryIncome +
                ", paid=" + this.paid +
                ", fixedCosts=" + this.fixedCosts +
                ", nextPosId=" + this.nextPosId +
                ", nextOrderId=" + this.nextOrderId +
                '}';
    }
}
