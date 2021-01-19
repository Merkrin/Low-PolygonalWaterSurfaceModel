package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformVector2f extends Uniform {
    private float xValue;
    private float yValue;
    private boolean isUsed = false;

    public UniformVector2f(String name) {
        super(name);
    }

    public void loadVector2f(Vector2f vector) {
        loadVector2f(vector.x, vector.y);
    }

    public void loadVector2f(float xValue, float yValue) {
        if (!isUsed || this.xValue != xValue || this.yValue != yValue) {
            GL20.glUniform2f(super.getLocation(), xValue, yValue);

            isUsed = true;
            this.xValue = xValue;
            this.yValue = yValue;
        }
    }

}