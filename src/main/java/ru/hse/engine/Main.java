package ru.hse.engine;

import org.lwjgl.LWJGLException;
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
import ru.hse.utils.InputParser;
import ru.hse.water.generation.WaterGenerator;
import ru.hse.water.utils.WaterTile;

import java.io.IOException;

public class Main {
    /**
     * Method for command line arguments reading for further processing.
     *
     * @param args arguments from the command line
     */
    private static void readArguments(String[] args) {
        boolean isMac = System.getProperty("os.name").startsWith("Mac");

        String[] arguments = null;

        if (isMac) {
            if (args.length != 1) {
                arguments = new String[args.length - 1];

                if (args.length - 1 >= 0) System.arraycopy(args, 1,
                        arguments, 0, args.length - 1);
            }
        } else {
            if (args.length != 0)
                arguments = args;
        }

        if (arguments != null) {
            try {
                CommandLineUtils.readArguments(arguments);
            } catch (CommandLineArgumentsException | InvalidSettingExeption e) {
                System.out.println("An error in command line format found: " +
                        e.getMessage());
                System.out.println("Starting with standard settings...");
            } catch (IOException e) {
                System.out.println("An error while reading file occurred: " +
                        e.getMessage());
                System.out.println("Starting with standard settings...");
            }
        }
    }

    public static void main(String[] args) {
        readArguments(args);

        try {
            RenderEngine engine = new RenderEngine(Configs.getFpsCap(),
                    Configs.getScreenWidth(), Configs.getScreenHeight());
            Daemon daemon = new Daemon(engine.getWINDOW(), new Vector3f(200, 50, 200),
                    0, 0, 0, 1);
            Camera camera = new Camera(daemon);

            Light light = new Light(Configs.getLightPosition(),
                    Configs.getLightColor(),
                    Configs.getLightBias());

            PerlinNoiseGenerator noise = new PerlinNoiseGenerator(Configs.getSEED(),
                    Configs.getOCTAVES(),
                    Configs.getAMPLITUDE(),
                    Configs.getROUGHNESS());

            ColorGenerator colourGen = new ColorGenerator(Configs.getTerrainColors(),
                    Configs.getColorSpread());
            TerrainGenerator terrainGenerator = new PolygonizedTerrainGenerator(noise, colourGen);

            Terrain terrain = terrainGenerator.generateTerrain(Configs.getWorldSize());

            WaterTile water = WaterGenerator.generate(Configs.getWorldSize(), Configs.getWaterHeight());

            while (!engine.getWINDOW().isCloseRequested()) {
                camera.move();
                daemon.move();
                engine.render(terrain, water, camera, light);

                InputParser.performInput();
            }

            water.delete();
            terrainGenerator.cleanUp();
            terrain.delete();

            engine.cleanUp();
        } catch (LWJGLException | IOException exception) {
            System.out.println("An error occurred: " + exception.getMessage());
        }
    }
}
