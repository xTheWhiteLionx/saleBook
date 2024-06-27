package utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * This class inherits some Utilities for {@link File}s.
 *
 * @author xthe_white_lionx
 */
public class FileUtils {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private FileUtils() {
    }

    /**
     * Returns the extension of the specified file
     *
     * @param file the file which extension should be checked
     * @return the extension of the file or an empty string if the file has no extension
     */
    public static @NotNull String getExtension(@NotNull File file) {
        String extension = "";

        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;

    }

    /**
     * Creates the specified file, including any
     * necessary but nonexistent parent directories. Note that if this
     * operation fails it may have succeeded in creating some of the necessary
     * parent directories.
     *
     * @param file the file which should be created
     * @return  {@code true} if and only if the directory was created and does not exist yet,
     *          along with all necessary parent directories; {@code false}
     *          otherwise
     */
    public static boolean createIfAbsent(@NotNull File file) {
        if (!file.exists()) {
            return file.mkdirs();
        }
        return false;
    }
}
