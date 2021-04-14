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
    private static final int PI = 180;

    private static final float PITCH_DELTA = 1f / 60;

    // Pitch is the y-axis rotation.
    private static final float PITCH_SENSITIVITY = 0.3f;
    private static final float MAXIMAL_PITCH = 100;

    // Yaw is the z-axis rotation.
    private static final float YAW_SENSITIVITY = 0.3f;

    private static final float ZOOM_MULTIPLIER = 0.0008f;

    private static final float NEAR_PLANE = 0.4f;
    private static final float FAR_PLANE = 2500;

    // Field of view and other daemon settings.
    private static final float FIELD_OF_VIEW = 70;

    private final SmoothFloat pitch = new SmoothFloat(10, 10);
    private final SmoothFloat angleAroundDaemon = new SmoothFloat(0, 10);
    private final SmoothFloat distanceFromDaemon = new SmoothFloat(10, 5);

    private final Vector3f cameraPosition = new Vector3f(0, 0, 0);

    // Matrices to transform the view.
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f reflectedMatrix = new Matrix4f();

    private final Daemon daemon;

    private float yaw = 0;

    private boolean isReflected = false;

    /**
     * The class' constructor.
     *
     * @param daemon daemon instance to use.
     */
    public Camera(Daemon daemon) {
        this.daemon = daemon;

        projectionMatrix = createProjectionMatrix();
    }

    /**
     * Camera moving method.
     */
    public void move() {
        calculatePitch();
        calculateAngleAroundPlayer();
        calculateZoom();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

        yaw = PI - (daemon.getYRotation() + angleAroundDaemon.getActualValue());

        updateViewMatrices();
    }

    /**
     * Method for view matrix and other matrices update.
     */
    private void updateViewMatrices() {
        Maths.updateViewMatrix(viewMatrix,
                cameraPosition.x, cameraPosition.y, cameraPosition.z,
                pitch.getActualValue(), yaw);

        float yPosition = cameraPosition.y -
                (2 * (cameraPosition.y - Configs.getWaterHeight()));
        float pitchReflection = -pitch.getActualValue();

        Maths.updateViewMatrix(reflectedMatrix,
                cameraPosition.x, yPosition, cameraPosition.z,
                pitchReflection, yaw);
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

    /**
     * Method for camera position calculation.
     *
     * @param horizontalDistance current horizontal distance
     * @param verticalDistance   current vertical distance
     */
    private void calculateCameraPosition(float horizontalDistance,
                                         float verticalDistance) {
        float theta = daemon.getYRotation() + angleAroundDaemon.getActualValue();
        float offsetX = (float) (horizontalDistance *
                Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance *
                Math.cos(Math.toRadians(theta)));

        cameraPosition.x = daemon.getPosition().x - offsetX;
        cameraPosition.z = daemon.getPosition().z - offsetZ;
        cameraPosition.y = daemon.getPosition().y + verticalDistance;
    }

    /**
     * Method for horizontal distance calculation.
     *
     * @return horizontal distance of the camera from daemon
     */
    private float calculateHorizontalDistance() {
        return (float) (distanceFromDaemon.getActualValue() *
                Math.cos(Math.toRadians(pitch.getActualValue())));
    }

    /**
     * Method for vertical distance calculation.
     *
     * @return height of the camera from daemon
     */
    private float calculateVerticalDistance() {
        return (float) (distanceFromDaemon.getActualValue() *
                Math.sin(Math.toRadians(pitch.getActualValue())));
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

        pitch.update(PITCH_DELTA);
    }

    /**
     * Method for zoom calculation.
     */
    private void calculateZoom() {
        float targetZoom = distanceFromDaemon.getTargetValue();
        float zoomLevel = Mouse.getDWheel() * ZOOM_MULTIPLIER * targetZoom;

        targetZoom -= zoomLevel;

        if (targetZoom < 1)
            targetZoom = 1;

        distanceFromDaemon.setTargetValue(targetZoom);
        distanceFromDaemon.update(0.01f);
    }

    /**
     * Calculate the angle of the camera around the player (when looking down at
     * the camera from above). Basically the yaw. Changes the yaw when the user
     * moves the mouse horizontally with the LMB down.
     */
    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * YAW_SENSITIVITY;

            daemon.changeRotation(-angleChange);
        }
    }

    /**
     * Method for checking if the camera's pitch is not too high or too low.
     */
    private void clampPitch() {
        if (pitch.getTargetValue() < 0)
            pitch.setTargetValue(0);
        else if (pitch.getTargetValue() > MAXIMAL_PITCH)
            pitch.setTargetValue(MAXIMAL_PITCH);
    }

    @Override
    public Vector3f getCameraPosition() {
        return cameraPosition;
    }

    @Override
    public void reflect() {
        isReflected = !isReflected;
    }

    @Override
    public Matrix4f getProjectionViewMatrix() {
        if (isReflected)
            return Matrix4f.mul(projectionMatrix, reflectedMatrix, null);

        return Matrix4f.mul(projectionMatrix, viewMatrix, null);
    }

    @Override
    public float getNearPlane() {
        return NEAR_PLANE;
    }

    @Override
    public float getFarPlane() {
        return FAR_PLANE;
    }
}
