package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

public class UniformVector3f extends Uniform {
    private float xValue;
    private float yValue;
    private float zValue;
    private boolean isUsed = false;

    public UniformVector3f(String name) {
        super(name);
    }

    public void loadVector3f(Vector3f vector) {
        loadVector3f(vector.x, vector.y, vector.z);
    }

    public void loadVector3f(float xValue, float yValue, float zValue) {
        if (!isUsed ||
                this.xValue != xValue ||
                this.yValue != yValue ||
                this.zValue != zValue) {
            GL20.glUniform3f(super.getLocation(), xValue, yValue, zValue);

            isUsed = true;
            this.xValue = xValue;
            this.yValue = yValue;
            this.zValue = zValue;
        }
    }
}