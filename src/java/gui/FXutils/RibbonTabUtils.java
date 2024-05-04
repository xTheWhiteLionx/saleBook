package gui.FXutils;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import org.jetbrains.annotations.NotNull;

/**
 * Some Utility's for RibbonTab's.
 *
 * @author xthe_white_lionx
 */
public class RibbonTabUtils {

    /**
     * Creates a RibbonTab with the specified title and ribbonGroups
     *
     * @param title the title of the ribbonTab
     * @param ribbonGroups the ribbonGroups of the RibbonTab
     * @return the newly created RibbonTab
     */
    public static @NotNull RibbonTab createRibbonTab(@NotNull String title,
                                                       RibbonGroup @NotNull ... ribbonGroups){
        RibbonTab result = new RibbonTab(title);
        result.getRibbonGroups().addAll(ribbonGroups);
        return result;
    }
}
