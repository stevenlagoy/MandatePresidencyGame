package com.stevenlagoy.presidency.map.hydrology

import com.stevenlagoy.presidency.core.Engine

data class LoticWaterSystemType(
    val label: String
)

object LoticWaterSystemTypes {
    val RIVER = LoticWaterSystemType("river")
    val STREAM = LoticWaterSystemType("stream")
    val CREEK = LoticWaterSystemType("creek")
    val BROOK = LoticWaterSystemType("brook")
    val RAPID = LoticWaterSystemType("rapid")
}

class LoticWaterSystem(
    engine: Engine,
    name: String,
    val type: LoticWaterSystemType,
    val tributaryOf: LoticWaterSystem? = null,
    val trubutaries: List<LoticWaterSystem> = emptyList(),
) : HydrologicalFeature(engine, name)
