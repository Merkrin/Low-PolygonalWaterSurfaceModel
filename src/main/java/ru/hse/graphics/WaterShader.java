package ru.hse.graphics;

import ru.hse.openGL.utils.*;
import ru.hse.utils.FileBuffer;

public class WaterShader extends ShaderProgram {
    protected static final int REFLECT_TEX_UNIT = 0;
    protected static final int REFRACT_TEX_UNIT = 1;
    protected static final int DEPTH_TEX_UNIT = 2;

    private static final FileBuffer VERTEX_SHADER = new FileBuffer("ru/hse/openGL/shaders", "waterVertex.glsl");
    private static final FileBuffer FRAGMENT_SHADER = new FileBuffer("ru/hse/openGL/shaders", "waterFragment.glsl");

    public UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
    public UniformFloat height = new UniformFloat("height");
    public UniformVector3f cameraPos = new UniformVector3f("cameraPos");
    public UniformVector2f nearFarPlanes = new UniformVector2f("nearFarPlanes");

    protected UniformFloat waveTime = new UniformFloat("waveTime");

    protected UniformVector3f lightDirection = new UniformVector3f("lightDirection");
    protected UniformVector3f lightColor = new UniformVector3f("lightColour");
    protected UniformVector2f lightBias = new UniformVector2f("lightBias");

    protected UniformSampler reflectionTexture = new UniformSampler("reflectionTexture");
    protected UniformSampler refractionTexture = new UniformSampler("refractionTexture");
    protected UniformSampler depthTexture = new UniformSampler("depthTexture");

    public WaterShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
        super.storeAllUniformLocations(projectionViewMatrix, height, reflectionTexture, refractionTexture, depthTexture,
                cameraPos, nearFarPlanes, waveTime, lightDirection, lightColor, lightBias);
        linkTextureUnits();
    }

    /**
     * Links the texture samplers in the fragment shader to the texture units
     * that they're going to be sampling from.
     */
    private void linkTextureUnits() {
        super.start();
        reflectionTexture.loadTexUnit(REFLECT_TEX_UNIT);
        refractionTexture.loadTexUnit(REFRACT_TEX_UNIT);
        depthTexture.loadTexUnit(DEPTH_TEX_UNIT);
        super.stop();
    }

}
