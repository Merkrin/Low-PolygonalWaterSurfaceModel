package ru.hse.utils;

import org.lwjgl.opengl.ContextAttribs;

/**
 * Class for window context representation.
 */
public class Context {
    private final int[] openGlVersion;

    /**
     * The class' constructor.
     *
     * @param version OpenGL version
     * @param subVersion OpenGL subversion
     */
    public Context(int version, int subVersion) {
        this.openGlVersion = new int[]{version, subVersion};
    }

    /**
     * Getter of OpenGL window context attributes.
     *
     * @return context attributes
     */
    public ContextAttribs getAttribs() {
        return new ContextAttribs(openGlVersion[0], openGlVersion[1])
                .withForwardCompatible(true)
                .withProfileCore(true);
    }
}
