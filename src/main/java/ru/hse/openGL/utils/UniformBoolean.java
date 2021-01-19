package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;

public class UniformBoolean extends Uniform {
    private boolean value;
    private boolean isUsed = false;

    protected UniformBoolean(String name) {
        super(name);
    }

    public void loadBoolean(boolean value) {
        if (!isUsed || this.value != value) {
            GL20.glUniform1f(super.getLocation(), value ? 1f : 0f);

            isUsed = true;
            this.value = value;
        }
    }
}
