package com.stevenlagoy.presidency.screens;

import com.badlogic.gdx.Screen;
import com.stevenlagoy.presidency.GameRoot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ScreenManager {

    private final GameRoot game;
    private final Map<Class<? extends Screen>, Screen> cache = new HashMap<>();

    public ScreenManager(GameRoot game) {
        this.game = game;
    }

    @SuppressWarnings("unchecked")
    public <T extends Screen> T get(Class<T> type, Supplier<T> factory) {
        return (T) cache.computeIfAbsent(type, k -> factory.get());
    }

    public <T extends Screen> void show(Class<T> type, Supplier<T> factory) {
        game.setScreen(get(type, factory));
    }

    public void disposeAll() {
        cache.values().forEach(Screen::dispose);
        cache.clear();
    }

}
