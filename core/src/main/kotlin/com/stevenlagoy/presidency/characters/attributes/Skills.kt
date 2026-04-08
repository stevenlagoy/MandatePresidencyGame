package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.data.Repr

/**
 * Track the skills of a Character including Legislative, Executive, and Judicial skills.
 * Also allows additive or multiplicative modifiers, and a value for overall aptitude.
 *
 * @property baseLegislative Base Legislative Skill, representing ability to plan effectively, leverage advantages, and make lasting decisions. Ranges [[0, 100]].
 * @property baseExecutive Base Executive Skill, representing ability to make decisions quickly, apply charismatic persuasion, and execute strong or decisive actions. Ranges [[0, 100]].
 * @property baseJudicial Base Judicial Skill, representing ability to inspect facts, reason through problems, apply precedent, and make informed decisions. Ranges [[0, 100]].
 * @property addLegislative Additive modifier to [legislativeSkill]. A +1 modifier results in skill equal to [baseLegislative] + 1. Applied after [multLegislative].
 * @property addExecutive Additive modifier to [executiveSkill]. A +1 modifier results in skill equal to [baseExecutive] + 1. Applied after [multExecutive].
 * @property addJudicial Additive modifier to [judicialSkill]. A +1 modifier results in skill equal to [baseJudicial] + 1. Applied after [multJudicial].
 * @property multLegislative Multiplicative modifier to [legislativeSkill]. A 200% (2.0) modifier results in skill equal to [baseLegislative] * 2.0. Applied before [addLegislative].
 * @property multExecutive Multiplicative modifier to [executiveSkill]. A 200% (2.0) modifier results in skill equal to [baseExecutive] * 2.0. Applied before [addExecutive].
 * @property multJudicial Multiplicative modifier to [judicialSkill]. A 200% (2.0) modifier results in skill equal to [baseJudicial] * 2.0. Applied before [addJudicial].
 * @property legislativeSkill Legislative Skill, representing ability to plan effectively, leverage advantages, and make lasting decisions. Calculated as [baseLegislative] * [multLegislative] + [addLegislative]. Ranges [[0, 100]].
 * @property executiveSkill Executive Skill, representing ability to make decisions quickly, apply charismatic persuasion, and execute strong or decisive actions. Calculated as [baseExecutive] * [multExecutive] + [addExecutive]. Ranges [[0, 100]].
 * @property judicialSkill Judicial Skill, representing ability to inspect facts, reason through problems, apply precedent, and make informed decisions. Calculated as [baseJudicial] * [multJudicial] + [addJudicial]. Ranges [[0, 100]].
 * @property aptitude Overall aptitude, calculated as the sum [baseLegislative] + [baseExecutive] + [baseJudicial]. Ranges [[0, 300]].
 *
 * @author Steven LaGoy
 */
data class Skills(
    var baseLegislative: Int    = 50,
    var baseExecutive:   Int    = 50,
    var baseJudicial:    Int    = 50,
    var addLegislative:  Int    = 0,
    var addExecutive:    Int    = 0,
    var addJudicial:     Int    = 0,
    var multLegislative: Double = 1.0,
    var multExecutive:   Double = 1.0,
    var multJudicial:    Double = 1.0,
) : Jsonic<Skills>
{

    constructor(json: JSONObject) : this() { fromJson(json) }

    val legislativeSkill: Int
        get() = (baseLegislative * multLegislative + addLegislative).toInt().coerceIn(0, 100)

    val executiveSkill: Int
        get() = (baseExecutive * multExecutive + addExecutive).toInt().coerceIn(0, 100)

    val judicialSkill: Int
        get() = (baseJudicial * multJudicial + addJudicial).toInt().coerceIn(0, 100)

    val aptitude: Int
        get() = baseLegislative + baseExecutive + baseJudicial

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    override fun toString() = """[
        baseLegislative: $baseLegislative,
        baseExecutive: $baseExecutive,
        baseJudicial: $baseJudicial,
        addLegislative: $addLegislative,
        addExecutive: $addExecutive,
        addJudicial: $addJudicial,
        multLegislative: $multLegislative,
        multExecutive: $multExecutive,
        multJudicial: $multJudicial,
        aptitude: $aptitude,
    ]""".trimIndent()

    override fun toJson() = JSONObject(this.hashCode().toString(), mutableListOf<JSONObject>().run {
        add(JSONObject("base_legislative", baseLegislative))
        add(JSONObject("base_executive",   baseExecutive))
        add(JSONObject("base_judicial",    baseJudicial))
        add(JSONObject("add_legislative",  addLegislative))
        add(JSONObject("add_executive",    addExecutive))
        add(JSONObject("add_judicial",     addJudicial))
        add(JSONObject("mult_legislative", multLegislative))
        add(JSONObject("mult_executive",   multExecutive))
        add(JSONObject("mult_judicial",    multJudicial))
    })

    override fun fromJson(json: JSONObject) = this.apply {
        baseLegislative = json.get("baseLegislative") as? Int ?: baseLegislative
        baseExecutive   = json.get("baseExecutive")   as? Int ?: baseExecutive
        baseJudicial    = json.get("baseJudicial")    as? Int ?: baseJudicial
        addLegislative  = json.get("addLegislative")  as? Int ?: addLegislative
        addExecutive    = json.get("addExecutive")    as? Int ?: addExecutive
        addJudicial     = json.get("addJudicial")     as? Int ?: addJudicial
        multLegislative = json.get("multLegislative") as? Double ?: multLegislative
        multExecutive   = json.get("multExecutive")   as? Double ?: multExecutive
        multJudicial    = json.get("multJudicial")    as? Double ?: multJudicial
    }

}
