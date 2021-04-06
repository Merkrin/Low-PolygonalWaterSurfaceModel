package ru.hse.openGL.objects;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import ru.hse.utils.DataUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for vertex array object representation.
 */
public class Vao {
    private final List<Attribute> attributes = new ArrayList<>();

    private final List<Vbo> relatedVbos = new ArrayList<>();

    private Vbo indexBuffer;

    public final int id;

    /**
     * Method for a VAO creation.
     *
     * @return new VAO
     */
    public static Vao create() {
        int id = GL30.glGenVertexArrays();

        return new Vao(id);
    }

    /**
     * The class' constructor.
     *
     * @param id id of the VAO
     */
    private Vao(int id) {
        this.id = id;
    }

    /**
     * Method for the VAO binding.
     */
    public void bind() {
        GL30.glBindVertexArray(id);
    }

    /**
     * Method for attributes linking.
     *
     * @param bytesPerVertex amount of bytes used per each vertex
     * @param attributes     new attributes to bind
     */
    private void linkAttributes(int bytesPerVertex, Attribute... attributes) {
        int offset = 0;

        for (Attribute attribute : attributes) {
            attribute.link(offset, bytesPerVertex);

            offset += attribute.bytesPerVertex;

            attribute.enable(true);

            this.attributes.add(attribute);
        }
    }

    /**
     * Getter of the total amount of bytes used for the VAO.
     *
     * @param attributes attributes of the VAO
     * @return total amount of bytes used
     */
    private int getVertexDataTotalBytes(Attribute... attributes) {
        int amountOfBytes = 0;

        for (Attribute attribute : attributes)
            amountOfBytes += attribute.bytesPerVertex;

        return amountOfBytes;
    }

    /**
     * Method for data storage initialization.
     *
     * @param data       data to store
     * @param usage      type of usage
     * @param attributes attributes of the VAO
     */
    public void initializeData(ByteBuffer data, int usage, Attribute... attributes) {
        int bytesPerVertex = getVertexDataTotalBytes(attributes);

        Vbo vbo = Vbo.create(GL15.GL_ARRAY_BUFFER, usage);

        relatedVbos.add(vbo);

        vbo.allocateData(data.limit());
        vbo.storeData(0, data);

        linkAttributes(bytesPerVertex, attributes);

        vbo.unbind();
    }

    /**
     * Method for index buffer creation.
     *
     * @param indices indices to use
     */
    public void createIndexBuffer(IntBuffer indices) {
        this.indexBuffer = Vbo.create(GL15.GL_ELEMENT_ARRAY_BUFFER,
                GL15.GL_STATIC_DRAW);

        indexBuffer.allocateData((long) indices.limit() *
                DataUtils.BYTES_IN_INT);
        indexBuffer.storeData(0, indices);
    }

    /**
     * Method for VAO unbinding.
     */
    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    /**
     * "Destructor" of the VAO.
     *
     * @param deleteVbos flag if related VBOs have to be deleted too
     */
    public void delete(boolean deleteVbos) {
        GL30.glDeleteVertexArrays(id);

        if (deleteVbos)
            for (Vbo vbo : relatedVbos)
                vbo.delete();
    }
}
