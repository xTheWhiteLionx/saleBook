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
     * Was faulty but was repaired and can again be used
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
    FAULTY;

    /**
     * Checks if the condition allows using the object.
     * An object is usable if it is not faulty.
     *
     * @return true if the condition allows the using, otherwise false.
     */
    public boolean isUsable(){
        return this != FAULTY;
    }
}
