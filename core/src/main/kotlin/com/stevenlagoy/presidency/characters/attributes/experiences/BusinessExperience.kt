package com.stevenlagoy.presidency.characters.attributes.experiences

import java.time.LocalDate

class BusinessExperience(
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    val companyName: String,
    val title: String,
) : Experience(startDate, endDate, description) {
}
