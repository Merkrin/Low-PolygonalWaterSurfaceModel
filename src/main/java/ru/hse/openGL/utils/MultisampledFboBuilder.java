package ru.hse.openGL.utils;

import ru.hse.openGL.objects.Fbo;
import ru.hse.openGL.objects.RenderBufferAttachment;

public class MultisampledFboBuilder {
    private final FboBuilder fboBuilder;

    public MultisampledFboBuilder(FboBuilder fboBuilder){
        this.fboBuilder = fboBuilder;
    }

    public MultisampledFboBuilder addColourAttachment(int index, RenderBufferAttachment attachment){
        fboBuilder.addColourAttachment(index, attachment);
        return this;
    }

    public MultisampledFboBuilder addDepthAttachment(RenderBufferAttachment attachment){
        fboBuilder.addDepthAttachment(attachment);
        return this;
    }

    public Fbo init(){
        return fboBuilder.init();
    }
}
