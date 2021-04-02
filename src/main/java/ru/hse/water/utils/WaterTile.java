package ru.hse.water.utils;

import ru.hse.openGL.objects.Vao;

/**
 * Water tile class.
 */
public class WaterTile {
    // The tile's vertex array object.
    private final Vao VAO;

    private final int VERTEX_AMOUNT;

    // Height of the tile.
    private final float HEIGHT;

    /**
     * The class' constructor.
     *
     * @param vao          vertex array object of the tile
     * @param vertexAmount amount of vertices of the tile
     * @param height       height of the tile
     */
    public WaterTile(Vao vao, int vertexAmount, float height) {
        VAO = vao;
        HEIGHT = height;
        VERTEX_AMOUNT = vertexAmount;
    }

    /**
     * Getter of the tile's vertex array object.
     *
     * @return vertex array object
     */
    public Vao getVao() {
        return VAO;
    }

    /**
     * Getter of the tile's height.
     *
     * @return height of the tile
     */
    public float getHeight() {
        return HEIGHT;
    }

    /**
     * Getter of the tile's vertices amount.
     *
     * @return amount of vertices of the tile
     */
    public int getVertexAmount() {
        return VERTEX_AMOUNT;
    }

    /**
     * "Destructor" of the tile.
     */
    public void delete() {
        VAO.delete(true);
    }
}
