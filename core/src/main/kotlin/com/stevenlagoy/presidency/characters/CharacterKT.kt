package com.stevenlagoy.presidency.characters

import java.time.LocalDate

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

import com.stevenlagoy.presidency.characters.attributes.CharacterModel
import com.stevenlagoy.presidency.characters.attributes.names.Name
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.data.Repr
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality

/**
 * Basic form of game Character, from which other types of Characters -- including Player -- inherit.
 * Contains fields common to all Characters, including Demographics, Name, and Birthday.
 *
 * @property demographics Demographics of this Character
 * @property name         Name of this Character.
 * @property origin       Municipality from which this character originates. This location can be
 *                        approximate for various reasons, including uncertainty or changes to the
 *                        map.
 * @property location     Current location of this character. Should be the nearest known
 *                        municipality to actual location, if not exact.
 * @property residence    Municipality in which this character legally resides.
 * @property birthday     Date of this character's birth, or best known approximation.
 * @property appearance   Appearance of this character.
 * @property engine       Reference to main game Engine.
 *
 * @constructor Create a Character with given property values.
 *
 * @author Steven LaGoy
 */
open class CharacterKT(
    val demographics: Demographics,
    val name: Name,
    val origin: Municipality,
    var location: Municipality,
    var residence: Municipality,
    birthday: LocalDate,
    val appearance: CharacterModel,
    val engine: Engine
) : Repr<CharacterKT>, Jsonic<CharacterKT>
{

    companion object {
        /** Minimum age of a Character. */
        const val MIN_AGE = 0
        /** Maximum age of a Character. */
        const val MAX_AGE = 120
    }

    protected open val minAge: Int
        get() = MIN_AGE
    protected open val maxAge: Int
        get() = MAX_AGE

    var birthday: LocalDate = birthday
        set(value) {
            val years = engine.TimeManager().yearsAgo(value)
            field = when {
                years > MAX_AGE -> engine.TimeManager().dateYearsAgo(MAX_AGE.toLong())
                years < MIN_AGE -> engine.TimeManager().currentDate.toLocalDate()
                else -> value
            }
        }

    val age: Int
        get() = engine.TimeManager().yearsAgo(birthday)

    override fun fromRepr(repr: String): CharacterKT {
        return this
    }

    override fun toRepr(): String {
        return """
            ${this::class.simpleName}:[
                demographics:${demographics.toRepr()}
                name:${name.toRepr()}
                birthplace:${origin.toRepr()}
                currentLocation:${location.toRepr()}
                residence:${residence.toRepr()}
                birthday:${birthday}
                appearance:${appearance.toRepr()}
            ];
        """.trimIndent()
    }

    override fun fromJson(json: JSONObject): CharacterKT {
        demographics.fromJson(json.get("demographics") as? JSONObject)
        name.fromJson(json.get("name") as? JSONObject)
        appearance.fromJson(json.get("appearance") as? JSONObject)
        return this
    }

    override fun toJson(): JSONObject {
        val fields = mutableListOf<JSONObject>()
        fields.add(demographics.toJson())
        fields.add(name.toJson())
        fields.add(JSONObject("birthplace", origin.nameWithCountyAndState))
        fields.add(JSONObject("current_location", location.nameWithCountyAndState))
        fields.add(JSONObject("residence", residence.nameWithCountyAndState))
        fields.add(JSONObject("birthday", birthday.toString()))
        fields.add(appearance.toJson())
        return JSONObject(hashCode().toString(), fields)
    }

}
