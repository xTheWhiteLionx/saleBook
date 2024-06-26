package costumeClasses.FXClasses;

import javafx.scene.control.DateCell;
import utils.LocalDateUtils;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

/**
 * This class represents a BoundedDateCell. A BoundedDateCell is a DateCell for which a minimal
 * and maximal reachable date can be set as bounds. Excluding dates will be highlighted as disabled
 *
 * @author xthe_white_lionx
 */
public class BoundedDateCell extends DateCell {

    /**
     * The minimal reachable date inclusive this date
     */
    private final @Nullable LocalDate minDate;

    /**
     * The maximal reachable date inclusive this date
     */
    private final @Nullable LocalDate maxDate;

    /**
     * Creates a new BoundedDateCell with the specified minDate and maxDate as inclusive bounds.
     * If minDate is null no upper bound for the date will be set, the same applies to the maxDate
     *
     * @param minDate the minimal reachable date, inclusive this date or null if there is now
     *                minimal date
     * @param maxDate the maximal reachable date, inclusive this date or null if there is now
     *                maximal date
     * @throws IllegalArgumentException if the minDate is after the maxDate
     */
    public BoundedDateCell(@Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        if (minDate != null && maxDate != null) {
            if (!LocalDateUtils.areAcceptableDates(minDate, maxDate)){
                throw new IllegalArgumentException("the minDate is after the maxDate");
            }
        }

        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if ((this.minDate != null && item.isBefore(this.minDate)) ||
                (this.maxDate != null && item.isAfter(this.maxDate))) {
            this.setDisable(true);
        }
    }
}
