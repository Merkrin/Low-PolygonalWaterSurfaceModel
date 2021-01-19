package ru.hse.graphics;

import org.lwjgl.opengl.GL11;
import ru.hse.engine.ICamera;
import ru.hse.engine.Light;
import ru.hse.water.utils.WaterTile;

public class WaterRenderer {
    private final WaterShader WATER_SHADER;

    public WaterRenderer() {
        WATER_SHADER = new WaterShader();
    }

    public void render(WaterTile water, ICamera camera, Light light) {
        prepare(water, camera, light);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, water.getVERTEX_AMOUNT());

        finish(water);
    }

    public void cleanUp() {
        WATER_SHADER.cleanUp();
    }

    private void prepare(WaterTile water, ICamera camera, Light light) {
        water.getVAO().bind();

        prepareShader(water, camera, light);
    }

    private void finish(WaterTile water) {
        water.getVAO().unbind();
        WATER_SHADER.stop();
    }

    private void prepareShader(WaterTile water, ICamera camera, Light light){
        WATER_SHADER.start();

        loadCameraVariables(camera);

        WATER_SHADER.height.loadFloat(water.getHEIGHT());
    }

    private void loadCameraVariables(ICamera camera){
        WATER_SHADER.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
    }
}
