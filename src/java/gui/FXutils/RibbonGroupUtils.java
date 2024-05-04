package gui.FXutils;

import com.pixelduke.control.ribbon.RibbonGroup;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
/**
 * Some Utility's for RibbonGroup's.
 *
 * @author xthe_white_lionx
 */
public class RibbonGroupUtils {

    /**
     * Creates a RibbonGroup with the specified title and nodes
     *
     * @param title the title of the ribbonGroup
     * @param nodes the nodes of the ribbonGroup
     * @return the newly created ribbonGroup
     */
    public static @NotNull RibbonGroup createRibbonGroup(@NotNull String title, Node @NotNull ... nodes){
        RibbonGroup result = new RibbonGroup();
        result.setTitle(title);
        result.getNodes().addAll(nodes);
        return result;
    }
}
