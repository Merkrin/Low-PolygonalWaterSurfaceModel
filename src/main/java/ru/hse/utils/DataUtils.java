package ru.hse.utils;

/**
 * Utility class for data storing.
 */
public class DataUtils {
    private static final int TEN_BITS_MAX = (int) (Math.pow(2, 10) - 1);
    private static final int TWO_BITS_MAX = (int) (Math.pow(2, 2) - 1);

    public static final int BYTES_IN_INT = 4;

    /**
     * Pack vector to integer.
     *
     * @param x x-value
     * @param y y-value
     * @param z z-value
     * @param w w-value
     * @return packed value
     */
    public static int packInt(float x, float y, float z, float w) {
        int value = 0;

        value = value | (quantizeNormalized(w, TWO_BITS_MAX, false) << 30);
        value = value | (quantizeNormalized(z, TEN_BITS_MAX, true) << 20);
        value = value | (quantizeNormalized(y, TEN_BITS_MAX, true) << 10);
        value = value | quantizeNormalized(x, TEN_BITS_MAX, true);

        return value;
    }

    /**
     * Utility method for value storing.
     *
     * @param original     original value
     * @param highestLevel maximal value
     * @param signed       is value signed or nor
     * @return needed value
     */
    public static int quantizeNormalized(float original, int highestLevel,
                                         boolean signed) {
        if (signed)
            original = original * 0.5f + 0.5f;

        return Math.round(original * highestLevel);
    }
}
