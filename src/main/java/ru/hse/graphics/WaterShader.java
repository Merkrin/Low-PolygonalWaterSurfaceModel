package ru.hse.graphics;

import ru.hse.openGL.utils.*;
import ru.hse.utils.FileBuffer;

/**
 * Water shader storage class.
 */
public class WaterShader extends ShaderProgram {
    // Indexes of texture units.
    protected static final int REFLECTION_TEXTURE_UNIT = 0;
    protected static final int REFRACTION_TEXTURE_UNIT = 1;
    protected static final int DEPTH_TEXTURE_UNIT = 2;

    private static final FileBuffer VERTEX_SHADER =
            new FileBuffer("waterVertex.glsl");
    private static final FileBuffer FRAGMENT_SHADER =
            new FileBuffer("waterFragment.glsl");

    public UniformMatrix projectionViewMatrix =
            new UniformMatrix("projectionViewMatrix");

    public UniformFloat height =
            new UniformFloat("height");

    public UniformVector3f cameraPosition =
            new UniformVector3f("cameraPos");

    public UniformVector2f nearFarPlanes =
            new UniformVector2f("nearFarPlanes");

    protected UniformFloat waveTime =
            new UniformFloat("waveTime");
    protected UniformFloat waveLength =
            new UniformFloat("waveLength");
    protected UniformFloat waveAmplitude =
            new UniformFloat("waveAmplitude");

    protected UniformVector3f lightDirection =
            new UniformVector3f("lightDirection");
    protected UniformVector3f lightColor =
            new UniformVector3f("lightColour");
    protected UniformVector2f lightBias =
            new UniformVector2f("lightBias");

    protected UniformSampler reflectionTexture =
            new UniformSampler("reflectionTexture");
    protected UniformSampler refractionTexture =
            new UniformSampler("refractionTexture");
    protected UniformSampler depthTexture =
            new UniformSampler("depthTexture");

    /**
     * The class' constructor.
     */
    public WaterShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
        super.storeAllUniformLocations(projectionViewMatrix,
                height,
                reflectionTexture, refractionTexture, depthTexture,
                cameraPosition,
                nearFarPlanes,
                waveTime, waveLength, waveAmplitude,
                lightDirection, lightColor, lightBias);

        linkTextureUnits();
    }

    /**
     * Method for texture samplers linking to the fragment shader.
     */
    private void linkTextureUnits() {
        super.start();

        reflectionTexture.loadTexUnit(REFLECTION_TEXTURE_UNIT);
        refractionTexture.loadTexUnit(REFRACTION_TEXTURE_UNIT);
        depthTexture.loadTexUnit(DEPTH_TEXTURE_UNIT);

        super.stop();
    }
}
