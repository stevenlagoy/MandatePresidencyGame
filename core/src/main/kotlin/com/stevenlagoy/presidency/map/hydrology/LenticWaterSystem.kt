package com.stevenlagoy.presidency.map.hydrology

import com.stevenlagoy.presidency.core.Engine

data class LenticWaterSystemType(
    val label: String,
    val description: String,
)

object LenticWaterSystemTypes {
    val INLAND_SEA = LenticWaterSystemType("inland sea", "Inland seas are very large bodies of water located within a landmass. The Great Lakes are considered freshwater inland seas.")
    val LAKE = LenticWaterSystemType("lake", "A lake is an inland body of standing water, usually but not always with inflowing or outflowing river connections.")
    val POND = LenticWaterSystemType("pond", "A pond is an inland body of standing water which is self-contained and usually fed by runoff from precipitation rather than by a river.")
    val WETLAND = LenticWaterSystemType("wetland", "A wetland is an area of land where water saturates the soil or covers the surface shallowly for a majority of the year.")
    val BAYOU = LenticWaterSystemType("bayou", "A bayou is a marshy water body commonly found in flat, low-lying regions especially in the South. Bayous often have a very slow current, unlike swamps.")
    val MARSH = LenticWaterSystemType("marsh", "A marsh is a kind of wetland in which vegetation is primarily grasses, sedges, and reeds rather than trees.")
    val BOG = LenticWaterSystemType("bog", "A bog is a kind of wetland which accumulates peat and usually has acidic soil and water.")
    val FEN = LenticWaterSystemType("fen", "A fen is a kind of wetland with a deep layer of peat with alkaline water fed from mineral-rich groundwater sources.")
    val SWAMP = LenticWaterSystemType("swamp", "A swamp is a kind of wetland with slow or stagnant water in which vegetation consists of woody plants as well as grasses and mosses.")
    val MANGROVE = LenticWaterSystemType("mangrove", "A mangrove is a kind of coastal wetland in which salt-tolerant trees and shrubs grow in tidal waters.")
}

class LenticWaterSystem(
    engine: Engine,
    name: String,
    val type: LenticWaterSystemType = LenticWaterSystemTypes.LAKE,
) : HydrologicalFeature(engine, name)
