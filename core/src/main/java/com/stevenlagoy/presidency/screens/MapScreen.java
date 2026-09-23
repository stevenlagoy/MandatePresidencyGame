package com.stevenlagoy.presidency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.stevenlagoy.presidency.GameRoot;
import org.jetbrains.annotations.NotNull;

public class MapScreen extends BaseScreen {

    private static final float PLANE_WIDTH = 522.24f;
    private static final float PLANE_HEIGHT = 286.72f;

    private PerspectiveCamera camera;
    private Environment environment;
    private ModelBatch modelBatch;
    private Model groundModel;
    private ModelInstance groundInstance;
    private Texture  groundTexture;
    private MapCameraController cameraController;

    public MapScreen(GameRoot game) {
        super(game);
    }

    @Override
    protected void buildUI(@NotNull Table root, @NotNull Skin skin) {
        setupCamera();
        setupScene();

        cameraController = new MapCameraController(
            camera,
            new Vector3(0, 0, 0),
            0f, 45f, 150f
        );

        TextButton resetButton = new TextButton("Reset View", skin);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cameraController.reset();
            }
        });

        root.bottom().right();
        root.add(resetButton).pad(20).expand().bottom().right();
    }

    private void setupCamera() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.viewportWidth = 16f;
        camera.viewportHeight = 9f;
        camera.near = 1f;
        camera.far = 1000f;
    }

    private void setupScene() {
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(ColorAttribute.createAmbientLight(0.9f, 0.9f, 0.9f, 1f));

        groundTexture = new Texture(Gdx.files.internal("textures/backgrounds/background.png"));
        groundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Material material = new Material(TextureAttribute.createDiffuse(groundTexture));
//        Material material = new Material(ColorAttribute.createDiffuse(0f, 1f, 0f, 1f)); // Green

        ModelBuilder modelBuilder = new ModelBuilder();
        groundModel = modelBuilder.createRect(
            -PLANE_WIDTH, 0, -PLANE_HEIGHT,
            -PLANE_WIDTH, 0,  PLANE_HEIGHT,
             PLANE_WIDTH, 0,  PLANE_HEIGHT,
             PLANE_WIDTH, 0,  -PLANE_HEIGHT,
            0, 1, 0,
            material,
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        groundInstance = new ModelInstance(groundModel);
    }

    @Override
    protected InputProcessor getInputProcessor() {
        InputMultiplexer multiplexer = new  InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(cameraController);
        return multiplexer;
    }

    @Override
    protected void renderBackground(float delta) {
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        modelBatch.begin(camera);
        modelBatch.render(groundInstance, environment);
        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        modelBatch.dispose();
        groundModel.dispose();
        groundTexture.dispose();
    }
}
