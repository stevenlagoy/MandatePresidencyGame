package com.stevenlagoy.presidency.graphics.rendering;

import org.joml.Vector3f;
import com.stevenlagoy.presidency.graphics.Camera;
import com.stevenlagoy.presidency.graphics.entity.Model;
import com.stevenlagoy.presidency.graphics.lighting.DirectionalLight;
import com.stevenlagoy.presidency.graphics.lighting.PointLight;
import com.stevenlagoy.presidency.graphics.lighting.SpotLight;

public interface IRenderer<T> {
    public void init() throws Exception;

    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights,
            DirectionalLight directionalLight, Vector3f ambientLight, float specularPower);

    abstract void bind(Model model);

    public void unbind();

    public void prepare(T t, Camera camera);

    public void cleanup();
}
