package com.stevenlagoy.presidency.core;

import java.io.File;
import java.util.Arrays;

public final class MapReader {

    public static void compileMaps() {
        File[] mapFiles = { new File("Counties.png"), new File("Land.png"), new File("UrbanAreas.png") };
        Arrays.stream(mapFiles).forEach(MapReader::compileMap);
    }

    public static void compileMap(File mapFile) {
        // Read pixels

        // Convert colors to IDs

        // Split into tiles

        // Write binary files
    }

}
