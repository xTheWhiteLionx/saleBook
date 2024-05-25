package logic.products;

/**
 * This interface presents a Product.
 *
 * @author xthe_white_lionx
 */
public interface Product {
    /**
     * Returns the id of the implemented object
     *
     * @return id of the implemented object
     */
    int getId();

    /**
     * Returns the simple class name of the implemented class
     *
     * @return the simple class name of the implemented class
     */
   default String getSimpleName(){
       return this.getClass().getSimpleName();
   }
}
