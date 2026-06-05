package com.stevenlagoy.presidency.characters.attributes.names

import com.stevenlagoy.jsonic.JSONObject
import kotlin.String

class WesternPersonalName(
    var firstName:  String             = "",
    var middleName: String?            = null,
    var lastName:   String             = "",
    var ordinal:    String?            = null,
    honorific:      String?            = null,
    nickname:       String?            = null,
    suffixes:       List<String>       = listOf(),
    displayOptions: Set<DisplayOption> = setOf(),
) : PersonalName(honorific, nickname, suffixes, displayOptions)
{

    constructor(other: PersonalName) : this(
        "", null, "", null,
        other.honorific, other.nickname, other.suffixes, other.displayOptions,
    ) {
        other as WesternPersonalName
        this.firstName      = other.firstName
        this.middleName     = other.middleName
        this.lastName       = other.lastName
        this.ordinal        = other.ordinal
    }

    constructor(json: JSONObject) : this() { fromJson(json) }

    val preferredMiddle: String? get() = if (middleName != null && displayOptions.contains(DisplayOption.ABBREVIATE_MIDDLE)) abbreviate(middleName!!) else middleName

    val preferredFirst: String get() = if (displayOptions.contains(DisplayOption.ABBREVIATE_FIRST)) abbreviate(firstName) else firstName

    override val preferredGiven: String get() = if (nickname != null) nickname!! else if (displayOptions.contains(DisplayOption.PREFER_MIDDLE)) preferredMiddle ?: "" else preferredFirst

    override val preferredFamily = lastName.trim()

    override val legalName: String get() = "$firstName $middleName $lastName ${ordinal ?: ""}".normalize()

    override val formalName: String get() = "${honorific ?: ""} $preferredFirst $preferredMiddle $lastName ${ordinal ?: ""}, $formattedSuffixes".normalize()

    override val biographicalName: String get() = "${honorific ?: ""} $firstName $middleName \"${nickname ?: ""}\" $lastName ${ordinal ?: ""}, $formattedSuffixes".normalize()

    override val commonName: String get() = "$preferredFirst $preferredMiddle $lastName ${ordinal ?: ""}".normalize()

    override val informalName: String get() = "$preferredGiven $lastName".normalize()

    override val indexedName: String get() = "$lastName, $firstName $middleName".normalize()

    override val initials: String get() = abbreviate("$firstName ${middleName ?: ""} $lastName")

    override fun copy(other: PersonalName) {
        this.honorific = other.honorific
        this.nickname = other.nickname
        this.suffixes = other.suffixes
        this.displayOptions = other.displayOptions
        if (other is WesternPersonalName) {
            this.firstName = other.firstName
            this.middleName = other.middleName
            this.lastName = other.lastName
            this.ordinal = other.ordinal
        }
    }

    override fun compareTo(other: PersonalName) = indexedName.compareTo(other.indexedName)

    override fun toJson() = JSONObject(indexedName, listOf(
        *((super.toJson().value as List<JSONObject>).toTypedArray()),
        JSONObject("first_name",      firstName),
        JSONObject("middle_name",     middleName),
        JSONObject("last_name",       lastName),
        JSONObject("ordinal",         ordinal),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        firstName      = json.get("first_name")      as String
        middleName     = json.get("middle_name")     as String
        lastName       = json.get("last_name")       as String
        ordinal        = json.get("ordinal")         as String
    }
}
