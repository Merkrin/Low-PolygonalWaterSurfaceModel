package ru.hse.utils;

import org.lwjgl.LWJGLException;

public class WindowBuilder {
    private static final String TITLE = "Low-Polygonal water surface model";

    private final int WIDTH;
    private final int HEIGHT;
    private final int FPS_CAP;

    WindowBuilder(int width, int height, int fpsCap){
        WIDTH = width;
        HEIGHT = height;
        FPS_CAP = fpsCap;
    }

    public Window create() throws LWJGLException {
        return new Window(new Context(), this);
    }

    int getWidth() {
        return WIDTH;
    }

    int getHeight(){
        return HEIGHT;
    }

    int getFpsCap() {
        return FPS_CAP;
    }

    String getTitle() {
        return TITLE;
    }
}
