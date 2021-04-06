package ru.hse.openGL.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

/**
 * Class for matrix uniform variable representation.
 */
public class UniformMatrix extends Uniform {
    private static final FloatBuffer matrixBuffer =
            BufferUtils.createFloatBuffer(16);

    /**
     * The class' constructor.
     *
     * @param name name of the variable
     */
    public UniformMatrix(String name) {
        super(name);
    }

    /**
     * Method for variable loading.
     *
     * @param matrix matrix to load
     */
    public void loadMatrix(Matrix4f matrix) {
        matrix.store(matrixBuffer);

        matrixBuffer.flip();

        GL20.glUniformMatrix4(super.getLocation(), false, matrixBuffer);
    }
}
