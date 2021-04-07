package ru.hse.terrain.generation;

import org.lwjgl.util.vector.Vector4f;
import ru.hse.engine.ICamera;
import ru.hse.engine.Light;
import ru.hse.graphics.TerrainRenderer;
import ru.hse.openGL.objects.Vao;

/**
 * Class for terrain representation.
 */
public class Terrain {
    private final TerrainRenderer terrainRenderer;

    private final Vao vao;

    private final int verticesAmount;

    /**
     * The class' constructor.
     *
     * @param vao             VAO of the map
     * @param verticesAmount  amount of vertices used
     * @param terrainRenderer terrain renderer class instance
     */
    public Terrain(Vao vao, int verticesAmount, TerrainRenderer terrainRenderer) {
        this.vao = vao;
        this.verticesAmount = verticesAmount;
        this.terrainRenderer = terrainRenderer;
    }

    /**
     * Method for terrain rendering.
     *
     * @param camera        camera used
     * @param light         light used
     * @param clippingPlane clipping plane vector used
     */
    public void render(ICamera camera, Light light, Vector4f clippingPlane) {
        terrainRenderer.render(this, camera, light, clippingPlane);
    }

    /**
     * "Destructor" of the class.
     */
    public void delete() {
        vao.delete(true);
    }

    /**
     * Getter of the amount of vertices.
     *
     * @return amount of vertices
     */
    public int getVerticesAmount() {
        return verticesAmount;
    }

    /**
     * Getter of the VAO used.
     *
     * @return VAO of the terrain
     */
    public Vao getVao() {
        return vao;
    }
}
