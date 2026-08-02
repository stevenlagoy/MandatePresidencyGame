package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PartyManager extends Manager {

    // Instance Fields

    private final @NotNull Set<Party> parties;
    private final @NotNull Set<PartyGoverningBody> partyGoverningBodies;

    // Constructors

    public PartyManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        parties = new HashSet<>();
        partyGoverningBodies = new HashSet<>();
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

    public @NotNull Set<Party> getParties() {
        return parties;
    }

    public @NotNull Party createParty() {
        Party party = new Party();
        parties.add(party);
        return party;
    }

    public @NotNull Optional<Party> matchParty(@NotNull String partyName) {
        return parties.stream().filter(party -> party.getName().equals(partyName)).findFirst();
    }

    public @NotNull Set<PartyGoverningBody> getPartyGoverningBodies() {
        return partyGoverningBodies;
    }

    public @NotNull Optional<PartyGoverningBody> matchPartyGoverningBody(@NotNull String bodyName) {
        return partyGoverningBodies.stream().filter(body ->  body.getName().equals(bodyName)).findFirst();
    }

}
