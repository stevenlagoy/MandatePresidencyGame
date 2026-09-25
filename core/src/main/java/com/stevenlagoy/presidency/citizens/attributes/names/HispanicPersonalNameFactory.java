package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Factory;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class HispanicPersonalNameFactory extends Factory<HispanicPersonalName, PersonalNameFactory.NameContext> {

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

    // Instance Fields

    private final PersonalNameFactory personalNameFactory;

    HispanicPersonalNameFactory(@NotNull Engine engine, @NotNull PersonalNameFactory personalNameFactory) {
        super(engine);
        this.personalNameFactory = personalNameFactory;
    }

    @Override
    public @NotNull HispanicPersonalName build(@NotNull PersonalNameFactory.NameContext context) {
        HispanicPersonalName name = new HispanicPersonalName();
        fillGivenName(name, context);
        fillPaternalName(name, context);
        fillMaternalName(name, context);
        personalNameFactory.fillNickname(name, context);
        return name;
    }

    private void fillGivenName(@NotNull HispanicPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
        // Choose number of given names
        int numberGivenNames = 1 + RandomUtils.probabilisticCount(hispanicMultipleForenamesPercent);
        // Choose appropriate names
        String givenName = personalNameFactory.selectDistinctGivenNames(context, numberGivenNames);
        // Set
        name.setGivenName(givenName);
    }

    private void fillPaternalName(@NotNull HispanicPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
        name.setPaternalName(selectApellido(context));
    }

    private void fillMaternalName(@NotNull HispanicPersonalName name, @NotNull PersonalNameFactory.NameContext context) {
        name.setMaternalName(selectApellido(context));
    }

    private @NotNull String selectApellido(@NotNull PersonalNameFactory.NameContext context) {
        // Choose number of family names in the apellido
        int numberNames = 1 + RandomUtils.probabilisticCount(hispanicCompositeSurnamePercent);
        // Choose conjoiners
        Set<String> possibleConjoiners = hispanicCompositeSurnameConjoiners.get("Hispanic");
        for (String blocName : context.demographics().getRaceEthnicity().getAncestorNames()) {
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
        return personalNameFactory.selectDistinctFamilyNames(context, numberNames, conjoiners);
    }



}
