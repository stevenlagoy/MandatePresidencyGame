package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.map.Municipality
import java.time.LocalDate

class BusinessExperience(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    location: Municipality,
    val companyName: String,
    val title: String,
) : Experience(name, startDate, endDate, description, location) {
}
