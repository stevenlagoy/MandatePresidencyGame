package com.stevenlagoy.presidency.characters

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.attributes.CharacterModel
import com.stevenlagoy.presidency.characters.attributes.Education
import com.stevenlagoy.presidency.characters.attributes.Experience
import com.stevenlagoy.presidency.characters.attributes.Personality
import com.stevenlagoy.presidency.characters.attributes.RoleKT
import com.stevenlagoy.presidency.characters.attributes.Skills
import com.stevenlagoy.presidency.characters.attributes.names.Name
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.Position
import java.time.LocalDate
import kotlin.math.E
import kotlin.math.pow

class PoliticalActorKT(
    demographics: Demographics,
    name: Name,
    birthplace: Municipality,
    currentLocation: Municipality,
    residence: Municipality,
    birthday: LocalDate,
    appearance: CharacterModel,
    engine: Engine,
    var cash: Int,
    var education: Education,
    alignments: IntArray,
    var partyAlignment: Party,
    var experiences: List<Experience>,
    val skills: Skills,
    val positions: List<Position>,
    var personality: Personality,
    val roles: Set<RoleKT>
) : CharacterKT(demographics, name, birthplace, currentLocation, residence, birthday, appearance, engine)
{

    companion object {
        const val MIN_AGE = 20
    }

    override val minAge: Int
        get() = MIN_AGE

    val conviction: Int
        get() = 75 // TODO Evaluate conviction based on positions and alignments

    var alignments: IntArray = IntArray(2)
        set(value) {
            if (value.size < 2) throw IllegalArgumentException()
            field = value.slice(0..2).toIntArray()
        }
    fun getAuthLibAlignment(): Int { return alignments[0] }
    fun setAuthLibAlignment(authLib: Int) { this.alignments[0] = authLib }
    fun getRightLeftAlignment(): Int { return alignments[1] }
    fun setRightLeftAlignment(rightLeft: Int) { this.alignments[1] = rightLeft }

    fun getAgeMod() = 100 * E.pow(-1 * ((age - 55) / 30.0).pow(2))

    fun addCash(cash: Int) {
        this.cash += cash
    }
    operator fun plusAssign(cash: Int) {
        this.cash += cash
    }

    override fun fromRepr(repr: String): PoliticalActorKT {
        return this
    }

    override fun toRepr(): String {
        return """
            ${this::class.simpleName};[
                ${super.toRepr()}
                cash:${this.cash}
                education:${this.education.value}
                alignments:${this.alignments}
                partyAlignment:${this.partyAlignment.name}
                experiences:${this.experiences}
                skills:${this.skills}
                positions:${this.positions}
                personality:${this.personality.toRepr()}
                roles:${this.roles}
            ];
        """.trimIndent()
    }

    override fun fromJson(json: JSONObject): PoliticalActorKT {
        return this
    }

    override fun toJson(): JSONObject {
        val fields = mutableListOf<JSONObject>()
        for (field in super.toJson()) fields.add(field as JSONObject)
        fields.add(JSONObject("cash", cash))
        fields.add(JSONObject("education", education))
        fields.add(JSONObject("alignments", alignments))
        fields.add(JSONObject("partyAlignment", partyAlignment))
        fields.add(JSONObject("experiences", experiences))
        fields.add(JSONObject("skills", skills.toJson()))
        fields.add(JSONObject("positions", positions))
        fields.add(JSONObject("personality", personality.toJson()))
        fields.add(JSONObject("roles", roles))
        return JSONObject(hashCode().toString(), fields)
    }

}
