package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.presidency.citizens.attributes.Family;
import com.stevenlagoy.presidency.citizens.attributes.experiences.PersonalExperienceHistory;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.CompletionMethodFor;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static com.stevenlagoy.presidency.citizens.attributes.names.PersonalNameFactory.nicknameNotFromGivenNamesPercent;
import static com.stevenlagoy.presidency.citizens.attributes.names.PersonalNameFactory.nicknamePercent;

public class WesternPersonalNameFactory extends EntityFactory<WesternPersonalName, WesternPersonalNameFactory.WesternPersonalNameContext> {

    // Constants
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
    /** Percentage of people who commonly abbreviate their first name(s). */
    public static final float abbreviateFirstNamesPercent = 0.06f;
    /** Percentage of people who commonly abbreviate their middle name(s). */
    public static final float abbreviateMiddleNamesPercent = 0.38f;
    /** Percentage of people who commonly abbreviate all their given names. */
    public static final float abbreviateBothNamesPercent = 0.08f;

    public static class WesternPersonalNameContext extends PersonalNameFactory.PersonalNameContext<WesternPersonalName> {

        protected @Nullable String nickname;
        protected static final String nicknameNullSentinel = "";

        public WesternPersonalNameContext(
            @NotNull Engine engine,
            @NotNull Demographics demographics,
            @NotNull Integer age,
            @NotNull Family family,
            @NotNull PersonalExperienceHistory experiences,
            @Nullable String honorific,
            @Nullable List<String> suffixes
        ) {
            super(engine, demographics, age, family, experiences, WesternPersonalName.class, honorific, suffixes);
        }

        public WesternPersonalNameContext(@NotNull Engine engine, @NotNull PersonalNameFactory.PersonalNameContext<PersonalName> superContext) {
            this(engine, superContext.demographics, superContext.age, superContext.family, superContext.experiences, superContext.honorific, superContext.suffixes);
        }

        @Override
        @CompletionMethodFor("nameForm")
        protected @Nullable Class<? extends PersonalName> selectNameForm() {
            return WesternPersonalName.class;
        }

        @CompletionMethodFor("nickname")
        protected @Nullable String selectNickname() {
            String nickname = this.nickname;
            // Determine whether to have a nickname
            if (!RandomUtils.chance(nicknamePercent)) {
                return nicknameNullSentinel;
            }
            // Determine if nickname is not based on given names
            if (RandomUtils.chance(nicknameNotFromGivenNamesPercent)) {
                // Choose from all nicknames for a person with this presentation
                Set<String> possibleNicknames = engine.getManager(PersonalNameManager.class).getNicknamesForPresentation(demographics.getPresentation());
                if (!possibleNicknames.isEmpty()) {
                    nickname = RandomUtils.randSelect(possibleNicknames);
                }
            } // Nickname should come from given names



            return nickname != null ? nickname : nicknameNullSentinel;
        }
    }

    public WesternPersonalNameFactory(@NotNull Engine engine, @NotNull PersonalNameManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable WesternPersonalName doCreation(@NotNull WesternPersonalNameFactory.WesternPersonalNameContext context) {
        return null;
    }

    @Override
    protected @Nullable WesternPersonalName doSelection(@NotNull WesternPersonalNameFactory.WesternPersonalNameContext context) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Selection is invalid for type " + WesternPersonalName.class.getSimpleName());
    }

//    @Override
//    public @NotNull WesternPersonalName build(@NotNull PersonalNameFactory.NameContext context) {
//        WesternPersonalName name = new WesternPersonalName();
//        fillFirstName(name, context);
//        fillMiddleName(name, context);
//        fillLastName(name, context);
//        personalNameFactory.fillNickname(name, context);
//        fillOrdinal(name, context);
//        return name;
//    }
//
//    private void fillFirstName(@NotNull WesternPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
//        // Choose number of first names
//        int numberFirstNames = 1 + RandomUtils.probabilisticCount(multipleFirstNamesPercent);
//        // Choose appropriate names
//        String firstName = personalNameFactory.selectDistinctGivenNames(context, numberFirstNames);
//        // Set
//        name.setFirstName(firstName);
//        // Determine whether to abbreviate first name
//        if (RandomUtils.chance(abbreviateFirstNamesPercent)) {
//            name.getDisplayOptions().add(PersonalName.DisplayOption.ABBREVIATE_FIRST);
//        }
//        else if (RandomUtils.chance(abbreviateBothNamesPercent)) {
//            name.getDisplayOptions().add(PersonalName.DisplayOption.ABBREVIATE_FIRST);
//            name.getDisplayOptions().add(PersonalName.DisplayOption.ABBREVIATE_MIDDLE);
//        }
//    }
//
//    private void fillMiddleName(@NotNull WesternPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
//        // Determine whether to have a middle name
//        if (!RandomUtils.chance(middleNamePercent)) {
//            // If there are multiple first names, use the last one as middle name
//            String[] firstNames = name.getFirstName().split("\\s+");
//            if (firstNames.length > 1) {
//                // Reconstitute first name
//                String firstName = Arrays.stream(firstNames).limit(firstNames.length - 2).collect(Collectors.joining(" ")).strip();
//                // Grab last of the first names as middle name
//                String middleName = firstNames[firstNames.length - 1].strip();
//                // Set
//                name.setFirstName(firstName);
//                name.setMiddleName(middleName);
//                return;
//            }
//            // Otherwise set middle name to null
//            name.setMiddleName(null);
//            return;
//        }
//        // Choose number of middle names
//        int numberMiddleNames = RandomUtils.probabilisticCount(multipleMiddleNamesPercent);
//        // Choose appropriate names
//        String middleName = personalNameFactory.selectDistinctGivenNames(context, numberMiddleNames);
//        // Set
//        name.setMiddleName(middleName);
//        // Determine whether to abbreviate middle name
//        if (RandomUtils.chance(abbreviateMiddleNamesPercent)) {
//            name.getDisplayOptions().add(PersonalName.DisplayOption.ABBREVIATE_MIDDLE);
//        }
//        // Determine whether to prefer middle name
//        if (RandomUtils.chance(preferMiddleNamePercent)) {
//            name.getDisplayOptions().add(PersonalName.DisplayOption.PREFER_MIDDLE);
//        }
//        // Determine whether to include middle name in common name
//        if (RandomUtils.chance(includeMiddleNameInCommonPercent)) {
//            name.getDisplayOptions().add(PersonalName.DisplayOption.INCLUDE_MIDDLE);
//        }
//    }
//
//    private void fillLastName(@NotNull WesternPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
//        // Determine how many last names to have
//        int numberLastNames = 1 + RandomUtils.probabilisticCount(doubleBarrelledSurnamePercent);
//        // Select conjoiner pattern
//        String[] conjoiners = new String[numberLastNames];
//        for (int i = 0; i < numberLastNames; i++) {
//            conjoiners[i] = RandomUtils.chance(hyphenatedSurnamePercent) ? "-" : " ";
//        }
//        // Choose appropriate names
//        String lastName = personalNameFactory.selectDistinctFamilyNames(context, numberLastNames, conjoiners);
//        // Set
//        name.setLastName(lastName);
//    }
//
//    private void fillOrdinal(@NotNull WesternPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
//        if (RandomUtils.chance(srOrdinationPercent)) {
//            name.setOrdinal("Sr.");
//        }
//        else if (RandomUtils.chance(jrOrdinationPercent)) {
//            name.setOrdinal("Jr.");
//        }
//        else if (RandomUtils.chance(iOrdinationPercent)) {
//            name.setOrdinal("I");
//        }
//        else if (RandomUtils.chance(iiOrdinationPercent)) {
//            name.setOrdinal("II");
//        }
//        else if (RandomUtils.chance(iiiOrdinationPercent)) {
//            name.setOrdinal("III");
//        }
//    }

    //    // Selection
    //
    //    protected String selectGivenName(@NotNull PersonalNameFactory.NameContext context) {
    //        return RandomUtils.weightedSelect(engine.getManager(PersonalNameManager.class).getGivenNamesDistribution(context));
    //    }
    //
    //    protected @NotNull String selectDistinctGivenNames(@NotNull PersonalNameFactory.NameContext context, int n) {
    //        StringBuilder nameBuilder = new StringBuilder();
    //        for (int i = 0; i < n; i++) {
    //            String selected;
    //            do {
    //                selected = selectGivenName(context);
    //            } while (nameBuilder.toString().contains(selected));
    //            nameBuilder.append(selected).append(" ");
    //        }
    //        return nameBuilder.toString().strip();
    //    }
    //
    //    protected String selectFamilyName(@NotNull PersonalNameFactory.NameContext context) {
    //        return RandomUtils.weightedSelect(engine.getManager(PersonalNameManager.class).getFamilyNamesDistribution(context));
    //    }
    //
    //    protected @NotNull String selectDistinctFamilyNames(@NotNull PersonalNameFactory.NameContext context, int n, String... conjoinerPattern) {
    //        StringBuilder nameBuilder = new StringBuilder();
    //        Iterator<String> conjoinerStream = Arrays.stream(conjoinerPattern).iterator();
    //        for (int i = 0; i < n; i++) {
    //            String selected;
    //            do {
    //                selected = selectFamilyName(context);
    //            } while (nameBuilder.toString().contains(selected));
    //            nameBuilder.append(selected);
    //            try {
    //                nameBuilder.append(conjoinerStream.next());
    //            } catch (NoSuchElementException e) { // Ran out - fallback with first conjoiner
    //                nameBuilder.append(conjoinerPattern[0]);
    //            }
    //        }
    //        return nameBuilder.toString().strip();
    //    }
    //
}
