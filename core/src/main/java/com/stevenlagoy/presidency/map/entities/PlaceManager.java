package com.stevenlagoy.presidency.map.entities;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.MapManager3;
import com.stevenlagoy.presidency.util.FilePath;
import com.stevenlagoy.presidency.util.IOUtils;
import com.stevenlagoy.presidency.util.MatchingException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class PlaceManager extends EntityManager<Place, String> {

    // Constants
    private final String placesFileMarker = "_places";

    public PlaceManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {
        try {
            // Get data paths
            Set<Path> placesFiles = new HashSet<>();
            for (Path stateDir : IOUtils.listDirectories(FilePath._STATES)) {
                placesFiles.addAll(
                    IOUtils.listFiles(stateDir).stream().filter(path ->
                        path.toString().contains(placesFileMarker)
                    ).collect(Collectors.toSet())
                );
            }
            // Create Places
            for (Path placesFile : placesFiles) {
                JSONObject placesJson;
                try {
                    placesJson = new JSONObject(placesFile);
                }
                catch (IOException e) {
                    onDegraded(e);
                    continue;
                }
                Map<String, JSONObject> independentCityJsons = engine.getManager(MapManager3.class).getIndependentCityJsons();
                for (Object placeObj : placesJson) {
                    if (placeObj instanceof JSONObject placeJson) {
                        String placeName = String.format("%s %s", placeJson.requireString("fullName", "full_name", "commonName", "common_name", "name"), placeJson.requireString("state", "territory"));
                        if (independentCityJsons.containsKey(placeName)) {
                            JSONObject merged = independentCityJsons.get(placeName);
                            for (Object component : placeJson.requireArray()) {
                                if (component instanceof JSONObject componentJson) {
                                    merged.merge(componentJson);
                                }
                            }
                            IndependentCity independentCity = new IndependentCity(engine, merged);
                            engine.getManager(CountyEquivalentManager.class).register(independentCity);
                            register(independentCity);
                            independentCityJsons.remove(placeName);
                        }
                        else {
                            register(new Place(engine, placeJson));
                        }
                    }
                }
                // Check that all the independent cities have been loaded
                if (!independentCityJsons.isEmpty()) {
                    throw new IllegalStateException("There were " + independentCityJsons.size() + " independent cities identified from county equivalents files which were not present in places files: " + independentCityJsons);
                }
            }
        }
        catch (IOException e) {
            onDegraded(e);
        }
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        for (Object placeObj : json.requireArray()) {
            if (placeObj instanceof JSONObject placeJson) {
                register(new Place(engine, placeJson));
            }
        }
    }

    @Override
    protected @Nullable String keyOf(@NotNull Place entity) {
        return entity.getQualifiedName();
    }

    // Query

    public @NotNull Set<Place> matchPlaces(@NotNull String name) {
        if (name.isBlank()) return Collections.emptySet();
        return entities.stream().filter(place -> place.getName().equals(name)).collect(Collectors.toSet());
    }

    public @NotNull Optional<Place> matchPlace(@NotNull String name) {
        if (name.isBlank()) return Optional.empty();
        return entities.stream().filter(place -> place.getName().equals(name)).findFirst();
    }

    public @NotNull Place requirePlace(@NotNull String name) throws MatchingException {
        return matchPlace(name).orElseThrow(() -> new MatchingException(String.format("Could not find required place with name: %s", name), () -> matchPlace(name)));
    }

    public @NotNull Set<Place> matchPlaces(@NotNull String name, @NotNull StateEquivalent stateEquivalent) {
        if (name.isBlank()) return Collections.emptySet();
        return entities.stream().filter(place -> place.getName().equals(name) && place.getCountyEquivalent().getStateEquivalent().equals(stateEquivalent)).collect(Collectors.toSet());
    }

    public @NotNull Optional<Place> matchPlace(@NotNull String name, @NotNull StateEquivalent stateEquivalent) {
        if (name.isBlank()) return Optional.empty();
        return entities.stream().filter(place -> place.getName().equals(name) && place.getCountyEquivalent().getStateEquivalent().equals(stateEquivalent)).findFirst();
    }

    public @NotNull Place requirePlace(@NotNull String name, @NotNull StateEquivalent stateEquivalent) throws MatchingException {
        return matchPlace(name, stateEquivalent).orElseThrow(() -> new MatchingException(String.format("Could not find required place in %s with name: %s", stateEquivalent.getName(), name), () -> matchPlace(name, stateEquivalent)));
    }

    public @NotNull Set<Place> matchPlaces(@NotNull String placeName, @NotNull String stateEquivalentName) {
        if (placeName.isBlank() || stateEquivalentName.isBlank()) return Collections.emptySet();
        Optional<StateEquivalent> stateEquivalent = engine.getManager(StateEquivalentManager.class).matchStateEquivalent(stateEquivalentName);
        return stateEquivalent.map(state -> matchPlaces(placeName, state)).orElseGet(HashSet::new);
    }

    public @NotNull Optional<Place> matchPlace(@NotNull String placeName, @NotNull String stateEquivalentName) {
        if (placeName.isBlank() || stateEquivalentName.isBlank()) return Optional.empty();
        Optional<StateEquivalent> stateEquivalent = engine.getManager(StateEquivalentManager.class).matchStateEquivalent(stateEquivalentName);
        return stateEquivalent.flatMap(state -> matchPlace(placeName, state));
    }

    public @NotNull Place requirePlace(@NotNull String placeName, @NotNull String stateEquivalentName) throws MatchingException {
        return matchPlace(placeName, stateEquivalentName).orElseThrow(() -> new MatchingException(String.format("Could not find required place in %s with name: %s", stateEquivalentName, placeName), () -> matchPlace(placeName, stateEquivalentName)));
    }

    public @NotNull Set<Place> matchPlaces(@NotNull String placeName, @NotNull String countyEquivalentName, @NotNull String stateEquivalentName) {
        if (placeName.isBlank() || countyEquivalentName.isBlank() || stateEquivalentName.isBlank()) return Collections.emptySet();
        Optional<CountyEquivalent> countyEquivalent = engine.getManager(CountyEquivalentManager.class).matchCountyEquivalent(countyEquivalentName, stateEquivalentName);
        return countyEquivalent.map(county -> entities.stream().filter(place ->
            place.getName().equals(placeName) && place.getCountyEquivalent().equals(county)
        ).collect(Collectors.toSet())).orElseGet(HashSet::new);
    }

    public @NotNull Optional<Place> matchPlace(@NotNull String placeName, @NotNull String countyEquivalentName, @NotNull String stateEquivalentName) {
        if (placeName.isBlank() || countyEquivalentName.isBlank() || stateEquivalentName.isBlank()) return Optional.empty();
        Optional<CountyEquivalent> countyEquivalent = engine.getManager(CountyEquivalentManager.class).matchCountyEquivalent(countyEquivalentName, stateEquivalentName);
        return countyEquivalent.flatMap(county -> entities.stream().filter(place ->
            place.getName().equals(placeName) && place.getCountyEquivalent().equals(county)
        ).findFirst());
    }

    public @NotNull Place requirePlace(@NotNull String placeName, @NotNull String countyEquivalentName, @NotNull String stateEquivalentName) throws MatchingException {
        return matchPlace(placeName, countyEquivalentName, stateEquivalentName).orElseThrow(() -> new MatchingException(String.format("Could not find required place in %s in %s with name: %s", countyEquivalentName, stateEquivalentName, placeName), () ->  matchPlace(placeName, countyEquivalentName, stateEquivalentName)));
    }

}
