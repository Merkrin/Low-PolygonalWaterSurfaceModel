package ru.hse.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileBuffer {
    private static final String FILE_SEPARATOR =
            System.getProperty("file.separator");

    private String path;
    private String name;

    public FileBuffer(String path) {
        this.path = FILE_SEPARATOR + path;

        String[] directories = path.split(FILE_SEPARATOR);

        this.name = directories[directories.length - 1];
    }

    public FileBuffer(String... paths) {
        path = "";

        for (String part : paths)
            path += (FILE_SEPARATOR + part);

        String[] directories = path.split(FILE_SEPARATOR);

        name = directories[directories.length - 1];
    }

    public FileBuffer(FileBuffer file, String subFile) {
        path = file.path + FILE_SEPARATOR + subFile;

        name = subFile;
    }

    public FileBuffer(FileBuffer file, String... subFiles) {
        path = file.path;

        for (String part : subFiles)
            path += (FILE_SEPARATOR + part);

        String[] directories = path.split(FILE_SEPARATOR);

        name = directories[directories.length - 1];
    }

    public InputStream getInputStream(){
//        InputStream a = getClass().getResourceAsStream("/"+ name);//this.getClass().getResourceAsStream(path);
//        return a;
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    public BufferedReader getBufferedReader() throws Exception{
        InputStreamReader inputStreamReader =
                new InputStreamReader(getInputStream());

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        return bufferedReader;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return path;
    }
}
