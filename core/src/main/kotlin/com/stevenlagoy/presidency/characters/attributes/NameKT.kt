package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.jsonic.JSONObject

import com.stevenlagoy.presidency.data.Repr

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
class NameKT(
    var nameForm: NameForm,
    givenName: String,
    var middleName: String,
    familyName: String,
    var birthSurname: String,
    var paternalName: String,
    var maternalName: String,
    var nickname: String,
    var religiousName: String,
    var westernName: String,
    var honorific: String,
    var ordinal: String,
    var suffixes: List<String>,
    var displayOptions: Set<DisplayOption>
) : Repr<NameKT>, Jsonic<NameKT>
{
    /** Default-construct a name with empty names, lists, and default [NameForm.WESTERN].*/
    constructor() : this(
        NameForm.WESTERN,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        listOf(),
        setOf()
    )

    /** Create a name based on the Repr string. See [fromRepr]. */
    constructor(repr: String) : this() { fromRepr(repr) }

    /** Create a name based on the JSON Object. See [fromJson]. */
    constructor(json: JSONObject) : this() { fromJson(json) }

    /** Copy-construct a name based on the properties of the passed name object. */
    constructor(other: NameKT) : this(
        other.nameForm,
        other.givenName,
        other.middleName,
        other.familyName,
        other.birthSurname,
        other.paternalName,
        other.maternalName,
        other.nickname,
        other.religiousName,
        other.westernName,
        other.honorific,
        other.ordinal,
        other.suffixes,
        other.displayOptions
    )

    /** Create a name with the given properties, with others being default-constructed. */
    constructor(nameForm: NameForm, givenName: String, middleName: String, lastName: String) : this(
        nameForm,
        givenName,
        middleName,
        lastName,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        listOf(),
        setOf()
    ) { if (nameForm == NameForm.HISPANIC) splitApellidos() }

    /** Create a name with the given properties, with others being default-constructed. */
    constructor(firstName: String, middleName: String, lastName: String) : this(NameForm.WESTERN, firstName, middleName, lastName)

    /** Parts of a Name */
    enum class NamePart {
        /** Title or sign of respect or position, including "Mr.", "Ms.", "Dr.", "Sir", "Rev.", "Father", "Prof." etc. */
        HONORIFIC,
        /** Given name often identifying a person most personally. */
        GIVEN_NAME,
        /** All given names, including first and middle names. */
        GIVEN_NAMES,
        /** Preferred personal name, which may be first name, middle name, either of these abbreviated, or a nickname. */
        PREFERRED_NAME,
        /** Preferred form of the first [GIVEN_NAME], which may be abbreviated. */
        PREFERRED_FIRST,
        /** Second given name, or generation name for Eastern-form. */
        MIDDLE_NAME,
        /** Preferred form of the [MIDDLE_NAME], which may be abbreviated or omitted. */
        PREFERRED_MIDDLE,
        /** Generation name, which in Eastern-form names may be shared between siblings, or may be latent. */
        GENERATION,
        /** Those with Eastern-form names and latent generation names may choose to omit the [GENERATION] name. */
        PREFERRED_GENERATION,
        /** Those given Eastern-form names may choose to take an additional Western given name, which is meaningfully different from a nickname. */
        WESTERN_NAME,
        /** [WESTERN_NAME] surrounded by quotes, for biographical purposes. */
        WESTERN_NAME_QUOTED,
        /** Nickname, which may or may not be derived from a given or middle name. */
        NICKNAME,
        /** [NICKNAME] surrounded by quotes, for biographical purposes. */
        NICKNAME_QUOTED,
        /** Family name, last name, or surname. May change upon marriage. */
        FAMILY_NAME,
        /** In a Hispanic-form name, the first apellido of the father usually becomes the first apellido of the child. */
        APELLIDO_1, // Usually Paternal Surname
        /** In a Hispanic-form name, the first apellido of the mother, usually becomes the second apellido of the child. */
        APELLIDO_2, // Usually Maternal Surname
        /** Number or label indicating lineage, such as "Sr.", "Jr.", "III", etc. */
        ORDINAL,
        /** Set of titles placed after the name, such as "PhD", "MD", "Esq." */
        SUFFIXES;
    }

    /** Forms of a name informed by customs and traditions. */
    enum class NameForm {
        /** Western-style name, usually with forenames (first, middle) followed by a surname. */
        WESTERN,
        /** Eastern-style name, usually with a family name followed by a given name. */
        EASTERN,
        /** Hispanic-style name, usually with given names followed by apellidos. */
        HISPANIC;
    }

    /** Style of writing one's name, determined by formality or purpose. */
    enum class NameStyle(val pattern: Map<NameForm, List<NamePart>>) {
        /** Name style that would appear on government documents. */
        LEGAL(mapOf(
            NameForm.WESTERN to listOf(NamePart.GIVEN_NAMES, NamePart.FAMILY_NAME, NamePart.ORDINAL),
            NameForm.EASTERN to listOf(NamePart.GIVEN_NAMES, NamePart.FAMILY_NAME),
            NameForm.HISPANIC to listOf(NamePart.GIVEN_NAMES, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
        )),
        /** Name style for formal address, like on a diploma or award. */
        FORMAL(mapOf(
            NameForm.WESTERN to listOf(NamePart.HONORIFIC, NamePart.GIVEN_NAMES, NamePart.FAMILY_NAME, NamePart.ORDINAL),
            NameForm.EASTERN to listOf(NamePart.HONORIFIC, NamePart.FAMILY_NAME, NamePart.GIVEN_NAMES),
            NameForm.HISPANIC to listOf(NamePart.HONORIFIC, NamePart.GIVEN_NAMES, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
        )),
        /** Name style that would be used in a biography, including quoted nickname or Western name. */
        BIOGRAPHICAL(mapOf(
            NameForm.WESTERN to listOf(NamePart.GIVEN_NAMES, NamePart.NICKNAME_QUOTED, NamePart.FAMILY_NAME, NamePart.ORDINAL, NamePart.SUFFIXES),
            NameForm.EASTERN to listOf(NamePart.WESTERN_NAME_QUOTED, NamePart.FAMILY_NAME, NamePart.GIVEN_NAMES, NamePart.SUFFIXES),
            NameForm.HISPANIC to listOf(NamePart.GIVEN_NAMES, NamePart.NICKNAME_QUOTED, NamePart.APELLIDO_1, NamePart.APELLIDO_2, NamePart.SUFFIXES),
        )),
        /** Most preferred name style, as would be used for a signature. */
        COMMON(mapOf(
            NameForm.WESTERN to listOf(NamePart.PREFERRED_FIRST, NamePart.PREFERRED_MIDDLE, NamePart.FAMILY_NAME),
            NameForm.EASTERN to listOf(NamePart.FAMILY_NAME, NamePart.PREFERRED_NAME),
            NameForm.HISPANIC to listOf(NamePart.PREFERRED_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
        )),
        /** Name style that would be used by friends. */
        INFORMAL(mapOf(
            NameForm.WESTERN to listOf(NamePart.PREFERRED_NAME, NamePart.FAMILY_NAME),
            NameForm.EASTERN to listOf(NamePart.FAMILY_NAME, NamePart.PREFERRED_NAME),
            NameForm.HISPANIC to listOf(NamePart.PREFERRED_NAME, NamePart.APELLIDO_1),
        ));
    }

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
        INCLUDE_SUFFIX;
    }

    var givenName: String = givenName
        get() = if (nameForm == NameForm.EASTERN && !middleName.isBlank() && !displayOptions.contains((DisplayOption.LATENT_GENERATION))) field.lowercase() else field

    val givenNames: String
        get() = if (nameForm == NameForm.EASTERN) "$middleName$givenName" else "$givenName $middleName".trim()

    var familyName: String = familyName
        set(value: String) {
            field = value
            if (nameForm == NameForm.HISPANIC) splitApellidos()
        }

    /** Split [familyName] and set [paternalName] and [maternalName] based on [displayOptions]. */
    private fun splitApellidos() {
        // Should normally result in two names
        // Separated by spaces, but not by " y ", " de ", " do ", " da ", " del ", " de la ", " e ", " i ", " d'", " d'el "
        val separators = listOf("y", "e", "i", "de", "do", "da", "d'", "del", "d'el", "de", "de la").joinToString("|")
        val apellidos = familyName.split("(?<!${separators}) (?!${separators})".toRegex(), limit=2)
        if (apellidos.isEmpty()) {
            paternalName = ""
            maternalName = ""
        }
        else if (apellidos.size == 1) {
            paternalName = if (displayOptions.contains(DisplayOption.MATERNAL_FIRST)) "" else apellidos[0]
            maternalName = if (displayOptions.contains(DisplayOption.MATERNAL_FIRST)) apellidos[0] else ""
        }
        else if (displayOptions.contains(DisplayOption.MATERNAL_FIRST)) {
            maternalName = apellidos[0]
            paternalName = apellidos[1]
        }
        else {
            paternalName = apellidos[0]
            maternalName = apellidos[1]
        }
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

    val preferredFirst: String
        get() = if (displayOptions.contains(DisplayOption.ABBREVIATE_FIRST)) abbreviate(givenName) else givenName

    val preferredMiddle: String
        get() = if (displayOptions.contains(DisplayOption.ABBREVIATE_MIDDLE)) abbreviate(middleName)
                else if (nameForm == NameForm.EASTERN && displayOptions.contains(DisplayOption.LATENT_GENERATION)) ""
                else middleName

    val preferredName: String
        get() = if (displayOptions.contains(DisplayOption.INCLUDE_NICKNAME)) nickname
                else if (displayOptions.contains(DisplayOption.PREFER_MIDDLE)) preferredMiddle
                else if (nameForm == NameForm.EASTERN) "$preferredMiddle$preferredFirst"
                else preferredFirst

    val formattedSuffixes: String
        get() = suffixes.joinToString(", ")

    /** Get part of a name based on [displayOptions]. */
    fun getNamePart(part: NamePart) = when(part) {
        NamePart.HONORIFIC            -> honorific
        NamePart.GIVEN_NAME           -> givenName
        NamePart.GIVEN_NAMES          -> givenNames
        NamePart.PREFERRED_NAME       -> preferredName
        NamePart.PREFERRED_FIRST      -> preferredFirst
        NamePart.MIDDLE_NAME          -> middleName
        NamePart.PREFERRED_MIDDLE     -> preferredMiddle
        NamePart.NICKNAME             -> nickname
        NamePart.NICKNAME_QUOTED      -> if (!nickname.isBlank()) "\"${nickname}\"" else ""
        NamePart.FAMILY_NAME          -> familyName
        NamePart.ORDINAL              -> ordinal
        NamePart.SUFFIXES             -> formattedSuffixes
        NamePart.GENERATION           -> middleName
        NamePart.PREFERRED_GENERATION -> if (displayOptions.contains(DisplayOption.LATENT_GENERATION)) "" else middleName
        NamePart.WESTERN_NAME         -> westernName
        NamePart.WESTERN_NAME_QUOTED  -> if (!westernName.isBlank()) "\"$westernName\"" else ""
        NamePart.APELLIDO_1           -> if (displayOptions.contains(DisplayOption.MATERNAL_FIRST)) maternalName else paternalName
        NamePart.APELLIDO_2           -> if (displayOptions.contains(DisplayOption.MATERNAL_FIRST)) paternalName else maternalName
    }

    /** Get the name in the given style. */
    fun getNameInStyle(style: NameStyle) = buildString {
        for (part in style.pattern[nameForm]!!) {
            val p = getNamePart(part)
            append(p.trim())
            if (!p.isBlank()) append(" ")
        }
    }.replace("\\s+,",",").replace("\\s+"," ").trim()

    /** Name in the [NameStyle.LEGAL] style. */
    val legalName: String
        get() = getNameInStyle(NameStyle.LEGAL)
    /** Name in the [NameStyle.FORMAL] style. */
    val formalName: String
        get() = getNameInStyle(NameStyle.FORMAL)
    /** Name in the [NameStyle.BIOGRAPHICAL] style. */
    val biographicalName: String
        get() = getNameInStyle(NameStyle.BIOGRAPHICAL)
    /** Name in the [NameStyle.COMMON] style. */
    val commonName: String
        get() = getNameInStyle(NameStyle.COMMON)
    /** Name in the [NameStyle.INFORMAL] style. */
    val informalName: String
        get() = getNameInStyle(NameStyle.INFORMAL)

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    override fun fromRepr(repr: String?): NameKT {
        return this
    }

    override fun toRepr() = """
        ${this::class.simpleName}:[
            nameForm:${nameForm.name};
            givenName:$givenName;
            middleName:$middleName;
            familyName:$familyName;
            birthSurname:$birthSurname;
            paternalName:$paternalName;
            maternalName:$maternalName;
            nickname:$nickname;
            religiousName:$religiousName;
            westernName:$westernName;
            honorific:$honorific;
            ordinal:$ordinal;
            suffixes:$suffixes;
            displayOptions:$displayOptions;
        ];
    """.trimIndent()

    override fun fromJson(json: JSONObject): NameKT {
        nameForm = NameForm.valueOf((json.get("name_form") as JSONObject).value as String)
        givenName = (json.get("given_name") as JSONObject).value as String
        middleName = (json.get("middle_name") as JSONObject).value as String
        familyName = (json.get("family_name") as JSONObject).value as String
        birthSurname = (json.get("birth_surname") as JSONObject).value as String
        paternalName = (json.get("paternal_name") as JSONObject).value as String
        maternalName = (json.get("maternal_name") as JSONObject).value as String
        nickname = (json.get("nickname") as JSONObject).value as String
        religiousName = (json.get("religious_name") as JSONObject).value as String
        westernName = (json.get("western_name") as JSONObject).value as String
        honorific = (json.get("honorific") as JSONObject).value as String
        ordinal = (json.get("ordinal") as JSONObject).value as String
        suffixes = (json.get("suffixes") as JSONObject).asList.map { it.toString() }
        displayOptions = (json.get("displayOptions") as JSONObject).asList.map { DisplayOption.valueOf(it as String) }.toSet()
        return this
    }

    override fun toJson(): JSONObject {
        val fields = mutableListOf<JSONObject>()
        fields.add(JSONObject("name_form", nameForm.name))
        fields.add(JSONObject("given_name", givenName))
        fields.add(JSONObject("middle_name", middleName))
        fields.add(JSONObject("family_name", familyName))
        fields.add(JSONObject("birth_surname", birthSurname))
        fields.add(JSONObject("paternal_name", paternalName))
        fields.add(JSONObject("maternal_name", maternalName))
        fields.add(JSONObject("nickname", nickname))
        fields.add(JSONObject("religious_name", religiousName))
        fields.add(JSONObject("western_name", westernName))
        fields.add(JSONObject("honorific", honorific))
        fields.add(JSONObject("ordinal", ordinal))
        fields.add(JSONObject("suffixes", suffixes))
        fields.add(JSONObject("displayOptions", displayOptions))
        return JSONObject(hashCode().toString(), fields)
    }

    // OBJECT METHODS -----------------------------------------------------------------------------

    /** See [biographicalName]. Also see [toRepr] for a way to stringify this object as a whole. */
    override fun toString() = biographicalName

    override fun hashCode() = toRepr().hashCode()

    override fun equals(other: Any?) = this.hashCode() == other.hashCode()
}
