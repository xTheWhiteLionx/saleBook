package logic;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * An ProgressWriter is a bridge from character streams to byte streams
 *
 * @author xthe_white_lionx
 */
public class ProgressWriter extends OutputStreamWriter {
    /**
     * The total bytes which are written until now
     */
    private int totalBytes = 0;

    /**
     * The progressListener to communicate progress advancing
     */
    private final ProgressListener progressListener;

    /**
     * Constructor for a ProgressWriter
     *
     * @param outputStream     the outputStream
     * @param progressListener a progress advancing will be communicated this
     *                         progressListener
     */
    public ProgressWriter(@NotNull OutputStream outputStream, @NotNull ProgressListener progressListener) {
        super(outputStream);
        this.progressListener = progressListener;
    }

    @Override
    public void write(int c) throws IOException {
        super.write(c);
        this.progressListener.updateProgress(++this.totalBytes);
    }

    @Override
    public void write(char @NotNull[] cbuf, int off, int len) throws IOException {
        super.write(cbuf, off, len);
        this.totalBytes += len;
        this.progressListener.updateProgress(this.totalBytes);
    }

    @Override
    public void write(@NotNull String str, int off, int len) throws IOException {
        super.write(str, off, len);
        this.totalBytes += len;
        this.progressListener.updateProgress(this.totalBytes);
    }
}
