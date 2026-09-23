package com.stevenlagoy.presidency.map.physiography

import com.badlogic.gdx.graphics.Color
import com.stevenlagoy.presidency.util.parseGdxColor

data class TopographyBathymetry(
    val isTopographic: Boolean,
    val height: Int,
) {
    constructor(color: Color) : this(getIsTopographic(color), getHeight(color))

    companion object {
        const val LOW_LAND_ELEVATION = -134
        const val HIGH_LAND_ELEVATION = 6132

        private val unclassifiedLand = parseGdxColor("#CFFFFF")
        private val oceanDeep = parseGdxColor("#0000FF")
        private val ocean2500 = parseGdxColor("#0040FF")
        private val ocean500 = parseGdxColor("#0080FF")
        private val ocean200 = parseGdxColor("#00C0FF")
        private val oceanShallow = parseGdxColor("#00FFFF")

        fun getIsBathymetric(color: Color): Boolean = color in listOf(oceanDeep, ocean2500, ocean500, ocean200, oceanShallow)
        fun getIsTopographic(color: Color): Boolean = !getIsBathymetric(color)

        fun getHeight(color: Color): Int {
            return when (color) {
                oceanDeep -> -5000
                ocean2500 -> -2500
                ocean500 -> -500
                ocean200 -> -200
                oceanShallow -> -1
                unclassifiedLand -> 10
                else -> (LOW_LAND_ELEVATION + ((HIGH_LAND_ELEVATION - LOW_LAND_ELEVATION) * color.g)).toInt() // color.g is a float in [0, 1]
            }
        }
    }
}
