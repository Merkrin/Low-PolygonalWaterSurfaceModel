package ru.hse.utils;

import org.lwjgl.LWJGLException;

public class WindowBuilder {
    private static final String TITLE = "Low-Polygonal water surface model";

    private final int WIDTH;
    private final int HEIGHT;
    private final int FPS_CAP;

    private boolean hasVSync = false;
    private boolean withAntialiasing = false;

    protected WindowBuilder(int width, int height, int fpsCap){
        WIDTH = width;
        HEIGHT = height;
        FPS_CAP = fpsCap;
    }

    public WindowBuilder withVSync(boolean vSync){
        hasVSync = vSync;

        return this;
    }

    public WindowBuilder antialias(boolean antialias){
        this.withAntialiasing = antialias;
        return this;
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

    protected boolean hasVSync() {
        return hasVSync;
    }

    protected boolean isWithAntialiasing() {
        return withAntialiasing;
    }
}
