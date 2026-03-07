package com.stevenlagoy.presidency.characters.attributes.names

import com.stevenlagoy.jsonic.JSONObject
import kotlin.text.replace

data class WesternPersonalName(
    override var honorific: String? = null,
    var firstName: String = "",
    var middleName: String? = null,
    override var nickname: String? = null,
    var lastName: String = "",
    override var ordinal: String? = null,
    override var suffixes: List<String> = listOf(),
    override var displayOptions: Set<DisplayOption> = setOf(),
) : PersonalName(honorific, nickname, ordinal, suffixes, displayOptions)
{
    constructor(json: JSONObject) : this() { fromJson(json) }

    val preferredMiddle: String? get() = if (middleName != null && displayOptions.contains(DisplayOption.ABBREVIATE_MIDDLE)) abbreviate(middleName!!) else middleName

    val preferredFirst: String get() = if (displayOptions.contains(DisplayOption.ABBREVIATE_FIRST)) abbreviate(firstName) else firstName

    override val preferredGiven: String get() = if (nickname != null) nickname!! else if (displayOptions.contains(DisplayOption.PREFER_MIDDLE)) preferredMiddle ?: "" else preferredFirst

    override val preferredFamily = lastName.trim()

    override val legalName: String get() = "$firstName $middleName $lastName $ordinal".trim()

    override val formalName: String get() = "$honorific $preferredFirst $preferredMiddle $lastName $ordinal".trim()

    override val biographicalName: String get() = "$honorific $firstName $middleName \"$nickname\" $lastName $ordinal $suffixes".replace("(\\s*\"\"\\s*)|(\\s{2,})"," ").trim()

    override val commonName: String get() = "$preferredFirst $preferredMiddle $lastName $ordinal".trim()

    override val informalName: String get()= "$preferredGiven $lastName".trim()

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        *((super.toJson().value as List<JSONObject>).toTypedArray()),
        JSONObject("first_name", firstName),
        JSONObject("middle_name", middleName),
        JSONObject("last_name", lastName),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        firstName = (json.get("first_name") as JSONObject).value as String
        middleName = (json.get("middle_name") as JSONObject).value as String
        lastName = (json.get("last_name") as JSONObject).value as String
    }

}
