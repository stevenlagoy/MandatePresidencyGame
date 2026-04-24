package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import kotlin.uuid.Uuid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PartyManager extends Manager {

    private Set<Party> parties;

    private Engine ENGINE;
    private ManagerState currentState;

    public PartyManager(Engine engine) {
        currentState = ManagerState.INACTIVE;
        ENGINE = engine;
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

    public @Nullable Party getPartyById(@NotNull Uuid id) {
        return parties.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
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
