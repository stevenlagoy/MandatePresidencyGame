package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

class EconomyManager(
    engine: Engine
) : EngineBound(engine) {

    val banks = mutableListOf<Bank>()
    val corporations = mutableListOf<Corporation>()


}
