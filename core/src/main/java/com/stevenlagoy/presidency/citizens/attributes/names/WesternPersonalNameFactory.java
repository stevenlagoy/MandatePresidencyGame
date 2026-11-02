package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.presidency.citizens.Citizen;
import com.stevenlagoy.presidency.citizens.attributes.Family;
import com.stevenlagoy.presidency.citizens.attributes.experiences.PersonalExperienceHistory;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.CompletionMethodFor;
import com.stevenlagoy.presidency.util.RandomUtils;
import com.stevenlagoy.presidency.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

import static com.stevenlagoy.presidency.citizens.attributes.names.PersonalNameFactory.*;

public class WesternPersonalNameFactory extends EntityFactory<WesternPersonalName, WesternPersonalNameFactory.WesternPersonalNameContext> {

    // Constants
    /** Percentage of people with multiple first names. Percentage for three first names is this value squared, and so on. */
    public static final float multipleFirstNamesPercent = 0.01f;
    /** Percentage of people who have one or more middle names. */
    public static final float middleNamePercent = 0.96f;
    /** Percentage of people who prefer their middle name over first name. */
    public static final float preferMiddleNamePercent = 0.10f;
    /** Percentage of people with multiple middle names. Percentage for three middle names is this value squared, and so on. */
    public static final float multipleMiddleNamesPercent = 0.05f;
    public static final float includeNicknamePercent = 0.60f;
    public static final float includeOrdinalPercent = 0.50f;
    /** Percentage of people who include their middle name in their common name. */
    public static final float includeMiddleNameInCommonPercent = 0.15f;
    /** Percentage of people who have a double-barreled surname or multiple surnames. */
    public static final float doubleBarrelledSurnamePercent = 0.055f;
    /** Percentage of people with multiple surnames who hyphenate one or more of those surnames. */
    public static final float hyphenatedSurnamePercent = 0.65f;
    /** Percentage of people with the Senior ordination. */
    public static final float srOrdinationPercent = 0.04f;
    public static final String srOrdination = "Sr.";
    /** Percentage of people with the Junior ordination. */
    public static final float jrOrdinationPercent = 0.08f;
    public static final String jrOrdination = "Jr.";
    public static final String ordinationTally = "I";
    /** Percentage of people with the I ordination. */
    public static final float iOrdinationPercent = 0.005f;
    /** Percentage of people with the II ordination. */
    public static final float iiOrdinationPercent = 0.025f;
    /** Percentage of people with the III ordination. */
    public static final float iiiOrdinationPercent = 0.02f;
    /** Percentage of people who commonly abbreviate their first name(s). */
    public static final float abbreviateFirstNamesPercent = 0.06f;
    /** Percentage of people who commonly abbreviate their middle name(s). */
    public static final float abbreviateMiddleNamesPercent = 0.38f;
    /** Percentage of people who commonly abbreviate all their given names. */
    public static final float abbreviateBothNamesPercent = 0.08f;

    /** Percentage of people who have the first name of an ancestor (parent, grandparent, great-grandparent, etc.). */
    public static final float firstNameFromAncestorPercent = 0.27f;
    /**
     * Probability that a name inherited from an ancestor comes from parent, grandparent, etc. with each
     * step being this likely compared to the previous step (parent = 50%, grandparent = 25%, great-grandparent
     * = 0.125%). Note that if not enough ancestors are present, the most distant possibility is chosen.
     * Following the maternal or paternal branch at each step have equal probability.
     */
    public static final float ancestorNamingStepBackProbability = 0.50f;
    public static final float middleNameFromAncestorPercent = 0.27f;
    /** Percentage of people whose first and middle names come from an ancestor, but from two different ancestors. */
    public static final float twoAncestorsUsedForNamesPercent = 0.25f;
    /** Percentage of people with a father who receive their father's last name. */
    public static final float inheritFathersLastNamePercent = 0.98f;
    public static final float womanTakeSpousesSurnameAfterMarriagePercent = 0.80f;
    public static final float manTakeSpousesSurnameAfterMarriagePercent = 0.05f;
    public static final float straightHyphenatedSurnameAfterMarriagePercent = 0.05f;
    public static final float nonbinaryHyphenateNamesPercent = 0.175f;
    public static final float nonbinaryTakeSpousesSurnameAfterMarriagePercent = 0.075f;
    public static final float gayHyphenateNamesPercent = 0.175f;
    public static final float gayTakeSpousesSurnameAfterMarriagePercent = 0.075f;

    public static class WesternPersonalNameContext extends PersonalNameFactory.PersonalNameContext<WesternPersonalName> {

        protected @Nullable String nickname;
        protected static final String nicknameNullSentinel = "";
        protected @Nullable String firstName;
        protected @Nullable String middleName;
        protected static final String middleNameNullSentinel = "";
        protected @Nullable String lastName;
        protected @Nullable String ordinal;
        protected static final String ordinalNullSentinel = "";
        protected @Nullable String birthSurname;
        public static final String birthSurnameNullSentinel = "";
        private @Nullable Citizen namedAncestor;

        public WesternPersonalNameContext(
            @NotNull Engine engine,
            @NotNull Demographics demographics,
            @NotNull Integer age,
            @NotNull Family family,
            @NotNull PersonalExperienceHistory experiences,
            @Nullable String honorific,
            @Nullable List<String> suffixes,
            @Nullable Set<PersonalName.DisplayOption> displayOptions,
            @Nullable String nickname,
            @Nullable String firstName,
            @Nullable String middleName,
            @Nullable String lastName,
            @Nullable String ordinal,
            @Nullable Citizen namedAncestor
        ) {
            super(engine,
                demographics,
                age,
                family,
                experiences,
                WesternPersonalName.class,
                honorific,
                suffixes,
                displayOptions
            );
            this.nickname = nickname;
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.ordinal = ordinal;
            this.namedAncestor = namedAncestor;
        }

        public WesternPersonalNameContext(
            @NotNull Engine engine,
            @NotNull Demographics demographics,
            @NotNull Integer age,
            @NotNull Family family,
            @NotNull PersonalExperienceHistory experiences
        ) {
            super(engine, demographics, age, family, experiences, null, null, null, null);
        }

        public WesternPersonalNameContext(@NotNull Engine engine, @NotNull PersonalNameFactory.PersonalNameContext<? extends PersonalName> superContext) {
            this(
                engine,
                superContext.demographics,
                superContext.age,
                superContext.family,
                superContext.experiences,
                superContext.honorific,
                superContext.suffixes,
                superContext.displayOptions,
                null,
                null,
                null,
                null,
                null,
                null
            );
        }

        @Override
        @CompletionMethodFor("nameForm")
        protected @Nullable Class<? extends PersonalName> selectNameForm() {
            return WesternPersonalName.class;
        }

        @CompletionMethodFor("nickname")
        protected @Nullable String selectNickname() {
            if (firstName == null || middleName == null) return null; // Unable to impute
            // Determine whether to have a nickname
            if (!RandomUtils.chance(nicknamePercent)) {
                nickname = nicknameNullSentinel;
                return nickname;
            }
            PersonalNameManager manager = engine.getManager(PersonalNameManager.class);
            // Determine if nickname is not based on given names
            if (RandomUtils.chance(nicknameNotFromGivenNamesPercent)) {
                // Choose from all nicknames for a person with this presentation
                Set<String> possibleNicknames = manager.getNicknamesForPresentation(demographics.getPresentation());
                if (!possibleNicknames.isEmpty()) {
                    nickname = RandomUtils.randSelect(possibleNicknames);
                    return nickname;
                }
            } // Nickname should come from given names
            for (String givenName : (firstName + " " + middleName).split("\\s+")) {
                Set<String> nicks = manager.getNicknames().getOrDefault(givenName, Set.of());
                if (nicks.isEmpty()) continue;
                nickname = RandomUtils.randSelect(nicks);
                return nickname;
            }
            return nickname;
        }

        @CompletionMethodFor("firstName")
        protected @Nullable String selectFirstName() {
            if (firstName != null) return firstName;
            // Determine if we should select a name from an ancestor
            if (RandomUtils.chance(firstNameFromAncestorPercent)) {
                if (RandomUtils.chance(twoAncestorsUsedForNamesPercent)) namedAncestor = null;
                selectNamedAncestor();
                if (namedAncestor != null && namedAncestor.getName().getClass().equals(WesternPersonalName.class)) {
                    firstName = ((WesternPersonalName) namedAncestor.getName()).getFirstName();
                }
            } // Not named for an ancestor
            // Select a new first name
            if (firstName == null) firstName = selectGivenNames(multipleFirstNamesPercent);
            return firstName;
        }

        @CompletionMethodFor("middleName")
        protected @Nullable String selectMiddleName() {
            if (middleName != null || firstName == null) return middleName;
            // Determine if we should select a name from an ancestor
            if (RandomUtils.chance(middleNameFromAncestorPercent)) {
                if (RandomUtils.chance(twoAncestorsUsedForNamesPercent)) namedAncestor = null;
                selectNamedAncestor();
                if (namedAncestor != null && namedAncestor.getName().getClass().equals(WesternPersonalName.class)) {
                    middleName = ((WesternPersonalName) namedAncestor.getName()).getMiddleName();
                }
            } // Not named for an ancestor
            // Determine whether to have a middle name
            else if (!RandomUtils.chance(middleNamePercent)) { // No middle name
                // People may have multiple first names and no middle name
                middleName = middleNameNullSentinel;
            }
            else {
                middleName = selectGivenNames(multipleMiddleNamesPercent);
            }
            if (middleName == null) middleName = middleNameNullSentinel;
            return middleName;
        }

        private @NotNull String selectGivenNames(float multipleNamesProbability) {
            // Choose number of names
            int numberNames = 1 + RandomUtils.probabilisticCount(multipleNamesProbability);
            // Choose appropriate names
            PersonalNameManager manager = engine.getManager(PersonalNameManager.class);
            Collection<String> names = RandomUtils.weightedSelectN(numberNames, manager.getGivenNamesDistribution(this));
            return String.join(" ", names);
        }

        @CompletionMethodFor("lastName")
        protected @Nullable String selectLastName() {
            if (birthSurname == null) return lastName;
            if (lastName != null) return lastName;
            lastName = "";
            // If married, determine whether to take spouse's last name
            Citizen spouse = family.getSpouse();
            if (spouse != null) {
                byte presentationFlags = getPresentationFlags(spouse);
                boolean hyphenate = false, takeSpouseName = false, spouseTakeMyName = false;
                switch (presentationFlags) {
                    case 0:  // NB x NB
                        if (RandomUtils.chance(nonbinaryHyphenateNamesPercent)) hyphenate = true;
                        else if (RandomUtils.chance(nonbinaryTakeSpousesSurnameAfterMarriagePercent)) { // I take
                            takeSpouseName = true;
                        }
                        else spouseTakeMyName = RandomUtils.chance(nonbinaryTakeSpousesSurnameAfterMarriagePercent);
                        break;
                    case 3:  // M x M
                    case 12: // W x W
                        if (RandomUtils.chance(gayHyphenateNamesPercent)) hyphenate = true;
                        else if (RandomUtils.chance(gayTakeSpousesSurnameAfterMarriagePercent)) { // I take
                            takeSpouseName = true;
                        }
                        else spouseTakeMyName = RandomUtils.chance(gayTakeSpousesSurnameAfterMarriagePercent);
                        break;
                    case 1:  // M x NB -> 2
                        if (RandomUtils.chance(straightHyphenatedSurnameAfterMarriagePercent)) hyphenate = true;
                        else if (RandomUtils.chance(manTakeSpousesSurnameAfterMarriagePercent)) {
                            takeSpouseName = true;
                        }
                        else spouseTakeMyName = RandomUtils.chance(nonbinaryTakeSpousesSurnameAfterMarriagePercent);
                        break;
                    case 2:  // NB x M -> 1
                        if (RandomUtils.chance(straightHyphenatedSurnameAfterMarriagePercent)) hyphenate = true;
                        else if (RandomUtils.chance(nonbinaryTakeSpousesSurnameAfterMarriagePercent)) {
                            takeSpouseName = true;
                        }
                        else spouseTakeMyName = RandomUtils.chance(manTakeSpousesSurnameAfterMarriagePercent);
                        break;
                    case 4:  // W x NB -> 8
                    case 8:  // NB x W -> 4
                        if (RandomUtils.chance(gayHyphenateNamesPercent)) hyphenate = true;
                        else if (RandomUtils.chance(nonbinaryTakeSpousesSurnameAfterMarriagePercent)) {
                            takeSpouseName = true;
                        }
                        else spouseTakeMyName = RandomUtils.chance(nonbinaryTakeSpousesSurnameAfterMarriagePercent);
                        break;
                    case 6:  // W x M -> 9
                        if (RandomUtils.chance(straightHyphenatedSurnameAfterMarriagePercent)) hyphenate = true;
                        else if (RandomUtils.chance(womanTakeSpousesSurnameAfterMarriagePercent)) {
                            takeSpouseName = true;
                        }
                        else spouseTakeMyName = RandomUtils.chance(manTakeSpousesSurnameAfterMarriagePercent);
                    case 9:  // M x W -> 6
                        if (RandomUtils.chance(straightHyphenatedSurnameAfterMarriagePercent)) hyphenate = true;
                        else if (RandomUtils.chance(manTakeSpousesSurnameAfterMarriagePercent)) {
                            takeSpouseName = true;
                        }
                        else spouseTakeMyName = RandomUtils.chance(womanTakeSpousesSurnameAfterMarriagePercent);
                }
                String spouseLastName = spouse.getName().getPreferredFamily();
                if (hyphenate && spouse.getName().getClass().equals(WesternPersonalName.class)) {
                    String conjoiner = RandomUtils.chance(hyphenatedSurnamePercent) ? "-" : " ";
                    String sharedLastName = birthSurname + conjoiner + spouseLastName;
                    lastName = sharedLastName;
                    ((WesternPersonalName) spouse.getName()).setBirthSurname(spouseLastName);
                    ((WesternPersonalName) spouse.getName()).setLastName(sharedLastName);
                }
                else if (takeSpouseName) {
                    lastName = spouseLastName;
                }
                else if (spouseTakeMyName && spouse.getName().getClass().equals(WesternPersonalName.class)) {
                    ((WesternPersonalName) spouse.getName()).setBirthSurname(spouseLastName);
                    ((WesternPersonalName) spouse.getName()).setLastName(birthSurname);
                }
            }
            if (lastName.isEmpty()) {
                lastName = birthSurname;
                birthSurname = birthSurnameNullSentinel;
            }
            return lastName;
        }

        private byte getPresentationFlags(@NotNull Citizen spouse) {
            byte presentationFlags = 0;
            String ownPresentation = demographics.getPresentation().getName();
            String spousePresentation = spouse.getDemographics().getPresentation().getName();
            if (ownPresentation.equals("Man")) presentationFlags += 1;
            if (spousePresentation.equals("Man")) presentationFlags += 2;
            if (ownPresentation.equals("Woman")) presentationFlags += 4;
            if (spousePresentation.equals("Woman")) presentationFlags += 8;
            return presentationFlags;
        }

        @CompletionMethodFor("ordinal")
        protected @Nullable String selectOrdinal() {
            if (firstName == null || middleName == null) return null; // Unable to impute
            // Only non-women have ordinals, and only when named for an ancestor with a Western-style name
            if (
                namedAncestor == null ||
                demographics.getPresentation().getName().equals("Woman") ||
                !(namedAncestor.getName() instanceof WesternPersonalName ancestorName)
            ) {
                ordinal = ordinalNullSentinel;
                return ordinal;
            }
            String namedAncestorOrdinal = ancestorName.getOrdinal();
            boolean ancestorHasOrdinal = namedAncestorOrdinal != null && !namedAncestorOrdinal.isBlank();
            if (!ancestorHasOrdinal) {
                ordinal = namedAncestorOrdinal.equals(family.getFather()) ? jrOrdination : ordinationTally.repeat(2);
            }
            else if (namedAncestorOrdinal.equals(jrOrdination)) {
                ordinal = ordinationTally.repeat(3);
            }
            else if (namedAncestorOrdinal.contains(ordinationTally)) {
                ordinal = ordinationTally.repeat(
                    StringUtils.stringCountMatches(namedAncestorOrdinal, ordinationTally) + 1
                );
            }
            else ordinal = ordinalNullSentinel;
            return ordinal;
        }

        @CompletionMethodFor("birthSurname")
        protected @Nullable String selectBirthSurname() {
            if (birthSurname != null) return birthSurname;
            birthSurname = "";
            // Choose number of names
            int numberNames = 1 + RandomUtils.probabilisticCount(doubleBarrelledSurnamePercent);
            String conjoiner = RandomUtils.chance(hyphenatedSurnamePercent) ? "-" : " ";
            if (family.getFather() != null && RandomUtils.chance(inheritFathersLastNamePercent)) {
                if (!birthSurname.isEmpty()) birthSurname += conjoiner;
                birthSurname += family.getFather().getName().getPreferredFamily();
                numberNames--;
            }
            else if (family.getMother() != null && numberNames >= 1) {
                if (!birthSurname.isEmpty()) birthSurname += conjoiner;
                birthSurname += family.getMother().getName().getPreferredFamily();
                numberNames--;
            }
            if (!birthSurname.isBlank()) return birthSurname;

            // Create a new last name
            // Select conjoiner pattern
            String[] conjoiners = new String[numberNames-1];
            for (int i = 0; i < numberNames-1; i++) {
                conjoiners[i] = RandomUtils.chance(hyphenatedSurnamePercent) ? "-" : " ";
            }
            // Choose appropriate names
            List<String> names = RandomUtils.weightedSelectN(numberNames, engine
                .getManager(PersonalNameManager.class)
                .getFamilyNamesDistribution(this)
            ).stream().toList();
            // Create last name
            StringBuilder familyName = new StringBuilder();
            familyName.append(names.get(0));
            for (int i = 1; i < names.size(); i++) {
                familyName.append(conjoiners[i-1]).append(names.get(i));
            }
            birthSurname = familyName.toString();
            return birthSurname;
        }

        @Override
        protected @NotNull Map<PersonalName.DisplayOption, Float> getDisplayOptionsPercentages() {
            return Map.of(
                PersonalName.DisplayOption.ABBREVIATE_FIRST, abbreviateFirstNamesPercent + abbreviateBothNamesPercent,
                PersonalName.DisplayOption.ABBREVIATE_MIDDLE, abbreviateMiddleNamesPercent + abbreviateBothNamesPercent,
                PersonalName.DisplayOption.PREFER_MIDDLE, preferMiddleNamePercent,
                PersonalName.DisplayOption.INCLUDE_NICKNAME, includeNicknamePercent,
                PersonalName.DisplayOption.INCLUDE_ORDINAL, includeOrdinalPercent,
                PersonalName.DisplayOption.INCLUDE_HONORIFIC, includeHonorificPercent,
                PersonalName.DisplayOption.INCLUDE_SUFFIX, includeSuffixPercent
            );
        }

        // Not a completion method, because this is not used for construction of a name itself
        private void selectNamedAncestor() {
            if (namedAncestor != null) return;
            // Determine how many steps back to take
            int stepsBack = RandomUtils.probabilisticCount(ancestorNamingStepBackProbability);
            // Find an ancestor that many steps back
            namedAncestor = family.getMother() != null && RandomUtils.randNextBoolean() ?
                family.getMother() :
                family.getFather();
            Citizen prevAncestor;
            int stepsBackTaken = 1;
            while (stepsBackTaken < stepsBack && namedAncestor != null) {
                prevAncestor = namedAncestor;
                namedAncestor = namedAncestor.getFamily().getMother() != null && RandomUtils.randNextBoolean() ?
                    namedAncestor.getFamily().getMother() :
                    namedAncestor.getFamily().getFather();
                if (namedAncestor == null) namedAncestor = prevAncestor;
                stepsBackTaken++;
            }
            if (namedAncestor == null) { // Unable to find an ancestor
                return;
            } // We found an ancestor
            Function<Citizen, Boolean> matches = a ->
                a.getDemographics().getPresentation().equals(demographics.getPresentation()) &&
                a.getName().getClass().equals(WesternPersonalName.class);
            if (!matches.apply(namedAncestor)) {
                // Try to find a sibling, child, spouse, or parent with the correct presentation.
                // Prefer siblings and children first because that means people may sometimes be named
                // for an aunt, great uncle, etc. rather than always for a direct ancestor.
                List<Citizen> possibleAncestors = new ArrayList<>();
                possibleAncestors.addAll(namedAncestor.getFamily().getSiblings());
                possibleAncestors.addAll(namedAncestor.getFamily().getChildren());
                if (namedAncestor.getFamily().getSpouse() != null)
                    possibleAncestors.add(namedAncestor.getFamily().getSpouse());
                possibleAncestors.addAll(namedAncestor.getFamily().getParents());
                namedAncestor = possibleAncestors.stream().filter(matches::apply).findFirst().orElse(null);
            }
        }

        @Override
        public void desentinelize() {
            super.desentinelize();
            nickname = desentinelize(nickname, nicknameNullSentinel);
            middleName = desentinelize(middleName, middleNameNullSentinel);
            ordinal = desentinelize(ordinal, ordinalNullSentinel);
            birthSurname = desentinelize(birthSurname, birthSurnameNullSentinel);
        }
    }

    public WesternPersonalNameFactory(@NotNull Engine engine, @NotNull PersonalNameManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable WesternPersonalName doCreation(@NotNull WesternPersonalNameFactory.WesternPersonalNameContext context) {
        assert (
            context.firstName != null &&
            context.lastName != null &&
            context.suffixes != null &&
            context.displayOptions != null
        );
        return new WesternPersonalName(
            context.firstName,
            context.middleName,
            context.lastName,
            context.ordinal,
            context.birthSurname,
            context.honorific,
            context.nickname,
            context.suffixes,
            context.displayOptions
        );
    }

    @Override
    protected @Nullable WesternPersonalName doSelection(@NotNull WesternPersonalNameFactory.WesternPersonalNameContext context) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Selection is invalid for type " + WesternPersonalName.class.getSimpleName());
    }
}
