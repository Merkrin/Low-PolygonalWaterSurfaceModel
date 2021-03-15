package ru.hse.utils;

import org.apache.commons.cli.CommandLine;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import ru.hse.engine.exceptions.CommandLineArgumentsException;
import ru.hse.engine.exceptions.InvalidSettingExeption;

import java.util.HashMap;

public class CommandLineUtils {
    private static int MINIMAL_ARGS_AMOUNT = 3;
    private static int MAXIMAL_ARGS_AMOUNT = 17;

    private static HashMap<String, String> settings = new HashMap<>();

    private static String[] flags = {"-fps", "-w", "-h", "-WS", "-S", "-A",
            "-R", "-O", "-cs", "-tc", "-lp", "-lc", "-lb", "-wh", "-ws",
            "-wl", "-wa"};

    public static void readArguments(String[] args)
            throws CommandLineArgumentsException, InvalidSettingExeption {
        double argsAmount = args.length / 2.0;

        checkLength(argsAmount);

        for (int i = 0; i < argsAmount * 2; i += 2) {
            if (isValidFlag(args[i]))
                settings.put(args[i], args[i + 1]);
            else
                throw new CommandLineArgumentsException("Invalid argument found.");
        }

        if (settings.containsKey("-fps"))
            Configs.setFpsCap(Integer.parseInt(settings.get("-fps")));

        Configs.setScreenWidth(Integer.parseInt(settings.get("-w")));
        Configs.setScreenHeight(Integer.parseInt(settings.get("-h")));
        Configs.setWorldSize(Integer.parseInt(settings.get("-WS")));

        if (settings.containsKey("-S"))
            Configs.setSEED(Integer.parseInt(settings.get("-S")));
        if (settings.containsKey("-A"))
            Configs.setAMPLITUDE(Float.parseFloat(settings.get("-A")));
        if (settings.containsKey("-R"))
            Configs.setROUGHNESS(Float.parseFloat(settings.get("-R")));
        if (settings.containsKey("-O"))
            Configs.setOCTAVES(Integer.parseInt(settings.get("-O")));
        if (settings.containsKey("-cs"))
            Configs.setColorSpread(Float.parseFloat(settings.get("-cs")));

        if (settings.containsKey("-tc"))
            Configs.setTerrainColors(createTerrainColors(settings.get("-tc")));

        if (settings.containsKey("-lp"))
            Configs.setLightPosition(createVector3f(settings.get("-lp")));
        if (settings.containsKey("-lc"))
            Configs.setLightColor(new Color(createVector3f(settings.get("-lc"))));
        if (settings.containsKey("-lb"))
            Configs.setLightBias(createVector2f(settings.get("-lb")));

        if (settings.containsKey("-wh"))
            Configs.setWaterHeight(Integer.parseInt(settings.get("-wh")));
        if (settings.containsKey("-ws"))
            Configs.setWaveSpeed(Float.parseFloat(settings.get("-ws")));
        if (settings.containsKey("-wl"))
            Configs.setWaveLength(Float.parseFloat(settings.get("-wl")));
        if (settings.containsKey("-wa"))
            Configs.setWaveAmplitude(Float.parseFloat(settings.get("-wa")));
    }

    private static void checkLength(double argsAmount) throws CommandLineArgumentsException {
        if (argsAmount < MINIMAL_ARGS_AMOUNT ||
                argsAmount > MAXIMAL_ARGS_AMOUNT ||
                argsAmount != (int) argsAmount)
            throw new CommandLineArgumentsException("Invalid arguments amount.");
    }

    private static boolean isValidFlag(String flag) {
        for (String validFlag : flags)
            if (validFlag.equals(flag))
                return true;

        return false;
    }

    private static Color[] createTerrainColors(String cmd) {
        String[] colorStrings = cmd.split(";");

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

    private static Vector3f createVector3f(String cmd) {
        String[] positionStrings = cmd.split(";");

        return new Vector3f(Float.parseFloat(positionStrings[0]),
                Float.parseFloat(positionStrings[1]),
                Float.parseFloat(positionStrings[2]));
    }

    private static Vector2f createVector2f(String cmd) {
        String[] positionStrings = cmd.split(";");

        return new Vector2f(Float.parseFloat(positionStrings[0]),
                Float.parseFloat(positionStrings[1]));
    }
}
