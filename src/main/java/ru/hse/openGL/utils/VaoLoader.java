package ru.hse.openGL.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import ru.hse.openGL.objects.Attribute;
import ru.hse.openGL.objects.Vao;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Class for VAO loading.
 */
public class VaoLoader {
    /**
     * Method for VAO creation.
     *
     * @param meshData data of the mesh to store
     * @param indices  indices to store
     * @return new VAO
     */
    public static Vao createVao(byte[] meshData, int[] indices) {
        Vao vao = Vao.create();

        vao.bind();

        storeVertexDataInVao(vao, meshData);

        if (indices != null)
            storeIndicesInVao(vao, indices);

        vao.unbind();

        return vao;
    }

    /**
     * Method for water VAO creation.
     *
     * @param meshData data of the mesh to store
     * @return new VAO
     */
    public static Vao createWaterVao(byte[] meshData) {
        Vao vao = Vao.create();

        vao.bind();

        ByteBuffer buffer = storeMeshDataInBuffer(meshData);

        vao.initializeData(buffer, GL15.GL_STATIC_DRAW,
                new Attribute(0, GL11.GL_FLOAT, 2),
                new Attribute(1, GL11.GL_BYTE, 4));

        vao.unbind();

        return vao;
    }

    /**
     * Method for vertex data VAO storing.
     *
     * @param vao      VAO to use
     * @param meshData mesh data to use
     */
    private static void storeVertexDataInVao(Vao vao, byte[] meshData) {
        ByteBuffer buffer = storeMeshDataInBuffer(meshData);

        vao.initializeData(buffer, GL15.GL_STATIC_DRAW,
                new Attribute(0, GL11.GL_FLOAT, 3),
                new Attribute(1, GL12.GL_UNSIGNED_INT_2_10_10_10_REV, 4, true),
                new Attribute(2, GL11.GL_UNSIGNED_BYTE, 4, true));
    }

    /**
     * Method for vertex data buffer storing.
     *
     * @param meshData mesh data to use
     * @return buffer with vertex data
     */
    private static ByteBuffer storeMeshDataInBuffer(byte[] meshData) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(meshData.length);

        buffer.put(meshData);
        buffer.flip();

        return buffer;
    }

    /**
     * Method for indices VAO storing.
     *
     * @param vao     VAO to use
     * @param indices indices to use
     */
    private static void storeIndicesInVao(Vao vao, int[] indices) {
        IntBuffer intBuffer = BufferUtils.createIntBuffer(indices.length);

        intBuffer.put(indices);
        intBuffer.flip();

        vao.createIndexBuffer(intBuffer);
    }
}
