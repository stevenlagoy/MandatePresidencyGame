package com.stevenlagoy.presidency.politics

import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.map.Municipality

class InterestGroup(
    var name: String,
    var alignment: Pair<Int, Int>,
) {
    var headquarters: Municipality? = null
    var operationalArea: MapEntity? = null

    var influence: Int = 0
}
