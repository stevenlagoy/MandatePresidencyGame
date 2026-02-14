package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.demographics.BlocKT

abstract class MapEntity {
    abstract val fullName: String
    abstract val commonName: String
    abstract var population: Int
    abstract val squareMileage: Double
    abstract val descriptors: Set<Descriptor>
    abstract val demographics: Map<BlocKT, Double>
}
