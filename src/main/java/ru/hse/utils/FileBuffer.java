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

    private String path;
    private String name;

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
        path = "";

        for (String part : paths)
            path += (FILE_SEPARATOR + part);

        String[] directories = path.split(FILE_SEPARATOR);

        name = directories[directories.length - 1];
    }

    /**
     * Input stream getter.
     * @return input stream
     */
    public InputStream getInputStream(){
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
