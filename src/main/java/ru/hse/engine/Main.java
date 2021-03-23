package ru.hse.engine;

import org.lwjgl.util.vector.Vector3f;
import ru.hse.engine.exceptions.CommandLineArgumentsException;
import ru.hse.engine.exceptions.InvalidSettingExeption;
import ru.hse.terrain.generation.PolygonizedTerrainGenerator;
import ru.hse.terrain.generation.Terrain;
import ru.hse.terrain.generation.TerrainGenerator;
import ru.hse.terrain.utils.ColorGenerator;
import ru.hse.terrain.utils.PerlinNoiseGenerator;
import ru.hse.utils.CommandLineUtils;
import ru.hse.utils.Configs;
import ru.hse.water.generation.WaterGenerator;
import ru.hse.water.utils.WaterTile;

import java.io.IOException;

public class Main {
    private static void readArguments(String[] args) {
        boolean isMac = System.getProperty("os.name").startsWith("Mac");

        String[] arguments = null;

        if (isMac) {
            if (args.length != 1) {
                arguments = new String[args.length - 1];

                if (args.length - 1 >= 0) System.arraycopy(args, 1, arguments, 0, args.length - 1);
            }
        } else {
            if (args.length != 0) {
                arguments = args;
            }
        }

        if (arguments != null) {
            try {
                CommandLineUtils.readArguments(arguments);
            } catch (CommandLineArgumentsException e) {
                e.printStackTrace();
            } catch (InvalidSettingExeption invalidSettingExeption) {
                invalidSettingExeption.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        readArguments(args);
        RenderEngine engine = new RenderEngine(Configs.FPS_CAP, Configs.SCREEN_WIDTH, Configs.SCREEN_HEIGHT);
        Daemon player = new Daemon(engine.getWINDOW(), new Vector3f(200, 50, 200),
                0, 0, 0, 1);
        Camera camera = new Camera(player);
        Light light = new Light(Configs.LIGHT_POSITION, Configs.LIGHT_COLOR, Configs.LIGHT_BIAS);

        PerlinNoiseGenerator noise = new PerlinNoiseGenerator(Configs.SEED, Configs.OCTAVES, Configs.AMPLITUDE,
                Configs.ROUGHNESS);
        ColorGenerator colourGen = new ColorGenerator(Configs.TERRAIN_COLORS, Configs.COLOR_SPREAD);
        TerrainGenerator terrainGenerator = new PolygonizedTerrainGenerator(noise, colourGen);
        Terrain terrain = terrainGenerator.generateTerrain(Configs.WORLD_SIZE);

        WaterTile water = WaterGenerator.generate(Configs.WORLD_SIZE, Configs.WATER_HEIGHT);

        while (!engine.getWINDOW().isCloseRequested()) {
            camera.move();
            player.move();
            engine.render(terrain, water, camera, light);
        }

        water.delete();
        terrainGenerator.cleanUp();
        terrain.delete();

        engine.cleanUp();
    }
}
