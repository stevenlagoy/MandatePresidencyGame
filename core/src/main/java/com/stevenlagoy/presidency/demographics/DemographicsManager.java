package com.stevenlagoy.presidency.demographics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.characters.Citizen;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.Logger;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * <h1>DEMOGRAPHICS MANAGER</h1>
 * {@code ~/demographics/DemographicsManager.java}
 * <p>
 *     <b>Author:</b>
 *     Steven LaGoy
 *     <br>
 *     <b>Created:</b>
 *     10 December 2024 at 8:21 PM
 *     <br>
 *     <b>Modified:</b>
 *     11 April 2026
 * </p>
 * DemographicsManager is responsible for tracking bloc and demographic data, or any
 * population-based state which is detached from individual Character instances.
 */
public class DemographicsManager extends Manager {

    // CONSTANTS ----------------------------------------------------------------------------------

    /** Used to convert counts in the Blocs data file into percentages. */
    public static final long GAME_START_VOTERS = 341_275_500; // 1 Feb 2025

    // INSTANCE FIELDS ----------------------------------------------------------------------------

    private Map<DemographicCategory, List<Bloc>> demographicBlocs;

    private Map<Bloc, Map<Integer, Double>> populationPyramid;

    private final Engine ENGINE;
    private ManagerState currentState;

    // CONSTRUCTOR --------------------------------------------------------------------------------

    public DemographicsManager(Engine engine) {
        ENGINE = engine;
        currentState = ManagerState.INACTIVE;

        demographicBlocs = new HashMap<>();
        populationPyramid = new HashMap<>();
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    @Override
    public boolean init() {
        readBlocData();
        readPopulationPyramidData();
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

    // JSONIC -------------------------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        return new JSONObject("DemographicsManager", Map.of(

        ));
    }

    @Override
    public Manager fromJson(JSONObject json) {
        return this;
    }

    // INSTANCE METHODS ---------------------------------------------------------------------------

    private void readBlocData() {
        demographicBlocs = new HashMap<>();
        JSONObject json = JSONProcessor.processJson(FilePaths.BLOCS);
        for (Object categoryObject : json.getAsList()) {
            if (categoryObject instanceof JSONObject categoryJson) {
                String key = categoryJson.getKey();
                DemographicCategory category = DemographicCategory.valueOf(key.toUpperCase().replaceAll("[^a-zA-Z]+","_"));
                List<Bloc> blocs = createBlocs(category, categoryJson.getAsObject());
                demographicBlocs.put(category, blocs);
            }
        }
    }

    private @NotNull List<Bloc> createBlocs(@NotNull DemographicCategory category, @NotNull JSONObject structure) {
        return createBlocs(category, null, structure);
    }

    public @NotNull List<Bloc> createBlocs(@NotNull DemographicCategory category, @Nullable Bloc parent, @NotNull JSONObject structure) {
        List<Bloc> blocs = new ArrayList<>();

        for (Object keyObj : structure.getAsList()) {
            if (keyObj instanceof JSONObject keyJson) {
                String blocName = keyJson.getKey();
                Object value = keyJson.getValue();

                Bloc bloc;
                if (value instanceof Number numValue) {
                    // Base case: numerical value representing percentage
                    double percentage;
                    if (numValue.doubleValue() >= 1.0f) {
                        // Value is a count of individuals
                        percentage = numValue.doubleValue() / GAME_START_VOTERS;
                    }
                    else {
                        // Value is a percentage of individuals
                        percentage = numValue.doubleValue();
                    }
                    bloc = new Bloc(blocName, category, percentage, Collections.emptySet(), parent, Collections.emptyList());
                    blocs.add(bloc);
                }
                else if (value instanceof JSONObject valueJson) {
                    // Recursive case: nested blocs
                    bloc = new Bloc(blocName, category, 0.0, Collections.emptySet(), parent, Collections.emptyList());
                    bloc.getSubBlocs().addAll(createBlocs(category, bloc, valueJson));
                    blocs.add(bloc);
                }
            }
        }
        return blocs;
    }

    private void readPopulationPyramidData() {
        populationPyramid = new HashMap<>();
        JSONObject json = JSONProcessor.processJson(FilePaths.BIRTHYEAR_PERCENTAGES);
        populationPyramid.put(null, null); // TODO
    }

    public @NotNull Map<Integer, Double> getPopulationPyramid(@NotNull Bloc... blocs) {
        return populationPyramid.get(blocs[0]); // Should find the average combination of the blocs and return the pyramid for that combination
    }

    public @Nullable Bloc matchBlocName(@NotNull String name) {
        for (List<Bloc> blocList : demographicBlocs.values()) {
            for (Bloc bloc : blocList) {
                if (bloc.getName().equals(name))
                    return bloc;
            }
        }
        Logger.log("INVALID BLOC NAME",
            String.format("The Bloc name \"%s\" is non-existent and could not be matched.", name), new Exception());
        return null;
    }

    public Demographics getCommonDemographics() {
        return new Demographics(this, "Millennial", "White Catholic", "English", "Woman");
    }

    public @Nullable Bloc getCommonBloc(@NotNull DemographicCategory category) {
        return demographicBlocs.get(category).stream().max(Comparator.comparing(Bloc::getPercentageMembership)).orElse(null);
    }

    public @NotNull Demographics selectDemographics() {
        Bloc generation, religion, raceEthnicity, presentation;
        presentation = selectBloc(DemographicCategory.PRESENTATION, Set.of());
        generation = selectBloc(DemographicCategory.GENERATION, Set.of(presentation));
        raceEthnicity = selectBloc(DemographicCategory.RACE_ETHNICITY, Set.of(presentation, generation));
        religion = selectBloc(DemographicCategory.RELIGION, Set.of(presentation, generation, raceEthnicity));
        return new Demographics(generation, religion, raceEthnicity, presentation);
    }

    public @NotNull Bloc selectBloc(@NotNull DemographicCategory category, @NotNull Set<Bloc> alreadySelected) {
        Map<Bloc, Double> weights = new HashMap<>();
        for (Bloc bloc : demographicBlocs.get(category)) {
            weights.put(bloc, bloc.getPercentageMembership());
        }
        return RandomUtils.weightedSelect(weights);
    }

    public @NotNull Demographics selectRandomDemographics() {
        return new Demographics(
            selectRandomBloc(DemographicCategory.GENERATION),
            selectRandomBloc(DemographicCategory.RELIGION),
            selectRandomBloc(DemographicCategory.RACE_ETHNICITY),
            selectRandomBloc(DemographicCategory.PRESENTATION)
        );
    }

    public @NotNull Bloc selectRandomBloc(@NotNull DemographicCategory category) {
        var selected = RandomUtils.randSelect(demographicBlocs.get(category));
        assert selected != null;
        return selected;
    }

    // TODO Instead of picking one bloc and then populating the rest normally, this should use bloc overlaps to find the most underrepresented
    public @NotNull Demographics selectUnderrepresentedDemographics() {
        if (ENGINE.MANAGERS.CHARACTER_MANAGER.getNumCitizens() == 0) return getCommonDemographics();
        List<Bloc> allBlocs = new ArrayList<>();
        demographicBlocs.values().forEach(allBlocs::addAll);
        Bloc underrepresentedBloc = selectUnderrepresentedBloc(allBlocs);
        Bloc generation, religion, raceEthnicity, presentation;
        switch (underrepresentedBloc.getCategory()) {
            case GENERATION :
                generation    = underrepresentedBloc;
                religion      = selectBloc(DemographicCategory.RELIGION,       Set.of(generation));
                raceEthnicity = selectBloc(DemographicCategory.RACE_ETHNICITY, Set.of(generation, religion));
                presentation  = selectBloc(DemographicCategory.PRESENTATION,   Set.of(generation, religion, raceEthnicity));
                break;
            case RELIGION :
                religion      = underrepresentedBloc;
                generation    = selectBloc(DemographicCategory.GENERATION,     Set.of(religion));
                raceEthnicity = selectBloc(DemographicCategory.RACE_ETHNICITY, Set.of(religion, generation));
                presentation  = selectBloc(DemographicCategory.PRESENTATION,   Set.of(religion, generation, raceEthnicity));
                break;
            case RACE_ETHNICITY :
                raceEthnicity = underrepresentedBloc;
                generation    = selectBloc(DemographicCategory.GENERATION,   Set.of(raceEthnicity));
                religion      = selectBloc(DemographicCategory.RELIGION,     Set.of(raceEthnicity, generation));
                presentation  = selectBloc(DemographicCategory.PRESENTATION, Set.of(raceEthnicity, generation, religion));
                break;
            case PRESENTATION :
                presentation  = underrepresentedBloc;
                generation    = selectBloc(DemographicCategory.GENERATION,     Set.of(presentation));
                religion      = selectBloc(DemographicCategory.RELIGION,       Set.of(presentation, generation));
                raceEthnicity = selectBloc(DemographicCategory.RACE_ETHNICITY, Set.of(presentation, generation, religion));
                break;
            default :
                return selectDemographics();
        }
        return new Demographics(generation, religion, raceEthnicity, presentation);
    }

    public @NotNull Bloc selectUnderrepresentedBloc(@NotNull List<Bloc> blocs) {
        Bloc underrepresentedBloc = blocs.get(0);
        double underrepresentedValue = determineRepresentationRatio(underrepresentedBloc);

        for (Bloc bloc : blocs) {
            if (bloc.getSubBlocs().isEmpty()) {
                double representationRatio = determineRepresentationRatio(bloc);
                if (representationRatio < underrepresentedValue ||
                    (representationRatio == underrepresentedValue &&
                        bloc.getPercentageMembership() > underrepresentedBloc.getPercentageMembership())) {
                    underrepresentedBloc = bloc;
                    underrepresentedValue = representationRatio;
                }
            }
            else {
                Bloc candidate = selectUnderrepresentedBloc(bloc.getSubBlocs());
                double candidateRatio = determineRepresentationRatio(candidate);
                if (Double.isNaN(underrepresentedValue) || candidateRatio < underrepresentedValue ||
                    (candidateRatio == underrepresentedValue &&
                        bloc.getPercentageMembership() > underrepresentedBloc.getPercentageMembership())) {
                    underrepresentedBloc = candidate;
                    underrepresentedValue = candidateRatio;
                }
            }
        }

        return underrepresentedBloc;
    }

    private double determineRepresentationRatio(Bloc bloc) {
        // Returns ratio of actual character membership to expected membership
        // <1 if underrepresented, >1 if overrepresented, =1 if perfectly represented
        try {
            if (ENGINE.MANAGERS.CHARACTER_MANAGER.getNumCitizens() == 0)
                return 1.0f; // if there are no characters, every bloc is perfectly represented
            double expectedRepresentation = bloc.getPercentageMembership();
            double actualRepresentation = bloc.getMembers().size() * 1.0f / ENGINE.MANAGERS.CHARACTER_MANAGER.getNumCitizens();
            return (actualRepresentation / expectedRepresentation);
        }
        catch (ArithmeticException e) {
            return 1.0f;
        }
    }

    public void addCharacterToBlocs(@NotNull Citizen citizen) {
        for (Bloc bloc : citizen.getDemographics().getBlocs()) {
            bloc.getMembers().add(citizen);
        }
    }
}
