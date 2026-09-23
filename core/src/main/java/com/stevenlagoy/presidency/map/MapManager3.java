package com.stevenlagoy.presidency.map;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.entities.*;
import com.stevenlagoy.presidency.map.hydrology.HydrologicalFeature;
import com.stevenlagoy.presidency.map.hydrology.HydrologicalFeatureManager;
import com.stevenlagoy.presidency.map.physiography.PhysiographicalRegion;
import com.stevenlagoy.presidency.map.physiography.PhysiographicalRegionManager;
import com.stevenlagoy.presidency.map.physiography.TopographyBathymetry;
import com.stevenlagoy.presidency.map.travel.route.RouteManager;
import com.stevenlagoy.presidency.util.FilePath;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>MAP MANAGER</h1>
 * {@code ~/map/MapManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy      <br>
 *     <b>Created: </b> 10 December 2024  <br>
 *     <b>Modified:</b> 11 September 2026 <br>
 * </p>
 *
 * MapManager is responsible for creating and tracking information about {@link MapEntity}
 * instances, including the {@link Nation}, {@link StateEquivalent}s, {@link CountyEquivalent}s,
 * {@link CountySubdivision}s, and {@link Place}s.
 *
 * @author Steven LaGoy
 */

public class MapManager3 extends Manager {

    // Instance Fields

    // Submanagers
    private final RouteManager ROUTE_MANAGER;
    private final CensusRegionManager CENSUS_REGION_MANAGER;
    private final CensusDivisionManager CENSUS_DIVISION_MANAGER;
    private final StateEquivalentManager STATE_EQUIVALENT_MANAGER;
    private final CongressionalDistrictManager CONGRESSIONAL_DISTRICT_MANAGER;
    private final CountyEquivalentManager COUNTY_EQUIVALENT_MANAGER;
    private final CountySubdivisionManager COUNTY_SUBDIVISION_MANAGER;
    private final PlaceManager PLACE_MANAGER;
    private final HistoricalProvinceManager HISTORICAL_PROVINCE_MANAGER;
    private final HydrologicalFeatureManager HYDROLOGICAL_FEATURE_MANAGER;
    private final PhysiographicalRegionManager PHYSIOGRAPHICAL_REGION_MANAGER;
    private final TimeZoneRegionManager TIME_ZONE_REGION_MANAGER;
    private final UniversityManager UNIVERSITY_MANAGER;
    private final DescriptorManager DESCRIPTOR_MANAGER;

    // Objects
    private final @NotNull Nation nation;
    private String nationalCapitalName;
    private Map<StateEquivalent, String> stateEquivalentsCapitalsNames;
    private Map<County, String> countySeatsNames;
    private final Map<String, JSONObject> independentCityJsons = new HashMap<>();

    // Map Layers
    private MapLayer climateLayer;
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

    public MapManager3(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        ROUTE_MANAGER = new RouteManager(engine, this);
        CENSUS_REGION_MANAGER = new CensusRegionManager(engine, this);
        CENSUS_DIVISION_MANAGER = new CensusDivisionManager(engine, this);
        STATE_EQUIVALENT_MANAGER = new StateEquivalentManager(engine, this);
        CONGRESSIONAL_DISTRICT_MANAGER = new CongressionalDistrictManager(engine, this);
        COUNTY_EQUIVALENT_MANAGER = new CountyEquivalentManager(engine, this);
        COUNTY_SUBDIVISION_MANAGER = new CountySubdivisionManager(engine, this);
        PLACE_MANAGER = new PlaceManager(engine, this);
        HISTORICAL_PROVINCE_MANAGER = new HistoricalProvinceManager(engine, this);
        HYDROLOGICAL_FEATURE_MANAGER = new HydrologicalFeatureManager(engine, this);
        PHYSIOGRAPHICAL_REGION_MANAGER = new PhysiographicalRegionManager(engine, this);
        TIME_ZONE_REGION_MANAGER = new TimeZoneRegionManager(engine, this);
        UNIVERSITY_MANAGER = new UniversityManager(engine, this);
        DESCRIPTOR_MANAGER = new DescriptorManager(engine, this);

        nation = Nation.INSTANCE;
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
        return List.of(
            ROUTE_MANAGER,
            CENSUS_REGION_MANAGER,
            CENSUS_DIVISION_MANAGER,
            STATE_EQUIVALENT_MANAGER,
            CONGRESSIONAL_DISTRICT_MANAGER,
            COUNTY_EQUIVALENT_MANAGER,
            COUNTY_SUBDIVISION_MANAGER,
            PLACE_MANAGER,
            HISTORICAL_PROVINCE_MANAGER,
            HYDROLOGICAL_FEATURE_MANAGER,
            PHYSIOGRAPHICAL_REGION_MANAGER,
            TIME_ZONE_REGION_MANAGER,
            UNIVERSITY_MANAGER,
            DESCRIPTOR_MANAGER
        );
    }

    @Override
    protected void doInit() {
        // Load the layers for realspace maps
        loadMapLayers();
        // Create the nation
        try {
            nation.fromJson(new JSONObject(FilePath.NATION.path));
        }
        catch (IOException e) {
            onError(e);
        }
        // Finish MapEntities
        // Finish County Subdivisions
        for (Place place : PLACE_MANAGER.getAll()) {
            for (CountySubdivision countySubdivision : place.getCountySubdivisions()) {
                countySubdivision.getPlaces().add(place);
            }
        }
        // Finish County Equivalents
        countySeatsNames.forEach((county, countySeatName) -> {
            if (countySeatName == null || countySeatName.isEmpty()) return;
            county.setCountySeat$core(PLACE_MANAGER.requirePlace(countySeatName));
        });
        for (CountySubdivision countySubdivision : COUNTY_SUBDIVISION_MANAGER.getAll()) {
            countySubdivision.getCountyEquivalent().getCountySubdivisions().add(countySubdivision);
        }
        for (Place place : PLACE_MANAGER.getAll()) {
            for (CountyEquivalent countyEquivalent : place.getCountyEquivalents()) {
                countyEquivalent.getPlaces().add(place);
            }
        }
        // Finish State Equivalents
        stateEquivalentsCapitalsNames.forEach((stateEquivalent, capitalName) -> {
            if (capitalName == null || capitalName.isBlank()) return;
            stateEquivalent.setCapital(PLACE_MANAGER.requirePlace(capitalName));
        });
        for (CountyEquivalent countyEquivalent : COUNTY_EQUIVALENT_MANAGER.getAll()) {
            countyEquivalent.getStateEquivalent().addCountyEquivalent$core(countyEquivalent);
        }
        // Finish Nation
        nation.setCapital(PLACE_MANAGER.requirePlace(nationalCapitalName));
        nation.setStateEquivalents$core(STATE_EQUIVALENT_MANAGER.getAll());
        // Build map indices
        CONGRESSIONAL_DISTRICT_MANAGER.getAll().forEach(congressionalDistrict ->
            congressionalDistrictsIndex.put(congressionalDistrict.getRegion(), congressionalDistrict)
        );
        COUNTY_SUBDIVISION_MANAGER.getAll().forEach(countySubdivision ->
            countySubdivisionsIndex.put(countySubdivision.getRegion(), countySubdivision)
        );
        HISTORICAL_PROVINCE_MANAGER.getAll().forEach(historicalProvince ->
            historicalProvincesIndex.put(historicalProvince.getRegion(), historicalProvince)
        );
        HYDROLOGICAL_FEATURE_MANAGER.getAll().forEach(hydrologicalFeature ->
            hydrologicalFeaturesIndex.put(hydrologicalFeature.getRegion(), hydrologicalFeature)
        );
        PHYSIOGRAPHICAL_REGION_MANAGER.getAll().forEach(physiographicalRegion ->
            physiographicalRegionsIndex.put(physiographicalRegion.getRegion(), physiographicalRegion)
        );
        TIME_ZONE_REGION_MANAGER.getAll().forEach(timeZoneRegion ->
            timeZoneRegionsIndex.put(timeZoneRegion.getRegion(), timeZoneRegion)
        );
        PLACE_MANAGER.getAll().forEach(place ->
            placesIndex.put(place.getRegion(), place)
        );
        // Verify
        if (toJson().requireArray().isEmpty()) {
            throw new IllegalStateException("Failed to validate " + getClass().getSimpleName() + " after initialization.");
        }
    }

    @Override
    protected void doCleanup() {

    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject();
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        if (json.hasKey("nation")) nation.fromJson(json.requireJson("nation"));
    }

    public @NotNull Nation getNation() {
        return nation;
    }

    public @NotNull Map<String, JSONObject> getIndependentCityJsons() {
        return independentCityJsons;
    }

    // Instance Methods

    // Layers, Indices, and Regions

    private void loadMapLayers() {
        climateLayer = MapLayer.Companion.load(FilePath.CLIMATE_MAP.toFile());
        congressionalDistrictsLayer = MapLayer.Companion.load(FilePath.CONGRESSIONAL_DISTRICTS_MAP.toFile());
        countySubdivisionsLayer = MapLayer.Companion.load(FilePath.COUNTY_SUBDIVISIONS_MAP.toFile());
        historicalProvincesLayer = MapLayer.Companion.load(FilePath.HISTORICAL_PROVINCES_MAP.toFile());
        hydrologyLayer = MapLayer.Companion.load(FilePath.HYDROLOGY_MAP.toFile());
        physiographyLayer = MapLayer.Companion.load(FilePath.PHYSIOGRAPHY_MAP.toFile());
        placesLayer = MapLayer.Companion.load(FilePath.PLACES_MAP.toFile());
        timeZonesLayer = MapLayer.Companion.load(FilePath.TIMEZONES_MAP.toFile());
        topographyBathymetryLayer = MapLayer.Companion.load(FilePath.TOPOGRAPHY_BATHYMETRY_MAP.toFile());

        allLayers = Set.of(
            climateLayer,
            congressionalDistrictsLayer,
            countySubdivisionsLayer,
            historicalProvincesLayer,
            hydrologyLayer,
            physiographyLayer,
            placesLayer,
            timeZonesLayer,
            topographyBathymetryLayer
        );
    }

    public @NotNull MapLayer getClimateLayer() {
        return climateLayer;
    }

    public @Nullable Climate getClimateAt(int mapX, int mapY) {
        MapRegion region = climateLayer.regionAt(mapX, mapY);
        if (region == null) return null;
        return new Climate(region.getColor());
    }

    public @NotNull MapLayer getCongressionalDistrictsLayer() {
        return congressionalDistrictsLayer;
    }

    public @Nullable CongressionalDistrict getCongressionalDistrictAt(int mapX, int mapY) {
        return congressionalDistrictsIndex.get(congressionalDistrictsLayer.regionAt(mapX, mapY));
    }

    public @NotNull MapLayer getCountySubdivisionsLayer() {
        return countySubdivisionsLayer;
    }

    public @Nullable CountySubdivision getCountySubdivisionAt(int mapX, int mapY) {
        return countySubdivisionsIndex.get(countySubdivisionsLayer.regionAt(mapX, mapY));
    }

    public @NotNull MapLayer getHistoricalProvincesLayer() {
        return historicalProvincesLayer;
    }

    public @Nullable HistoricalProvince getHistoricalProvinceAt(int mapX, int mapY) {
        return historicalProvincesIndex.get(historicalProvincesLayer.regionAt(mapX, mapY));
    }

    public @NotNull MapLayer getHydrologyLayer() {
        return hydrologyLayer;
    }

    public @Nullable HydrologicalFeature getHydrologicalFeatureAt(int mapX, int mapY) {
        return hydrologicalFeaturesIndex.get(hydrologyLayer.regionAt(mapX, mapY));
    }

    public @NotNull MapLayer getPhysiographyLayer() {
        return physiographyLayer;
    }

    public @Nullable PhysiographicalRegion getPhysiographicalRegionAt(int mapX, int mapY) {
        return physiographicalRegionsIndex.get(physiographyLayer.regionAt(mapX, mapY));
    }

    public @NotNull MapLayer getPlacesLayer() {
        return placesLayer;
    }

    public @Nullable Place getPlaceAt(int mapX, int mapY) {
        return placesIndex.get(placesLayer.regionAt(mapX, mapY));
    }

    public @NotNull MapLayer getTimeZonesLayer() {
        return timeZonesLayer;
    }

    public @Nullable TimeZoneRegion getTimeZoneRegionAt(int mapX, int mapY) {
        return timeZoneRegionsIndex.get(timeZonesLayer.regionAt(mapX, mapY));
    }

    public @NotNull MapLayer getTopographyBathymetryLayer() {
        return topographyBathymetryLayer;
    }

    public @Nullable TopographyBathymetry getTopographyBathymetryAt(int mapX, int mapY) {
        MapRegion region = topographyBathymetryLayer.regionAt(mapX, mapY);
        if (region == null) return null;
        return new TopographyBathymetry(region.getColor());
    }

    public @NotNull Set<MapLayer> getAllLayers() {
        return allLayers;
    }

    protected @NotNull Set<MapRegion> getMapRegionsAt(int mapX, int mapY) {
        return allLayers.stream().map(layer -> layer.regionAt(mapX, mapY)).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public @Nullable CensusRegion getCensusRegionAt(int mapX, int mapY) {
        CountySubdivision couSub = getCountySubdivisionAt(mapX, mapY);
        if (couSub == null) return null;
        return couSub.getCensusRegion();
    }

    public @Nullable CensusDivision getCensusDivisionAt(int mapX, int mapY) {
        CountySubdivision couSub = getCountySubdivisionAt(mapX, mapY);
        if (couSub == null) return null;
        return couSub.getCensusDivision();
    }

    public @Nullable StateEquivalent getStateEquivalentAt(int mapX, int mapY) {
        CountySubdivision couSub = getCountySubdivisionAt(mapX, mapY);
        if (couSub == null) return null;
        return couSub.getStateEquivalent();
    }

    public @Nullable CountyEquivalent getCountyEquivalentAt(int mapX, int mapY) {
        CountySubdivision couSub = getCountySubdivisionAt(mapX, mapY);
        if (couSub == null) return null;
        return couSub.getCountyEquivalent();
    }

    protected void setNationalCapital(@NotNull String nationalCapitalName) {
        this.nationalCapitalName = nationalCapitalName;
    }

    protected @NotNull Map<StateEquivalent, String> getStateEquivalentsCapitalsNames() {
        return stateEquivalentsCapitalsNames;
    }

    protected @NotNull Map<County, String> getCountySeatsNames() {
        return countySeatsNames;
    }

}
