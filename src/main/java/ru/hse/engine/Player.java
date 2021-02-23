package ru.hse.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.utils.Window;

public class Player {
    private final static float RUN_SPEED = 20;
    private final static float TURN_SPEED = 160;
    private final static float JUMP_POWER = 1;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private Vector3f position;

    private float rotX, rotY, rotZ;
    private float scale;

    private Window window;

    public Player(Window window, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.window = window;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz){
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz){
        rotX += dx;
        rotY += dy;
        rotZ += dz;
    }

    public void move(){
        checkInput();

        increaseRotation(0, currentTurnSpeed * window.getFrameTimeSeconds(), 0);

        float distance = currentSpeed * window.getFrameTimeSeconds();
        float dx = (float)(distance * Math.sin(Math.toRadians(getRotY())));
        float dz = (float)(distance * Math.cos(Math.toRadians(getRotY())));

        increasePosition(dx, upwardsSpeed, dz);
    }

    private void checkInput(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            currentSpeed = RUN_SPEED;
            //position.z -= 0.2f;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            currentSpeed = -RUN_SPEED;
            //position.z += 0.2f;
        }else{
            currentSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            currentTurnSpeed = -TURN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            currentTurnSpeed = TURN_SPEED;
        }else{
            currentTurnSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            upwardsSpeed = JUMP_POWER;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_X)){
            upwardsSpeed = -JUMP_POWER;
        }else{
            upwardsSpeed = 0;
        }
//        }else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
//            position.x -= 0.2f;
//        }else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
//            position.x += 0.2f;
//        }else if(Keyboard.isKeyDown(Keyboard.KEY_X)){
//            position.y -= 0.2f;
//        }else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
//            position.y += 0.2f;
//        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
