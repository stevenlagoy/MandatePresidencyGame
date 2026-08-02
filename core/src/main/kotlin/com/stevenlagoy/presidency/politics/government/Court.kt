package com.stevenlagoy.presidency.politics.government

import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.Municipality

class Court(
    engine: Engine,
    fullName: String = "",
    commonName: String = "",
    citation: String = "",
    _basis: Municipality? = null,
    val judges: MutableList<PoliticalActor?> = mutableListOf(),
    apellateCourt: Court? = null,
    lowerCourts: Set<Court> = emptySet(),
) : EngineBound(engine) {

    var fullName: String = fullName
        internal set

    var commonName: String = commonName
        internal set

    /**
     * How this court is cited in legal documents, similar to an abbreviation.
     */
    var citation: String = citation
        internal set

    /**
     * Location in which this court is based.
     */
    lateinit var basis: Municipality
        internal set

    /**
     * Higher court to which this court appeals. If null then this is a supreme court.
     */
    var apellateCourt: Court? = apellateCourt
        internal set

    /**
     * Courts which appeal to this court.
     */
    var lowerCourts: Set<Court> = lowerCourts
        internal set

    init {
        if (_basis != null) { basis = _basis }
    }
}
