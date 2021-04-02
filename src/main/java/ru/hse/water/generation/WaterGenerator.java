package ru.hse.water.generation;

import org.lwjgl.util.vector.Vector2f;
import ru.hse.openGL.objects.Vao;
import ru.hse.openGL.utils.VaoLoader;
import ru.hse.utils.DataStorage;
import ru.hse.water.utils.WaterTile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class of water model generation.
 */
public class WaterGenerator {
    // There are 2 triangles with 3 vertices in a square of the model.
    private static final int VERTICES_PER_SQUARE = 3 * 2;
    // X and Z position (2 * 4) and indicator (4).
    private static final int VERTEX_SIZE_IN_BYTES = 8 + 4;

    /**
     * Method for water model generation.
     *
     * @param gridSize size of the wanted grid
     * @param height   height of the wanted grid
     * @return generated {@link WaterTile}
     */
    public static WaterTile generate(int gridSize, float height) {
        int totalVertexAmount = gridSize * gridSize * VERTICES_PER_SQUARE;

        byte[] waterMeshData = createMeshData(gridSize, totalVertexAmount);

        Vao vao = VaoLoader.createWaterVao(waterMeshData);

        return new WaterTile(vao, totalVertexAmount, height);
    }

    /**
     * Method for the water model's mesh data generation.
     *
     * @param gridSize          size of the wanted grid
     * @param totalVertexAmount amount of the vertices in the mesh
     * @return array of bytes with vertices' data
     */
    private static byte[] createMeshData(int gridSize, int totalVertexAmount) {
        int bytesAmount = VERTEX_SIZE_IN_BYTES * totalVertexAmount;

        ByteBuffer buffer = ByteBuffer.allocate(bytesAmount)
                .order(ByteOrder.nativeOrder());

        for (int row = 0; row < gridSize; row++)
            for (int column = 0; column < gridSize; column++)
                storeGridSquare(column, row, buffer);

        return buffer.array();
    }

    /**
     * Method for grid square storing.
     *
     * @param column column of the square in the grid
     * @param row    row of the square in the grid
     * @param buffer buffer to store the square in
     */
    private static void storeGridSquare(int column, int row,
                                        ByteBuffer buffer) {
        Vector2f[] cornerPosition = calculateCornerPositions(column, row);

        storeTriangle(cornerPosition, buffer, true);
        storeTriangle(cornerPosition, buffer, false);
    }

    /**
     * Method for grid square triangle storing.
     *
     * @param cornerPositions vector with triangle's corner positions
     * @param buffer          buffer to store the triangle in
     * @param left            flag if the square is the left one or not
     */
    private static void storeTriangle(Vector2f[] cornerPositions,
                                      ByteBuffer buffer,
                                      boolean left) {
        int firstIndex = left ? 0 : 2;
        int secondIndex = 1;
        int thirdIndex = left ? 2 : 3;

        DataStorage.packVertexData(cornerPositions[firstIndex],
                getIndicators(firstIndex, cornerPositions,
                        secondIndex, thirdIndex),
                buffer);
        DataStorage.packVertexData(cornerPositions[secondIndex],
                getIndicators(secondIndex, cornerPositions,
                        thirdIndex, firstIndex),
                buffer);
        DataStorage.packVertexData(cornerPositions[thirdIndex],
                getIndicators(thirdIndex, cornerPositions,
                        firstIndex, secondIndex),
                buffer);
    }

    /**
     * Method for square's corner positions calculations.
     *
     * @param column column of the square in the grid
     * @param row    row of the square in the grid
     * @return vector of corners' positions
     */
    private static Vector2f[] calculateCornerPositions(int column, int row) {
        Vector2f[] vertices = new Vector2f[4];

        vertices[0] = new Vector2f(column, row);
        vertices[1] = new Vector2f(column, row + 1);
        vertices[2] = new Vector2f(column + 1, row);
        vertices[3] = new Vector2f(column + 1, row + 1);

        return vertices;
    }

    /**
     * Method for vertex' indicator getting.
     *
     * @param currentVertex   current vertex index
     * @param vertexPositions array of vertex positions
     * @param firstVertex     another vertex of the triangle
     * @param secondVertex    another vertex of the triangle
     * @return array of bytes with indicators
     */
    private static byte[] getIndicators(int currentVertex,
                                        Vector2f[] vertexPositions,
                                        int firstVertex, int secondVertex) {
        Vector2f currentVertexPosition = vertexPositions[currentVertex];
        Vector2f firstVertexPosition = vertexPositions[firstVertex];
        Vector2f secondVertexPosition = vertexPositions[secondVertex];

        Vector2f firstOffset = Vector2f.sub(firstVertexPosition,
                currentVertexPosition,
                null);
        Vector2f secondOffset = Vector2f.sub(secondVertexPosition,
                currentVertexPosition,
                null);

        return new byte[]{(byte) firstOffset.x,
                (byte) firstOffset.y,
                (byte) secondOffset.x,
                (byte) secondOffset.y};
    }
}
