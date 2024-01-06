package logic;

/**
 * This is in interface for progressListener. The progress of an progressListener can be updated
 * by the {@link #updateProgress(int)} methode.
 *
 * @author xthe_white_lionx
 */
public interface ProgressListener {
    /**
     * Updates the progress to the specified totalBytes
     *
     * @param totalBytes the totalBytes that the progress has currently
     */
    void updateProgress(int totalBytes);
}
