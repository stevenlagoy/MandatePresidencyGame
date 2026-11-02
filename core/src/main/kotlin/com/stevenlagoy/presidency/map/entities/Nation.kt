package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.HasGovernment
import com.stevenlagoy.presidency.politics.PartyManager
import com.stevenlagoy.presidency.politics.elections.ElectionManager
import com.stevenlagoy.presidency.politics.government.GovernmentManager
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
    engine.getManager(GovernmentManager::class.java).matchByKey("United States of America").getOrNull(),
    engine.getManager(ElectionManager::class.java).getElectionsFor("United States of America").toMutableSet(),
    engine.getManager(PartyManager::class.java).parties,
    mutableSetOf()
), HasGovernment {

    var stateEquivalents: Set<StateEquivalent> = emptySet()
        internal set
}
