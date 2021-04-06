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

    private final Map<Integer, Attachment> COLOR_ATTACHMENTS;

    private final Attachment DEPTH_ATTACHMENT;

    /**
     * The class' constructor.
     *
     * @param fboId             ID of the FBO
     * @param width             width of the FBO
     * @param height            height of the FBO
     * @param COLOR_ATTACHMENTS list of color attachments
     * @param DEPTH_ATTACHMENT  depth attachment
     */
    public Fbo(int fboId,
               int width, int height,
               Map<Integer, Attachment> COLOR_ATTACHMENTS,
               Attachment DEPTH_ATTACHMENT) {
        FBO_ID = fboId;
        WIDTH = width;
        HEIGHT = height;

        this.COLOR_ATTACHMENTS = COLOR_ATTACHMENTS;
        this.DEPTH_ATTACHMENT = DEPTH_ATTACHMENT;
    }

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

    public int getColorBuffer(int colorAttachmentIndex) {
        return COLOR_ATTACHMENTS.get(colorAttachmentIndex).getBufferId();
    }

    public int getDepthBuffer() {
        return DEPTH_ATTACHMENT.getBufferId();
    }

    public void bindForRender(int colorIndex) {
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0 + colorIndex);

        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, FBO_ID);

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public void unbindAfterRender() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);

        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public void delete() {
        for (Attachment attachment : COLOR_ATTACHMENTS.values())
            attachment.delete();

        if (DEPTH_ATTACHMENT != null)
            DEPTH_ATTACHMENT.delete();
    }

    public static FboBuilder newFbo(int width, int height) {
        return new FboBuilder(width, height, 0);
    }
}
