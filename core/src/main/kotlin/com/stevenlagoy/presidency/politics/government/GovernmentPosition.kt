package com.stevenlagoy.presidency.politics.government

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.map.Nation
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GovernmentPosition(
    engine: Engine,
    var title: String = "",
    var constituency: MapEntity = Nation,
    var federalLevel: FederalLevel = FederalLevel.FEDERAL,
    var holder: PoliticalActor? = null,
) : JSONSerializable<GovernmentPosition>, EngineBound(engine) {

    override fun toJson() = JSONObject(title, listOf(
        JSONObject("name", title),
        JSONObject("constituency", constituency.name),
        JSONObject("federalLevel", federalLevel.name),
        JSONObject("holder", holder?.id.toString()),
    ))

    override fun fromJson(json: JSONObject) = apply {
        title = json.requireString("name")
        constituency = engine.MAP_MANAGER.matchMapEntity(json.requireString("constituency")).orElseThrow()
        federalLevel = FederalLevel.valueOf(json.requireString("federalLevel"))
        holder = engine.CHARACTER_MANAGER.matchCitizenById(json.requireString("holder")).orElseThrow() as PoliticalActor
    }

}
