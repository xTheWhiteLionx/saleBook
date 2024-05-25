package utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * This class inherits some Utilities for LocalDates
 *
 * @author xthe_white_lionx
 */
public class LocalDateUtils {
    /** The date pattern that is used for conversion. Change as you wish. */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private LocalDateUtils() {
    }

    /**
     * Checks if the end date is after the start date or are equals
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return true if the end date is after the start date or if they are equals, otherwise false
     */
    public static boolean areAcceptableDates(@NotNull LocalDate startDate,
                                             @NotNull LocalDate endDate) {
        return startDate.compareTo(endDate) <= 0;
    }

    /**
     * Returns the given date as a well formatted String. The above defined
     * {@link #DATE_FORMATTER} is used.
     * For example, the format might be '30.12.1952'
     *
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static @NotNull String format(@NotNull LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}
