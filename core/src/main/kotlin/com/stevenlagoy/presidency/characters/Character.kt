package com.stevenlagoy.presidency.characters

import java.time.LocalDate

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

import com.stevenlagoy.presidency.characters.attributes.CharacterAppearance
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName
import com.stevenlagoy.presidency.core.Engine
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
 * @property managers       Reference to main game Engine's Managers.
 *
 * @constructor Create a Character with given property values.
 *
 * @author Steven LaGoy
 */
open class Character(
    val managers: Engine.Managers,
    val demographics: Demographics,
    val name: PersonalName,
    val origin: Municipality,
    var location: Municipality,
    var residence: Municipality,
    birthday: LocalDate,
    val appearance: CharacterAppearance,
) : Jsonic<Character>
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
            val years = managers.TimeManager().yearsAgo(value)
            field = when {
                years > MAX_AGE -> managers.TimeManager().dateYearsAgo(MAX_AGE.toLong())
                years < MIN_AGE -> managers.TimeManager().currentDate.toLocalDate()
                else -> value
            }
        }

    val age: Int
        get() = managers.TimeManager().yearsAgo(birthday)

    override fun toString() = """{
        demographics: $demographics,
        name: $name,
        birthplace: ${origin.uniqueName},
        location: ${location.uniqueName},
        residence: ${residence.uniqueName},
        birthday: $birthday,
        appearance: $appearance,
    }""".trimIndent()

    override fun fromJson(json: JSONObject): Character {
        demographics.fromJson(json.get("demographics") as JSONObject)
        name.fromJson(json.get("name") as JSONObject)
        appearance.fromJson(json.get("appearance") as JSONObject)
        return this
    }

    override fun toJson() = JSONObject(hashCode().toString(), mapOf(
        "demographics" to demographics.toString(),
        "name"         to name.toString(),
        "origin"       to origin.uniqueName,
        "location"     to location.uniqueName,
        "residence"    to residence.uniqueName,
        "birthday"     to birthday.toString(),
        "appearance"   to appearance.toString(),
    ))
}
