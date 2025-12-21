/*
 * Main.java
 * Steven LaGoy
 */

package com.stevenlagoy.presidency.app;

import com.stevenlagoy.presidency.characters.Character;
import com.stevenlagoy.presidency.characters.attributes.names.Name;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.IOUtil;
import com.stevenlagoy.presidency.util.Logger;
import com.stevenlagoy.presidency.core.Engine;

/** Main entry point */
public class Main {

    /**
     * This class is non-instantiable. The {@code main()} entry point should be
     * accessed in a static way.
     */
    private Main() {} // Non-Instantiable

    private static Engine engine;

    public static Engine Engine() {
        return engine;
    }

    public static void main(String[] args) {
        int errorCode = 0;

        // Initialize the engine
        engine = new Engine();

        if (!engine.init())
            return;

        // Run the game

        // ...

        // Game is finished

        engine.writeGameState();

        // Clean up the engine
        engine.cleanup();
        Logger.log("Main Done");
        System.exit(errorCode);
    }
}