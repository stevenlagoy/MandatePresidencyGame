package com.stevenlagoy.presidency.demographics

data class DemographicsKT (
    val generation: BlocKT,
    val religion: BlocKT,
    val raceEthnicity: BlocKT,
    val presentation: BlocKT
) {

    val blocs: Array<BlocKT> = arrayOf(generation, religion, raceEthnicity, presentation)

}
