package ru.hse.engine;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.utils.Color;

public class Light {
    private Vector3f direction;

    private Color colour;

    private Vector2f lightBias;// how much ambient light and how much diffuse light

    public Light(Vector3f direction, Color colour, Vector2f lightBias) {
        this.direction = direction;
        this.direction.normalise();

        this.colour = colour;

        this.lightBias = lightBias;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public Color getColor() {
        return colour;
    }

    /**
     * @return A vector with 2 float values. The x value is how much ambient
     *         lighting should be used, and the y value is how much diffuse
     *         lighting should be used.
     */
    public Vector2f getLightBias() {
        return lightBias;
    }
}
