package ru.hse.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector4f;
import ru.hse.graphics.WaterRenderer;
import ru.hse.openGL.objects.Attachment;
import ru.hse.openGL.objects.Fbo;
import ru.hse.openGL.objects.RenderBufferAttachment;
import ru.hse.openGL.objects.TextureAttachment;
import ru.hse.openGL.utils.GraphicsUtils;
import ru.hse.terrain.generation.Terrain;
import ru.hse.utils.Window;
import ru.hse.water.utils.WaterTile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RenderEngine {
    private static final float REFRACT_OFFSET = 1f;
    private static final float REFLECT_OFFSET = 0.1f;

    private final Window WINDOW;

    private final WaterRenderer WATER_RENDERER;

    private final Fbo reflectionFbo;
    private final Fbo refractionFbo;

    public RenderEngine(int fps, int displayWidth, int displayHeight) {
        WINDOW = Window.newWindow(displayWidth, displayHeight, fps)
                .antialias(true)
                .create();

        WATER_RENDERER = new WaterRenderer();

        refractionFbo = createWaterFbo(displayWidth / 2, displayHeight / 2, true);
        reflectionFbo = createWaterFbo(displayWidth, displayHeight, false);
    }

    public void render(Terrain terrain, WaterTile waterTile, ICamera camera, Light light) {
        GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
        doReflectionPass(terrain, camera, light, waterTile.getHEIGHT());
        doRefractionPass(terrain, camera, light, waterTile.getHEIGHT());
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
        doMainRenderPass(terrain, waterTile, camera, light);
    }

    public Window getWINDOW() {
        return WINDOW;
    }

    public void close() {
        reflectionFbo.delete();
        refractionFbo.delete();
        WATER_RENDERER.cleanUp();
        WINDOW.destroy();
    }

    private void prepare() {
        GL11.glClearColor(1f, 1f, 1f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
//        GraphicsUtils.cullBackFaces(true);
        GraphicsUtils.enableDepthTesting(true);
        GraphicsUtils.antialias(true);
    }

    private void doReflectionPass(Terrain terrain, ICamera camera, Light light, float waterHeight) {
        reflectionFbo.bindForRender(0);
        camera.reflect();
        prepare();
        terrain.render(camera, light, new Vector4f(0, 1, 0, -waterHeight + REFLECT_OFFSET));
        camera.reflect();
        reflectionFbo.unbindAfterRender();
    }

    private void doRefractionPass(Terrain terrain, ICamera camera, Light light, float waterHeight) {
        refractionFbo.bindForRender(0);
        prepare();
        terrain.render(camera, light, new Vector4f(0, -1, 0, waterHeight + REFRACT_OFFSET));
        refractionFbo.unbindAfterRender();
    }

    private void doMainRenderPass(Terrain terrain, WaterTile waterTile, ICamera camera, Light light) {
        prepare();
        terrain.render(camera, light, new Vector4f(0, 0, 0, 0));
        GraphicsUtils.goWireframe(Keyboard.isKeyDown(Keyboard.KEY_G));
        WATER_RENDERER.render(waterTile, camera, light, reflectionFbo.getColourBuffer(0), refractionFbo.getColourBuffer(0),
                refractionFbo.getDepthBuffer());
//        performInput();
        GraphicsUtils.goWireframe(false);
        WINDOW.update();
    }

    private static Fbo createWaterFbo(int width, int height, boolean useTextureForDepth) {
        Attachment colourAttach = new TextureAttachment(GL11.GL_RGBA8);
        Attachment depthAttach;
        if (useTextureForDepth) {
            depthAttach = new TextureAttachment(GL14.GL_DEPTH_COMPONENT24);
        } else {
            depthAttach = new RenderBufferAttachment(GL14.GL_DEPTH_COMPONENT24);
        }
        return Fbo.newFbo(width, height).addColourAttachment(0, colourAttach).addDepthAttachment(depthAttach).init();
    }

//    private void performInput() {
//        if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
//            reflectionFbo.bindForRender(0);
//            refractionFbo.bindForRender(0);
//            takeScreenshot();
//            refractionFbo.unbindAfterRender();
//            reflectionFbo.unbindAfterRender();
//        }
//    }

//    private void takeScreenshot() {
//
//
//        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
//        GL11.glReadBuffer(GL11.GL_FRONT);
//        int width = Display.getDisplayMode().getWidth();
//        int height= Display.getDisplayMode().getHeight();
//        int bpp = 4;
//        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
//        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
//
//        DateTimeFormatter dtf =
//                DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
//        LocalDateTime now = LocalDateTime.now();
//
//        File file = new File(dtf.format(now) + ".png");
//        String format = "PNG";
//        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
//        for(int y = 0; y < height; ++y) {
//            for(int x = 0; x < width; ++x) {
//                int i = y * width * bpp + x * bpp;
//                int r = buffer.get(i) & 0xFF;
//                int g = buffer.get(i + 1) & 0xFF;
//                int b = buffer.get(i + 2) & 0xFF;
//                image.setRGB(x,y,(0xFF << 24) | (r << 16) | (g << 8) | b);
//            }
//        }
//        try {
//            ImageIO.write(image, format, file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
