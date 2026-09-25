package com.stevenlagoy.presidency.citizens;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.MatchingException;
import kotlin.uuid.Uuid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public class CitizenManager extends EntityManager<Citizen, Uuid> {

    public CitizenManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {

    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        for (Object citizenObj : json.requireArray()) {
            if (citizenObj instanceof JSONObject citizenJson) {
                register(new Citizen(engine, citizenJson));
            }
        }
    }

    @Override
    protected @Nullable Uuid keyOf(@NotNull Citizen entity) {
        return entity.getId();
    }

    // Query

    public @NotNull Set<Citizen> matchByName(@NotNull String name) {
        return matchWhere(citizen ->
            citizen.getName().getIndexedName().equals(name) ||
            citizen.getName().getCommonName().equals(name) ||
            citizen.getName().getBiographicalName().equals(name) ||
            citizen.getName().getFormalName().equals(name) ||
            citizen.getName().getInformalName().equals(name) ||
            citizen.getName().getLegalName().equals(name)
        );
    }

    public @NotNull Optional<Citizen> matchFirstByName(@NotNull String name) {
        return matchFirstWhere(citizen ->
            citizen.getName().getIndexedName().equals(name) ||
            citizen.getName().getCommonName().equals(name) ||
            citizen.getName().getBiographicalName().equals(name) ||
            citizen.getName().getFormalName().equals(name) ||
            citizen.getName().getInformalName().equals(name) ||
            citizen.getName().getLegalName().equals(name)
        );
    }

    public @NotNull Citizen requireByName(@NotNull String name) throws MatchingException {
        return requireWhere(citizen ->
            citizen.getName().getIndexedName().equals(name) ||
            citizen.getName().getCommonName().equals(name) ||
            citizen.getName().getBiographicalName().equals(name) ||
            citizen.getName().getFormalName().equals(name) ||
            citizen.getName().getInformalName().equals(name) ||
            citizen.getName().getLegalName().equals(name),
            () -> "Failed to match citizen by name: " + name
        );
    }
}
