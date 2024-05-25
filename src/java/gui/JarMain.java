package gui;

/**
 * Wrapper class is necessary as the main class for our program must not inherit
 * from {@link javafx.application.Application}
 *
 * @author xthe_white_lionx
 */
public final class JarMain {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private JarMain() {
    }

    /**
     * Starts the {@link ApplicationMain#main(String[])} methode with the specified args
     *
     * @param args the arguments for the main program
     */
    public static void main(String... args) {
        ApplicationMain.main(args);
    }
}
