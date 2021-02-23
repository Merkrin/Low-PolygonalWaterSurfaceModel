package ru.hse.engine;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.utils.Configs;
import ru.hse.utils.Maths;
import ru.hse.utils.SmoothFloat;

/**
 * Class of camera logic implementation.
 */
public class Camera implements ICamera {
    // Pitch is the y-axis rotation.
    private static final float PITCH_SENSITIVITY = 0.3f;
    private static final float MAXIMAL_PITCH = 90;

    // Yaw is the z-axis rotation.
    private static final float YAW_SENSITIVITY = 0.3f;

    private static final float FIELD_OF_VIEW = 70;

    private static final float NEAR_PLANE = 0.4f;
    private static final float FAR_PLANE = 2500;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f reflectedMatrix = new Matrix4f();

    private boolean isReflected = false;

    private Vector3f cameraPosition = new Vector3f(0, 0, 0);

    private float yaw = 0;

    private SmoothFloat pitch = new SmoothFloat(10, 10);

    private SmoothFloat angleAroundPlayer = new SmoothFloat(0, 10);
    private SmoothFloat distanceFromPlayer = new SmoothFloat(10, 5);

    private Daemon daemon;

    public Camera(Daemon daemon) {
        this.daemon = daemon;
        this.projectionMatrix = createProjectionMatrix();
    }

    public void move() {
        calculatePitch();
        //calculateAngleAroundPlayer();
        calculateZoom();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

        yaw = 180 - (daemon.getRotY() + angleAroundPlayer.getActualValue());

        updateViewMatrices();
    }


    @Override
    public Vector3f getCameraPosition() {
        return cameraPosition;
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public void reflect() {
        this.isReflected = !isReflected;
    }

    @Override
    public Matrix4f getProjectionViewMatrix() {
        if (isReflected) {
            return Matrix4f.mul(projectionMatrix, reflectedMatrix, null);
        } else {
            return Matrix4f.mul(projectionMatrix, viewMatrix, null);
        }
    }

    @Override
    public float getNearPlane() {
        return NEAR_PLANE;
    }

    @Override
    public float getFarPlane() {
        return FAR_PLANE;
    }

    private void updateViewMatrices() {
        Maths.updateViewMatrix(viewMatrix, cameraPosition.x, cameraPosition.y, cameraPosition.z, pitch.getActualValue(), yaw);
        float posY = cameraPosition.y - (2 * (cameraPosition.y - Configs.WATER_HEIGHT));
        float pitchReflect = -pitch.getActualValue();
        Maths.updateViewMatrix(reflectedMatrix, cameraPosition.x, posY, cameraPosition.z, pitchReflect, yaw);
    }

    /**
     * Method for projection matrix creation.
     *
     * @return projection matrix
     */
    private static Matrix4f createProjectionMatrix() {
        Matrix4f projectionMatrix = new Matrix4f();

        // Ratio of window's width to its height.
        float aspectRatio = (float) Display.getWidth() /
                (float) Display.getHeight();

        float yScale = (float) ((1f /
                Math.tan(Math.toRadians(FIELD_OF_VIEW / 2f))));
        float xScale = yScale / aspectRatio;

        // Length of "field of view" viewing region.
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;

        return projectionMatrix;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = daemon.getRotY() + angleAroundPlayer.getActualValue();
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));

        cameraPosition.x = daemon.getPosition().x - offsetX;
        cameraPosition.z = daemon.getPosition().z - offsetZ;
        cameraPosition.y = daemon.getPosition().y + verticDistance;
    }

    /**
     * @return The horizontal distance of the camera from the origin.
     */
    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer.getActualValue() * Math.cos(Math.toRadians(pitch.getActualValue())));
    }

    /**
     * @return The height of the camera from the aim point.
     */
    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer.getActualValue() * Math.sin(Math.toRadians(pitch.getActualValue())));
    }

    /**
     * Calculate and change the pitch if the user is moving the mouse
     * up or down with the left mouse button pressed.
     */
    private void calculatePitch() {
        if (Mouse.isButtonDown(0)) {
            float pitchChange = Mouse.getDY() * PITCH_SENSITIVITY;

            pitch.increaseTargetValue(-pitchChange);

            clampPitch();
        }

        pitch.update(1f / 60);
    }

    private void calculateZoom() {
        float targetZoom = distanceFromPlayer.getTargetValue();
        float zoomLevel = Mouse.getDWheel() * 0.0008f * targetZoom;
        targetZoom -= zoomLevel;
        if (targetZoom < 1) {
            targetZoom = 1;
        }
        distanceFromPlayer.setTargetValue(targetZoom);
        distanceFromPlayer.update(0.01f);
    }

    /**
     * Calculate the angle of the camera around the player (when looking down at
     * the camera from above). Basically the yaw. Changes the yaw when the user
     * moves the mouse horizontally with the LMB down.
     * TODO: make it also change the demon's rotation
     */
    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * YAW_SENSITIVITY;
            angleAroundPlayer.increaseTargetValue(-angleChange);
        }
        angleAroundPlayer.update(1f / 60);
    }

    /**
     * Ensures the camera's pitch isn't too high or too low.
     */
    private void clampPitch() {
        if (pitch.getTargetValue() < 0) {
            pitch.setTargetValue(0);
        } else if (pitch.getTargetValue() > MAXIMAL_PITCH) {
            pitch.setTargetValue(MAXIMAL_PITCH);
        }
    }
}
