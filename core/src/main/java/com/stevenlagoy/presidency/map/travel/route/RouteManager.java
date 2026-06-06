package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.CensusDivision;
import com.stevenlagoy.presidency.map.CensusRegion;
import com.stevenlagoy.presidency.map.Municipality;
import com.stevenlagoy.presidency.map.State;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
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
    public @NotNull Set<Manager> getSubManagers() {
        return Set.of();
    }

    @Override
    protected void doInit() {
        readRoadwayTypes();
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

    private void readRoadwayTypes() {
        JSONObject json = JSONProcessor.processJson(ROADWAY_DESIGNATIONS);
        for (Object obj : json.getAsList()) {
            if (obj instanceof JSONObject designationJson) {
                String name = designationJson.getKey();
                int speed = designationJson.getAsNumber().intValue();
                roadwayDesignations.add(new Roadway.RoadwayDesignation(name, speed));
            }
        }
    }

    private void readRoadways() {
        JSONObject json = JSONProcessor.processJson(ROADWAYS);
        for (Object obj : json.getAsList()) {
            if (obj instanceof JSONObject roadwayJson) {
                roadways.add(new Roadway(ENGINE, roadwayJson));
            }
        }
    }

    private void readAirports() {
        JSONObject json = JSONProcessor.processJson(AIRPORTS);
        for (Object obj : json.getAsList()) {
            if (obj instanceof JSONObject airportJson) {
                airports.add(new Airport(ENGINE, airportJson));
            }
        }
    }

    private void readRailways() {
        JSONObject json = JSONProcessor.processJson(RAILWAYS);
        for (Object obj : json.getAsList()) {
            if (obj instanceof JSONObject railwayJson) {
                railways.add(new Railway(ENGINE, railwayJson));
            }
        }
    }

    private void readSeaports() {
        JSONObject json = JSONProcessor.processJson(SEAPORTS);
        for (Object obj : json.getAsList()) {
            if (obj instanceof JSONObject seaportJson) {
                seaports.add(new Seaport(ENGINE, seaportJson));
            }
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

    public @NotNull Optional<Roadway.RoadwayDesignation> matchRoadwayDesignation(@NotNull String designationName) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        return roadwayDesignations.stream().filter(designation -> designation.getName().equals(designationName)).findFirst();
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
        region.getDivisions().forEach(division -> res.addAll(getAirportsInDivision(division)));
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
