package ru.hse.openGL.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import ru.hse.utils.FileBuffer;

import java.io.BufferedReader;

/**
 * Class for shaders processing.
 */
public class ShaderProgram {
    private int programID;

    public ShaderProgram(FileBuffer vertexFile, FileBuffer fragmentFile,
                         String... inVariables) {
        int vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        int fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram();

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        bindAttributes(inVariables);
        GL20.glLinkProgram(programID);

        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);

        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
    }

    public ShaderProgram(FileBuffer vertexFile, FileBuffer geometryFile,
                         FileBuffer fragmentFile, String... inVariables) {
        int vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        int geometryShaderID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);
        int fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram();

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, geometryShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        bindAttributes(inVariables);
        GL20.glLinkProgram(programID);

        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, geometryShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);

        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(geometryShaderID);
        GL20.glDeleteShader(fragmentShaderID);
    }

    /**
     * Method for uniforms saving.
     *
     * @param uniforms uniform variables to save
     */
    protected void storeAllUniformLocations(Uniform... uniforms) {
        for (Uniform uniform : uniforms)
            uniform.storeUniformLocation(programID);

        GL20.glValidateProgram(programID);
    }

    protected void storeSomeUniformLocations(Uniform... uniforms) {
        for (Uniform uniform : uniforms)
            uniform.storeUniformLocation(programID);
    }

    protected void validateProgram() {
        GL20.glValidateProgram(programID);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        GL20.glUseProgram(0);
        GL20.glDeleteProgram(programID);
    }

    private void bindAttributes(String[] inVariables) {
        for (int i = 0; i < inVariables.length; i++)
            GL20.glBindAttribLocation(programID, i, inVariables[i]);
    }

    private int loadShader(FileBuffer file, int type) {
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader reader = file.getBufferedReader();
            String line;

            while ((line = reader.readLine()) != null)
                shaderSource.append(line).append("//\n");

            reader.close();
        } catch (Exception e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);

        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader " + file);
            System.exit(-1);
        }

        return shaderID;
    }
}
