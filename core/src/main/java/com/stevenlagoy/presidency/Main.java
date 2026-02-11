package com.stevenlagoy.presidency;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.ScreenUtils;

import net.mgsx.gltf.scene3d.scene.SceneManager;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {

    private SceneManager sceneManager;

    public PerspectiveCamera cam;
    public ModelBatch batch;
    public List<ModelInstance> instances;
    public Environment environment;

    @Override
    public void create() {

        sceneManager = new SceneManager();

        batch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(Settings.FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0, 0, 0);
        cam.lookAt(1f, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

    @Override
    public void render() {

        ScreenUtils.clear(0, 0, 0, 1, true);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin(cam);
        for (ModelInstance instance : instances)
            batch.render(instance, environment);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
