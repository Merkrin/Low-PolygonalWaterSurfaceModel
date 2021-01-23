package ru.hse.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.utils.Configs;
import ru.hse.utils.Maths;
import ru.hse.utils.SmoothFloat;

// TODO: try to set positioning in view matrix getter like here https://github.com/lwjglgamedev/lwjglbook/blob/master/chapter08/src/main/java/org/lwjglb/engine/graph/Transformation.java
// TODO: maybe only have to add changer to coordinates (YES!!!!!)??? BEAUTIFY!!!!!!!
// TODO: watch the videos about the third view camera - you can implement it here
public class Camera implements ICamera {
    private static final float PITCH_SENSITIVITY = 0.3f;
    private static final float YAW_SENSITIVITY = 0.3f;
    private static final float MAX_PITCH = 90;

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.4f;
    private static final float FAR_PLANE = 2500;

    private static final float Y_OFFSET = 5;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f reflectedMatrix = new Matrix4f();

    private boolean reflected = false;

    private Vector3f position = new Vector3f(0, 0, 0);

    private float yaw = 0;
    private SmoothFloat pitch = new SmoothFloat(10, 10);
    private SmoothFloat angleAroundPlayer = new SmoothFloat(0, 10);
    private SmoothFloat distanceFromPlayer = new SmoothFloat(10, 5);

    public Camera() {
        this.projectionMatrix = createProjectionMatrix();
    }

    public void move() {
        calculatePitch();
        calculateAngleAroundPlayer();
        calculateZoom();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 360 - angleAroundPlayer.get();
        yaw %= 360;
        updateViewMatrices();
    }


    @Override
    public Vector3f getPosition() {
        return position;
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
    public void reflect(){
        this.reflected = !reflected;
    }

    @Override
    public Matrix4f getProjectionViewMatrix() {
        if(reflected){
            return Matrix4f.mul(projectionMatrix, reflectedMatrix, null);
        }else{
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
        Maths.updateViewMatrix(viewMatrix, position.x, position.y, position.z, pitch.get(), yaw);
        float posY = position.y - (2 * (position.y - Configs.WATER_HEIGHT));
        float pitchReflect = -pitch.get();
        Maths.updateViewMatrix(reflectedMatrix, position.x, posY, position.z, pitchReflect, yaw);
    }

    private static Matrix4f createProjectionMatrix() {
        Matrix4f projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
        return projectionMatrix;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = angleAroundPlayer.get();
        position.x = Configs.WORLD_SIZE / 2f + (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        position.y = verticDistance + Y_OFFSET;
        position.z = Configs.WORLD_SIZE / 2f + (float) (horizDistance * Math.cos(Math.toRadians(theta)));
    }

    /**
     * @return The horizontal distance of the camera from the origin.
     */
    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
    }

    /**
     * @return The height of the camera from the aim point.
     */
    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get())));
    }

    /**
     * Calculate the pitch and change the pitch if the user is moving the mouse
     * up or down with the LMB pressed.
     */
    private void calculatePitch() {
        if (Mouse.isButtonDown(0)) {
            float pitchChange = Mouse.getDY() * PITCH_SENSITIVITY;
            pitch.increaseTarget(-pitchChange);
            clampPitch();
        }
        pitch.update(1f / 60);
    }

    private void calculateZoom() {
        float targetZoom = distanceFromPlayer.getTarget();
        float zoomLevel = Mouse.getDWheel() * 0.0008f * targetZoom;
        targetZoom -= zoomLevel;
        if (targetZoom < 1) {
            targetZoom = 1;
        }
        distanceFromPlayer.setTarget(targetZoom);
        distanceFromPlayer.update(0.01f);
    }

    /**
     * Calculate the angle of the camera around the player (when looking down at
     * the camera from above). Basically the yaw. Changes the yaw when the user
     * moves the mouse horizontally with the LMB down.
     */
    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * YAW_SENSITIVITY;
            angleAroundPlayer.increaseTarget(-angleChange);
        }
        angleAroundPlayer.update(1f / 60);
    }

    /**
     * Ensures the camera's pitch isn't too high or too low.
     */
    private void clampPitch() {
        if (pitch.getTarget() < 0) {
            pitch.setTarget(0);
        } else if (pitch.getTarget() > MAX_PITCH) {
            pitch.setTarget(MAX_PITCH);
        }
    }

//    private static final float CAMERA_POS_STEP = 0.05f;
//
//    private static final float PITCH_SENSITIVITY = 0.3f;
//    private static final float YAW_SENSITIVITY = 0.3f;
//    private static final float MAX_PITCH = 90;
//
//    private static final float FOV = 70;
//    private static final float NEAR_PLANE = 0.4f;
//    private static final float FAR_PLANE = 2500;
//
//    private static final float Y_OFFSET = 5;
//
//    private Matrix4f projectionMatrix;
//    private Matrix4f viewMatrix = new Matrix4f();
//    private Matrix4f reflectedMatrix = new Matrix4f();
//
//    private boolean isReflected = false;
//
//    private Vector3f position = new Vector3f(0, 0, 0);
//    private Vector3f playerPosition = new Vector3f(0, 0, 0);
//
//    private float yaw = 0;
//
//    private SmoothFloat pitch = new SmoothFloat(10, 10);
//    private SmoothFloat angleAroundPlayer = new SmoothFloat(0, 10);
//    private SmoothFloat distanceFromPlayer = new SmoothFloat(10, 5);
//
//    public Camera() {
//        this.projectionMatrix = createProjectionMatrix();
//    }
//
//    public void move() {
//        calculatePitch();
//        calculateAngleAroundPlayer();
//        calculateZoom();
//        float horizontalDistance = calculateHorizontalDistance();
//        float verticalDistance = calculateVerticalDistance();
//        calculateCameraPosition(horizontalDistance, verticalDistance);
//        //input();
//        this.yaw = 360 - angleAroundPlayer.get();
//        yaw %= 360;
//        updateViewMatrices();
//    }
//
//
//    @Override
//    public Vector3f getPosition() {
//        return position;
//    }
//
//    @Override
//    public Matrix4f getViewMatrix() {
//        return viewMatrix;
//    }
//
//    @Override
//    public Matrix4f getProjectionMatrix() {
//        return projectionMatrix;
//    }
//
//    @Override
//    public void reflect(){
//        this.isReflected = !isReflected;
//    }
//
//    @Override
//    public Matrix4f getProjectionViewMatrix() {
//        if(isReflected){
//            return Matrix4f.mul(projectionMatrix, reflectedMatrix, null);
//        }else{
//            return Matrix4f.mul(projectionMatrix, viewMatrix, null);
//        }
//    }
//
//    @Override
//    public float getNearPlane() {
//        return NEAR_PLANE;
//    }
//
//    @Override
//    public float getFarPlane() {
//        return FAR_PLANE;
//    }
//
//    private void updateViewMatrices() {
//        Maths.updateViewMatrix(viewMatrix, position.x, position.y, position.z, pitch.get(), yaw);
//        float posY = position.y - (2 * (position.y - Configs.WATER_HEIGHT));
//        float pitchReflect = -pitch.get();
//        Maths.updateViewMatrix(reflectedMatrix, position.x, posY, position.z, pitchReflect, yaw);
//    }
//
//    private static Matrix4f createProjectionMatrix() {
//        Matrix4f projectionMatrix = new Matrix4f();
//        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
//        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
//        float x_scale = y_scale / aspectRatio;
//        float frustum_length = FAR_PLANE - NEAR_PLANE;
//
//        projectionMatrix.m00 = x_scale;
//        projectionMatrix.m11 = y_scale;
//        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
//        projectionMatrix.m23 = -1;
//        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
//        projectionMatrix.m33 = 0;
//        return projectionMatrix;
//    }
//
//    private void calculateCameraPosition(float horizDistance, float verticDistance) {
//        float theta = angleAroundPlayer.get();
//        position.x = Configs.WORLD_SIZE / 2f + (float) (horizDistance * Math.sin(Math.toRadians(theta)));
//        position.y = verticDistance + Y_OFFSET;
//        position.z = Configs.WORLD_SIZE / 2f + (float) (horizDistance * Math.cos(Math.toRadians(theta)));
//
////        float theta = 2*angleAroundPlayer.get();
////        float offsetX = (float)(horizDistance * Math.sin(Math.toRadians(theta)));
////        float offsetZ = (float)(horizDistance * Math.cos(Math.toRadians(theta)));
////
////        position.x = playerPosition.x - offsetX;
////        position.z = playerPosition.z - offsetZ;
////        position.y = playerPosition.y - verticDistance;
//    }
//
//    /**
//     * @return The horizontal distance of the camera from the origin.
//     */
//    private float calculateHorizontalDistance() {
//        return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
//    }
//
//    /**
//     * @return The height of the camera from the aim point.
//     */
//    private float calculateVerticalDistance() {
//        return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get())));
//    }
//
//    /**
//     * Calculate the pitch and change the pitch if the user is moving the mouse
//     * up or down with the LMB pressed.
//     */
//    private void calculatePitch() {
//        if (Mouse.isButtonDown(0)) {
//            float pitchChange = Mouse.getDY() * PITCH_SENSITIVITY;
//            pitch.increaseTarget(-pitchChange);
//            clampPitch();
//        }
//        pitch.update(1f / 60);
//    }
//
//    private void calculateZoom() {
//        float targetZoom = distanceFromPlayer.getTarget();
//        float zoomLevel = Mouse.getDWheel() * 0.0008f * targetZoom;
//        targetZoom -= zoomLevel;
//        if (targetZoom < 1) {
//            targetZoom = 1;
//        }
//        distanceFromPlayer.setTarget(targetZoom);
//        distanceFromPlayer.update(0.01f);
//    }
//
//    /**
//     * Calculate the angle of the camera around the player (when looking down at
//     * the camera from above). Basically the yaw. Changes the yaw when the user
//     * moves the mouse horizontally with the LMB down.
//     */
//    private void calculateAngleAroundPlayer() {
//        if (Mouse.isButtonDown(0)) {
//            float angleChange = Mouse.getDX() * YAW_SENSITIVITY;
//            angleAroundPlayer.increaseTarget(-angleChange);
//        }
//        angleAroundPlayer.update(1f / 60);
//    }
//
//    /**
//     * Ensures the camera's pitch isn't too high or too low.
//     */
//    private void clampPitch() {
//        if (pitch.getTarget() < 0) {
//            pitch.setTarget(0);
//        } else if (pitch.getTarget() > MAX_PITCH) {
//            pitch.setTarget(MAX_PITCH);
//        }
//    }
//
//    private void input(){
//        Vector3f positionInc = new Vector3f(0, 0, 0);
//
//        if(Keyboard.isKeyDown(Keyboard.KEY_W))
//            positionInc.z -= 1;
//        else if(Keyboard.isKeyDown(Keyboard.KEY_S))
//            positionInc.z += 1;
//        else if(Keyboard.isKeyDown(Keyboard.KEY_D))
//            positionInc.x += 1;
//        else if(Keyboard.isKeyDown(Keyboard.KEY_A))
//            positionInc.x -= 1;
//        else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
//            positionInc.y += 1;
//        else if(Keyboard.isKeyDown(Keyboard.KEY_X))
//            positionInc.y -= 1;
//
//        positionInc.x *= CAMERA_POS_STEP;
//        positionInc.y *= CAMERA_POS_STEP;
//        positionInc.z *= CAMERA_POS_STEP;
//
//        playerPosition.x += positionInc.x;
//        playerPosition.y += positionInc.y;
//        playerPosition.z += positionInc.z;
//    }
}
