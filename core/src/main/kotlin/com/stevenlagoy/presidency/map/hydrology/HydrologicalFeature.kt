package com.stevenlagoy.presidency.map.hydrology

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.entities.MapEntity

abstract class HydrologicalFeature(
    engine: Engine,
    name: String
) : MapEntity(engine, name)
