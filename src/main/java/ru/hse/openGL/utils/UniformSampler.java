package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;

/**
 * Class for sampler uniform variable representation.
 */
public class UniformSampler extends Uniform {
    private int value;

    private boolean isUsed = false;

    /**
     * The class' constructor.
     *
     * @param name name of the variable
     */
    public UniformSampler(String name) {
        super(name);
    }

    /**
     * Method for variable loading.
     *
     * @param value value to load
     */
    public void loadTextureUnit(int value) {
        if (!isUsed || this.value != value) {
            GL20.glUniform1i(super.getLocation(), value);

            this.value = value;

            isUsed = true;
        }
    }
}