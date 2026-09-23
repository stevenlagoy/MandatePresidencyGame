package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.politics.government.Government

/**
 * Independent Cities are places where a city government has full jurisdiction without
 * an encompassing county-- that is, the county does not exist in any form. The city itself
 * acts as a county equivalent and a county subdivision. In CCD states, Independent Cities
 * are still dependent upon a single CCD (this applies only to Carson City, NV, which is
 * dependent upon the Carson City CCD, despite there being no *de jure* county).
 */
class IndependentCity(
    engine: Engine,
    name: String = "",
    _stateEquivalent: StateEquivalent? = null,
    government: Government? = null,
) : CountyEquivalent, Place(engine, name, PlaceTypes.CITY, null, government) {

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override val censusRegion: CensusRegion? = stateEquivalent.censusRegion
    override val censusDivision: CensusDivision? = stateEquivalent.censusDivision
    override lateinit var stateEquivalent: StateEquivalent
        internal set

    override val qualifiedName: String
        get() = super<CountyEquivalent>.qualifiedName

    private val _subdivisions = mutableListOf<CountySubdivision>()
    override val countySubdivisions get() = _subdivisions

    init {
        if (_stateEquivalent != null) stateEquivalent = _stateEquivalent
        container = this
        validateDependency()
    }

    fun addSubdivision(subdivision: CountySubdivision) {
        val ok = when (stateEquivalent.subdivisionScheme) {
            StateEquivalent.SubdivisionScheme.CCD -> subdivision is CountySubdivision.CensusCountyDivision
            StateEquivalent.SubdivisionScheme.MCD -> subdivision is CountySubdivision.MinorCivilDivision
        }
        require(ok) { "${subdivision::class.simpleName} is not valid in a ${stateEquivalent.subdivisionScheme} state (${stateEquivalent.name}" }
        _subdivisions += subdivision
    }

    override fun toJson(): JSONObject {
        return super.toJson()
    }

    override fun fromJson(json: JSONObject): Place {
        return super.fromJson(json)
    }
}
