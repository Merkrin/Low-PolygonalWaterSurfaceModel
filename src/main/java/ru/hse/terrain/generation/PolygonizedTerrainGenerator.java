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

public class PolygonizedTerrainGenerator extends TerrainGenerator {
    private static final FileBuffer VERTEX_SHADER = new FileBuffer("ru/hse/openGL/shaders", "terrainVertex.glsl");
    private static final FileBuffer FRAGMENT_SHADER = new FileBuffer("ru/hse/openGL/shaders", "terrainFragment.glsl");

    private static final int VERTEX_SIZE_BYTES = 12 + 4 + 4;// position + normal + colour

    private final TerrainRenderer renderer;

    public PolygonizedTerrainGenerator(PerlinNoiseGenerator perlinNoise, ColorGenerator colourGen) {
        super(perlinNoise, colourGen);
        this.renderer = new TerrainRenderer(new TerrainShader(VERTEX_SHADER, FRAGMENT_SHADER), true);
    }

    @Override
    public void cleanUp() {
        renderer.cleanUp();
    }

    @Override
    protected Terrain createTerrain(float[][] heights, Color[][] colours) {
        int vertexCount = calculateVertexCount(heights.length);
        byte[] terrainData = createMeshData(heights, colours, vertexCount);
        int[] indices = IndexGenerator.generateIndexBuffer(heights.length);
        Vao vao = VaoLoader.createVao(terrainData, indices);

        return new Terrain(vao, indices.length, renderer);
    }

    private int calculateVertexCount(int vertexLength) {
        int bottom2Rows = 2 * vertexLength;
        int remainingRowCount = vertexLength - 2;
        int topCount = remainingRowCount * (vertexLength - 1) * 2;
        return topCount + bottom2Rows;
    }

    private byte[] createMeshData(float[][] heights, Color[][] colours, int vertexCount) {
        int byteSize = VERTEX_SIZE_BYTES * vertexCount;

        ByteBuffer buffer = ByteBuffer.allocate(byteSize).order(ByteOrder.nativeOrder());
        GridCell[] lastRow = new GridCell[heights.length - 1];

        for (int row = 0; row < heights.length - 1; row++) {
            for (int col = 0; col < heights[row].length - 1; col++) {
                GridCell square = new GridCell(row, col, heights, colours);
                square.storeSquareData(buffer);
                if (row == heights.length - 2) {
                    lastRow[col] = square;
                }
            }
        }

        for (int i = 0; i < lastRow.length; i++) {
            lastRow[i].storeBottomRowData(buffer);
        }

        return buffer.array();
    }
}
