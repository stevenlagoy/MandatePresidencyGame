package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PoliticsManager extends Manager {

    // Instance Fields

    public final PartyManager PARTY_MANAGER;
    public final IssueManager ISSUE_MANAGER;

    // Constructors

    public PoliticsManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        PARTY_MANAGER = new PartyManager(engine, this);
        ISSUE_MANAGER = new IssueManager(engine, this);
    }

    // Manager Methods

    @Override
    public @NotNull Set<Manager> getSubManagers() {
        return Set.of(PARTY_MANAGER, ISSUE_MANAGER);
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


    // Creational Methods

    public Government createGovernment() {
        return null;
    }
}
