package logic;

/**
 * This enum represents the conditions an item or a spare part can have.
 *
 * @author xthe_white_lionx
 */
public enum Condition {
    /**
     * The item or spare part is brand new and never used before
     */
    NEW,
    /**
     * Were opened and might be used but look as new
     */
    AS_NEW,
    /**
     * Was faulty but was repaired
     */
    REFURBISHED,
    /**
     * Were used and has small signs of usage
     */
    VERY_GOOD,
    /**
     * Were used and has signs of usage
     */
    GOOD,
    /**
     * Were used and has significant signs of usage
     */
    ACCEPTABLE,
    /**
     * Does not work correctly
     */
    FAULTY
}
