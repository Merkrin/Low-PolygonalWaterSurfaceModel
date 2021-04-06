package ru.hse.openGL.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

/**
 * Class of an attribute representation.
 */
public class Attribute {
    protected final int attributeNumber;
    protected final int dataType;
    protected final int componentsAmount;
    protected final int bytesPerVertex;

    protected final boolean isNormalized;

    /**
     * The class' constructor.
     *
     * @param attributeNumber  number of the attribute
     * @param dataType         type of used data
     * @param componentsAmount amount of components
     */
    public Attribute(int attributeNumber, int dataType, int componentsAmount) {
        this.attributeNumber = attributeNumber;
        this.dataType = dataType;
        this.componentsAmount = componentsAmount;

        bytesPerVertex = calculateBytesPerVertex();

        isNormalized = false;
    }

    /**
     * The class' constructor.
     *
     * @param attributeNumber  number of the attribute
     * @param dataType         type of used data
     * @param componentsAmount amount of components
     * @param isNormalized     boolean value if the used VBO is normalized
     */
    public Attribute(int attributeNumber, int dataType, int componentsAmount,
                     boolean isNormalized) {
        this.attributeNumber = attributeNumber;
        this.dataType = dataType;
        this.componentsAmount = componentsAmount;

        bytesPerVertex = calculateBytesPerVertex();

        this.isNormalized = isNormalized;
    }

    /**
     * Method for attributes array enabling.
     *
     * @param enable boolean value if the array has to be enabled or disabled
     */
    protected void enable(boolean enable) {
        if (enable)
            GL20.glEnableVertexAttribArray(attributeNumber);
        else
            GL20.glDisableVertexAttribArray(attributeNumber);
    }

    /**
     * Method for the attribute linking.
     *
     * @param offset point of start
     * @param stride length
     */
    protected void link(int offset, int stride) {
        GL20.glVertexAttribPointer(attributeNumber,
                componentsAmount,
                dataType,
                isNormalized,
                stride, offset);
    }

    /**
     * Method for bytes amount calculation to use it for every vertex.
     *
     * @return amount of bytes to use
     */
    private int calculateBytesPerVertex() {
        if (dataType == GL11.GL_FLOAT ||
                dataType == GL11.GL_UNSIGNED_INT ||
                dataType == GL11.GL_INT)
            return 4 * componentsAmount;

        if (dataType == GL11.GL_SHORT ||
                dataType == GL11.GL_UNSIGNED_SHORT)
            return 2 * componentsAmount;

        if (dataType == GL11.GL_BYTE ||
                dataType == GL11.GL_UNSIGNED_BYTE)
            return componentsAmount;

        if (dataType == GL12.GL_UNSIGNED_INT_2_10_10_10_REV)
            return 4;

        System.err.println("Unsupported data type for VAO attribute: " + dataType);

        return 0;
    }
}
