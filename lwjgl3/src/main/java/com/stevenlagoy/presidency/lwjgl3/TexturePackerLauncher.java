package com.stevenlagoy.presidency.lwjgl3;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerLauncher {
    public static void packIt() {
        System.out.println("Packing It...");
        TexturePacker.process(
            "assets_raw/ui", // input folder
            "assets",        // output folder
            "uiskin"         // atlas name
        );
        System.out.println("Packed It!");
    }

    public static void main(String[] args) {
        packIt();
    }

}
