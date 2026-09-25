package com.stevenlagoy.presidency.map.entities;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.HasFIPS;
import com.stevenlagoy.presidency.map.MapManager3;
import com.stevenlagoy.presidency.util.FilePath;
import com.stevenlagoy.presidency.util.IOUtils;
import com.stevenlagoy.presidency.util.MatchingException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CountyEquivalentManager extends EntityManager<CountyEquivalent, String> {

    // Constants
    private final String countyEquivalentsFileMarker = "_counties";

    public CountyEquivalentManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    @Override
    protected void doInit() {
        try {
            // Get data paths
            Set<Path> countyEquivalentsFiles = new HashSet<>();
            for (Path stateDir : IOUtils.listDirectories(FilePath._STATES)) {
                countyEquivalentsFiles.addAll(
                    IOUtils.listFiles(stateDir).stream().filter(path ->
                        path.toString().contains(countyEquivalentsFileMarker)
                    ).collect(Collectors.toSet())
                );
            }
            // Create County Equivalents
            for (Path countyEquivalentsFile : countyEquivalentsFiles) {
                JSONObject countyEquivalentsJson;
                try {
                    countyEquivalentsJson = new JSONObject(countyEquivalentsFile);
                }
                catch (IOException e) {
                    onDegraded(e);
                    continue;
                }
                for (Object countyEquivalentObj : countyEquivalentsJson.requireArray()) {
                    if (countyEquivalentObj instanceof JSONObject countyJson) {
                        String type = countyJson.findString("type", () -> "county");
                        assert type != null; // Can never be null because the supplier returns non-null
                        switch (type.toLowerCase().replaceAll("[^a-z]", " ")) {
                            case "federal district" :
                                register(engine.getManager(MapManager3.class).getNation().getCapital().getCountyEquivalent());
                            case "independent city" :
                                // These will be made later when places are created
                                String independentCityName = String.format("%s, %s", countyJson.requireString("fullName", "commonName", "name"), countyJson.requireString("state", "territory"));
                                engine.getManager(MapManager3.class).getIndependentCityJsons().put(independentCityName, countyJson);
                                break;
                            case "county" :
                            case "parish" :
                            case "borough" :
                            case "planning region" :
                            case "consolidated city county" :
                            default :
                                register(new County(engine, countyJson));
                                break;
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            onDegraded(e);
        }
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    @Override
    protected @Nullable String keyOf(@NotNull CountyEquivalent entity) {
        if (entity instanceof HasFIPS) return ((HasFIPS) entity).getFIPS();
        return entity.getName();
    }

    // Query

    public @NotNull Set<CountyEquivalent> matchCountyEquivalents(@NotNull String nameOrFIPS) {
        if (nameOrFIPS.isBlank()) return Collections.emptySet();
        return entities.stream().filter(county ->
            county.getName().equals(nameOrFIPS) ||
                county.getQualifiedName().equals(nameOrFIPS) ||
                (county instanceof County && ((County) county).getFIPS().equals(nameOrFIPS))
        ).collect(Collectors.toSet());
    }

    public @NotNull Optional<CountyEquivalent> matchCountyEquivalent(@NotNull String nameOrFIPS) {
        if (nameOrFIPS.isBlank()) return Optional.empty();
        if (index.containsKey(nameOrFIPS)) return Optional.of(index.get(nameOrFIPS));
        return entities.stream().filter(county ->
            county.getName().equals(nameOrFIPS) ||
                county.getQualifiedName().equals(nameOrFIPS)
        ).findFirst();
    }

    public @NotNull CountyEquivalent requireCountyEquivalent(@NotNull String nameOrFIPS) throws MatchingException {
        return matchCountyEquivalent(nameOrFIPS).orElseThrow(() -> new MatchingException(String.format("Could not find required county equivalent with name or FIPS: %s", nameOrFIPS), () -> this.matchCountyEquivalent(nameOrFIPS)));
    }

    public @NotNull Set<CountyEquivalent> matchCountyEquivalents(@NotNull String name, @NotNull StateEquivalent stateEquivalent) {
        return matchCountyEquivalents(name).stream().filter(county -> county.getStateEquivalent().equals(stateEquivalent)).collect(Collectors.toSet());
    }

    public @NotNull Optional<CountyEquivalent> matchCountyEquivalent(@NotNull String name, @NotNull StateEquivalent stateEquivalent) {
        return matchCountyEquivalents(name, stateEquivalent).stream().findFirst();
    }

    public @NotNull CountyEquivalent requireCountyEquivalent(@NotNull String name, @NotNull StateEquivalent state) throws MatchingException {
        return matchCountyEquivalent(name, state).orElseThrow(() -> new MatchingException(String.format("Could not find required county equivalent in %s with name: %s", state.getName(), name), () -> this.matchCountyEquivalent(name, state)));
    }

    public @NotNull Set<CountyEquivalent> matchCountyEquivalents(@NotNull String countyEquivalentName, @NotNull String stateEquivalentNameOrFIPS) {
        if (countyEquivalentName.isBlank() || stateEquivalentNameOrFIPS.isBlank()) return Collections.emptySet();
        Optional<StateEquivalent> stateEquivalent = engine.getManager(StateEquivalentManager.class).matchStateEquivalent(stateEquivalentNameOrFIPS);
        return stateEquivalent.map(state -> matchCountyEquivalents(countyEquivalentName, state)).orElseGet(HashSet::new);
    }

    public @NotNull Optional<CountyEquivalent> matchCountyEquivalent(@NotNull String countyEquivalentName, @NotNull String stateEquivalentNameOrFIPS) {
        return matchCountyEquivalents(countyEquivalentName, stateEquivalentNameOrFIPS).stream().findFirst();
    }

    public @NotNull CountyEquivalent requireCountyEquivalent(@NotNull String countyEquivalentName, @NotNull String stateEquivalentNameOrFIPS) throws MatchingException {
        return matchCountyEquivalent(countyEquivalentName, stateEquivalentNameOrFIPS).orElseThrow(() -> new MatchingException(String.format("Could not find required county equivalent in %s with name: %s", stateEquivalentNameOrFIPS, countyEquivalentName), () -> this.matchCountyEquivalent(countyEquivalentName, stateEquivalentNameOrFIPS)));
    }
}
