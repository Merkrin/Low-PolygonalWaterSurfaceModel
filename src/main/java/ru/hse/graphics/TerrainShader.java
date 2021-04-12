package ru.hse.graphics;

import ru.hse.openGL.utils.*;
import ru.hse.utils.FileBuffer;

/**
 * Terrain shader storing class.
 */
public class TerrainShader extends ShaderProgram {
    protected final UniformMatrix projectionViewMatrix
            = new UniformMatrix("projectionViewMatrix");
    protected final UniformVector3f lightDirection
            = new UniformVector3f("lightDirection");
    protected final UniformVector3f lightColor
            = new UniformVector3f("lightColor");
    protected final UniformVector2f lightBias
            = new UniformVector2f("lightBias");
    protected final UniformVector4f plane
            = new UniformVector4f("plane");

    /**
     * The class' constructor.
     *
     * @param vertexShaderFile   file with vertex shader
     * @param fragmentShaderFile file with fragment shader
     */
    public TerrainShader(FileBuffer vertexShaderFile,
                         FileBuffer fragmentShaderFile) {
        super(vertexShaderFile, fragmentShaderFile);
        super.storeAllUniformLocations(projectionViewMatrix,
                lightDirection,
                lightColor,
                lightBias,
                plane);
    }
}
