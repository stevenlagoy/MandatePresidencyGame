package com.stevenlagoy.presidency.map.entities;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.politics.government.Government;
import com.stevenlagoy.presidency.util.FilePath;
import com.stevenlagoy.presidency.util.IOUtils;
import com.stevenlagoy.presidency.util.MatchingException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CountySubdivisionManager extends EntityManager<CountySubdivision, String> {

    // Constants
    private final String countySubdivisionsFileMarker = "_county_subdivisions";

    public CountySubdivisionManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    @Override
    protected void doInit() {
        try {
            // Get data paths
            Set<Path> countySubdivisionsFiles = new HashSet<>();
            for (Path stateDir : IOUtils.listDirectories(FilePath._STATES)) {
                countySubdivisionsFiles.addAll(
                    IOUtils.listFiles(stateDir).stream().filter(path ->
                        path.toString().contains(countySubdivisionsFileMarker)
                    ).collect(Collectors.toSet())
                );
            }
            // Create County Subdivisions
            for (Path countySubdivisionsFile : countySubdivisionsFiles) {
                JSONObject countySubdivisionsJson;
                try {
                     countySubdivisionsJson = new JSONObject(countySubdivisionsFile);
                }
                catch (IOException e) {
                    onDegraded(e);
                    continue;
                }
                String stateFIPS = countySubdivisionsFile.getFileName().toString().substring(0, 2);
                StateEquivalent state = engine.getManager(StateEquivalentManager.class).matchByKey(stateFIPS)
                    .orElseThrow(() -> new MatchingException("Could not find a state with FIPS: " + stateFIPS, () -> engine.getManager(StateEquivalentManager.class).matchByKey(stateFIPS)));
                StateEquivalent.SubdivisionScheme stateSubdivisionScheme = state.getSubdivisionScheme();
                for (Object subdivisionObj : countySubdivisionsJson) {
                    if (subdivisionObj instanceof JSONObject subdivisionJson) {
                        @NotNull String name = subdivisionJson.findString("name").orElse("");
                        @NotNull CountyEquivalent countyEquivalent = engine.getManager(CountyEquivalentManager.class).requireCountyEquivalent(
                            subdivisionJson.requireString("countyEquivalent", "county_equivalent", "county")
                        );
                        switch (stateSubdivisionScheme) {
                            case MCD :
                                SubdivisionType type = Objects.requireNonNull(SubdivisionTypes.INSTANCE.fromString(subdivisionJson.requireString("type")));
                                Government government = subdivisionJson.hasKey("government") ? new Government(engine, subdivisionJson.requireJson("government")) : null;
                                register(new CountySubdivision.MinorCivilDivision(engine, name, countyEquivalent, type, government));
                                break;
                            case CCD :
                                register(new CountySubdivision.CensusCountyDivision(engine, name, countyEquivalent));
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
    protected @Nullable String keyOf(@NotNull CountySubdivision entity) {
        return entity.getName();
    }

    public @NotNull Set<CountySubdivision> matchCountySubdivisions(@NotNull String name) {
        if (name.isBlank()) return Collections.emptySet();
        return entities.stream().filter(couSub ->
            couSub.getName().equals(name)
        ).collect(Collectors.toSet());
    }

    public @NotNull Optional<CountySubdivision> matchCountySubdivision(@NotNull String name) {
        return matchCountySubdivisions(name).stream().findFirst();
    }

    public @NotNull CountySubdivision requireCountySubdivision(@NotNull String name) throws MatchingException {
        return matchCountySubdivision(name).orElseThrow(() -> new MatchingException(String.format("Could not find required county subdivision with name: %s", name), () -> this.matchCountySubdivision(name)));
    }

    public @NotNull Set<CountySubdivision> matchCountySubdivisions(@NotNull String name, @NotNull StateEquivalent stateEquivalent) {
        if (name.isBlank()) return Collections.emptySet();
        return entities.stream().filter(couSub ->
            couSub.getName().equals(name) && couSub.getCountyEquivalent().getStateEquivalent().equals(stateEquivalent)
        ).collect(Collectors.toSet());
    }

    public @NotNull Optional<CountySubdivision> matchCountySubdivision(@NotNull String name, @NotNull StateEquivalent stateEquivalent) {
        return matchCountySubdivisions(name, stateEquivalent).stream().findFirst();
    }

    public @NotNull CountySubdivision requireCountySubdivision(@NotNull String name, @NotNull StateEquivalent stateEquivalent) {
        return matchCountySubdivision(name, stateEquivalent).orElseThrow(() -> new MatchingException(String.format("Could not find required county subdivision in %s with name: %s", stateEquivalent.getName(), name), () -> this.matchCountySubdivision(name, stateEquivalent)));
    }

    public @NotNull Set<CountySubdivision> matchCountySubdivisions(@NotNull String name, @NotNull CountyEquivalent countyEquivalent) {
        if (name.isBlank()) return Collections.emptySet();
        return entities.stream().filter(couSub ->
            couSub.getName().equals(name) && couSub.getCountyEquivalent().equals(countyEquivalent)
        ).collect(Collectors.toSet());
    }

    public @NotNull Optional<CountySubdivision> matchCountySubdivision(@NotNull String name, @NotNull CountyEquivalent countyEquivalent) {
        return matchCountySubdivisions(name, countyEquivalent).stream().findFirst();
    }

    public @NotNull CountySubdivision requireCountySubdivision(@NotNull String name, @NotNull CountyEquivalent countyEquivalent) throws MatchingException {
        return matchCountySubdivision(name, countyEquivalent).orElseThrow(() -> new MatchingException(String.format("Could not find required county subdivision in %s with name: %s", countyEquivalent.getQualifiedName(), name), () -> this.matchCountySubdivision(name, countyEquivalent)));
    }

    public @NotNull Set<CountySubdivision> matchCountySubdivisions(@NotNull String countySubdivisionName, @NotNull String stateEquivalentName) {
        if (countySubdivisionName.isBlank() || stateEquivalentName.isBlank()) return Collections.emptySet();
        Optional<StateEquivalent> stateEquivalent = engine.getManager(StateEquivalentManager.class).matchStateEquivalent(stateEquivalentName);
        return stateEquivalent.map(state -> matchCountySubdivisions(countySubdivisionName, state)).orElseGet(HashSet::new);
    }

    public @NotNull Optional<CountySubdivision> matchCountySubdivision(@NotNull String countySubdivisionName, @NotNull String stateEquivalentName) {
        return matchCountySubdivisions(countySubdivisionName, stateEquivalentName).stream().findFirst();
    }

    public @NotNull CountySubdivision requireCountySubdivision(@NotNull String countySubdivisionName, @NotNull String stateEquivalentName) throws MatchingException {
        return matchCountySubdivision(countySubdivisionName, stateEquivalentName).orElseThrow(() -> new MatchingException(String.format("Could not find required county subdivision in %s with name: %s", stateEquivalentName, countySubdivisionName), () -> this.matchCountySubdivision(countySubdivisionName, stateEquivalentName)));
    }

    public @NotNull Set<CountySubdivision> matchCountySubdivisions(@NotNull String countySubdivisionName, @NotNull String countyEquivalentName, @NotNull String stateEquivalentName) {
        if (countySubdivisionName.isBlank() || countyEquivalentName.isBlank() || stateEquivalentName.isBlank()) return Collections.emptySet();
        Optional<CountyEquivalent> countyEquivalent = engine.getManager(CountyEquivalentManager.class).matchCountyEquivalent(countyEquivalentName, stateEquivalentName);
        return countyEquivalent.map(county -> matchCountySubdivisions(countySubdivisionName, county)).orElseGet(HashSet::new);
    }

    public @NotNull Optional<CountySubdivision> matchCountySubdivision(@NotNull String countySubdivisionName, @NotNull String countyEquivalentName, @NotNull String stateEquivalentName) {
        if (countySubdivisionName.isBlank() || countyEquivalentName.isBlank() || stateEquivalentName.isBlank()) return Optional.empty();
        Optional<CountyEquivalent> countyEquivalent = engine.getManager(CountyEquivalentManager.class).matchCountyEquivalent(countyEquivalentName, stateEquivalentName);
        return countyEquivalent.flatMap(county -> matchCountySubdivision(countySubdivisionName, county));
    }

    public @NotNull CountySubdivision requireCountySubdivision(@NotNull String countySubdivisionName, @NotNull String countyEquivalentName, @NotNull String stateEquivalentName) {
        return matchCountySubdivision(countySubdivisionName, countyEquivalentName, stateEquivalentName).orElseThrow(() -> new MatchingException(String.format("Could not find required county subdivision in %s in %s with name: %s", countyEquivalentName, stateEquivalentName, countySubdivisionName), () -> this.matchCountySubdivision(countySubdivisionName, countyEquivalentName, stateEquivalentName)));
    }
}
