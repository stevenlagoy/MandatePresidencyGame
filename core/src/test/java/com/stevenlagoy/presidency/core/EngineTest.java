package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import org.junit.jupiter.api.Test;

public class EngineTest {

    @Test
    public void testEngineConstruction() {
        Engine engine = new Engine();
        assert(!engine.DEBUG_MODE);
    }

    @Test
    public void testEngineConstructionDebugMode() {
        Engine engine = new Engine(true);
        assert(engine.DEBUG_MODE);
    }

    @Test
    public void testEngineInit() {
        Engine engine = new Engine();
        engine.init();
        assert(engine.getState() == Manager.ManagerState.ACTIVE);
        for (Manager manager : engine.getAllSubManagers()) {
            manager.printState();
            assert(manager.getState() == Manager.ManagerState.ACTIVE);
        }
    }

    @Test
    public void testEngineCleanup() throws InterruptedException {
        Engine engine = new Engine();
        engine.init();
        Thread.sleep(2000); // Pause for managers to initialize
        engine.cleanup();
        assert(engine.getState() == Manager.ManagerState.INACTIVE);
        for (Manager manager : engine.getAllSubManagers()) {
            manager.printState();
            assert(manager.getState() == Manager.ManagerState.INACTIVE);
        }
    }

    @Test
    public void testEngineToJson() throws InterruptedException {
        Engine engine = new Engine();
        engine.init();
        Thread.sleep(2000); // Pause for managers to initialize
        JSONObject engineJson = engine.toJson();
        assert(engineJson.toString().length() > 100);
    }

    @Test
    public void testEngineFromJson() throws InterruptedException {
        Engine engine = new Engine();
        engine.init();
        Thread.sleep(2000); // Pause for managers to initialize
        JSONObject engineJson = engine.toJson();
        Thread.sleep(2000);
        engine.cleanup();
        engine.init();
        Thread.sleep(2000); // Pause for managers to initialize
        engine.fromJson(engineJson);
        Thread.sleep(2000); // Pause for managers to load
        for (Manager manager : engine.getSubManagers()) {
            assert(manager.getState() == Manager.ManagerState.ACTIVE);
        }
    }
}
