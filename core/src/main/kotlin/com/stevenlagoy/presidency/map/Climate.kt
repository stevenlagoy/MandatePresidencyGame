package com.stevenlagoy.presidency.map

import com.badlogic.gdx.graphics.Color
import com.stevenlagoy.presidency.util.degreesCelsiusToFahrenheit

/**
 * @property precipitation Average annual precipitation in inches
 * @property summerTemperature Normal mean temperature in July
 * @property vegetation Tree cover as a percentage of land from 0 to 100
 * @property winterTemperature Normal mean temperature in January
 */
data class Climate(
    val precipitation: Precipitation = Precipitation.MESIC,
    val summerTemperature: Double = 74.4,
    val vegetation: Vegetation = Vegetation.PLAIN,
    val winterTemperature: Double = 33.2,
) {

    constructor(precipitation: Int, summerMeanTemperature: Int, vegetation: Int, winterMeanTemperature: Int) : this(
        Precipitation.fromShade(precipitation),
        temperatureFahrenheitFromShade(summerMeanTemperature),
        Vegetation.fromShade(vegetation),
        temperatureFahrenheitFromShade(winterMeanTemperature)
    )

    constructor(precipitation: Float, summerMeanTemperature: Float, vegetation: Float, winterMeanTemperature: Float) : this(
        (precipitation * 255).toInt(),
        (summerMeanTemperature * 255).toInt(),
        (vegetation * 255).toInt(),
        (winterMeanTemperature * 255).toInt(),
    )

    constructor(color: Color) : this(
        color.a, color.r, color.g, color.b
    )

    companion object {
        fun temperatureCelsiusFromShade(shade: Int): Double {
            return (shade / 255) * 60.0 - 20.0
        }
        fun temperatureFahrenheitFromShade(shade: Int): Double = degreesCelsiusToFahrenheit(temperatureCelsiusFromShade(shade))
    }

    enum class Precipitation(
        val minAnnualInches: Int,
        val maxAnnualInches: Int,
        val shade: Int,
    ) {
        DESERT     ( 0,  8, 0x20),
        SEMI_ARID  ( 8, 15, 0x40),
        SUB_MESIC  (15, 30, 0x60),
        MESIC      (30, 50, 0x80),
        HUMID      (50, 65, 0xA0),
        SUPER_HUMID(65, 80, 0xC0),
        PERHUMID   (80, Int.MAX_VALUE, 0xE0);
        init {
            require(minAnnualInches >= 0 && maxAnnualInches >= 0) { "Annual inches of rainfall must be a positive number" }
            require(minAnnualInches <= maxAnnualInches) { "Minimum annual inches of rainfall must be less than the maximum annual inches" }
        }
        companion object {
            val colorChannel: Color = Color.BLUE
            fun fromShade(shade: Int): Precipitation {
                if (shade < 0) return DESERT
                for (precipitation in entries) {
                    if (shade in precipitation.minAnnualInches..precipitation.maxAnnualInches) return precipitation
                }
                return PERHUMID
            }
        }
    }

    enum class Vegetation(
        val minTreeCoverage: Int,
        val maxTreeCoverage: Int,
        val shade: Int,
    ) {
        BARREN     ( 0,   2, 0x1F),
        SCRUB      ( 2,   6, 0x3F),
        PLAIN      ( 6,  15, 0x5F),
        SAVANNA    (15,  25, 0x7F),
        WOODLAND   (25,  40, 0x9F),
        TIMBERLAND (40,  55, 0xBF),
        FOREST     (55,  65, 0xDF),
        DEEP_FOREST(65, 100, 0xFF);
        init {
            require(minTreeCoverage in 0..100) { "Minimum tree coverage must be between 0 and 100" }
            require(maxTreeCoverage in 0..100) { "Maximum tree coverage must be between 0 and 100" }
            require(minTreeCoverage <= maxTreeCoverage) { "Minimum tree coverage must be less than the maximum tree coverage" }
        }
        companion object {
            val colorChannel: Color = Color.GREEN
            fun fromShade(shade: Int): Vegetation {
                if (shade < 0) return BARREN
                if (shade > 100) return DEEP_FOREST
                for (vegetation in entries) {
                    if (shade in vegetation.minTreeCoverage..vegetation.maxTreeCoverage) return vegetation
                }
                return BARREN
            }
        }
    }
}
