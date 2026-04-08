package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.FederalOfficial
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Government
import kotlin.collections.mapOf

object Nation: MapEntity(Engine.Managers()) {

    override var fullName: String = "the United States of America"

    override var commonName: String = "the United States"

    override var uniqueName: String = fullName

    override var population: Int = 0

    override var squareMileage: Double = 0.0

    override var descriptors: Set<Descriptor> = setOf()

    override var demographics: Map<Bloc, Double> = mapOf()

    override var capital: Municipality? = managers.MAP_MANAGER.matchMunicipality("Washington, DC")

    var states: List<State> = emptyList()

    override val government: Government = managers.POLITICS_MANAGER.createGovernment()

    override fun toJson() = JSONObject(hashCode().toString(), mapOf(
        "full_name" to fullName,
        "common_name" to commonName,
        "population" to population,
        "square_mileage" to squareMileage,
        "descriptors" to descriptors,
        "demographics" to demographics,
        "capital" to capital?.uniqueName,
        "states" to states.map { it.FIPS },
        "government" to government.toJson()
    ))

    override fun fromJson(json: JSONObject) = this.apply {
    }

}
