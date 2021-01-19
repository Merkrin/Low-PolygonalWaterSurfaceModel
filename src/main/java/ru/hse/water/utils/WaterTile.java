package ru.hse.water.utils;

import ru.hse.openGL.objects.Vao;

public class WaterTile {
    private final Vao VAO;

    private final int VERTEX_AMOUNT;

    private final float HEIGHT;

    public WaterTile(Vao vao, int vertexCount, float height) {
        VAO = vao;
        HEIGHT = height;
        VERTEX_AMOUNT = vertexCount;
    }

    public Vao getVAO() {
        return VAO;
    }

    public float getHEIGHT() {
        return HEIGHT;
    }

    public int getVERTEX_AMOUNT() {
        return VERTEX_AMOUNT;
    }

    public void delete() {
        VAO.delete(true);
    }
}
