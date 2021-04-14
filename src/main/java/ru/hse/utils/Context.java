package ru.hse.utils;

import org.lwjgl.opengl.ContextAttribs;

/**
 * Class for window context representation.
 */
class Context {
    private final int[] openGlVersion;

    /**
     * The class' constructor.
     *
     */
    Context() {
        this.openGlVersion = new int[]{3, 3};
    }

    /**
     * Getter of OpenGL window context attributes.
     *
     * @return context attributes
     */
    ContextAttribs getAttribs() {
        return new ContextAttribs(openGlVersion[0], openGlVersion[1])
                .withForwardCompatible(true)
                .withProfileCore(true);
    }
}
