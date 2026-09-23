package com.stevenlagoy.presidency.map.entities;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
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

public class StateEquivalentManager extends EntityManager<StateEquivalent, String> {

    // Constants
    private final String stateEquivalentFileMarker = "_state";

    public StateEquivalentManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    @Override
    protected void doInit() {
        try {
            // Get data paths
            Set<Path> stateEquivalentsFiles = new HashSet<>();
            for (Path stateDir : IOUtils.listDirectories(FilePath._STATES)) {
                stateEquivalentsFiles.addAll(
                    IOUtils.listFiles(stateDir).stream().filter(path ->
                        path.toString().contains(stateEquivalentFileMarker)
                    ).collect(Collectors.toSet())
                );
            }
            // Create State Equivalents
            for (Path stateEquivalentFile : stateEquivalentsFiles) {
                JSONObject stateEquivalentJson;
                try {
                    stateEquivalentJson = new JSONObject(stateEquivalentFile);
                }
                catch (IOException e) {
                    onDegraded(e);
                    continue;
                }
                String type = stateEquivalentJson.requireString("type").toLowerCase().replaceAll("[^a-z]", " ");
                // switch-case didn't work here because enum values are considered non-const
                if (type.equals(StateEquivalent.StateType.FEDERAL_DISTRICT.getLabel())) {
                    FederalDistrict.INSTANCE.fromJson(stateEquivalentJson);
                    register(FederalDistrict.INSTANCE);
                }
                else {
                    register(new StateEquivalent(engine, stateEquivalentJson));
                }
            }
        }
        catch (Exception e) {
            onDegraded(e);
        }
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    @Override
    protected @Nullable String keyOf(@NotNull StateEquivalent entity) {
        return entity.getFIPS();
    }

    // Query

    public @NotNull Set<StateEquivalent> matchStateEquivalents(@NotNull String nameOrFIPS) {
        if (nameOrFIPS.isBlank()) return Collections.emptySet();
        return entities.stream().filter(state ->
            state.getName().equals(nameOrFIPS) ||
                state.getFullName().equals(nameOrFIPS) ||
                state.getFIPS().equals(nameOrFIPS)
        ).collect(Collectors.toSet());
    }

    public @NotNull Optional<StateEquivalent> matchStateEquivalent(@NotNull String nameOrFIPS) {
        if (nameOrFIPS.isBlank()) return Optional.empty();
        if (index.containsKey(nameOrFIPS)) return Optional.of(index.get(nameOrFIPS));
        return entities.stream().filter(state ->
            state.getName().equals(nameOrFIPS) ||
                state.getFullName().equals(nameOrFIPS)
        ).findFirst();
    }

    public @NotNull StateEquivalent requireStateEquivalent(@NotNull String nameOrFIPS) throws MatchingException {
        return matchStateEquivalent(nameOrFIPS).orElseThrow(() -> new MatchingException(String.format("Could not find required state equivalent with name or FIPS: %s", nameOrFIPS), () -> this.matchStateEquivalent(nameOrFIPS)));
    }
}
