package com.stevenlagoy.presidency.map.entities

import com.badlogic.gdx.graphics.Color
import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.map.Descriptor
import com.stevenlagoy.presidency.map.HasFIPS
import com.stevenlagoy.presidency.map.RegionData
import com.stevenlagoy.presidency.politics.government.Government
import com.stevenlagoy.presidency.util.parseGdxColor

class County(
    engine: Engine,
    FIPS: String = "",
    _state: StateEquivalent? = null,
    color: Color? = null,
    fullName: String = "",
    commonName: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    descriptors: Set<Descriptor> = emptySet(),
    region: RegionData? = null,
    _countySeat: Place? = null,
    government: Government = Government(engine),
    municipalities: Set<Place> = emptySet(),
    type: CountyType = CountyType.COUNTY,
) : SoverignArea(
    engine,
    fullName,
    commonName,
    squareMileage,
    population,
    demographics,
    descriptors,
    region,
    _countySeat,
    government,
), CountyEquivalent, HasFIPS {

    override val countyEquivalent = this
    override val censusRegion: CensusRegion? = stateEquivalent.censusRegion
    override val censusDivision: CensusDivision? = stateEquivalent.censusDivision

    private val _subdivisions = mutableListOf<CountySubdivision>()

    override val countySubdivisions: List<CountySubdivision> get() = _subdivisions

    override val qualifiedName: String
        get() = "$commonName, ${stateEquivalent.commonName}"

    override var FIPS: String = FIPS
        internal set

    override lateinit var stateEquivalent: StateEquivalent
        internal set

    var color: Color? = color
        internal set

    lateinit var countySeat: Place
        internal set

    override var capital: Place = countySeat
        get() = countySeat

    override var places: Set<Place> = municipalities
        internal set

    var type: CountyType = type
        internal set

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_state != null) stateEquivalent = _state
        if (_countySeat != null) countySeat = _countySeat
    }

    internal fun addPlace(place: Place) {
        places = (places.toMutableSet() + place).toSet()
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("state", stateEquivalent.fullName),
        JSONObject("places", places.map { it.FIPS }),
        JSONObject("color", color),
        JSONObject("type", type),
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.requireString("FIPS")
        val _state = engine.MAP_MANAGER.matchStateEquivalent(json.requireString("state"))
        if (_state.isPresent) stateEquivalent = _state.get()
        places = if (json.hasKey("places"))
            json.requireArray("places").asSequence().filterIsInstance<String>().map { engine.MAP_MANAGER.matchPlace(it) }.filter { it.isPresent }.map { it.get() }.toSet()
            else setOf()
        color = parseGdxColor(json.requireString("color"))
        type = if (json.hasKey("type"))
            CountyType.valueOf(json.requireString("type").uppercase().replace(Regex("[^A-Z0-9]"), "_"))
            else if (_state.isPresent) when(_state.get().commonName) {
                "Louisiana" -> CountyType.PARISH
                "Alaska" -> CountyType.BOROUGH
                "Connecticut" -> CountyType.PLANNING_REGION
                else -> CountyType.COUNTY
            } else CountyType.COUNTY
    }

    enum class CountyType {
        COUNTY,
        PARISH,
        BOROUGH,
        PLANNING_REGION,
        CONSOLIDATED_CITY_COUNTY,
    }
}
