package ru.hse.openGL.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

/**
 * Class of an attribute representation.
 */
public class Attribute {
    protected final int ATTRIBUTE_NUMBER;
    protected final int DATA_TYPE;
    protected final int COMPONENTS_AMOUNT;
    protected final int BYTES_PER_VERTEX;

    protected final boolean IS_NORMALIZED;

    /**
     * The class' constructor.
     *
     * @param attributeNumber  number of the attribute
     * @param dataType         type of used data
     * @param componentsAmount amount of components
     */
    public Attribute(int attributeNumber, int dataType, int componentsAmount) {
        ATTRIBUTE_NUMBER = attributeNumber;
        DATA_TYPE = dataType;
        COMPONENTS_AMOUNT = componentsAmount;
        IS_NORMALIZED = false;
        BYTES_PER_VERTEX = calculateBytesPerVertexAmount();
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
        ATTRIBUTE_NUMBER = attributeNumber;
        DATA_TYPE = dataType;
        COMPONENTS_AMOUNT = componentsAmount;
        IS_NORMALIZED = isNormalized;
        BYTES_PER_VERTEX = calculateBytesPerVertexAmount();
    }

    /**
     * Method for attributes array enabling.
     *
     * @param enable boolean value if the array has to be enabled or disabled
     */
    protected void enable(boolean enable) {
        if (enable)
            GL20.glEnableVertexAttribArray(ATTRIBUTE_NUMBER);
        else
            GL20.glDisableVertexAttribArray(ATTRIBUTE_NUMBER);
    }

    /**
     * Method for the attribute linking.
     *
     * @param offset point of start
     * @param stride length
     */
    protected void link(int offset, int stride) {
        GL20.glVertexAttribPointer(ATTRIBUTE_NUMBER,
                COMPONENTS_AMOUNT,
                DATA_TYPE,
                IS_NORMALIZED,
                stride, offset);
    }

    /**
     * Method for bytes amount calculation to use it for every vertex.
     *
     * @return amount of bytes to use
     */
    private int calculateBytesPerVertexAmount() {
        if (DATA_TYPE == GL11.GL_FLOAT ||
                DATA_TYPE == GL11.GL_UNSIGNED_INT ||
                DATA_TYPE == GL11.GL_INT)
            return 4 * COMPONENTS_AMOUNT;

        if (DATA_TYPE == GL11.GL_SHORT ||
                DATA_TYPE == GL11.GL_UNSIGNED_SHORT)
            return 2 * COMPONENTS_AMOUNT;

        if (DATA_TYPE == GL11.GL_BYTE ||
                DATA_TYPE == GL11.GL_UNSIGNED_BYTE)
            return COMPONENTS_AMOUNT;

        if (DATA_TYPE == GL12.GL_UNSIGNED_INT_2_10_10_10_REV)
            return 4;

        System.err.println("Unsupported data type for VAO attribute: " + DATA_TYPE);

        return 0;
    }
}
