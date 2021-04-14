package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

/**
 * Class for Vector3f uniform variable representation.
 */
public class UniformVector3f extends Uniform {
    private float xValue;
    private float yValue;
    private float zValue;

    private boolean isUsed = false;

    /**
     * The class' constructor.
     *
     * @param name name of the variable
     */
    public UniformVector3f(String name) {
        super(name);
    }

    /**
     * Method for variable loading.
     *
     * @param vector vector to load
     */
    public void loadVector3f(Vector3f vector) {
        loadVector3f(vector.x, vector.y, vector.z);
    }

    /**
     * Method for variable loading.
     *
     * @param xValue x-value to load
     * @param yValue y-value to load
     * @param zValue z-value to load
     */
    private void loadVector3f(float xValue, float yValue, float zValue) {
        if (!isUsed ||
                this.xValue != xValue ||
                this.yValue != yValue ||
                this.zValue != zValue) {
            GL20.glUniform3f(super.getLocation(), xValue, yValue, zValue);

            this.xValue = xValue;
            this.yValue = yValue;
            this.zValue = zValue;

            isUsed = true;
        }
    }
}