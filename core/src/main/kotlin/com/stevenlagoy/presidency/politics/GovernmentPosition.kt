package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.MapEntity
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GovernmentPosition(
    engine: Engine,
    var name: String,
    var constituency: MapEntity,
    var federalLevel: FederalLevel,
    var holder: PoliticalActor
) : JSONSerializable<GovernmentPosition>, EngineBound(engine) {

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
        JSONObject("constituency", constituency.name),
        JSONObject("federalLevel", federalLevel.name),
        JSONObject("holder", holder.id.toString()),
    ))

    override fun fromJson(json: JSONObject) = apply {
        name = json.requireString("name")
        constituency = engine.MAP_MANAGER.matchMapEntity(json.requireString("constituency")).orElseThrow()
        federalLevel = FederalLevel.valueOf(json.requireString("federal_level"))
        holder = engine.CHARACTER_MANAGER.matchCitizenById(json.requireString("holder")).orElseThrow() as PoliticalActor
    }

}
