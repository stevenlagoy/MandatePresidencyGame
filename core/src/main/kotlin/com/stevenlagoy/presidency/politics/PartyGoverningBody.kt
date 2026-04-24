package com.stevenlagoy.presidency.politics

import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.map.Municipality

class PartyGoverningBody(
    val party: Party,
    var location: MapEntity,
    var headquarters: Municipality,
    var chairPerson: PoliticalActor? = null,
    var boardMembers: Set<PoliticalActor>? = null,
    var registrees: Set<PoliticalActor>? = null,
) {}
