package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.politics.voting.Election;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ElectionsManager extends Manager {

    private ManagerState currentState;

    private List<Election> elections;

    public ElectionsManager() {
        currentState = ManagerState.INACTIVE;
    }

    public void doElections(LocalDateTime currentDate) {
        for (Election election : elections) {
            if (!election.getPollsOpenDate().isBefore(currentDate)) {
                // TODO
            }
        }
    }

    @Override
    public boolean init() {
        currentState = ManagerState.ACTIVE;
        return true;
    }

    @NotNull
    @Override
    public ManagerState getState() {
        return currentState;
    }

    @Override
    public boolean cleanup() {
        currentState = ManagerState.INACTIVE;
        return true;
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
