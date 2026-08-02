package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.government.Government
import com.stevenlagoy.presidency.politics.Party

/**
 * SoverignArea defines an area which has some amount of home rule over a certain geographic area
 * and population. This is exercised through an elected or appointed government.
 */
abstract class SoverignArea(
    // EngineBound
    engine: Engine,
    // MapEntity
    fullName: String = "",
    commonName: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    descriptors: Set<Descriptor> = emptySet(),
    region: RegionData? = null,
    // HasGovernment
    government: Government = Government(engine),
    _capital: Municipality? = null,
    override val electionResults: MutableSet<ElectionResult> = mutableSetOf(),
    override val partiesPresent: MutableSet<Party> = mutableSetOf(),
    override val partyCloutFactors: MutableSet<(party: Party) -> Double> = mutableSetOf(),
) : MapEntity(engine, fullName, squareMileage, population, demographics, descriptors, region), HasGovernment {

    /** Full name of this map entity, possibly including a qualifier like 'State of', 'County', or 'Commonwealth of'. */
    var fullName: String = fullName
        internal set
    override var name: String = fullName
        get() = fullName

    /** Common name of this map entity, not including qualifiers like 'State of', 'County', or 'Commonwealth of'.*/
    var commonName: String = commonName
        internal set

    /** Government of this soverign area, consisting of executive, legislative, and judicial branches. */
    override var government: Government = government
        internal set

    /** Municipality which serves as the capital of this soverign area. Other names may be used for the same concept, like county seats. */
    override lateinit var capital: Municipality

    init {
        if (_capital != null) capital = _capital
    }

    override fun toJson() = super.toJson().merge(
        politicsToJson(),
        JSONObject("fullName", fullName),
        JSONObject("commonName", commonName),
    ).apply { key = fullName }

    override fun fromJson(json: JSONObject) = apply {
        super.fromJson(json)
        populateFromJson(json, engine)
        fullName      = json.requireString("fullName", "full_name", "name")
        commonName    = json.requireString("commonName", "common_name", "name")
    }
}
