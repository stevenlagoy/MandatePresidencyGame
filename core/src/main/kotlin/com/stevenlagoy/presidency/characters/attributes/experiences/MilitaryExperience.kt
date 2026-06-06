package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.map.Municipality
import java.time.LocalDate

class MilitaryExperience(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    location: Municipality,
    val branch: MilitaryBranch,
    val rank: String,
) : Experience(name, startDate, endDate, description, location) {
}
