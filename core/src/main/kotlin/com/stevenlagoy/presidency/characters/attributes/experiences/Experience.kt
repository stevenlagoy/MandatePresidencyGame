package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.map.Municipality
import java.time.LocalDate

open class Experience(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val description: String,
    val location: Municipality,
)
