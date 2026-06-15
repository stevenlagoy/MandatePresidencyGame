package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Party

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

    var counties: Set<County> = counties
        internal set

    var type: StateType = type
        internal set

    lateinit var censusDivision: CensusDivision
        internal set

    var censusRegion: CensusRegion = censusDivision.censusRegion
        get() = censusDivision.censusRegion
        internal set

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_censusDivision != null) censusDivision = _censusDivision
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("abbreviation", abbreviation),
        JSONObject("nickname", nickname),
        JSONObject("motto", motto),
        JSONObject("counties", counties.map { it.fullName }),
        JSONObject("censusDivision", censusDivision.name),
        JSONObject("type", type),
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.get("FIPS", String::class.java)
        abbreviation = json.get("abbreviation", String::class.java)
        nickname = json.get("nickname", String::class.java)
        motto = json.get("motto", String::class.java)
        counties = json.get("counties", List::class.java).map { engine.MAP_MANAGER.matchCounty(it as String) }.filter { it.isPresent }.map { it.get() }.toSet()
        val _censusDivision = engine.MAP_MANAGER.matchCensusDivision(json.get("censusDivision", String::class.java))
        if (_censusDivision.isPresent) censusDivision = _censusDivision.get()
        type = StateType.valueOf(json.get("type", String::class.java))
    }

    enum class StateType {
        SOVERIGN_STATE_COMMONWEALTH,
        FEDERAL_DISTRICT,
        TERRITORY,
    }
}
