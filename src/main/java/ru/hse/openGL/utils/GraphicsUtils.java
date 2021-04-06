package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Class with graphics utility methods.
 */
public class GraphicsUtils {
    private static boolean cullingBackFace = false;
    private static boolean inWireframe = false;
    private static boolean usesAlphaBlending = false;
    private static boolean usesAntialiasing = false;
    private static boolean usesDepthTesting = false;

    /**
     * Method for antialiasing setting.
     *
     * @param enable flag if the setting has to be enabled or not
     */
    public static void setAntialiasing(boolean enable) {
        if (enable && !usesAntialiasing) {
            GL11.glEnable(GL13.GL_MULTISAMPLE);

            usesAntialiasing = true;
        } else if (!enable && usesAntialiasing) {
            GL11.glDisable(GL13.GL_MULTISAMPLE);

            usesAntialiasing = false;
        }
    }

    /**
     * Method for alpha blending enabling.
     */
    public static void enableAlphaBlending() {
        if (!usesAlphaBlending) {
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            usesAlphaBlending = true;
        }
    }

    /**
     * Method for alpha blending disabling.
     */
    public static void disableAlphaBlending() {
        if (usesAlphaBlending) {
            GL11.glDisable(GL11.GL_BLEND);

            usesAlphaBlending = false;
        }
    }

    /**
     * Method for depth testing setting.
     *
     * @param enable flag if the setting has to be enabled or not
     */
    public static void enableDepthTesting(boolean enable) {
        if (enable && !usesDepthTesting) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            usesDepthTesting = true;
        } else if (!enable && usesDepthTesting) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            usesDepthTesting = false;
        }
    }

    /**
     * Method for back faces culling setting.
     *
     * @param enable flag if the setting has to be enabled or not
     */
    public static void cullBackFaces(boolean enable) {
        if (enable && !cullingBackFace) {
            GL11.glEnable(GL11.GL_CULL_FACE);

            GL11.glCullFace(GL11.GL_BACK);

            cullingBackFace = true;
        } else if (!enable && cullingBackFace) {
            GL11.glDisable(GL11.GL_CULL_FACE);

            cullingBackFace = false;
        }
    }

    /**
     * Method for wireframe setting.
     *
     * @param enable flag if the setting has to be enabled or not
     */
    public static void setWireframe(boolean enable) {
        if (enable && !inWireframe) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

            inWireframe = true;
        } else if (!enable && inWireframe) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

            inWireframe = false;
        }
    }
}
