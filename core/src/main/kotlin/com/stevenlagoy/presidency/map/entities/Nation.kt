package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.HasGovernment
import kotlin.jvm.optionals.getOrNull

class Nation private constructor(
    engine: Engine,
): SoverignArea(
    engine,
    "United States of America",
    "United States",
    0.0,
    0,
    emptyMap(),
    emptySet(),
    null,
    null,
    Engine.getInstance().POLITICS_MANAGER.GOVERNMENT_MANAGER.matchGovernment("nation").get(),
    Engine.getInstance().POLITICS_MANAGER.ELECTION_MANAGER.getElectionsFor("United States of America").toMutableSet(),
    Engine.getInstance().POLITICS_MANAGER.PARTY_MANAGER.parties,
    mutableSetOf()
), HasGovernment {

    companion object {
        fun create(engine: Engine): Nation = Nation(engine)
    }

    var stateEquivalents: Set<StateEquivalent> = emptySet()
        internal set
}
