package com.stevenlagoy.presidency.politics

import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.map.Nation
import com.stevenlagoy.presidency.map.State
import com.stevenlagoy.presidency.politics.conventions.Convention
import java.io.File
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Party(
    var id: Uuid,
    var name: String,
    var adjective: String,
    var abbreviation: String,
    var colorCodes: List<String>? = null,
    var symbol: String? = null,
    var iconFile: File? = null,
) {

    var nationalChairperson: PoliticalActor? = null
    var boardMembers: Set<PoliticalActor>? = null
    var members = mutableSetOf<PoliticalActor>()

    val alignment: Pair<Int, Int> = Pair(0, 0)

    var nationalConvention: Convention? = null

    var nationalGoverningBody: PartyGoverningBody? = null
    val headquarters = nationalGoverningBody?.headquarters
    var stateGoverningBodies: Map<State, PartyGoverningBody> = mapOf()
    var localGoverningBodies: Map<MapEntity, PartyGoverningBody> = mapOf()
}
