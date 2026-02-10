/*
 * Name
 * ~/characters/attributes/names/Name.java
 * Steven LaGoy
 * Created: 23 March 2025 at 1:27 AM
 * Modified: 28 December 2025
 */

package com.stevenlagoy.presidency.characters.attributes.names;

// IMPORTS ----------------------------------------------------------------------------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.characters.attributes.names.Name.NameForm;
import com.stevenlagoy.presidency.data.Repr;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                              NAME                                              //
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Models the Personal Name of a Character, with options for several Name Forms,
 * Patterns, and Display Options.
 * <p>
 * Contains fields and methods for basic parts of a person's name, data about
 * the way those parts are used, and various forms of displaying that name.
 */
public final class Name implements Repr<Name>, Jsonic<Name> {

    /** Enum for parts of a name. */
    private static enum NamePart {
        HONORIFIC,
        GIVEN_NAME,
        PREFERRED_NAME,
        PREFERRED_FIRST,
        MIDDLE_NAME,
        PREFERRED_MIDDLE,
        GENERATION,
        PREFERRED_GENERATION,
        WESTERN_NAME,
        WESTERN_NAME_QUOTED,
        NICKNAME,
        NICKNAME_QUOTED,
        FAMILY_NAME,
        APELLIDO_1, // Usually Paternal Surname
        APELLIDO_2, // Usually Maternal Surname
        ORDINAL,
        SUFFIXES;
    }

    /** Enum for styles of writing a name, and patterns for that style. */
    private static enum NameStyle {
        /** Name as it would appear on government documents. */
        LEGAL(Map.of(
                NameForm.WESTERN,         List.of(NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.FAMILY_NAME, NamePart.ORDINAL),
                NameForm.EASTERN,         List.of(NamePart.GENERATION, NamePart.GIVEN_NAME, NamePart.FAMILY_NAME),
                NameForm.HISPANIC,        List.of(NamePart.GIVEN_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
                NameForm.NATIVE_AMERICAN, List.of(NamePart.GIVEN_NAME, NamePart.FAMILY_NAME))),
        /** Name for formally addressing, like on a diploma or award. */
        FORMAL(Map.of(
                NameForm.WESTERN,         List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.FAMILY_NAME, NamePart.ORDINAL),
                NameForm.EASTERN,         List.of(NamePart.HONORIFIC, NamePart.FAMILY_NAME, NamePart.GENERATION, NamePart.GIVEN_NAME),
                NameForm.HISPANIC,        List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2, NamePart.ORDINAL),
                NameForm.NATIVE_AMERICAN, List.of(NamePart.HONORIFIC, NamePart.GIVEN_NAME, NamePart.FAMILY_NAME))),
        /** Name that would be used in a biography, including nickname. */
        BIOGRAPHICAL(Map.of(
                NameForm.WESTERN,         List.of(NamePart.GIVEN_NAME, NamePart.MIDDLE_NAME, NamePart.NICKNAME_QUOTED, NamePart.FAMILY_NAME, NamePart.ORDINAL, NamePart.SUFFIXES),
                NameForm.EASTERN,         List.of(NamePart.WESTERN_NAME, NamePart.FAMILY_NAME, NamePart.GENERATION, NamePart.GIVEN_NAME, NamePart.SUFFIXES),
                NameForm.HISPANIC,        List.of(NamePart.GIVEN_NAME, NamePart.NICKNAME_QUOTED, NamePart.APELLIDO_1, NamePart.APELLIDO_2, NamePart.SUFFIXES),
                NameForm.NATIVE_AMERICAN, List.of(NamePart.GIVEN_NAME, NamePart.FAMILY_NAME, NamePart.SUFFIXES))),
        /** Most preferred name as would be used for a signature. */
        COMMON(Map.of(
                NameForm.WESTERN,         List.of(NamePart.PREFERRED_FIRST, NamePart.PREFERRED_MIDDLE, NamePart.FAMILY_NAME),
                NameForm.EASTERN,         List.of(NamePart.FAMILY_NAME, NamePart.PREFERRED_GENERATION, NamePart.GIVEN_NAME),
                NameForm.HISPANIC,        List.of(NamePart.PREFERRED_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
                NameForm.NATIVE_AMERICAN, List.of(NamePart.PREFERRED_FIRST, NamePart.FAMILY_NAME))),
        /** Name that would be used by friends. */
        INFORMAL(Map.of(
                NameForm.WESTERN,         List.of(NamePart.PREFERRED_NAME, NamePart.FAMILY_NAME),
                NameForm.EASTERN,         List.of(NamePart.FAMILY_NAME, NamePart.PREFERRED_GENERATION, NamePart.GIVEN_NAME),
                NameForm.HISPANIC,        List.of(NamePart.PREFERRED_NAME, NamePart.APELLIDO_1, NamePart.APELLIDO_2),
                NameForm.NATIVE_AMERICAN, List.of(NamePart.PREFERRED_FIRST, NamePart.FAMILY_NAME)));

        public final Map<NameForm, List<NamePart>> pattern;

        private NameStyle(Map<NameForm, List<NamePart>> pattern) {
            this.pattern = pattern;
        }
    }

    /** Enum for broader forms of a name, from culture or religion etc. */
    public static enum NameForm {
        EASTERN,
        HISPANIC,
        NATIVE_AMERICAN,
        WESTERN;

        public static NameForm defaultForm = NameForm.WESTERN;
    }

    /** Options for displaying a name. */
    public static enum DisplayOption {
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
        /** Place the paternal surname before the maternal surname */
        PATERNAL_FIRST,
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

    // INSTANCE VARIABLES -------------------------------------------------------------------------------------------------------------------------------------

    private NameForm nameForm;

    /** First or Given Name */       private String givenName;
    /** Middle or Generation Name */ private String middleName;
    /** Last or Family Name */       private String familyName;
    /** */                           private String birthSurname;
    /** */                           private String paternalName;
    /** */                           private String maternalName;
    /** Nicknames */                 private String nickname;
    // Other Names
    /** */                           private String religiousName;
    /** */                           private String westernName;
    /** */                           private String legalName;
    /** */                           private String informalName;
    // Additional Name Parts
    /** */                           private String honorific;
    /** */                           private String ordinal;
    /** */                           private List<String> suffixes;
    /** */                           private Set<DisplayOption> displayOptions;

    // CONSTRUCTORS -------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Creates a Name with all fields default. The nameform will be
     * {@code NameForm.defaultForm}
     * 
     * @see #Name(NameForm, String, boolean, boolean, String, boolean, boolean,
     *      String, String, String, String, List, boolean, String, String, String,
     *      String, String, List)
     */
    public Name() {
        this.nameForm      = NameForm.defaultForm;
        this.givenName     = "";
        this.middleName    = "";
        this.familyName    = "";
        this.birthSurname  = "";
        this.paternalName  = "";
        this.maternalName  = "";
        this.nickname      = "";
        this.religiousName = "";
        this.westernName   = "";
        this.legalName     = "";
        this.informalName  = "";
        this.honorific     = "";
        this.ordinal       = "";
        this.suffixes = new ArrayList<>();
        this.displayOptions = new HashSet<>();
    }

    public Name(String buildstring) {
        if (buildstring == null || buildstring.isBlank()) {
            throw new IllegalArgumentException("The given buildstring was null, and a Name object could not be created.");
        }
        fromRepr(buildstring);
    }

    public Name(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException("The passed JSON Object was null, and a Character object could not be created.");
        }
        fromJson(json);
    }

    public Name(Name other) {
        this();
        this.nameForm      = other.getNameForm();
        this.givenName     = other.getGivenName();
        this.middleName    = other.getMiddleName();
        this.familyName    = other.getFamilyName();
        this.birthSurname  = other.getBirthSurname();
        this.paternalName  = other.getPaternalName();
        this.maternalName  = other.getMaternalName();
        this.nickname      = other.getNickname();
        this.religiousName = other.religiousName;
        this.westernName   = other.getWesternName();
        this.legalName     = other.legalName;
        this.informalName  = other.informalName;
        this.honorific     = other.honorific;
        this.ordinal       = other.ordinal;
        for (String suffix : other.getSuffixes())
            this.suffixes.add(suffix);
        for (DisplayOption option : other.getDisplayOptions())
            this.displayOptions.add(option);
    }

    public Name(String firstName, String middleName, String lastName) {
        this(NameForm.defaultForm, firstName, middleName, lastName);
    }

    public Name(NameForm nameform, String givenName, String middleName, String familyName) {
        this();
        this.nameForm = nameform != null ? nameform : NameForm.defaultForm;
        switch (this.nameForm) {
            case WESTERN:
                this.givenName    = givenName  != null ? givenName  : "";
                this.middleName   = middleName != null ? middleName : "";
                this.familyName   = familyName != null ? familyName : "";
                this.birthSurname = familyName != null ? familyName : "";
                break;
            case EASTERN:
                this.familyName   = familyName != null ? familyName : "";
                this.middleName   = middleName != null ? middleName : "";
                this.givenName    = givenName  != null ? givenName  : "";
                break;
            case HISPANIC:
                this.givenName    = givenName  != null ? givenName  : "";
                this.middleName   = middleName != null ? middleName : "";
                splitApellidos(familyName != null ? familyName : "");
                break;
            case NATIVE_AMERICAN:
                this.givenName    = givenName  != null ? givenName  : "";
                this.familyName   = familyName != null ? familyName : "";
                break;
        }
        this.suffixes = new ArrayList<>();
    }

    public Name(
            NameForm nameForm,
            String givenName,
            String middleName,
            String familyName,
            String birthSurname,
            String paternalName,
            String maternalName,
            String nickname,
            String religiousName,
            String legalName,
            String informalName,
            String honorific,
            String ordinal,
            List<String> suffixes,
            Set<DisplayOption> displayOptions
    ) {
        this.nameForm       = nameForm;
        this.givenName      = givenName     != null ? givenName     : "";
        this.middleName     = middleName    != null ? middleName    : "";
        this.familyName     = familyName    != null ? familyName    : "";
        this.birthSurname   = birthSurname  != null ? birthSurname  : "";
        this.paternalName   = paternalName  != null ? paternalName  : "";
        this.maternalName   = maternalName  != null ? maternalName  : "";
        this.nickname       = nickname      != null ? nickname      : "";
        this.religiousName  = religiousName != null ? religiousName : "";
        this.honorific      = honorific;
        this.ordinal        = ordinal;
        this.suffixes       = suffixes;
        this.displayOptions = displayOptions;
    }

    private void splitApellidos(String familyName) {
        // Should normally result in two names
        // Separated by spaces, but not by " y ", " de ", " do ", " da ", " del ", " de la ", " e ", " i ", " d'", " d'el "
        String[] separators = {"y", "e", "i", "de", "do", "da", "d'", "del", "d'el", "de"};
        String joined = String.join("|", separators);
        String regex = String.format("(?<!%s) (?!%s)", joined, joined);
        String[] apellidos = familyName.split(regex);
        if (apellidos.length == 1) {
            setFamilyName(apellidos[0]);
        }
        else if (apellidos.length == 2) {
            if (hasDisplayOption(DisplayOption.PATERNAL_FIRST)) {
                setPaternalName(apellidos[0]);
                setMaternalName(apellidos[1]);
            }
            else {
                setMaternalName(apellidos[0]);
                setPaternalName(apellidos[1]);
            }
        }
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------------------------------------------------------------------

    // NameForm: NameForm

    public NameForm getNameForm() {
        return nameForm;
    }

    public void setNameForm(NameForm nameForm) {
        this.nameForm = nameForm;
    }

    public String getGivenName() {
        if (nameForm.equals(NameForm.EASTERN) && !middleName.isEmpty())
            return givenName.toLowerCase();
        return givenName;
    }

    public void setGivenName(String name) {
        this.givenName = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String name) {
        this.middleName = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String name) {
        this.familyName = name;
    }

    public String getBirthSurname() {
        return this.birthSurname;
    }

    public void setBirthSurname(String name) {
        this.birthSurname = name;
    }

    public String getPaternalName() {
        return this.paternalName;
    }

    public void setPaternalName(String name) {
        this.paternalName = name;
    }

    public String getMaternalName() {
        return this.maternalName;
    }

    public void setMaternalName(String name) {
        this.maternalName = name;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOrdinal() {
        return this.ordinal;
    }

    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }

    private Set<DisplayOption> getDisplayOptions() {
        return displayOptions;
    }

    private boolean hasDisplayOption(DisplayOption displayOption) {
        return displayOptions.contains(displayOption);
    }

    public void setSuffix(String suffix) {
        this.suffixes = new ArrayList<>();
        addSuffix(suffix);
    }

    public boolean addSuffix(String suffix) {
        return this.suffixes.add(suffix);
    }

    public boolean removeSuffix(String suffix) {
        return this.suffixes.remove(suffix);
    }

    public List<String> getSuffixes() {
        return suffixes;
    }

    public String getWesternName() {
        return westernName;
    }

    public void setWesternName(String westernName) {
        this.westernName = westernName;
    }

    public void addDisplayOption(DisplayOption option) {
        this.displayOptions.add(option);
    }

    // STYLE METHODS ------------------------------------------------------------------------------------------------------------------------------------------

    public String getNameInStyle(NameStyle style) {
        StringBuilder sb = new StringBuilder();
        for (NamePart part : style.pattern.get(nameForm)) {
            String namePart = getNamePart(part);
            if (!namePart.isBlank()
                && !namePart.startsWith(",")
                && !(part == NamePart.GIVEN_NAME && nameForm == NameForm.EASTERN && !middleName.isBlank())
            ) {
                sb.append(" ");
            }
            sb.append(namePart);
        }
        String name = sb.toString().replaceAll("\\s+", " ").trim();
        return name;
    }

    /**
     * Get this name in the Legal Style: Given Middle Family Ordinal
     * <p>
     * Example: Joseph Robinette Biden Jr.
     * 
     * @return Name in Legal Style
     */
    public String getLegalName() {
        return getNameInStyle(NameStyle.LEGAL);
    }

    /**
     * Get this name in the Formal Style: Honorific Given Middle Family Ordinal
     * <p>
     * Example: Rev. Martin Luther King Jr.
     */
    public String getFormalName() {
        return getNameInStyle(NameStyle.FORMAL);
    }

    /**
     * Get this name in the Biographical Style: Honorific Given Middle Nickname?
     * Family Ordinal
     * <p>
     * Example: Pres. James Earl "Jimmy" Carter Jr.
     * 
     * @return Name in Biographical Style
     */
    public String getBiographicalName() {
        return getNameInStyle(NameStyle.BIOGRAPHICAL);
    }

    /**
     * Get this name in the Common Style: First Middle Family
     * <p>
     * Example: George W. Bush
     * 
     * @return Name in Common Style
     */
    public String getCommonName() {
        return getNameInStyle(NameStyle.COMMON);
    }

    /**
     * Get this name in the Informal Style: First/Nickname Family
     * <p>
     * Example: Bill Clinton
     * 
     * @return Name in Informal Style
     */
    public String getInformalName() {
        return getNameInStyle(NameStyle.INFORMAL);
    }

    public String getNamePart(NamePart part) {
        return switch (part) {
            case HONORIFIC            -> getHonorific();
            case GIVEN_NAME           -> getGivenName();
            case PREFERRED_NAME       -> getPreferredName();
            case PREFERRED_FIRST      -> getPreferredFirst();
            case MIDDLE_NAME          -> getMiddleName();
            case PREFERRED_MIDDLE     -> getPreferredMiddle();
            case NICKNAME             -> getNickname();
            case NICKNAME_QUOTED      -> ("\"" + getNickname() + "\"").replace("\"\"", "").trim();
            case FAMILY_NAME          -> getFamilyName();
            case ORDINAL              -> getOrdinal();
            case SUFFIXES             -> getFormattedSuffixes();
            case GENERATION           -> getMiddleName();
            case PREFERRED_GENERATION -> displayOptions.contains(DisplayOption.LATENT_GENERATION) ? "" : getMiddleName();
            case WESTERN_NAME         -> getWesternName();
            case WESTERN_NAME_QUOTED  -> ("\"" + getWesternName() + "\"").replace("\"\"", "").trim();
            case APELLIDO_1           -> {
                if (displayOptions.contains(DisplayOption.MATERNAL_FIRST))
                    yield getMaternalName();
                else if (displayOptions.contains(DisplayOption.PATERNAL_FIRST))
                    yield getPaternalName();
                else
                    yield "";
            }
            case APELLIDO_2           -> {
                if (displayOptions.contains(DisplayOption.MATERNAL_FIRST))
                    yield getPaternalName();
                else if (displayOptions.contains(DisplayOption.PATERNAL_FIRST))
                    yield getMaternalName();
                else
                    yield "";
            }
        };
    }

    private String getPreferredName() {
        if (displayOptions.contains(DisplayOption.INCLUDE_NICKNAME))
            return nickname;
        if (displayOptions.contains(DisplayOption.PREFER_MIDDLE))
            return middleName;
        else return givenName;
    }

    private String getPreferredFirst() {
        return displayOptions.contains(DisplayOption.ABBREVIATE_FIRST) ? abbreviate(givenName) : givenName;
    }

    private String getPreferredMiddle() {
        return displayOptions.contains(DisplayOption.ABBREVIATE_MIDDLE) ? abbreviate(middleName) : middleName;
    }

    public void setHonorific(String honorific) {
        this.honorific = honorific;
    }

    private String getHonorific() {
        return honorific;
    }

    private String abbreviate(String name) {
        char initial;
        name = name.strip();
        for (int i = 0; i < name.length(); i++) {
            initial = name.charAt(i);
            if (Character.isAlphabetic(initial))
                return String.valueOf(initial);
        }
        return "";
    }

    private String getFormattedSuffixes() {
        StringBuilder sb = new StringBuilder();
        for (String suffix : suffixes) {
            sb.append(", ").append(suffix);
        }
        return sb.toString();
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    public String toRepr() {
        String[] suffixesStrings = new String[suffixes.size()];
        for (int i = 0; i < suffixes.size(); i++) {
            suffixesStrings[i] = suffixes.get(i);
        }
        String suffixesRepr = Repr.arrayToReprList(suffixesStrings);

        String repr = String.format(
                "%s:[nameForm=\"%s\";givenName=\"%s\";middleName=\"%s\";familyName=\"%s\";paternalName=\"%s\";maternalName=\"%s\";nickname=\"%s\";honorific=\"%s\";ordinal=\"%s\";suffixes=[%s];];",
                this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
                this.nameForm.toString(),
                givenName,
                middleName,
                familyName,
                paternalName,
                maternalName,
                nickname,
                honorific,
                ordinal,
                suffixesRepr
        );
        return repr;
    }

    public Name fromRepr(String repr) {
        return this;
    }

    public Name fromJson(JSONObject nameJson) {
        return this;
    }

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("name_form", nameForm.toString()));
        if (givenName != null && !givenName.isEmpty())
            fields.add(new JSONObject("given_name", givenName));
        if (middleName != null && !middleName.isEmpty())
            fields.add(new JSONObject("middle_name", middleName));
        if (familyName != null && !familyName.isEmpty())
            fields.add(new JSONObject("family_name", familyName));
        if (birthSurname != null && !birthSurname.isEmpty())
            fields.add(new JSONObject("birth_surname", birthSurname));
        if (paternalName != null && !paternalName.isEmpty())
            fields.add(new JSONObject("paternal_name", paternalName));
        if (maternalName != null && !maternalName.isEmpty())
            fields.add(new JSONObject("maternal_name", maternalName));
        if (nickname != null && !nickname.isEmpty())
            fields.add(new JSONObject("nickname", nickname));
        if (religiousName != null && !religiousName.isEmpty())
            fields.add(new JSONObject("religious_name", religiousName));
        if (westernName != null && !westernName.isEmpty())
            fields.add(new JSONObject("western_name", westernName));
        // legal name is derived
        // informal name is derived
        if (honorific != null && !honorific.isEmpty())
            fields.add(new JSONObject("honorific", honorific));
        if (ordinal != null && !ordinal.isEmpty())
            fields.add(new JSONObject("ordinal", ordinal));
        if (suffixes != null && !suffixes.isEmpty())
            fields.add(new JSONObject("suffixes", List.copyOf(suffixes)));
        if (displayOptions != null && !displayOptions.isEmpty()) {
            List<String> displayOptionsWords = new ArrayList<>();
            for (DisplayOption option : displayOptions) {
                displayOptionsWords.add(option.toString());
            }
            fields.add(new JSONObject("display_options", displayOptionsWords));
        }
        return new JSONObject(String.format("%s (%d)", getLegalName(), hashCode()), fields);
    }

    // OBJECT METHODS -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 7;
        hash = prime * hash + (givenName  == null ? 0 : givenName.hashCode());
        hash = prime * hash + (middleName == null ? 0 : middleName.hashCode());
        hash = prime * hash + (familyName == null ? 0 : familyName.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return getBiographicalName();
    }
}