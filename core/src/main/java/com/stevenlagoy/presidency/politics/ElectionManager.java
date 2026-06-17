package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.politics.voting.Election;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ElectionManager extends Manager {

    // Instance Fields

    private final @NotNull List<Election> elections;

    // Constructors

    public ElectionManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        elections = new ArrayList<>();
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
        for (Object electionObj : json.get("elections", List.class)) {
            if (electionObj instanceof JSONObject electionJson) {
                elections.add(new Election(electionJson));
            }
        }
    }

    // Instance Methods

    public void doElections(LocalDateTime currentDate) {
        for (Election election : elections) {
            if (!election.getPollsOpenDate().isBefore(currentDate)) {
                // TODO
            }
        }
    }

    public @NotNull Election createElection(@NotNull GovernmentPosition targetPosition) {
        requireState(ManagerState.ACTIVE);
        Election election = new Election(targetPosition);
        elections.add(election);
        return election;
    }
}
