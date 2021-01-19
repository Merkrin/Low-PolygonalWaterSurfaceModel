package ru.hse.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import ru.hse.graphics.WaterRenderer;
import ru.hse.openGL.utils.GraphicsUtils;
import ru.hse.terrain.generation.Terrain;
import ru.hse.utils.Window;
import ru.hse.water.utils.WaterTile;

public class RenderEngine {
    private final Window WINDOW;

    private final WaterRenderer WATER_RENDERER;

    public RenderEngine(int fps, int displayWidth, int displayHeight) {
        WINDOW = Window.newWindow(displayWidth, displayHeight, fps)
                .antialias(true)
                .create();

        WATER_RENDERER = new WaterRenderer();
    }

    public void render(Terrain terrain, WaterTile waterTile, ICamera camera, Light light) {
        doMainRenderPass(terrain, waterTile, camera, light);
    }

    public Window getWINDOW() {
        return WINDOW;
    }

    public void close() {
        WINDOW.destroy();
    }

    private void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);

        GraphicsUtils.cullBackFaces(true);

        GraphicsUtils.enableDepthTesting(true);

        GraphicsUtils.antialias(true);
    }

    private void doMainRenderPass(Terrain terrain, WaterTile waterTile, ICamera camera, Light light) {
        prepare();

        terrain.render(camera, light);

        GraphicsUtils.goWireframe(Keyboard.isKeyDown(Keyboard.KEY_G));
        WATER_RENDERER.render(waterTile, camera, light);
        GraphicsUtils.goWireframe(false);

        WINDOW.update();
    }
}
