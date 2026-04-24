package com.stevenlagoy.presidency.characters.attributes.names

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

/**
 * Model the personal name of a Character, with options for several forms of name, traditional or
 * customary patterns, and display options.
 * Contains fields for basic parts of a person's name, data about the way those parts are used, and
 * various forms of displaying a name.
 *
 * @property nameForm       Form of the name, whether Western- Eastern- or Hispanic- form.
 * @property givenName      Given name often identifying a person most personally.
 * @property middleName     Second given name, or generation name for Eastern-form.
 * @property familyName     Family name, last name, or surname. May change upon marriage.
 * @property birthSurname   Surname given at birth, sometimes shown as "née Surname".
 * @property paternalName   In a Hispanic-form name, the first apellido of the father usually
 *                          becomes the first apellido of the child.
 * @property maternalName   In a Hispanic-form name, the first apellido of the mother, usually
 *                          becomes the second apellido of the child.
 * @property nickname       Nickname, which may or may not be derived from a given or middle name.
 * @property religiousName  Also baptismal name, a given name of religious significance gained on
 *                          conversion or after a coming-of-age ceremony.
 * @property westernName    Those given Eastern-form names may choose to take an additional Western
 *                          given name, which is meaningfully different from a nickname.
 * @property honorific      Title or sign of respect or position, including "Mr.", "Ms.", "Dr.",
 *                          "Sir", "Rev.", "Father", "Prof." etc.
 * @property ordinal        Number or label indicating lineage, such as "Sr.", "Jr.", "III", etc.
 * @property suffixes       Set of titles placed after the name, such as "PhD", "MD", "Esq."
 * @property displayOptions Set of options for displaying the name in different styles.
 *
 * @constructor Create a Name with the given property values.
 *
 * @author Steven LaGoy
 */
abstract class PersonalName(
    open var honorific: String? = null,
    open var nickname: String? = null,
    open var suffixes: List<String> = listOf(),
    open var displayOptions: Set<DisplayOption> = setOf(),
) : Jsonic<PersonalName>, Comparable<PersonalName>
{

    /** Create a name based on the JSON Object. See [fromJson]. */
    constructor(json: JSONObject) : this() { fromJson(json) }

    /** Copy-construct a name based on the properties of the passed name object. */
    constructor(other: PersonalName) : this(
        other.honorific,
        other.nickname,
        other.suffixes,
        other.displayOptions.toSet(), // Copy
    )

    /** Options for displaying the name in different styles. */
    enum class DisplayOption {
        /** Abbreviate the first name(s) */
        ABBREVIATE_FIRST,
        /** Abbreviate the middle name(s) */
        ABBREVIATE_MIDDLE,
        /** Prefer the middle name over the first name */
        PREFER_MIDDLE,
        /** Include the middle name in the common name */
        INCLUDE_MIDDLE,
        /** Place the Western name before the Traditional name */
        WESTERN_FIRST,
        /** Place the Traditional name before the Western name */
        TRADITIONAL_FIRST,
        /** The generational name is latent, not clan-based */
        LATENT_GENERATION,
        /** Place the maternal surname before the paternal surname */
        MATERNAL_FIRST,
        /** Include the additional Western name */
        INCLUDE_WESTERN,
        /** Include the nickname */
        INCLUDE_NICKNAME,
        /** Include the ordinal */
        INCLUDE_ORDINAL,
        /** Include the honorific(s) */
        INCLUDE_HONORIFIC,
        /** Include the suffix(es) */
        INCLUDE_SUFFIX,
        /** Prefer just the maternal name over both apellidos */
        PREFER_MATERNAL,
        /** Prefer just the paternal name over both apellidos */
        PREFER_PATERNAL;
    }

    /** Abbreviate a name by returning the uppercase form of the first letter in the name. */
    fun abbreviate(name: String): String {
        var initial: Char
        for (i in 0..name.trim().length) {
            initial = name[i]
            if (initial.isLetter()) return initial.uppercase()
        }
        return ""
    }

    abstract val preferredGiven: String

    abstract val preferredFamily: String

    val formattedSuffixes: String
        get() = suffixes.joinToString(", ")

    abstract val legalName: String

    abstract val formalName: String

    abstract val biographicalName: String

    abstract val commonName: String

    abstract val informalName: String

    abstract val indexedName: String

    abstract val initials: String

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    override fun toString() = """
        honorific:$honorific;
        nickname:$nickname;
        suffixes:$suffixes;
        displayOptions:$displayOptions;
    """.trimIndent()

    override fun fromJson(json: JSONObject) = this.apply {
        honorific = (json.get("honorific") as JSONObject).value as String
        nickname = (json.get("nickname") as JSONObject).value as String
        suffixes = (json.get("suffixes") as JSONObject).asList.map { it.toString() }
        displayOptions = (json.get("displayOptions") as JSONObject).asList.map { DisplayOption.valueOf(it as String) }.toSet()
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("honorific", honorific),
        JSONObject("nickname", nickname),
        JSONObject("suffixes", suffixes),
        JSONObject("displayOptions", displayOptions),
    ))

    // OBJECT METHODS -----------------------------------------------------------------------------

    override fun hashCode() = toString().hashCode()

    override fun equals(other: Any?) = this.hashCode() == other.hashCode()
}
