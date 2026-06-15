package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.Municipality

abstract class Route(
    engine: Engine,
    val name: String,
    open val connections: List<Municipality>
) : Jsonic<Route>, EngineBound(engine) {

    fun connects(to: Municipality) = connections.contains(to)

}
