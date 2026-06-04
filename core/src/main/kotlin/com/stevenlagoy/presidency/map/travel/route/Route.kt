package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

abstract class Route(
    val ENGINE: Engine,
    val name: String,
    open val connections: List<Municipality>
) : Jsonic<Route> {

    fun connects(to: Municipality) = connections.contains(to);

}
