package com.stevenlagoy.presidency.characters.attributes.names;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import core.JSONObject;
import core.JSONProcessor;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.characters.attributes.names.Name.DisplayOption;
import com.stevenlagoy.presidency.characters.attributes.names.Name.NameForm;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.CollectionOperations;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.Logger;
import com.stevenlagoy.presidency.util.RandomOperations;

public final class NameManager extends Manager {

    // CONSTANTS ----------------------------------------------------------------------------------------------------------------------------------------------

    // PERCENTAGES FOR WESTERN-STYLE NAMES
    /** Percentage of people with multiple first names. Percentage for three first names is this value squared, and so on. */
    public static final float multipleFirstNamesPercent = 0.01f;
    /** Percentage of people who have one or more middle names. */
    public static final float middleNamePercent = 0.90f;
    /** Percentage of people with multiple middle names. Percentage for three middle names is this value squared, and so on. */
    public static final float multipleMiddleNamesPercent = 0.05f;
    /** Percentage of people who include their middle name in their common name. */
    public static final float includeMiddleNameInCommonPercent = 0.15f;
    /** Percentage of people who commonly use a nickname. */
    public static final float nicknamePercent = 0.33f;
    /** Percentage of people with a nickname based on the abbreviations of their names, like Thomas James -> TJ. */
    public static final float nicknameFromAbbreviationPercent = 0.15f;
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
    public static final float MDSuffixPercent = 0.001f;
    /** Percentage of people with the PhD suffix. */
    public static final float PhDSuffixPercent = 0.0075f;
    /** Percentage of people with the Esq suffix. */
    public static final float esqSuffixPercent = 0.0025f;
    /** Percentage of people who commonly abbreviate their first name(s). */
    public static final float abbreviateFirstNamesPercent = 0.06f;
    /** Percentage of people who commonly abbreviate their middle name(s). */
    public static final float abbreviateMiddleNamesPercent = 0.38f;
    /** Percentage of people who commonly abbreviate all their given names. */
    public static final float abbreviateBothNamesPercent = 0.08f;

    public static final float manMrHonorificPercent = 0.95f;
    /** Percentage of woman who are not Ms. who are Mrs. */
    public static final float womanMrsHonorificPercent = 0.80f;
    public static final float womanMsHonorificPercent = 0.30f;
    public static final float drHonorificPercent = 0.12f;

    // PERCENTAGES FOR HISPANIC-STYLE NAMES
    /** Percentage of Hispanic people who have a Hispanic-style name. */
    public static final float hispanicHispanicNamePercent = 0.80f;
    /** Percentage of Hispanic people with multiple forenames. Percentage for three forenames is this value squared, and so on. */
    public static final float hispanicMultipleForenamesPercent = 0.25f;
    /** Odds that one part of a hispanic surname is composite. */
    public static final float hispanicCompositeSurnamePercent = 0.33f;

    // PERCENTAGES FOR EASTERN-STYLE NAMES
    /** Percentage of Asian people who have an Eastern-style name. */
    public static final float asianEasternNamePercent = 0.30f;
    /** Percentage of people with a generational name. */
    public static final float generationalNamePercent = 0.60f;
    /** Percentage of people with an additional Western-style name. */
    public static final float westernNamePercent = 0.50f;
    /** Percentage of people who place their western name before their traditional name. */
    public static final float westernNameFirstPercent = 0.33f;

    // PERCENTAGES FOR NATIVE-AMERICAN-STYLE NAMES
    public static final float nativeNativeNamePercent = 0.25f;

    // INSTANCE VARIABLES -------------------------------------------------------------------------------------------------------------------------------------

    private Map<Set<Bloc>, Map<String, Double>> givenNamesDistribution;
    private Map<Set<Bloc>, Map<String, Double>> familyNamesDistribution;
    private Map<String, List<String>> nicknames;

    private final Engine ENGINE;
    private ManagerState currentState;

    // CONSTRUCTORS -------------------------------------------------------------------------------------------------------------------------------------------

    public NameManager(Engine engine) {
        ENGINE                  = engine;
        currentState            = ManagerState.INACTIVE;
        givenNamesDistribution  = new HashMap<>();
        familyNamesDistribution = new HashMap<>();
        nicknames               = new HashMap<>();
    }

    // MANAGER METHODS ----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean init() {
        boolean successFlag = true;
        double startTime = ENGINE.getProgramTime();
        Logger.log(String.format("%s starting at %f",this.getClass().getSimpleName(),startTime));
        successFlag = successFlag && readGivenNamesFile();
        successFlag = successFlag && readFamilyNamesFile();
        successFlag = successFlag && readNicknamesFile();
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        double endTime = ENGINE.getProgramTime();
        Logger.log(String.format("%s initialized %s at %f. Elapsed: %f",this.getClass().getSimpleName(),successFlag ? "successfully" : "unsuccessfully",endTime,
                endTime - startTime));
        return successFlag;
    }

    @Override
    public ManagerState getState() {
        return currentState;
    }

    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        if (!successFlag)
            currentState = ManagerState.ERROR;
        return successFlag;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                     INSTANCE METHODS                                      //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // SELECTING FROM DISTRIBUTIONS ---------------------------------------------------------------------------------------------------------------------------

    /**
     * Find the most specific bloc set which covers as many of the passed blocs as
     * possible. Accepts any number of any kind of blocs (do not have to be solely
     * leaf blocs, do not have to be explicitly accompanied by superblocs). Assume
     * little about how set refinement works: (A, B) may be turned to [(A) & (B)] or
     * to [(A & B)]; (A, B) may result in (A) alone being returned; (A, B, C) may
     * result in [(A & C) & (B & C)] or any other combination.
     * 
     * @param targets Collection of Blocs to refine into a key bloc set.
     * @return List of bloc sets which were determined as the best key for
     *         {@code givenNamesDistribution} while covering as many as possible of
     *         the passed blocs.
     */
    private Set<Set<Bloc>> refineGivenNamesBlocsKey(Collection<Bloc> targets) {
        return refineNamesBlocsKey(targets,getGivenNamesDistribution().keySet());
    }

    private Set<Set<Bloc>> refineFamilyNamesBlocsKey(Collection<Bloc> targets) {
        return refineNamesBlocsKey(targets,getFamilyNamesDistribution().keySet());
    }

    private Set<Set<Bloc>> refineNamesBlocsKey(Collection<Bloc> targets,Set<Set<Bloc>> realkeys) {
        Set<Bloc> targs = new HashSet<>(targets);

        // Repeatedly find and add superblocs until no more can be found
        boolean superblocsUnrepresented = true;
        while (superblocsUnrepresented) {
            superblocsUnrepresented = false;
            Set<Bloc> newTargs = new HashSet<>(targs);
            for (Bloc b : targs) {
                Bloc sb = b.getSuperBloc();
                if (sb != null) {
                    if (newTargs.add(sb))
                        superblocsUnrepresented = true;
                }
            }
            targs = newTargs;
        }

        // Generate combinations of the targets
        Set<Set<Bloc>> combinations = CollectionOperations.combinations(targs);

        // Sort out invalid combinations (not part of the real keyset)
        Set<Set<Bloc>> validCombos = new HashSet<>();
        for (Set<Bloc> combo : combinations) {
            if (realkeys.contains(combo))
                validCombos.add(combo);
        }

        // // Find maximum coverage from combinations
        // Set<Set<Bloc>> result = new HashSet<>();
        // Set<Bloc> covered = new HashSet<>();

        // // Sort valid combinations by size to prioritize specificity
        // List<Set<Bloc>> sortedCombos = new ArrayList<>(validCombos);
        // sortedCombos.sort((a, b) -> Integer.compare(b.size(), a.size()));

        // // Select combos greedily to maximize coverage
        // for (Set<Bloc> combo : sortedCombos) {
        // if (!covered.containsAll(combo)) {
        // result.add(combo);
        // covered.addAll(combo);
        // if (covered.containsAll(targs)) break;
        // }
        // }

        // Perform backtracking to find most specific solution
        Set<Set<Bloc>> result = new HashSet<>();
        backtrack(new ArrayList<>(validCombos),new HashSet<>(),targs,new HashSet<>(),result);

        return result;
    }

    /**
     * Backtracking helper method to find the most specific solution.
     *
     * @param validCombos List of valid combinations of blocs.
     * @param current     Current set of selected combinations.
     * @param targets     Set of target blocs to cover.
     * @param covered     Set of blocs already covered.
     * @param bestResult  The best result found so far.
     */
    private void backtrack(List<Set<Bloc>> validCombos,Set<Set<Bloc>> current,Set<Bloc> targets,Set<Bloc> covered,Set<Set<Bloc>> bestResult) {
        // Check if the current solution is better than the best result
        if (isBetterSolution(current,bestResult)) {
            bestResult.clear();
            bestResult.addAll(current);
        }

        // Try each valid combination
        for (int i = 0; i < validCombos.size(); i++) {
            Set<Bloc> combo = validCombos.get(i);

            // Skip if combo is already in current
            if (current.contains(combo))
                continue;

            // Add combo to current
            current.add(combo);
            Set<Bloc> newCovered = new HashSet<>(covered);
            newCovered.addAll(combo);

            // Recurse with new solution
            backtrack(validCombos,current,targets,newCovered,bestResult);

            // Backtrack: remove combo from current solution
            current.remove(combo);
        }
    }

    /**
     * Determines if the current solution is better than the best result. A solution
     * is better if it uses fewer sets or is more specific.
     *
     * @param current    The current solution being evaluated.
     * @param bestResult The best solution found so far.
     * @return True if the current solution is better, false otherwise.
     */
    private boolean isBetterSolution(Set<Set<Bloc>> current,Set<Set<Bloc>> bestResult) {
        // Prefer fewer sets
        if (bestResult.isEmpty() || current.size() < bestResult.size())
            return true;

        // If num sets is the same, prefer specificity
        if (current.size() == bestResult.size()) {
            int currentSpecificity = current.stream().mapToInt(Set::size).sum();
            int bestSpecificity = bestResult.stream().mapToInt(Set::size).sum();
            return currentSpecificity > bestSpecificity;
        }
        return false;
    }

    // GENERATING NAMES ---------------------------------------------------------------------------

    public NameForm selectNameForm(Demographics demographics) {
        if (demographics.getRaceEthnicity().getNestedNames().contains("Asian")) {
            if (RandomOperations.randPercent() <= asianEasternNamePercent) {
                return NameForm.EASTERN;
            }
        } else if (demographics.getRaceEthnicity().getNestedNames().contains("Hispanic / Latino")) {
            if (demographics.getRaceEthnicity().getNestedNames().contains("Argentinian")) {
                return NameForm.WESTERN; // Only the paternal apellido is inherited in the
                                         // Argentinian custom
            }
            if (RandomOperations.randPercent() <= hispanicHispanicNamePercent) {
                return NameForm.HISPANIC;
            }
        } else if (demographics.getRaceEthnicity().getNestedNames().contains("Native / Indian")) {
            if (RandomOperations.randPercent() <= nativeNativeNamePercent) {
                return NameForm.NATIVE_AMERICAN;
            }
        }
        return NameForm.WESTERN;
    }

    public String selectGivenName(Demographics demographics) {
        return selectGivenName(demographics.toBlocsArray());
    }

    public String selectGivenName(Bloc... blocs) {
        return selectGivenName(Arrays.asList(blocs));
    }

    public String selectGivenName(Collection<Bloc> blocs) {
        return RandomOperations.weightedRandSelect(getGivenNamesDistribution(blocs));
    }

    public String selectGenerationalName() {
        return "";
    }

    public String selectFamilyName(Demographics demographics) {
        return selectFamilyName(demographics.toBlocsArray());
    }

    public String selectFamilyName(Bloc... blocs) {
        return selectFamilyName(Arrays.asList(blocs));
    }

    public String selectFamilyName(Collection<Bloc> blocs) {
        return RandomOperations.weightedRandSelect(getFamilyNamesDistribution(blocs));
    }

    public String selectNickname(Demographics demographics,String... names) {
        return selectNickname(Arrays.asList(demographics.toBlocsArray()),Arrays.asList(names));
    }

    public String selectNickname(Demographics demographics,Collection<String> names) {
        return selectNickname(Arrays.asList(demographics.toBlocsArray()),names);
    }

    public String selectNickname(Collection<Bloc> blocs,String... names) {
        return selectNickname(blocs,Arrays.asList(names));
    }

    /**
     * Select a nickname given blocs and a collection of names.
     * 
     * @param blocs Blocs to possibly choose another name or nickname based on.
     * @param names Given (first or middle) names to possibly choose a nickname
     *              based on.
     * @return String semi-random nickname which may or may not be based on the
     *         passed names. May be empty if none of the names have a known
     *         nickname.
     */
    public String selectNickname(Collection<Bloc> blocs,Collection<String> names) {
        // Nicknames may be based on one of a person's actual names
        // (with their preferred first name being most commonly nicked)
        // or may be completely separate from their actual names.
        List<String> allNicknames = new ArrayList<>();
        if (RandomOperations.randPercent() <= nicknameNotFromGivenNamesPercent) {
            // Nickname not based on the given names, but on the blocs instead
            String newname = selectGivenName(blocs);
            allNicknames.add(newname);
            if (nicknames.containsKey(newname))
                allNicknames.addAll(nicknames.get(newname));
        } else {
            // Nickname is based on given names
            for (String name : names) {
                if (!nicknames.containsKey(name))
                    continue;
                for (String nick : nicknames.get(name)) {
                    allNicknames.add(nick);
                }
            }
        }
        // Choose one of the possible nicknames
        if (!allNicknames.isEmpty()) {
            return RandomOperations.randSelect(allNicknames);
        }
        return ""; // Failed to select, return pool empty string
    }

    /**
     * Select the number of each part of a name.
     * 
     * @param form NameForm of the name
     * @return int[3] with {firstnames, middlenames, surnames} or {givennames,
     *         generationnames, familynames}
     */
    private int[] selectPartsCounts(NameForm form) {
        int[] counts = { 0, 0, 0 };
        switch (form) {
        case WESTERN:
            counts[0] = 1 + RandomOperations.probabilisticCount(multipleFirstNamesPercent);
            counts[1] = RandomOperations.randPercent() <= middleNamePercent ? 1 + RandomOperations.probabilisticCount(multipleMiddleNamesPercent) : 0;
            counts[2] = 1 + RandomOperations.probabilisticCount(doubleBarrelledSurnamePercent);
            break;
        case EASTERN:
            counts[0] = 1;
            counts[1] = RandomOperations.randPercent() <= generationalNamePercent ? 1 : 0;
            counts[2] = 1;
            break;
        case HISPANIC:
            counts[1] = 1 + RandomOperations.probabilisticCount(hispanicMultipleForenamesPercent);
            counts[2] = 2 + RandomOperations.probabilisticCount(hispanicCompositeSurnamePercent);
            break;
        case NATIVE_AMERICAN:
            counts[0] = 1;
            counts[1] = 1;
            counts[2] = 1;
            break;
        }
        return counts;
    }

    /**
     * Combines family names according to rules of the given name form.
     * 
     * @param form         Name form to provide rules for combination. <br>
     *                     EASTERN -> First passed family name; <br>
     *                     HISPANIC -> combine in some pattern with "y", "de", "-",
     *                     " "; <br>
     *                     NATIVE_AMERICAN | WESTERN -> combine with "-", " ";
     * @param demographics Demographics to provide additional rules (like "e"
     *                     instead of "y" for Portuguese or Brazilian)
     * @param familyNames  Family names to be combined
     * @return Combined string family name following the rules of the form and
     *         demographics
     */
    private String combineFamilyNames(NameForm form,Demographics demographics,String... familyNames) {
        if (familyNames.length < 1)
            return ""; // No names to combine
        switch (form) {
        case EASTERN:
            return familyNames[0];
        case HISPANIC:
            final String[] conjoiners = { " y ", " de ", "-", " " };
            // Choose the location of the divide between the maternal and paternal family
            // names
            int divide = RandomOperations.randInt(1,familyNames.length - 1);
            String[] paternalNames = Arrays.copyOfRange(familyNames,0,divide);
            String[] maternalNames = Arrays.copyOfRange(familyNames,divide,familyNames.length);
            String paternalName = "";
            for (String s : paternalNames) {
                if (paternalName == null || paternalName.isBlank()) {
                    paternalName = s;
                    continue;
                }
                paternalName += RandomOperations.randSelect(conjoiners) + s;
            }
            String maternalName = "";
            for (String s : maternalNames) {
                if (maternalName == null || maternalName.isBlank()) {
                    maternalName = s;
                    continue;
                }
                maternalName += RandomOperations.randSelect(conjoiners) + s;
            }
            // Apply rules from demographics
            if (demographics.getRaceEthnicity().getNestedNames().contains("Brazilian")) {
                paternalName = paternalName.replace(" y "," e ");
                paternalName = paternalName.replace(" de "," do "); // 'do' or 'dos' for masculine,
                                                                    // or 'da' or
                                                                    // 'das' for feminine - consider
                                                                    // later
                maternalName = maternalName.replace(" y "," e ");
                maternalName = maternalName.replace(" de "," da "); // 'do' or 'dos' for masculine,
                                                                    // or 'da' or
                                                                    // 'das' for feminine - consider
                                                                    // later
            }
            if (demographics.getRaceEthnicity().getNestedNames().contains("Catalan")) {
                paternalName = paternalName.replace(" y "," i");
                paternalName = paternalName.replace(" de ","d'");
                maternalName = maternalName.replace(" y "," i ");
                maternalName = maternalName.replace(" de "," d'");
            }
            paternalName = paternalName.replace(" de el "," del ");
            paternalName = paternalName.replace(" d'el "," del ");
            maternalName = maternalName.replace(" de el "," del ");
            maternalName = maternalName.replace(" d'el "," del ");

            // Combine paternal and maternal name
            return String.join(" ",paternalName,maternalName);

        case NATIVE_AMERICAN:
        case WESTERN:
            String surname = "";
            for (String s : familyNames) {
                if (RandomOperations.randPercent() <= hyphenatedSurnamePercent)
                    surname += (surname.isBlank() ? "" : "-") + s;
                else
                    surname += " " + s;
            }
            return surname;
        default:
            return familyNames[0];
        }
    }

    // NAME GENERATOR -----------------------------------------------------------------------------------------------------------------------------------------

    public Name generateName(Demographics demographics) {
        Name name = new Name();
        NameForm form = selectNameForm(demographics);
        name.setNameForm(form);

        // Basic name parts
        int[] partsCounts = selectPartsCounts(form);
        String[] firstOrGivenNames = new String[partsCounts[0]];
        for (int i = 0; i < partsCounts[0]; i++) {
            firstOrGivenNames[i] = selectGivenName(demographics);
        }
        String[] middleOrGenerationalNames = new String[partsCounts[1]];
        for (int i = 0; i < partsCounts[1]; i++) {
            middleOrGenerationalNames[i] = form == NameForm.EASTERN ? selectGenerationalName() : selectGivenName(demographics);
        }
        String[] familyNames = new String[partsCounts[2]];
        for (int i = 0; i < partsCounts[2]; i++) {
            familyNames[i] = selectFamilyName(demographics);
        }
        String firstOrGivenName = String.join(" ",firstOrGivenNames);
        String middleOrGenerationalName = String.join(" ",middleOrGenerationalNames);
        String familyName = combineFamilyNames(form,demographics,familyNames);
        name.setGivenName(firstOrGivenName);
        name.setMiddleName(middleOrGenerationalName);
        name.setFamilyName(familyName);

        // Extra name parts

        // Nicknames
        if (form != NameForm.EASTERN && RandomOperations.randPercent() <= nicknamePercent) {
            List<String> allGivens = new ArrayList<>(Arrays.asList(firstOrGivenNames));
            allGivens.addAll(Arrays.asList(middleOrGenerationalNames));
            String nickname;
            if (allGivens.size() == 2 && RandomOperations.randPercent() <= nicknameFromAbbreviationPercent) {
                nickname = Character.toString(allGivens.get(0).charAt(0)) + Character.toString(allGivens.get(1).charAt(0));
            } else {
                allGivens.addAll(Arrays.asList(middleOrGenerationalNames));
                nickname = selectNickname(demographics,allGivens);
            }
            name.setNickname(nickname);
            name.addDisplayOption(DisplayOption.INCLUDE_NICKNAME);
        }

        // Western Name
        if ((form == NameForm.EASTERN || form == NameForm.NATIVE_AMERICAN) && RandomOperations.randPercent() <= westernNamePercent) {
            String westernName = selectGivenName(demographics.getPresentation(),demographics.getGeneration(),
                    ENGINE.DemographicsManager().matchBlocName("Anglo"));
            name.setWesternName(westernName);
            name.addDisplayOption(DisplayOption.INCLUDE_WESTERN);
            if (RandomOperations.randPercent() <= westernNameFirstPercent)
                name.addDisplayOption(DisplayOption.WESTERN_FIRST);
            else
                name.addDisplayOption(DisplayOption.TRADITIONAL_FIRST);
        }

        // Religious or Baptismal name

        // Honorific
        // Some will be based on being President, being Governor, being Senator, etc.
        // and should be done by the character
        boolean isMan = demographics.getPresentation().getNestedNames().contains("Man");
        boolean isWoman = demographics.getPresentation().getNestedNames().contains("Woman");
        if (RandomOperations.randPercent() <= drHonorificPercent) {
            name.setHonorific("Dr.");
            name.addDisplayOption(DisplayOption.INCLUDE_HONORIFIC);
        }
        if (isMan && RandomOperations.randPercent() <= manMrHonorificPercent) {
            name.setHonorific("Mr.");
            name.addDisplayOption(DisplayOption.INCLUDE_HONORIFIC);
        }
        if (isWoman && RandomOperations.randPercent() <= womanMrsHonorificPercent) {
            name.setHonorific("Mrs.");
            name.addDisplayOption(DisplayOption.INCLUDE_HONORIFIC);
        }
        if (isWoman && RandomOperations.randPercent() <= womanMsHonorificPercent) {
            name.setHonorific("Ms.");
            name.addDisplayOption(DisplayOption.INCLUDE_HONORIFIC);
        }

        // Ordinal
        if (form == NameForm.WESTERN) {
            boolean aa = demographics.getRaceEthnicity().getNestedNames().contains("Black");
            if (RandomOperations.randPercent() <= srOrdinationPercent) {
                name.setOrdinal("Sr.");
            } else if (RandomOperations.randPercent() <= jrOrdinationPercent * (aa ? 2 : 1)) {
                name.setOrdinal("Jr.");
            } else if (RandomOperations.randPercent() <= iOrdinationPercent * (aa ? 2 : 1)) {
                name.setOrdinal("I");
            } else if (RandomOperations.randPercent() <= iiOrdinationPercent * (aa ? 2 : 1)) {
                name.setOrdinal("II");
            } else if (RandomOperations.randPercent() <= iiiOrdinationPercent * (aa ? 2 : 1)) {
                name.setOrdinal("III");
            }
        }

        // Suffixes
        // Only people with a PhD should have that suffix. Consider how to make the
        // character do this.
        if (RandomOperations.randPercent() <= esqSuffixPercent) {
            // name.addSuffix("Esq.");
            name.addDisplayOption(DisplayOption.INCLUDE_SUFFIX);
        }

        // Abbreviation
        if (form == NameForm.WESTERN) {
            // Abbreviate first name(s)?
            if (RandomOperations.randPercent() <= abbreviateFirstNamesPercent) {
                name.addDisplayOption(DisplayOption.ABBREVIATE_FIRST);
            }
            // Abbreviate middle name(s)?
            if (RandomOperations.randPercent() <= abbreviateMiddleNamesPercent) {
                name.addDisplayOption(DisplayOption.ABBREVIATE_MIDDLE);
            }
            // Abbreviate all given names?
            if (RandomOperations.randPercent() <= abbreviateBothNamesPercent) {
                name.addDisplayOption(DisplayOption.ABBREVIATE_FIRST);
                name.addDisplayOption(DisplayOption.ABBREVIATE_MIDDLE);
            }
        }

        // Display Options
        if (form == NameForm.WESTERN && !middleOrGenerationalName.isEmpty()) {
            if (RandomOperations.randPercent() <= includeMiddleNameInCommonPercent) {
                name.addDisplayOption(DisplayOption.INCLUDE_MIDDLE);
            }
        }

        return name;
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------------------------------------------------------------------

    public Map<Set<Bloc>, Map<String, Double>> getGivenNamesDistribution() {
        if (givenNamesDistribution == null || givenNamesDistribution.isEmpty())
            readGivenNamesFile();
        return givenNamesDistribution;
    }

    /**
     * Return the given names distribution for a person who is a member of all the
     * passed blocs.
     * 
     * @param blocs Any number of blocs. This set will be refined and as many as
     *              possible will be covered.
     * @return Map of names to weights.
     */
    public Map<String, Double> getGivenNamesDistribution(Bloc... blocs) {
        return getGivenNamesDistribution(Arrays.asList(blocs));
    }

    public Map<String, Double> getGivenNamesDistribution(Demographics demographics) {
        return getGivenNamesDistribution(demographics.toBlocsArray());
    }

    /**
     * Return the given names distribution for a person who is a member of all the
     * passed blocs.
     * 
     * @param blocs Any number of blocs. This set will be refined and as many as
     *              possible will be covered.
     * @return Map of names to weights.
     */
    public Map<String, Double> getGivenNamesDistribution(Collection<Bloc> blocs) {
        Set<Set<Bloc>> blocsets = refineGivenNamesBlocsKey(blocs);
        Map<String, Double> res = new HashMap<>();
        for (Set<Bloc> blocset : blocsets) {
            Map<String, Double> distribution = getGivenNamesDistribution().get(blocset);
            for (String name : distribution.keySet()) {
                double prev = res.containsKey(name) ? res.get(name) : 0;
                res.put(name,prev + distribution.get(name));
            }
        }
        return res;
    }

    public Map<Set<Bloc>, Map<String, Double>> getFamilyNamesDistribution() {
        if (familyNamesDistribution == null || familyNamesDistribution.isEmpty())
            readFamilyNamesFile();
        return familyNamesDistribution;
    }

    public Map<String, Double> getFamilyNamesDistribution(Bloc... blocs) {
        return getFamilyNamesDistribution(Arrays.asList(blocs));
    }

    public Map<String, Double> getFamilyNamesDistribution(Demographics demographics) {
        return getFamilyNamesDistribution(demographics.toBlocsArray());
    }

    public Map<String, Double> getFamilyNamesDistribution(Collection<Bloc> blocs) {
        Set<Set<Bloc>> blocsets = refineFamilyNamesBlocsKey(blocs);
        Map<String, Double> res = new HashMap<>();
        for (Set<Bloc> blocset : blocsets) {
            Map<String, Double> distribution = getFamilyNamesDistribution().get(blocset);
            for (String name : distribution.keySet()) {
                double prev = res.containsKey(name) ? res.get(name) : 0;
                res.put(name,prev + distribution.get(name));
            }
        }
        return res;
    }

    public Map<String, List<String>> getNicknames() {
        if (nicknames == null || nicknames.isEmpty())
            readNicknamesFile();
        return nicknames;
    }

    /**
     * Get all known nicknames for the given full name.
     * 
     * @param fullname Full name for which to get all nicknames.
     * @return List of all known nicknames.
     * @see #getNicknameFor(String)
     */
    public List<String> getNicknamesFor(String fullname) {
        return getNicknames().get(fullname);
    }

    /**
     * Get one randomly selected nickname for the given full name.
     * 
     * @param fullname Full name for which to choose a nickname.
     * @return One randomly selected nickname.
     * @see #getNicknamesFor(String)
     */
    public String getNicknameFor(String fullname) {
        return RandomOperations.randSelect(getNicknamesFor(fullname));
    }

    // DATA READING AND PROCESSING METHODS --------------------------------------------------------------------------------------------------------------------

    /**
     * Read the given names file ({@code FilePaths.GIVEN_NAMES}) into
     * {@code givenNamesDistribution}.
     */
    private boolean readGivenNamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.GIVEN_NAMES);
        givenNamesDistribution = processNamesStructure(json);
        return true;
    }

    /**
     * Read the family names file ({@code FilePaths.FAMILY_NAMES}) into
     * {@code familyNamesDistribution}.
     */
    private boolean readFamilyNamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.FAMILY_NAMES);
        familyNamesDistribution = processNamesStructure(json);
        return true;
    }

    /**
     * Read the nicknames file ({@code FilePaths.NICKNAMES}) into {@code nicknames}
     */
    private boolean readNicknamesFile() {
        JSONObject json = JSONProcessor.processJson(FilePaths.NICKNAMES);
        nicknames = new HashMap<>();
        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry))
                continue;

            String key = entry.getKey();
            List<?> value = entry.getAsList();
            List<String> nnames = new ArrayList<>();

            for (Object nickname : value) {
                nnames.add((String) nickname);
            }
            nicknames.put(key,nnames);
        }
        return true;
    }

    /**
     * Process the structure of a names file.
     * 
     * @param json The JSON data to parse for names distribution.
     * @return Mapping of bloc sets to weighted names.
     * @see #processNamesStructure(JSONObject, Set)
     */
    private Map<Set<Bloc>, Map<String, Double>> processNamesStructure(JSONObject json) {
        return processNamesStructure(json,new HashSet<>());
    }

    /**
     * Process the structure of a names file. Recurses when a subobject exists in
     * the json.
     * 
     * @param json         The JSON data to parse for names distribution.
     * @param currentBlocs Set of blocs to use when recursing.
     * @return Mapping of bloc sets to weighted names.
     * @see #processNamesStructure(JSONObject)
     */
    private Map<Set<Bloc>, Map<String, Double>> processNamesStructure(JSONObject json,Set<Bloc> currentBlocs) {
        Map<Set<Bloc>, Map<String, Double>> distributions = new HashMap<>();

        for (Object obj : json.getAsList()) {
            if (!(obj instanceof JSONObject entry))
                continue;

            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Number) {
                // This is a name-number pair
                // Add the name and value to the distributions with the current blocs keyset
                Map<String, Double> d = distributions.get(currentBlocs);
                if (d == null)
                    d = new HashMap<>();
                d.put(key,((Number) value).doubleValue());
                distributions.put(currentBlocs,d);
            } else if (value instanceof List<?>) {
                // This is a nested structure
                // If key is valid bloc, add it to a new bloc set
                Bloc bloc = ENGINE.DemographicsManager().matchBlocName(key);
                Set<Bloc> updatedBlocs = new HashSet<>(currentBlocs);
                if (bloc != null) {
                    updatedBlocs.add(bloc);
                }
                // Recurse with updated bloc set
                Map<Set<Bloc>, Map<String, Double>> subDistr = processNamesStructure(entry,updatedBlocs);
                for (Set<Bloc> k : subDistr.keySet()) {
                    Map<String, Double> v = subDistr.get(k);
                    distributions.put(k,v);
                }
            }
        }
        return distributions;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Manager fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
}
