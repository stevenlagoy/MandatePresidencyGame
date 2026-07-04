package com.stevenlagoy.presidency.map;

import com.badlogic.gdx.Gdx;
import com.stevenlagoy.jsonic.JSONObject;
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
import java.util.stream.Stream;

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

    private final Map<String, Set<Municipality>> countiesMunicipalities = new HashMap<>();
    private final Map<String, Set<County>> statesCounties = new HashMap<>();
    private final Map<String, String> statesDivisions = new HashMap<>();


    private MapIndex mapIndex;
    public MapIndex getMapIndex() {
        return mapIndex;
    }

    private boolean selfIsInitialized = false;

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
        resolveCountiesMunicipalities();
        createStates(statesDataPaths);
        resolveStatesCounties();
        createNation(nationPath);
        createCongressionalDistricts(congressionalDistrictPaths);
        createCensusRegionsDivisions(censusRegionsDivisionsPath);
        resolveStatesDivisions();

        selfIsInitialized = verifyInitialization();
        if (!selfIsInitialized) onDegraded(new Exception("Verification failed"));
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
        getSubManagers().forEach(manager -> manager.fromJson(json.requireJson(manager.getClass().getSimpleName())));
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
        try {
            JSONObject descriptorsJson = new JSONObject(FilePaths.DESCRIPTORS);
            for (Object descriptorObj : descriptorsJson.requireArray()) {
                if (descriptorObj instanceof JSONObject descriptorJson) {
                    descriptors.add(new Descriptor(engine, descriptorJson));
                }
            }
        } catch (IOException e) {
            onError(e);
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

    @SuppressWarnings("unchecked")
    private void createMunicipalities(@NotNull Set<Path> municipalitiesDataPaths) {
        try {
            countiesMunicipalities.clear();
            for (Path municipalityDataPath : municipalitiesDataPaths) {
                JSONObject municipalitiesJson = new JSONObject(municipalityDataPath);
                for (Object municipalityObj : municipalitiesJson.requireArray()) {
                    if (municipalityObj instanceof JSONObject municipalityJson) {
                        String municipalityFIPS = municipalityJson.requireString("FIPS");
                        String stateFips = municipalityFIPS.substring(0, 2);
                        List<String> countiesFIPS = (List<String>) municipalityJson.requireArray("counties");
                        for (String countyFIPS : countiesFIPS) {
                            String countyStateFIPS = countyFIPS.substring(0, 2);
                            if (!countyStateFIPS.equals(stateFips)) {
                                System.out.println("County FIPS does not match municipality FIPS: " + municipalityFIPS + ", " + countyFIPS);
                            }
                        }
                        Municipality m = new Municipality(engine, municipalityJson);
                        municipalities.add(m);
                        if (countiesFIPS.isEmpty()) {
                            System.out.println("Municipality " + municipalityJson.find("name") + " has no counties");
                        }
                        countiesFIPS.forEach(countyFIPS -> countiesMunicipalities.computeIfAbsent(countyFIPS, k -> new HashSet<>()).add(m));
                    }
                }
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void createCounties(@NotNull Set<Path> countyDataPaths) {
        try {
            statesCounties.clear();
            for (Path countyDataPath : countyDataPaths) {
                JSONObject countyJson = new JSONObject(countyDataPath);
                County c = new County(engine, countyJson);
                counties.add(c);
                String stateName = countyJson.requireString("state");
                statesCounties.computeIfAbsent(stateName, k -> new HashSet<>()).add(c);
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void resolveCountiesMunicipalities() {
        for (Map.Entry<String, Set<Municipality>> entry : countiesMunicipalities.entrySet()) {
            Optional<County> county = matchCounty(entry.getKey());
            if (county.isEmpty()) {
                System.out.println("County with FIPS = " + entry.getKey() + " could not be matched.");
                continue;
            }
            entry.getValue().forEach(municipality -> {
                county.get().addMunicipality$core(municipality);
                municipality.addCounty$core(county.get());
            });
        }
    }

    private void createStates(@NotNull Set<Path> statesDataPaths) {
        try {
            for (Path statesDataPath : statesDataPaths) {
                JSONObject stateJson = new JSONObject(statesDataPath);
                states.add(new State(engine, stateJson));
                statesDivisions.put(stateJson.requireString("fullName"), stateJson.requireString("censusDivision"));
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void resolveStatesCounties() {
        for (Map.Entry<String, Set<County>> entry : statesCounties.entrySet()) {
            Optional<State> state = matchState(entry.getKey());
            if (state.isEmpty()) {
                System.out.println("State " + entry.getKey() + " could not be matched.");
                continue;
            }
            entry.getValue().forEach(county -> {
                county.setState$core(state.get());
                state.get().addCounty$core(county);
            });
        }
    }

    private void createNation(@NotNull Path nationDataPath) {
        Nation.INSTANCE.setStates$core(states);
    }

    private void createCongressionalDistricts(@NotNull Set<Path> congressionalDistrictsDataPaths) {
        try {
            for (Path congressionalDistrictDataPath : congressionalDistrictsDataPaths) {
                JSONObject districtJson = new JSONObject(congressionalDistrictDataPath);
                congressionalDistricts.add(new CongressionalDistrict(engine, districtJson));
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void createCensusRegionsDivisions(@NotNull Path censusRegionsDivisionsDataPath) {
        try {
            JSONObject censusRegionsDivisionsJson = new JSONObject(censusRegionsDivisionsDataPath);
            for (Object regionObj : censusRegionsDivisionsJson.requireArray()) {
                if (regionObj instanceof JSONObject regionJson) {
                    censusRegions.add(new CensusRegion(engine, regionJson)); // Also builds Census Divisions
                }
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void resolveStatesDivisions() {
        for (Map.Entry<String, String> entry : statesDivisions.entrySet()) {
            if (entry.getValue() == null) continue; // No division
            Optional<State> state = matchState(entry.getKey());
            if (state.isEmpty()) {
                System.out.println("State " + entry.getKey() + " not found.");
                continue;
            }
            Optional<CensusDivision> censusDivision = matchCensusDivision(entry.getValue());
            if (censusDivision.isEmpty()) {
                System.out.println("Division " + entry.getValue() + " not found for state " + state.get().getName());
                continue;
            }
            state.get().setCensusDivision$core(censusDivision.get());
            censusDivision.get().addState(state.get());
        }
    }

    private boolean verifyInitialization() {
        boolean successFlag = true;
        for (Municipality municipality : municipalities) {
            try {
                municipality.getCounties();
                municipality.getState();
            }
            catch (Exception e) {
                System.out.println("Municipality " + municipality.getName() + " (" + municipality.getFIPS() + ") could not be verified.");
                successFlag = false;
            }
        }
        for (County county : counties) {
            try {
                county.getMunicipalities();
                county.getCapital();
                county.getState();
            }
            catch (Exception e) {
                System.out.println("County " + county.getName() + " (" + county.getFIPS() + ") could not be verified.");
                successFlag = false;
            }
        }
        for (State state : states) {
            try {
                state.getMunicipalities();
                state.getCounties();
                state.getCapital();
                state.getCensusDivision();
            }
            catch (Exception e) {
                System.out.println("State " + state.getName() + " (" + state.getFIPS() + ") could not be verified.");
                successFlag = false;
            }
        }
        for (CensusDivision division : censusDivisions) {
            try {
                division.getStates();
                division.getCensusRegion();
            }
            catch (Exception e) {
                System.out.println("Census Division " + division.getName() + " could not be verified." );
                successFlag = false;
            }
        }
        for (CensusRegion region : censusRegions) {
            try {
                region.getCensusDivisions();
                region.getStates();
            }
            catch (NoSuchElementException e) {
                System.out.println("Census Region " + region.getName() + " could not be verified.");
                successFlag = false;
            }
        }
        return successFlag;
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

    public @NotNull Optional<CongressionalDistrict> matchCongressionalDistrict(String name) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        return congressionalDistricts.stream().filter(district -> district.getName().equals(name)).findFirst();
    }

    public @NotNull Set<County> getCounties() {
        requireOperational();
        return counties;
    }

    public @NotNull Set<Municipality> getMunicipalities() {
        requireOperational();
        return municipalities;
    }

    private final Map<String, Municipality> fipsToMunicipality = new HashMap<>();
    private final Map<String, Municipality> fullNameToMunicipality = new HashMap<>();
    private final Map<String, Municipality> commonNameToMunicipality = new HashMap<>();
    private final Map<String, Municipality> qualifiedNameToMunicipality = new HashMap<>();
    private final Map<String, Municipality> commonNameAndStateCommonNameToMunicipality = new HashMap<>();
    private final Map<String, Municipality> commonNameAndStateAbbreviationToMunicipality = new HashMap<>();
    public @NotNull Optional<Municipality> matchMunicipality(@NotNull String name) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        if (!selfIsInitialized) {
            return Optional.ofNullable(
                municipalities.stream().filter(municipality -> municipality.getFIPS().equals(name)).findFirst().orElse(
                municipalities.stream().filter(municipality -> municipality.getFullName().equals(name)).findFirst().orElse(
                municipalities.stream().filter(municipality -> municipality.getCommonName().equals(name)).findFirst().orElse(null)
            )));
        }
        if (fipsToMunicipality.isEmpty()) buildMunicipalityMatchingIndices();
        return Optional.ofNullable(
            fipsToMunicipality.getOrDefault(name,
            fullNameToMunicipality.getOrDefault(name,
            commonNameToMunicipality.getOrDefault(name,
            qualifiedNameToMunicipality.getOrDefault(name,
            commonNameAndStateCommonNameToMunicipality.getOrDefault(name,
            commonNameAndStateAbbreviationToMunicipality.getOrDefault(name,
        null)))))));
    }
    private void buildMunicipalityMatchingIndices() {
        municipalities.forEach(municipality -> {
            fipsToMunicipality.put(municipality.getFIPS(), municipality);
            fullNameToMunicipality.put(municipality.getFullName(), municipality);
            commonNameToMunicipality.put(municipality.getCommonName(), municipality);
            qualifiedNameToMunicipality.put(municipality.getQualifiedName(), municipality);
            commonNameAndStateCommonNameToMunicipality.put(String.format("%s, %s", municipality.getCommonName(), municipality.getState().getCommonName()), municipality);
            commonNameAndStateAbbreviationToMunicipality.put(String.format("%s, %s", municipality.getCommonName(), municipality.getState().getAbbreviation()), municipality);
        });
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
        try {
            JSONObject json = new JSONObject(FilePaths.NATION);
            nation.fromJson(json);
        } catch (IOException e) {
            onError(e);
        }
    }

    public @NotNull Optional<University> matchUniversity(@NotNull String name) {
        return universities.stream().filter(university -> university.getCommonName().equals(name)).findFirst();
    }

    public @NotNull Optional<County> matchCounty(String name) {
        return Optional.ofNullable(
            counties.stream().filter(county -> county.getFIPS().equals(name)).findFirst().orElse(
            counties.stream().filter(county -> county.getFullName().equals(name)).findFirst().orElse(
            counties.stream().filter(county -> county.getCommonName().equals(name)).findFirst().orElse(null)
        )));
    }

    public @NotNull Optional<Descriptor> matchDescriptor(String name) {
        return descriptors.stream().filter(descriptor -> descriptor.getName().equals(name)).findFirst();
    }

    public @NotNull Optional<State> matchState(String name) {
        return Optional.ofNullable(
            states.stream().filter(state -> state.getFIPS().equals(name)).findFirst().orElse(
            states.stream().filter(state -> state.getFullName().equals(name)).findFirst().orElse(
            states.stream().filter(state -> state.getCommonName().equals(name)).findFirst().orElse(null)
        )));
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

    public @NotNull Optional<? extends MapEntity> matchMapEntity(@NotNull String name) {
        return Stream.of(
            matchCensusRegion(name),
            matchCensusDivision(name),
            matchState(name),
            matchCongressionalDistrict(name),
            matchCounty(name),
            matchMunicipality(name)
        ).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }
}
