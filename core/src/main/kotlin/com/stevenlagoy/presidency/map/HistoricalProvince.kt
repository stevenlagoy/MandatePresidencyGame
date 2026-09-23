package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.entities.MapEntity
import com.stevenlagoy.presidency.map.entities.StateEquivalent

class HistoricalProvince(
    engine: Engine,
    name: String,
    val districts: List<HistoricalDistrict>,
    val state: StateEquivalent?,
    val description: String,
) : MapEntity(engine, name)
