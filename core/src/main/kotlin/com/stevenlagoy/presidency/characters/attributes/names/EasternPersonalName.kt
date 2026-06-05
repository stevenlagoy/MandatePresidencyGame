package com.stevenlagoy.presidency.characters.attributes.names

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.util.titlecase

class EasternPersonalName(
    var familyName:     String             = "",
    var generationName: String?            = null,
    var givenName:      String             = "",
    var westernName:    String?            = null,
    honorific:          String?            = null,
    nickname:           String?           = null,
    suffixes:           List<String>       = listOf(),
    displayOptions:     Set<DisplayOption> = setOf(),
) : PersonalName(honorific, null, suffixes, displayOptions)
{

    constructor(other: PersonalName) : this(
        "", null, "", null,
        other.honorific, other.nickname, other.suffixes, other.displayOptions
    ) {
        other as EasternPersonalName
        this.familyName     = other.familyName
        this.generationName = other.generationName
        this.givenName      = other.givenName
        this.westernName    = other.westernName
    }

    constructor(json: JSONObject) : this() { fromJson(json) }

    val fullGiven: String get() = (generationName + givenName).titlecase().trim()

    override val preferredGiven: String get() = if (displayOptions.contains(DisplayOption.LATENT_GENERATION)) givenName.trim() else fullGiven

    override val preferredFamily get() = familyName.trim()

    override val legalName: String get() = "$fullGiven $familyName".normalize()

    override val formalName: String get() = "${honorific ?: ""} $familyName $fullGiven, $formattedSuffixes".normalize()

    override val biographicalName: String get() = "${honorific ?: ""} ${westernName ?: ""} $familyName $preferredGiven, $formattedSuffixes".normalize()

    override val commonName: String get() = "$familyName $fullGiven".normalize()

    override val informalName: String get() = "$familyName $preferredGiven".normalize()

    override val indexedName: String get() = "$familyName, $fullGiven".normalize()

    override val initials: String get() = abbreviate("$familyName $preferredGiven")

    override fun copy(other: PersonalName) {
        this.honorific = other.honorific
        this.nickname = other.nickname
        this.suffixes = other.suffixes
        this.displayOptions = other.displayOptions
        if (other is EasternPersonalName) {
            this.familyName = other.familyName
            this.generationName = other.generationName
            this.givenName = other.givenName
            this.westernName = other.westernName
        }
    }

    override fun compareTo(other: PersonalName) = indexedName.compareTo(other.indexedName)

    override fun toJson() = JSONObject(indexedName, listOf(
        *((super.toJson().value as List<JSONObject>).toTypedArray()),
        JSONObject("family_name",     familyName),
        JSONObject("generation_name", generationName),
        JSONObject("given_name",      givenName),
        JSONObject("western_name",    westernName),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        familyName     = json.get("family_name")     as String
        generationName = json.get("generation_name") as String
        givenName      = json.get("given_name")      as String
        westernName    = json.get("western_name")    as String
    }
}
