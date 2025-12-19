package com.stevenlagoy.presidency.graphics.rendering;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.graphics.Camera;
import com.stevenlagoy.presidency.graphics.GraphicsManager;
import com.stevenlagoy.presidency.graphics.ShaderManager;
import com.stevenlagoy.presidency.graphics.Transformation;
import com.stevenlagoy.presidency.graphics.entity.Material;
import com.stevenlagoy.presidency.graphics.entity.Model;
import com.stevenlagoy.presidency.graphics.entity.terrain.Terrain;
import com.stevenlagoy.presidency.graphics.lighting.DirectionalLight;
import com.stevenlagoy.presidency.graphics.lighting.PointLight;
import com.stevenlagoy.presidency.graphics.lighting.SpotLight;
import com.stevenlagoy.presidency.graphics.utils.Utils;
import com.stevenlagoy.presidency.util.FilePaths;

public class TerrainRenderer implements IRenderer<Object> {

    ShaderManager shader;
    private List<Terrain> terrains;

    private final Engine ENGINE;

    public TerrainRenderer(Engine engine) throws Exception {
        this.ENGINE = engine;
        terrains = new ArrayList<>();
        shader = new ShaderManager();
    }

    @Override
    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResource(FilePaths.SHADERS_GFX_LOC.resolve("terrain_vertex.vs")));
        shader.createFragmentShader(Utils.loadResource(FilePaths.SHADERS_GFX_LOC.resolve("terrain_fragment.fs")));
        shader.link();
        shader.createUniform("projectionMatrix");
        shader.createUniform("backgroundTexture");
        shader.createUniform("redTexture");
        shader.createUniform("greenTexture");
        shader.createUniform("blueTexture");
        shader.createUniform("blendMap");
        shader.createUniform("transformationMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createMaterialUniform("material");
        shader.createUniform("specularPower");
        shader.createDirectionalLightUniform("directionalLight");
        shader.createPointLightListUniform("pointLights", GraphicsManager.MAX_POINT_LIGHTS);
        shader.createSpotLightListUniform("spotLights", GraphicsManager.MAX_SPOT_LIGHTS);
    }

    @Override
    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights,
            DirectionalLight directionalLight, Vector3f ambientLight, float specularPower) {
        shader.bind();
        shader.setUniform("projectionMatrix", ENGINE.GraphicsManager().getWindow().updateProjectionMatrix());
        RenderManager.renderLights(pointLights, spotLights, directionalLight, shader, ambientLight, specularPower);

        terrains.sort((t1, t2) -> {
            float dist1 = camera.getPosition().distance(t1.getPosition());
            float dist2 = camera.getPosition().distance(t2.getPosition());
            return Float.compare(dist2, dist1);
        });

        for (Terrain terrain : terrains) {
            bind(terrain.getModel());
            prepare(terrain, camera);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbind();
        }
        terrains.clear();
        shader.unbind();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Material material = model.getMaterial();
        if (material.hasTransparency()) {
            RenderManager.disableCulling();
        } else {
            RenderManager.enableCulling();
        }

        shader.setUniform("backgroundTexture", 0);
        shader.setUniform("redTexture", 1);
        shader.setUniform("greenTexture", 2);
        shader.setUniform("blueTexture", 3);
        shader.setUniform("blendMap", 4);

        shader.setUniform("material", model.getMaterial());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object terrain, Camera camera) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getBackground().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getRedTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                ((Terrain) terrain).getBlendMapTerrain().getGreenTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                ((Terrain) terrain).getBlendMapTerrain().getBlueTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMap().getTextureID());

        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix((Terrain) terrain));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public List<Terrain> getTerrain() {
        return terrains;
    }
}
