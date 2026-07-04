package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.CensusDivision;
import com.stevenlagoy.presidency.map.CensusRegion;
import com.stevenlagoy.presidency.map.Municipality;
import com.stevenlagoy.presidency.map.State;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.stevenlagoy.presidency.util.FilePaths.*;

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

    // Read and Create

    private void readRoadways() {
        try {
            JSONObject json = new JSONObject(ROADWAYS);
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
            JSONObject json = new JSONObject(AIRPORTS);
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
            JSONObject json = new JSONObject(RAILWAYS);
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
            JSONObject json = new JSONObject(SEAPORTS);
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

    public @NotNull Set<Roadway> getRoadwaysConnecting(Municipality source, Municipality destination) {
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

    public @NotNull Set<Airport> getAirportsInRegion(@NotNull CensusRegion region) {
        Set<Airport> res = new HashSet<>();
        region.getCensusDivisions().forEach(division -> res.addAll(getAirportsInDivision(division)));
        return res;
    }

    public @NotNull Set<Airport> getAirportsInRegion(@NotNull CensusRegion region, @NotNull Airport.AirportSize size) {
        return getAirportsInRegion(region).stream().filter(airport -> airport.getSize().equals(size)).collect(Collectors.toSet());
    }

    public @NotNull Set<Airport> getAirportsInDivision(@NotNull CensusDivision division) {
        Set<Airport> res = new HashSet<>();
        division.getStates().forEach(state -> res.addAll(getAirportsInState(state)));
        return res;
    }

    public @NotNull Set<Airport> getAirportsInDivision(@NotNull CensusDivision division, @NotNull Airport.AirportSize size) {
        return getAirportsInDivision(division).stream().filter(airport -> airport.getSize().equals(size)).collect(Collectors.toSet());
    }

    public @NotNull Set<Airport> getAirportsInState(@NotNull State state) {
        return airports.stream().filter(airport -> airport.getLocation().getState().equals(state)).collect(Collectors.toSet());
    }

    public @NotNull Set<Airport> getAirportsInState(@NotNull State state, @NotNull Airport.AirportSize size) {
        return getAirportsInState(state).stream().filter(airport -> airport.getSize().equals(size)).collect(Collectors.toSet());
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
