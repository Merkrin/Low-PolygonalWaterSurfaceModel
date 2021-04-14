package ru.hse.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.engine.exceptions.CommandLineArgumentsException;
import ru.hse.engine.exceptions.InvalidSettingException;
import ru.hse.engine.exceptions.SettingsFileException;
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

class Main {
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
            } catch (CommandLineArgumentsException | InvalidSettingException | NullPointerException e) {
                System.out.println("An error in command line format found: " +
                        e.getMessage());
                System.out.println("Starting with standard settings...");
            } catch (IOException | SettingsFileException e) {
                System.out.println("An error while reading file occurred: " +
                        e.getMessage());
                System.out.println("Starting with standard settings...");
            } catch (Exception e) {
                System.out.println("An unhandled exception occurred: " + e.getMessage());
                System.out.println("Starting with standard settings...");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Low-polygonal water surface model program!");
        System.out.println("You can press <H> to get help anytime.");

        try {
            readArguments(args);
        }catch(Exception exception){
            System.out.println("An unhandled exception occurred: " + exception.getMessage());
            System.out.println("Starting with standard settings...");
        }

        try {
            RenderEngine engine = new RenderEngine(Configs.getFpsCap(),
                    Configs.getScreenWidth(), Configs.getScreenHeight());
            Daemon daemon = new Daemon(engine.getWindow(), new Vector3f(200, 50, 200)
            );
            Camera camera = new Camera(daemon);

            Light light = new Light(Configs.getLightDirection(),
                    Configs.getLightColor(),
                    Configs.getLightBias());

            PerlinNoiseGenerator noise = new PerlinNoiseGenerator(Configs.getOctaves(),
                    Configs.getAmplitude(),
                    Configs.getRoughness());

            ColorGenerator colorGenerator = new ColorGenerator(Configs.getTerrainColors(),
                    Configs.getColorSpread());
            TerrainGenerator terrainGenerator = new PolygonizedTerrainGenerator(noise, colorGenerator);

            Terrain terrain = terrainGenerator.generateTerrain(Configs.getWorldSize());

            WaterTile water = WaterGenerator.generate(Configs.getWorldSize(), Configs.getWaterHeight());

            while (!engine.getWindow().isCloseRequested()) {
                camera.move();
                daemon.move();
                engine.render(terrain, water, camera, light);
            }

            water.delete();
            terrainGenerator.cleanUp();
            terrain.delete();

            engine.cleanUp();
        } catch (LWJGLException exception) {
            System.out.println("An error occurred: " + exception.getMessage());
        }
    }
}
