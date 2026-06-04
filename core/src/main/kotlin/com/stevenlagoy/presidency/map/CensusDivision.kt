package com.stevenlagoy.presidency.map

data class CensusDivision(
    val name: String,
    val states: Set<State>,
    val region: CensusRegion,
)
