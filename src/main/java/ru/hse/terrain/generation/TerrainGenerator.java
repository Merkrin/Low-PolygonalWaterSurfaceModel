package ru.hse.terrain.generation;

import ru.hse.terrain.utils.ColorGenerator;
import ru.hse.terrain.utils.PerlinNoiseGenerator;
import ru.hse.utils.Color;

/**
 * Terrain generation class.
 */
public abstract class TerrainGenerator {
    private final PerlinNoiseGenerator perlinNoiseGenerator;
    private final ColorGenerator colorGenerator;

    /**
     * The class' constructor.
     *
     * @param perlinNoiseGenerator perlin noise generator instance
     * @param colorGenerator       color generator instance
     */
    TerrainGenerator(PerlinNoiseGenerator perlinNoiseGenerator,
                     ColorGenerator colorGenerator) {
        this.perlinNoiseGenerator = perlinNoiseGenerator;
        this.colorGenerator = colorGenerator;
    }

    /**
     * Abstract method for terrain generation.
     *
     * @param heights heights of the map
     * @param colors  color of the map
     * @return created Terrain
     */
    protected abstract Terrain createTerrain(float[][] heights, Color[][] colors);

    /**
     * Method for terrain generation.
     *
     * @param gridSize size of the grid to generate
     * @return generated Terrain
     */
    public Terrain generateTerrain(int gridSize) {
        float[][] heights = generateHeights(gridSize, perlinNoiseGenerator);

        Color[][] colors = colorGenerator
                .generateColors(heights, perlinNoiseGenerator.getAmplitude());

        return createTerrain(heights, colors);
    }

    /**
     * Method for height map generation.
     *
     * @param gridSize             size of the grid
     * @param perlinNoiseGenerator perlin noise generator instance
     * @return height map
     */
    private float[][] generateHeights(int gridSize,
                                      PerlinNoiseGenerator perlinNoiseGenerator) {
        float[][] heights = new float[gridSize + 1][gridSize + 1];

        for (int row = 0; row < gridSize + 1; row++)
            for (int column = 0; column < gridSize + 1; column++)
                heights[row][column] = perlinNoiseGenerator
                        .getPerlinNoise(column, row);

        return heights;
    }

    /**
     * "Destructor" for the class.
     */
    public abstract void cleanUp();
}
