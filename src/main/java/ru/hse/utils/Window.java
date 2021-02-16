package ru.hse.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Window {
    private static final int MIN_HEIGHT = 700;

    private final int fpsCap;

    private DisplayMode resolution;
    private boolean fullScreen;
    private float aspectRatio;

    private List<DisplayMode> availableResolutions = new ArrayList<DisplayMode>();

    protected Window(Context context, WindowBuilder settings) {
        this.fpsCap = settings.getFPS_CAP();
        try {
            getSuitableFullScreenModes();
            DisplayMode resolution = getStartResolution(settings);
            Display.setInitialBackground(1, 1, 1);
            this.aspectRatio = (float) resolution.getWidth() / resolution.getHeight();
            setResolution(resolution, settings.isFullScreen());
//            if (settings.hasIcon()) {
//                Display.setIcon(settings.getIcon());
//            }
            Display.setVSyncEnabled(settings.hasVSync());
            Display.setTitle(settings.getTITLE());
            Display.create(new PixelFormat().withDepthBits(24).withSamples(4), context.getAttribs());
            GL11.glViewport(0, 0, resolution.getWidth(), resolution.getHeight());
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public float getAspectRatio(){
        return aspectRatio;
    }

    public DisplayMode getResolution(){
        return resolution;
    }

    public boolean isFullScreen(){
        return fullScreen;
    }

    public List<DisplayMode> getAvailableResolutions(){
        return availableResolutions;
    }

    public void setResolution(DisplayMode resolution, boolean fullscreen) {
        try {
            Display.setDisplayMode(resolution);
            this.resolution = resolution;
            if (fullscreen && resolution.isFullscreenCapable()) {
                Display.setFullscreen(true);
                this.fullScreen = fullscreen;
            }
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        Display.sync(fpsCap);
        Display.update();

        performInput();
    }

    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    public void destroy() {
        Display.destroy();
    }

    public static WindowBuilder newWindow(int width, int height, int fpsCap) {
        return new WindowBuilder(width, height, fpsCap);
    }

    private void getSuitableFullScreenModes() throws LWJGLException {
        DisplayMode[] resolutions = Display.getAvailableDisplayModes();
        DisplayMode desktopResolution = Display.getDesktopDisplayMode();
        for (DisplayMode resolution : resolutions) {
            if (isSuitableFullScreenResolution(resolution, desktopResolution)) {
                availableResolutions.add(resolution);
            }
        }
    }

    private boolean isSuitableFullScreenResolution(DisplayMode resolution, DisplayMode desktopResolution) {
        if (resolution.getBitsPerPixel() == desktopResolution.getBitsPerPixel()) {
            if (resolution.getFrequency() == desktopResolution.getFrequency()) {
                float desktopAspect = (float) desktopResolution.getWidth() / desktopResolution.getHeight();
                float resAspect = (float) resolution.getWidth() / resolution.getHeight();
                float check = resAspect / desktopAspect;
                if (check > 0.95f && check < 1.05f) {
                    return resolution.getHeight() > MIN_HEIGHT;
                }
            }
        }
        return false;
    }

    private DisplayMode getFullScreenDisplayMode(int width, int height) {
        for (DisplayMode potentialMode : availableResolutions) {
            if (potentialMode.getWidth() == width && potentialMode.getHeight() == height) {
                return potentialMode;
            }
        }
        return null;
    }

    private DisplayMode getStartResolution(WindowBuilder settings) {
        if (settings.isFullScreen()) {
            DisplayMode fullScreenMode = getFullScreenDisplayMode(settings.getWIDTH(), settings.getHEIGHT());
            if (fullScreenMode != null) {
                return fullScreenMode;
            }
            settings.fullScreen(false);
        }
        return new DisplayMode(settings.getWIDTH(), settings.getHEIGHT());

    }

    private void performInput() {
        if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
            takeScreenshot();
        }
    }

    private void takeScreenshot() {
        GL11.glReadBuffer(GL11.GL_FRONT);
        int width = Display.getDisplayMode().getWidth();
        int height= Display.getDisplayMode().getHeight();
        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
//        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        File file = new File(dtf.format(now) + ".png");
        String format = "PNG";
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                int i = y * width * bpp + x * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x,y,(0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
