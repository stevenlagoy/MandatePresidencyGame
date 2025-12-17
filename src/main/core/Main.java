/*
 * Main.java
 * Steven LaGoy
 */

package main.core;

import main.core.characters.Character;
import main.core.characters.attributes.names.NameManager2;
import main.core.demographics.Bloc;
import main.core.demographics.DemographicsManager;
import main.core.utils.IOUtil;
import main.core.utils.Logger;

/** Main entry point and relative root of the game engine and engine submanagers. */
public class Main {

    /** This class is non-instantiable. The {@code main()} entry point should be accessed in a static way. */
    private Main() {} // Non-Instantiable

    private static Engine engine;

    public static Engine Engine() { return engine; }

    private static DemographicsManager dm = new DemographicsManager();
    public static DemographicsManager DemographicsManager() { return dm; }

    public static void main(String[] args) {
        int errorCode = 0;
        
        // Initialize the engine
        engine = new Engine();

        NameManager2 nm = new NameManager2();
        dm.init();
        nm.init();

        Bloc[] blocs = {
            dm.matchBlocName("Man"),
            dm.matchBlocName("Anglo"),
            dm.matchBlocName("Christian"),
            dm.matchBlocName("Baby Boomer")
        };
        nm.getGivenNamesDistribution(blocs);

        System.exit(0);
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