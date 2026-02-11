package com.stevenlagoy.presidency.characters

import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.characters.attributes.names.Name
import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.characters.attributes.CharacterModel
import com.stevenlagoy.presidency.core.TimeManager
import com.stevenlagoy.presidency.data.Repr
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.jsonic.JSONObject
import java.time.LocalDate

class CharacterKT(
    val demographics: Demographics,
    val name: Name,
    val birthplace: Municipality,
    val currentLocation: Municipality,
    val residence: Municipality,
    val birthday: LocalDate,
    val appearance: CharacterModel
) : Repr<CharacterKT>, Jsonic<CharacterKT> {

    fun age(tm: TimeManager): Int {
        return tm.yearsAgo(birthday)
    }

    override fun fromRepr(repr: String): CharacterKT {
        return this
    }

    override fun toRepr(): String {
        val repr = """
            ${Character::class.simpleName}:[
                demographics:${demographics.toRepr()}
                name:${name.toRepr()}
                birthplace:${birthplace.toRepr()}
                currentLocation:${currentLocation.toRepr()}
                residence:${residence.toRepr()}
                birthday:${birthday}
                appearance:${appearance.toRepr()}
            ];
        """
        return repr
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
        fields.add(JSONObject("birthplace", birthplace.getNameWithCountyAndState()))
        fields.add(JSONObject("current_location", currentLocation.getNameWithCountyAndState()))
        fields.add(JSONObject("residence", residence.getNameWithCountyAndState()))
        fields.add(JSONObject("birthday", birthday.toString()))
        fields.add(appearance.toJson())
        return JSONObject(hashCode().toString(), fields)
    }

}
