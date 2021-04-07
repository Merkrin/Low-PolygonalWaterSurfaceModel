package ru.hse.terrain.utils;

import ru.hse.utils.Color;
import ru.hse.utils.Maths;

/**
 * Class for map colors generation.
 */
public class ColorGenerator {
    private final Color[] biomeColors;

    private final float spread;
    private final float halfSpread;
    private final float part;

    /**
     * The class' constructor.
     *
     * @param biomeColors colors of the terrain to be interpolated over the map
     * @param spread      setting of how big is the range of color spread
     */
    public ColorGenerator(Color[] biomeColors, float spread) {
        this.biomeColors = biomeColors;

        this.spread = spread;
        this.halfSpread = spread / 2f;
        this.part = 1f / (biomeColors.length - 1);
    }

    /**
     * Method for vertex color calculation.
     *
     * @param heights   height map
     * @param amplitude amplitude of the map generation
     * @return colors of the map
     */
    public Color[][] generateColors(float[][] heights, float amplitude) {
        Color[][] colors = new Color[heights.length][heights.length];

        for (int row = 0; row < heights.length; row++)
            for (int column = 0; column < heights[row].length; column++)
                colors[row][column] = calculateColor(heights[row][column],
                        amplitude);

        return colors;
    }

    /**Determines the colour of the vertex based on the provided height.
     * @param height - Height of the vertex.
     * @param amplitude - The maximum height that a vertex can be (
     * @return
     */
    /**
     * Method for color calculation.
     *
     * @param height    height of the vertex
     * @param amplitude amplitude of the map generation
     * @return color of the vertex
     */
    private Color calculateColor(float height, float amplitude) {
        float value = (height + amplitude) / (amplitude * 2);

        value = Maths.clamp((value - halfSpread) * (1f / spread), 0f, 0.9999f);

        int firstBiome = (int) Math.floor(value / part);

        float blend = (value - (firstBiome * part)) / part;

        return Color.interpolateColours(biomeColors[firstBiome],
                biomeColors[firstBiome + 1], blend,
                null);
    }
}
