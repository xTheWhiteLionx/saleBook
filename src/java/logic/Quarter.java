package logic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Month;
import java.util.Set;

import static java.time.Month.APRIL;
import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;

/**
 * Enum of quarter.
 * A quarter is a quarter of a year, three months.
 * Denoted by Q1 to Q4
 *
 * @author xthe_white_lionx
 */
public enum Quarter {
    /**
     * First quarter of a year
     */
    Q1(JANUARY, FEBRUARY, MARCH),
    /**
     * Second quarter of a year
     */
    Q2(APRIL, MAY, JUNE),
    /**
     * Third quarter of a year
     */
    Q3(JULY, AUGUST, SEPTEMBER),
    /**
     * Fourth quarter of a year
     */
    Q4(OCTOBER, NOVEMBER, DECEMBER);

    /**
     * Months of the quarter
     */
    private final Set<Month> months;

    /**
     * Constructor of a quarter
     *
     * @param months vararg of months
     */
    Quarter(@NotNull Month... months) {
        this.months = Set.of(months);
    }

    /**
     * Returns the corresponding quarter to the specified month
     *
     * @param month the specified month
     * @return corresponding quarter to the specified month or null, if the month has no corresponding quarter
     */
    public static @Nullable Quarter getQuarterOfMonth(@NotNull Month month) {
        Quarter[] quarters = Quarter.values();
        for (Quarter quarter : quarters) {
            if (quarter.months.contains(month)) {
                return quarter;
            }
        }
        return null;
    }

    /**
     * Checks if this quarter contains the specified month
     *
     * @param month the month which should be checked
     * @return true if the quarter contains the specified month, otherwise false
     */
    public boolean contains(@NotNull Month month){
        return this.months.contains(month);
    }
}
