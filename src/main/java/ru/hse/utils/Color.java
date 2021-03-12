package ru.hse.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

public class Color {
    private Vector3f color = new Vector3f();

    private float alpha = 1;

    public Color() {
    }

    public Color(Vector3f color) {
        this.color = color;
    }

    public Color(float red, float green, float blue) {
        color.set(red, green, blue);
    }

    public Color(float red, float green, float blue, float alpha) {
        color.set(red, green, blue);

        this.alpha = alpha;
    }

    // TODO: remove magic number
    public Color(float red, float green, float blue, boolean toBeConverted) {
        if (toBeConverted) {
            color.set(red / 255f, green / 255f, blue / 255f);
        } else {
            color.set(red, green, blue);
        }
    }

    public byte[] getColorAsByteArray() {
        int red = (int) (color.x * 255);
        int green = (int) (color.y * 255);
        int blue = (int) (color.z * 255);
        int alpha = (int) (this.alpha * 255);
        return new byte[]{(byte) red, (byte) green, (byte) blue, (byte) alpha};
    }

    public FloatBuffer getColorAsFloatBuffer() {
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);

        colorBuffer.put(new float[]{color.x, color.y, color.z, alpha});
        colorBuffer.flip();

        return colorBuffer;
    }

    public Color getDuplicate() {
        return new Color(getRedValue(), getGreenValue(), getBlueValue());
    }

    public static Color add(Color firstColor, Color secondColor,
                            Color destination) {
        if (destination == null)
            return new Color(Vector3f.add(firstColor.getColor(),
                    secondColor.getColor(), null));

        Vector3f.add(firstColor.getColor(),
                secondColor.getColor(),
                destination.getColor());

        return destination;
    }

    public void multiply(Color color) {
        this.color.x *= color.getRedValue();
        this.color.y *= color.getGreenValue();
        this.color.z *= color.getBlueValue();
    }

    public static Color subtract(Color firstColor, Color secondColor,
                                 Color destination) {
        if (destination == null)
            return new Color(Vector3f.sub(firstColor.getColor(),
                    secondColor.getColor(),
                    null));

        Vector3f.sub(firstColor.getColor(),
                secondColor.getColor(),
                destination.getColor());

        return destination;
    }

    public Color scale(float value) {
        color.scale(value);

        return this;
    }

    public static Color interpolateColours(Color firstColor, Color secondColor,
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

    public Color getUnit() {
        Color colour = new Color();

        if (getRedValue() == 0 &&
                getGreenValue() == 0 &&
                getBlueValue() == 0)
            return colour;

        colour.setColor(this);
        colour.scale(1f / getLength());

        return colour;
    }

    public boolean isEqualTo(Color color) {
        return getRedValue() == color.getRedValue() &&
                getGreenValue() == color.getGreenValue() &&
                getBlueValue() == color.getBlueValue();
    }

    @Override
    public String toString() {
        return "[" + color.x + ";" + color.y + ";" + color.z + "]";
    }

    public void setColor(float red, float green, float blue) {
        color.set(red, green, blue);
    }

    public void setColor(Vector3f color) {
        color.set(color);
    }

    public void setColor(Color color) {
        this.color.set(color.getColor());
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColorFromHsv(float hue, float saturation, float value) {
        setColor(hsvToRgb(hue, saturation, value));
    }

    public void setRedValue(float redValue) {
        color.x = redValue;
    }

    public float getRedValue() {
        return color.x;
    }

    public void setGreenValue(float greenValue) {
        color.y = greenValue;
    }

    public float getGreenValue() {
        return color.y;
    }

    public void setBlueValue(float blueValue) {
        color.z = blueValue;
    }

    public float getBlueValue() {
        return color.z;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getLength() {
        return (float) Math.sqrt(getLengthSquared());
    }

    public float getLengthSquared() {
        return color.lengthSquared();
    }

    public static Color hsvToRgb(float hue, float saturation, float value) {
        int h = (int) (hue * 6);

        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0:
                return new Color(value, t, p);
            case 1:
                return new Color(q, value, p);
            case 2:
                return new Color(p, value, t);
            case 3:
                return new Color(p, q, value);
            case 4:
                return new Color(t, p, value);
            case 5:
                return new Color(value, p, q);
            default:
                return new Color();
        }
    }
}
