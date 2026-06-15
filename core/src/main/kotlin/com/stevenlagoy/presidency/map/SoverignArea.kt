package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Government

abstract class SoverignArea(
    engine: Engine,
    fullName: String,
    commonName: String,
    squareMileage: Double,
    population: Int,
    demographics: Map<Bloc, Double>,
    descriptors: Set<Descriptor>,
    region: RegionData?,
    capital: Municipality?,
    government: Government?,
) : MapEntity(engine, fullName, squareMileage, population, demographics, descriptors, region) {

    /** Full name of this map entity, possibly including a qualifier like 'State of', 'County', or 'Commonwealth of'. */
    var fullName: String = fullName
        internal set

    /** Common name of this map entity, not including qualifiers like 'State of', 'County', or 'Commonwealth of'.*/
    var commonName: String = commonName
        internal set

    /** Municipality which serves as the capital of this soverign area. Other names may be used for the same concept, like county seats. */
    open var capital: Municipality? = capital
        internal set

    /** Government of this soverign area, consisting of executive, legislative, and judicial branches. */
    var government: Government? = government
        internal set

    override var name: String = fullName
        get() = fullName
}
