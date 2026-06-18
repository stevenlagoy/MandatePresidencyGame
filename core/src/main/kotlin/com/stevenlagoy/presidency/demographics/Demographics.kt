package com.stevenlagoy.presidency.demographics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.util.Logger

class Demographics (
    engine: Engine,
    generation: Bloc,
    religion: Bloc,
    raceEthnicity: Bloc,
    presentation: Bloc
) : Jsonic<Demographics>, EngineBound(engine) {

    constructor(other: Demographics) : this(other.engine, other.generation, other.religion, other.raceEthnicity, other.presentation)

    constructor(
        engine: Engine,
        generationBlocName: String,
        religionBlocName: String,
        raceEthnicityBlocName: String,
        presentationBlocName: String
    ) : this(
        engine,
        engine.DEMOGRAPHICS_MANAGER.matchBloc(generationBlocName).orElseThrow(),
        engine.DEMOGRAPHICS_MANAGER.matchBloc(religionBlocName).orElseThrow(),
        engine.DEMOGRAPHICS_MANAGER.matchBloc(raceEthnicityBlocName).orElseThrow(),
        engine.DEMOGRAPHICS_MANAGER.matchBloc(presentationBlocName).orElseThrow(),
    )

    var generation = generation
        set(value) {
            if (value.category != DemographicCategory.GENERATION) {
                Logger.error("INVALID BLOC GROUP", "The bloc \"${value.name}\" of type ${value.category} was assigned to a demographic category of type GENERATION.", Exception())
                return
            }
            field = value
        }

    var religion = religion
        set(value) {
            if (value.category != DemographicCategory.RELIGION) {
                Logger.error("INVALID BLOC GROUP", "The bloc \"${value.name}\" of type ${value.category} was assigned to a demographic category of type RELIGION.", Exception())
                return
            }
            field = value
        }

    var raceEthnicity = raceEthnicity
        set(value) {
            if (value.category != DemographicCategory.RACE_ETHNICITY) {
                Logger.error("INVALID BLOC GROUP", "The bloc \"${value.name}\" of type ${value.category} was assigned to a demographic category of type RACE_ETHNICITY.", Exception())
                return
            }
            field = value
        }

    var presentation = presentation
        set(value) {
            if (value.category != DemographicCategory.PRESENTATION) {
                Logger.error("INVALID BLOC GROUP", "The bloc \"${value.name}\" of type ${value.category} was assigned to a demographic category of type PRESENTATION.", Exception())
                return
            }
            field = value
        }

    init {
        assert(generation.category == DemographicCategory.GENERATION)
        assert(religion.category == DemographicCategory.RELIGION)
        assert(raceEthnicity.category == DemographicCategory.RACE_ETHNICITY)
        assert(presentation.category == DemographicCategory.PRESENTATION)
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

    override fun fromJson(json: JSONObject) = this.apply {
        generation = engine.DEMOGRAPHICS_MANAGER.matchBloc(json.get("generation") as String).orElseThrow()
        religion = engine.DEMOGRAPHICS_MANAGER.matchBloc(json.get("religion") as String).orElseThrow()
        raceEthnicity = engine.DEMOGRAPHICS_MANAGER.matchBloc(json.get("race_ethnicity") as String).orElseThrow()
        presentation = engine.DEMOGRAPHICS_MANAGER.matchBloc(json.get("presentation") as String).orElseThrow()
    }

}
