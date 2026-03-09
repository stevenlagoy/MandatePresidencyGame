package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.Engine

class EconomyManager(
    private val engine: Engine
) {

    val banks = mutableListOf<Bank>()
    val corporations = mutableListOf<Corporation>()


}
