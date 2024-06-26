package gui.saleBookController.pages;

import com.pixelduke.control.ribbon.RibbonTab;
import javafx.scene.layout.Pane;
import logic.saleBook.SaleBook;
import org.jetbrains.annotations.NotNull;

/**
 * A Page is one of the main program . A Page can be set in focus by his pane {@link #getBasePane()}.
 * Each Page has a ribbonTab, which inherits his own groups and controls. To communicate to the
 * logic each page needs access to the {@link SaleBook} therefor the
 * {@link #setSaleBook(SaleBook)} shall be set.
 *
 * @author xthe_white_lionx
 */
public interface Page {

    /**
     * Sets the saleBook to this page. To give the page the accessibility to communicate to the
     * logic
     *
     * @param saleBook the saleBock that should be set
     */
    void setSaleBook(@NotNull SaleBook saleBook);

    /**
     * Returns the base pane of this page
     *
     * @return the base pane of this page
     */
    @NotNull Pane getBasePane();

    /**
     * Returns the ribbonTab of this page
     *
     * @return the ribbonTab of this page
     */
    @NotNull RibbonTab getRibbonTab();
}
