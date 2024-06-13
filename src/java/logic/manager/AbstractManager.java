package logic.manager;

import logic.GUIConnector;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an abstract Manager and administrates the comment connections to the gui
 * and the saleBook for each manager
 *
 * @author xThe_white_Lionx
 * @Date 30.05.2024
 */
public abstract class AbstractManager {

    /**
     * Connection to the saleBook
     */
    protected final SaleBook saleBook;

    /**
     * Connection to the gui
     */
    protected final GUIConnector gui;

    /**
     * Constructor
     *
     * @param saleBook connection to the saleBook
     * @param gui connection to the gui
     */
    protected AbstractManager(@NotNull SaleBook saleBook, @NotNull GUIConnector gui) {
        this.saleBook = saleBook;
        this.gui = gui;
    }
}
