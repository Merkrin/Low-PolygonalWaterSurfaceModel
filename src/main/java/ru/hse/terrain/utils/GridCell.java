package ru.hse.terrain.utils;

import org.lwjgl.util.vector.Vector3f;
import ru.hse.utils.Color;
import ru.hse.utils.DataStorage;
import ru.hse.utils.Maths;

import java.nio.ByteBuffer;

/**
 * Class for grid cell representation.
 */
public class GridCell {
    private final Color[] colors;

    private final Vector3f[] cornerPositions;
    private final Vector3f leftNormal;
    private final Vector3f rightNormal;

    private final int row;
    private final int column;

    private final int lastIndex;

    /**
     * The class' constructor.
     *
     * @param row     row of the cell
     * @param column  column of the cell
     * @param heights height map of the terrain
     * @param colors  colors of the terrain
     */
    public GridCell(int row, int column, float[][] heights, Color[][] colors) {
        cornerPositions = getCornerPositions(row, column, heights);

        this.colors = getCornerColors(row, column, colors);

        this.row = row;
        this.column = column;
        lastIndex = heights.length - 2;

        boolean isRightHanded = column % 2 != row % 2;

        leftNormal = Maths.calculateNormal(cornerPositions[0],
                cornerPositions[1],
                cornerPositions[isRightHanded ? 3 : 2]);
        rightNormal = Maths.calculateNormal(cornerPositions[2],
                cornerPositions[isRightHanded ? 0 : 1],
                cornerPositions[3]);
    }

    /**
     * Method for square data storing.
     *
     * @param buffer buffer to use
     */
    public void storeSquareData(ByteBuffer buffer) {
        storeTopLeftVertex(buffer);

        if (row != lastIndex || column == lastIndex)
            storeTopRightVertex(buffer);
    }

    /**
     * Method for the last row data storing.
     *
     * @param buffer buffer to use
     */
    public void storeLastRowData(ByteBuffer buffer) {
        if (column == 0)
            storeBottomLeftVertex(buffer);

        storeBottomRightVertex(buffer);
    }

    /**
     * Method for corner colors getting.
     *
     * @param row    row of the cell
     * @param column column of the cell
     * @param colors colors of the terrain
     * @return colors of cell corners
     */
    private Color[] getCornerColors(int row, int column,
                                    Color[][] colors) {
        Color[] cornerCols = new Color[4];

        cornerCols[0] = colors[row][column];
        cornerCols[1] = colors[row + 1][column];
        cornerCols[2] = colors[row][column + 1];
        cornerCols[3] = colors[row + 1][column + 1];

        return cornerCols;
    }

    /**
     * Method for corner positions getting.
     *
     * @param row     row of the cell
     * @param column  column of the cell
     * @param heights height map of the terrain
     * @return positions of cell corners
     */
    private Vector3f[] getCornerPositions(int row, int column,
                                          float[][] heights) {
        Vector3f[] vertices = new Vector3f[4];

        vertices[0] = new Vector3f(column, heights[row][column], row);
        vertices[1] = new Vector3f(column, heights[row + 1][column], row + 1);
        vertices[2] = new Vector3f(column + 1, heights[row][column + 1], row);
        vertices[3] = new Vector3f(column + 1, heights[row + 1][column + 1],
                row + 1);

        return vertices;
    }

    /**
     * Method for top left vertex storing.
     *
     * @param buffer buffer to use
     */
    private void storeTopLeftVertex(ByteBuffer buffer) {
        DataStorage.packVertexData(cornerPositions[0], leftNormal,
                colors[0], buffer);
    }

    /**
     * Method for top right vertex storing.
     *
     * @param buffer buffer to use
     */
    private void storeTopRightVertex(ByteBuffer buffer) {
        DataStorage.packVertexData(cornerPositions[2], rightNormal,
                colors[2], buffer);
    }

    /**
     * Method for bottom left vertex storing.
     *
     * @param buffer buffer to use
     */
    private void storeBottomLeftVertex(ByteBuffer buffer) {
        DataStorage.packVertexData(cornerPositions[1], leftNormal,
                colors[1], buffer);
    }

    /**
     * Method for bottom right vertex storing.
     *
     * @param buffer buffer to use
     */
    private void storeBottomRightVertex(ByteBuffer buffer) {
        DataStorage.packVertexData(cornerPositions[3], rightNormal,
                colors[3], buffer);
    }
}
