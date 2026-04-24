package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.demographics.Bloc

data class Descriptor (
    var name: String = "",
    var description: String = "",
    var members: List<MapEntity> = emptyList(),
    var effects: Map<Bloc, Double> = emptyMap(),
)
