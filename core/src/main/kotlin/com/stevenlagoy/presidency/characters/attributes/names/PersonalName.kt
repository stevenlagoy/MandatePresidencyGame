package com.stevenlagoy.presidency.characters.attributes.names

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

/**
 * Model the personal name of a Character, with options for several forms of name, traditional or
 * customary patterns, and display options.
 * Contains fields for basic parts of a person's name, data about the way those parts are used, and
 * various forms of displaying a name.
 *
 * @property honorific      Title or sign of respect or position, including "Mr.", "Ms.", "Dr.",
 *                          "Sir", "Rev.", "Father", "Prof." etc.
 * @property nickname       Nickname, which may or may not be derived from a given or middle name.
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

    /** Abbreviate names by returning the uppercase form of the first letter in each name. */
    fun abbreviate(names: String) = names.split(" ").fold("") { res, name -> res + name.find { it.isLetter() }?.uppercaseChar() }

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

    protected fun String.normalize() = this.replace(Regex("(\\s*\"\"\\s*)|\\s+|,\\s*$")," ").trim()

    abstract fun copy(other: PersonalName);

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    override fun fromJson(json: JSONObject) = this.apply {
        honorific = json.get("honorific") as String
        nickname = json.get("nickname") as String?
        suffixes = json.get("suffixes") as List<String>
        displayOptions = json.get("display_options") as Set<DisplayOption>
    }

    override fun toJson() = JSONObject(indexedName, listOf(
        JSONObject("honorific", honorific),
        JSONObject("nickname", nickname),
        JSONObject("suffixes", suffixes),
        JSONObject("display_options", displayOptions),
    ))

    // OBJECT METHODS -----------------------------------------------------------------------------

    override fun hashCode() = toString().hashCode()

    override fun equals(other: Any?) = this.hashCode() == other.hashCode()
}
