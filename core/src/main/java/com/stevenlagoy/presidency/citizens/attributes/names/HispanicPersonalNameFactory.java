package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.presidency.citizens.Citizen;
import com.stevenlagoy.presidency.citizens.attributes.Family;
import com.stevenlagoy.presidency.citizens.attributes.experiences.PersonalExperienceHistory;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.CompletionMethodFor;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

import static com.stevenlagoy.presidency.citizens.attributes.names.PersonalNameFactory.*;
import static com.stevenlagoy.presidency.citizens.attributes.names.PersonalNameFactory.includeSuffixPercent;
import static com.stevenlagoy.presidency.citizens.attributes.names.WesternPersonalNameFactory.*;

public class HispanicPersonalNameFactory extends EntityFactory<HispanicPersonalName, HispanicPersonalNameFactory.HispanicPersonalNameContext> {

    // Constants
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
    public static final float mariaMascGivenNamePercent = 0.25f;
    public static final String maria = "María";
    public static final float mariaJoseFemGivenNamePercent = 0.05f;
    public static final String mariaJose = "María José";
    public static final float maternalFirstPercent = 0.0047f;
    public static final float preferMaternalPercent = 0.125f;
    private static final float preferPaternalAlonePercent = 0.50f;

    public static class HispanicPersonalNameContext extends PersonalNameFactory.PersonalNameContext<HispanicPersonalName> {

        protected @Nullable String nickname;
        protected static final String nicknameNullSentinel = "";
        protected @Nullable String givenName;
        protected @Nullable String paternalName;
        protected @Nullable String maternalName;
        private @Nullable Citizen namedAncestor;

        public HispanicPersonalNameContext(
            @NotNull Engine engine,
            @NotNull Demographics demographics,
            @NotNull Integer age,
            @NotNull Family family,
            @NotNull PersonalExperienceHistory experiences,
            @Nullable String honorific,
            @Nullable List<String> suffixes,
            @Nullable Set<PersonalName.DisplayOption> displayOptions,
            @Nullable String nickname,
            @Nullable String givenName,
            @Nullable String paternalName,
            @Nullable String maternalName
        ) {
            super(engine,
                demographics,
                age,
                family,
                experiences,
                HispanicPersonalName.class,
                honorific,
                suffixes,
                displayOptions
            );
            this.nickname = nickname;
            this.givenName = givenName;
            this.paternalName = paternalName;
            this.maternalName = maternalName;
        }

        public HispanicPersonalNameContext(@NotNull Engine engine, @NotNull PersonalNameFactory.PersonalNameContext<? extends PersonalName> superContext) {
            this(engine,
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
                null
            );
        }

        @Override
        @CompletionMethodFor("nameForm")
        protected @Nullable Class<? extends PersonalName> selectNameForm() {
            return HispanicPersonalName.class;
        }

        @CompletionMethodFor("nickname")
        protected @Nullable String selectNickname() {
            if (givenName == null) return null;
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
            for (String givenName : givenName.split("\\s+")) {
                Set<String> nicks = manager.getNicknames().getOrDefault(givenName, Set.of());
                if (nicks.isEmpty()) continue;
                nickname = RandomUtils.randSelect(nicks);
                return nickname;
            }
            if (nickname == null) nickname = nicknameNullSentinel;
            return nickname;
        }

        @CompletionMethodFor("givenName")
        protected @Nullable String selectGivenName() {
            if (givenName != null) return givenName;
            // Determine if we should select a name from an ancestor
            if (RandomUtils.chance(firstNameFromAncestorPercent)) {
                selectNamedAncestor();
                if (namedAncestor != null && namedAncestor.getName().getClass().equals(HispanicPersonalName.class)) {
                    givenName = ((HispanicPersonalName) namedAncestor.getName()).getGivenName();
                }
            } // Not named for an ancestor
            // Select a new first name
            if (givenName == null) givenName = selectGivenNames(hispanicMultipleForenamesPercent);
            return givenName;
        }

        private @NotNull String selectGivenNames(float multipleNamesProbability) {
            // Choose number of names
            int numberNames = 1 + RandomUtils.probabilisticCount(multipleNamesProbability);
            // Choose appropriate names
            PersonalNameManager manager = engine.getManager(PersonalNameManager.class);
            List<String> names = new ArrayList<>(RandomUtils.weightedSelectN(
                numberNames, manager.getGivenNamesDistribution(this)
            ));
            if (
                numberNames >= 2 &&
                demographics.getPresentation().getName().equals("Man") &&
                RandomUtils.chance(mariaMascGivenNamePercent)
            ) {
                names.set(1, maria);
            }
            else if (
                numberNames >= 2 &&
                demographics.getPresentation().getName().equals("Woman") &&
                RandomUtils.chance(mariaJoseFemGivenNamePercent)
            ) {
                names.set(0, mariaJose);
                names.remove(names.size()-1);
            }
            return String.join(" ", names);
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
                a.getName().getClass().equals(HispanicPersonalName.class);
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

        @CompletionMethodFor("paternalName")
        protected @Nullable String selectPaternalName() {
            if (paternalName != null) return paternalName;
            if (family.getFather() == null) {
                paternalName = selectApellido();
                return paternalName;
            }
            PersonalName fathersName = family.getFather().getName();
            if (fathersName.getClass().equals(HispanicPersonalName.class)) {
                paternalName = ((HispanicPersonalName) fathersName).getPaternalName();
            }
            else paternalName = fathersName.getPreferredFamily();
            return paternalName;
        }

        @CompletionMethodFor("maternalName")
        protected @Nullable String selectMaternalName() {
            if (maternalName != null) return maternalName;
            if (family.getMother() == null) {
                maternalName = selectApellido();
                return maternalName;
            }
            PersonalName mothersName = family.getMother().getName();
            if (mothersName.getClass().equals(HispanicPersonalName.class)) {
                maternalName = ((HispanicPersonalName) mothersName).getPaternalName();
            }
            else maternalName = mothersName.getPreferredFamily();
            return maternalName;
        }

        private @NotNull String selectApellido() {
            // Choose number of family names in the apellido
            int numberNames = 1 + RandomUtils.probabilisticCount(hispanicCompositeSurnamePercent);
            // Choose conjoiners
            Set<String> possibleConjoiners = hispanicCompositeSurnameConjoiners.get("Hispanic");
            for (String blocName : demographics.getRaceEthnicity().getAncestorNames()) {
                if (hispanicCompositeSurnameConjoiners.containsKey(blocName)) {
                    possibleConjoiners = hispanicCompositeSurnameConjoiners.get(blocName);
                    break;
                }
            }
            String[] conjoiners = new String[numberNames-1];
            for (int i = 0; i < numberNames-1; i++) {
                conjoiners[i] = RandomUtils.randSelect(possibleConjoiners);
            }
            // Choose appropriate names
            List<String> names = RandomUtils.weightedSelectN(numberNames, engine
                .getManager(PersonalNameManager.class)
                .getFamilyNamesDistribution(this)
            ).stream().toList();
            // Create apellido
            StringBuilder apellido = new StringBuilder();
            apellido.append(names.get(0));
            for (int i = 1; i < names.size(); i++) {
                apellido.append(conjoiners[i-1]).append(names.get(i));
            }
            return apellido.toString();
        }

        @Override
        protected @NotNull Map<PersonalName.DisplayOption, Float> getDisplayOptionsPercentages() {
            return Map.of(
                PersonalName.DisplayOption.MATERNAL_FIRST, maternalFirstPercent,
                PersonalName.DisplayOption.PREFER_MATERNAL,preferMaternalPercent,
                PersonalName.DisplayOption.PREFER_PATERNAL, preferPaternalAlonePercent,
                PersonalName.DisplayOption.INCLUDE_NICKNAME, includeNicknamePercent,
                PersonalName.DisplayOption.INCLUDE_HONORIFIC, includeHonorificPercent,
                PersonalName.DisplayOption.INCLUDE_SUFFIX, includeSuffixPercent
            );
        }

        @Override
        public void desentinelize() {
            super.desentinelize();
            nickname = desentinelize(nickname, nicknameNullSentinel);
        }
    }

    public HispanicPersonalNameFactory(@NotNull Engine engine, @NotNull PersonalNameManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable HispanicPersonalName doCreation(@NotNull HispanicPersonalNameFactory.HispanicPersonalNameContext context) {
        assert (
            context.givenName != null &&
            context.suffixes != null &&
            context.displayOptions != null
        );
        return new HispanicPersonalName(
            context.givenName,
            context.paternalName,
            context.maternalName,
            context.honorific,
            context.nickname,
            context.suffixes,
            context.displayOptions
        );
    }

    @Override
    protected @Nullable HispanicPersonalName doSelection(@NotNull HispanicPersonalNameFactory.HispanicPersonalNameContext context) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Selection is invalid for type " + HispanicPersonalName.class.getSimpleName());
    }
}
