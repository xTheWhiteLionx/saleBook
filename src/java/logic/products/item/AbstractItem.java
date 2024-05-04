package logic.products.item;

import logic.Condition;
import logic.Variant;
import org.jetbrains.annotations.NotNull;

/**
 * This abstract class presents a comment Position
 *
 * @author xthe_white_lionx
 */
public abstract class AbstractItem {
    /**
     * The id of this item
     */
    protected int id;
    /**
     * The condition of this item
     */
    protected Condition condition;
    /**
     * The variant of this item
     */
    protected Variant variant;
    /**
     * The error and faulty description of this item
     */
    protected String errorDescription;

    /**
     *
     *
     * @param id
     * @param condition
     * @param variant
     * @param errorDescription
     */
    //TODO 02.05.2024 JavaDoc
    protected AbstractItem(int id, Condition condition, Variant variant, String errorDescription) {
        this.id = id;
        this.condition = condition;
        this.variant = variant;
        this.errorDescription = errorDescription;
    }

    /**
     * Returns the id of this item
     *
     * @return the id of this item
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the id of this item
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Return the condition of this item
     *
     * @return the condition of this item
     */
    public @NotNull Condition getCondition() {
        return this.condition;
    }

    /**
     * Sets the condition of this item to the specified condition
     *
     * @param condition the new condition of this item
     */
    public void setCondition(@NotNull Condition condition) {
        this.condition = condition;
    }

    /**
     * Returns the variant of this item
     *
     * @return he variant of this item
     */
    public @NotNull Variant getVariant() {
        return this.variant;
    }

    /**
     * Sets the variant of this item to the specified variant
     *
     * @param variant the new variant of this item
     */
    public void setVariant(@NotNull Variant variant) {
        this.variant = variant;
    }


    /**
     * Returns the error and faulty description of this ...
     *
     * @return the error description of this ...
     */
    public @NotNull String getErrorDescription() {
        return this.errorDescription;
    }

    /**
     * Sets the errorDescription of this ... to the specified errorDescription
     *
     * @param errorDescription the errorDescription which will be set
     */
    public void setErrorDescription(@NotNull String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
