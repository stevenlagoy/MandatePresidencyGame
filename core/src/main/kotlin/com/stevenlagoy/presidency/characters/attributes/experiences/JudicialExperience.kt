package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.politics.branches.JudicialBranch
import java.time.LocalDate

class JudicialExperience(
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    val court: JudicialBranch,
) : Experience(startDate, endDate, description) {
}
