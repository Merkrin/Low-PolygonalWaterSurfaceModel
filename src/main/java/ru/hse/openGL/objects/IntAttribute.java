package ru.hse.openGL.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class IntAttribute extends Attribute {
    public IntAttribute(int attrNumber, int dataType, int componentCount) {
        super(attrNumber, dataType, componentCount);
    }

    @Override
    protected void link(int offset, int stride) {
        GL30.glVertexAttribIPointer(ATTRIBUTE_NUMBER, COMPONENTS_AMOUNT, GL11.GL_INT, stride, offset);
    }
}
