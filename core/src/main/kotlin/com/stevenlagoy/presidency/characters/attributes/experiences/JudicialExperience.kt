package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.branches.JudicialBranch
import java.time.LocalDate

class JudicialExperience(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    location: Municipality,
    val court: JudicialBranch,
) : Experience(name, startDate, endDate, description, location) {
}
