package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;

public class UniformFloat extends Uniform {
    private float value;
    private boolean isUsed = false;

    protected UniformFloat(String name) {
        super(name);
    }

    public void loadFloat(float value) {
        if (!isUsed || this.value != value) {
            GL20.glUniform1f(super.getLocation(), value);

            isUsed = true;
            this.value = value;
        }
    }
}
