package com.stevenlagoy.presidency;

import com.badlogic.gdx.Game;
import com.stevenlagoy.presidency.screens.MainMenuScreen;

public class GameRoot extends Game {

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }

}
