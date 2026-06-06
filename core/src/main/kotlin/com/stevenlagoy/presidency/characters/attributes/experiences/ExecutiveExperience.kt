package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.branches.ExecutiveBranch
import java.time.LocalDate

class ExecutiveExperience(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    location: Municipality,
    office: ExecutiveBranch,
) : Experience(name, startDate, endDate, description, location) {
}
