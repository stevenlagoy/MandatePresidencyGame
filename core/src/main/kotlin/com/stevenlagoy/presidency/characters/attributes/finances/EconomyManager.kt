package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.Engine

class EconomyManager(
    protected val engine: Engine
) {

    val banks = mutableListOf<Bank>()
    val corporations = mutableListOf<Corporation>()


}
