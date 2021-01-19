package ru.hse.graphics;

import org.lwjgl.opengl.GL11;
import ru.hse.engine.ICamera;
import ru.hse.engine.Light;
import ru.hse.terrain.generation.Terrain;

public class TerrainRenderer {
    private final TerrainShader TERRAIN_SHADER;

    private final boolean USES_INDICES;

    /**
     * @param shader
     *            - The shader program used for rendering this terrain.
     * @param usesIndices
     *            - Indicates whether the terrain will be rendered with an index
     *            buffer or not.
     */
    public TerrainRenderer(TerrainShader shader, boolean usesIndices) {
        TERRAIN_SHADER = shader;

        USES_INDICES = usesIndices;
    }

    /**
     * Renders a terrain to the screen. If the terrain has an index buffer the
     * glDrawElements is used. Otherwise glDrawArrays is used.
     *
     * @param terrain
     *            - The terrain to be rendered.
     * @param camera
     *            - The camera being used for rendering the terrain.
     * @param light
     *            - The light being used to iluminate the terrain.
     */
    public void render(Terrain terrain, ICamera camera, Light light) {
        prepare(terrain, camera, light);

        if (USES_INDICES) {
            GL11.glDrawElements(GL11.GL_TRIANGLES,
                    terrain.getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0);
        } else {
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, terrain.getVertexCount());
        }
        finish(terrain);
    }

    /**
     * Used when the program closes. Deletes the shader program.
     */
    public void cleanUp() {
        TERRAIN_SHADER.cleanUp();
    }

    /**
     * Starts the shader program and loads up any necessary uniform variables.
     *
     * @param terrain
     *            - The terrain to be rendered.
     * @param camera
     *            - The camera being used to render the scene.
     * @param light
     *            - The light in the scene.
     */
    private void prepare(Terrain terrain, ICamera camera, Light light) {
        terrain.getVao().bind();
        TERRAIN_SHADER.start();
        TERRAIN_SHADER.lightBias.loadVector2f(light.getLightBias());
        TERRAIN_SHADER.lightDirection.loadVector3f(light.getDirection());
        TERRAIN_SHADER.lightColour.loadVector3f(light.getColor().getColor());
        TERRAIN_SHADER.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
    }

    /**
     * End the rendering process by unbinding the VAO and stopping the shader
     * program.
     *
     * @param terrain
     */
    private void finish(Terrain terrain) {
        terrain.getVao().unbind();
        TERRAIN_SHADER.stop();
    }
}
