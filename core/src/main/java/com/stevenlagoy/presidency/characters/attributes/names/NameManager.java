package com.stevenlagoy.presidency.characters.attributes.names;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.characters.attributes.Family;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.map.MapEntity;
import com.stevenlagoy.presidency.util.CollectionUtils;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.Logger;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>NAME MANAGER</h1>
 * {@code ~/characters/attributes/names/NameManager.java}
 * <p>
 *     <b>Author:</b>
 *     Steven LaGoy
 *     <br>
 *     <b>Created:</b>
 *     01 June 2025 at 1:04 AM
 *     <br>
 *     <b>Modified:</b>
 *     08 April 2026
 * </p>
 * NameManager is responsible for creating and maintaining information about PersonalNames
 * @implNote PersonalName is an abstract class with several concretions. This class violates the open-closed principle as specific name forms (concretions)
 * must each have separate implementation details. At this time, this is considered acceptable because the number of possible name forms is not
 * expected to increase quickly enough to warrant a more abstract strategy-pattern solution to concrete name class construction.
 */
public class NameManager extends Manager {

    // CONSTANTS ----------------------------------------------------------------------------------------------------------------------------------------------

    // PERCENTAGES FOR WESTERN-STYLE NAMES
    /** Percentage of people with multiple first names. Percentage for three first names is this value squared, and so on. */
    public static final float multipleFirstNamesPercent = 0.01f;
    /** Percentage of people who have one or more middle names. */
    public static final float middleNamePercent = 0.90f;
    /** Percentage of people who prefer their middle name over first name. */
    public static final float preferMiddleNamePercent = 0.10f;
    /** Percentage of people with multiple middle names. Percentage for three middle names is this value squared, and so on. */
    public static final float multipleMiddleNamesPercent = 0.05f;
    /** Percentage of people who include their middle name in their common name. */
    public static final float includeMiddleNameInCommonPercent = 0.15f;
    /** Percentage of people who commonly use a nickname. */
    public static final float nicknamePercent = 0.33f;
    /** Percentage of people with a nickname whose nickname is based on their middle name. */
    public static final float nicknameFromMiddleNamePercent = 0.10f;
    /** Percentage of people with a nickname based on the abbreviations of their names, like Thomas James -> TJ. */
    public static final float nicknameFromAbbreviationPercent = 0.15f;
    /** Percentage controlling length of nicknames based on abbreviations. This is the chance for each additional letter to be included. */
    public static final float nicknameFromAbbreviationLengthPercent = 0.6f;
    /** Percentage of people who commonly use a nickname which is not based on one of their given names. */
    public static final float nicknameNotFromGivenNamesPercent = 0.05f;
    /** Percentage of people who have a double-barreled surname or multiple surnames. */
    public static final float doubleBarrelledSurnamePercent = 0.055f;
    /** Percentage of people with multiple surnames who hyphenate one or more of those surnames. */
    public static final float hyphenatedSurnamePercent = 0.30f;
    /** Percentage of people with the Senior ordination. */
    public static final float srOrdinationPercent = 0.04f;
    /** Percentage of people with the Junior ordination. */
    public static final float jrOrdinationPercent = 0.08f;
    /** Percentage of people with the I ordination. */
    public static final float iOrdinationPercent = 0.005f;
    /** Percentage of people with the II ordination. */
    public static final float iiOrdinationPercent = 0.025f;
    /** Percentage of people with the III ordination. */
    public static final float iiiOrdinationPercent = 0.02f;
    /** Percentage of people with the MD suffix. */
    public static final float mdSuffixPercent = 0.001f;
    /** Percentage of people with the PhD suffix. */
    public static final float phdSuffixPercent = 0.0075f;
    /** Percentage of people with the Esq suffix. */
    public static final float esqSuffixPercent = 0.0025f;
    /** Percentage of people who commonly abbreviate their first name(s). */
    public static final float abbreviateFirstNamesPercent = 0.06f;
    /** Percentage of people who commonly abbreviate their middle name(s). */
    public static final float abbreviateMiddleNamesPercent = 0.38f;
    /** Percentage of people who commonly abbreviate all their given names. */
    public static final float abbreviateBothNamesPercent = 0.08f;

    public static final float manMrHonorificPercent = 0.85f;
    public static final float womanMsHonorificPercent = 0.30f;
    /** Percentage of woman who are not Ms. who are Mrs. */
    public static final float womanMrsHonorificPercent = 0.80f;
    public static final float drHonorificPercent = 0.12f;
    public static final float nonBinaryMxHonorificPercent = 0.10f;
    public static final float nonBinaryMrHonorificPercent = 0.20f;
    public static final float nonBinaryMsHonorificPercent = 0.20f;

    // PERCENTAGES FOR HISPANIC-STYLE NAMES
    /** Percentage of Hispanic people who have a Hispanic-style name. */
    public static final float hispanicHispanicNamePercent = 0.80f;
    /** Percentage of Hispanic people with multiple forenames. Percentage for three forenames is this value squared, and so on. */
    public static final float hispanicMultipleForenamesPercent = 0.25f;
    /** Odds that one part of a hispanic surname is composite. */
    public static final float hispanicCompositeSurnamePercent = 0.33f;
    /** Possible conjoiners for composite surnames */
    public static final Map<String, Set<String>> hispanicCompositeSurnameConjoiners = Map.of(
        "Brazilian", Set.of(" ", " e ", "-", " de "), // Also do, da, dos, das
        "Portuguese", Set.of(" ", " e ", "-", " de "),
        "Galician", Set.of(" ", " e ", "-", " de "),
        "Catalan", Set.of(" ", " i ", "-", " d'"),
        "Basque", Set.of(" ", " eta ", "-", " de "),
        "Hispanic", Set.of(" ", " y ", "-", " de ") // Catch-all
    );

    // PERCENTAGES FOR EASTERN-STYLE NAMES
    /** Percentage of Asian people who have an Eastern-style name. */
    public static final float asianEasternNamePercent = 0.30f;
    /** Percentage of people with a generation name. */
    public static final float generationNamePercent = 0.60f;
    /** Percentage of people with an additional Western-style name. */
    public static final float westernNamePercent = 0.50f;
    /** Percentage of people who place their western name before their traditional name. */
    public static final float westernNameFirstPercent = 0.33f;

    // PERCENTAGES FOR NATIVE-AMERICAN-STYLE NAMES
    public static final float nativeNativeNamePercent = 0.25f;


    public record NameContext(Demographics demographics, int age, Family family, MapEntity location) {}

    // INSTANCE FIELDS ----------------------------------------------------------------------------

    private Map<Set<Bloc>, Map<String, Double>> givenNamesDistribution;
    private Map<Set<Bloc>, Map<String, Double>> familyNamesDistribution;
    private Map<Set<Bloc>, Map<String, Double>> generationNamesDistribution;
    private Map<String, List<String>> nicknames;

    private final Engine ENGINE;
    private ManagerState currentState;

    // CONSTRUCTOR --------------------------------------------------------------------------------

    public NameManager(Engine engine) {
        ENGINE = engine;
        currentState = ManagerState.INACTIVE;

        givenNamesDistribution      = new HashMap<>();
        familyNamesDistribution     = new HashMap<>();
        generationNamesDistribution = new HashMap<>();
        nicknames                   = new HashMap<>();
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    @Override
    public boolean init() {
        readGivenNamesData();
        readFamilyNamesData();
        readGenerationNamesData();
        readNicknamesData();
        currentState = ManagerState.ACTIVE;
        return true;
    }

    @Override
    public boolean cleanup() {
        currentState = ManagerState.INACTIVE;
        return true;
    }

    @NotNull
    @Override
    public ManagerState getState() {
        return currentState;
    }

    // INSTANCE METHODS ---------------------------------------------------------------------------

    private void readGivenNamesData() {
        JSONObject json = JSONProcessor.processJson(FilePaths.GIVEN_NAMES);
        givenNamesDistribution = processNamesStructure(json);
    }

    private void readFamilyNamesData() {
        JSONObject json = JSONProcessor.processJson(FilePaths.FAMILY_NAMES);
        familyNamesDistribution = processNamesStructure(json);
    }

    private void readGenerationNamesData() {
        JSONObject json = JSONProcessor.processJson(FilePaths.GENERATION_NAMES);
        generationNamesDistribution = processNamesStructure(json);
    }

    private void readNicknamesData() {
        JSONObject json = JSONProcessor.processJson(FilePaths.NICKNAMES);
        nicknames = new HashMap<>();
        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry)) continue;
            String key = entry.getKey();
            List<?> value = entry.getAsList();
            List<String> names = new ArrayList<>();
            for (Object nickname : value) {
                names.add((String) nickname);
            }
            nicknames.put(key, names);
        }
    }

    private @NotNull Map<Set<Bloc>, Map<String, Double>> processNamesStructure(@NotNull JSONObject json) {
        return processNamesStructure(json, null);
    }

    private @NotNull Map<Set<Bloc>, Map<String, Double>> processNamesStructure(@NotNull JSONObject json, @Nullable Set<Bloc> currentBlocs) {
        if (currentBlocs == null) currentBlocs = new HashSet<>();
        Map<Set<Bloc>, Map<String, Double>> distributions = new HashMap<>();

        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry)) continue;

            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Number) {
                // This is a name-number pair
                // Add the name and value to the distributions with the current blocs keyset
                Map<String, Double> d = distributions.get(currentBlocs);
                if (d == null) d = new HashMap<>();
                d.put(key, ((Number) value).doubleValue());
                distributions.put(currentBlocs, d);
            }
            else if (value instanceof List<?>) {
                // This is a nested structure
                // If key is a valid bloc, add it to a new bloc set
                Bloc bloc = ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.matchBlocName(key);
                Set<Bloc> updatedBlocs = new HashSet<>(currentBlocs);
                updatedBlocs.add(bloc);
                // Recurse with updated bloc set
                Map<Set<Bloc>, Map<String, Double>> subDistribution = processNamesStructure(entry, updatedBlocs);
                for (Set<Bloc> k : subDistribution.keySet()) {
                    Map<String, Double> v = subDistribution.get(k);
                    distributions.put(k, v);
                }
            }
        }
        return distributions;
    }

    // JSONIC -------------------------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        return new JSONObject("NameManager", Map.of(

        ));
    }

    @Override
    public NameManager fromJson(JSONObject json) {
        return this;
    }

    // NAME BUILDER -------------------------------------------------------------------------------

    /**
     * Builder pattern to create a complete Personal Name based on context values
     * @param demographics Demographics of the person being given a name
     * @param age Age of a person being given a name
     * @param family Family of a person being given a name
     * @param location Location of a person being given a name
     * @return Complete Personal Name instance catered to the passed information
     */
    public @NotNull PersonalName buildPersonalName(@NotNull Demographics demographics, int age, @NotNull Family family, @NotNull MapEntity location) {
        return buildPersonalName(new NameContext(demographics, age, family, location));
    }

    /**
     * Builder pattern to create a complete Personal Name based on Name Context
     * @param context Name Context for a person being given a name
     * @return Complete Personal Name instance catered to the Name Context
     */
    public @NotNull PersonalName buildPersonalName(@NotNull NameContext context) {
        Class<? extends PersonalName> nameForm = selectNameForm(context);
        PersonalName name;
        if (nameForm.equals(HispanicPersonalName.class)) {
            name = new HispanicPersonalName();
            name = fillHispanicPersonalNameFields(name, context);
        } else if (nameForm.equals(EasternPersonalName.class)) {
            name = new EasternPersonalName();
            name = fillEasternPersonalNameFields(name, context);
        } else { // Default case: Western Personal Name
            name = new WesternPersonalName();
            name = fillWesternPersonalNameFields(name, context);
        }
        fillHonorific(name, context);
        fillSuffixes(name, context);
        return name;
    }

    /**
     * Select a name form (concretion of PersonalName) based on demographic context
     * @param context Context object containing demographic information.
     * @return Concrete class extending PersonalName
     */
    private @NotNull Class<? extends PersonalName> selectNameForm(@NotNull NameContext context) {
        if (context.demographics.getRaceEthnicity().getAncestorNames().contains("Asian")) {
            if (RandomUtils.randNextPercent() <= asianEasternNamePercent) {
                return EasternPersonalName.class;
            }
        }
        else if (context.demographics.getRaceEthnicity().getAncestorNames().contains("Hispanic / Latino")) {
            if (context.demographics.getRaceEthnicity().getAncestorNames().contains("Argentinian")) {
                return WesternPersonalName.class; // Only the paternal apellido is inherited in the Argentinian custom
            }
            if (RandomUtils.randNextPercent() <= hispanicHispanicNamePercent) {
                return HispanicPersonalName.class;
            }
        }
        return WesternPersonalName.class;
    }

    private void fillHonorific(@NotNull PersonalName name, @NotNull NameContext context) {
        if (RandomUtils.chance(drHonorificPercent)) {
            name.setHonorific("Dr.");
        }
        switch (context.demographics.getPresentation().getName()) {
            case "Man" :
                if (RandomUtils.chance(manMrHonorificPercent)) {
                    name.setHonorific("Mr.");
                }
                break;
            case "Woman" :
                if (RandomUtils.chance(womanMsHonorificPercent)) {
                    name.setHonorific("Ms.");
                }
                else if (RandomUtils.chance(womanMrsHonorificPercent)) {
                    name.setHonorific("Mrs.");
                }
                break;
            case "Non-Binary" :
                if (RandomUtils.chance(nonBinaryMxHonorificPercent)) {
                    name.setHonorific("Mx.");
                }
                else if (RandomUtils.chance(nonBinaryMrHonorificPercent)) {
                    name.setHonorific("Mr.");
                }
                else if (RandomUtils.chance(nonBinaryMsHonorificPercent)) {
                    name.setHonorific("Ms.");
                }
                break;
        }
    }

    private void fillSuffixes(@NotNull PersonalName name, @NotNull NameContext context) {
        if (RandomUtils.chance(esqSuffixPercent)) {
            name.getSuffixes().add("Esq.");
        }
        if (RandomUtils.chance(mdSuffixPercent)) {
            name.getSuffixes().add("MD");
        }
        if (RandomUtils.chance(phdSuffixPercent)) {
            name.getSuffixes().add("PhD");
        }
    }

    // Western Personal Name ----------------------------------------------------------------------

    private @NotNull WesternPersonalName fillWesternPersonalNameFields(@NotNull PersonalName baseName, @NotNull NameContext context) {
        WesternPersonalName westernName = new WesternPersonalName(baseName);
        fillFirstName(westernName, context);
        fillMiddleName(westernName, context);
        fillLastName(westernName, context);
        fillNickname(westernName, context);
        fillOrdinal(westernName, context);
        return westernName;
    }

    private void fillFirstName(@NotNull WesternPersonalName name, @NotNull NameContext context) {
        // Choose number of first names
        int numberFirstNames = 1 + RandomUtils.probabilisticCount(multipleFirstNamesPercent);
        // Choose appropriate names
        String firstName = selectNUniqueGivenNames(context, numberFirstNames);
        // Set
        name.setFirstName(firstName);
        // Determine whether to abbreviate first name
        if (RandomUtils.chance(abbreviateFirstNamesPercent)) {
            name.getDisplayOptions().add(PersonalName.DisplayOption.ABBREVIATE_FIRST);
        }
        else if (RandomUtils.chance(abbreviateBothNamesPercent)) {
            name.getDisplayOptions().add(PersonalName.DisplayOption.ABBREVIATE_FIRST);
            name.getDisplayOptions().add(PersonalName.DisplayOption.ABBREVIATE_MIDDLE);
        }
    }

    private void fillMiddleName(@NotNull WesternPersonalName name, @NotNull NameContext context) {
        // Determine whether to have a middle name
        if (!RandomUtils.chance(middleNamePercent)) {
            // If there are multiple first names, use the last one as middle name
            String[] firstNames = name.getFirstName().split("\\s+");
            if (firstNames.length > 1) {
                // Reconstitute first name
                String firstName = Arrays.stream(firstNames).limit(firstNames.length - 2).collect(Collectors.joining(" ")).strip();
                // Grab last of the first names as middle name
                String middleName = firstNames[firstNames.length - 1].strip();
                // Set
                name.setFirstName(firstName);
                name.setMiddleName(middleName);
                return;
            }
            // Otherwise set middle name to null
            name.setMiddleName(null);
            return;
        }
        // Choose number of middle names
        int numberMiddleNames = RandomUtils.probabilisticCount(multipleMiddleNamesPercent);
        // Choose appropriate names
        String middleName = selectNUniqueGivenNames(context, numberMiddleNames);
        // Set
        name.setMiddleName(middleName);
        // Determine whether to abbreviate middle name
        if (RandomUtils.chance(abbreviateMiddleNamesPercent)) {
            name.getDisplayOptions().add(PersonalName.DisplayOption.ABBREVIATE_MIDDLE);
        }
        // Determine whether to prefer middle name
        if (RandomUtils.chance(preferMiddleNamePercent)) {
            name.getDisplayOptions().add(PersonalName.DisplayOption.PREFER_MIDDLE);
        }
        // Determine whether to include middle name in common name
        if (RandomUtils.chance(includeMiddleNameInCommonPercent)) {
            name.getDisplayOptions().add(PersonalName.DisplayOption.INCLUDE_MIDDLE);
        }
    }

    private void fillLastName(@NotNull WesternPersonalName name, @NotNull NameContext context) {
        // Determine how many last names to have
        int numberLastNames = 1 + RandomUtils.probabilisticCount(doubleBarrelledSurnamePercent);
        // Select conjoiner pattern
        String[] conjoiners = new String[numberLastNames];
        for (int i = 0; i < numberLastNames; i++) {
            conjoiners[i] = RandomUtils.chance(hyphenatedSurnamePercent) ? "-" : " ";
        }
        // Choose appropriate names
        String lastName = selectNDistinctFamilyNames(context, numberLastNames, conjoiners);
        // Set
        name.setLastName(lastName);
    }

    private void fillNickname(@NotNull WesternPersonalName name, @NotNull NameContext context) {
        // Determine whether to have a nickname
        if (!RandomUtils.chance(nicknamePercent)) {
            name.setNickname(null);
            return;
        }
        // Determine if nickname is not based on given names
        if (RandomUtils.chance(nicknameNotFromGivenNamesPercent)) {
            // Choose a given name as nickname
            String nickname = selectGivenName(context);
            // Determine whether to shorten that nickname
            if (nicknames.containsKey(nickname) && RandomUtils.chance(nicknamePercent)) {
                nickname = RandomUtils.randSelect(nicknames.get(nickname));
            }
            name.setNickname(nickname);
        }
        // Determine if nickname comes from middle name
        else if (name.getMiddleName() != null && RandomUtils.chance(nicknameFromMiddleNamePercent)) {
            // Choose a middle name to nick
            String nickedMiddle;
            do {
                nickedMiddle = RandomUtils.randSelect(name.getMiddleName().split("\\s+"));
            } while (!nicknames.containsKey(nickedMiddle));
            String nickname = RandomUtils.randSelect(nicknames.get(nickedMiddle));
            name.setNickname(nickname);
        }
        // Determine if nickname comes from initials
        else if (RandomUtils.chance(nicknameFromAbbreviationPercent)) {
            String initials = name.getInitials();
            int length = RandomUtils.probabilisticCount(nicknameFromAbbreviationLengthPercent);
            String nickname = initials.substring(0, length);
            name.setNickname(nickname);
        }
        // Nickname comes from first name
        else {
            // Choose a first name to nick
            String nickedFirst;
            do {
                nickedFirst = RandomUtils.randSelect(name.getFirstName().split("\\s+"));
            } while (!nicknames.containsKey(nickedFirst));
            String nickname = RandomUtils.randSelect(nicknames.get(nickedFirst));
            name.setNickname(nickname);
        }
    }

    private void fillOrdinal(@NotNull WesternPersonalName name, @NotNull NameContext context) {
        if (RandomUtils.chance(srOrdinationPercent)) {
            name.setOrdinal("Sr.");
        }
        else if (RandomUtils.chance(jrOrdinationPercent)) {
            name.setOrdinal("Jr.");
        }
        else if (RandomUtils.chance(iOrdinationPercent)) {
            name.setOrdinal("I");
        }
        else if (RandomUtils.chance(iiOrdinationPercent)) {
            name.setOrdinal("II");
        }
        else if (RandomUtils.chance(iiiOrdinationPercent)) {
            name.setOrdinal("III");
        }
    }

    // Hispanic Personal Name ---------------------------------------------------------------------

    private @NotNull HispanicPersonalName fillHispanicPersonalNameFields(@NotNull PersonalName baseName, @NotNull NameContext context) {
        HispanicPersonalName hispanicName = new HispanicPersonalName(baseName);
        fillGivenName(hispanicName, context);
        fillPaternalName(hispanicName, context);
        fillMaternalName(hispanicName, context);
        fillNickname(hispanicName, context);
        return hispanicName;
    }

    private void fillGivenName(@NotNull HispanicPersonalName name, @NotNull NameContext context) {
        // Choose number of given names
        int numberGivenNames = 1 + RandomUtils.probabilisticCount(hispanicMultipleForenamesPercent);
        // Choose appropriate names
        String givenName = selectNUniqueGivenNames(context, numberGivenNames);
        // Set
        name.setGivenName(givenName);
    }

    private void fillPaternalName(@NotNull HispanicPersonalName name, @NotNull NameContext context) {
        name.setPaternalName(selectApellido(context));
    }

    private void fillMaternalName(@NotNull HispanicPersonalName name, @NotNull NameContext context) {
        name.setMaternalName(selectApellido(context));
    }

    private @NotNull String selectApellido(@NotNull NameContext context) {
        // Choose number of family names in the apellido
        int numberNames = 1 + RandomUtils.probabilisticCount(hispanicCompositeSurnamePercent);
        // Choose conjoiners
        Set<String> possibleConjoiners = hispanicCompositeSurnameConjoiners.get("Hispanic");
        for (String blocName : context.demographics.getRaceEthnicity().getAncestorNames()) {
            if (hispanicCompositeSurnameConjoiners.containsKey(blocName)) {
                possibleConjoiners = hispanicCompositeSurnameConjoiners.get(blocName);
                break;
            }
        }
        String[] conjoiners = new String[numberNames];
        for (int i = 0; i < numberNames; i++) {
            conjoiners[i] = RandomUtils.randSelect(possibleConjoiners);
        }
        // Create apellido
        return selectNDistinctFamilyNames(context, numberNames, conjoiners);
    }

    private void fillNickname(@NotNull HispanicPersonalName name, @NotNull NameContext context) {
        // Determine whether to have a nickname
        if (!RandomUtils.chance(nicknamePercent)) {
            name.setNickname(null);
            return;
        }
        // Determine if nickname is not based on given names
        if (RandomUtils.chance(nicknameNotFromGivenNamesPercent)) {
            // Choose a given name as nickname
            String nickname = selectGivenName(context);
            // Determine whether to shorten that nickname
            if (nicknames.containsKey(nickname) && RandomUtils.chance(nicknamePercent)) {
                nickname = RandomUtils.randSelect(nicknames.get(nickname));
            }
            name.setNickname(nickname);
        }
        // Nickname comes from first name
        else {
            // Choose a first name to nick
            String nickedFirst;
            do {
                nickedFirst = RandomUtils.randSelect(name.getGivenName().split("\\s+"));
            } while (!nicknames.containsKey(nickedFirst));
            String nickname = RandomUtils.randSelect(nicknames.get(nickedFirst));
            name.setNickname(nickname);
        }
    }

    // Eastern Personal Name ----------------------------------------------------------------------

    private @NotNull EasternPersonalName fillEasternPersonalNameFields(@NotNull PersonalName baseName, @NotNull NameContext context) {
        EasternPersonalName easternName = new EasternPersonalName(baseName);
        fillFamilyName(easternName, context);
        fillGenerationName(easternName, context);
        fillGivenName(easternName, context);
        fillWesternName(easternName, context);
        return easternName;
    }

    private void fillFamilyName(@NotNull EasternPersonalName name, @NotNull NameContext context) {
        name.setFamilyName(selectFamilyName(context));
    }

    private void fillGenerationName(@NotNull EasternPersonalName name, @NotNull NameContext context) {
        // Determine whether to have a generation name
        if (!RandomUtils.chance(generationNamePercent)) {
            name.setGenerationName(null);
            return;
        }
        // Select a generation name
        String generationName = selectGenerationName(context);
        // Set
        name.setGenerationName(generationName);
    }

    private void fillGivenName(@NotNull EasternPersonalName name, @NotNull NameContext context) {
        name.setGivenName(selectGivenName(context));
    }

    private void fillWesternName(@NotNull EasternPersonalName name, @NotNull NameContext context) {
        // Determine whether to have a Western name
        if (!RandomUtils.chance(westernNamePercent)) {
            name.setWesternName(null);
            return;
        }
        // Select a Western name
        String westernName = selectGivenName(new NameContext(
            new Demographics(
                context.demographics.getGeneration(),
                context.demographics.getReligion(),
                Objects.requireNonNull(ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.matchBlocName("Anglo")),
                context.demographics.getPresentation()
            ),
            context.age,
            context.family,
            context.location
        ));
        name.setWesternName(westernName);
    }

    // General selection --------------------------------------------------------------------------

    private String selectGivenName(@NotNull NameContext context) {
        return RandomUtils.weightedSelect(getGivenNamesDistribution(context));
    }

    private String selectFamilyName(@NotNull NameContext context) {
        return RandomUtils.weightedSelect(getFamilyNamesDistribution(context));
    }

    private String selectGenerationName(@NotNull NameContext context) {
        return RandomUtils.weightedSelect(getGenerationNamesDistribution(context));
    }

    private @NotNull String selectNUniqueGivenNames(@NotNull NameContext context, int n) {
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String selected;
            do {
                selected = selectGivenName(context);
            } while (nameBuilder.toString().contains(selected));
            nameBuilder.append(selected).append(" ");
        }
        return nameBuilder.toString().strip();
    }

    private @NotNull String selectNDistinctFamilyNames(@NotNull NameContext context, int n) {
        return selectNDistinctFamilyNames(context, n, " ");
    }

    private @NotNull String selectNDistinctFamilyNames(@NotNull NameContext context, int n, String... conjoinerPattern) {
        StringBuilder nameBuilder = new StringBuilder();
        Iterator<String> conjoinerStream = Arrays.stream(conjoinerPattern).iterator();
        for (int i = 0; i < n; i++) {
            String selected;
            do {
                selected = selectFamilyName(context);
            } while (nameBuilder.toString().contains(selected));
            nameBuilder.append(selected);
            try {
                nameBuilder.append(conjoinerStream.next());
            } catch (NoSuchElementException e) { // Ran out - fallback with first conjoiner
                nameBuilder.append(conjoinerPattern[0]);
            }
        }
        return nameBuilder.toString().strip();
    }

    private @NotNull Map<String, Double> getGivenNamesDistribution(@NotNull NameContext context) {
        return getNamesDistributionForContext(context, givenNamesDistribution);
    }

    private @NotNull Map<String, Double> getFamilyNamesDistribution(@NotNull NameContext context) {
        return getNamesDistributionForContext(context, familyNamesDistribution);
    }

    private @NotNull Map<String, Double> getGenerationNamesDistribution(@NotNull NameContext context) {
        return getNamesDistributionForContext(context, generationNamesDistribution);
    }

    private @NotNull Map<String, Double> getNamesDistributionForContext(@NotNull NameContext context, @NotNull Map<Set<Bloc>, Map<String, Double>> namesDistribution) {
        Set<Set<Bloc>> realKeys = namesDistribution.keySet();

        // Get targets including ancestor blocs
        Set<Bloc> targets = new HashSet<>();
        for (Bloc bloc : context.demographics.getBlocs()) {
            targets.addAll(bloc.getAncestors());
        }

        Set<Set<Bloc>> combinations = CollectionUtils.combinations(targets);
        // Sort out invalid combinations
        Set<Set<Bloc>> validCombinations = combinations.stream().filter(realKeys::contains).collect(Collectors.toSet());

        // Check that there is a valid combination
        if (validCombinations.isEmpty()) {
            Logger.log("NO VALID NAMES", String.format("Could not find any valid names with these target demographics: %s", targets));
            return new HashMap<>();
        }

        // Pick one of the combinations with the most included Blocs
        Set<Bloc> bestCombination = Collections.max(combinations, Comparator.comparingInt(Set::size));

        return namesDistribution.get(bestCombination);
    }
}
