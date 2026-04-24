package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

public class PoliticsManager extends Manager {

    private Engine ENGINE;
    private ManagerState currentState;


    public PoliticsManager(Engine engine) {
        ENGINE = engine;
        currentState = ManagerState.INACTIVE;
    }

    @Override
    public boolean init() {
        currentState = ManagerState.ACTIVE;
        return true;
    }

    @Override
    public @NotNull ManagerState getState() {
        return currentState;
    }

    @Override
    public boolean cleanup() {
        currentState = ManagerState.INACTIVE;
        return true;
    }

    public Government createGovernment() {
        return null;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public Manager fromJson(JSONObject json) {
        return null;
    }
}
