package com.stevenlagoy.presidency;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.screens.MainMenuScreen;
import com.stevenlagoy.presidency.screens.ScreenManager;

public class GameRoot extends Game {

    private AssetManager assets;
    private Engine engine;
    private Preferences prefs;
    private ScreenManager screenManager;

    @Override
    public void create() {
        assets = new AssetManager();
        loadCoreAssets();

        prefs = Gdx.app.getPreferences("mandate-prefs");

        engine = new Engine();

        screenManager = new ScreenManager(this);

        setScreen(screenManager.get(MainMenuScreen.class, () -> new MainMenuScreen(this)));
    }

    public AssetManager getAssets() {
        return assets;
    }

    public Skin getUiSkin() {
        return assets.get("uiskin.json", Skin.class);
    }

    public Engine getEngine() {
        return engine;
    }

    public Preferences getPreferences() {
        return prefs;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    private void loadCoreAssets() {
        assets.load("uiskin.atlas",  TextureAtlas.class);
        assets.load("uiskin.json", Skin.class);
        assets.finishLoading(); // Switch to LoadingScreen + assets.update() later
    }

    @Override
    public void dispose() {
        screenManager.disposeAll();
        assets.dispose();
    }

}
