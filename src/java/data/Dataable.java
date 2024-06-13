package data;

/**
 * This is in interface to convert a komplex object to its data form
 *
 * @author xThe_white_Lionx
 * @Date 03.06.2024
 * @param <D> Type of the data
 */
public interface Dataable<D> {

    /**
     * Returns the data representation of this Dataable
     *
     * @return the data representation of this Dataable
     */
    D toData();
}
