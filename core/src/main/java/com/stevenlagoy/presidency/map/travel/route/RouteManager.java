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

    private final @NotNull Set<Roadway.RoadwayDesignation> roadwayDesignations;
    private final @NotNull Set<Roadway> roadways;
    private final @NotNull Set<Airport> airports;
    private final @NotNull Set<Railway> railways;
    private final @NotNull Set<Seaport> seaports;

    // Constructors

    public RouteManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        roadwayDesignations = new HashSet<>();
        roadways = new HashSet<>();
        airports = new HashSet<>();
        railways = new HashSet<>();
        seaports = new HashSet<>();
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
        readRoadways();
        readAirports();
        readSeaports();
        readRailways();
    }

    @Override
    protected void doCleanup() {
        roadwayDesignations.clear();
        roadways.clear();
        airports.clear();
        railways.clear();
        seaports.clear();
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

    // Read and Create

    private void readRoadways() {
        try {
            JSONObject json = new JSONObject(FilePath.ROADWAYS.path);
            for (Object roadwayObj : json.requireArray()) {
                if (roadwayObj instanceof JSONObject roadwayJson) {
                    try {
                        roadways.add(new Roadway(engine, roadwayJson));
                    }
                    catch (IllegalArgumentException e) {
                        onDegraded(e);
                    }
                }
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void readAirports() {
        try {
            JSONObject json = new JSONObject(FilePath.AIRPORTS.path);
            for (Object obj : json.requireArray()) {
                if (obj instanceof JSONObject airportJson) {
                    try {
                        airports.add(new Airport(engine, airportJson));
                    }
                    catch (IllegalArgumentException e) {
                        onDegraded(e);
                    }
                }
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void readRailways() {
        try {
            JSONObject json = new JSONObject(FilePath.RAILWAYS.path);
            for (Object obj : json.requireArray()) {
                if (obj instanceof JSONObject railwayJson) {
                    try {
                        railways.add(new Railway(engine, railwayJson));
                    }
                    catch (IllegalArgumentException e) {
                        onDegraded(e);
                    }
                }
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void readSeaports() {
        try {
            JSONObject json = new JSONObject(FilePath.SEAPORTS.path);
            for (Object obj : json.requireArray()) {
                if (obj instanceof JSONObject seaportJson) {
                    try {
                        seaports.add(new Seaport(engine, seaportJson));
                    }
                    catch (IllegalArgumentException e) {
                        onDegraded(e);
                    }
                }
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    // Roadways

    public @NotNull Set<Roadway> getRoadways() {
        return roadways;
    }

    public @NotNull Set<Roadway> getRoadwaysConnecting(MapEntity source, MapEntity destination) {
        Set<Roadway> connections = new HashSet<>();
        for (Roadway roadway : roadways) {
            if(roadway.connects(source) && roadway.connects(destination)) {
                connections.add(roadway);
            }
        }
        return connections;
    }

    public @NotNull Optional<Roadway> matchRoadway(@NotNull String nameOrCode) {
        requireOperational();
        Optional<Roadway> res = roadways.stream().filter(roadway -> roadway.getName().equals(nameOrCode)).findFirst();
        if (res.isEmpty()) res = roadways.stream().filter(roadway -> roadway.getCode().equals(nameOrCode)).findFirst();
        return res;
    }

    // Airports

    public @NotNull Set<Airport> getAirports() {
        return airports;
    }

    public @NotNull Set<Airport> getAirports(@NotNull Airport.AirportSize size) {
        return getAirports().stream().filter(airport -> airport.getSize().equals(size)).collect(Collectors.toSet());
    }

    // Seaports

    public @NotNull Set<Seaport> getSeaports() {
        return seaports;
    }

    // Railways

    public @NotNull Set<Railway> getRailways() {
        return railways;
    }

}
