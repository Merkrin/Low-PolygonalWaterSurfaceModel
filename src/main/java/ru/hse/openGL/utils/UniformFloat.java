package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;

/**
 * Class for float uniform variable representation.
 */
public class UniformFloat extends Uniform {
    private float value;

    private boolean isUsed = false;

    /**
     * The class' constructor.
     *
     * @param name name of the variable
     */
    public UniformFloat(String name) {
        super(name);
    }

    /**
     * Method for variable loading.
     *
     * @param value value to load
     */
    public void loadFloat(float value) {
        if (!isUsed || this.value != value) {
            GL20.glUniform1f(super.getLocation(), value);

            this.value = value;

            isUsed = true;
        }
    }
}
