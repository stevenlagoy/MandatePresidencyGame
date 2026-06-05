package com.stevenlagoy.presidency.characters.attributes.names

import com.badlogic.gdx.net.HttpRequestBuilder.json
import com.stevenlagoy.jsonic.JSONObject

class HispanicPersonalName(
    var givenName:    String             = "",
    var paternalName: String?            = "",
    var maternalName: String?            = "",
    honorific:        String?            = null,
    nickname:         String?            = null,
    suffixes:         List<String>       = listOf(),
    displayOptions:   Set<DisplayOption> = setOf(),
) : PersonalName(honorific, nickname, suffixes, displayOptions)
{

    constructor(other: PersonalName) : this(
        "", "", "",
        other.honorific, other.nickname, other.suffixes, other.displayOptions
    ) {
        other as HispanicPersonalName
        this.givenName      = other.givenName
        this.paternalName   = other.paternalName
        this.maternalName   = other.maternalName
    }

    constructor(json: JSONObject) : this() { fromJson(json) }

    override val preferredGiven: String get() = if (nickname != null) nickname!! else givenName.split("\\s").first()

    val apellidos: String get() = if (displayOptions.contains(DisplayOption.MATERNAL_FIRST)) "$maternalName $paternalName".trim() else "$paternalName $maternalName".trim()

    override val preferredFamily: String get() = if (displayOptions.contains(DisplayOption.PREFER_MATERNAL)) "$maternalName" else if (displayOptions.contains(
            DisplayOption.PREFER_PATERNAL)) "$paternalName" else apellidos

    override val legalName: String get() = "$givenName $apellidos".normalize()

    override val formalName: String get() = "${honorific ?: ""} $givenName $apellidos, $formattedSuffixes".normalize()

    override val biographicalName: String get() = "${honorific ?: ""} $givenName \"${nickname ?: ""}\" $apellidos, $formattedSuffixes".normalize()

    override val commonName: String get() = "$preferredGiven $apellidos".normalize()

    override val informalName: String get() = "$preferredGiven $preferredFamily".normalize()

    override val indexedName: String get() = "$apellidos, $givenName".normalize()

    override val initials: String get() = abbreviate("$givenName $apellidos")

    override fun copy(other: PersonalName) {
        this.honorific = other.honorific
        this.nickname = other.nickname
        this.suffixes = other.suffixes
        this.displayOptions = other.displayOptions
        if (other is HispanicPersonalName) {
            this.givenName = other.givenName
            this.paternalName = other.paternalName
            this.maternalName = other.maternalName
        }
    }

    override fun compareTo(other: PersonalName) = indexedName.compareTo(other.indexedName)

    override fun toJson() = JSONObject(indexedName, listOf(
        *((super.toJson().value as List<JSONObject>).toTypedArray()),
        JSONObject("given_name",      givenName),
        JSONObject("paternal_name",   paternalName),
        JSONObject("maternal_name",   maternalName),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        givenName      = json.get("given_name")      as String
        paternalName   = json.get("paternal_name")   as String
        maternalName   = json.get("maternal_name")   as String
    }
}
