package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Government

class State (
    engine: Engine,
    FIPS: String = "",
    fullName: String = "",
    commonName: String = "",
    var abbreviation: String = "",
    var nickname: String? = null,
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    descriptors: Set<Descriptor> = emptySet(),
    region: RegionData? = null,
    capital: Municipality? = null,
    government: Government? = null,
    var motto: String? = null,
    counties: Set<County> = emptySet(),
    _censusDivision: CensusDivision? = null,
    type: StateType = StateType.SOVERIGN_STATE_COMMONWEALTH
) : SoverignArea(
    engine,
    fullName,
    commonName,
    squareMileage,
    population,
    demographics,
    descriptors,
    region,
    capital,
    government
), HasFIPS {

    override var FIPS: String = FIPS
        internal set

    var counties: Set<County> = counties.toSet()
        internal set

    val municipalities: Set<Municipality>
        get() = counties.flatMap { it.municipalities }.toSet()

    var type: StateType = type
        internal set

    var censusDivision: CensusDivision? = null
        internal set

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_censusDivision != null) censusDivision = _censusDivision
    }

    internal fun addCounty(county: County) {
        counties = (counties.toMutableSet() + county).toSet()
    }

    override fun toJson(): JSONObject = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("abbreviation", abbreviation),
        JSONObject("nickname", nickname),
        JSONObject("motto", motto),
        JSONObject("counties", counties.map { it.fullName }),
        JSONObject("censusDivision", censusDivision?.name),
        JSONObject("type", type),
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.requireString("FIPS")
        abbreviation = json.requireString("abbreviation")
        nickname = json.requireString("nickname")
        motto = json.requireString("motto")
        counties = json.findArray("counties") { emptyList<String>() }!!.asSequence().filterIsInstance<String>().map { engine.MAP_MANAGER.matchCounty(it) }.filter { it.isPresent }.map { it.get() }.toSet()
        val _censusDivision = engine.MAP_MANAGER.matchCensusDivision(json.findString(listOf("censusDivision", "census_division", "division")) { "" } )
        if (_censusDivision.isPresent) censusDivision = _censusDivision.get()
        type = StateType.valueOf(json.findString("type") { "State" }!!)
    }

    enum class StateType {
        SOVERIGN_STATE_COMMONWEALTH,
        FEDERAL_DISTRICT,
        TERRITORY,
    }
}
