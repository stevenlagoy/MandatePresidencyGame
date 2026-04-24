package com.stevenlagoy.presidency.characters.attributes.names

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.attributes.names.HispanicPersonalName

data class EasternPersonalName(
    override var honorific: String? = null,
    var familyName: String = "",
    var generationName: String? = null,
    var givenName: String = "",
    var westernName: String? = null,
    override var suffixes: List<String> = listOf(),
    override var displayOptions: Set<DisplayOption> = setOf(),
) : PersonalName(honorific, null, suffixes, displayOptions)
{

    constructor(other: PersonalName) : this() {
        this.honorific = other.honorific
        this.nickname = other.nickname
        this.suffixes = other.suffixes
        this.displayOptions = other.displayOptions
    }

    constructor(familyName: String, generationName: String?, givenName: String) : this(null, familyName, generationName, givenName)

    constructor(json: JSONObject) : this() { fromJson(json) }

    override val preferredGiven: String = if (displayOptions.contains(DisplayOption.LATENT_GENERATION)) givenName.trim() else "$generationName$givenName".trim()

    override val preferredFamily = familyName.trim()

    override val legalName: String get() = "$generationName$givenName $familyName".trim()

    override val formalName: String get() = "$honorific $familyName $generationName$givenName".trim()

    override val biographicalName: String get() = "$honorific $westernName $familyName $preferredGiven".trim()

    override val commonName: String get() = "$familyName $generationName$givenName".trim()

    override val informalName: String get() = "$familyName $preferredGiven".trim()

    override val indexedName: String get() = "$familyName, $generationName$givenName".trim()

    override val initials: String get() = "${familyName[0]}${preferredGiven[0]}".uppercase().trim()

    override fun toJson() = JSONObject()

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
    }

    override fun compareTo(other: PersonalName) = indexedName.compareTo(other.indexedName)
}
