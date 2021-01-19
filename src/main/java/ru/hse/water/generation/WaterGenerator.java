package ru.hse.water.generation;

import org.lwjgl.util.vector.Vector2f;
import ru.hse.openGL.objects.Vao;
import ru.hse.openGL.utils.VaoLoader;
import ru.hse.utils.DataStorage;
import ru.hse.water.utils.WaterTile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WaterGenerator {
    private static final int VERTICES_PER_SQUARE = 3 * 2;// 2 triangles
    private static final int VERTEX_SIZE_BYTES = 8 + 4;// x,z position +
    // indicator

    public static WaterTile generate(int gridSize, float height) {
        int totalVertexAmount = gridSize * gridSize * VERTICES_PER_SQUARE;

        byte[] waterMeshData = createMeshData(gridSize, totalVertexAmount);

        Vao vao = VaoLoader.createWaterVao(waterMeshData);

        return new WaterTile(vao, totalVertexAmount, height);
    }

    private static byte[] createMeshData(int gridSize, int totalVertexCount) {
        int byteSize = VERTEX_SIZE_BYTES * totalVertexCount;

        ByteBuffer buffer = ByteBuffer.allocate(byteSize).order(ByteOrder.nativeOrder());

        for (int row = 0; row < gridSize; row++)
            for (int column = 0; column < gridSize; column++)
                storeGridSquare(column, row, buffer);

        return buffer.array();
    }

    private static void storeGridSquare(int column, int row,
                                        ByteBuffer buffer) {
        Vector2f[] cornerPosition = calculateCornerPositions(column, row);

        storeTriangle(cornerPosition, buffer, true);
        storeTriangle(cornerPosition, buffer, false);
    }

    private static void storeTriangle(Vector2f[] cornerPosition,
                                      ByteBuffer buffer,
                                      boolean left) {
        int index0 = left ? 0 : 2;

        byte[] indicators = {0, 0, 0, 0};

        DataStorage.packVertexData(cornerPosition[index0], indicators, buffer);

        int index1 = 1;

        DataStorage.packVertexData(cornerPosition[index1], indicators, buffer);

        int index2 = left ? 2 : 3;

        DataStorage.packVertexData(cornerPosition[index2], indicators, buffer);
    }

    private static Vector2f[] calculateCornerPositions(int column, int row) {
        Vector2f[] vertices = new Vector2f[4];

        vertices[0] = new Vector2f(column, row);
        vertices[1] = new Vector2f(column, row + 1);
        vertices[2] = new Vector2f(column + 1, row);
        vertices[3] = new Vector2f(column + 1, row + 1);

        return vertices;
    }
}
