package ru.hse.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import ru.hse.engine.ICamera;
import ru.hse.engine.Light;
import ru.hse.terrain.generation.Terrain;

/**
 * Terrain renderer class.
 */
public class TerrainRenderer {
    private final TerrainShader TERRAIN_SHADER;

    private final boolean USES_INDICES;

    /**
     * The class' constructor.
     *
     * @param terrainShader terrain shader class instance
     * @param usesIndices   boolean value if the renderer
     *                      should use indices or not
     */
    public TerrainRenderer(TerrainShader terrainShader, boolean usesIndices) {
        TERRAIN_SHADER = terrainShader;

        USES_INDICES = usesIndices;
    }

    /**
     * Method for terrain rendering.
     *
     * @param terrain       active terrain class instance
     * @param camera        active camera class instance
     * @param light         active light class instance
     * @param clippingPlane plane to clip
     */
    public void render(Terrain terrain,
                       ICamera camera, Light light,
                       Vector4f clippingPlane) {
        prepare(terrain, camera, light, clippingPlane);

        if (USES_INDICES)
            GL11.glDrawElements(GL11.GL_TRIANGLES,
                    terrain.getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0);
        else
            GL11.glDrawArrays(GL11.GL_TRIANGLES,
                    0,
                    terrain.getVertexCount());

        finish(terrain);
    }

    /**
     * Method for rendering preparation.
     *
     * @param terrain       active terrain class instance
     * @param camera        active camera class instance
     * @param light         active light class instance
     * @param clippingPlane plane to clip
     */
    private void prepare(Terrain terrain,
                         ICamera camera, Light light,
                         Vector4f clippingPlane) {
        terrain.getVao().bind();

        TERRAIN_SHADER.start();

        TERRAIN_SHADER.plane
                .loadVector4f(clippingPlane);
        TERRAIN_SHADER.lightBias
                .loadVector2f(light.getLightBias());
        TERRAIN_SHADER.lightDirection
                .loadVector3f(light.getDirection());
        TERRAIN_SHADER.lightColour
                .loadVector3f(light.getColor().getColor());
        TERRAIN_SHADER.projectionViewMatrix
                .loadMatrix(camera.getProjectionViewMatrix());
    }

    /**
     * Method for render stopping.
     *
     * @param terrain active terrain class instance
     */
    private void finish(Terrain terrain) {
        terrain.getVao().unbind();

        TERRAIN_SHADER.stop();
    }

    /**
     * "Destructor" of the class.
     */
    public void cleanUp() {
        TERRAIN_SHADER.cleanUp();
    }
}
