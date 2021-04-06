package ru.hse.openGL.objects;

import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Class for vertex buffer object representation.
 */
public class Vbo {
    private final int vboId;
    private final int type;
    private final int usage;

    /**
     * The class' constructor.
     *
     * @param vboId id of the VBO
     * @param type  type of the VBO
     * @param usage usage type of the VBO
     */
    private Vbo(int vboId, int type, int usage) {
        this.vboId = vboId;
        this.type = type;
        this.usage = usage;

        bind();
    }

    /**
     * Method for a VBO creation.
     *
     * @param type  type of the VBO
     * @param usage usage type of the VBO
     * @return new VBO
     */
    public static Vbo create(int type, int usage) {
        int id = GL15.glGenBuffers();

        return new Vbo(id, type, usage);
    }

    /**
     * Method for the VBO binding.
     */
    public void bind() {
        GL15.glBindBuffer(type, vboId);
    }

    /**
     * Method for data storage allocating.
     *
     * @param sizeInBytes size in bytes used to store data
     */
    public void allocateData(long sizeInBytes) {
        GL15.glBufferData(type, sizeInBytes, usage);
    }

    /**
     * Method for data storing.
     *
     * @param startInBytes index of data start in buffer
     * @param data         data to store
     */
    public void storeData(long startInBytes, IntBuffer data) {
        GL15.glBufferSubData(type, startInBytes, data);
    }

    /**
     * Method for data storing.
     *
     * @param startInBytes index of data start in buffer
     * @param data         data to store
     */
    public void storeData(long startInBytes, ByteBuffer data) {
        GL15.glBufferSubData(type, startInBytes, data);
    }

    /**
     * Method for the VBO unbinding.
     */
    public void unbind() {
        GL15.glBindBuffer(type, 0);
    }

    /**
     * "Destructor" of the VBO.
     */
    public void delete() {
        GL15.glDeleteBuffers(vboId);
    }
}
