package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.Manager
import com.stevenlagoy.presidency.demographics.Bloc
import kotlin.jvm.optionals.getOrNull
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class CongressionalDistrict(
    ENGINE: Engine,
    name: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = mapOf(),
    descriptors: Set<Descriptor> = setOf(),
    region: RegionData? = null,
    _state: State? = null,
    var districtNumber: Int = 0,
    var representative: PoliticalActor? = null,
) : MapEntity(
    ENGINE,
    name,
    squareMileage,
    population,
    demographics,
    descriptors,
    region,
) {

    lateinit var state: State
        internal set

    val officeID: String
        get() = "${state.FIPS}${districtNumber.toString().padStart(2, '0')}"

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_state != null) state = _state
    }

    override fun toJson(): JSONObject = super.toJson().merge(
        JSONObject("state", state.fullName),
        JSONObject("districtNumber", districtNumber),
        JSONObject("representative", representative?.name?.indexedName)
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        val _state = engine.MAP_MANAGER.matchState(json.requireString("state"))
        if (_state.isPresent) state = _state.get()
        districtNumber = json.requireInt("districtNumber")
        representative = if (json.hasKey("representative") && engine.CHARACTER_MANAGER.state == Manager.ManagerState.ACTIVE)
            engine.CHARACTER_MANAGER.matchCitizenById(json.requireString("representative")).getOrNull() as PoliticalActor?
            else null
    }
}
