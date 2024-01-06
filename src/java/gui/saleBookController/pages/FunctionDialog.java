package gui.saleBookController.pages;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 *
 * @param <T> the type of return , of the specific function dialog
 */
public abstract class FunctionDialog<T> {
    /**
     *
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
