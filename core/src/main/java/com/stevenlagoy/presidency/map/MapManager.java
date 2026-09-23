package com.stevenlagoy.presidency.map;

import com.badlogic.gdx.graphics.Texture;
import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.map.entities.*;
import com.stevenlagoy.presidency.util.FilePath;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
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
 * including {@link Nation}, {@link StateEquivalent}, {@link CongressionalDistrict}, {@link County}, and
 * {@link Place}.
 *
 * @author Steven LaGoy
 */
public class MapManager extends Manager {

    // Instance Fields

    // LOD Textures generated from binary data
    private @NotNull List<Texture> lodTextures = new ArrayList<>();
    private @NotNull List<Texture> lodPixmaps  = new ArrayList<>();

    private boolean selfIsInitialized = false;

    // Instance Methods

    private void createMunicipalities(@NotNull Set<Path> placesDataPaths) {
        try {
            countiesPlaces.clear();
            for (Path placeDataPath : placesDataPaths) {
                JSONObject placesJson = new JSONObject(placeDataPath);
                for (Object placeObj : placesJson.requireArray()) {
                    if (placeObj instanceof JSONObject placeJson) {
                        String placeFIPS = placeJson.requireString("FIPS");
                        String stateFips = placeFIPS.substring(0, 2);
                        @SuppressWarnings("unchecked")
                        List<String> countiesFIPS = (List<String>) placeJson.requireArray("counties");
                        for (String countyFIPS : countiesFIPS) {
                            String countyStateFIPS = countyFIPS.substring(0, 2);
                            if (!countyStateFIPS.equals(stateFips)) {
                                System.out.println("County FIPS does not match place FIPS: " + placeFIPS + ", " + countyFIPS);
                            }
                        }
                        Place m = new Place(engine, placeJson);
                        places.add(m);
                        if (countiesFIPS.isEmpty()) {
                            System.out.println("Place " + placeJson.find("name") + " has no counties");
                        }
                        countiesFIPS.forEach(countyFIPS -> countiesPlaces.computeIfAbsent(countyFIPS, k -> new HashSet<>()).add(m));
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
        for (Map.Entry<String, Set<Place>> entry : countiesPlaces.entrySet()) {
            Optional<County> county = matchCounty(entry.getKey());
            if (county.isEmpty()) {
                System.out.println("County with FIPS = " + entry.getKey() + " could not be matched.");
                continue;
            }
            entry.getValue().forEach(place -> {
                county.get().addPlace$core(place);
                place.addAdditionalCounty$core(county.get());
            });
        }
    }

    private void createStates(@NotNull Set<Path> statesDataPaths) {
        try {
            for (Path statesDataPath : statesDataPaths) {
                JSONObject stateJson = new JSONObject(statesDataPath);
                stateEquivalents.add(new StateEquivalent(engine, stateJson));
                statesDivisions.put(stateJson.requireString("fullName"), stateJson.requireString("censusDivision"));
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void resolveStatesCounties() {
        for (Map.Entry<String, Set<County>> entry : statesCounties.entrySet()) {
            Optional<StateEquivalent> state = matchState(entry.getKey());
            if (state.isEmpty()) {
                System.out.println("State " + entry.getKey() + " could not be matched.");
                continue;
            }
            entry.getValue().forEach(county -> {
                county.setStateEquivalent$core(state.get());
                state.get().addCountyEquivalent$core(county);
            });
        }
    }

    private void createNation(@NotNull Path nationDataPath) {
        Nation.INSTANCE.setStateEquivalents$core(stateEquivalents);
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
            Optional<StateEquivalent> state = matchState(entry.getKey());
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
        for (Place place : places) {
            try {
                place.getCountyEquivalents();
                place.container.getCountyEquivalent().getStateEquivalent();
            }
            catch (Exception e) {
                System.out.println("Place " + place.getName() + " (" + place.getFIPS() + ") could not be verified.");
                successFlag = false;
            }
        }
        for (County county : counties) {
            try {
                county.getPlaces();
                county.getCapital();
                county.getStateEquivalent();
            }
            catch (Exception e) {
                System.out.println("County " + county.getName() + " (" + county.getFIPS() + ") could not be verified.");
                successFlag = false;
            }
        }
        for (StateEquivalent state : stateEquivalents) {
            try {
                state.getMunicipalities();
                state.getCountyEquivalents();
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

    private final Map<String, Place> fipsToPlace = new HashMap<>();
    private final Map<String, Place> fullNameToPlace = new HashMap<>();
    private final Map<String, Place> commonNameToPlace = new HashMap<>();
    private final Map<String, Place> qualifiedNameToPlace = new HashMap<>();
    private final Map<String, Place> commonNameAndStateCommonNameToPlace = new HashMap<>();
    private final Map<String, Place> commonNameAndStateAbbreviationToPlace = new HashMap<>();
    public @NotNull Optional<Place> matchPlace(@NotNull String name) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        if (!selfIsInitialized) {
            return Optional.ofNullable(
                places.stream().filter(place -> place.getFIPS().equals(name)).findFirst().orElse(
                places.stream().filter(place -> place.getFullName().equals(name)).findFirst().orElse(
                places.stream().filter(place -> place.getCommonName().equals(name)).findFirst().orElse(null)
            )));
        }
        if (fipsToPlace.isEmpty()) buildPlaceMatchingIndices();
        return Optional.ofNullable(
            fipsToPlace.getOrDefault(name,
            fullNameToPlace.getOrDefault(name,
            commonNameToPlace.getOrDefault(name,
            qualifiedNameToPlace.getOrDefault(name,
            commonNameAndStateCommonNameToPlace.getOrDefault(name,
            commonNameAndStateAbbreviationToPlace.getOrDefault(name,
        null)))))));
    }
    private void buildPlaceMatchingIndices() {
        places.forEach(place -> {
            fipsToPlace.put(place.getFIPS(), place);
            fullNameToPlace.put(place.getFullName(), place);
            commonNameToPlace.put(place.getCommonName(), place);
            qualifiedNameToPlace.put(place.getQualifiedName(), place);
            commonNameAndStateCommonNameToPlace.put(String.format("%s, %s", place.getCommonName(), place.container.getCountyEquivalent().getStateEquivalent().getCommonName()), place);
            commonNameAndStateAbbreviationToPlace.put(String.format("%s, %s", place.getCommonName(), place.container.getCountyEquivalent().getStateEquivalent().getAbbreviation()), place);
        });
    }

    public @NotNull Place selectPlace(Demographics demographics) {
        requireOperational();
        Place selected = RandomUtils.randSelect(places);
        assert selected != null;
        return selected;
    }

    public @NotNull Place selectPlace() {
        requireOperational();
        return selectPlace(engine.DEMOGRAPHICS_MANAGER.getCommonDemographics());
    }

    public @NotNull Place getMostPopulatedPlace() {
        assert(places.stream().max(Comparator.comparing(Place::getPopulation)).isPresent());
        return places.stream().max(Comparator.comparing(Place::getPopulation)).get();
    }

    public void readNationData() {
        requireState(ManagerState.INITIALIZING);
        try {
            JSONObject json = new JSONObject(FilePath.NATION.path);
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

    public @NotNull Optional<StateEquivalent> matchState(String name) {
        return Optional.ofNullable(
            stateEquivalents.stream().filter(state -> state.getFIPS().equals(name)).findFirst().orElse(
            stateEquivalents.stream().filter(state -> state.getFullName().equals(name)).findFirst().orElse(
            stateEquivalents.stream().filter(state -> state.getCommonName().equals(name)).findFirst().orElse(null)
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
            matchPlace(name)
        ).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }
}
