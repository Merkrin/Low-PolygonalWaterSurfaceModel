package ru.hse.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility file buffer class.
 */
public class FileBuffer {
    private static final String FILE_SEPARATOR =
            System.getProperty("file.separator");

    private final String name;

    private final String path;

    /**
     * The class' constructor.
     * @param path path to file
     */
    public FileBuffer(String path) {
        this.path = FILE_SEPARATOR + path;

        String[] directories = path.split(FILE_SEPARATOR);

        this.name = directories[directories.length - 1];
    }

    /**
     * The class' constructor.
     * @param paths paths to files
     */
    public FileBuffer(String... paths) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String part : paths)
            stringBuilder.append(FILE_SEPARATOR).append(part);

        path = stringBuilder.toString();

        String[] directories = path.split(FILE_SEPARATOR);

        name = directories[directories.length - 1];
    }

    /**
     * Input stream getter.
     * @return input stream
     */
    private InputStream getInputStream(){
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    /**
     * Buffered reader getter.
     * @return buffered reader
     */
    public BufferedReader getBufferedReader(){
        InputStreamReader inputStreamReader =
                new InputStreamReader(getInputStream());

        return new BufferedReader(inputStreamReader);
    }

    @Override
    public String toString() {
        return path;
    }
}
