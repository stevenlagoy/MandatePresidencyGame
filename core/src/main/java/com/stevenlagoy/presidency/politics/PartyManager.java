package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import kotlin.uuid.Uuid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PartyManager extends Manager {

    // Instance Fields

    private final @NotNull Set<Party> parties;

    // Constructors

    public PartyManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        parties = new HashSet<>();
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
        parties.clear();
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

    // Parties

    public @NotNull Party createParty() {
        Party party = new Party();
        parties.add(party);
        return party;
    }

    public @NotNull Optional<Party> matchParty(@NotNull String partyName) {
        return parties.stream().filter(party -> party.getName().equals(partyName)).findFirst();
    }

}
