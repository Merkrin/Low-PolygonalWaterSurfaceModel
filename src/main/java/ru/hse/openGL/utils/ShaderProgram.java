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
    private final int programId;

    /**
     * The class' constructor.
     *
     * @param vertexShader   vertex shader file
     * @param fragmentShader fragment shader file
     * @param inVariables    incoming variables
     */
    public ShaderProgram(FileBuffer vertexShader, FileBuffer fragmentShader,
                         String... inVariables) {
        int vertexShaderID = loadShader(vertexShader,
                GL20.GL_VERTEX_SHADER);
        int fragmentShaderID = loadShader(fragmentShader,
                GL20.GL_FRAGMENT_SHADER);

        programId = GL20.glCreateProgram();

        GL20.glAttachShader(programId, vertexShaderID);
        GL20.glAttachShader(programId, fragmentShaderID);

        bindAttributes(inVariables);
        GL20.glLinkProgram(programId);

        GL20.glDetachShader(programId, vertexShaderID);
        GL20.glDetachShader(programId, fragmentShaderID);

        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
    }

    /**
     * Method for uniforms saving.
     *
     * @param uniforms uniform variables to save
     */
    protected void storeAllUniformLocations(Uniform... uniforms) {
        for (Uniform uniform : uniforms)
            uniform.storeUniformLocation(programId);

        GL20.glValidateProgram(programId);
    }

    /**
     * Method for shader program starting.
     */
    public void start() {
        GL20.glUseProgram(programId);
    }

    /**
     * Method for attributes binding.
     *
     * @param inVariables incoming variables
     */
    private void bindAttributes(String[] inVariables) {
        for (int i = 0; i < inVariables.length; i++)
            GL20.glBindAttribLocation(programId, i, inVariables[i]);
    }

    /**
     * Method for shader loading.
     *
     * @param shaderFile shader file to load
     * @param type       shader type
     * @return shader id
     */
    private int loadShader(FileBuffer shaderFile, int type) {
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader reader = shaderFile.getBufferedReader();
            String line;

            while ((line = reader.readLine()) != null)
                shaderSource.append(line).append("//\n");

            reader.close();
        } catch (Exception e) {
            System.err.println("Could not read shader file.");
            e.printStackTrace();

            System.exit(-1);
        }

        int shaderId = GL20.glCreateShader(type);

        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderId, 500));
            System.err.println("Could not compile shader " + shaderFile);

            System.exit(-1);
        }

        return shaderId;
    }

    /**
     * Method for shader stopping.
     */
    public void stop() {
        GL20.glUseProgram(0);
    }

    /**
     * "Destructor" of the shader.
     */
    public void cleanUp() {
        GL20.glUseProgram(0);
        GL20.glDeleteProgram(programId);
    }
}
