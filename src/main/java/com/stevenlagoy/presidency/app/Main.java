/*
 * Main
 * ~/app/Main.java
 * Steven LaGoy
 * Created: 28 August 2024 at 11:25 PM
 * Modified: 28 December 2025
 */

package com.stevenlagoy.presidency.app;

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

    /** Get the game engine being used by {@code Main}. Accessing the engine or subengines this way is discouraged. */
    public static Engine Engine() {
        return engine;
    }

    public static void main(String[] args) {
        int errorCode = 0;
        boolean debug = false;
        String debugFlag = "-d";

        for (String arg : args) {
            if (arg.equals(debugFlag)) {
                debug = true;
            }
        }

        // Initialize the engine
        engine = new Engine(debug);

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