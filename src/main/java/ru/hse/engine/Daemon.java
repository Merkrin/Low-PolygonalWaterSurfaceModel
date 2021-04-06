package ru.hse.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.utils.Window;

/**
 * Daemon-"player" class. The idea is to implement a player figure,
 * that does not really exist, so it can be moved and the camera
 * works as the third view camera.
 */
public class Daemon {
    private final static float RUN_SPEED = 20;
    private final static float TURN_SPEED = 160;
    private final static float FLIGHT_SPEED = 0.6f;

    private final Window window;

    private final Vector3f position;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private float yRotation;

    /**
     * The class' constructor.
     *
     * @param window    used window instance
     * @param position  starting position
     * @param yRotation y-axis starting rotation
     */
    public Daemon(Window window, Vector3f position,
                  float yRotation) {
        this.window = window;

        this.position = position;

        this.yRotation = yRotation;
    }

    /**
     * Daemon's moving method.
     */
    public void move() {
        getInput();

        changeRotation(currentTurnSpeed * window.getFrameTimeSeconds());

        float distance = currentSpeed * window.getFrameTimeSeconds();

        float dx = (float) (distance * Math.sin(Math.toRadians(getYRotation())));
        float dz = (float) (distance * Math.cos(Math.toRadians(getYRotation())));

        changePosition(dx, upwardsSpeed, dz);
    }

    /**
     * Method for user input processing.
     */
    private void getInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            currentSpeed = RUN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
            currentSpeed = -RUN_SPEED;
        else
            currentSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            currentTurnSpeed = -TURN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_A))
            currentTurnSpeed = TURN_SPEED;
        else
            currentTurnSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            upwardsSpeed = FLIGHT_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_X))
            upwardsSpeed = -FLIGHT_SPEED;
        else
            upwardsSpeed = 0;
    }

    /**
     * Method for daemon's position change.
     *
     * @param xDelta x-coordinate delta
     * @param yDelta y-coordinate delta
     * @param zDelta z-coordinate delta
     */
    public void changePosition(float xDelta, float yDelta, float zDelta) {
        position.x += xDelta;
        position.y += yDelta;
        position.z += zDelta;
    }

    /**
     * Method for daemon's rotation change.
     *
     * @param yDelta y-coordinate delta
     */
    public void changeRotation(float yDelta) {
        yRotation += yDelta;
    }

    /**
     * Daemon's position getter.
     *
     * @return position of the daemon
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Daemon's y-axis rotation getter.
     *
     * @return y-axis rotation of the daemon
     */
    public float getYRotation() {
        return yRotation;
    }
}
