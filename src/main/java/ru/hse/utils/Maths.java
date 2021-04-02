package ru.hse.utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Utility maths class.
 */
public class Maths {
    /**
     * Method for value clamping.
     *
     * @param value   value to clamp.
     * @param minimum minimum of clamping
     * @param maximum maximum of clamping
     * @return clamped value
     */
    public static float clamp(float value, float minimum, float maximum) {
        return Math.max(Math.min(value, maximum), minimum);
    }

    /**
     * Method for normal of the triangle calculation.
     *
     * @param firstVertex  first vertex of the triangle
     * @param secondVertex second vertex of the triangle
     * @param thirdVertex  third vertex of the triangle
     * @return normal of the triangle plane
     */
    public static Vector3f calculateNormal(Vector3f firstVertex,
                                           Vector3f secondVertex,
                                           Vector3f thirdVertex) {
        Vector3f tangentA = Vector3f.sub(secondVertex, firstVertex, null);
        Vector3f tangentB = Vector3f.sub(thirdVertex, firstVertex, null);

        Vector3f normal = Vector3f.cross(tangentA, tangentB, null);

        normal.normalise();

        return normal;
    }

    /**
     * Method for the view matrix update.
     *
     * @param viewMatrix view matrix to update
     * @param x          x-coordinate of camera position
     * @param y          y-coordinate of camera position
     * @param z          z-coordinate of camera position
     * @param pitch      pitch of the camera
     * @param yaw        yaw of the camera
     */
    public static void updateViewMatrix(Matrix4f viewMatrix,
                                        float x, float y, float z,
                                        float pitch, float yaw) {
        viewMatrix.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(pitch),
                new Vector3f(1, 0, 0),
                viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(yaw),
                new Vector3f(0, 1, 0),
                viewMatrix, viewMatrix);

        Vector3f negativeCameraPos = new Vector3f(-x, -y, -z);

        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
    }
}
