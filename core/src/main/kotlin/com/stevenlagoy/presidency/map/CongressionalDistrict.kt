package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Government

class CongressionalDistrict(
    managers: Engine.Managers,
    var state: State,
    var districtNumber: Int,
    var representative: PoliticalActor,
    override var fullName: String,
    override var commonName: String,
    override var uniqueName: String,
    override var population: Int,
    override var squareMileage: Double,
    override var descriptors: Set<Descriptor>,
    override var demographics: Map<Bloc, Double>,
    // Congressional Districts do not have Capitals or Governments
) : MapEntity(managers) {

    override var capital: Municipality? = null

    override var government: Government? = null

    var officeID = "${state.FIPS}${districtNumber.toString().padStart(2, '0')}"

    override fun toJson() = super.toJson().apply {
    }

    override fun fromJson(json: JSONObject) = super.fromJson(json).apply {
    }

}
