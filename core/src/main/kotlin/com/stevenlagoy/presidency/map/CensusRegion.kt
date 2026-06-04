package com.stevenlagoy.presidency.map

data class CensusRegion(
    val name: String,
    val divisions: Set<CensusDivision>
) {
    val states = divisions.flatMap { it.states }.toSet()
}
