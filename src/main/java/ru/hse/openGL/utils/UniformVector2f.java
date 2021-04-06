package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

/**
 * Class for Vector2f uniform variable representation.
 */
public class UniformVector2f extends Uniform {
    private float xValue;
    private float yValue;

    private boolean isUsed = false;

    /**
     * The class' constructor.
     *
     * @param name name of the variable
     */
    public UniformVector2f(String name) {
        super(name);
    }

    /**
     * Method for variable loading.
     *
     * @param vector vector to load
     */
    public void loadVector2f(Vector2f vector) {
        loadVector2f(vector.x, vector.y);
    }

    /**
     * Method for variable loading.
     *
     * @param xValue x-value to load
     * @param yValue y-value to load
     */
    public void loadVector2f(float xValue, float yValue) {
        if (!isUsed || this.xValue != xValue || this.yValue != yValue) {
            GL20.glUniform2f(super.getLocation(), xValue, yValue);

            this.xValue = xValue;
            this.yValue = yValue;

            isUsed = true;
        }
    }

}