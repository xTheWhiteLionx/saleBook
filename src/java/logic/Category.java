package logic;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author xthe_white_lionx
 * @date 04.06.2024
 */
public class Category {
    String name;
    List<String> saleText;
    List<String> longSaleText;

    /**
     *
     * @param name
     * @param saleText
     * @param longSaleText
     */
    public Category(@NotNull String name, @NotNull List<String> saleText,
                    @NotNull List<String> longSaleText) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.saleText = saleText;
        this.longSaleText = longSaleText;
    }
}
