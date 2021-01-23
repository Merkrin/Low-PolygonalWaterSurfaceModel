package ru.hse.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
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

public class RenderEngine {
    private static final float OFFSET = 2f;

    private final Window WINDOW;

    private final WaterRenderer WATER_RENDERER;

    private final Fbo reflectionFbo;
    private final Fbo refractionFbo;

    public RenderEngine(int fps, int displayWidth, int displayHeight) {
        WINDOW = Window.newWindow(displayWidth, displayHeight, fps)
                .antialias(true)
                .create();

        WATER_RENDERER = new WaterRenderer();

        refractionFbo = createWaterFbo(displayWidth/2, displayHeight/2, true);
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
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);

        GraphicsUtils.cullBackFaces(true);

        GraphicsUtils.enableDepthTesting(true);

        GraphicsUtils.antialias(true);
    }

    private void doReflectionPass(Terrain terrain, ICamera camera, Light light, float waterHeight) {
        reflectionFbo.bindForRender(0);
        camera.reflect();
        prepare();
        terrain.render(camera, light, new Vector4f(0, 1, 0, -waterHeight));
        camera.reflect();
        reflectionFbo.unbindAfterRender();
    }

    private void doRefractionPass(Terrain terrain, ICamera camera, Light light, float waterHeight) {
        refractionFbo.bindForRender(0);
        prepare();
        terrain.render(camera, light, new Vector4f(0, -1, 0, waterHeight + OFFSET));
        refractionFbo.unbindAfterRender();
    }

    private void doMainRenderPass(Terrain terrain, WaterTile waterTile, ICamera camera, Light light) {
        prepare();
        terrain.render(camera, light, new Vector4f(0, 0, 0, 0));
        GraphicsUtils.goWireframe(Keyboard.isKeyDown(Keyboard.KEY_G));
        WATER_RENDERER.render(waterTile, camera, light, reflectionFbo.getColourBuffer(0), refractionFbo.getColourBuffer(0),
                refractionFbo.getDepthBuffer());
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
}
