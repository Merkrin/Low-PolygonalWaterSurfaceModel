package ru.hse.utils;

import org.apache.commons.cli.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Class for command line parsing. There are several types of arguments to read:
 * -fps FPS cap
 * -w window width
 * -h window height
 * ================
 * -WS world size
 * -S world generation seed
 * -A world generation amplitude
 * -R world generation roughness
 * -O world generation octaves
 * -cs terrain color spread
 * -tc array of terrain colors
 * ================
 * -lp position of light
 * -lc light color
 * -lb - light bias
 * ================
 * -wh water height
 * -ws wave speed
 * -wl wave length
 * -wa wave amplitude
 */
public class CommandLineParser {
    private final static Options options = new Options();
    private final static DefaultParser parser = new DefaultParser();
    private final static HelpFormatter formatter = new HelpFormatter();

    public static void initialize() {
        // Optional argument for FPS cap
        options.addOption("fps", false, "FPS cap");

        //Mandatory arguments for window width and height.
        options.addOption("w", true, "window width");
        options.addOption("h", true, "window height");

        //Mandatory
        options.addOption("WS", true, "world size");

        // Optional
        options.addOption("S", false, "world generation seed");
        options.addOption("A", false, "world generation amplitude");
        options.addOption("R", false, "world generation roughness");
        options.addOption("O", false, "world generation octaves");
        options.addOption("cs", false, "terrain color spread");

        // Optional
        addOption("tc", false, "terrain colors");

        addOption("lp", false, "position of light");
        addOption("lc", false, "light color");
        addOption("lb", false, "light bias");

        // Optional
        options.addOption("wh", false, "water height");
        options.addOption("ws", false, "wave speed");
        options.addOption("wl", false, "wave length");
        options.addOption("wa", false, "wave amplitude");
    }

    public static void readArguments(String[] args) {
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("fps"))
                Configs.setFpsCap(Integer.parseInt(cmd.getOptionValue("fps")));

            Configs.setScreenWidth(Integer.parseInt(cmd.getOptionValue("w")));
            Configs.setScreenHeight(Integer.parseInt(cmd.getOptionValue("h")));

            Configs.setWorldSize(Integer.parseInt(cmd.getOptionValue("WS")));

            if (cmd.hasOption("S"))
                Configs.setSEED(Integer.parseInt(cmd.getOptionValue("S")));
            if (cmd.hasOption("A"))
                Configs.setAMPLITUDE(Float.parseFloat(cmd.getOptionValue("A")));
            if (cmd.hasOption("R"))
                Configs.setROUGHNESS(Float.parseFloat(cmd.getOptionValue("R")));
            if (cmd.hasOption("O"))
                Configs.setOCTAVES(Integer.parseInt(cmd.getOptionValue("O")));
            if (cmd.hasOption("cs"))
                Configs.setColorSpread(Float.parseFloat(cmd.getOptionValue("cs")));

            if (cmd.hasOption("tc"))
                Configs.setTerrainColors(createTerrainColors(cmd));

            if (cmd.hasOption("lp"))
                Configs.setLightPosition(createVector3f(cmd, "lp"));
            if (cmd.hasOption("lc"))
                Configs.setLightColor(new Color(createVector3f(cmd, "lc")));
            if (cmd.hasOption("lb"))
                Configs.setLightBias(createVector2f(cmd));

            if (cmd.hasOption("wh"))
                Configs.setWaterHeight(Integer.parseInt(cmd.getOptionValue("wh")));
            if (cmd.hasOption("ws"))
                Configs.setWaveSpeed(Float.parseFloat(cmd.getOptionValue("ws")));
            if (cmd.hasOption("wl"))
                Configs.setWaveLength(Float.parseFloat(cmd.getOptionValue("wl")));
            if (cmd.hasOption("wa"))
                Configs.setWaveAmplitude(Float.parseFloat(cmd.getOptionValue("wa")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }

    private static void addOption(String optionValue, boolean isMandatory, String description) {
        Option option = new Option(optionValue, isMandatory, description);
        option.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(option);
    }

    public static Color[] createTerrainColors(CommandLine cmd) {
        String[] colorStrings = cmd.getOptionValue("tc").split(";");

        Color[] colors = null;

        if (colorStrings.length % 3 == 0) {
            colors = new Color[colorStrings.length / 3];
            int index = 0;

            for (int i = 0; i <= colorStrings.length - 3; i += 3) {
                colors[index] = new Color(Float.parseFloat(colorStrings[i]),
                        Float.parseFloat(colorStrings[i + 1]),
                        Float.parseFloat(colorStrings[i + 2]));

                index++;
            }
        }

        return colors;
    }

    public static Vector3f createVector3f(CommandLine cmd, String option) {
        String[] positionStrings = cmd.getOptionValue(option).split(";");

        return new Vector3f(Float.parseFloat(positionStrings[0]),
                Float.parseFloat(positionStrings[1]),
                Float.parseFloat(positionStrings[2]));
    }

    private static Vector2f createVector2f(CommandLine cmd) {
        String[] biasStrings = cmd.getOptionValue("lb").split(";");

        return new Vector2f(Float.parseFloat(biasStrings[0]),
                Float.parseFloat(biasStrings[1]));
    }
}
