package ru.hse.engine;

import ru.hse.terrain.generation.PolygonizedTerrainGenerator;
import ru.hse.terrain.generation.Terrain;
import ru.hse.terrain.generation.TerrainGenerator;
import ru.hse.terrain.utils.ColorGenerator;
import ru.hse.terrain.utils.PerlinNoiseGenerator;
import ru.hse.utils.Configs;
import ru.hse.water.generation.WaterGenerator;
import ru.hse.water.utils.WaterTile;

public class Main {
    public static void main(String[] args) {
        RenderEngine engine = new RenderEngine(Configs.FPS_CAP, Configs.SCREEN_WIDTH, Configs.SCREEN_HEIGHT);
        Camera camera = new Camera();
        Light light = new Light(Configs.LIGHT_POSITION, Configs.LIGHT_COLOR, Configs.LIGHT_BIAS);

        PerlinNoiseGenerator noise = new PerlinNoiseGenerator(Configs.SEED, Configs.OCTAVES, Configs.AMPLITUDE, Configs.ROUGHNESS);
        ColorGenerator colourGen = new ColorGenerator(Configs.TERRAIN_COLORS, Configs.COLOR_SPREAD);
        TerrainGenerator terrainGenerator = new PolygonizedTerrainGenerator(noise, colourGen);
        Terrain terrain = terrainGenerator.generateTerrain(Configs.WORLD_SIZE);

        WaterTile water = WaterGenerator.generate(Configs.WORLD_SIZE, Configs.WATER_HEIGHT);

        while (!engine.getWINDOW().isCloseRequested()) {
            camera.move();
            engine.render(terrain, water, camera, light);
        }

        water.delete();
        terrainGenerator.cleanUp();
        terrain.delete();

        engine.close();
    }
}
