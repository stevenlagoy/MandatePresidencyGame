package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.map.Descriptor
import com.stevenlagoy.presidency.map.HasFIPS
import com.stevenlagoy.presidency.map.RegionData
import com.stevenlagoy.presidency.politics.government.Government

open class StateEquivalent (
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
    _capital: Place? = null,
    government: Government = Government(engine),
    var motto: String? = null,
    counties: Set<County> = emptySet(),
    _censusDivision: CensusDivision? = null,
    type: StateType = StateType.STATE,
    val subdivisionScheme: SubdivisionScheme = SubdivisionScheme.MCD,
    // specify which type(s) of county subdivisions are allowed // val allowedCountySubdivisions: List<KClass<CountySubdivisions>> = mutableListOf()
) : SoverignArea(
    engine,
    fullName,
    commonName,
    squareMileage,
    population,
    demographics,
    descriptors,
    region,
    _capital,
    government,
), HasFIPS {

    override var FIPS: String = FIPS
        internal set

    var countyEquivalents: Set<CountyEquivalent> = counties.toSet()
        internal set

    val municipalities: Set<Place>
        get() = countyEquivalents.flatMap { it.places }.toSet()

    var type: StateType = type
        internal set

    var censusDivision: CensusDivision? = null
        internal set

    val censusRegion: CensusRegion?
        get() = censusDivision?.censusRegion

    val nation: Nation = Nation

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_censusDivision != null) censusDivision = _censusDivision
        if (_capital != null) capital = _capital
    }

    internal fun addCountyEquivalent(countyEquivalent: CountyEquivalent) {
        countyEquivalents = (countyEquivalents.toMutableSet() + countyEquivalent).toSet()
    }

    override fun toJson(): JSONObject = super.toJson().merge(
        JSONObject("FIPS", FIPS),
        JSONObject("abbreviation", abbreviation),
        JSONObject("nickname", nickname),
        JSONObject("motto", motto),
        JSONObject("counties", countyEquivalents.map { it.qualifiedName }),
        JSONObject("censusDivision", censusDivision?.name),
        JSONObject("type", type),
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        FIPS = json.requireString("FIPS")
        abbreviation = json.requireString("abbreviation")
        nickname = json.requireString("nickname")
        motto = json.requireString("motto")
        countyEquivalents = json.findArray("counties") { emptyList<String>() }!!.asSequence().filterIsInstance<String>().map { engine.MAP_MANAGER.matchCountyEquivalent(it) }.filter { it.isPresent }.map { it.get() }.toSet()
        val _censusDivision = engine.MAP_MANAGER.matchCensusDivision(json.findString(listOf("censusDivision", "census_division", "division")) { "" }!! )
        if (_censusDivision.isPresent) censusDivision = _censusDivision.get()
        type = StateType.entries.find { it.label == json.findString("type") { "state" }!! }!!
    }

    enum class StateType(val label: String) {
        STATE("state"),
        COMMONWEALTH("commonwealth"),
        FEDERAL_DISTRICT("federal district"),
        TERRITORY("territory"),
    }

    enum class SubdivisionScheme { CCD, MCD }
}
