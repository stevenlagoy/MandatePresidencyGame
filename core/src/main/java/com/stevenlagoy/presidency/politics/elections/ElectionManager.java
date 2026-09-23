package com.stevenlagoy.presidency.politics.elections;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.HasGovernment;
import com.stevenlagoy.presidency.politics.government.GovernmentPosition;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ElectionManager extends Manager {

    // Instance Fields

    private final @NotNull Set<Election> elections;

    // Constructors

    public ElectionManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        elections = new HashSet<>();
    }

    // Manager Methods

    @Override
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doCleanup() {
        elections.clear();
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName(), List.of(
            new JSONObject("elections", elections.stream().map(Election::toJson))
        ));
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        elections.clear();
        for (Object electionObj : json.requireArray("elections")) {
            if (electionObj instanceof JSONObject electionJson) {
                elections.add(new Election(engine, electionJson));
            }
        }
    }

    // Instance Methods

    public @NotNull Set<Election> getElections() {
        return elections;
    }

    public @NotNull Set<Election> getElectionsFor(@NotNull HasGovernment hasGovernment) {
        return elections.stream().filter(election -> election.getConstituency().equals(hasGovernment)).collect(Collectors.toSet());
    }

    public @NotNull Set<Election> getElectionsFor(@NotNull String name) {
        return elections.stream().filter(election -> election.getConstituency().getName().equals(name)).collect(Collectors.toSet());
    }

    public @NotNull Election createElection(@NotNull GovernmentPosition targetPosition) {
        requireState(ManagerState.ACTIVE);
        Election election = new Election(engine, targetPosition, new HashSet<>(), false, null);
        elections.add(election);
        return election;
    }
}
