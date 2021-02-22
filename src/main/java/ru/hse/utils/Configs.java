package ru.hse.utils;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.Serializable;

public class Configs implements Serializable {
    // Maximal FPS-rate.
    public static int FPS_CAP = 100;
    // Window sizes
    public static int SCREEN_WIDTH = 1280;
    public static int SCREEN_HEIGHT = 720;

    // Range of heights where colors should be spread over.
    public static float COLOR_SPREAD = 0.45f;
    public static Color[] TERRAIN_COLORS = new Color[]{
            new Color(201, 178, 99, true),
            new Color(164, 155, 98, true),
            new Color(164, 155, 98, true),
            new Color(229, 219, 164, true),
            new Color(135, 184, 82, true),
            new Color(120, 120, 120, true),
            new Color(200, 200, 210, true)};

    public static Vector3f LIGHT_POSITION = new Vector3f(0.3f, -1f, 0.5f);
    public static Color LIGHT_COLOR = new Color(1f, 0.95f, 0.95f);
    public static Vector2f LIGHT_BIAS = new Vector2f(0.3f, 0.8f);

    public static int WORLD_SIZE = 200;
    public static int SEED = 404536029;

    public static float AMPLITUDE = 30;
    public static float ROUGHNESS = 0.4f;
    // Like mountain intensivity
    public static int OCTAVES = 5;

    public static float WATER_HEIGHT = -1;

    public static float WAVE_SPEED = 0.002f;
    public static float WAVE_LENGTH = 4.0f;
    public static float WAVE_AMPLITUDE = 0.2f;

    public static int getFpsCap() {
        return FPS_CAP;
    }

    public static void setFpsCap(int fpsCap) {
        FPS_CAP = fpsCap;
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static void setScreenWidth(int screenWidth) {
        SCREEN_WIDTH = screenWidth;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static void setScreenHeight(int screenHeight) {
        SCREEN_HEIGHT = screenHeight;
    }

    public static float getColorSpread() {
        return COLOR_SPREAD;
    }

    public static void setColorSpread(float colorSpread) {
        COLOR_SPREAD = colorSpread;
    }

    public static Color[] getTerrainColors() {
        return TERRAIN_COLORS;
    }

    public static void setTerrainColors(Color[] terrainColors) {
        TERRAIN_COLORS = terrainColors;
    }

    public static Vector3f getLightPosition() {
        return LIGHT_POSITION;
    }

    public static void setLightPosition(Vector3f lightPosition) {
        LIGHT_POSITION = lightPosition;
    }

    public static Color getLightColor() {
        return LIGHT_COLOR;
    }

    public static void setLightColor(Color lightColor) {
        LIGHT_COLOR = lightColor;
    }

    public static Vector2f getLightBias() {
        return LIGHT_BIAS;
    }

    public static void setLightBias(Vector2f lightBias) {
        LIGHT_BIAS = lightBias;
    }

    public static int getWorldSize() {
        return WORLD_SIZE;
    }

    public static void setWorldSize(int worldSize) {
        WORLD_SIZE = worldSize;
    }

    public static int getSEED() {
        return SEED;
    }

    public static void setSEED(int SEED) {
        Configs.SEED = SEED;
    }

    public static float getAMPLITUDE() {
        return AMPLITUDE;
    }

    public static void setAMPLITUDE(float AMPLITUDE) {
        Configs.AMPLITUDE = AMPLITUDE;
    }

    public static float getROUGHNESS() {
        return ROUGHNESS;
    }

    public static void setROUGHNESS(float ROUGHNESS) {
        Configs.ROUGHNESS = ROUGHNESS;
    }

    public static int getOCTAVES() {
        return OCTAVES;
    }

    public static void setOCTAVES(int OCTAVES) {
        Configs.OCTAVES = OCTAVES;
    }

    public static float getWaterHeight() {
        return WATER_HEIGHT;
    }

    public static void setWaterHeight(float waterHeight) {
        WATER_HEIGHT = waterHeight;
    }

    public static float getWaveSpeed() {
        return WAVE_SPEED;
    }

    public static void setWaveSpeed(float waveSpeed) {
        WAVE_SPEED = waveSpeed;
    }

    public static float getWaveLength() {
        return WAVE_LENGTH;
    }

    public static void setWaveLength(float waveLength) {
        WAVE_LENGTH = waveLength;
    }

    public static float getWaveAmplitude() {
        return WAVE_AMPLITUDE;
    }

    public static void setWaveAmplitude(float waveAmplitude) {
        WAVE_AMPLITUDE = waveAmplitude;
    }
}
