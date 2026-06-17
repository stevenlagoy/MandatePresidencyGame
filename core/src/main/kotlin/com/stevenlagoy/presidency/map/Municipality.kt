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
    counties: List<County> = emptyList(),
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

    var counties: List<County> = counties
        internal set

    var type: MunicipalityType = type
        internal set

    override var capital: Municipality? = this

    val state: State
        get() = counties[0].state

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("UACE", UACE),
        JSONObject("counties", counties.map { it.fullName }),
        JSONObject("color", color),
        JSONObject("type", type)
    )!!

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.get("FIPS", String::class.java)
        UACE = if (json.get("UACE") != null)
            json.get("UACE", String::class.java)
            else ""
        counties = json.get("counties", List::class.java).map { engine.MAP_MANAGER.matchCounty(it as String) }.filter { it.isPresent }.map { it.get() }.toList()
        color = if (json.get("color") != null)
            parseHex(json.get("color", String::class.java))
            else 0xFF000000.toInt()
        try {
            type = MunicipalityType.valueOf(json.get("type", String::class.java).uppercase().replace(Regex("[^A-Z]"), "_")) // TODO change all the data files to use "type" instead of "type_class"
        } catch (e: Exception) {
            println(json)
        }
    }

    enum class MunicipalityType {
        CITY,
        TOWN,
        VILLAGE,
        FIRST_CLASS,
        SECOND_CLASS,
        THIRD_CLASS,
        FOURTH_CLASS,
        HOME_RULE,
        STATUTORY_TOWN,
        STATUTORY_CITY,
        CENSUS_DESIGNATED_PLACE,
        COUNTY,
        UNIFIED_HOME_RULE,
        CORPORATION,
        PLANTATION,
        CITY_AND_COUNTY,
        TOWNSHIP,
        CHARTER_TOWNSHIP,
        INDEPENDENT_CITY,
        SPECIAL_CHARTER,
        CONSOLIDATED_CITY_COUNTY,
        BOROUGH,
        CODE_CITY,
        UNCLASSIFIED_CITY,
        COTERMINOUS_TOWN_VILLAGE,
        CONSOLIDATED_TOWN_VILLAGE,
        CONSOLIDATED_CITY_PARISH,
        SECOND_A_CLASS,
        HOME_RULE_CITY,
        HOME_RULE_TOWN,
        TERRITORIAL_CHARTER_MUNICIPALITY,
        UNIFIED_GOVERNMENT,
    }
}
