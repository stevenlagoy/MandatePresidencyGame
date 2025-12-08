/*
 * Main.java
 * Steven LaGoy
 */

package main.core;

import main.core.characters.Character;
import main.core.characters.CharacterManager;
import main.core.characters.attributes.names.NameManager;

public class Main {

    /** This class is non-instantiable. The {@code main()} entry point should be accessed in a static way. */
    private Main() {} // Non-Instantiable

    private static Engine engine;

    public static Engine Engine() { return engine; }

    public static void main(String[] args) {
        int errorCode = 0;
        
        // Initialize the engine
        engine = new Engine();
        if (!engine.init()) return;

        // Create some characters
        for (int i = 0; i < 100; i++) {
            Character c = new Character();
            IOUtil.stdout.println(c.getName().getBiographicalName());
        }

        engine.writeGameState();

        // Clean up the engine
        engine.cleanup();
        Logger.log("Main Done");
        System.exit(errorCode);
    }
}