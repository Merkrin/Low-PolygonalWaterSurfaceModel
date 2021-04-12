package ru.hse.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility file buffer class.
 */
@SuppressWarnings("ALL")
public class FileBuffer {
    private static final String FILE_SEPARATOR =
            System.getProperty("file.separator");

    private final String name;

    private String path;

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

        //path = "";

        for (String part : paths)
            stringBuilder.append(FILE_SEPARATOR + part);
            //path += (FILE_SEPARATOR + part);

        path = stringBuilder.toString();

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
