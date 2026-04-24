package com.stevenlagoy.presidency.map;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.map.travel.route.Airport;
import com.stevenlagoy.presidency.map.travel.route.Roadway;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.IOUtils;
import com.stevenlagoy.presidency.util.Logger;
import com.stevenlagoy.presidency.util.RandomUtils;
import kotlin.uuid.Uuid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class MapManager extends Manager {

    // CONSTANTS ----------------------------------------------------------------------------------

    private Nation nation;
    private Set<State> states;
    private Set<CongressionalDistrict> congressionalDistricts;
    private Set<County> counties;
    private Set<Municipality> municipalities;

    private Set<Roadway> roadways;
    private Set<Airport> airports;
    private Set<University> universities;

    // INSTANCE FIELDS ----------------------------------------------------------------------------

    private Engine ENGINE;
    private ManagerState currentState;

    // CONSTRUCTOR --------------------------------------------------------------------------------

    public MapManager(Engine engine) {
        ENGINE = engine;
        currentState = ManagerState.INACTIVE;
        states = new HashSet<>();
        congressionalDistricts = new HashSet<>();
        counties = new HashSet<>();
        municipalities = new HashSet<>();
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    @Override
    public boolean init() {
        readNationData();
        currentState = ManagerState.ACTIVE;
        return true;
    }

    @Override
    public boolean cleanup() {
        currentState = ManagerState.INACTIVE;
        return true;
    }

    @Override
    public @NotNull ManagerState getState() {
        return currentState;
    }

    // JSONIC -------------------------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        return new JSONObject("MapManager", Map.of(

        ));
    }

    @Override
    public Manager fromJson(JSONObject json) {
        return this;
    }

    // INSTANCE METHODS ---------------------------------------------------------------------------

    public @NotNull Nation getNation() {
        return nation;
    }

    public @NotNull Set<State> getStates() {
        return states;
    }

    public @NotNull Set<CongressionalDistrict> getCongressionalDistricts() {
        return congressionalDistricts;
    }

    public @NotNull Set<County> getCounties() {
        return counties;
    }

    public @NotNull Set<Municipality> getMunicipalities() {
        return municipalities;
    }

    public @Nullable Municipality getMunicipalityByUniqueName(@NotNull String uniqueName) {
        return municipalities.stream().filter(municipality -> municipality.getUniqueName().equals(uniqueName)).findFirst().orElse(null);
    }

    public @Nullable Municipality getMunicipalityByNameAndState(@NotNull String municipalityName, @NotNull State state) {
        return Objects.requireNonNull(state.getMunicipalities()).stream().filter(municipality -> municipality.getCommonName().equals(municipalityName)).findFirst().orElse(null);
        // TODO do a better search
    }

    public @NotNull Municipality selectMunicipality(Demographics demographics) {
        Municipality selected = RandomUtils.randSelect(municipalities);
        assert selected != null;
        return selected;
    }

    public @NotNull Municipality selectMunicipality() {
        return selectMunicipality(ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.getCommonDemographics());
    }

    public void readMapData() {

    }

    public void readNationData() {
        JSONObject json = JSONProcessor.processJson(FilePaths.NATION);
    }

    public void readAllStateData(@NotNull Path stateDataDirectory) {
        try {
            Set<Path> dataFiles = IOUtils.listFiles(stateDataDirectory, IOUtils.FileExtension.JSON);
            State state = null;
            Set<CongressionalDistrict> congressionalDistricts = new HashSet<>();
            Set<County> counties = new HashSet<>();
            Set<Municipality> municipalities;
            // Find state first
            for (Path dataFile : dataFiles) {
                if (dataFile.getFileName().toString().matches("^[0-9]{2}\\.json$")) {
                    state = new State(ENGINE.MANAGERS, JSONProcessor.processJson(dataFile));
                }
            }
            // Check that state was created
            if (state == null) {
                Logger.log("NO STATE DATA", String.format("Could not find state data file in directory %s", stateDataDirectory), new Exception());
                return;
            }
            for (Path dataFile : dataFiles) {
                JSONObject dataJson = JSONProcessor.processJson(dataFile);
                String fileName = dataFile.getFileName().toString();
                if (fileName.matches("^[A-Z]{2}-[0-9]+\\.json$")) {
                    // Congressional District data file
                    congressionalDistricts.add(new CongressionalDistrict(ENGINE.MANAGERS, state, dataJson));
                }
                else if (fileName.matches("^[0-9]{5}\\.json$")) {
                    // County data file
                    counties.add(new County(ENGINE.MANAGERS, state, dataJson));
                }
                else if (fileName.matches("^[0-9]{2}_municipalities\\.json$")) {
                    // Municipalities data file
                    municipalities = createMunicipalitiesFromData(state, dataJson);
                }
                else {
                    Logger.log("UNKNOWN DATA FILE", String.format("The file %s is not a known datatype", dataFile), new Exception());
                }
            }
            // State data filename matches XX.json
            // Municipalities data filename matches XX_municipalities.json
            // County data filenames match XXYYY.json
            // Congressional District data filenames match AA-N.json
        } catch (IOException e) {
            Logger.log(e);
        }
    }

    public @NotNull Set<Municipality> createMunicipalitiesFromData(@NotNull State state, @NotNull JSONObject municipalitiesJson) {
        Set<Municipality> municipalities = new HashSet<>();
        for (Object municipalityObj : municipalitiesJson.getAsList()) {
            if (municipalityObj instanceof JSONObject municipalityJson) {
                municipalities.add(new Municipality(ENGINE.MANAGERS, state, municipalityJson));
            }
        }
        return municipalities;
    }

    // DISTANCE FUNCTIONS -------------------------------------------------------------------------------------------------------------------------------------

    public static double getAbsoluteDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    public static double getRoadDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    public static double getTrainDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    public static double getAirDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    public static double getWaterDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }
}
