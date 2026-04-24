package com.stevenlagoy.presidency.map.travel;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

public class TravelManager extends Manager {

    private Engine ENGINE;
    private ManagerState currentState;

    public TravelManager(Engine engine) {
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

    // ROUTING METHODS ----------------------------------------------------------------------------

//    public Route shortestRoute(Municipality source, Municipality destination) {
//        return shortestRoute(source, destination, true, true, true, true);
//    }
//
//    public Route shortestRoute(Municipality source, Municipality destination, boolean useRoads, boolean useAir, boolean useWater, boolean useRail) {
//
//    }
//
//    public Route shortestRoadRoute(Municipality source, Municipality destination) {
//
//    }
//
//    public Route shortestAirRoute(Municipality source, Municipality destination) {
//    }
//
//    public Route shortestWaterRoute(Municipality source, Municipality destination) {
//
//    }
//
//    public Route shortestRailRoute(Municipality source, Municipality destination) {
//
//    }
//
//    public Route cheapestRoute(Municipality source, Municipality destination) {
//
//    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public Manager fromJson(JSONObject json) {
        return null;
    }
}
