package ru.hse.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import ru.hse.engine.ICamera;
import ru.hse.engine.Light;
import ru.hse.openGL.utils.GraphicsUtils;
import ru.hse.utils.Configs;
import ru.hse.water.utils.WaterTile;

/**
 * Water renderer class.
 */
public class WaterRenderer {
    private final WaterShader WATER_SHADER;

    private float time = 0;

    /**
     * The class' constructor.
     */
    public WaterRenderer() {
        WATER_SHADER = new WaterShader();
    }

    /**
     * Method for waterTile rendering.
     *
     * @param waterTile         water tile class instance
     * @param camera            active camera class instance
     * @param light             used light class instance
     * @param reflectionTexture texture of water reflection
     * @param refractionTexture texture of water refraction
     * @param depthTexture      texture of depth
     */
    public void render(WaterTile waterTile,
                       ICamera camera, Light light,
                       int reflectionTexture,
                       int refractionTexture,
                       int depthTexture) {
        prepare(waterTile, camera, light);

        bindTextures(reflectionTexture, refractionTexture, depthTexture);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, waterTile.getVertexAmount());

        finish(waterTile);
    }

    /**
     * Method for preparation for rendering.
     *
     * @param waterTile water tile class instance
     * @param camera    active camera class instance
     * @param light     used light class instance
     */
    private void prepare(WaterTile waterTile, ICamera camera, Light light) {
        waterTile.getVao().bind();

        GraphicsUtils.enableAlphaBlending();

        prepareShader(waterTile, camera, light);
    }

    /**
     * Method for textures binding.
     *
     * @param reflectionTexture texture of water reflection
     * @param refractionTexture texture of water refraction
     * @param depthTexture      texture of depth
     */
    private void bindTextures(int reflectionTexture, int refractionTexture, int depthTexture) {
        bindTextureToUnit(reflectionTexture, WaterShader.REFLECTION_TEXTURE_UNIT);
        bindTextureToUnit(refractionTexture, WaterShader.REFRACTION_TEXTURE_UNIT);
        bindTextureToUnit(depthTexture, WaterShader.DEPTH_TEXTURE_UNIT);
    }

    /**
     * Method for the texture binding.
     *
     * @param textureId   id of the texture
     * @param textureUnit id of the texture unit
     */
    private void bindTextureToUnit(int textureId, int textureUnit) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }

    /**
     * Method for shader preparation.
     *
     * @param waterTile water tile class instance
     * @param camera    active camera class instance
     * @param light     used light class instance
     */
    private void prepareShader(WaterTile waterTile,
                               ICamera camera, Light light) {
        WATER_SHADER.start();

        updateTime();

        loadCameraVariables(camera);
        loadLightVariables(light);

        WATER_SHADER.height.loadFloat(waterTile.getHeight());

        WATER_SHADER.waveLength.loadFloat(Configs.getWaveLength());
        WATER_SHADER.waveAmplitude.loadFloat(Configs.getWaveAmplitude());

        WATER_SHADER.applyAnimation.loadBoolean(Configs.getAnimateWater());
    }

    /**
     * Method for camera variables loading to shader.
     *
     * @param camera active camera class instance
     */
    private void loadCameraVariables(ICamera camera) {
        WATER_SHADER.projectionViewMatrix
                .loadMatrix(camera.getProjectionViewMatrix());
        WATER_SHADER.cameraPosition
                .loadVector3f(camera.getCameraPosition());
        WATER_SHADER.nearFarPlanes
                .loadVector2f(camera.getNearPlane(), camera.getFarPlane());
    }

    /**
     * Method for light variables loading to shader.
     *
     * @param light used light class instance
     */
    private void loadLightVariables(Light light) {
        WATER_SHADER.lightBias
                .loadVector2f(light.getLightBias());
        WATER_SHADER.lightDirection
                .loadVector3f(light.getDirection());
        WATER_SHADER.lightColor
                .loadVector3f(light.getColor().getColor());
    }

    /**
     * Method for time of wave updating (like position changing).
     */
    private void updateTime() {
        time += Configs.getWaveSpeed();
        WATER_SHADER.waveTime.loadFloat(time);
    }

    /**
     * Method for rendering stopping.
     *
     * @param waterTile water tile class instance
     */
    private void finish(WaterTile waterTile) {
        waterTile.getVao().unbind();

        WATER_SHADER.stop();

        GraphicsUtils.disableBlending();
    }

    /**
     * "Destructor" of the class.
     */
    public void cleanUp() {
        WATER_SHADER.cleanUp();
    }
}
