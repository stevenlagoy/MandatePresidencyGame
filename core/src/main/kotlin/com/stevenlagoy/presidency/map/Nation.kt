package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.core.Engine
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
    Engine.getInstance().MAP_MANAGER.matchMunicipality("Washington, District of Columbia, District of Columbia").getOrNull(),
    Engine.getInstance().POLITICS_MANAGER.createGovernment()
) {

    var states: Set<State> = emptySet()
        internal set
}
