package com.stevenlagoy.presidency.characters

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.characters.attributes.CharacterAppearance
import com.stevenlagoy.presidency.characters.attributes.Family
import com.stevenlagoy.presidency.characters.attributes.Sex
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialProfile
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName
import com.stevenlagoy.presidency.characters.attributes.names.WesternPersonalName
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality
import java.time.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Base class for any kind of in-game character
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
    engine: Engine,
    val sex: Sex = Sex.FEMALE,
    birthday: LocalDate = LocalDate.of(1970, 1, 1),
    val demographics: Demographics = engine.DEMOGRAPHICS_MANAGER.commonDemographics,
    val family: Family = Family(engine),
    val appearance: CharacterAppearance = CharacterAppearance(),
    val name: PersonalName = WesternPersonalName(),
    var origin: Municipality = engine.MAP_MANAGER.mostPopulatedMunicipality,
    var location: Municipality = origin,
    var residence: Municipality = location,
    var financialProfile: FinancialProfile? = null,
) : JSONSerializable<Citizen>, EngineBound(engine) {

    companion object {
        /** Minimum age of a Character. */
        const val MIN_AGE = 0
        /** Maximum age of a Character. */
        const val MAX_AGE = 120
    }

    val id: Uuid = Uuid.random()

    var birthday: LocalDate = birthday
        set(value) {
            val years = engine.TIME_MANAGER.yearsAgo(value)
            field = when {
                years > MAX_AGE -> engine.TIME_MANAGER.dateYearsAgo(MAX_AGE.toLong())
                years < MIN_AGE -> engine.TIME_MANAGER.currentDate.toLocalDate()
                else -> value
            }
        }

    val age: Int get() = engine.TIME_MANAGER.yearsAgo(birthday)

    override fun fromJson(json: JSONObject) = this.apply {
        name.fromJson(json.requireJson("name"))
        birthday = LocalDate.parse(json.requireString("birthday"))
        demographics.fromJson(json.requireJson("demographics"))
        appearance.fromJson(json.requireJson("appearance"))
        family.fromJson(json.requireJson("family"))
        origin = engine.MAP_MANAGER.matchMunicipality(json.requireString("originMunicipality", "origin_municipality")).get()
        location = engine.MAP_MANAGER.matchMunicipality(json.requireString("locationMunicipality", "location_municipality")).get()
        residence = engine.MAP_MANAGER.matchMunicipality(json.requireString("residenceMunicipality", "residence_municipality")).get()
        financialProfile = FinancialProfile(engine, json.requireJson("financialProfile", "financial_profile"))
    }

    override fun toJson() = JSONObject(id.toString(), listOf(
        JSONObject("name", name.toJson()),
        JSONObject("birthday", birthday.toString()),
        JSONObject("demographics", demographics.toJson()),
        JSONObject("appearance", appearance.toJson()),
        JSONObject("family", family.toJson()),
        JSONObject("originMunicipality", origin.fullName),
        JSONObject("locationMunicipality", location.fullName),
        JSONObject("residenceMunicipality", residence.fullName),
        JSONObject("financialProfile", financialProfile?.toJson())
    ))
}
