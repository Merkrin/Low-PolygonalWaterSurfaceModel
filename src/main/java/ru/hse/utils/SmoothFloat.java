package ru.hse.utils;

/**
 * Class for float representation, that can be increased smoothly.
 */
public class SmoothFloat {
    // Smoothness of float changing.
    private final float SMOOTHNESS;

    // Actual float value and target value.
    private float actualValue;
    private float targetValue;

    /**
     * Constructor for the class.
     *
     * @param initialValue initial value of the float
     * @param smoothness   smoothness of float changing
     */
    public SmoothFloat(float initialValue, float smoothness) {
        actualValue = initialValue;
        targetValue = initialValue;

        SMOOTHNESS = smoothness;
    }

    /**
     * Method for actual value updating.
     *
     * @param delta updating delta
     */
    public void update(float delta) {
        float offset = targetValue - actualValue;
        float change = offset * delta * SMOOTHNESS;

        actualValue += change;
    }

    /**
     * Getter of the actual value.
     *
     * @return actual value
     */
    public float getActualValue() {
        return actualValue;
    }

    /**
     * Method for target value increasing.
     *
     * @param targetValueDelta increasing delta
     */
    public void increaseTargetValue(float targetValueDelta) {
        this.targetValue += targetValueDelta;
    }

    /**
     * Setter of the target value.
     *
     * @param targetValue value to set
     */
    public void setTargetValue(float targetValue) {
        this.targetValue = targetValue;
    }

    /**
     * Getter of the target value.
     *
     * @return target value
     */
    public float getTargetValue() {
        return targetValue;
    }
}
