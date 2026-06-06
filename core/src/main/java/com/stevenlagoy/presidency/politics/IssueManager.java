package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IssueManager extends Manager {

    // Constructors

    public IssueManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    public @NotNull Set<Manager> getSubManagers() {
        return Set.of();
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
