package com.stevenlagoy.presidency.map.entities

sealed interface PlaceContainer {
    val countyEquivalent: CountyEquivalent
    val censusRegion: CensusRegion?
    val censusDivision: CensusDivision?
}
