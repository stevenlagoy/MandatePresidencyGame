package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import kotlin.collections.mapOf

data class CharacterAppearance(
    var sex:                 Sex = Sex.FEMALE,
    var age:                 Double = 40.0,
    var weightLbs:           Double = 190.0,
    var heightInches:        Double = 65.0,
    var bustInches:          Double = 40.0,
    var headHeight:          Double = 0.5,
    var headWidth:           Double = 0.5,
    var headLength:          Double = 0.5,
    var neckLength:          Double = 0.5,
    var neckWidth:           Double = 0.5,
    var complexion:          Double = 0.5, // Pale -> Ruddy
    var earAngle:            Double = 0.5,
    var earBend:             Double = 0.5,
    var earShape:            Double = 0.5,
    var earSize:             Double = 0.5,
    var cheekDefinition:     Double = 0.5,
    var cheekForward:        Double = 0.5,
    var cheekHeight:         Double = 0.5,
    var chinDefinition:      Double = 0.5,
    var chinForward:         Double = 0.5,
    var chinHeight:          Double = 0.5,
    var chinWidth:           Double = 0.5,
    var foreheadHeight:      Double = 0.5,
    var foreheadAngle:       Double = 0.5,
    var browAngle:           Double = 0.5,
    var browForward:         Double = 0.5,
    var browWidth:           Double = 0.5,
    var jawDefinition:       Double = 0.5,
    var jawForward:          Double = 0.5,
    var jawHeight:           Double = 0.5,
    var jawWidth:            Double = 0.5,
    var templeDefinition:    Double = 0.5,
    var eyeAngle:            Double = 0.5,
    var eyeColor:            String = "Brown",
    var eyeForward:          Double = 0.5,
    var interEyeDistance:    Double = 0.5,
    var eyeHeight:           Double = 0.5,
    var eyeSize:             Double = 0.5,
    var eyeOpenness:         Double = 0.5,
    var eyebrowFullness:     Double = 0.5,
    var noseForward:         Double = 0.5,
    var noseHeight:          Double = 0.5,
    var noseLength:          Double = 0.5,
    var noseSize:            Double = 0.5,
    var noseBridgeForward:   Double = 0.5,
    var noseAngle:           Double = 0.5,
    var nostrilHeight:       Double = 0.5,
    var nostrilWidth:        Double = 0.5,
    var lipFullness:         Double = 0.5,
    var lipSize:             Double = 0.5,
    var mouthForward:        Double = 0.5,
    var mouthHeight:         Double = 0.5,
    var mouthWidth:          Double = 0.5,
    val mouthOpenness:       Double = 0.5,
    val hairStyle:           String = "Wavy",
    val hairColor:           String = "Brown",
    val beardStyle:          String = "No Beard",
    val beardColor:          String = "Brown",
    val skinColor:           String = "White",
    val muscularity:         Double = 0.5,
    val shoulderWidthInches: Double = 15.0,
) : Jsonic<CharacterAppearance>
{

    override fun toJson() = JSONObject(hashCode().toString(), mapOf(
        "age" to age,
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        this.age = json.get("age") as Double
    }
}
