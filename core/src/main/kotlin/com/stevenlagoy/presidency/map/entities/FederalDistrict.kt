package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.presidency.core.Engine

object FederalDistrict : StateEquivalent(
    Engine.getInstance(),
    fullName = "District of Columbia",
    commonName = "Washington",
    type = StateType.FEDERAL_DISTRICT,
    subdivisionScheme = SubdivisionScheme.CCD,
), CountyEquivalent, PlaceContainer {

    override val countyEquivalent: CountyEquivalent get() = this
    override val stateEquivalent: StateEquivalent get() = this

    private val _subdivisions = mutableListOf<CountySubdivision>()
    override val countySubdivisions: List<CountySubdivision> get() = _subdivisions

    val city: Place
        get() = engine.MAP_MANAGER.requirePlace("Washington, DC")

    override val places: Set<Place> get() = setOf(city)
}
