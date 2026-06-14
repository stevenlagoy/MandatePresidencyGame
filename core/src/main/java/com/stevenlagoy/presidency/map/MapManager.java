package com.stevenlagoy.presidency.map;

import com.badlogic.gdx.Gdx;
import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.map.travel.route.RouteManager;
import com.stevenlagoy.presidency.util.*;
import net.mgsx.gltf.scene3d.model.CubicVector3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * <h1>MAP MANAGER</h1>
 * {@code ~/map/MapManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy     <br>
 *     <b>Created: </b> 10 December 2024 <br>
 *     <b>Modified:</b> 02 June 2026     <br>
 * </p>
 *
 * MapManager is responsible for creating and tracking information about {@link MapEntity} instances,
 * including {@link Nation}, {@link State}, {@link CongressionalDistrict}, {@link County}, and
 * {@link Municipality}.
 *
 * @author Steven LaGoy
 */
public class MapManager extends Manager {

    // Static Functions

    public static double getAbsoluteDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    public static double getRoadDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    public static double getRailDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    public static double getAirDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    public static double getWaterDistance(MapEntity source, MapEntity destination) {
        return 0.0;
    }

    // Instance Fields

    public final @NotNull RouteManager ROUTE_MANAGER;

    private Nation nation;
    private final @NotNull Set<State> states;
    private final @NotNull Set<CongressionalDistrict> congressionalDistricts;
    private final @NotNull Set<County> counties;
    private final @NotNull Set<Municipality> municipalities;

    private final @NotNull Set<University> universities;

    // Constructors

    public MapManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        ROUTE_MANAGER = new RouteManager(engine, this);
        states = new HashSet<>();
        congressionalDistricts = new HashSet<>();
        counties = new HashSet<>();
        municipalities = new HashSet<>();
        universities = new HashSet<>();
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull Set<Manager> getSubManagers() {
        return Set.of(ROUTE_MANAGER);
    }

    @Override
    protected void doInit() {
        readNationData();
    }

    @Override
    protected void doCleanup() {
        universities.clear();
        municipalities.clear();
        counties.clear();
        congressionalDistricts.clear();
        states.clear();
        nation = null;
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        getSubManagers().forEach(manager -> manager.fromJson(json.get(manager.getClass().getSimpleName(), JSONObject.class)));
    }

    // Instance Methods

    public @NotNull Nation getNation() {
        requireOperational();
        return nation;
    }

    public @NotNull Set<State> getStates() {
        requireOperational();
        return states;
    }

    public @NotNull Set<CongressionalDistrict> getCongressionalDistricts() {
        requireOperational();
        return congressionalDistricts;
    }

    public @NotNull Set<County> getCounties() {
        requireOperational();
        return counties;
    }

    public @NotNull Set<Municipality> getMunicipalities() {
        requireOperational();
        return municipalities;
    }

    public @NotNull Optional<Municipality> matchMunicipalityByName(@NotNull String name) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        Optional<Municipality> res = municipalities.stream().filter(municipality -> municipality.getUniqueName().equals(name)).findFirst();
        if (res.isEmpty()) res = municipalities.stream().filter(municipality -> municipality.getFullName().equals(name)).findFirst();
        return res;
    }

    public @Nullable Municipality getMunicipalityByUniqueName(@NotNull String uniqueName) {
        requireOperational();
        return municipalities.stream().filter(municipality -> municipality.getUniqueName().equals(uniqueName)).findFirst().orElse(null);
    }

    public @Nullable Municipality getMunicipalityByNameAndState(@NotNull String municipalityName, @NotNull State state) {
        requireOperational();
        return Objects.requireNonNull(state.getMunicipalities()).stream().filter(municipality -> municipality.getCommonName().equals(municipalityName)).findFirst().orElse(null);
        // TODO do a better search
    }

    public @NotNull Municipality selectMunicipality(Demographics demographics) {
        requireOperational();
        Municipality selected = RandomUtils.randSelect(municipalities);
        assert selected != null;
        return selected;
    }

    /*

    Nation
        States
            Congressional Districts
            Counties
            Municipalities
        Routes

     */

    public @NotNull Municipality selectMunicipality() {
        requireOperational();
        return selectMunicipality(ENGINE.DEMOGRAPHICS_MANAGER.getCommonDemographics());
    }

    public @NotNull Municipality getMostPopulatedMunicipality() {
        assert(municipalities.stream().max(Comparator.comparing(Municipality::getPopulation)).isPresent());
        return municipalities.stream().max(Comparator.comparing(Municipality::getPopulation)).get();
    }

    public void readNationData() {
        requireState(ManagerState.INITIALIZING);
        JSONObject json = JSONProcessor.processJson(FilePaths.NATION);
    }

    public void readAllStateData(@NotNull Path stateDataDirectory) {
        requireState(ManagerState.INITIALIZING);
        try {
            Set<Path> dataFiles = IOUtils.listFiles(stateDataDirectory, IOUtils.FileExtension.JSON);
            State state = null;
            Set<CongressionalDistrict> congressionalDistricts = new HashSet<>();
            Set<County> counties = new HashSet<>();
            Set<Municipality> municipalities;
            // Find state first
            for (Path dataFile : dataFiles) {
                if (dataFile.getFileName().toString().matches("^[0-9]{2}\\.json$")) {
                    state = new State(ENGINE, JSONProcessor.processJson(dataFile));
                }
            }
            // Check that state was created
            if (state == null) {
                Logger.error("NO STATE DATA", String.format("Could not find state data file in directory %s", stateDataDirectory), new Exception());
                return;
            }
            for (Path dataFile : dataFiles) {
                JSONObject dataJson = JSONProcessor.processJson(dataFile);
                String fileName = dataFile.getFileName().toString();
                if (fileName.matches("^[A-Z]{2}-[0-9]+\\.json$")) {
                    // Congressional District data file
                    congressionalDistricts.add(new CongressionalDistrict(ENGINE, state, dataJson));
                } else if (fileName.matches("^[0-9]{5}\\.json$")) {
                    // County data file
                    counties.add(new County(ENGINE, state, dataJson));
                } else if (fileName.matches("^[0-9]{2}_municipalities\\.json$")) {
                    // Municipalities data file
                    municipalities = createMunicipalitiesFromData(state, dataJson);
                } else {
                    Logger.error("UNKNOWN DATA FILE", String.format("The file %s is not a known datatype", dataFile), new Exception());
                }
            }
            // State data filename matches XX.json
            // Municipalities data filename matches XX_municipalities.json
            // County data filenames match XXYYY.json
            // Congressional District data filenames match AA-N.json
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    public @NotNull Set<Municipality> createMunicipalitiesFromData(@NotNull State state, @NotNull JSONObject municipalitiesJson) {
        requireState(ManagerState.INITIALIZING);
        Set<Municipality> municipalities = new HashSet<>();
        for (Object municipalityObj : municipalitiesJson.getAsList()) {
            if (municipalityObj instanceof JSONObject municipalityJson) {
                municipalities.add(new Municipality(ENGINE, state, municipalityJson));
            }
        }
        return municipalities;
    }

    public @NotNull Optional<University> matchUniversity(@NotNull String name) {
        return universities.stream().filter(university -> university.getCommonName().equals(name)).findFirst();
    }

    public void loadMapBinaries() {
        File binFile = Gdx.files.internal("maps/counties.bin").file();
        MapIndex mapIndex = MapIndex.Companion.load(binFile);
        int matched = 0, unmatched = 0;
        for (County county : counties) {
            int countyColor = county.getColor();
            RegionData region = mapIndex.regionByColor(countyColor);
            if (region != null) {
                county.setMapRegion$core(region);
                matched++;
            }
            else {
                Logger.error("No map region for %s (color #%s)", county.getUniqueName(), ColorUtils.toHex(countyColor));
            }
        }
    }
}
