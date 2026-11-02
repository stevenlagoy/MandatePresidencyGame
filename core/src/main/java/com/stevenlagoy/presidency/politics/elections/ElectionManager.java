package com.stevenlagoy.presidency.politics.elections;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.HasGovernment;
import com.stevenlagoy.presidency.politics.government.GovernmentPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ElectionManager extends EntityManager<Election, String> {

    public ElectionManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        setFactory(new ElectionFactory(engine, this));
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected @NotNull Election entityFromJson(JSONObject json) {
        return new Election(engine, json);
    }

    @Override
    protected @Nullable String keyOf(@NotNull Election entity) {
        return entity.getName();
    }

    public @NotNull Set<Election> getElectionsFor(@NotNull HasGovernment hasGovernment) {
        return entities.stream().filter(election -> election.getConstituency().equals(hasGovernment)).collect(Collectors.toSet());
    }

    public @NotNull Set<Election> getElectionsFor(@NotNull String name) {
        return entities.stream().filter(election -> election.getConstituency().getName().equals(name)).collect(Collectors.toSet());
    }
}
