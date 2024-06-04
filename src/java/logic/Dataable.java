package logic;

/**
 *
 *
 * @author xThe_white_Lionx
 * @Date 03.06.2024
 */
public interface Dataable<D> {

    /**
     * Returns the data representation of this Dataable
     *
     * @return the data representation of this Dataable
     */
    D toData();
}
