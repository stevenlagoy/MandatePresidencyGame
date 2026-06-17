package com.stevenlagoy.presidency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.stevenlagoy.presidency.GameRoot;
import org.jetbrains.annotations.NotNull;

public abstract class BaseScreen implements Screen {

    protected final GameRoot game;
    protected Stage stage;
    protected SpriteBatch batch;
    protected Skin skin;

    private boolean initialized = false;

    protected BaseScreen(@NotNull GameRoot game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (!initialized) {
            batch = new SpriteBatch();
            stage = new Stage();
            skin = game.getUiSkin();

            Table root = new Table();
            root.setFillParent(true);
            stage.addActor(root);

            buildUI(root, skin);
            initialized = true;
        }
        Gdx.input.setInputProcessor(getInputProcessor());
    }

    protected abstract void buildUI(@NotNull Table root, @NotNull Skin skin);

    protected InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderBackground(delta);
        stage.act(delta);
        stage.draw();
    }

    protected void renderBackground(float delta) {}

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        // Skin is shared and owned by GameRoot
    }

}
