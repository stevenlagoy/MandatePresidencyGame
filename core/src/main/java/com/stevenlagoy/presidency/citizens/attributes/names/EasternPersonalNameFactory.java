package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Factory;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EasternPersonalNameFactory extends Factory<EasternPersonalName, PersonalNameFactory.NameContext> {

    // Constants

    /** Percentage of Asian people who have an Eastern-style name. */
    public static final float asianEasternNamePercent = 0.30f;
    /** Percentage of people with a generation name. */
    public static final float generationNamePercent = 0.60f;
    /** Percentage of people with an additional Western-style name. */
    public static final float westernNamePercent = 0.50f;
    /** Percentage of people who place their western name before their traditional name. */
    public static final float westernNameFirstPercent = 0.33f;

    // Instance Fields

    private final PersonalNameFactory personalNameFactory;

    EasternPersonalNameFactory(@NotNull Engine engine, @NotNull PersonalNameFactory personalNameFactory) {
        super(engine);
        this.personalNameFactory = personalNameFactory;
    }

    @Override
    public @NotNull EasternPersonalName build(@NotNull PersonalNameFactory.NameContext context) {
        EasternPersonalName name = new EasternPersonalName();
        fillFamilyName(name, context);
        fillGenerationName(name, context);
        fillGivenName(name, context);
        fillWesternName(name, context);
        return name;
    }

    private void fillFamilyName(@NotNull EasternPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
        name.setFamilyName(personalNameFactory.selectFamilyName(context));
    }

    private void fillGenerationName(@NotNull EasternPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
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

    private void fillGivenName(@NotNull EasternPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
        name.setGivenName(personalNameFactory.selectGivenName(context));
    }

    private void fillWesternName(@NotNull EasternPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
        // Determine whether to have a Western name
        if (!RandomUtils.chance(westernNamePercent)) {
            name.setWesternName(null);
            return;
        }
        // Select a Western name
        String westernName = personalNameFactory.selectGivenName(new PersonalNameFactory.NameContext(
            new Demographics(
                engine,
                context.demographics().getGeneration(),
                context.demographics().getReligion(),
                Objects.requireNonNull(engine.DEMOGRAPHICS_MANAGER.matchBloc("Anglo")).orElseThrow(),
                context.demographics().getPresentation()
            ),
            context.age(),
            context.family()
        ));
        name.setWesternName(westernName);
    }

    private String selectGenerationName(@NotNull PersonalNameFactory.NameContext context) {
        return RandomUtils.weightedSelect(engine.getManager(NameManager.class).getGenerationNamesDistribution(context));
    }

}
