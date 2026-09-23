package com.stevenlagoy.presidency.map.hydrology

import com.badlogic.gdx.graphics.Color
import com.stevenlagoy.presidency.core.Engine

class RiverBasin(
    engine: Engine,
    name: String,
    override var colors: Set<Color> = emptySet()
) : HydrologicalFeature(engine, name) {

    constructor(engine: Engine, name: String, primaryColor: Color) : this(engine, name, setOf(primaryColor))

}
