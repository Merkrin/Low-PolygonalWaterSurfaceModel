package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL30;
import ru.hse.openGL.objects.Attachment;
import ru.hse.openGL.objects.Fbo;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for an FBO building.
 */
public class FboBuilder {
    private final int width;
    private final int height;
    private final int samples;

    private final Map<Integer, Attachment> colorAttachments = new HashMap<>();

    private Attachment depthAttachment;

    /**
     * The class' constructor.
     *
     * @param width   width of the FBO
     * @param height  height of the FBO
     * @param samples samples to use
     */
    public FboBuilder(int width, int height, int samples) {
        this.width = width;
        this.height = height;
        this.samples = samples;
    }

    /**
     * Method for an FBO initialization.
     *
     * @return initialized FBO
     */
    public Fbo initialize() {
        int fboId = createFbo();

        createColorAttachments();
        createDepthAttachment();

        return new Fbo(fboId,
                width, height,
                colorAttachments, depthAttachment);
    }

    /**
     * Method for FBO creation.
     *
     * @return new FBO id
     */
    private int createFbo() {
        int fboId = GL30.glGenFramebuffers();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

        return fboId;
    }

    /**
     * Method for color attachments creation.
     */
    private void createColorAttachments() {
        for (Map.Entry<Integer, Attachment> entry :
                colorAttachments.entrySet()) {
            Attachment attachment = entry.getValue();

            int attachmentId = GL30.GL_COLOR_ATTACHMENT0 + entry.getKey();

            attachment.init(attachmentId, width, height, samples);
        }
    }

    /**
     * Method for depth attachment creation.
     */
    private void createDepthAttachment() {
        if (depthAttachment != null)
            depthAttachment.init(GL30.GL_DEPTH_ATTACHMENT,
                    width, height,
                    samples);
    }

    /**
     * Method for color attachment adding.
     *
     * @param index      index of the attachment
     * @param attachment attachment to add
     * @return this
     */
    public FboBuilder addColorAttachment(int index, Attachment attachment) {
        colorAttachments.put(index, attachment);

        return this;
    }

    /**
     * Method for depth attachment adding.
     *
     * @param attachment attachment to add
     * @return this
     */
    public FboBuilder addDepthAttachment(Attachment attachment) {
        depthAttachment = attachment;

        attachment.setAsDepthAttachment();

        return this;
    }
}
