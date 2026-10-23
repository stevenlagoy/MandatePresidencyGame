package com.stevenlagoy.presidency.map;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.map.entities.*;
import com.stevenlagoy.presidency.map.hydrology.HydrologicalFeature;
import com.stevenlagoy.presidency.map.physiography.PhysiographicalRegion;
import com.stevenlagoy.presidency.map.physiography.TopographyBathymetry;
import com.stevenlagoy.presidency.map.travel.route.RouteManager;
import com.stevenlagoy.presidency.politics.government.Government;
import com.stevenlagoy.presidency.util.FilePath;
import com.stevenlagoy.presidency.util.IOUtils;
import com.stevenlagoy.presidency.util.MatchingException;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.stevenlagoy.presidency.util.StringUtils.truncate;

/**
 * <h1>MAP MANAGER</h1>
 * {@code ~/map/MapManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy      <br>
 *     <b>Created: </b> 10 December 2024  <br>
 *     <b>Modified:</b> 08 September 2026 <br>
 * </p>
 *
 * MapManager is responsible for creating and tracking information about {@link MapEntity}
 * instances, including the {@link Nation}, {@link StateEquivalent}s, {@link CountyEquivalent}s,
 * {@link CountySubdivision}s, and {@link Place}s.
 *
 * @author Steven LaGoy
 */
public class MapManager2 extends Manager {

    // Constants

    private final String federalDistrictFIPS = "11";

    private final String congressionalDistrictsFileMarker = "_congressional_districts";

    // Instance Fields

    // Submanagers
    public final RouteManager ROUTE_MANAGER;

    // Objects
    public @NotNull Nation nation;
    private final @NotNull Set<CensusRegion> censusRegions;
    private final @NotNull Set<CensusDivision> censusDivisions;
    private final @NotNull Set<StateEquivalent> stateEquivalents;
    private final @NotNull Set<CongressionalDistrict> congressionalDistricts;
    private final @NotNull Set<CountyEquivalent> countyEquivalents;
    private final @NotNull Set<CountySubdivision> countySubdivisions;
    private final @NotNull Set<Place> places;
    private final @NotNull Set<HistoricalProvince> historicalProvinces;
    private final @NotNull Set<HydrologicalFeature> hydrologicalFeatures;
    private final @NotNull Set<PhysiographicalRegion> physiographicalRegions;
    private final @NotNull Set<TimeZoneRegion> timeZoneRegions;
    private final @NotNull Set<University> universities;
    private final @NotNull Set<Descriptor> descriptors;

    private final @NotNull Map<String, StateEquivalent> stateEquivalentsFIPS;
    private final @NotNull Map<String, CountyEquivalent> countyEquivalentsFIPS;

    // Map Layers
    private MapLayer congressionalDistrictsLayer;
    private MapLayer countySubdivisionsLayer;
    private MapLayer historicalProvincesLayer;
    private MapLayer hydrologyLayer;
    private MapLayer physiographyLayer;
    private MapLayer precipitationLayer;
    private MapLayer timeZonesLayer;
    private MapLayer topographyBathymetryLayer;
    private MapLayer placesLayer;
    private Set<MapLayer> allLayers;

    // Map Layer Indices
    private final @NotNull Map<MapRegion, CongressionalDistrict> congressionalDistrictsIndex;
    private final @NotNull Map<MapRegion, CountySubdivision> countySubdivisionsIndex;
    private final @NotNull Map<MapRegion, HistoricalProvince> historicalProvincesIndex;
    private final @NotNull Map<MapRegion, HydrologicalFeature> hydrologicalFeaturesIndex;
    private final @NotNull Map<MapRegion, PhysiographicalRegion> physiographicalRegionsIndex;
    private final @NotNull Map<MapRegion, TimeZoneRegion> timeZoneRegionsIndex;
    private final @NotNull Map<MapRegion, Place> placesIndex;

    // Initialization helper variables
    private String nationalCapital;
    private Map<StateEquivalent, String> stateEquivalentsCapitals;
    private Map<County, String> countySeats;

    // Constructors

    public MapManager2(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        ROUTE_MANAGER = new RouteManager(engine, this);
        nation = Nation.INSTANCE;
        censusRegions = new HashSet<>();
        censusDivisions = new HashSet<>();
        stateEquivalents = new HashSet<>();
        congressionalDistricts = new HashSet<>();
        countyEquivalents = new HashSet<>();
        countySubdivisions = new HashSet<>();
        places = new HashSet<>();
        historicalProvinces = new HashSet<>();
        hydrologicalFeatures = new HashSet<>();
        physiographicalRegions = new HashSet<>();
        timeZoneRegions = new HashSet<>();
        universities = new HashSet<>();
        descriptors = new HashSet<>();
        stateEquivalentsFIPS = new HashMap<>();
        countyEquivalentsFIPS = new HashMap<>();
        congressionalDistrictsIndex = new HashMap<>();
        countySubdivisionsIndex = new HashMap<>();
        historicalProvincesIndex = new HashMap<>();
        hydrologicalFeaturesIndex = new HashMap<>();
        physiographicalRegionsIndex = new HashMap<>();
        timeZoneRegionsIndex = new HashMap<>();
        placesIndex = new HashMap<>();
    }

    // Manager Methods

    @Override @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of(ROUTE_MANAGER);
    }

    @Override
    protected void doInit() {
        // Load the layers for realspace maps
        loadMapLayers();
        // Gather relevant files for creating MapEntities
        Map<String, Set<Path>> statesDataPaths = categorizeStatesDataPaths(getAllStatesDataPaths());
        // Creation methods establish upwards links and inverse relationships: states' counties and counties' states
        createNation(FilePath.NATION.path);
        createStateEquivalents(statesDataPaths.get(stateEquivalentFileMarker));
        createCountyEquivalents(statesDataPaths.get(countyEquivalentsFileMarker));
        createCountySubdivisions(statesDataPaths.get(countySubdivisionsFileMarker));
        createPlaces(statesDataPaths.get(placesFileMarker));
        // Finishing methods establish downwards links: state capitals, county seats, etc
        finishPlaces();
        finishCountySubdivisions();
        finishCountyEquivalents();
        finishStateEquivalents();
        finishNation();
        // Verifying methods check that map entities are initialized with all their data
        verifyNation();
        verifyStateEquivalents();
        verifyCountyEquivalents();
        verifyCountySubdivisions();
        verifyPlaces();
        // Build map indices to allow fast lookup of geo points
        buildCongressionalDistrictsIndex();
        buildCountySubdivisionsIndex();
        buildHistoricalProvincesIndex();
        buildHydrologicalFeaturesIndex();
        buildPhysiographicalRegionsIndex();
        buildTimeZonesIndex();

    }

    @Override
    protected void doCleanup() {
        nation = Nation.INSTANCE;
        Stream.of(
            censusRegions,
            censusDivisions,
            stateEquivalents,
            congressionalDistricts,
            countyEquivalents,
            countySubdivisions,
            places,
            historicalProvinces,
            hydrologicalFeatures,
            physiographicalRegions,
            timeZoneRegions,
            universities,
            descriptors,
            congressionalDistrictsIndex,
            countySubdivisionsIndex,
            historicalProvincesIndex,
            hydrologicalFeaturesIndex,
            physiographicalRegionsIndex,
            timeZoneRegionsIndex
        ).forEach(collection -> ((Collection<?>) collection).clear());
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName(), List.of(
            new JSONObject(),
            new JSONObject("nation", nation.toJson()),
            new JSONObject("censusRegions", censusRegions.stream().map(CensusRegion::toJson).collect(Collectors.toList())),
            new JSONObject("censusDivisions", censusDivisions.stream().map(CensusDivision::toJson).collect(Collectors.toList())),
            new JSONObject("stateEquivalents", stateEquivalents.stream().map(StateEquivalent::toJson).collect(Collectors.toList())),
            new JSONObject("congressionalDistricts", congressionalDistricts.stream().map(CongressionalDistrict::toJson).collect(Collectors.toList())),
            new JSONObject("countyEquivalents", countyEquivalents.stream().map(CountyEquivalent::toJson).collect(Collectors.toList())),
            new JSONObject("countySubdivisions", countySubdivisions.stream().map(CountySubdivision::toJson).collect(Collectors.toList())),
            new JSONObject("places", places.stream().map(Place::toJson).collect(Collectors.toList())),
            new JSONObject("historicalProvinces", historicalProvinces.stream().map(HistoricalProvince::toJson).collect(Collectors.toList())),
            new JSONObject("hydrologicalFeatures", hydrologicalFeatures.stream().map(HydrologicalFeature::toJson).collect(Collectors.toList())),
            new JSONObject("physiographicalRegions", physiographicalRegions.stream().map(PhysiographicalRegion::toJson).collect(Collectors.toList())),
            new JSONObject("timeZoneRegions", timeZoneRegions.stream().map(TimeZoneRegion::toJson).collect(Collectors.toList())),
            new JSONObject("universities", new ArrayList<>()),
            new JSONObject("descriptors", descriptors.stream().map(Descriptor::toJson).collect(Collectors.toList()))
            // It might be possible to just save data for atomic map entites, because we can reconstitute upwards
        ));
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        json.requireJson("censusRegions", "census_regions").requireArray().forEach(censusRegion -> censusRegions.add(new CensusRegion(engine, (JSONObject) censusRegion)));
        json.requireJson("censusDivisions", "census_divisions").requireArray().forEach(censusDivision -> censusDivisions.add(new CensusDivision(engine, (JSONObject) censusDivision)));
        json.requireJson("stateEquivalents", "state_equivalents").requireArray().forEach(stateEquivalent -> stateEquivalents.add(new StateEquivalent(engine, (JSONObject) stateEquivalent)));
        // We might reinitialize from files and then use the stored data to set dynamic variables (like population and politics)
    }

    // Instance Methods

    public @NotNull Nation getNation() {
        return nation;
    }

    public @NotNull Set<CensusRegion> getCensusRegions() {
        return censusRegions;
    }

    public @NotNull Set<CensusDivision> getCensusDivisions() {
        return censusDivisions;
    }

    public @NotNull Set<StateEquivalent> getStateEquivalents() {
        return stateEquivalents;
    }

    public @NotNull StateEquivalent selectStateEquivalent(@NotNull Demographics demographics) {
        return (StateEquivalent) selectMapEntity(demographics, stateEquivalents);
    }

    public @NotNull Set<CongressionalDistrict> getCongressionalDistricts() {
        return congressionalDistricts;
    }

    public @NotNull Set<CountyEquivalent> getCountyEquivalents() {
        return countyEquivalents;
    }

    public @NotNull Set<CountySubdivision> getCountySubdivisions() {
        return countySubdivisions;
    }

    public @NotNull CountySubdivision selectCountySubdivision(@NotNull Demographics demographics) {
        return (CountySubdivision) selectMapEntity(demographics, countySubdivisions);
    }

    public @NotNull Set<Place> getPlaces() {
        return places;
    }

    public @NotNull Place getMostPopulousPlace() throws MatchingException {
        return places.stream().max(Comparator.comparingInt(Place::getPopulation)).orElseThrow(() -> new MatchingException("No places were present", this::getMostPopulousPlace));
    }

    public @NotNull Place selectPlace(@NotNull Demographics demographics) {
        return (Place) selectMapEntity(demographics, places);
    }

    public @NotNull Set<HistoricalProvince> getHistoricalProvinces() {
        return historicalProvinces;
    }

    public @NotNull Set<HydrologicalFeature> getHydrologicalFeatures() {
        return hydrologicalFeatures;
    }

    public @NotNull Set<PhysiographicalRegion> getPhysiographicalRegions() {
        return physiographicalRegions;
    }

    public @NotNull Set<TimeZoneRegion> getTimeZoneRegions() {
        return timeZoneRegions;
    }

    public @NotNull Set<University> getUniversities() {
        return universities;
    }

    public @NotNull Set<Descriptor> getDescriptors() {
        return descriptors;
    }

    public @NotNull MapLayer getCongressionalDistrictsLayer() {
        return congressionalDistrictsLayer;
    }

    public @NotNull MapLayer getCountySubdivisionsLayer() {
        return countySubdivisionsLayer;
    }

    public @NotNull MapLayer getHydrologyLayer() {
        return hydrologyLayer;
    }

    public @NotNull MapLayer getPhysiographyLayer() {
        return physiographyLayer;
    }

    public @NotNull MapLayer getPrecipitationLayer() {
        return precipitationLayer;
    }

    public @NotNull MapLayer getHistoricalProvincesLayer() {
        return historicalProvincesLayer;
    }

    public @NotNull MapLayer getTimeZonesLayer() {
        return timeZonesLayer;
    }

    public @NotNull MapLayer getTopographyBathymetryLayer() {
        return topographyBathymetryLayer;
    }

    public @NotNull MapEntity selectMapEntity(@NotNull Demographics demographics, @NotNull Collection<? extends MapEntity> selectables) {
        Map<MapEntity, Double> weightedMapEntities = new HashMap<>();
        for (MapEntity mapEntity : selectables) {
            double mapEntityWeight = 0.0;
            for (Bloc bloc : demographics.getBlocs()) {
                mapEntityWeight += mapEntity.getDemographicPopulation(bloc);
            }
            weightedMapEntities.put(mapEntity, mapEntityWeight);
        }
        return Objects.requireNonNull(RandomUtils.weightedSelect(weightedMapEntities));
    }

    // Files

    private void loadMapLayers() {
        congressionalDistrictsLayer = MapLayer.Companion.load(FilePath.CONGRESSIONAL_DISTRICTS_MAP.toFile());
        countySubdivisionsLayer     = MapLayer.Companion.load(FilePath.COUNTY_SUBDIVISIONS_MAP.toFile());
        hydrologyLayer              = MapLayer.Companion.load(FilePath.HYDROLOGY_MAP.toFile());
        physiographyLayer           = MapLayer.Companion.load(FilePath.PHYSIOGRAPHY_MAP.toFile());
        precipitationLayer          = MapLayer.Companion.load(FilePath.PRECIPITATION_MAP.toFile());
        historicalProvincesLayer    = MapLayer.Companion.load(FilePath.HISTORICAL_PROVINCES_MAP.toFile());
        timeZonesLayer              = MapLayer.Companion.load(FilePath.TIMEZONES_MAP.toFile());
        topographyBathymetryLayer   = MapLayer.Companion.load(FilePath.TOPOGRAPHY_BATHYMETRY_MAP.toFile());
        allLayers = Set.of(
            congressionalDistrictsLayer, countySubdivisionsLayer, hydrologyLayer,
            physiographyLayer, precipitationLayer, historicalProvincesLayer, timeZonesLayer,
            topographyBathymetryLayer, placesLayer
        );
    }

    private void buildCongressionalDistrictsIndex() {
        for (CongressionalDistrict congressionalDistrict : congressionalDistricts) {
            congressionalDistrictsIndex.put(congressionalDistrict.getRegion(), congressionalDistrict);
        }
    }

    private void buildCountySubdivisionsIndex() {
        for (CountySubdivision countySubdivision : countySubdivisions) {
            countySubdivisionsIndex.put(countySubdivision.getRegion(), countySubdivision);
        }
    }

    private void buildHistoricalProvincesIndex() {
        for (HistoricalProvince historicalProvince : historicalProvinces) {
            historicalProvincesIndex.put(historicalProvince.getRegion(), historicalProvince);
        }
    }

    private void buildHydrologicalFeaturesIndex() {
        for (HydrologicalFeature hydrologicalFeature : hydrologicalFeatures) {
            hydrologicalFeaturesIndex.put(hydrologicalFeature.getRegion(), hydrologicalFeature);
        }
    }

    private void buildPhysiographicalRegionsIndex() {
        for (PhysiographicalRegion physiographicalRegion : physiographicalRegions) {
            physiographicalRegionsIndex.put(physiographicalRegion.getRegion(), physiographicalRegion);
        }
    }

    private void buildTimeZonesIndex() {
        for (TimeZoneRegion timeZoneRegion : timeZoneRegions) {
            timeZoneRegionsIndex.put(timeZoneRegion.getRegion(), timeZoneRegion);
        }
    }

    private void loadDescriptors() {
        try {
            JSONObject descriptorsJson = new JSONObject(FilePath.DESCRIPTORS.path);
            descriptorsJson.requireArray().forEach(descriptor -> descriptors.add(new Descriptor(engine, (JSONObject) descriptor)));
        }
        catch (IOException e) {
            onDegraded(e);
        }
    }

    private @NotNull Set<Path> getAllStatesDataPaths() {
        Set<Path> statesFiles = new HashSet<>();
        try {
            Set<Path> statesDirectories = IOUtils.listDirectories(FilePath._STATES);
            for (Path stateDir : statesDirectories) {
                statesFiles.addAll(IOUtils.listFiles(stateDir));
            }
        }
        catch (IOException e) {
            onDegraded(e);
        }
        return statesFiles;
    }

    private @NotNull Map<String, Set<Path>> categorizeStatesDataPaths(@NotNull Set<Path> statesDataPaths) {
        Function<String, Set<Path>> filterBy = (String marker) -> statesDataPaths.stream().filter(path -> path.toString().contains(marker)).collect(Collectors.toSet());
        return Map.of(
            stateEquivalentFileMarker, filterBy.apply(stateEquivalentFileMarker),
            countyEquivalentsFileMarker, filterBy.apply(countyEquivalentsFileMarker),
            countySubdivisionsFileMarker, filterBy.apply(countySubdivisionsFileMarker),
            placesFileMarker, filterBy.apply(placesFileMarker)
        );
    }

    // Creation

    private void createNation(@NotNull Path nationFile) {
        try {
            nation.fromJson(new JSONObject(nationFile));
        }
        catch (IOException e) {
            onDegraded(e);
        }
    }

    private void createCensusRegionsDivisions(@NotNull Path censusRegionsDivisionsFile) {
        try {
            JSONObject regionsDivisionsJson = new JSONObject(censusRegionsDivisionsFile);
            for (Object regionObj : regionsDivisionsJson.requireArray()) {
                if (regionObj instanceof JSONObject regionJson) {
                    CensusRegion region = new CensusRegion(engine, regionJson);
                    censusRegions.add(region);
                    censusDivisions.addAll(region.getCensusDivisions());
                }
            }
        }
        catch (IOException e) {
            onDegraded(e);
        }
    }

    private void createStateEquivalents(@NotNull Set<Path> stateEquivalentFiles) {
        for (Path stateEquivalentFile : stateEquivalentFiles) {
            try {
                JSONObject stateEquivalentJson = new JSONObject(stateEquivalentFile);
                String type = stateEquivalentJson.requireString("type").toLowerCase().replaceAll("[^a-z]", " ");
                // switch-case didn't work here because enum values are considered non-const
                if (type.equals(StateEquivalent.StateType.FEDERAL_DISTRICT.getLabel())) {
                    FederalDistrict.INSTANCE.fromJson(stateEquivalentJson);
                    stateEquivalents.add(FederalDistrict.INSTANCE);
                    stateEquivalentsFIPS.put(FederalDistrict.INSTANCE.getFIPS(), FederalDistrict.INSTANCE);
                }
                else {
                    StateEquivalent stateEquivalent = new StateEquivalent(engine, stateEquivalentJson);
                    stateEquivalents.add(stateEquivalent);
                    stateEquivalentsFIPS.put(stateEquivalent.getFIPS(), stateEquivalent);
                }
            }
            catch (IOException e) {
                onDegraded(e);
            }
        }
    }

    private final Map<String, JSONObject> independentCityJsons = new  HashMap<>();
    private void createCountyEquivalents(@NotNull Set<Path> countyEquivalentsFiles) {
        for (Path countyEquivalentsFile : countyEquivalentsFiles) {
            try {

            }
            catch (IOException e) {
                onDegraded(e);
            }
        }
    }

    private void createCountySubdivisions(@NotNull Set<Path> countySubdivisionsFiles) {
        for (Path countySubdivisionsFile : countySubdivisionsFiles) {
            try {
                JSONObject countySubdivisionsJson = new JSONObject(countySubdivisionsFile);
                String stateFIPS = countySubdivisionsFile.getFileName().toString().substring(0, 2);
                Optional<StateEquivalent> state = matchStateEquivalent(stateFIPS);
                if (state.isEmpty()) continue;
                StateEquivalent.SubdivisionScheme stateSubdivisionScheme = state.get().getSubdivisionScheme();
                for (Object subdivisionObj : countySubdivisionsJson.requireArray()) {
                    if (subdivisionObj instanceof JSONObject subdivisionJson) {
                        @NotNull String name = subdivisionJson.findString("name").orElse("");
                        @NotNull CountyEquivalent countyEquivalent = requireCountyEquivalent(subdivisionJson.findString("countyEquivalent").orElse(""));
                        switch (stateSubdivisionScheme) {
                            case MCD :
                                SubdivisionType type = Objects.requireNonNull(SubdivisionTypes.INSTANCE.fromString(subdivisionJson.requireString("type")));
                                Government government = subdivisionJson.hasKey("government") ? new Government(engine, subdivisionJson.requireJson("government")) : null;
                                countySubdivisions.add(new CountySubdivision.MinorCivilDivision(engine, name, countyEquivalent, type, government));
                                break;
                            case CCD :
                                countySubdivisions.add(new CountySubdivision.CensusCountyDivision(engine, name, countyEquivalent));
                                break;
                        }
                    }
                }
            }
            catch (IOException e) {
                onDegraded(e);
            }
        }
    }

    private void createPlaces(@NotNull Set<Path> placeFiles) {
        for (Path placeFile : placeFiles) {
            try {
                JSONObject placesJson = new JSONObject(placeFile);
                for (Object placeObj : placesJson.requireArray()) {
                    if (placeObj instanceof JSONObject placeJson) {
                        String placeName = String.format("%s, %s", placeJson.requireString("fullName", "commonName", "name"), placeJson.requireString("state", "territory"));
                        if (independentCityJsons.containsKey(placeName)) {
                            JSONObject merged = independentCityJsons.get(placeName);
                            for (Object component : placeJson.requireArray()) {
                                if (component instanceof JSONObject componentJson) {
                                    merged.merge(componentJson);
                                }
                            }
                            IndependentCity independentCity = new IndependentCity(engine, merged);
                            countyEquivalents.add(independentCity);
                            countyEquivalentsFIPS.put(independentCity.getFIPS(), independentCity);
                            places.add(independentCity);
                            independentCityJsons.remove(placeName);
                        }
                        else {
                            places.add(new Place(engine, placeJson));
                        }
                    }
                    else throw new IllegalArgumentException(String.format("The place data file %s contained an invalid entry: %s", placeFile.getFileName(), truncate(placeObj, 20)));
                }
                // Check that all the independent cities we found in the county equivalent step were loaded here
                if (!independentCityJsons.isEmpty()) {
                    throw new IllegalStateException("There were " + independentCityJsons.size() + " independent cities identified from county equivalents files which were not present in places files: " + independentCityJsons);
                }
            }
            catch (IOException e) {
                onDegraded(e);
            }
        }
    }

    private void finishPlaces() {
    }

    private void finishCountySubdivisions() {
        for (Place place : places) {
            for (CountySubdivision countySubdivision : place.getCountySubdivisions()) {
                countySubdivision.getPlaces().add(place);
            }
        }
    }

    private void finishCountyEquivalents() {
        countySeats.forEach((county, countySeatName) -> {
            if (countySeatName == null || countySeatName.isEmpty()) return;
            county.setCountySeat$core(requirePlace(countySeatName));
        });
        for (CountySubdivision countySubdivision : countySubdivisions) {
            countySubdivision.getCountyEquivalent().getCountySubdivisions().add(countySubdivision);
        }
        for (Place place : places) {
            for (CountyEquivalent countyEquivalent : place.getCountyEquivalents()) {
                countyEquivalent.getPlaces().add(place);
            }
        }
    }

    private void finishStateEquivalents() {
        stateEquivalentsCapitals.forEach((stateEquivalent, capitalName) -> {
            if (capitalName == null || capitalName.isBlank()) return;
            stateEquivalent.setCapital(PLACE_MANAGER.requirePlace(capitalName));
        });
        for (CountyEquivalent countyEquivalent : countyEquivalents) {
            countyEquivalent.getStateEquivalent().addCountyEquivalent$core(countyEquivalent);
        }
    }

    private void finishNation() {
        nation.setCapital(PLACE_MANAGER.requirePlace(nationalCapital));
        nation.setStateEquivalents$core(stateEquivalents);
    }

    private void verifyNation() throws IllegalStateException {
        nation.toJson();
    }

    private void verifyStateEquivalents() throws IllegalStateException {
        stateEquivalents.forEach(StateEquivalent::toJson);
        assert stateEquivalentsFIPS.size() == stateEquivalents.size();
    }

    private void verifyCountyEquivalents() throws IllegalStateException {
        countyEquivalents.forEach(CountyEquivalent::toJson);
        assert countyEquivalentsFIPS.size() == countyEquivalents.size();
    }

    private void verifyCountySubdivisions() throws IllegalStateException {
        countySubdivisions.forEach(CountySubdivision::toJson);
    }

    private void verifyPlaces() throws IllegalStateException {
        places.forEach(Place::toJson);
    }

    // Query

    public @NotNull Set<MapRegion> regionsAt(int mapX, int mapY) {
        return allLayers.stream()
            .map(layer -> layer.regionAt(mapX, mapY))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    public @Nullable CountySubdivision countySubdivisionAt(int mapX, int mapY) {
        return countySubdivisionsIndex.get(countySubdivisionsLayer.regionAt(mapX, mapY));
    }

    public @Nullable CountyEquivalent countyEquivalentAt(int mapX, int mapY) {
        CountySubdivision couSub = countySubdivisionAt(mapX, mapY);
        if (couSub != null) return couSub.getCountyEquivalent();
        return null;
    }

    public @Nullable StateEquivalent stateEquivalentAt(int mapX, int mapY) {
        CountySubdivision couSub = countySubdivisionAt(mapX, mapY);
        if (couSub != null) return couSub.getStateEquivalent();
        return null;
    }

    public @Nullable CongressionalDistrict congressionalDistrictAt(int mapX, int mapY) {
        return congressionalDistrictsIndex.get(congressionalDistrictsLayer.regionAt(mapX, mapY));
    }

    public @Nullable CensusDivision censusDivisionAt(int mapX, int mapY) {
        CountySubdivision couSub = countySubdivisionAt(mapX, mapY);
        if (couSub != null) return couSub.getCensusDivision();
        return null;
    }

    public @Nullable CensusRegion censusRegionAt(int mapX, int mapY) {
        CountySubdivision couSub = countySubdivisionAt(mapX, mapY);
        if (couSub != null) return couSub.getCensusRegion();
        return null;
    }

    public @Nullable HistoricalProvince historicalProvinceAt(int mapX, int mapY) {
        return historicalProvincesIndex.get(historicalProvincesLayer.regionAt(mapX, mapY));
    }

    public @Nullable HydrologicalFeature hydrologicalFeatureAt(int mapX, int mapY) {
        return hydrologicalFeaturesIndex.get(hydrologyLayer.regionAt(mapX, mapY));
    }

    public @Nullable PhysiographicalRegion physiographicalRegionAt(int mapX, int mapY) {
        return physiographicalRegionsIndex.get(physiographyLayer.regionAt(mapX, mapY));
    }

    public @Nullable TimeZoneRegion timeZoneRegionAt(int mapX, int mapY) {
        return timeZoneRegionsIndex.get(timeZonesLayer.regionAt(mapX, mapY));
    }

    public @Nullable TopographyBathymetry topographicBathymetricTypeAt(int mapX, int mapY) {
        MapRegion region = topographyBathymetryLayer.regionAt(mapX, mapY);
        if (region == null) return null;
        return new TopographyBathymetry(region.getColor());
    }

    public @Nullable Place placeAt(int mapX, int mapY) {
        return placesIndex.get(placesLayer.regionAt(mapX, mapY));
    }

}
