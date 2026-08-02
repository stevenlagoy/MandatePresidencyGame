package com.stevenlagoy.presidency.politics.government;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GovernmentManager extends Manager {

    // Instance Fields

    private final @NotNull Set<Government> governments;
    private final @NotNull Set<GovernmentPosition> governmentPositions;

    // Constructors

    public GovernmentManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        governments = new HashSet<>();
        governmentPositions = new HashSet<>();
    }

    // Manager Methdos

    @Override
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doCleanup() {
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
    }

    // Instance Methods

    public @NotNull Set<Government> getGovernments() {
        return governments;
    }

    public @NotNull Optional<Government> matchGovernment(@NotNull String governmentName) {
        return governments.stream().filter(it -> it.getName().equals(governmentName)).findFirst();
    }

    public @NotNull Set<GovernmentPosition> getGovernmentPositions() {
        return governmentPositions;
    }

    public @NotNull Optional<GovernmentPosition> matchGovernmentPosition(@NotNull String governmentPositionTitle) {
        return governmentPositions.stream().filter(it -> it.getTitle().equals(governmentPositionTitle)).findFirst();
    }

}
