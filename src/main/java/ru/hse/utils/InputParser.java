package ru.hse.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// TODO: https://www.youtube.com/watch?v=WQLPZKpnod8&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=48 about multiple rendering
public class InputParser {
    private static boolean isKeyDown = false;

    public static void performInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_P) && !isKeyDown) {
            isKeyDown = true;

            takeScreenshot();
        }else if(Keyboard.isKeyDown(Keyboard.KEY_H) && !isKeyDown){
            //Configs.saveConfigs();
        }

        if(!Keyboard.getEventKeyState())
            isKeyDown = false;
    }

    private static void takeScreenshot() {
        GL11.glReadBuffer(GL11.GL_FRONT);
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();
        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        File file = new File(dtf.format(now) + ".png");
        String format = "PNG";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int i = y * width * bpp + x * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, y, (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
