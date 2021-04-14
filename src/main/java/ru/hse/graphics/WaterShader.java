package ru.hse.graphics;

import ru.hse.openGL.utils.*;
import ru.hse.utils.FileBuffer;

/**
 * Water shader storage class.
 */
class WaterShader extends ShaderProgram {
    // Indexes of texture units.
    static final int REFLECTION_TEXTURE_UNIT = 0;
    static final int REFRACTION_TEXTURE_UNIT = 1;
    static final int DEPTH_TEXTURE_UNIT = 2;

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

    final UniformFloat waveTime =
            new UniformFloat("waveTime");
    final UniformFloat waveLength =
            new UniformFloat("waveLength");
    final UniformFloat waveAmplitude =
            new UniformFloat("waveAmplitude");

    final UniformVector3f lightDirection =
            new UniformVector3f("lightDirection");
    final UniformVector3f lightColor =
            new UniformVector3f("lightColor");
    final UniformVector2f lightBias =
            new UniformVector2f("lightBias");

    private final UniformSampler reflectionTexture =
            new UniformSampler("reflectionTexture");
    private final UniformSampler refractionTexture =
            new UniformSampler("refractionTexture");
    private final UniformSampler depthTexture =
            new UniformSampler("depthTexture");

    final UniformBoolean applyAnimation = new UniformBoolean("applyAnimation");

    /**
     * The class' constructor.
     */
    WaterShader() {
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
