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

/**
 * Main entry point and relative root of the game engine and engine submanagers.
 */
public class Main {

    /**
     * This class is non-instantiable. The {@code main()} entry point should be
     * accessed in a static way.
     */
    private Main() {
    } // Non-Instantiable

    private static Engine engine;

    public static Engine Engine() {
        return engine;
    }

    public static void main(String[] args) {
        int errorCode = 0;

        // Initialize the engine
        engine = new Engine();

        engine.DemographicsManager().init();
        engine.NameManager().init();

        Demographics d = new Demographics(Engine().DemographicsManager(), "Generation Z", "Christian",
                "African American", "Man");

        System.out.printf("| %40s | %40s | %40s | %40s | %40s |%n", "LEGAL", "FORMAL", "BIOGRAPHICAL", "COMMON",
                "INFORMAL");
        for (int i = 0; i < 100; i++) {
            Name n = engine.NameManager().generateName(d);
            System.out.printf("| %40s | %40s | %40s | %40s | %40s |%n", n.getLegalName(), n.getFormalName(),
                    n.getBiographicalName(), n.getCommonName(), n.getInformalName());
        }

        System.exit(0);

        if (!engine.init())
            return;

        // Create some characters
        for (int i = 0; i < 100; i++) {
            Character c = new Character(Engine().CharacterManager(), Engine().DemographicsManager(),
                    Engine().MapManager(), Engine().NameManager());
            IOUtil.stdout.println(c.getName().getBiographicalName());
        }

        engine.writeGameState();

        // Clean up the engine
        engine.cleanup();
        Logger.log("Main Done");
        System.exit(errorCode);
    }
}