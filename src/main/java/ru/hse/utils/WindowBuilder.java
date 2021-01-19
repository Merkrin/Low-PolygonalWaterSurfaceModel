package ru.hse.utils;

public class WindowBuilder {
    private static final String TITLE = "Low-Polygonal water ";

    private final int WIDTH;
    private final int HEIGHT;
    private final int FPS_CAP;

    private boolean hasVSync = false;
    private boolean withAntialiasing = false;
    private boolean isFullScreen = false;

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

    public WindowBuilder fullScreen(boolean full){
        this.isFullScreen = full;
        return this;
    }

    public Window create(){
        return new Window(new Context(3,3), this);
    }

    protected int getWIDTH() {
        return WIDTH;
    }

    protected int getHEIGHT(){
        return HEIGHT;
    }

    protected int getFPS_CAP() {
        return FPS_CAP;
    }

    protected String getTITLE() {
        return TITLE;
    }

    protected boolean hasVSync() {
        return hasVSync;
    }

    protected boolean isWithAntialiasing() {
        return withAntialiasing;
    }

    protected boolean isFullScreen() {
        return isFullScreen;
    }
}
