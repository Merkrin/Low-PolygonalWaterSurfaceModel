package ru.hse.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import ru.hse.openGL.utils.GraphicsUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InputParser {
    private static boolean isKeyDown = false;

    public static void performInput() throws IOException {
        while (Keyboard.next()) {
            if (checkKey((Keyboard.KEY_0))) {
                isKeyDown = true;

                Configs.invertShowWater();

                System.out.println("Water surface showing set to: " + Configs.getShowWater());
            } else if (checkKey((Keyboard.KEY_MINUS))) {
                isKeyDown = true;

                Configs.invertAnimateWater();

                System.out.println("Water animation set to: " + Configs.getAnimateWater());
            } else if (checkKey(Keyboard.KEY_P)) {
                isKeyDown = true;

                takeScreenshot();
            } else if (checkKey(Keyboard.KEY_P)) {
                isKeyDown = true;

                saveConfigurations();
            } else if (checkKey(Keyboard.KEY_H)) {
                printHelp();
            } else if (checkKey(Keyboard.KEY_1)) {
                try {
                    Configs.setWaveAmplitude(Configs.getWaveAmplitude() + 0.1f);

                    System.out.println("Wave amplitude changed to: " + Configs.getWaveAmplitude());
                } catch (Exception e) {
                }
            } else if (checkKey(Keyboard.KEY_2)) {
                try {
                    Configs.setWaveAmplitude(Configs.getWaveAmplitude() - 0.1f);

                    System.out.println("Wave amplitude changed to: " + Configs.getWaveAmplitude());
                } catch (Exception e) {
                }
            } else if (checkKey(Keyboard.KEY_3)) {
                try {
                    Configs.setWaveLength(Configs.getWaveLength() + 1);

                    System.out.println("Wave length changed to: " + Configs.getWaveLength());
                } catch (Exception e) {
                }
            } else if (checkKey(Keyboard.KEY_4)) {
                try {
                    Configs.setWaveLength(Configs.getWaveLength() - 1);

                    System.out.println("Wave length changed to: " + Configs.getWaveLength());
                } catch (Exception e) {
                }
            } else if (checkKey(Keyboard.KEY_5)) {
                try {
                    Configs.setWaveSpeed(Configs.getWaveSpeed() + 0.001f);

                    System.out.println("Wave speed changed to: " + Configs.getWaveSpeed());
                } catch (Exception e) {
                }
            } else if (checkKey(Keyboard.KEY_6)) {
                try {
                    Configs.setWaveSpeed(Configs.getWaveSpeed() - 0.001f);

                    System.out.println("Wave speed changed to: " + Configs.getWaveSpeed());
                } catch (Exception e) {
                }
            }

            if (!Keyboard.getEventKeyState())
                isKeyDown = false;
        }
    }

    private static void saveConfigurations() throws IOException {
        String cli = CommandLineUtils.createCommandLine();

        File file = getFileToSave(".lpw");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(cli);

        writer.close();
    }

    private static void printHelp() {
        System.out.println("Use yor keyboard keys <W>, <S>, <A>, <D>, <X>" +
                " and <SPACE> to move and right mouse button to rotate camera.");
        System.out.println("Use <1> and <2> to change wave amplitude " +
                "(+0.1 and -0.1 respectively).");
        System.out.println("Use <3> and <4> to change wave length " +
                "(+1 and -1 respectively).");
        System.out.println("Use <5> and <6> to change wave speed " +
                "(+0.1 and -0.1 respectively).");
        System.out.println("Use <P> to save your settings in a file.");
    }

    private static void takeScreenshot() {
        GL11.glReadBuffer(GL11.GL_FRONT);
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();
        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        File file = getFileToSave(".png");
        String format = "PNG";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int i = y * width * bpp + x * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - y - 1, (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getFileToSave(String fileFormat) {
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        return new File(dtf.format(now) + fileFormat);
    }

    private static boolean checkKey(int key) {
        return Keyboard.isKeyDown(key) && !isKeyDown;
    }
}
