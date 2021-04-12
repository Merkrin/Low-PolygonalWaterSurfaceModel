package ru.hse.utils;

import org.lwjgl.LWJGLException;

public class WindowBuilder {
    private static final String TITLE = "Low-Polygonal water surface model";

    private final int WIDTH;
    private final int HEIGHT;
    private final int FPS_CAP;

    protected WindowBuilder(int width, int height, int fpsCap){
        WIDTH = width;
        HEIGHT = height;
        FPS_CAP = fpsCap;
    }

    public Window create() throws LWJGLException {
        return new Window(new Context(3,3), this);
    }

    protected int getWidth() {
        return WIDTH;
    }

    protected int getHeight(){
        return HEIGHT;
    }

    protected int getFpsCap() {
        return FPS_CAP;
    }

    protected String getTitle() {
        return TITLE;
    }
}
