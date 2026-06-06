package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.Chamber
import java.time.LocalDate

class LegislativeExperience(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate?,
    description: String,
    location: Municipality,
    chamber: Chamber,
) : Experience(name, startDate, endDate, description, location) {
}
