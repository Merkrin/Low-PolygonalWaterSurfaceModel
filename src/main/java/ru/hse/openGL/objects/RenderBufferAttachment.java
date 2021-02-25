package ru.hse.openGL.objects;

import org.lwjgl.opengl.GL30;

public class RenderBufferAttachment extends Attachment {
    private final int format;

    public RenderBufferAttachment(int format) {
        this.format = format;
    }

    @Override
    public void init(int attachmentType, int width, int height, int samplesAmount) {
        int buffer = GL30.glGenRenderbuffers();
        super.setBufferId(buffer);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, buffer);
        GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, samplesAmount, format, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachmentType, GL30.GL_RENDERBUFFER, buffer);
    }

    @Override
    public void delete() {
        GL30.glDeleteRenderbuffers(super.getBufferId());
    }
}
