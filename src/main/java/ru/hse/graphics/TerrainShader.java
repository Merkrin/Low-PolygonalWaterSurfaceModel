package ru.hse.graphics;

import ru.hse.openGL.utils.*;
import ru.hse.utils.FileBuffer;

public class TerrainShader extends ShaderProgram {
    protected UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
    protected UniformVector3f lightDirection = new UniformVector3f("lightDirection");
    protected UniformVector3f lightColour = new UniformVector3f("lightColour");
    protected UniformVector2f lightBias = new UniformVector2f("lightBias");
    protected UniformVector4f plane = new UniformVector4f("plane");

    public TerrainShader(FileBuffer vertexFile, FileBuffer fragmentFile) {
        super(vertexFile, fragmentFile);
        super.storeAllUniformLocations(projectionViewMatrix, lightDirection, lightColour, lightBias, plane);
    }

    public TerrainShader(FileBuffer vertexFile, FileBuffer geometryFile, FileBuffer fragmentFile) {
        super(vertexFile, geometryFile, fragmentFile);
        super.storeAllUniformLocations(projectionViewMatrix, lightDirection, lightColour, lightBias, plane);
    }
}
