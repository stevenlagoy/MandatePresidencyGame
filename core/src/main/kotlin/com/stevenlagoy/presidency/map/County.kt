package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.government.Government
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

    val qualifiedName: String
        get() = "$commonName, ${state.commonName}"

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

    internal fun addMunicipality(municipality: Municipality) {
        municipalities = (municipalities.toMutableSet() + municipality).toSet()
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("state", state.fullName),
        JSONObject("municipalities", municipalities.map { it.FIPS }),
        JSONObject("color", color),
        JSONObject("type", type),
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.requireString("FIPS")
        val _state = engine.MAP_MANAGER.matchState(json.requireString("state"))
        if (_state.isPresent) state = _state.get()
        municipalities = if (json.hasKey("municipalities"))
            json.requireArray("municipalities").asSequence().filterIsInstance<String>().map { engine.MAP_MANAGER.matchMunicipality(it) }.filter { it.isPresent }.map { it.get() }.toSet()
            else setOf()
        color = parseHex(json.requireString("color"))
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
