package ru.hse.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Camera interface for program scaling, if it is needed.
 */
public interface ICamera {
    /**
     * Getter of camera position.
     *
     * @return position of camera
     */
    Vector3f getCameraPosition();

    /**
     * Getter of projection view matrix.
     *
     * @return projection view matrix
     */
    Matrix4f getProjectionViewMatrix();

    /**
     * Getter of the near plane.
     *
     * @return near plane
     */
    float getNearPlane();

    /**
     * Getter of the far plane.
     *
     * @return far plane
     */
    float getFarPlane();

    /**
     * Method for matrix reflection.
     */
    void reflect();
}
