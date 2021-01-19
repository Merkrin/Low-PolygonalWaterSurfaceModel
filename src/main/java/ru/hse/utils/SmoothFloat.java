package ru.hse.utils;

public class SmoothFloat {
    private final float AGILITY;

    private float target;
    private float actual;

    public SmoothFloat(float initialValue, float agility) {
        target = initialValue;
        actual = initialValue;
        AGILITY = agility;
    }

    public void update(float delta) {
        float offset = target - actual;
        float change = offset * delta * AGILITY;

        actual += change;
    }

    public void increaseTarget(float dT) {
        this.target += dT;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public float get() {
        return actual;
    }

    public float getTarget() {
        return target;
    }
}
