package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

public class UniformVector4f extends Uniform {
    public UniformVector4f(String name) {
        super(name);
    }

    public void loadVector4f(Vector4f vector) {
        loadVector4f(vector.x, vector.y, vector.z, vector.w);
    }

    public void loadVector4f(float x, float y, float z, float w) {
        GL20.glUniform4f(super.getLocation(), x, y, z, w);
    }

}