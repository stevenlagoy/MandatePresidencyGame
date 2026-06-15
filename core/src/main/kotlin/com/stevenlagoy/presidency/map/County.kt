package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.util.parseHex

class County(
    ENGINE: Engine,
    FIPS: String = "",
    _state: State? = null,
    color: Int? = null,
    fullName: String = "",
    commonName: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    descriptors: Set<Descriptor> = emptySet(),
    region: RegionData? = null,
    countySeat: Municipality? = null,
    government: Government? = null,
    municipalities: Set<Municipality> = emptySet(),
    type: CountyType = CountyType.COUNTY,
) : SoverignArea(
    ENGINE,
    fullName,
    commonName,
    squareMileage,
    population,
    demographics,
    descriptors,
    region,
    countySeat,
    government,
), HasFIPS {

    override var FIPS: String = FIPS
        internal set

    lateinit var state: State
        internal set

    var color: Int? = color
        internal set

    var countySeat = countySeat
        internal set

    override var capital: Municipality? = countySeat
        get() = countySeat

    var municipalities: Set<Municipality> = municipalities
        internal set

    var type: CountyType = type
        internal set

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_state != null) state = _state
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("state", state.fullName),
        JSONObject("municipalities", municipalities.map { it.fullName }),
        JSONObject("color", color),
        JSONObject("type", type),
    )!!

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.get("FIPS", String::class.java)
        val _state = engine.MAP_MANAGER.matchState(json.get("state", String::class.java))
        if (_state.isPresent) state = _state.get()
        municipalities = json.get("municipalities", List::class.java).map { engine.MAP_MANAGER.matchMunicipality(it as String) }.filter { it.isPresent }.map { it.get() }.toSet()
        color = parseHex(json.get("color", String::class.java))
        type = CountyType.valueOf(json.get("type", String::class.java))
    }

    enum class CountyType {
        COUNTY,
        PARISH,
        BOROUGH,
        PLANNING_REGION,
        CONSOLIDATED_CITY_COUNTY,
    }
}
