package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.demographics.DemographicCategory;
import com.stevenlagoy.presidency.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>NAME MANAGER</h1>
 * {@code ~/characters/attributes/names/NameManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy            <br>
 *     <b>Created: </b> 01 June 2025 at 1:04 AM <br>
 *     <b>Modified:</b> 04 June 2026            <br>
 * </p>
 *
 * NameManager is responsible for creating and maintaining information about {@link PersonalName} attributes.
 *
 * @implNote PersonalName is an abstract class with several concretions. This class violates the open-closed principle as specific name forms (concretions)
 * must each have separate implementation details. At this time, this is considered acceptable because the number of possible name forms is not
 * expected to increase quickly enough to warrant a more abstract strategy-pattern solution to concrete name class construction.
 *
 * @author Steven LaGoy
 */
public class PersonalNameManager extends EntityManager<PersonalName, String> {

    // Instance Fields

    /** Distribution of given names for members of key bloc sets. */
    private final @NotNull Map<Set<Bloc>, Map<String, Double>> givenNamesDistribution;
    /** Distribution of family names for members of key bloc sets. */
    private final @NotNull Map<Set<Bloc>, Map<String, Double>> familyNamesDistribution;
    /** Distribution of generation names for members of key bloc sets. */
    private final @NotNull Map<Set<Bloc>, Map<String, Double>> generationNamesDistribution;
    /** Map of given names to associated nicknames. */
    private final @NotNull Map<String, Set<String>> nicknames;

    // Constructors

    public PersonalNameManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        setFactory(new PersonalNameFactory(engine, this));
        givenNamesDistribution      = new HashMap<>();
        familyNamesDistribution     = new HashMap<>();
        generationNamesDistribution = new HashMap<>();
        nicknames                   = new HashMap<>();
    }

    // Manager Methods

    @Override
    protected void doInit() {
        readGivenNamesData();
        readFamilyNamesData();
        readGenerationNamesData();
        readNicknamesData();
    }

    @Override
    protected void doCleanup() {
        givenNamesDistribution.clear();
        familyNamesDistribution.clear();
        generationNamesDistribution.clear();
        nicknames.clear();
    }

    @Override
    protected @Nullable String keyOf(@NotNull PersonalName entity) {
        return entity.getIndexedName();
    }

    @Override
    protected @NotNull PersonalName entityFromJson(JSONObject json) {
        // TODO this is an abstract method, but abstract factories might not have enough information
        return new PersonalName(engine, json);
    }

    // Instance Methods

    private void readGivenNamesData() {
        try {
            JSONObject json = new JSONObject(FilePath.GIVEN_NAMES.path);
            givenNamesDistribution.putAll(processNamesStructure(json));
        } catch (IOException e) {
            onError(e);
        }
    }

    private void readFamilyNamesData() {
        try {
            JSONObject json = new JSONObject(FilePath.FAMILY_NAMES.path);
            familyNamesDistribution.putAll(processNamesStructure(json));
        } catch (IOException e) {
            onError(e);
        }
    }

    private void readGenerationNamesData() {
        try {
            JSONObject json = new JSONObject(FilePath.DECADE_NAMES.path);
            generationNamesDistribution.putAll(processNamesStructure(json));
        } catch (IOException e) {
            onError(e);
        }
    }

    private void readNicknamesData() {
        try {
            JSONObject json = new JSONObject(FilePath.NICKNAMES.path);
            nicknames.clear();
            for (Object obj : json.requireArray()) {
                if (!(obj instanceof JSONObject entry)) continue;
                String key = entry.getKey();
                List<?> value = entry.requireArray();
                Set<String> names = new HashSet<>();
                for (Object nickname : value) {
                    names.add((String) nickname);
                }
                nicknames.put(key, names);
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private @NotNull Map<Set<Bloc>, Map<String, Double>> processNamesStructure(@NotNull JSONObject json) {
        return processNamesStructure(json, null);
    }

    private @NotNull Map<Set<Bloc>, Map<String, Double>> processNamesStructure(@NotNull JSONObject json, @Nullable Set<Bloc> currentBlocs) {
        if (currentBlocs == null) currentBlocs = new HashSet<>();
        Map<Set<Bloc>, Map<String, Double>> distributions = new HashMap<>();

        for (Object obj : json.requireArray()) {
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
                Bloc bloc = engine.DEMOGRAPHICS_MANAGER.matchBloc(key).orElse(null);
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

//    public @NotNull PersonalName buildPersonalName(@NotNull PersonalNameFactory.NameContext context) {
//        PersonalName name = switch (personalNameFactory.selectNameForm(context)) {
//            case HISPANIC -> hispanicPersonalNameFactory.build(context);
//            case EASTERN  -> easternPersonalNameFactory.build(context);
//            case WESTERN  -> westernPersonalNameFactory.build(context);
//        };
//        personalNameFactory.decorate(name, context);
//        return name;
//    }

    // General selection --------------------------------------------------------------------------

//    public @NotNull Map<String, Double> getGivenNamesDistribution(@NotNull PersonalNameFactory.NameContext context) {
//        return getNamesDistributionForContext(context, givenNamesDistribution);
//    }
//
//    public @NotNull Map<String, Double> getFamilyNamesDistribution(@NotNull PersonalNameFactory.NameContext context) {
//        return getNamesDistributionForContext(context, familyNamesDistribution);
//    }
//
//    public @NotNull Map<String, Double> getGenerationNamesDistribution(@NotNull PersonalNameFactory.NameContext context) {
//        return getNamesDistributionForContext(context, generationNamesDistribution);
//    }
//
//    public @NotNull Map<String, Set<String>> getNicknames() {
//        return nicknames;
//    }

    public @NotNull Set<String> getNicknamesForPresentation(@NotNull Bloc presentation) {
        if (!presentation.getCategory().equals(DemographicCategory.PRESENTATION)) return Collections.emptySet();
        return Set.of("Nickname-A", "Nickname-B", "Nickname-C", "Nickname-D", "Nickname-E", "Nickname-F", "Nickname-G");
        // TODO we should have a file which associates nicknames with presentation
        // TODO the file mapping full names to nicknames should remain but be accessed differently
    }

//    private @NotNull Map<String, Double> getNamesDistributionForContext(@NotNull PersonalNameFactory.NameContext context, @NotNull Map<Set<Bloc>, Map<String, Double>> namesDistribution) {
//        Set<Set<Bloc>> realKeys = namesDistribution.keySet();
//
//        // Get targets including ancestor blocs
//        Set<Bloc> targets = new HashSet<>();
//        for (Bloc bloc : context.demographics.getBlocs()) {
//            targets.addAll(bloc.getAncestorBlocs());
//        }
//
//        Set<Set<Bloc>> combinations = CollectionUtils.combinations(targets);
//        // Sort out invalid combinations
//        Set<Set<Bloc>> validCombinations = combinations.stream().filter(realKeys::contains).collect(Collectors.toSet());
//
//        // Check that there is a valid combination
//        if (validCombinations.isEmpty()) {
//            Logger.error("NO VALID NAMES", String.format("Could not find any valid names with these target demographics: %s", targets));
//            return new HashMap<>();
//        }
//
//        // Pick one of the combinations with the most included Blocs
//        Set<Bloc> bestCombination = Collections.max(validCombinations, Comparator.comparingInt(Set::size));
//
//        return namesDistribution.get(bestCombination);
//    }
}
