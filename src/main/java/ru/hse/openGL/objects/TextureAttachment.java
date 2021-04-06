package ru.hse.openGL.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

/**
 * Class for texture attachments representation.
 */
public class TextureAttachment extends Attachment {
    private final int format;

    private final boolean clampsEdges;
    private final boolean usesNearestFiltering;

    /**
     * The class' constructor.
     *
     * @param format format of the attachment
     */
    public TextureAttachment(int format) {
        this.format = format;

        this.usesNearestFiltering = false;
        this.clampsEdges = false;
    }

    @Override
    public void init(int attachmentType,
                     int width, int height,
                     int samplesAmount) {
        int texture = GL11.glGenTextures();

        super.setBufferId(texture);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        indicateStorageType(width, height);
        setTextureParameters();

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachmentType,
                GL11.GL_TEXTURE_2D, texture, 0);
    }

    /**
     * Method for storage type indication.
     *
     * @param width  width of the attachment
     * @param height height of the attachment
     */
    private void indicateStorageType(int width, int height) {
        if (isDepthAttachment())
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format,
                    width, height, 0,
                    GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT,
                    (ByteBuffer) null);
        else
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format,
                    width, height, 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                    (ByteBuffer) null);
    }

    /**
     * Method for texture parameters setting.
     */
    private void setTextureParameters() {
        int filterType = usesNearestFiltering ? GL11.GL_NEAREST : GL11.GL_LINEAR;

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MAG_FILTER, filterType);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MIN_FILTER, filterType);

        int wrapType = clampsEdges ? GL12.GL_CLAMP_TO_EDGE : GL11.GL_REPEAT;

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_WRAP_S, wrapType);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_WRAP_T, wrapType);
    }

    @Override
    public void delete() {
        GL11.glDeleteTextures(getBufferId());
    }
}
