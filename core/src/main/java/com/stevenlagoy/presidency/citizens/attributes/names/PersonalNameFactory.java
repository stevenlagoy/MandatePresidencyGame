package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.presidency.citizens.Citizen;
import com.stevenlagoy.presidency.citizens.attributes.Education;
import com.stevenlagoy.presidency.citizens.attributes.Family;
import com.stevenlagoy.presidency.citizens.attributes.experiences.PersonalExperience;
import com.stevenlagoy.presidency.citizens.attributes.experiences.PersonalExperienceHistory;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.CompletionMethodFor;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonalNameFactory extends EntityFactory<PersonalName, PersonalNameFactory.PersonalNameContext<PersonalName>> {

    // Constants

    /** Percentage of people with a name form matching that of one of their siblings, among people with siblings. */
    public static final float nameFormMatchesSiblingPercent = 0.90f;
    /** Percentage of people with a name form matching that of one of their parents, among people with parents. */
    public static final float nameFormMatchesParentPercent = 0.75f;

    /** Percentage of Native people who have a Native-style name. */
    public static final float nativeNativeNamePercent = 0.25f;
    /** Percentage of Hispanic people who have a Hispanic-style name. */
    public static final float hispanicHispanicNamePercent = 0.80f;
    /** Percentage of Asian people who have an Eastern-style name. */
    public static final float asianEasternNamePercent = 0.30f;
    /** Percentage of people with a doctorate who use the Dr. honorific. */
    public static final float doctorateDrHonorificPercent = 0.80f;
    /** Percentage of medical doctors who use the Dr. honorific. */
    public static final float medicalDoctorDrHonorificPercent = 1.0f;
    public static final String drHonorific = "Dr.";
    public static final String judgeHonorific = "Judge";
    public static final String justiceHonorific = "Justice";
    public static final String chiefJusticeHonorific = "Chief Justice";
    public static final String senatorHonorific = "Senator";
    public static final String representativeHonorific = "Representative";
    /** Percentage of men who use the Mr. honorific. */
    public static final float manMrHonorificPercent = 0.90f;
    public static final String mrHonorific = "Mr.";

    /** Percentage of unmarried women who use the Miss honorific. */
    public static final float unmarriedWomanMissHonorificPercent = 0.30f;
    public static final String missHonorific = "Miss";
    /** Percentage of unmarried women who use the Ms. honorific. */
    public static final float unmarriedWomanMsHonorificPercent = 0.60f;
    /** Percentage of married women who prefer the Ms. honorific. */
    public static final float marriedWomanMsHonorificPercent = 0.25f;
    public static final String msHonorific = "Ms.";
    /** Percentage of married women who prefer the Ms. honorific. */
    public static final float marriedWomanMrsHonorificPercent = 0.65f;
    public static final String mrsHonorific = "Mrs.";
    /** Percentage of non-binary people who use the Mx. honorific. */
    public static final float nonBinaryMxHonorificPercent = 0.10f;
    public static final String mxHonorific = "Mx.";
    /** Percentage of non-binary people who use the Mr. honorific. */
    public static final float nonBinaryMrHonorificPercent = 0.20f;
    /** Percentage of non-binary people who use the Ms. honorific. */
    public static final float nonBinaryMsHonorificPercent = 0.20f;
    /** Percentage of medical doctors with the MD suffix. Others will use M.D. */
    public static final float mdSuffixPercent = 0.50f;
    public static final String mdSuffix = "MD";
    public static final String m_d_Suffix = "M.D.";
    public static final String phdSuffix = "PhD";
    public static final String esqSuffix = "Esq.";
    /** Percentage of people who commonly use a nickname. */
    public static final float nicknamePercent = 0.33f;
    /** Percentage of people with a nickname whose nickname is based on their middle name. */
    public static final float nicknameFromMiddleNamePercent = 0.10f;
    /** Percentage of people with a nickname based on the abbreviations of their names, like Thomas James -> TJ. */
    public static final float nicknameFromAbbreviationPercent = 0.15f;
    /** Percentage controlling length of nicknames based on abbreviations. This is the chance for each additional letter to be included. */
    public static final float nicknameFromAbbreviationLengthPercent = 0.7f;
    /** Percentage of people who commonly use a nickname which is not based on one of their given names. */
    public static final float nicknameNotFromGivenNamesPercent = 0.05f;

    /**
     * Context for creating or selecting a {@link PersonalName}.
     */
    public static class PersonalNameContext<N extends PersonalName> extends EntityFactory.EntityContext<N> {

        protected final @NotNull Demographics demographics;
        protected final @NotNull Integer age;
        protected final @NotNull Family family;
        protected final @NotNull PersonalExperienceHistory experiences;
        protected @Nullable Class<? extends PersonalName> nameForm;
        protected @Nullable String honorific;
        protected static final String honorificNullSentinel = "";
        protected @Nullable List<String> suffixes;

        public PersonalNameContext(
            @NotNull Engine engine,
            @NotNull Demographics demographics,
            @NotNull Integer age,
            @NotNull Family family,
            @NotNull PersonalExperienceHistory experiences,
            @Nullable Class<? extends PersonalName> nameForm,
            @Nullable String honorific,
            @Nullable List<String> suffixes
        ) {
            super(engine);
            this.demographics = demographics;
            this.age = age;
            this.family = family;
            this.experiences = experiences;
            this.nameForm = nameForm;
            this.honorific = honorific;
            this.suffixes = suffixes;
        }

        @Override
        public @NotNull String getImputationFailureConditionsMessage() {
            return "There are no nullable fields required for imputation.";
        }

        @CompletionMethodFor("nameForm")
        protected @Nullable Class<? extends PersonalName> selectNameForm() {
            Class<? extends PersonalName> nameForm = this.nameForm;
            // Try selecting a sibling's name form
            if (nameForm == null && RandomUtils.chance(nameFormMatchesSiblingPercent)) {
                List<Citizen> siblings = new ArrayList<>(family.getSiblings());
                Collections.shuffle(siblings);
                nameForm = siblings
                   .stream().findFirst()
                   .map(sibling -> sibling.getName().getClass())
                   .orElse(null);
            } // No siblings
            // Try selecting a parent's name form
            if (nameForm == null && RandomUtils.chance(nameFormMatchesParentPercent)) {
                List<Citizen> parents = family.getParents();
                Collections.shuffle(parents);
                nameForm = parents
                   .stream().findFirst()
                   .map(parent -> parent.getName().getClass())
                   .orElse(null);
            } // No parents
            // Try selecting based on demographics
            Collection<String> raceEthnicityAncestors = demographics.getRaceEthnicity().getAncestorNames();
            if (
                nameForm == null &&
                raceEthnicityAncestors.contains("Asian") &&
                RandomUtils.chance(asianEasternNamePercent)
            ) nameForm = EasternPersonalName.class;
            if (
                nameForm == null &&
                raceEthnicityAncestors.contains("Hispanic / Latino")
            ) {
                if (raceEthnicityAncestors.contains("Argentinian")) {
                    // Only the parernal apellido is inherited in the Argentinian custom
                    nameForm = WesternPersonalName.class;
                }
                else if (RandomUtils.chance(hispanicHispanicNamePercent)) {
                    nameForm = HispanicPersonalName.class;
                }
            }
            if (
                nameForm == null &&
                raceEthnicityAncestors.contains("Native / Indian") &&
                RandomUtils.chance(nativeNativeNamePercent)
            ) nameForm = WesternPersonalName.class; // TODO Native nameforms
            if (nameForm == null) nameForm = WesternPersonalName.class;

            return nameForm;
        }

        @CompletionMethodFor("honorific")
        protected @Nullable String selectHonorific() {
            String honorific = this.honorific;
            if (honorific != null && !honorific.equals(honorificNullSentinel)) return honorific;

            // These must be determined in reverse order of precedence: more prestigious honorifics should be checked first

            Function<String, Boolean> hasExperienceMatching = name -> experiences
                .stream().anyMatch(experience -> experience.getType()
                .getName().contains(name));

            // Chief Justice honorific
            if (hasExperienceMatching.apply("Chief Justice")) {
                return chiefJusticeHonorific;
            }
            // US Senator honorific
            if (hasExperienceMatching.apply("U.S. Senator")) {
                return senatorHonorific;
            }
            // US Representative honorific
            if (hasExperienceMatching.apply("U.S. Representative")) {
                return representativeHonorific;
            }
            // Justice honorific
            if (hasExperienceMatching.apply("justice")) {
                return justiceHonorific;
            }
            // State Senator honorific
            if (hasExperienceMatching.apply("Senator")) {
                return senatorHonorific;
            }
            // State Representative honorific
            if (hasExperienceMatching.apply("Representative")) {
                return representativeHonorific;
            }
            // Judge honorific
            if (hasExperienceMatching.apply("judge")) {
                return judgeHonorific;
            }
            // Dr. honorific
            Stream<PersonalExperience> doctorExperiences = experiences.stream().filter(
                experience -> experience.isCompleted() && experience.getType().getName().contains("Doctor")
            );
            for (PersonalExperience experience : doctorExperiences.collect(Collectors.toSet())) {
                if (experience.getType().getName().equals("Medical Doctor") && RandomUtils.chance(medicalDoctorDrHonorificPercent)) {
                    return drHonorific;
                }
                if (experience.getType().getName().equals("Juris Doctor")) { // May use Esq. suffix and are not addressed as "Dr."
                    continue;
                }
                if (RandomUtils.chance(doctorateDrHonorificPercent)) {
                    return drHonorific;
                }
            }
            // Ungranted honorifics
            switch (demographics.getPresentation().getName()) {
                case "Man" :
                    if (RandomUtils.chance(manMrHonorificPercent)) return mrHonorific;
                case "Woman" :
                    boolean isMarried = family.getSpouse() != null;
                    if (isMarried) {
                        if (RandomUtils.chance(marriedWomanMrsHonorificPercent)) return mrsHonorific;
                        if (RandomUtils.chance(marriedWomanMsHonorificPercent)) return msHonorific;
                    }
                    else {
                        if (RandomUtils.chance(unmarriedWomanMsHonorificPercent)) return msHonorific;
                        if (RandomUtils.chance(unmarriedWomanMissHonorificPercent)) return missHonorific;
                    }
                    break;
                case "Non-Binary" :
                    if (RandomUtils.chance(nonBinaryMsHonorificPercent)) return msHonorific;
                    if (RandomUtils.chance(nonBinaryMrHonorificPercent)) return mrHonorific;
                    if (RandomUtils.chance(nonBinaryMxHonorificPercent)) return mxHonorific;
                    break;
            }

            return honorific != null ? honorific : honorificNullSentinel;
        }

        @CompletionMethodFor("suffixes")
        protected @Nullable List<String> selectSuffixes() {
            List<String> suffixes = this.suffixes;
            if (suffixes != null || honorific == null) return suffixes;

            // Characters with prestigious honorifics don't use other suffixes
            if (
                Stream.of(chiefJusticeHonorific, senatorHonorific, representativeHonorific, justiceHonorific, judgeHonorific)
                .anyMatch(h -> honorific.equals(h))
            ) {
                return new ArrayList<>();
            }

            suffixes = new ArrayList<>();
            Stream<PersonalExperience> doctorExperiences = experiences.stream().filter(
                experience -> experience.isCompleted() && experience.getType().getName().contains("Doctor")
            );
            for (PersonalExperience experience : doctorExperiences.collect(Collectors.toSet())) {
                if (experience.getType().getName().equals("Medical Doctor")) {
                    if (RandomUtils.chance(mdSuffixPercent)) suffixes.add(mdSuffix);
                    else suffixes.add(m_d_Suffix);
                }
                if (experience.getType().getName().equals("Juris Doctor")) {
                    suffixes.add(esqSuffix);
                }
                if (RandomUtils.chance(doctorateDrHonorificPercent)) {
                    suffixes.add(phdSuffix);
                }
            }
            return suffixes;
        }
    }

    // Subtype Factories

    private final @NotNull WesternPersonalNameFactory westernPersonalNameFactory;
    private final @NotNull EasternPersonalNameFactory easternPersonalNameFactory;
    private final @NotNull HispanicPersonalNameFactory hispanicPersonalNameFactory;

    public PersonalNameFactory(@NotNull Engine engine, @NotNull PersonalNameManager manager) {
        super(engine, manager);
        westernPersonalNameFactory = new WesternPersonalNameFactory(engine, manager);
        easternPersonalNameFactory = new EasternPersonalNameFactory(engine, manager);
        hispanicPersonalNameFactory = new HispanicPersonalNameFactory(engine, manager);
    }

    @Override
    protected @Nullable PersonalName doCreation(@NotNull PersonalNameContext<PersonalName> context) {
        if (context.nameForm == WesternPersonalName.class) {
            WesternPersonalNameFactory.WesternPersonalNameContext childContext = new WesternPersonalNameFactory.WesternPersonalNameContext(engine, context);
            return westernPersonalNameFactory.create(childContext);
        }
        else if (context.nameForm == EasternPersonalName.class) {
            EasternPersonalNameFactory.EasternPersonalNameContext childContext = new EasternPersonalNameFactory.EasternPersonalNameContext(engine, context);
            return easternPersonalNameFactory.doCreation(childContext);
        }
        else if (context.nameForm == HispanicPersonalName.class) {
            HispanicPersonalNameFactory.HispanicPersonalNameContext childContext = new HispanicPersonalNameFactory.HispanicPersonalNameContext(engine, context);
            return hispanicPersonalNameFactory.doCreation(childContext);
        }
        throw new IllegalStateException("Unknown context nameForm: " + context.nameForm);
    }

    @Override
    protected @Nullable PersonalName doSelection(@NotNull PersonalNameContext<PersonalName> context) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Selection is invalid for type " + PersonalName.class.getSimpleName());
    }
}
