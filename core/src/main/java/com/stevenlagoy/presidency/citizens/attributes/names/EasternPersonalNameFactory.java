package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.presidency.citizens.Citizen;
import com.stevenlagoy.presidency.citizens.attributes.Family;
import com.stevenlagoy.presidency.citizens.attributes.experiences.PersonalExperienceHistory;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import com.stevenlagoy.presidency.demographics.BlocManager;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.CompletionMethodFor;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.stevenlagoy.presidency.citizens.attributes.names.PersonalNameFactory.*;
import static com.stevenlagoy.presidency.citizens.attributes.names.WesternPersonalNameFactory.inheritFathersLastNamePercent;

public class EasternPersonalNameFactory extends EntityFactory<EasternPersonalName, EasternPersonalNameFactory.EasternPersonalNameContext> {

    // Constants

    /** Percentage of Asian people who have an Eastern-style name. */
    public static final float asianEasternNamePercent = 0.30f;
    /** Percentage of people with a generation name. */
    public static final float generationNamePercent = 0.60f;
    public static final float latentGenerationNamePercent = 0.50f;
    public static final float includeWesternNamePercent = 0.20f;
    public static final float includeNicknamePercent = 0.30f;
    /** Percentage of people with an additional Western-style name. */
    public static final float westernNamePercent = 0.641f;
    /** Percentage of people who place their western name before their traditional name. */
    public static final float westernNameFirstPercent = 0.33f;

    public static class EasternPersonalNameContext extends PersonalNameFactory.PersonalNameContext<EasternPersonalName> {

        /** Nicknames may also be Western names. */
        protected @Nullable String nickname;
        protected static final String nicknameNullSentinel = "";
        protected @Nullable String familyName;
        protected @Nullable String generationName;
        protected static final String generationNameNullSentinel = "";
        protected @Nullable String givenName;

        public EasternPersonalNameContext(
            @NotNull Engine engine,
            @NotNull Demographics demographics,
            @NotNull Integer age,
            @NotNull Family family,
            @NotNull PersonalExperienceHistory experiences,
            @Nullable String honorific,
            @Nullable List<String> suffixes,
            @Nullable Set<PersonalName.DisplayOption> displayOptions,
            @Nullable String nickname,
            @Nullable String familyName,
            @Nullable String generationName,
            @Nullable String givenName
        ) {
            super(engine,
                demographics,
                age,
                family,
                experiences,
                EasternPersonalName.class,
                honorific,
                suffixes,
                displayOptions
            );
            this.nickname = nickname;
            this.familyName = familyName;
            this.generationName = generationName;
            this.givenName = givenName;
        }

        public EasternPersonalNameContext(@NotNull Engine engine, @NotNull PersonalNameFactory.PersonalNameContext<? extends PersonalName> superContext) {
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
            return EasternPersonalName.class;
        }

        @CompletionMethodFor("nickname")
        protected @Nullable String selectNickname() {
            if (givenName == null) return null; // Unable to impute
            String nickname = this.nickname;
            // Determine whether to have a Western name
            if (RandomUtils.chance(westernNamePercent)) {
                // Select a Western name
                nickname = RandomUtils.weightedSelect(
                    engine.getManager(PersonalNameManager.class).getGivenNamesDistribution(
                        new WesternPersonalNameFactory.WesternPersonalNameContext(
                            engine,
                            new Demographics(
                                engine,
                                demographics.getGeneration(),
                                demographics.getReligion(),
                                engine.getManager(BlocManager.class).requireByKey("Anglo"),
                                demographics.getPresentation()
                            ),
                            age,
                            family,
                            experiences
                        )
                    )
                );
                if (nickname != null) return nickname;
            }
            // Determine whether to have a nickname
            if (!RandomUtils.chance(nicknamePercent)) {
                nickname = nicknameNullSentinel;
                return nickname;
            }
            // Determine if nickname is not based on given names
            if (RandomUtils.chance(nicknameNotFromGivenNamesPercent)) {
                // Choose from all nicknames for a person with this presentation
                Set<String> possibleNicknames = engine.getManager(PersonalNameManager.class).getNicknamesForPresentation(demographics.getPresentation());
                if (!possibleNicknames.isEmpty()) {
                    nickname = RandomUtils.randSelect(possibleNicknames);
                    return nickname;
                }
            } // Nickname should come from given names
            Set<String> nicks = engine
                .getManager(PersonalNameManager.class)
                .getNicknames()
                .getOrDefault(givenName, Set.of());
            nickname = nicks.isEmpty() ? nicknameNullSentinel : RandomUtils.randSelect(nicks);
            return nickname;
        }

        @CompletionMethodFor("familyName")
        protected @Nullable String selectFamilyName() {
            if (familyName != null) return familyName;
            // Most people with a father inherit his family name
            if (family.getFather() != null && RandomUtils.chance(inheritFathersLastNamePercent)) {
                familyName = family.getFather().getName().getPreferredFamily();
            }
            else if (family.getMother() != null) {
                familyName = family.getMother().getName().getPreferredFamily();
            }
            else {
                familyName = RandomUtils.weightedSelect(engine
                    .getManager(PersonalNameManager.class)
                    .getFamilyNamesDistribution(this)
                );
            }
            return familyName;
        }

        @CompletionMethodFor("generationName")
        protected @Nullable String selectGenerationName() {
            if (generationName != null) return generationName;
            // Check if a sibling or cousin has a generation name
            Set<Citizen> siblingsCousins = new HashSet<>();
            siblingsCousins.addAll(family.getSiblings());
            siblingsCousins.addAll(family.getCousins());
            for (Citizen siblingCousin : siblingsCousins) {
                if (!siblingCousin.getName().getClass().equals(EasternPersonalName.class)) continue;
                String relativesGenerationName = ((EasternPersonalName) siblingCousin.getName()).getGenerationName();
                if (relativesGenerationName == null || relativesGenerationName.isBlank()) continue;
                generationName = relativesGenerationName;
                return generationName;
            } // No siblings or cousins with selectable generation names
            // Determine whether to have a generation name
            if (!RandomUtils.chance(generationNamePercent)) {
                generationName = generationNameNullSentinel;
            }
            if (generationName == null) {
                generationName = RandomUtils.weightedSelect(engine
                    .getManager(PersonalNameManager.class)
                    .getGenerationNamesDistribution(this)
                );
            }
            if (generationName == null) generationName = generationNameNullSentinel;
            return generationName;
        }

        @CompletionMethodFor("givenName")
        protected @Nullable String selectGivenName() {
            if (givenName != null) return givenName;
            givenName = RandomUtils.weightedSelect(engine
                .getManager(PersonalNameManager.class)
                .getGivenNamesDistribution(this)
            );
            return givenName;
        }

        @Override
        protected @NotNull Map<PersonalName.DisplayOption, Float> getDisplayOptionsPercentages() {
            return Map.of(
                PersonalName.DisplayOption.WESTERN_FIRST, westernNameFirstPercent,
                PersonalName.DisplayOption.LATENT_GENERATION, latentGenerationNamePercent,
                PersonalName.DisplayOption.INCLUDE_WESTERN, includeWesternNamePercent,
                PersonalName.DisplayOption.INCLUDE_NICKNAME, includeNicknamePercent,
                PersonalName.DisplayOption.INCLUDE_HONORIFIC, includeHonorificPercent,
                PersonalName.DisplayOption.INCLUDE_SUFFIX, includeSuffixPercent
            );
        }

        @Override
        public void desentinelize() {
            super.desentinelize();
            nickname = desentinelize(nickname, nicknameNullSentinel);
            generationName = desentinelize(generationName, generationNameNullSentinel);
        }

    }

    public EasternPersonalNameFactory(@NotNull Engine engine, @NotNull PersonalNameManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable EasternPersonalName doCreation(@NotNull EasternPersonalNameFactory.EasternPersonalNameContext context) {
        assert (
            context.familyName != null &&
            context.givenName != null &&
            context.suffixes != null &&
            context.displayOptions != null
        );
        return new EasternPersonalName(
            context.familyName,
            context.generationName,
            context.givenName,
            context.nickname,
            context.honorific,
            context.suffixes,
            context.displayOptions
        );
    }

    @Override
    protected @Nullable EasternPersonalName doSelection(@NotNull EasternPersonalNameFactory.EasternPersonalNameContext context) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Selection is invalid for type " + EasternPersonalName.class.getSimpleName());
    }
}
