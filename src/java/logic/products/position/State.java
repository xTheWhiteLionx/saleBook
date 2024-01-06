package logic.products.position;

/**
 * Enum of status which an {@link Position} could reach.
 *
 * @author xthe_white_lionx
 */
public enum State {
    /** state for an ordered position */
    ORDERED,
    /** state for a lost position */
    LOST,
    /** state for a received position */
    RECEIVED,
    /** state for a repaired position */
    REPAIRED,
    /** state for a sold position */
    SOLD,
    /** state for a shipped position */
    SHIPPED,
    /** state for a delivered position */
    DELIVERED
}


