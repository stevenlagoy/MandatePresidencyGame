package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.politics.branches.ExecutiveBranch
import java.time.LocalDate

class ExecutiveExperience(
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    office: ExecutiveBranch,
) : Experience(startDate, endDate, description) {
}
