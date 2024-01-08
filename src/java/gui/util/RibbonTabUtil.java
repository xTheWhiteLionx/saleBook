package gui.util;

import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author xthe_white_lionx
 */
public class RibbonTabUtil {

    /**
     * Creates a RibbonTab with the specified title and ribbonGroups
     *
     * @param title the title of the ribbonTab
     * @param ribbonGroups the ribbonGroups of the RibbonTab
     * @return the new created RibbonTab
     */
    public static @NotNull RibbonTab createRibbonTab(@NotNull String title,
                                                       RibbonGroup @NotNull ... ribbonGroups){
        RibbonTab result = new RibbonTab(title);
        result.getRibbonGroups().addAll(ribbonGroups);
        return result;
    }
}
