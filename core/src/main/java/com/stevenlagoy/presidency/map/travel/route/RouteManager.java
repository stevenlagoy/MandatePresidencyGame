package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.entities.MapEntity;
import com.stevenlagoy.presidency.util.FilePath;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteManager extends Manager {

    // Instance Fields

    // Submanagers
    private final AirportManager AIRPORT_MANAGER;
    private final RailwayManager RAILWAY_MANAGER;
    private final RoadwayManager ROADWAY_MANAGER;
    private final SeaportManager SEAPORT_MANAGER;

    public RouteManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        AIRPORT_MANAGER = new AirportManager(engine, this);
        RAILWAY_MANAGER = new RailwayManager(engine, this);
        ROADWAY_MANAGER = new RoadwayManager(engine, this);
        SEAPORT_MANAGER = new SeaportManager(engine, this);
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of(
            AIRPORT_MANAGER,
            RAILWAY_MANAGER,
            ROADWAY_MANAGER,
            SEAPORT_MANAGER
        );
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doCleanup() {
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
    }

    // Instance Methods

    public static double getDirectDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

}
