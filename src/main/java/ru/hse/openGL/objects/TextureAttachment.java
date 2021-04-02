package ru.hse.openGL.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

/**
 * Class for attachments representation.
 */
public class TextureAttachment extends Attachment{
    private final int format;
    private final boolean nearestFiltering;
    private final boolean clampEdges;

    public TextureAttachment(int format){
        this.format = format;
        this.nearestFiltering = false;
        this.clampEdges = false;
    }

    public TextureAttachment(int format, boolean nearestFiltering, boolean clampEdges){
        this.format = format;
        this.nearestFiltering = nearestFiltering;
        this.clampEdges = clampEdges;
    }

    @Override
    public void delete() {
        GL11.glDeleteTextures(getBufferId());
    }

    @Override
    public void init(int attachmentType, int width, int height, int samplesAmount) {
        int texture = GL11.glGenTextures();
        super.setBufferId(texture);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        indicateStorageType(width, height);
        setTextureParams();
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachmentType, GL11.GL_TEXTURE_2D, texture, 0);
    }

    private void indicateStorageType(int width, int height){
        if(isDepthAttachment()){
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        }else{
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        }
    }

    private void setTextureParams(){
        int filterType = nearestFiltering ? GL11.GL_NEAREST : GL11.GL_LINEAR;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filterType);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filterType);
        int wrapType = clampEdges ? GL12.GL_CLAMP_TO_EDGE : GL11.GL_REPEAT;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapType);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapType);
    }
}
