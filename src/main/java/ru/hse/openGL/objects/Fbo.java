package ru.hse.openGL.objects;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import ru.hse.openGL.utils.FboBuilder;
import ru.hse.openGL.utils.MultisampledFboBuilder;

import java.util.Map;

/**
 * Framebuffer object class.
 */
public class Fbo {
    private final int FBO_ID;

    private final int WIDTH;
    private final int HEIGHT;

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
        FBO_ID = fboId;
        WIDTH = width;
        HEIGHT = height;

        this.colorAttachments = colorAttachments;
        this.depthAttachment = depthAttachment;
    }

    /**
     * Method for blitting the FBO content to the screen.
     *
     * @param colorAttachmentIndex index of the color attachment
     */
    public void blitToScreen(int colorAttachmentIndex) {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);

        GL11.glDrawBuffer(GL11.GL_BACK);

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, FBO_ID);

        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0 + colorAttachmentIndex);

        GL30.glBlitFramebuffer(0, 0,
                WIDTH, HEIGHT,
                0, 0,
                Display.getWidth(), Display.getHeight(),
                GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /**
     * Method for blitting the FBO content to another FBO.
     *
     * @param sourceColorAttachmentIndex index of the source color attachment
     * @param targetFbo                  target FBO
     * @param targetColorAttachmentIndex index of the target color attachment
     */
    public void blitToFbo(int sourceColorAttachmentIndex,
                          Fbo targetFbo,
                          int targetColorAttachmentIndex) {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, targetFbo.FBO_ID);
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0 +
                targetColorAttachmentIndex);

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, FBO_ID);
        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0 +
                sourceColorAttachmentIndex);

        int bufferBit = ((depthAttachment != null) &&
                (targetFbo.depthAttachment != null) ?
                (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT) :
                GL11.GL_COLOR_BUFFER_BIT);

        GL30.glBlitFramebuffer(0, 0,
                WIDTH, HEIGHT,
                0, 0,
                targetFbo.WIDTH, targetFbo.HEIGHT,
                bufferBit, GL11.GL_NEAREST);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public int getColorBuffer(int colorAttachmentIndex) {
        return colorAttachments.get(colorAttachmentIndex).getBufferId();
    }

    public int getDepthBuffer() {
        return depthAttachment.getBufferId();
    }

    public boolean isComplete() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO_ID);
        boolean complete = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        return complete;
    }

    public void bindForRender(int colourIndex) {
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0 + colourIndex);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, FBO_ID);
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public void unbindAfterRender() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public void delete() {
        for (Attachment attachment : colorAttachments.values()) {
            attachment.delete();
        }
        if (depthAttachment != null) {
            depthAttachment.delete();
        }
    }

    public static FboBuilder newFbo(int width, int height) {
        return new FboBuilder(width, height, 0);
    }

    public static MultisampledFboBuilder newMultisampledFbo(int width, int height, int samples) {
        return new MultisampledFboBuilder(new FboBuilder(width, height, samples));
    }
}
