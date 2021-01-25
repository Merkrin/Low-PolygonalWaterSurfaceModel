package ru.hse.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import ru.hse.engine.ICamera;
import ru.hse.engine.Light;
import ru.hse.openGL.utils.GraphicsUtils;
import ru.hse.water.utils.WaterTile;

public class WaterRenderer {
    // TODO: make changeable
    private static final float WAVE_SPEED = 0.004f;

    private final WaterShader WATER_SHADER;

    private float time = 0;

    public WaterRenderer() {
        WATER_SHADER = new WaterShader();
    }

    public void render(WaterTile water, ICamera camera, Light light, int reflectionTexture, int refractionTexture, int depthTexture) {
        prepare(water, camera, light);
        bindTextures(reflectionTexture, refractionTexture, depthTexture);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, water.getVERTEX_AMOUNT());
        finish(water);
    }

    public void cleanUp() {
        WATER_SHADER.cleanUp();
    }

    private void prepare(WaterTile water, ICamera camera, Light light) {
        water.getVAO().bind();
        GraphicsUtils.enableAlphaBlending();
        prepareShader(water, camera, light);
    }

    private void bindTextures(int reflectionTexture, int refractionTexture, int depthTexture){
        bindTextureToUnit(reflectionTexture, WaterShader.REFLECT_TEX_UNIT);
        bindTextureToUnit(refractionTexture, WaterShader.REFRACT_TEX_UNIT);
        bindTextureToUnit(depthTexture, WaterShader.DEPTH_TEX_UNIT);
    }

    private void bindTextureToUnit(int textureId, int textureUnit){
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }

    private void finish(WaterTile water) {
        water.getVAO().unbind();
        WATER_SHADER.stop();
        GraphicsUtils.disableBlending();
    }

    private void prepareShader(WaterTile water, ICamera camera, Light light){
        WATER_SHADER.start();

        updateTime();

        loadCameraVariables(camera);

        WATER_SHADER.height.loadFloat(water.getHEIGHT());
    }

    private void loadCameraVariables(ICamera camera){
        WATER_SHADER.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
        WATER_SHADER.cameraPos.loadVector3f(camera.getPosition());
        WATER_SHADER.nearFarPlanes.loadVector2f(camera.getNearPlane(), camera.getFarPlane());
    }

    private void updateTime(){
        time += WAVE_SPEED;
        WATER_SHADER.waveTime.loadFloat(time);
    }
}
