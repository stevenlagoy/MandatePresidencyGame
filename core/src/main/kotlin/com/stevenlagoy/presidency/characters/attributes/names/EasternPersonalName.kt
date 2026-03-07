package com.stevenlagoy.presidency.characters.attributes.names

import com.stevenlagoy.jsonic.JSONObject

data class EasternPersonalName(
    override var honorific: String? = null,
    var familyName: String = "",
    var generationName: String? = null,
    var givenName: String = "",
    var westernName: String? = null,
    override var suffixes: List<String> = listOf(),
    override var displayOptions: Set<DisplayOption> = setOf(),
) : PersonalName(honorific, null, null, suffixes, displayOptions)
{
    constructor(json: JSONObject) : this() { fromJson(json) }

    override val preferredGiven: String = if (displayOptions.contains(DisplayOption.LATENT_GENERATION)) givenName.trim() else "$generationName$givenName".trim()

    override val preferredFamily = familyName.trim()

    override val legalName: String get() = "$generationName$givenName $familyName".trim()

    override val formalName: String get() = "$honorific $familyName $generationName$givenName".trim()

    override val biographicalName: String get() = "$honorific $westernName $familyName $preferredGiven".trim()

    override val commonName: String get() = "$familyName $generationName$givenName".trim()

    override val informalName: String get() = "$familyName $preferredGiven".trim()

    override fun toJson() = JSONObject()

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
    }
}
