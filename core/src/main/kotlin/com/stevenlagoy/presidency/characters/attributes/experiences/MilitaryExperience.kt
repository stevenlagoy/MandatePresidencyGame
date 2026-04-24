package com.stevenlagoy.presidency.characters.attributes.experiences

import java.time.LocalDate

class MilitaryExperience(
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    val branch: MilitaryBranch,
    val rank: String,
) : Experience(startDate, endDate, description) {
}
