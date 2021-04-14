package ru.hse.utils;

import org.lwjgl.util.vector.Vector3f;

/**
 * Class for color representation.
 */
public class Color {
    private final static float COLOR_FRACTION = 255f;

    private Vector3f color = new Vector3f();

    /**
     * The class' constructor
     *
     * @param color vector with color RGB values
     */
    public Color(Vector3f color) {
        color.x = color.x / COLOR_FRACTION;
        color.y = color.y / COLOR_FRACTION;
        color.z = color.z / COLOR_FRACTION;

        this.color = color;
    }

    /**
     * The class' constructor.
     *
     * @param red   red color value
     * @param green green color value
     * @param blue  blue color value
     */
    public Color(float red, float green, float blue) {
        color.set(red, green, blue);
    }

    /**
     * The class' constructor.
     *
     * @param red           red color value
     * @param green         green color value
     * @param blue          blue color value
     * @param toBeConverted flag of color converting
     */
    public Color(float red, float green, float blue,
                 boolean toBeConverted) {
        if (toBeConverted)
            color.set(red / COLOR_FRACTION,
                    green / COLOR_FRACTION,
                    blue / COLOR_FRACTION);
        else
            color.set(red, green, blue);
    }

    /**
     * Method for color getting in byte-array representation.
     *
     * @return byte array with color
     */
    public byte[] getColorAsByteArray() {
        byte red = (byte) (color.x * COLOR_FRACTION);
        byte green = (byte) (color.y * COLOR_FRACTION);
        byte blue = (byte) (color.z * COLOR_FRACTION);
        byte alpha = (byte) (COLOR_FRACTION);

        return new byte[]{red, green, blue, alpha};
    }

    /**
     * Method for colors interpolation.
     *
     * @param firstColor  first color
     * @param secondColor second color
     * @param blend       blending rate
     * @param destination destination color
     * @return interpolated color
     */
    public static Color interpolateColors(Color firstColor, Color secondColor,
                                          float blend,
                                          Color destination) {
        float firstColorWeight = 1 - blend;

        float red = (firstColorWeight * firstColor.getRedValue()) +
                (blend * secondColor.getRedValue());
        float green = (firstColorWeight * firstColor.getGreenValue()) +
                (blend * secondColor.getGreenValue());
        float blue = (firstColorWeight * firstColor.getBlueValue()) +
                (blend * secondColor.getBlueValue());

        if (destination == null)
            return new Color(red, green, blue);

        destination.setColor(red, green, blue);
        return destination;
    }

    /**
     * Setter of the color value.
     *
     * @param red   red value
     * @param green green value
     * @param blue  blue value
     */
    private void setColor(float red, float green, float blue) {
        color.set(red, green, blue);
    }

    /**
     * Getter of the color value.
     *
     * @return color vector
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * Getter of the red value.
     *
     * @return red value of the color
     */
    private float getRedValue() {
        return color.x;
    }

    /**
     * Getter of the green value.
     *
     * @return green value of the color
     */
    private float getGreenValue() {
        return color.y;
    }

    /**
     * Getter of the blue value.
     *
     * @return blue value of the color
     */
    private float getBlueValue() {
        return color.z;
    }

    /**
     * Overridden toString method.
     *
     * @return color value as a string
     */
    @Override
    public String toString() {
        return (int) (color.x * 255) + ";" + (int) (color.y * 255) + ";" + (int) (color.z * 255);
    }
}
