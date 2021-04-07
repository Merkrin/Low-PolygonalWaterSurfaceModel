package ru.hse.terrain.utils;

import java.util.Random;

/**
 * Class for terrain heights map generation.
 */
public class PerlinNoiseGenerator {
    private static final Random RANDOM = new Random();

    private static final int randomBound = 1_000_000_000;
    private static final int firstConstant = 49632;
    private static final int secondConstant = 325176;

    private final float amplitude;
    private final float roughness;

    private final int octaves;
    private final int seed;

    /**
     * The class' constructor.
     *
     * @param seed      random seed
     * @param octaves   octaves value
     * @param amplitude height amplitude value
     * @param roughness roughness value
     */
    public PerlinNoiseGenerator(int seed, int octaves, float amplitude, float roughness) {
        this.seed = seed;
        this.octaves = octaves;
        this.amplitude = amplitude;
        this.roughness = roughness;
    }

    /**
     * The class' constructor.
     *
     * @param octaves   octaves value
     * @param amplitude height amplitude value
     * @param roughness roughness value
     */
    public PerlinNoiseGenerator(int octaves, float amplitude, float roughness) {
//        this.seed = new Random().nextInt(1000000000);
        seed = RANDOM.nextInt(randomBound);

        this.octaves = octaves;
        this.amplitude = amplitude;
        this.roughness = roughness;
    }

    /**
     * Method for perlin noise value getting.
     *
     * @param x x-coordinate of map
     * @param y y-coordinate of map
     * @return perlin noise value
     */
    public float getPerlinNoise(int x, int y) {
        float result = 0;

        float d = (float) Math.pow(2, octaves - 1);

        for (int i = 0; i < octaves; i++) {
            float frequency = (float) (Math.pow(2, i) / d);

            float amplitude = (float) Math.pow(roughness, i) * this.amplitude;

            result += getInterpolatedNoise(x * frequency, y * frequency) *
                    amplitude;
        }

        return result;
    }

    /**
     * Method for smooth noise value getting.
     *
     * @param x x-coordinate of map
     * @param y y-coordinate of map
     * @return smooth noise value
     */
    private float getSmoothNoise(int x, int y) {
        float corners = (getNoise(x - 1, y - 1) +
                getNoise(x + 1, y - 1) +
                getNoise(x - 1, y + 1) +
                getNoise(x + 1, y + 1)) / 16f;

        float sides = (getNoise(x - 1, y) +
                getNoise(x + 1, y) +
                getNoise(x, y - 1) +
                getNoise(x, y + 1)) / 8f;

        float center = getNoise(x, y) / 4f;

        return corners + sides + center;
    }

    /**
     * Method for noise value getting.
     *
     * @param x x-coordinate of map
     * @param y y-coordinate of map
     * @return noise value
     */
    private float getNoise(int x, int y) {
        return new Random((long) x * firstConstant +
                (long) y * secondConstant + seed).nextFloat() * 2f - 1f;
    }

    /**
     * Method for interpolated noise value getting.
     *
     * @param x x-coordinate of map
     * @param y y-coordinate of map
     * @return interpolated noise value
     */
    private float getInterpolatedNoise(float x, float y) {
        int xInteger = (int) x;
        float xFraction = x - xInteger;

        int yInteger = (int) y;
        float yFraction = y - yInteger;

        float v1 = getSmoothNoise(xInteger, yInteger);
        float v2 = getSmoothNoise(xInteger + 1, yInteger);
        float v3 = getSmoothNoise(xInteger, yInteger + 1);
        float v4 = getSmoothNoise(xInteger + 1, yInteger + 1);
        float i1 = interpolate(v1, v2, xFraction);
        float i2 = interpolate(v3, v4, xFraction);

        return interpolate(i1, i2, yFraction);
    }

    /**
     * Method for values interpolating.
     *
     * @param firstValue  first value to interpolate
     * @param secondValue second value to interpolate
     * @param blend       blending rate
     * @return interpolated value
     */
    private float interpolate(float firstValue, float secondValue,
                              float blend) {
        double theta = blend * Math.PI;

        float f = (float) ((1f - Math.cos(theta)) * 0.5f);

        return firstValue * (1 - f) + secondValue * f;
    }

    /**
     * Getter of the amplitude value.
     *
     * @return amplitude
     */
    public float getAmplitude() {
        return amplitude;
    }
}
