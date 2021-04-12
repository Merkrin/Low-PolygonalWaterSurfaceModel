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

    public final UniformMatrix projectionViewMatrix =
            new UniformMatrix("projectionViewMatrix");

    public final UniformFloat height =
            new UniformFloat("height");

    public final UniformVector3f cameraPosition =
            new UniformVector3f("cameraPosition");

    public final UniformVector2f nearFarPlanes =
            new UniformVector2f("nearFarPlanes");

    protected final UniformFloat waveTime =
            new UniformFloat("waveTime");
    protected final UniformFloat waveLength =
            new UniformFloat("waveLength");
    protected final UniformFloat waveAmplitude =
            new UniformFloat("waveAmplitude");

    protected final UniformVector3f lightDirection =
            new UniformVector3f("lightDirection");
    protected final UniformVector3f lightColor =
            new UniformVector3f("lightColor");
    protected final UniformVector2f lightBias =
            new UniformVector2f("lightBias");

    protected final UniformSampler reflectionTexture =
            new UniformSampler("reflectionTexture");
    protected final UniformSampler refractionTexture =
            new UniformSampler("refractionTexture");
    protected final UniformSampler depthTexture =
            new UniformSampler("depthTexture");

    protected final UniformBoolean applyAnimation = new UniformBoolean("applyAnimation");

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
                lightDirection, lightColor, lightBias, applyAnimation);

        linkTextureUnits();
    }

    /**
     * Method for texture samplers linking to the fragment shader.
     */
    private void linkTextureUnits() {
        super.start();

        reflectionTexture.loadTextureUnit(REFLECTION_TEXTURE_UNIT);
        refractionTexture.loadTextureUnit(REFRACTION_TEXTURE_UNIT);
        depthTexture.loadTextureUnit(DEPTH_TEXTURE_UNIT);

        super.stop();
    }
}
