package ru.hse.terrain.utils;

import org.lwjgl.util.vector.Vector3f;
import ru.hse.utils.Color;
import ru.hse.utils.DataStorage;
import ru.hse.utils.Maths;

import java.nio.ByteBuffer;

public class GridCell {
    private final int row;
    private final int col;
    private final int lastIndex;
    private final Vector3f[] positions;
    private final Color[] colours;
    private final Vector3f normalLeft;
    private final Vector3f normalRight;

    public GridCell(int row, int col, float[][] heights, Color[][] colours) {
        this.positions = calculateCornerPositions(col, row, heights);
        this.colours = calculateCornerColours(col, row, colours);
        this.lastIndex = heights.length - 2;
        this.row = row;
        this.col = col;
        boolean rightHanded = col % 2 != row % 2;
        this.normalLeft = Maths.calculateNormal(positions[0], positions[1], positions[rightHanded ? 3 : 2]);
        this.normalRight = Maths.calculateNormal(positions[2], positions[rightHanded ? 0 : 1], positions[3]);
    }

    public void storeSquareData(ByteBuffer buffer) {
        storeTopLeftVertex(buffer);
        if (row != lastIndex || col == lastIndex) {
            storeTopRightVertex(buffer);
        }
    }

    public void storeBottomRowData(ByteBuffer buffer) {
        if (col == 0) {
            storeBottomLeftVertex(buffer);
        }
        storeBottomRightVertex(buffer);
    }

    private Color[] calculateCornerColours(int col, int row, Color[][] colours) {
        Color[] cornerCols = new Color[4];
        cornerCols[0] = colours[row][col];
        cornerCols[1] = colours[row + 1][col];
        cornerCols[2] = colours[row][col + 1];
        cornerCols[3] = colours[row + 1][col + 1];
        return cornerCols;
    }

    private Vector3f[] calculateCornerPositions(int col, int row, float[][] heights) {
        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(col, heights[row][col], row);
        vertices[1] = new Vector3f(col, heights[row + 1][col], row + 1);
        vertices[2] = new Vector3f(col + 1, heights[row][col + 1], row);
        vertices[3] = new Vector3f(col + 1, heights[row + 1][col + 1], row + 1);
        return vertices;
    }

    private void storeTopLeftVertex(ByteBuffer buffer) {
        DataStorage.packVertexData(positions[0], normalLeft, colours[0], buffer);
    }

    private void storeTopRightVertex(ByteBuffer buffer) {
        DataStorage.packVertexData(positions[2], normalRight, colours[2], buffer);
    }

    private void storeBottomLeftVertex(ByteBuffer buffer) {
        DataStorage.packVertexData(positions[1], normalLeft, colours[1], buffer);
    }

    private void storeBottomRightVertex(ByteBuffer buffer) {
        DataStorage.packVertexData(positions[3], normalRight, colours[3], buffer);
    }
}
