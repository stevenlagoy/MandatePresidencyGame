package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.jsonic.JSONObject

sealed interface CountyEquivalent : PlaceContainer {
    val name: String
    val qualifiedName: String get() = "$name, ${stateEquivalent.name}"
    val stateEquivalent: StateEquivalent
    val countySubdivisions: Set<CountySubdivision>
    val places: Set<Place>

    fun toJson(): JSONObject
}
