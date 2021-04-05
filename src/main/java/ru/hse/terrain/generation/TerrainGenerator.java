package ru.hse.terrain.generation;

import ru.hse.terrain.utils.ColorGenerator;
import ru.hse.terrain.utils.PerlinNoiseGenerator;
import ru.hse.utils.Color;

public abstract class TerrainGenerator {
    private final PerlinNoiseGenerator perlinNoise;
    private final ColorGenerator colorGenerator;

    public TerrainGenerator(PerlinNoiseGenerator perlinNoise,
                            ColorGenerator colorGenerator) {
        this.perlinNoise = perlinNoise;
        this.colorGenerator = colorGenerator;
    }

    public Terrain generateTerrain(int gridSize) {
        float[][] heights = generateHeights(gridSize, perlinNoise);
        Color[][] colours = colorGenerator.generateColours(heights, perlinNoise.getAmplitude());
        return createTerrain(heights, colours);
    }

    public abstract void cleanUp();

    protected abstract Terrain createTerrain(float[][] heights, Color[][] colours);

    private float[][] generateHeights(int gridSize, PerlinNoiseGenerator perlinNoise) {
        float heights[][] = new float[gridSize + 1][gridSize + 1];

        for (int z = 0; z < heights.length; z++)
            for (int x = 0; x < heights[z].length; x++)
                heights[z][x] = perlinNoise.getPerlinNoise(x, z);

        return heights;
    }
}
