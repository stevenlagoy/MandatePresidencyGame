package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.politics.election.ElectionManager;
import com.stevenlagoy.presidency.politics.government.GovernmentManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PoliticsManager extends Manager {

    // Instance Fields

    public final PartyManager PARTY_MANAGER;
    public final IssueManager ISSUE_MANAGER;
    public final ElectionManager ELECTION_MANAGER;
    public final GovernmentManager GOVERNMENT_MANAGER;

    // Constructors

    public PoliticsManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        PARTY_MANAGER = new PartyManager(engine, this);
        ISSUE_MANAGER = new IssueManager(engine, this);
        ELECTION_MANAGER = new ElectionManager(engine, this);
        GOVERNMENT_MANAGER = new GovernmentManager(engine, this);
    }

    // Manager Methods

    @Override
    public @NotNull List<Manager> getSubManagers() {
        return List.of(PARTY_MANAGER, ISSUE_MANAGER, ELECTION_MANAGER, GOVERNMENT_MANAGER);
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

}
