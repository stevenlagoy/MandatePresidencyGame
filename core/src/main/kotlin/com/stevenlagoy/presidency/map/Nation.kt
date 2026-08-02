package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.politics.ElectionResult
import kotlin.jvm.optionals.getOrNull

object Nation: SoverignArea(
    Engine.getInstance(),
    "United States of America",
    "United States",
    0.0,
    0,
    emptyMap(),
    emptySet(),
    null,
    Engine.getInstance().POLITICS_MANAGER.createGovernment(),
    Engine.getInstance().MAP_MANAGER.matchMunicipality("Washington, DC").getOrNull(),
    Engine.getInstance().POLITICS_MANAGER.ELECTION_MANAGER.readResultsFor("United States of America").toMutableSet(),
    Engine.getInstance().POLITICS_MANAGER.PARTY_MANAGER.parties,
    mutableSetOf()
), HasGovernment {

    var states: Set<State> = emptySet()
        internal set
}
