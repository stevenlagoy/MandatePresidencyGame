package com.stevenlagoy.presidency.demographics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.characters.Citizen;
import com.stevenlagoy.presidency.characters.attributes.Sex;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.*;

/**
 * <h1>DEMOGRAPHICS MANAGER</h1>
 * {@code ~/demographics/DemographicsManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy                <br>
 *     <b>Created: </b> 10 December 2024 at 8:21 PM <br>
 *     <b>Modified:</b> 11 April 2026               <br>
 * </p>
 *
 * DemographicsManager is responsible for tracking bloc and demographic data, or any
 * population-based state which is detached from individual Character instances.
 *
 * @author Steven LaGoy
 */
public class DemographicsManager extends Manager {

    // Constants

    /** Used to convert counts in the Blocs data file into percentages. */
    public static final long GAME_START_VOTERS = 341_275_500; // 1 Feb 2025
    public static final double FEMALE_WOMAN_PRESENTATION_PERCENT = 0.99;
    public static final double FEMALE_NONBINARY_PRESENTATION_PERCENT = 0.075;
    public static final double FEMALE_MAN_PRESENTATION_PERCENT = 0.025;
    public static final double INTERSEX_WOMAN_PRESENTATION_PERCENT = 0.55;
    public static final double INTERSEX_NONBINARY_PRESENTATION_PERCENT = 0.10;
    public static final double INTERSEX_MAN_PRESENTATION_PERCENT = 0.35;
    public static final double MALE_WOMAN_PRESENTATION_PERCENT = 0.05;
    public static final double MALE_NONBINARY_PRESENTATION_PERCENT = 0.05;
    public static final double MALE_MAN_PRESENTATION_PERCENT = 0.99;

    // Instance Fields

    private Map<DemographicCategory, List<Bloc>> demographicBlocs;
    private Map<Bloc, Map<Integer, Double>> populationPyramid;

    // Constructors

    public DemographicsManager(Engine engine, Manager superManager) {
        super(engine, superManager);
        demographicBlocs = new HashMap<>();
        populationPyramid = new HashMap<>();
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
        readBlocData();
        readPopulationPyramidData();
    }

    @Override
    protected void doCleanup() {
        demographicBlocs.clear();
        populationPyramid.clear();
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
    }

    // Instance Methods

    private void readBlocData() {
        requireState(ManagerState.INITIALIZING);
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
        requireState(ManagerState.INITIALIZING);
        return createBlocs(category, null, structure);
    }

    public @NotNull List<Bloc> createBlocs(@NotNull DemographicCategory category, @Nullable Bloc parent, @NotNull JSONObject structure) {
        requireState(ManagerState.INITIALIZING);
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
                    bloc = new Bloc(engine, blocName, category, percentage, Collections.emptySet(), parent, Collections.emptyList());
                    blocs.add(bloc);
                }
                else if (value instanceof List<?> valueList) {
                    if (valueList.get(0) instanceof JSONObject) {
                        JSONObject valueJson = new JSONObject("", valueList);
                        // Recursive case: nested blocs
                        bloc = new Bloc(engine, blocName, category, 0.0, Collections.emptySet(), parent, Collections.emptyList());
                        bloc.getSubBlocs().addAll(createBlocs(category, bloc, valueJson));
                        blocs.add(bloc);
                    }
                }
            }
        }
        return blocs;
    }

    private void readPopulationPyramidData() {
        requireState(ManagerState.INITIALIZING);
        populationPyramid = new HashMap<>();
        JSONObject json = JSONProcessor.processJson(FilePaths.BIRTHYEAR_PERCENTAGES);
        populationPyramid.put(null, null); // TODO
    }

    public @NotNull Map<Integer, Double> getPopulationPyramid(@NotNull Bloc... blocs) {
        requireOperational();
        return populationPyramid.get(blocs[0]); // Should find the average combination of the blocs and return the pyramid for that combination
    }

    public @NotNull Optional<Bloc> matchBloc(@NotNull String name) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        for (List<Bloc> blocList : demographicBlocs.values()) {
            for (Bloc rootBloc : blocList) {
                if (rootBloc.getName().equals(name))
                    return Optional.of(rootBloc);
                for (Bloc descendantBloc : rootBloc.getDescendantBlocs()) {
                    if (descendantBloc.getName().equals(name))
                        return Optional.of(descendantBloc);
                }
            }
        }
        onDegraded(new IllegalArgumentException("The Bloc name \"" + name + "\" is non-existent and could not be matched."));
        return Optional.empty();
    }

    public Demographics getCommonDemographics() {
        requireOperational();
        return new Demographics(engine, "Millennial", "White Catholic", "English", "Woman");
    }

    public @Nullable Bloc getCommonBloc(@NotNull DemographicCategory category) {
        requireOperational();
        return demographicBlocs.get(category).stream().max(Comparator.comparing(Bloc::getPercentageMembership)).orElse(null);
    }

    public @NotNull Demographics selectDemographics() {
        requireOperational();
        Bloc generation, religion, raceEthnicity, presentation;
        presentation = selectBloc(DemographicCategory.PRESENTATION, Set.of());
        generation = selectBloc(DemographicCategory.GENERATION, Set.of(presentation));
        raceEthnicity = selectBloc(DemographicCategory.RACE_ETHNICITY, Set.of(presentation, generation));
        religion = selectBloc(DemographicCategory.RELIGION, Set.of(presentation, generation, raceEthnicity));
        return new Demographics(engine, generation, religion, raceEthnicity, presentation);
    }

    public @NotNull Bloc selectBloc(@NotNull DemographicCategory category, @NotNull Set<Bloc> alreadySelected) {
        requireOperational();
        Map<Bloc, Double> weights = new HashMap<>();
        for (Bloc bloc : demographicBlocs.get(category)) {
            weights.put(bloc, bloc.getPercentageMembership());
        }
        Bloc selected = RandomUtils.weightedSelect(weights);
        assert(selected != null);
        return selected;
    }

    public @NotNull Bloc selectPresentationForSex(@NotNull Sex sex) {
        String presentationBlocName = switch(sex) {
            case FEMALE ->
                RandomUtils.chance(FEMALE_WOMAN_PRESENTATION_PERCENT) ? "Woman" :
                RandomUtils.chance(FEMALE_MAN_PRESENTATION_PERCENT) ? "Man" :
                "Nonbinary";
            case INTERSEX ->
                RandomUtils.chance(INTERSEX_WOMAN_PRESENTATION_PERCENT) ? "Woman" :
                RandomUtils.chance(INTERSEX_MAN_PRESENTATION_PERCENT) ? "Man" :
                "Nonbinary";
            case MALE ->
                RandomUtils.chance(MALE_WOMAN_PRESENTATION_PERCENT) ? "Woman" :
                RandomUtils.chance(MALE_MAN_PRESENTATION_PERCENT) ? "Man" :
                "Nonbinary";
        };
        return matchBloc(presentationBlocName).orElseThrow();
    }

    public @NotNull Demographics selectRandomDemographics() {
        requireOperational();
        return new Demographics(
            engine,
            selectRandomBloc(DemographicCategory.GENERATION),
            selectRandomBloc(DemographicCategory.RELIGION),
            selectRandomBloc(DemographicCategory.RACE_ETHNICITY),
            selectRandomBloc(DemographicCategory.PRESENTATION)
        );
    }

    public @NotNull Bloc selectRandomBloc(@NotNull DemographicCategory category) {
        requireOperational();
        var selected = RandomUtils.randSelect(demographicBlocs.get(category));
        assert selected != null;
        return selected;
    }

    // TODO Instead of picking one bloc and then populating the rest normally, this should use bloc overlaps to find the most underrepresented
    public @NotNull Demographics selectUnderrepresentedDemographics() {
        requireOperational();
        if (engine.CHARACTER_MANAGER.getNumCitizens() == 0) return getCommonDemographics();
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
        return new Demographics(engine, generation, religion, raceEthnicity, presentation);
    }

    public @NotNull Bloc selectUnderrepresentedBloc(@NotNull List<Bloc> blocs) {
        requireOperational();
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
        requireOperational();
        // Returns ratio of actual character membership to expected membership
        // <1 if underrepresented, >1 if overrepresented, =1 if perfectly represented
        try {
            if (engine.CHARACTER_MANAGER.getNumCitizens() == 0)
                return 1.0f; // if there are no characters, every bloc is perfectly represented
            double expectedRepresentation = bloc.getPercentageMembership();
            double actualRepresentation = bloc.getMembers().size() * 1.0f / engine.CHARACTER_MANAGER.getNumCitizens();
            return (actualRepresentation / expectedRepresentation);
        }
        catch (ArithmeticException e) {
            return 1.0f;
        }
    }

    public void addCharacterToBlocs(@NotNull Citizen citizen) {
        requireOperational();
        for (Bloc bloc : citizen.getDemographics().getBlocs()) {
            bloc.getMembers().add(citizen);
        }
    }

    public Bloc getGenerationForBirthday(@NotNull LocalDate birthday) {
        if (birthday.getYear() < 1883) return null;
        else if (birthday.getYear() < 1900) return matchBloc("Lost Generation").orElseThrow();
        else if (birthday.getYear() < 1927) return matchBloc("Greatest Generation").orElseThrow();
        else if (birthday.getYear() < 1945) return matchBloc("Silent Generation").orElseThrow();
        else if (birthday.getYear() < 1964) return matchBloc("Baby Boomer").orElseThrow();
        else if (birthday.getYear() < 1980) return matchBloc("Generation X").orElseThrow();
        else if (birthday.getYear() < 1996) return matchBloc("Millennial").orElseThrow();
        else if (birthday.getYear() < 2012) return matchBloc("Generation Z").orElseThrow();
        else if (birthday.getYear() < 2024) return matchBloc("Generation Alpha").orElseThrow();
        else return matchBloc("Generation Beta").orElseThrow();
    }
}
