package com.stevenlagoy.presidency.map;

import com.badlogic.gdx.Gdx;
import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.map.travel.route.RouteManager;
import com.stevenlagoy.presidency.util.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>MAP MANAGER</h1>
 * {@code ~/map/MapManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy     <br>
 *     <b>Created: </b> 10 December 2024 <br>
 *     <b>Modified:</b> 15 June 2026     <br>
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
    private final @NotNull Set<Descriptor> descriptors;

    private final @NotNull Set<CensusRegion> censusRegions;
    private final @NotNull Set<CensusDivision> censusDivisions;

    private MapIndex mapIndex;
    public MapIndex getMapIndex() {
        return mapIndex;
    }

    // Constructors

    public MapManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        ROUTE_MANAGER = new RouteManager(engine, this);
        states = new HashSet<>();
        congressionalDistricts = new HashSet<>();
        counties = new HashSet<>();
        municipalities = new HashSet<>();
        universities = new HashSet<>();
        descriptors = new HashSet<>();
        censusRegions = new HashSet<>();
        censusDivisions = new HashSet<>();
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of(ROUTE_MANAGER);
    }

    @Override
    protected void doInit() {
        Set<Set<Path>> statesPaths = getStatesPaths();
        Set<Path> statesDataPaths = getStatesDataPaths(statesPaths);
        Set<Path> statesMunicipalitiesPaths = getMunicipalitiesPaths(statesPaths);
        Set<Path> countiesPaths = getCountiesPaths(statesPaths);
        Set<Path> congressionalDistrictPaths = getCongressionalDistrictPaths(statesPaths);
        Path nationPath = FilePaths.NATION;
        Path censusRegionsDivisionsPath = FilePaths.CENSUS_REGIONS_DIVISISONS;

        createMunicipalities(statesMunicipalitiesPaths);
        createCounties(countiesPaths);
        resolveMunicipalitiesCounties();
        createStates(statesDataPaths);
        resolveCountiesStates();
        createNation(nationPath);
        createCongressionalDistricts(congressionalDistrictPaths);
        createCensusRegionsDivisions(censusRegionsDivisionsPath);
        resolveStatesDivisions();
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

    public void loadMapBinaries() {
        File binFile = Gdx.files.internal("maps/counties.bin").file();
        MapIndex mapIndex = MapIndex.Companion.load(binFile);
        int matched = 0, unmatched = 0;
        for (County county : counties) {
            if (county.getColor() == null) continue;
            int countyColor = county.getColor();
            RegionData region = mapIndex.regionByColor(countyColor);
            if (region != null) {
                county.setRegion$core(region);
                matched++;
            }
            else {
                Logger.error("No map region for %s (color #%s)", county.getFullName(), ColorUtils.toHex(countyColor));
            }
        }
    }

    private void readDescriptors() {
        JSONObject descriptorsJson = JSONProcessor.processJson(FilePaths.DESCRIPTORS);
        for (Object descriptorObj : descriptorsJson.getAsList()) {
            if (descriptorObj instanceof JSONObject descriptorJson) {
                descriptors.add(new Descriptor(engine, descriptorJson));
            }
        }
    }

    private @NotNull Set<Set<Path>> getStatesPaths() {
        requireState(ManagerState.INITIALIZING, ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED);
        try {
            Set<Path> statePaths = IOUtils.listDirectories(FilePaths.STATES_DIR);
            Set<Set<Path>> statesPaths = new HashSet<>();
            for (Path statePath : statePaths) {
                try {
                    statesPaths.add(IOUtils.listFiles(statePath));
                }
                catch (IOException e) {
                    onDegraded(e);
                }
            }
            return statesPaths;
        }
        catch (IOException e) {
            onError(e);
            return new HashSet<>();
        }
    }

    private @NotNull Optional<Path> getStateDataPath(@NotNull Set<Path> statePaths) {
        requireState(ManagerState.INITIALIZING, ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED);
        return statePaths.stream().filter(path -> path.getFileName().toString().matches("^[0-9]{2}\\.json$")).findFirst();
    }

    private @NotNull Optional<Path> getStateMunicipalitiesPath(@NotNull Set<Path> statePaths) {
        requireState(ManagerState.INITIALIZING, ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED);
        return statePaths.stream().filter(path -> path.getFileName().toString().matches("^[0-9]{2}_municipalities\\.json$")).findFirst();
    }

    private @NotNull Set<Path> getStateCountyPaths(@NotNull Set<Path> statePaths) {
        requireState(ManagerState.INITIALIZING, ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED);
        return statePaths.stream().filter(path -> path.getFileName().toString().matches("^[0-9]{5}\\.json$")).collect(Collectors.toSet());
    }

    private @NotNull Set<Path> getStateCongressionalDistrictPaths(@NotNull Set<Path> statePaths) {
        requireState(ManagerState.INITIALIZING, ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED);
        return statePaths.stream().filter(path -> path.getFileName().toString().matches("^[A-Z]{2}-[0-9]+\\.json$")).collect(Collectors.toSet());
    }

    private @NotNull Set<Path> getStatesDataPaths(@NotNull Set<Set<Path>> statesPaths) {
        return statesPaths.stream().map(this::getStateDataPath).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
    }

    private @NotNull Set<Path> getMunicipalitiesPaths(@NotNull Set<Set<Path>> statesPaths) {
        return statesPaths.stream().map(this::getStateMunicipalitiesPath).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
    }

    private @NotNull Set<Path> getCountiesPaths(@NotNull Set<Set<Path>> statesPaths) {
        return statesPaths.stream().map(this::getStateCountyPaths).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private @NotNull Set<Path> getCongressionalDistrictPaths(@NotNull Set<Set<Path>> statesPaths) {
        return statesPaths.stream().map(this::getStateCongressionalDistrictPaths).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private final Map<String, List<String>> municipalitiesCounties = new HashMap<>();
    @SuppressWarnings("unchecked")
    private void createMunicipalities(@NotNull Set<Path> municipalitiesDataPaths) {
        municipalitiesCounties.clear();
        for (Path municipalityDataPath : municipalitiesDataPaths) {
            JSONObject municipalitiesJson = JSONProcessor.processJson(municipalityDataPath);
            for (Object municipalityObj : municipalitiesJson.getAsList()) {
                if (municipalityObj instanceof JSONObject municipalityJson) {
                    municipalities.add(new Municipality(engine, municipalityJson));
                    municipalitiesCounties.put(municipalityJson.get("fullName", String.class), municipalityJson.get("counties", List.class));
                }
            }
        }
    }

    private final Map<String, String> countiesStates = new HashMap<>();
    private void createCounties(@NotNull Set<Path> countyDataPaths) {
        for (Path countyDataPath : countyDataPaths) {
            JSONObject countyJson = JSONProcessor.processJson(countyDataPath);
            counties.add(new County(engine, countyJson));
            countiesStates.put(countyJson.get("fullName", String.class), countyJson.get("state", String.class));
        }
    }

    private void resolveMunicipalitiesCounties() {
        for (Map.Entry<String, List<String>> entry : municipalitiesCounties.entrySet()) {
            Municipality municipality = matchMunicipality(entry.getKey()).orElseThrow();
            for (String countyName : entry.getValue()) {
                County county = matchCounty(countyName).orElseThrow();
                municipality.getCounties().add(county);
                county.getMunicipalities().add(municipality);
            }
        }
    }

    private final Map<String, String> statesDivisions = new HashMap<>();
    private void createStates(@NotNull Set<Path> statesDataPaths) {
        for (Path statesDataPath : statesDataPaths) {
            JSONObject stateJson = JSONProcessor.processJson(statesDataPath);
            states.add(new State(engine, stateJson));
            statesDivisions.put(stateJson.get("fullName", String.class), stateJson.get("censusDivision", String.class));
        }
    }

    private void resolveCountiesStates() {
        for (Map.Entry<String, String> entry : countiesStates.entrySet()) {
            County county = matchCounty(entry.getKey()).orElseThrow();
            State state = matchState(entry.getValue()).orElseThrow();
            county.setState$core(state);
            state.getCounties().add(county);
        }
    }

    private void createNation(@NotNull Path nationDataPath) {
        Nation.INSTANCE.setStates$core(states);
    }

    private void createCongressionalDistricts(@NotNull Set<Path> congressionalDistrictsDataPaths) {
        for (Path congressionalDistrictDataPath : congressionalDistrictsDataPaths) {
            JSONObject districtJson = JSONProcessor.processJson(congressionalDistrictDataPath);
            congressionalDistricts.add(new CongressionalDistrict(engine, districtJson));
        }
    }

    private void createCensusRegionsDivisions(@NotNull Path censusRegionsDivisionsDataPath) {
        JSONObject censusRegionsDivisionsJson = JSONProcessor.processJson(censusRegionsDivisionsDataPath);
        for (Object regionObj : censusRegionsDivisionsJson.getAsList()) {
            if (regionObj instanceof JSONObject regionJson) {
                censusRegions.add(new CensusRegion(engine, regionJson)); // Also builds Census Divisions
            }
        }
    }

    private void resolveStatesDivisions() {
        for (Map.Entry<String, String> entry : statesDivisions.entrySet()) {
            State state = matchState(entry.getValue()).orElseThrow();
            CensusDivision censusDivision = matchCensusDivision(entry.getKey()).orElseThrow();
            state.setCensusDivision$core(censusDivision);
            censusDivision.getStates().add(state);
        }
    }








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

    public @NotNull Optional<Municipality> matchMunicipality(@NotNull String name) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        return Optional.ofNullable(
            municipalities.stream().filter(municipality -> municipality.getFullName().equals(name)).findFirst().orElse(
            municipalities.stream().filter(municipality -> municipality.getCommonName().equals(name)).findFirst().orElse(null)
        ));
    }

    public @NotNull Municipality selectMunicipality(Demographics demographics) {
        requireOperational();
        Municipality selected = RandomUtils.randSelect(municipalities);
        assert selected != null;
        return selected;
    }

    public @NotNull Municipality selectMunicipality() {
        requireOperational();
        return selectMunicipality(engine.DEMOGRAPHICS_MANAGER.getCommonDemographics());
    }

    public @NotNull Municipality getMostPopulatedMunicipality() {
        assert(municipalities.stream().max(Comparator.comparing(Municipality::getPopulation)).isPresent());
        return municipalities.stream().max(Comparator.comparing(Municipality::getPopulation)).get();
    }

    public void readNationData() {
        requireState(ManagerState.INITIALIZING);
        JSONObject json = JSONProcessor.processJson(FilePaths.NATION);
    }

    public @NotNull Optional<University> matchUniversity(@NotNull String name) {
        return universities.stream().filter(university -> university.getCommonName().equals(name)).findFirst();
    }

    public @NotNull Optional<County> matchCounty(String name) {
        return counties.stream().filter(county -> county.getName().equals(name)).findFirst();
    }

    public @NotNull Optional<Descriptor> matchDescriptor(String name) {
        return descriptors.stream().filter(descriptor -> descriptor.getName().equals(name)).findFirst();
    }

    public @NotNull Optional<State> matchState(String name) {
        return states.stream().filter(state -> state.getName().equals(name)).findFirst();
    }

    public @NotNull Optional<CensusRegion> matchCensusRegion(String name) {
        return censusRegions.stream().filter(censusRegion -> censusRegion.getName().equals(name)).findFirst();
    }

    public @NotNull Optional<CensusDivision> matchCensusDivision(String name) {
        return censusDivisions.stream().filter(censusDivision -> censusDivision.getName().equals(name)).findFirst();
    }

    public @NotNull Set<CensusDivision> getCensusDivisions() {
        return censusDivisions;
    }
}
