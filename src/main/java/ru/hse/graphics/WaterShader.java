package ru.hse.graphics;

import ru.hse.openGL.utils.ShaderProgram;
import ru.hse.openGL.utils.UniformFloat;
import ru.hse.openGL.utils.UniformMatrix;
import ru.hse.utils.FileBuffer;

public class WaterShader extends ShaderProgram {
    private static final FileBuffer VERTEX_SHADER = new FileBuffer("ru/hse/openGL/shaders", "waterVertex.glsl");
    private static final FileBuffer FRAGMENT_SHADER = new FileBuffer("ru/hse/openGL/shaders", "waterFragment.glsl");

    public UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
    public UniformFloat height = new UniformFloat("height");

    public WaterShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
        super.storeAllUniformLocations(projectionViewMatrix, height);
    }
}
