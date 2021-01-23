package ru.hse.terrain.generation;

import org.lwjgl.util.vector.Vector4f;
import ru.hse.engine.ICamera;
import ru.hse.engine.Light;
import ru.hse.graphics.TerrainRenderer;
import ru.hse.openGL.objects.Vao;

public class Terrain {
    private final Vao vao;
    private final int vertexCount;
    private final TerrainRenderer renderer;

    public Terrain(Vao vao, int vertexCount, TerrainRenderer renderer){
        this.vao = vao;
        this.vertexCount = vertexCount;
        this.renderer = renderer;
    }

    public int getVertexCount(){
        return vertexCount;
    }

    public Vao getVao(){
        return vao;
    }

    public void render(ICamera camera, Light light, Vector4f clipPlane){
        renderer.render(this, camera, light, clipPlane);
    }

    public void delete(){
        vao.delete(true);
    }
}
