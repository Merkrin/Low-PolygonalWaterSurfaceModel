package ru.hse.utils;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.Serializable;

public class Configurator implements Serializable {
    private int FPS_CAP;

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private float COLOR_SPREAD;
    private Color[] TERRAIN_COLORS;

    private Vector3f LIGHT_POSITION;
    private Color LIGHT_COLOR;
    private Vector2f LIGHT_BIAS;

    private int WORLD_SIZE;
    private int SEED;

    private float AMPLITUDE;
    private float ROUGHNESS;
    private int OCTAVES;

    private float WATER_HEIGHT;

    private float WAVE_SPEED;
    private float WAVE_LENGTH;
    private float WAVE_AMPLITUDE;

    public void copyConfigs() {
        setFPS_CAP(Configs.getFpsCap());
    }

    public int getFPS_CAP() {
        return FPS_CAP;
    }

    public void setFPS_CAP(int FPS_CAP) {
        this.FPS_CAP = FPS_CAP;
    }

    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public void setSCREEN_WIDTH(int SCREEN_WIDTH) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
    }

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public void setSCREEN_HEIGHT(int SCREEN_HEIGHT) {
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
    }

    public float getCOLOR_SPREAD() {
        return COLOR_SPREAD;
    }

    public void setCOLOR_SPREAD(float COLOR_SPREAD) {
        this.COLOR_SPREAD = COLOR_SPREAD;
    }

    public Color[] getTERRAIN_COLORS() {
        return TERRAIN_COLORS;
    }

    public void setTERRAIN_COLORS(Color[] TERRAIN_COLORS) {
        this.TERRAIN_COLORS = TERRAIN_COLORS;
    }

    public Vector3f getLIGHT_POSITION() {
        return LIGHT_POSITION;
    }

    public void setLIGHT_POSITION(Vector3f LIGHT_POSITION) {
        this.LIGHT_POSITION = LIGHT_POSITION;
    }

    public Color getLIGHT_COLOR() {
        return LIGHT_COLOR;
    }

    public void setLIGHT_COLOR(Color LIGHT_COLOR) {
        this.LIGHT_COLOR = LIGHT_COLOR;
    }

    public Vector2f getLIGHT_BIAS() {
        return LIGHT_BIAS;
    }

    public void setLIGHT_BIAS(Vector2f LIGHT_BIAS) {
        this.LIGHT_BIAS = LIGHT_BIAS;
    }

    public int getWORLD_SIZE() {
        return WORLD_SIZE;
    }

    public void setWORLD_SIZE(int WORLD_SIZE) {
        this.WORLD_SIZE = WORLD_SIZE;
    }

    public int getSEED() {
        return SEED;
    }

    public void setSEED(int SEED) {
        this.SEED = SEED;
    }

    public float getAMPLITUDE() {
        return AMPLITUDE;
    }

    public void setAMPLITUDE(float AMPLITUDE) {
        this.AMPLITUDE = AMPLITUDE;
    }

    public float getROUGHNESS() {
        return ROUGHNESS;
    }

    public void setROUGHNESS(float ROUGHNESS) {
        this.ROUGHNESS = ROUGHNESS;
    }

    public int getOCTAVES() {
        return OCTAVES;
    }

    public void setOCTAVES(int OCTAVES) {
        this.OCTAVES = OCTAVES;
    }

    public float getWATER_HEIGHT() {
        return WATER_HEIGHT;
    }

    public void setWATER_HEIGHT(float WATER_HEIGHT) {
        this.WATER_HEIGHT = WATER_HEIGHT;
    }

    public float getWAVE_SPEED() {
        return WAVE_SPEED;
    }

    public void setWAVE_SPEED(float WAVE_SPEED) {
        this.WAVE_SPEED = WAVE_SPEED;
    }

    public float getWAVE_LENGTH() {
        return WAVE_LENGTH;
    }

    public void setWAVE_LENGTH(float WAVE_LENGTH) {
        this.WAVE_LENGTH = WAVE_LENGTH;
    }

    public float getWAVE_AMPLITUDE() {
        return WAVE_AMPLITUDE;
    }

    public void setWAVE_AMPLITUDE(float WAVE_AMPLITUDE) {
        this.WAVE_AMPLITUDE = WAVE_AMPLITUDE;
    }
}
