package ru.hse.engine;

import org.lwjgl.LWJGLException;
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
import ru.hse.utils.Configs;
import ru.hse.utils.InputParser;
import ru.hse.utils.Window;
import ru.hse.water.utils.WaterTile;

import java.io.IOException;

/**
 * Render engine class.
 */
class RenderEngine {
    private static final float REFRACTION_OFFSET = 1f;
    private static final float REFLECTION_OFFSET = 0.1f;

    private final Fbo reflectionFbo;
    private final Fbo refractionFbo;

    private final WaterRenderer waterRenderer;

    private final Window window;

    /**
     * The class' constructor.
     *
     * @param fpsRate       maximal frames per second rate
     * @param displayWidth  width of the window to show
     * @param displayHeight height of the window to show
     */
    public RenderEngine(int fpsRate, int displayWidth, int displayHeight)
            throws LWJGLException {
        window = Window.newWindow(displayWidth, displayHeight, fpsRate).create();

        waterRenderer = new WaterRenderer();

        refractionFbo = createWaterFbo(displayWidth / 2, displayHeight / 2,
                true);
        reflectionFbo = createWaterFbo(displayWidth, displayHeight,
                false);
    }

    /**
     * Main rendering method.
     *
     * @param terrain   terrain class instance
     * @param waterTile water tile class instance
     * @param camera    active camera class instance
     * @param light     light class instance
     */
    public void render(Terrain terrain, WaterTile waterTile,
                       ICamera camera, Light light) {
        if (Configs.getShowWater()) {
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            doReflectionPass(terrain, camera, light, waterTile.getHeight());
            doRefractionPass(terrain, camera, light, waterTile.getHeight());

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
        }

        doMainRenderPass(terrain, waterTile, camera, light);
    }

    /**
     * Method for reflection texture getting.
     *
     * @param terrain     terrain class instance
     * @param camera      active camera class instance
     * @param light       light class instance
     * @param waterHeight water surface height
     */
    private void doReflectionPass(Terrain terrain,
                                  ICamera camera, Light light,
                                  float waterHeight) {
        reflectionFbo.bindForRender(0);

        camera.reflect();

        prepare();
        terrain.render(camera,
                light,
                new Vector4f(0, 1, 0, -waterHeight + REFLECTION_OFFSET));

        camera.reflect();

        reflectionFbo.unbindAfterRender();
    }

    /**
     * Method for refraction texture getting.
     *
     * @param terrain     terrain class instance
     * @param camera      active camera class instance
     * @param light       light class instance
     * @param waterHeight water surface height
     */
    private void doRefractionPass(Terrain terrain,
                                  ICamera camera, Light light,
                                  float waterHeight) {
        refractionFbo.bindForRender(0);

        prepare();
        terrain.render(camera,
                light,
                new Vector4f(0, -1, 0, waterHeight + REFRACTION_OFFSET));

        refractionFbo.unbindAfterRender();
    }

    /**
     * Method for main rendering.
     *
     * @param terrain   terrain class instance
     * @param waterTile water tile class instance
     * @param camera    active camera class instance
     * @param light     light class instance
     */
    private void doMainRenderPass(Terrain terrain, WaterTile waterTile,
                                  ICamera camera, Light light) {
        prepare();

        terrain.render(camera, light, new Vector4f(0, 0, 0, 0));

        if (Configs.getShowWater()) {
            GraphicsUtils.setWireframe(Keyboard.isKeyDown(Keyboard.KEY_G));

            waterRenderer.render(waterTile, camera, light,
                    reflectionFbo.getColorBuffer(0),
                    refractionFbo.getColorBuffer(0),
                    refractionFbo.getDepthBuffer());

            GraphicsUtils.setWireframe(false);
        }

        window.update();

        reflectionFbo.blitToScreen(0);
        refractionFbo.blitToScreen(0);
        try {
            InputParser.performInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for screen preparing, i.e. clearing and setting.
     */
    private void prepare() {
        GL11.glClearColor(1f, 1f, 1f, 1f);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);

        GraphicsUtils.cullBackFaces(true);
        GraphicsUtils.enableDepthTesting(true);
        GraphicsUtils.setAntialiasing(true);
    }

    /**
     * Method for custom frame buffer object creation.
     *
     * @param width              FBO width
     * @param height             FBO height
     * @param useTextureForDepth boolean value if depth effect
     *                           should be added or not
     * @return created FBO class instance
     */
    private static Fbo createWaterFbo(int width, int height,
                                      boolean useTextureForDepth) {
        Attachment colorAttachment = new TextureAttachment(GL11.GL_RGBA8);
        Attachment depthAttachment;

        if (useTextureForDepth)
            depthAttachment =
                    new TextureAttachment(GL14.GL_DEPTH_COMPONENT24);
        else
            depthAttachment =
                    new RenderBufferAttachment(GL14.GL_DEPTH_COMPONENT24);

        return Fbo.newFbo(width, height)
                .addColorAttachment(0, colorAttachment)
                .addDepthAttachment(depthAttachment)
                .initialize();
    }

    /**
     * "Destructor" of the class.
     */
    public void cleanUp() {
        reflectionFbo.delete();
        refractionFbo.delete();

        waterRenderer.cleanUp();

        window.destroy();
    }

    /**
     * Getter of active window
     *
     * @return window class instance
     */
    public Window getWindow() {
        return window;
    }
}
