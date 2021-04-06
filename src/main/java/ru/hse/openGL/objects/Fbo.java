package ru.hse.openGL.objects;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import ru.hse.openGL.utils.FboBuilder;

import java.util.Map;

/**
 * Framebuffer object class.
 */
public class Fbo {
    private final int fboId;

    private final int width;
    private final int height;

    private final Map<Integer, Attachment> colorAttachments;

    private final Attachment depthAttachment;

    /**
     * The class' constructor.
     *
     * @param fboId            ID of the FBO
     * @param width            width of the FBO
     * @param height           height of the FBO
     * @param colorAttachments list of color attachments
     * @param depthAttachment  depth attachment
     */
    public Fbo(int fboId,
               int width, int height,
               Map<Integer, Attachment> colorAttachments,
               Attachment depthAttachment) {
        this.fboId = fboId;
        this.width = width;
        this.height = height;

        this.colorAttachments = colorAttachments;
        this.depthAttachment = depthAttachment;
    }

    /**
     * Method for rendering binding of the FBO.
     *
     * @param colorAttachmentIndex index of the color attachment used
     */
    public void bindForRender(int colorAttachmentIndex) {
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0 + colorAttachmentIndex);

        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fboId);

        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Method for FBO blitting to screen.
     *
     * @param colorAttachmentIndex index of the color attachment used
     */
    public void blitToScreen(int colorAttachmentIndex) {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);

        GL11.glDrawBuffer(GL11.GL_BACK);

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fboId);

        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0 + colorAttachmentIndex);

        GL30.glBlitFramebuffer(0, 0,
                width, height,
                0, 0,
                Display.getWidth(), Display.getHeight(),
                GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /**
     * Method for the FBO unbinding.
     */
    public void unbindAfterRender() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);

        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    /**
     * Method for the FBO deletion.
     */
    public void delete() {
        for (Attachment attachment : colorAttachments.values())
            attachment.delete();

        if (depthAttachment != null)
            depthAttachment.delete();
    }

    /**
     * Color buffer getter.
     *
     * @param colorAttachmentIndex index of the color attachment used
     * @return id of the color buffer
     */
    public int getColorBuffer(int colorAttachmentIndex) {
        return colorAttachments.get(colorAttachmentIndex).getBufferId();
    }

    /**
     * Depth buffer getter.
     *
     * @return id of the depth buffer
     */
    public int getDepthBuffer() {
        return depthAttachment.getBufferId();
    }

    /**
     * Method for a new FBO creation.
     *
     * @param width  width of the FBO
     * @param height height of the FBO
     * @return new FBO with given settings
     */
    public static FboBuilder newFbo(int width, int height) {
        return new FboBuilder(width, height, 0);
    }
}
