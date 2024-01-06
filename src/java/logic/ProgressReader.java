package logic;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * An ProgressReader is a bridge from byte streams to character streams.
 *
 * @author xthe_white_lionx
 */
public class ProgressReader extends InputStreamReader {
    /**
     * The total bytes which are read until now
     */
    private int totalBytes = 0;

    /**
     * The progressListener to communicate progress advancing
     */
    private final ProgressListener progressListener;

    /**
     * Constructor for an ProgressReader
     *
     * @param inputStream the inputStream
     * @param progressListener a progress advancing will be communicated this
     *                         progressListener
     */
    public ProgressReader(@NotNull InputStream inputStream, @NotNull ProgressListener progressListener) {
        super(inputStream);
        this.progressListener = progressListener;
    }

    @Override
    public int read() throws IOException {
        int bytes = super.read();
        this.progressListener.updateProgress(++this.totalBytes);
        return bytes;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        int bytes = super.read(target);
        this.progressListener.updateProgress(++this.totalBytes);
        return bytes;
    }

    @Override
    public int read(char @NotNull [] cbuf, int off, int len) throws IOException {
        int bytes = super.read(cbuf, off, len);
        this.totalBytes += bytes;
        this.progressListener.updateProgress(this.totalBytes);
        return bytes;
    }
}
