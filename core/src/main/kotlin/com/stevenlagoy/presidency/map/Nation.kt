package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.demographics.BlocKT
import com.stevenlagoy.presidency.util.FilePaths

object Nation: MapEntity() {

    private val data by lazy(LazyThreadSafetyMode.NONE) {
    }

    override val fullName: String = "the United States of America"

    override val commonName: String = "the United States"

    override var population: Int = 0

    override val squareMileage: Double = 0.0

    override val descriptors: Set<Descriptor> = setOf()

    override val demographics: Map<BlocKT, Double> = mapOf()

}
