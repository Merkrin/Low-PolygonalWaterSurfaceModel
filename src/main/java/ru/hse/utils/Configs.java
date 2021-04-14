package ru.hse.utils;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.engine.exceptions.InvalidSettingException;

import java.awt.*;
import java.io.Serializable;

/**
 * Configurations class.
 */
public class Configs implements Serializable {
    // Showing features flags.
    private static boolean showWater = true;
    private static boolean animateWater = true;

    // Maximal FPS-rate.
    public static int fpsCap = 100;
    // Window sizes
    public static int screenWidth = 1200;
    public static int screenHeight = 800;

    // Range of heights where colors should be spread over.
    public static float colorSpread = 0.45f;
    public static Color[] terrainColors = new Color[]{
            new Color(201, 178, 99, true),
            new Color(164, 155, 98, true),
            new Color(164, 155, 98, true),
            new Color(229, 219, 164, true),
            new Color(135, 184, 82, true),
            new Color(120, 120, 140, true),
            new Color(200, 200, 210, true)};

    public static Vector3f lightDirection = new Vector3f(0.3f, -1f, 0.5f);
    public static Color lightColor = new Color(1f, 0.95f, 0.95f);
    public static Vector2f lightBias = new Vector2f(0.3f, 0.8f);

    public static int worldSize = 200;
    public static int seed = 404536029;

    public static float amplitude = 30;
    public static float roughness = 0.4f;
    public static int octaves = 5;

    public static int waterHeight = -1;

    public static float waveSpeed = 0.002f;
    public static float waveLength = 4.0f;
    public static float waveAmplitude = 0.2f;

    public static boolean getShowWater() {
        return showWater;
    }

    public static void invertShowWater() {
        showWater = !showWater;
    }

    public static boolean getAnimateWater() {
        return animateWater;
    }

    public static void invertAnimateWater() {
        animateWater = !animateWater;
    }

    public static int getFpsCap() {
        return fpsCap;
    }

    public static void setFpsCap(int fpsCap) throws InvalidSettingException {
        if (fpsCap < 30 || fpsCap > 120)
            throw new InvalidSettingException("Invalid FPS cap argument.");

        Configs.fpsCap = fpsCap;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth)
            throws InvalidSettingException {
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();

        double width = screenRes.getWidth();

        if (screenWidth < width / 4 || screenWidth > width)
            throw new InvalidSettingException("Invalid screen width.");

        Configs.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight)
            throws InvalidSettingException {
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();

        double height = screenRes.getHeight();

        if (screenHeight < height / 4 || screenHeight > height)
            throw new InvalidSettingException("Invalid screen height.");

        Configs.screenHeight = screenHeight;
    }

    public static float getColorSpread() {
        return colorSpread;
    }

    public static void setColorSpread(float colorSpread)
            throws InvalidSettingException {
        if (colorSpread < 0.1f || colorSpread > 0.9f)
            throw new InvalidSettingException("Invalid " +
                    "color spread multiplier.");

        Configs.colorSpread = colorSpread;
    }

    public static Color[] getTerrainColors() {
        return terrainColors;
    }

    public static String getTerrainColorsAsString() {
        StringBuilder colors = new StringBuilder();

        for (int i = 0; i < terrainColors.length - 1; i++) {
            colors.append(terrainColors[i]).append(";");
        }

        colors.append(terrainColors[terrainColors.length - 1]);

        return colors.toString();
    }

    public static void setTerrainColors(Color[] terrainColors)
            throws InvalidSettingException {
        if (terrainColors == null || terrainColors.length == 0)
            throw new InvalidSettingException("Invalid terrain colors amount.");

        Configs.terrainColors = terrainColors;
    }

    public static Vector3f getLightDirection() {
        return lightDirection;
    }

    public static String getLightPositionAsString() {
        return lightDirection.x + ";" + lightDirection.y + ";" + lightDirection.z;
    }

    public static void setLightDirection(Vector3f lightDirection) {
        lightDirection.normalise();
        Configs.lightDirection = lightDirection;
    }

    public static Color getLightColor() {
        return lightColor;
    }

    public static void setLightColor(Color lightColor) {
        Configs.lightColor = lightColor;
    }

    public static Vector2f getLightBias() {
        return lightBias;
    }

    public static String getLightBiasAsString() {
        return lightBias.x + ";" + lightBias.y;
    }

    public static void setLightBias(Vector2f lightBias) {
        Configs.lightBias = lightBias;
    }

    public static int getWorldSize() {
        return worldSize;
    }

    public static void setWorldSize(int worldSize)
            throws InvalidSettingException {
        if (worldSize < 100 || worldSize > 500)
            throw new InvalidSettingException("Invalid world size.");

        Configs.worldSize = worldSize;
    }

    public static int getSeed() {
        return seed;
    }

    public static void setSeed(int seed) {
        Configs.seed = seed;
    }

    public static float getAmplitude() {
        return amplitude;
    }

    public static void setAmplitude(float amplitude)
            throws InvalidSettingException {
        if (amplitude < 10.0f || amplitude > 100.0f)
            throw new InvalidSettingException("Invalid amplitude value");

        Configs.amplitude = amplitude;
    }

    public static float getRoughness() {
        return roughness;
    }

    public static void setRoughness(float roughness)
            throws InvalidSettingException {
        if (roughness < 0.1f || roughness > 0.9f)
            throw new InvalidSettingException("Invalid roughness value");

        Configs.roughness = roughness;
    }

    public static int getOctaves() {
        return octaves;
    }

    public static void setOctaves(int octaves)
            throws InvalidSettingException {
        if (octaves < 2 || octaves > 10)
            throw new InvalidSettingException("Invalid octaves value");

        Configs.octaves = octaves;
    }

    public static int getWaterHeight() {
        return waterHeight;
    }

    public static void setWaterHeight(int waterHeight)
            throws InvalidSettingException {
        if (waterHeight < -amplitude || waterHeight > amplitude)
            throw new InvalidSettingException("Invalid water height");

        Configs.waterHeight = waterHeight;
    }

    public static float getWaveSpeed() {
        return waveSpeed;
    }

    public static void setWaveSpeed(float waveSpeed)
            throws InvalidSettingException {
        if (waveSpeed < 0.0005f || waveSpeed > 0.009f)
            throw new InvalidSettingException("Invalid wave speed value");

        Configs.waveSpeed = waveSpeed;
    }

    public static float getWaveLength() {
        return waveLength;
    }

    public static void setWaveLength(float waveLength)
            throws InvalidSettingException {
        if (waveLength < 1 || waveLength > 10)
            throw new InvalidSettingException("Invalid wave length");

        Configs.waveLength = waveLength;
    }

    public static float getWaveAmplitude() {
        return waveAmplitude;
    }

    public static void setWaveAmplitude(float waveAmplitude)
            throws InvalidSettingException {
        if (waveAmplitude < 0.05f || waveAmplitude > 0.9f)
            throw new InvalidSettingException("Invalid wave amplitude");

        Configs.waveAmplitude = waveAmplitude;
    }
}
