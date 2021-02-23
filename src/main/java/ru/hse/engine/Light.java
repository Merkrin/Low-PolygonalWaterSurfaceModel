package ru.hse.engine;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.utils.Color;

/**
 * Class for light representation.
 */
public class Light {
    // Light direction.
    private Vector3f direction;

    // Light color.
    private Color color;

    // Setting of how much ambient (x-value) and
    // how much diffuse (y-value) light there has to be.
    private Vector2f lightBias;

    /**
     * The class' constructor.
     *
     * @param direction light direction vector
     * @param color     light color vector
     * @param lightBias bias of the light
     */
    public Light(Vector3f direction, Color color, Vector2f lightBias) {
        this.direction = direction;
        this.direction.normalise();

        this.color = color;

        this.lightBias = lightBias;
    }

    /**
     * Getter of light direction.
     *
     * @return light direction
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * Getter of light color.
     *
     * @return color of the light
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter of light bias.
     *
     * @return bias of the light
     */
    public Vector2f getLightBias() {
        return lightBias;
    }
}
