package ru.hse.utils;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;

/**
 * Utility class for data storing.
 */
public class DataStorage {
    /**
     * Method for vertex data storing.
     *
     * @param position vertex position
     * @param normal   vertex normal
     * @param color    vertex color
     * @param buffer   buffer to use
     */
    public static void packVertexData(Vector3f position, Vector3f normal,
                                      Color color,
                                      ByteBuffer buffer) {
        packVertexData(position.x, position.y, position.z, normal,
                color,
                buffer);
    }

    /**
     * Method for vertex data storing.
     *
     * @param position   vertex position
     * @param indicators vertex indicators
     * @param buffer     buffer to use
     */
    public static void packVertexData(Vector2f position,
                                      byte[] indicators,
                                      ByteBuffer buffer) {
        buffer.putFloat(position.x);
        buffer.putFloat(position.y);
        buffer.put(indicators);
    }

    /**
     * Method for vertex data storing.
     *
     * @param x      vertex x-coordinate
     * @param y      vertex y-coordinate
     * @param z      vertex z-coordinate
     * @param normal vertex normal
     * @param color  vertex color
     * @param buffer buffer to use
     */
    public static void packVertexData(float x, float y, float z,
                                      Vector3f normal,
                                      Color color,
                                      ByteBuffer buffer) {
        storeFloats(buffer, x, y, z);
        storeNormal(buffer, normal);
        storeColor(buffer, color);
    }

    /**
     * Method for floats storing
     *
     * @param buffer buffer to use
     * @param a      float to store
     * @param b      float to store
     * @param c      float to store
     */
    private static void storeFloats(ByteBuffer buffer,
                                    float a, float b, float c) {
        buffer.putFloat(a);
        buffer.putFloat(b);
        buffer.putFloat(c);
    }

    /**
     * Method for normal storing.
     *
     * @param buffer buffer to use
     * @param normal normal to store
     */
    private static void storeNormal(ByteBuffer buffer, Vector3f normal) {
        int packedInt = DataUtils
                .packInt(normal.x, normal.y, normal.z, 0);

        buffer.putInt(packedInt);
    }

    /**
     * Method for color storing.
     *
     * @param buffer buffer to use
     * @param color  color to store
     */
    private static void storeColor(ByteBuffer buffer, Color color) {
        byte[] colourBytes = color.getColorAsByteArray();

        buffer.put(colourBytes);
    }
}
