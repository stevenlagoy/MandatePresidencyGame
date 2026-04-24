package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.politics.Chamber
import java.time.LocalDate

class LegislativeExperience(
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    chamber: Chamber,
) : Experience(startDate, endDate, description) {
}
