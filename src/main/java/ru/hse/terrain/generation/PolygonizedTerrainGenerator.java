package ru.hse.terrain.generation;

import ru.hse.graphics.TerrainRenderer;
import ru.hse.graphics.TerrainShader;
import ru.hse.openGL.objects.Vao;
import ru.hse.openGL.utils.VaoLoader;
import ru.hse.terrain.utils.ColorGenerator;
import ru.hse.terrain.utils.GridCell;
import ru.hse.terrain.utils.IndexGenerator;
import ru.hse.terrain.utils.PerlinNoiseGenerator;
import ru.hse.utils.Color;
import ru.hse.utils.FileBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class for terrain generation.
 */
public class PolygonizedTerrainGenerator extends TerrainGenerator {
    private static final FileBuffer VERTEX_SHADER =
            new FileBuffer("ru/hse/openGL/shaders", "terrainVertex.glsl");
    private static final FileBuffer FRAGMENT_SHADER =
            new FileBuffer("ru/hse/openGL/shaders", "terrainFragment.glsl");

    // position + normal + color
    private static final int BYTES_PER_VERTEX = 12 + 4 + 4;

    private final TerrainRenderer terrainRenderer;

    /**
     * The class' constructor.
     *
     * @param perlinNoiseGenerator perlin noise generator instance
     * @param colorGenerator       color generator instance
     */
    public PolygonizedTerrainGenerator(PerlinNoiseGenerator perlinNoiseGenerator,
                                       ColorGenerator colorGenerator) {
        super(perlinNoiseGenerator, colorGenerator);

        this.terrainRenderer = new TerrainRenderer(
                new TerrainShader(VERTEX_SHADER, FRAGMENT_SHADER),
                true);
    }

    @Override
    protected Terrain createTerrain(float[][] heights, Color[][] colors) {
        int vertexCount = calculateVerticesAmount(heights.length);

        byte[] terrainData = createMeshData(heights, colors, vertexCount);
        int[] indices = IndexGenerator.generateIndexBuffer(heights.length);

        Vao vao = VaoLoader.createVao(terrainData, indices);

        return new Terrain(vao, indices.length, terrainRenderer);
    }

    /**
     * Method for mesh vertices amount calculation.
     *
     * @param gridSize size of the grid
     * @return amount of vertices needed
     */
    private int calculateVerticesAmount(int gridSize) {
        int lastTwoRowsAmount = 2 * gridSize;
        int remainingRowsAmount = gridSize - 2;
        int topSideAmount = remainingRowsAmount * (gridSize - 1) * 2;

        return topSideAmount + lastTwoRowsAmount;
    }

    /**
     * Method for mesh creation.
     *
     * @param heights        height map
     * @param colors         colors of the map
     * @param verticesAmount amount of vertices
     * @return byte-array-like mesh
     */
    private byte[] createMeshData(float[][] heights, Color[][] colors,
                                  int verticesAmount) {
        int sizeInBytes = BYTES_PER_VERTEX * verticesAmount;

        ByteBuffer buffer = ByteBuffer.allocate(sizeInBytes)
                .order(ByteOrder.nativeOrder());

        GridCell[] lastRow = new GridCell[heights.length - 1];

        for (int row = 0; row < heights.length - 1; row++) {
            for (int column = 0; column < heights[row].length - 1; column++) {
                GridCell square = new GridCell(row, column, heights, colors);

                square.storeSquareData(buffer);

                if (row == heights.length - 2)
                    lastRow[column] = square;
            }
        }

        for (GridCell gridCell : lastRow)
            gridCell.storeLastRowData(buffer);

        return buffer.array();
    }

    @Override
    public void cleanUp() {
        terrainRenderer.cleanUp();
    }
}
