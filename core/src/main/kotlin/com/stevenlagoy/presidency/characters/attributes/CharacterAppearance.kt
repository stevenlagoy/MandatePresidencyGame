package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

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
    var mouthOpenness:       Double = 0.5,
    var hairStyle:           String = "Wavy",
    var hairColor:           String = "Brown",
    var beardStyle:          String = "No Beard",
    var beardColor:          String = "Brown",
    var skinColor:           String = "White",
    var muscularity:         Double = 0.5,
    var shoulderWidthInches: Double = 15.0,
) : JSONSerializable<CharacterAppearance>
{

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("sex",                 sex.toString()),
        JSONObject("age",                 age),
        JSONObject("weightLbs",           weightLbs),
        JSONObject("heightInches",        heightInches),
        JSONObject("bustInches",          bustInches),
        JSONObject("headHeight",          headHeight),
        JSONObject("headWidth",           headWidth),
        JSONObject("headLength",          headLength),
        JSONObject("neckLength",          neckLength),
        JSONObject("neckWidth",           neckWidth),
        JSONObject("complexion",          complexion),
        JSONObject("earAngle",            earAngle),
        JSONObject("earBend",             earBend),
        JSONObject("earShape",            earShape),
        JSONObject("earSize",             earShape),
        JSONObject("cheekDefinition",     cheekDefinition),
        JSONObject("cheekForward",        cheekForward),
        JSONObject("cheekHeight",         cheekHeight),
        JSONObject("chinDefinition",      chinDefinition),
        JSONObject("chinForward",         chinForward),
        JSONObject("chinHeight",          chinHeight),
        JSONObject("chinWidth",           chinWidth),
        JSONObject("foreheadHeight",      foreheadHeight),
        JSONObject("foreheadAngle",       foreheadAngle),
        JSONObject("browAngle",           browAngle),
        JSONObject("browForward",         browForward),
        JSONObject("browWidth",           browWidth),
        JSONObject("jawDefinition",       jawDefinition),
        JSONObject("jawForward",          jawForward),
        JSONObject("jawHeight",           jawHeight),
        JSONObject("jawWidth",            jawWidth),
        JSONObject("templeDefinition",    templeDefinition),
        JSONObject("eyeAngle",            eyeAngle),
        JSONObject("eyeColor",            eyeColor),
        JSONObject("eyeForward",          eyeForward),
        JSONObject("interEyeDistance",    interEyeDistance),
        JSONObject("eyeHeight",           eyeHeight),
        JSONObject("eyeSize",             eyeSize),
        JSONObject("eyeOpenness",         eyeOpenness),
        JSONObject("eyebrowFullness",     eyebrowFullness),
        JSONObject("noseForward",         noseForward),
        JSONObject("noseHeight",          noseHeight),
        JSONObject("noseLength",          noseLength),
        JSONObject("noseSize",            noseSize),
        JSONObject("noseBridgeForward",   noseBridgeForward),
        JSONObject("noseAngle",           noseAngle),
        JSONObject("nostrilHeight",       nostrilHeight),
        JSONObject("nostrilWidth",        nostrilWidth),
        JSONObject("lipFullness",         lipFullness),
        JSONObject("lipSize",             lipSize),
        JSONObject("mouthForward",        mouthForward),
        JSONObject("mouthHeight",         mouthHeight),
        JSONObject("mouthWidth",          mouthWidth),
        JSONObject("mouthOpenness",       mouthOpenness),
        JSONObject("hairStyle",           hairStyle),
        JSONObject("hairColor",           hairColor),
        JSONObject("beardStyle",          beardStyle),
        JSONObject("beardColor",          beardColor),
        JSONObject("skinColor",           skinColor),
        JSONObject("muscularity",         muscularity),
        JSONObject("shoulderWidthInches", shoulderWidthInches),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        sex                 = Sex.valueOf(json.requireString("sex"))
        age                 = json.requireDouble("age")
        weightLbs           = json.requireDouble("weightLbs")
        heightInches        = json.requireDouble("heightInches")
        bustInches          = json.requireDouble("bustInches")
        headHeight          = json.requireDouble("headHeight")
        headWidth           = json.requireDouble("headWidth")
        headLength          = json.requireDouble("headLength")
        neckLength          = json.requireDouble("neckLength")
        neckWidth           = json.requireDouble("neckWidth")
        complexion          = json.requireDouble("complexion")
        earAngle            = json.requireDouble("earAngle")
        earBend             = json.requireDouble("earBend")
        earShape            = json.requireDouble("earShape")
        earSize             = json.requireDouble("earSize")
        cheekDefinition     = json.requireDouble("cheekDefinition")
        cheekForward        = json.requireDouble("cheekForward")
        cheekHeight         = json.requireDouble("cheekHeight")
        chinDefinition      = json.requireDouble("chinDefinition")
        chinForward         = json.requireDouble("chinForward")
        chinHeight          = json.requireDouble("chinHeight")
        chinWidth           = json.requireDouble("chinWidth")
        foreheadHeight      = json.requireDouble("foreheadHeight")
        foreheadAngle       = json.requireDouble("foreheadAngle")
        browAngle           = json.requireDouble("browAngle")
        browForward         = json.requireDouble("browForward")
        browWidth           = json.requireDouble("browWidth")
        jawDefinition       = json.requireDouble("jawDefinition")
        jawForward          = json.requireDouble("jawForward")
        jawHeight           = json.requireDouble("jawHeight")
        jawWidth            = json.requireDouble("jawWidth")
        templeDefinition    = json.requireDouble("templeDefinition")
        eyeAngle            = json.requireDouble("eyeAngle")
        eyeColor            = json.requireString("eyeColor")
        eyeForward          = json.requireDouble("eyeForward")
        interEyeDistance    = json.requireDouble("interEyeDistance")
        eyeHeight           = json.requireDouble("eyeHeight")
        eyeSize             = json.requireDouble("eyeSize")
        eyeOpenness         = json.requireDouble("eyeOpenness")
        eyebrowFullness     = json.requireDouble("eyebrowFullness")
        noseForward         = json.requireDouble("noseForward")
        noseHeight          = json.requireDouble("noseHeight")
        noseLength          = json.requireDouble("noseLength")
        noseSize            = json.requireDouble("noseSize")
        noseBridgeForward   = json.requireDouble("noseBridgeForward")
        noseAngle           = json.requireDouble("noseAngle")
        nostrilHeight       = json.requireDouble("nostrilHeight")
        nostrilWidth        = json.requireDouble("nostrilWidth")
        lipFullness         = json.requireDouble("lipFullness")
        lipSize             = json.requireDouble("lipSize")
        mouthForward        = json.requireDouble("mouthForward")
        mouthHeight         = json.requireDouble("mouthHeight")
        mouthWidth          = json.requireDouble("mouthWidth")
        mouthOpenness       = json.requireDouble("mouthOpenness")
        hairStyle           = json.requireString("hairStyle")
        hairColor           = json.requireString("hairColor")
        beardStyle          = json.requireString("beardStyle")
        beardColor          = json.requireString("beardColor")
        skinColor           = json.requireString("skinColor")
        muscularity         = json.requireDouble("muscularity")
        shoulderWidthInches = json.requireDouble("shoulderWidthInches")
    }
}
