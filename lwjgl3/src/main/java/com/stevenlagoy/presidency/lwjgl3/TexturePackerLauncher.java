package com.stevenlagoy.presidency.lwjgl3;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerLauncher {
    public static void packIt() {
        TexturePacker.process(
            "assets_raw/ui", // input folder
            "assets",        // output folder
            "uiskin"         // atlas name
        );
    }

    public static void main(String[] args) {
        packIt();
    }

}
