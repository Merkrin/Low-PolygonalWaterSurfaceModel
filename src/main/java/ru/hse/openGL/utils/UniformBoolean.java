package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;

/**
 * Class for boolean uniform variable representation.
 */
public class UniformBoolean extends Uniform {
    private boolean value;

    private boolean isUsed = false;

    /**
     * The class' constructor.
     *
     * @param name name of the variable
     */
    public UniformBoolean(String name) {
        super(name);
    }

    /**
     * Method for variable loading.
     *
     * @param value value to load
     */
    public void loadBoolean(boolean value) {
        if (!isUsed || this.value != value) {
            GL20.glUniform1f(super.getLocation(), value ? 1f : 0f);

            this.value = value;

            isUsed = true;
        }
    }
}
