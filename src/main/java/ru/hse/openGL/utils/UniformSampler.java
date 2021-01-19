package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;

public class UniformSampler extends Uniform {
    private int value;
    private boolean isUsed = false;

    public UniformSampler(String name) {
        super(name);
    }

    public void loadTexUnit(int value) {
        if (!isUsed || this.value != value) {
            GL20.glUniform1i(super.getLocation(), value);

            isUsed = true;
            this.value = value;
        }
    }
}