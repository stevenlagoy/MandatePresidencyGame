package com.stevenlagoy.presidency.characters.attributes.names

import com.stevenlagoy.jsonic.JSONObject

data class HispanicPersonalName(
    override var honorific: String? = null,
    var givenName: String = "",
    override var nickname: String? = null,
    var paternalName: String? = "",
    var maternalName: String? = "",
    override var suffixes: List<String> = listOf(),
    override var displayOptions: Set<DisplayOption> = setOf(),
) : PersonalName(honorific, nickname, suffixes, displayOptions)
{

    constructor(other: PersonalName) : this() {
        this.honorific = other.honorific
        this.nickname = other.nickname
        this.suffixes = other.suffixes
        this.displayOptions = other.displayOptions
    }

    constructor(givenName: String, paternalName: String?, maternalName: String?) : this(null, givenName, null, paternalName, maternalName)

    constructor(json: JSONObject) : this() { fromJson(json) }

    override val preferredGiven: String get() = if (nickname != null) nickname!! else givenName.split("\\s").first()

    val apellidos: String get() = if (displayOptions.contains(DisplayOption.MATERNAL_FIRST)) "$maternalName $paternalName".trim() else "$paternalName $maternalName".trim()

    override val preferredFamily: String get() = if (displayOptions.contains(DisplayOption.PREFER_MATERNAL)) "$maternalName" else if (displayOptions.contains(
            DisplayOption.PREFER_PATERNAL)) "$paternalName" else apellidos

    override val legalName: String get() = "$givenName $apellidos".trim()

    override val formalName: String get() = "$honorific $givenName $apellidos".trim()

    override val biographicalName: String get() = "$honorific $givenName \"$nickname\" $apellidos $suffixes".replace("(\\s*\"\"\\s*)|(\\s{2,})"," ").trim()

    override val commonName: String get() = "$preferredGiven $apellidos".trim()

    override val informalName: String get() = "$preferredGiven $preferredFamily".trim()

    override val indexedName: String get() = "$apellidos, $givenName".trim()

    override val initials: String get() = "${givenName[0]}${apellidos.split(" ")[0][0]}${apellidos.split(" ")[1][0]}".uppercase().trim()

    override fun compareTo(other: PersonalName) = indexedName.compareTo(other.indexedName)
}
