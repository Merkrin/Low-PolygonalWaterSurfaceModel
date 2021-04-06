package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

/**
 * Class for Vector4f uniform variable representation.
 */
public class UniformVector4f extends Uniform {
    /**
     * The class' constructor.
     *
     * @param name name of the variable
     */
    public UniformVector4f(String name) {
        super(name);
    }

    /**
     * Method for variable loading.
     *
     * @param vector vector to load
     */
    public void loadVector4f(Vector4f vector) {
        loadVector4f(vector.x, vector.y, vector.z, vector.w);
    }

    /**
     * Method for variable loading.
     *
     * @param xValue x-value to load
     * @param yValue y-value to load
     * @param zValue z-value to load
     * @param wValue w-value to load
     */
    public void loadVector4f(float xValue, float yValue,
                             float zValue, float wValue) {
        GL20.glUniform4f(super.getLocation(), xValue, yValue, zValue, wValue);
    }

}