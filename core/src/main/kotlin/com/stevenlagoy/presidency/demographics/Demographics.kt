package com.stevenlagoy.presidency.demographics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.util.Logger

class Demographics (
    generation: Bloc,
    religion: Bloc,
    raceEthnicity: Bloc,
    presentation: Bloc
) : Jsonic<Demographics> {

    constructor(other: Demographics) : this(other.generation, other.religion, other.raceEthnicity, other.presentation)

    constructor(
        DEMOGRAPHICS_MANAGER: DemographicsManager,
        generationBlocName: String,
        religionBlocName: String,
        raceEthnicityBlocName: String,
        presentationBlocName: String
    ) : this(
        DEMOGRAPHICS_MANAGER.matchBlocName(generationBlocName)!!,
        DEMOGRAPHICS_MANAGER.matchBlocName(religionBlocName)!!,
        DEMOGRAPHICS_MANAGER.matchBlocName(raceEthnicityBlocName)!!,
        DEMOGRAPHICS_MANAGER.matchBlocName(presentationBlocName)!!,
    )

    var generation = generation
        set(value) {
            if (value.category != DemographicCategory.GENERATION) {
                Logger.log("INVALID BLOC GROUP", "The bloc \"${value.name}\" of type ${value.category} was assigned to a demographic category of type GENERATION.", Exception())
                return
            }
            field = value
        }

    var religion = religion
        set(value) {
            if (value.category != DemographicCategory.RELIGION) {
                Logger.log("INVALID BLOC GROUP", "The bloc \"${value.name}\" of type ${value.category} was assigned to a demographic category of type RELIGION.", Exception())
                return
            }
            field = value
        }

    var raceEthnicity = raceEthnicity
        set(value) {
            if (value.category != DemographicCategory.RACE_ETHNICITY) {
                Logger.log("INVALID BLOC GROUP", "The bloc \"${value.name}\" of type ${value.category} was assigned to a demographic category of type RACE_ETHNICITY.", Exception())
                return
            }
            field = value
        }

    var presentation = presentation
        set(value) {
            if (value.category != DemographicCategory.PRESENTATION) {
                Logger.log("INVALID BLOC GROUP", "The bloc \"${value.name}\" of type ${value.category} was assigned to a demographic category of type PRESENTATION.", Exception())
                return
            }
            field = value
        }

    val blocs: Set<Bloc> get() = setOf(generation, religion, raceEthnicity, presentation)

    override fun toString() = """[
        generation: $generation,
        religion: $religion,
        raceEthnicity: $raceEthnicity,
        presentation: $presentation,
    ]""".trimIndent()

    override fun toJson() = JSONObject(this.hashCode().toString(), mapOf(
        "generation" to generation.name,
        "religion" to religion.name,
        "race_ethnicity" to raceEthnicity.name,
        "presentation" to presentation.name,
    ))

    override fun fromJson(json: JSONObject): Demographics {
        return this
    }

}
