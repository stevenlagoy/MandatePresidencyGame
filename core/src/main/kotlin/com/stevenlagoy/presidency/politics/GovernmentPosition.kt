package com.stevenlagoy.presidency.politics

import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.map.MapEntity

class GovernmentPosition(
    var name: String,
    var constituency: MapEntity,
    var federalLevel: FederalLevel,
    var holder: PoliticalActor
) {
}
