package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Government
import kotlin.collections.listOf
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CongressionalDistrict(
    MANAGERS: Engine.Managers,
    var state: State,
    var name: String = "",
    var districtNumber: Int = 0,
    var representative: PoliticalActor? = null,
    override var fullName: String = "",
    override var commonName: String = "",
    override var uniqueName: String = "",
    override var population: Int = 0,
    override var squareMileage: Double = 0.0,
    override var descriptors: Set<Descriptor> = setOf(),
    override var demographics: Map<Bloc, Double> = mapOf(),
    // Congressional Districts do not have Capitals or Governments
) : MapEntity(MANAGERS) {

    constructor(MANAGERS: Engine.Managers, state: State, json: JSONObject) : this(MANAGERS, state) {
        fromJson(json)
    }

    override var capital: Municipality? = null

    override var government: Government? = null

    var officeID = "${state.FIPS}${districtNumber.toString().padStart(2, '0')}"

    override fun toJson() = JSONObject(uniqueName, super.toJson().asList + listOf(
        JSONObject("state", state.uniqueName),
        JSONObject("district_number", districtNumber.toString()),
        JSONObject("representative", representative?.name?.legalName),
        JSONObject("name", name),
        JSONObject("descriptors", descriptors.toList())
    ))

    override fun fromJson(json: JSONObject) = super.fromJson(json).apply {
        state = MANAGERS.MAP_MANAGER.states.find { it.uniqueName == json.get("state").toString() }!!
        districtNumber = json.get("district_num").toString().toInt() // TODO Make JSON terms consistent
        population = json.get("population").toString().toInt()
        squareMileage = json.get("land_area").toString().toDouble() // TODO Make JSON terms consistent
    }
}
