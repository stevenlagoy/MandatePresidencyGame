package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Government
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

abstract class SoverignArea(
    engine: Engine,
    fullName: String,
    commonName: String,
    squareMileage: Double,
    population: Int,
    demographics: Map<Bloc, Double>,
    descriptors: Set<Descriptor>,
    region: RegionData?,
    capital: Municipality?,
    government: Government?,
) : MapEntity(engine, fullName, squareMileage, population, demographics, descriptors, region) {

    /** Full name of this map entity, possibly including a qualifier like 'State of', 'County', or 'Commonwealth of'. */
    var fullName: String = fullName
        internal set

    /** Common name of this map entity, not including qualifiers like 'State of', 'County', or 'Commonwealth of'.*/
    var commonName: String = commonName
        internal set

    /** Municipality which serves as the capital of this soverign area. Other names may be used for the same concept, like county seats. */
    open var capital: Municipality? = capital
        internal set

    /** Government of this soverign area, consisting of executive, legislative, and judicial branches. */
    var government: Government? = government
        internal set

    override var name: String = fullName
        get() = fullName

    override fun toJson() = JSONObject(fullName, listOf(
        JSONObject("fullName",      fullName),
        JSONObject("commonName",    commonName),
        JSONObject("squareMileage", squareMileage),
        JSONObject("population",    population),
        JSONObject("demographics",  demographics),
        JSONObject("descriptors",   descriptors.map { it.name}.toList()),
        JSONObject("capital",       capital?.fullName),
        JSONObject("government",    government?.toJson()),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        fullName      = json.requireString("fullName", "full_name", "name")
        commonName    = json.requireString("commonName", "common_name", "name")
        squareMileage = json.findDouble("squareMileage", "landArea", "square_mileage", "land_area").getOrElse { 0.0 }
        population    = json.findInt("population") { 0 }!!
        demographics  = emptyMap()
        capital       = engine.MAP_MANAGER.matchMunicipality(json.findString(listOf("capital", "countySeat", "county_seat")) { "" }!!).getOrNull()
        descriptors   = json.findArray("descriptors") { emptyList<String>() }!!.map { engine.MAP_MANAGER.matchDescriptor(it as String) }.filter { it.isPresent }.map { it.get() }.toSet()
        if (government != null)
            government!!.fromJson(json.requireJson("government"))
        else government = Government(engine, json.requireJson("government"))
    }
}
