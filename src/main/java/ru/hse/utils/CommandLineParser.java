package ru.hse.utils;

import org.apache.commons.cli.*;

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
        addOption("tc", false, "terrain color spread");

        //Mandatory
        addOption("lp", true, "position of light");
        addOption("lc", true, "light color");
        addOption("lb", true, "light bias");

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
                Configs.FPS_CAP = Integer.parseInt(cmd.getOptionValue("fps"));

            Configs.SCREEN_WIDTH = Integer.parseInt(cmd.getOptionValue("w"));
            Configs.SCREEN_HEIGHT = Integer.parseInt(cmd.getOptionValue("h"));
            

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
}
