package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.government.Government
import com.stevenlagoy.presidency.util.parseHex
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class Municipality(
    engine: Engine,
    FIPS: String = "",
    UACE: String = "",
    counties: Set<County> = emptySet(),
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

    val qualifiedName: String
        get() = "$commonName, ${if (counties.isNotEmpty()) counties.first().qualifiedName else ""}"

    override var FIPS: String = FIPS
        internal set

    var UACE: String = UACE
        internal set

    var color: Int? = color
        internal set

    var counties: Set<County> = counties
        internal set

    var type: MunicipalityType = type
        internal set

    override var capital: Municipality? = this

    val state: State
        get() = counties.first().state

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    internal fun addCounty(county: County) {
        counties = (counties.toMutableSet() + county).toSet()
    }

    override fun toJson() = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("UACE", UACE),
        JSONObject("counties", counties.map { it.FIPS }),
        JSONObject("color", color),
        JSONObject("type", type)
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.requireString("FIPS")
        UACE = json.findString("UACE") { "" }!!
        counties = json.requireArray("counties").asSequence().filterIsInstance<String>().map { engine.MAP_MANAGER.matchCounty(it) }.filter { it.isPresent }.map { it.get() }.toSet()
        color = parseHex(json.findString("color") { "0xFF000000" })
        type = MunicipalityType.valueOf(json.requireString("type").uppercase().replace(Regex("[^A-Z0-9]"), "_")) // TODO change all the data files to use "type" instead of "type_class"
    }

    enum class MunicipalityType(var label: String) {
        BARRIO("Barrio de %s"), // Puerto Rico
        AUTONOMOUS_MUNICIPALITY("City and Municipality of %s"), // Puerto Rico
        BOROUGH("Borough of %s"), // Connecticut, New Jersey, Pennsylvania
        CENSUS_DESIGNATED_PLACE("%s"), // All states, specifically Hawaii
        CHARTER_CITY("City of %s"), // California
        CHARTER_TOWN("Town of %s"), // California
        CHARTER_TOWNSHIP("%s Township"), // Michigan
        CITY("City of %s"), // Arizona, Connecticut, Delaware, Florida, Georgia, Idaho, Illinois, Iowa, Louisiana, Maine, Maryland, Massachusetts, Michigan, Mississippi, Nebraska, Nevada, New Hampshire, New Jersey, New Mexico, New York, North Carolina, North Dakota, Ohio, Oklahoma, Oregon, Rhode Island, South Carolina, South Dakota, Tennessee, Utah, Vermont, West Virginia, Wisconsin
        CLASS_1_CITY("City of %s"), // Alabama
        CLASS_2_CITY("City of %s"), // Alabama
        CLASS_3_CITY("City of %s"), // Alabama
        CLASS_4_CITY("City of %s"), // Alabama
        CLASS_5_CITY("City of %s"), // Alabama
        CLASS_6_CITY("City of %s"), // Alabama
        CLASS_7_CITY("City of %s"), // Alabama
        CLASS_8_CITY("City of %s"), // Alabama
        CODE_CITY("City of %s"), // California, Washington
        CONSOLIDATED_BOROUGH_TOWN("%s Borough and Town"), // Connecticut
        CONSOLIDATED_CITY_COUNTY("City of %s"), // California, Colorado, Florida, Georgia
        CONSOLIDATED_CITY_PARISH("% City and Parish"), // Louisiana
        CONSOLIDATED_CITY_TOWN("%s Town and City"), // Connecticut
        CONSOLIDATED_CITY_UNIFIED_GOVERNMENT("%s County consolidated government"), // Georgia
        CONSOLIDATED_TOWN_VILLAGE("%s Town and Village"), // New York
        CONSTITUTIONAL_CHARTER_CITY("City of %s"), // Missouri
        COTERMINOUS_TOWN_VILLAGE("%s Town and Village"), // New York
        FIRST_CLASS_CITY("City of %s"), // Alaska (General Law), Arkansas, Indiana, Kansas, Kentucky, Minnesota, Montana, Pennsylvania, Washington, Wyoming
        FOURTH_CLASS_CITY("City of %s"), // Minnesota, Missouri
        HOME_RULE_CITY("City of %s"), // Alaska, Colorado, Kentucky, Texas
        HOME_RULE_CITY_AND_COUNTY("%s City and County"), // Colorado
        HOME_RULE_TOWN("Town of %s"), // Colorado, Texas
        HOME_RULE_VILLAGE("Village of %s"), // Texas?
        INCOIRPORATED_TOWN_AND_CITY("%s Town and City"), // Puerto Rico
        INCORPORATED_CITY_COUNTY("%s City and County"), // New Mexico
        INDEPENDENT_CITY("City of %s"), // Maryland, Nevada, Virginia
        LEGISLATIVE_CHARTER_CITY("City of %s"), // Missouri
        METRO_TOWNSHIP("%s Township"), // Utah
        PLANTATION("%s"), // Maine (All Plantations include the word "Plantation" in their Common Name)
        SECOND_A_CLASS_CITY("City of %s"), // Pennsylvania
        SECOND_CLASS_CITY("City of %s"), // Alaska (General Law), Arkansas, Indiana, Kansas, Minnesota, Montana, Pennsylvania, Washington
        STATUTORY_CITY("City of %s"), // Colorado
        STATUTORY_TOWN("Town of %s"), // Colorado
        TERRITORIAL_CHARTER_TOWN("Town of %s"), // Colorado
        THIRD_CLASS_CITY("City of %s"), // Indiana, Kansas, Minnesota, Missouri, Montana, Pennsylvania
        TOWN("Town of %s"), // Alabama, Arizona, Arkansas, Connecticut, Delaware, Florida, Georgia, Illinois, Indiana, Louisiana, Maine, Maryland, Massachusetts, Mississippi, Montana, New Hampshire, New Jersey, New Mexico, North Carolina, Oklahoma, Rhode Island, South Carolina, South Dakota, Tennessee, Utah, Vermont, Virginia, Washington, West Virginia, Wyoming, U.S. Virgin Islands
        TOWNSHIP("%s Township"), // New Jersey, Pennsylvania
        TYPE_A_CITY("City of %s"), // Texas
        TYPE_B_CITY("City of %s"), // Texas
        TYPE_C_CITY("City of %s"), // Texas
        UNCLASSIFIED_CITY("City of %s"), // Washington
        UNIFIED_HOME_RULE_BOROUGH_AND_CITY("%s Borough and Town"), // Alaska
        VILLAGE("Village of %s"), // Delaware, Florida, Illinois, Louisiana, Maryland, Michigan, Mississippi, Missouri, Nebraska, New Jersey, New Mexico, New York, North Carolina, Ohio, South Dakota, Vermont, West Virginia, Wisconsin, Guam
    }
}
