package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;

/**
 * Class for uniform variable representation.
 */
public class Uniform {
    private static final int NOT_FOUND = -1;

    private final String name;
    private int location;

    /**
     * The class' constructor.
     *
     * @param name name of the uniform
     */
    protected Uniform(String name) {
        this.name = name;
    }

    /**
     * Method for uniform storing.
     *
     * @param shaderId id of the shader to use
     */
    protected void storeUniformLocation(int shaderId) {
        location = GL20.glGetUniformLocation(shaderId, name);

        if (location == NOT_FOUND)
            System.err.println("No uniform variable called \"" + name +
                    "\" found for shader program: " + shaderId);
    }

    /**
     * Method for the uniform location getting.
     *
     * @return location of the uniform
     */
    protected int getLocation() {
        return location;
    }

}
