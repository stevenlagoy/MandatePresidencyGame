package com.stevenlagoy.presidency.characters.attributes.experiences

import java.time.LocalDate

abstract class Experience(
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val description: String
) {
}
