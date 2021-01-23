package ru.hse.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import ru.hse.engine.ICamera;
import ru.hse.engine.Light;
import ru.hse.terrain.generation.Terrain;

public class TerrainRenderer {
    private final TerrainShader TERRAIN_SHADER;

    private final boolean USES_INDICES;

    public TerrainRenderer(TerrainShader shader, boolean usesIndices) {
        TERRAIN_SHADER = shader;

        USES_INDICES = usesIndices;
    }

    public void render(Terrain terrain, ICamera camera, Light light, Vector4f clipPlane) {
        prepare(terrain, camera, light, clipPlane);

        if (USES_INDICES) {
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        } else {
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, terrain.getVertexCount());
        }
        finish(terrain);
    }

    public void cleanUp() {
        TERRAIN_SHADER.cleanUp();
    }

    private void prepare(Terrain terrain, ICamera camera, Light light, Vector4f clipPlane) {
        terrain.getVao().bind();
        TERRAIN_SHADER.start();
        TERRAIN_SHADER.plane.loadVector4f(clipPlane);
        TERRAIN_SHADER.lightBias.loadVector2f(light.getLightBias());
        TERRAIN_SHADER.lightDirection.loadVector3f(light.getDirection());
        TERRAIN_SHADER.lightColour.loadVector3f(light.getColor().getColor());
        TERRAIN_SHADER.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
    }

    private void finish(Terrain terrain) {
        terrain.getVao().unbind();
        TERRAIN_SHADER.stop();
    }
}
