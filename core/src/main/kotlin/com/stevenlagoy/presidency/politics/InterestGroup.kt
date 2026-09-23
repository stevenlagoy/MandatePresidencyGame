package com.stevenlagoy.presidency.politics

import com.stevenlagoy.presidency.map.entities.MapEntity
import com.stevenlagoy.presidency.map.entities.Place

class InterestGroup(
    var name: String,
    var alignment: Pair<Int, Int>,
) {
    var headquarters: Place? = null
    var operationalArea: MapEntity? = null

    var influence: Int = 0
}
