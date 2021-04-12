package ru.hse.terrain.utils;

import ru.hse.utils.Color;
import ru.hse.utils.Maths;

/**
 * Class for map colors generation.
 */
public class ColorGenerator {
    private final Color[] biomesColors;

    private final float spread;
    private final float halfSpread;
    private final float part;

    /**
     * The class' constructor.
     *
     * @param biomesColors colors of the terrain to be interpolated over the map
     * @param spread      setting of how big is the range of color spread
     */
    public ColorGenerator(Color[] biomesColors, float spread) {
        this.biomesColors = biomesColors;

        this.spread = spread;
        this.halfSpread = spread / 2f;
        this.part = 1f / (biomesColors.length - 1);
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

        int firstOfBiomes = (int) Math.floor(value / part);

        float blend = (value - (firstOfBiomes * part)) / part;

        return Color.interpolateColors(biomesColors[firstOfBiomes],
                biomesColors[firstOfBiomes + 1], blend,
                null);
    }
}
