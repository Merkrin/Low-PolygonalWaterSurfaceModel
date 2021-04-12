package ru.hse.utils;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Window {
    private static final float MILLISECONDS_IN_SECOND = 1000f;

    private final int fpsCap;

    private long lastFrameTime;
    private float delta;

    protected Window(Context context, WindowBuilder windowBuilder)
            throws LWJGLException {
        this.fpsCap = windowBuilder.getFpsCap();

        DisplayMode resolution = getStartResolution(windowBuilder);

        Display.setInitialBackground(1, 1, 1);

        setResolution(resolution);

        Display.setTitle(windowBuilder.getTitle());

        Display.create(new PixelFormat().withDepthBits(24).withSamples(4),
                context.getAttribs());

        GL11.glViewport(0, 0,
                resolution.getWidth(), resolution.getHeight());

        lastFrameTime = getCurrentTime();
    }

    public static WindowBuilder newWindow(int width, int height, int fpsCap) {
        return new WindowBuilder(width, height, fpsCap);
    }

    /**
     * Method for resolution setting.
     *
     * @param resolution resolution of the window
     */
    public void setResolution(DisplayMode resolution) {
        try {
            Display.setDisplayMode(resolution);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for frame update.
     */
    public void update() {
        Display.sync(fpsCap);
        Display.update();

        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / MILLISECONDS_IN_SECOND;
        lastFrameTime = currentFrameTime;
    }

    /**
     * Method for closing request check.
     *
     * @return true if the window has to be closed and false otherwise
     */
    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    /**
     * "Destructor" of the window.
     */
    public void destroy() {
        Display.destroy();
    }

    /**
     * Method for window resolution getting.
     *
     * @param windowBuilder the window's builder
     * @return display mode for the window
     */
    private DisplayMode getStartResolution(WindowBuilder windowBuilder) {
        return new DisplayMode(windowBuilder.getWidth(),
                windowBuilder.getHeight());

    }

    private long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    public float getFrameTimeSeconds() {
        return delta;
    }
}
