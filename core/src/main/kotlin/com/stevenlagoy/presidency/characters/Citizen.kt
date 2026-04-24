package com.stevenlagoy.presidency.characters

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.attributes.CharacterAppearance
import com.stevenlagoy.presidency.characters.attributes.Family
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialProfile
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality
import java.time.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Base class for any kind of in-game character
 * @property MANAGERS
 * @property id
 * @property name
 * @property birthday
 * @property age
 * @property demographics
 * @property appearance
 * @property family
 * @property origin
 * @property location
 * @property residence
 * @property financialProfile
 */
@OptIn(ExperimentalUuidApi::class)
open class Citizen(
    val MANAGERS: Engine.Managers,
    val id: Uuid,
    var name: PersonalName,
    birthday: LocalDate,
    val demographics: Demographics,
    val appearance: CharacterAppearance,
    val family: Family,
    var origin: Municipality,
    var location: Municipality,
    var residence: Municipality,
    var financialProfile: FinancialProfile?,
) : Jsonic<Citizen> {

    companion object {
        /** Minimum age of a Character. */
        const val MIN_AGE = 0
        /** Maximum age of a Character. */
        const val MAX_AGE = 120
    }

    var birthday: LocalDate = birthday
        set(value) {
            val years = MANAGERS.TIME_MANAGER.yearsAgo(value)
            field = when {
                years > MAX_AGE -> MANAGERS.TIME_MANAGER.dateYearsAgo(MAX_AGE.toLong())
                years < MIN_AGE -> MANAGERS.TIME_MANAGER.currentDate.toLocalDate()
                else -> value
            }
        }

    val age: Int get() = MANAGERS.TIME_MANAGER.yearsAgo(birthday)

    override fun fromJson(json: JSONObject) = this.apply {
        name.fromJson(json.get("name") as JSONObject)
        birthday = LocalDate.parse(json.get("birthday") as String)
        demographics.fromJson(json.get("demographics") as JSONObject)
        appearance.fromJson(json.get("appearance") as JSONObject)
        family.fromJson(json.get("family") as JSONObject)
        origin = MANAGERS.MAP_MANAGER.getMunicipalityByUniqueName(json.get("origin_municipality") as String)!!
        location = MANAGERS.MAP_MANAGER.getMunicipalityByUniqueName(json.get("location_municipality") as String)!!
        residence = MANAGERS.MAP_MANAGER.getMunicipalityByUniqueName(json.get("residence_municipality") as String)!!
        financialProfile = FinancialProfile(json.get("financial_profile") as JSONObject)
    }

    override fun toJson() = JSONObject(id.toString(), listOf(
        JSONObject("name", name.toJson()),
        JSONObject("birthday", birthday.toString()),
        JSONObject("demographics", demographics.toJson()),
        JSONObject("appearance", appearance.toJson()),
        JSONObject("family", family.toJson()),
        JSONObject("origin_municipality", origin.uniqueName),
        JSONObject("location_municipality", location.uniqueName),
        JSONObject("residence_municipality", residence.uniqueName),
        JSONObject("financial_profile", financialProfile?.toJson())
    ))
}
