package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.util.parseHex
import kotlin.jvm.optionals.getOrNull
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class Municipality(
    engine: Engine,
    FIPS: String = "",
    UACE: String = "",
    _county: County? = null,
    color: Int? = null,
    fullName: String = "",
    commonName: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = mapOf(),
    descriptors: Set<Descriptor> = setOf(),
    region: RegionData? = null,
    government: Government? = null,
    type: MunicipalityType = MunicipalityType.CITY
) : SoverignArea(
    engine,
    fullName,
    commonName,
    squareMileage,
    population,
    demographics,
    descriptors,
    region,
    null,
    government
), HasFIPS {

    override var FIPS: String = FIPS
        internal set

    var UACE: String = UACE
        internal set

    var color: Int? = color
        internal set

    lateinit var county: County
        internal set

    var type: MunicipalityType = type
        internal set

    override var capital: Municipality? = this

    val state = county.state

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_county != null) county = _county
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("UACE", UACE),
        JSONObject("county", county.fullName),
        JSONObject("color", color),
        JSONObject("type", type)
    )!!

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.get("FIPS", String::class.java)
        UACE = json.get("UACE", String::class.java)
        val _county = engine.MAP_MANAGER.matchCounty(json.get("county", String::class.java))
        if (_county.isPresent) county = _county.get()
        color = parseHex(json.get("color", String::class.java))
        type = MunicipalityType.valueOf(json.get("type", String::class.java))
    }

    enum class MunicipalityType {
        CITY,
        TOWN,
        VILLAGE,
        FIRST_CLASS,
        SECOND_CLASS,
        THIRD_CLASS,
        HOME_RULE,
    }
}
