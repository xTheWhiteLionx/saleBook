package gui.saleBookController.pages;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * This class represents a FunctionDialog.
 * A FunctionDialog as a Dialog which ends with a specific result.
 * For example, a Dialog that asks for a date has a date as a result.
 *
 * @param <T> the type of the result, of the specific function dialog
 * @author xthe_white_lionx
 */
public abstract class FunctionDialog<T> {
    /**
     * The result of this functionDialog
     */
    protected T result;

    /**
     * Returns the result of this functionDialog
     *
     * @return result of this functionDialog
     */
    public @NotNull Optional<T> getResult() {
        return Optional.ofNullable(this.result);
    }
}
