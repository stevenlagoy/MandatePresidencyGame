package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.characters.attributes.experiences.PersonalExperienceType;
import com.stevenlagoy.presidency.characters.attributes.experiences.PersonalExperienceHistory;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePath;
import com.stevenlagoy.presidency.util.Logger;
import com.stevenlagoy.presidency.util.RandomUtils;
import com.stevenlagoy.presidency.util.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>EXPERIENCE MANAGER</h1>
 * {@code ~/characters/attribtues/ExperienceManager.java}
 * <p>
 * <b>Author:  </b> Steven LaGoy             <br>
 * <b>Created: </b> 09 June 2025 at 12:48 AM <br>
 * <b>Modified:</b> 09 June 2026             <br>
 * </p>
 * <p>
 * ExperienceManager is responsible for creating and tracking Experiences and ExperienceHistories.
 *
 * @author Steven LaGoy
 */
public class ExperienceManager extends Manager {

    // Constants

    public static final int maxGapYearsBetweenExperiences = 3;
    public static final int minAgeForExperience = 18;

    // Instance Fields

    Set<PersonalExperienceType> personalExperienceTypes;

    // Constructors

    public ExperienceManager(@NotNull Engine engine, @Nullable Manager superManager) {
        super(engine, superManager);
        personalExperienceTypes = new HashSet<>();
    }

    // Manager Methods

    @Override
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
        readExperiences();
    }

    @Override
    protected void doCleanup() {
        personalExperienceTypes.clear();
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

    private void readExperiences() {
        requireState(ManagerState.INITIALIZING);
        try {
            JSONObject experiencesJson = new JSONObject(FilePath.EXPERINECES.path);
            for (Object obj : experiencesJson.requireArray()) {
                if (obj instanceof JSONObject experienceJson) {
                    personalExperienceTypes.add(new PersonalExperienceType(engine, experienceJson));
                }
            }
            resolveConnections();
        }
        catch (IOException e) {
            onError(e);
        }
    }

    private void resolveConnections() {
        requireState(ManagerState.INITIALIZING);
        try {
            JSONObject experiencesJson = new JSONObject(FilePath.EXPERINECES.path);
            for (Object obj : experiencesJson.requireArray()) {
                if (obj instanceof JSONObject experienceJson) {
                    PersonalExperienceType personalExperienceType = matchExperience(experienceJson.getKey()).orElseThrow();
                    for (Object connectionObj : experienceJson.requireArray("connections")) {
                        if (connectionObj instanceof JSONObject connection) {
                            try {
                                personalExperienceType.getConnections().put(matchExperience(connection.getKey()).orElseThrow(), connection.requireNumber().doubleValue());
                            }
                            catch (Exception e) {
                                Logger.error("Could not make connection '%s' for experience '%s': %s. Check keys in %s.", connection.getKey(), personalExperienceType.getLabel(), e.getMessage(), FilePath.EXPERINECES);
                            }
                        }
                        else throw new RuntimeException("Encountered an unexpected value in " + FilePath.EXPERINECES);
                    }
                }
            }
        }
        catch (IOException e) {
            onError(e);
        }
    }

    public @NotNull Optional<PersonalExperienceType> matchExperience(@NotNull String experienceName) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        Optional<PersonalExperienceType> res;
        res = personalExperienceTypes.stream().filter(personalExperienceType -> personalExperienceType.getLabel().equalsIgnoreCase(experienceName)).findFirst();
        if (res.isEmpty())
            res = personalExperienceTypes.stream().filter(personalExperienceType -> personalExperienceType.getName().replace(" ", "_").equalsIgnoreCase(experienceName.replace(" ", "_"))).findFirst();
        return res;
    }

    public @NotNull Set<PersonalExperienceType> getExperiences() {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        return personalExperienceTypes;
    }

    public @NotNull Set<PersonalExperienceType> getExperiences(@NotNull String track) {
        requireOperational();
        return personalExperienceTypes.stream().filter(personalExperienceType -> personalExperienceType.getTrack().equals(track)).collect(Collectors.toSet());
    }

    public @NotNull Set<PersonalExperienceType> getExperiences(int age) {
        requireOperational();
        return personalExperienceTypes.stream().filter(personalExperienceType -> personalExperienceType.getMinAge() <= age).collect(Collectors.toSet());
    }

    public @NotNull Set<PersonalExperienceType> getExperiences(@NotNull Collection<PersonalExperienceType> priors) {
        requireOperational();
        return personalExperienceTypes.stream().filter(personalExperienceType -> personalExperienceType.prerequisitsMet(priors)).collect(Collectors.toSet());
    }

    public @NotNull Set<PersonalExperienceType> getExperiences(@NotNull Collection<PersonalExperienceType> priors, int age) {
        requireOperational();
        return getExperiences(priors).stream().filter(personalExperienceType -> personalExperienceType.getMinAge() <= age && personalExperienceType.getMaxAge() >= age).collect(Collectors.toSet());
    }

    public @NotNull PersonalExperienceHistory buildExperienceHistory(LocalDate experienceHistoryStartDate) {
        requireOperational();
        PersonalExperienceHistory personalExperienceHistory = new PersonalExperienceHistory(engine, new TreeSet<>());
        fillExperienceHistory(experienceHistoryStartDate, personalExperienceHistory);
        return personalExperienceHistory;
    }

    public void fillExperienceHistory(LocalDate experienceHistoryStartDate, @NotNull PersonalExperienceHistory personalExperienceHistory) {
        requireOperational();
        double targetCompleteness = chooseTargetExperienceHistoryCompleteness();
        final long maxAttempts = 25;
        long attempts = 0;
        while (personalExperienceHistory.getTotalOccupiedYears() / engine.TIME_MANAGER.yearsAgo(experienceHistoryStartDate) < targetCompleteness && attempts++ < maxAttempts) {
            addOneExperienceToExperienceHistory(experienceHistoryStartDate, personalExperienceHistory);
        }
    }

    private double chooseTargetExperienceHistoryCompleteness() {
        requireOperational();
        return RandomUtils.randNextFloat(0.65f, 0.9f);
    }

    public void addOneExperienceToExperienceHistory(LocalDate experienceHistoryStartDate, @NotNull PersonalExperienceHistory personalExperienceHistory) {
        requireOperational();

        PersonalExperienceType personalExperienceType = null;

        // Check if there's an experience which is unfulfilled
        boolean foundRootExperience = false;
        for (PersonalExperienceHistory.ExperienceEntry experienceEntry : personalExperienceHistory.getExperiences().descendingSet()) {
            personalExperienceType = experienceEntry.getExperience();
            while (!personalExperienceType.prerequisitsMet(personalExperienceHistory.getExperiencesBefore(experienceEntry.getStartDate()).stream().map(PersonalExperienceHistory.ExperienceEntry::getExperience).collect(Collectors.toSet()))) {
                foundRootExperience = true;
                // Choose a prerequisite
                personalExperienceType = RandomUtils.randSelect(personalExperienceType.getPrerequisites());
                assert (personalExperienceType != null); // Can only be null in the case there are no prerequisites, which is impossible by the loop condition
            }
            if (foundRootExperience) break;
        }
        if (!foundRootExperience) personalExperienceType = null;

        // Find a gap of sufficient size between experiences
        LocalDate prevEndDate = experienceHistoryStartDate, nextStartDate = null;
        boolean foundGap = false;
        for (PersonalExperienceHistory.ExperienceEntry experienceEntry : personalExperienceHistory.getExperiences()) {
            nextStartDate = experienceEntry.getStartDate();
            assert (prevEndDate != null); // The only null end date should be the final experienceEntry, so this state is impossible
            if (TimeUtils.yearsBetween(prevEndDate, nextStartDate) > maxGapYearsBetweenExperiences * 2) {
                foundGap = true;
                break;
            }
            prevEndDate = experienceEntry.getEndDate();
        }
        if (!foundGap && prevEndDate != null) {
            nextStartDate = engine.TIME_MANAGER.getCurrentDate().toLocalDate();
            if (TimeUtils.yearsBetween(prevEndDate, nextStartDate) > maxGapYearsBetweenExperiences * 2) {
                foundGap = true;
            }
        }
        if (!foundGap) {
            // Could not find a large enough gap to add an experience
            return;
        }
        // Choose a start date within the first two thirds of the max gap size (up to 2 years)
        final LocalDate experienceStartDate = prevEndDate.plusDays(RandomUtils.nextInt(1, maxGapYearsBetweenExperiences * TimeUtils.daysInYear * 2 / 3));
        int ageAtStartDate = TimeUtils.yearsBetween(experienceHistoryStartDate, experienceStartDate) + minAgeForExperience;
        if (personalExperienceType == null) {
            // Get all the experiences isComplete before that date
            List<PersonalExperienceType> experiencesCompleted = personalExperienceHistory.getExperiencesBefore(experienceStartDate).stream().map(PersonalExperienceHistory.ExperienceEntry::getExperience).collect(Collectors.toList());
            // Get all valid experiences with those isComplete experiences and the age
            Set<PersonalExperienceType> possiblePersonalExperienceTypes = getExperiences(experiencesCompleted, ageAtStartDate);
            // Rank possible experiences using the connections
            Map<PersonalExperienceType, Double> liklihoods = new HashMap<>();
            possiblePersonalExperienceTypes.forEach(possible -> liklihoods.put(possible, possible.getBaseChance()));
            for (PersonalExperienceType completed : experiencesCompleted) {
                for (var entry : completed.getConnections().entrySet()) {
                    liklihoods.merge(entry.getKey(), entry.getValue(), Double::sum);
                }
            }
            liklihoods.keySet().removeIf(key -> !possiblePersonalExperienceTypes.contains(key) || (experiencesCompleted.contains(key) && !key.isRepeatable()));
            // Make academic experiences much more likely if the age is under 28 and there are fewer than 2 academic experiences
            if (ageAtStartDate < 28 && experiencesCompleted.stream().filter(prior -> !prior.getTrack().equals("acadmic")).count() < 2) {
                for (PersonalExperienceType possible : liklihoods.keySet()) {
                    if (possible.getTrack().equals("academic")) liklihoods.merge(possible, 5.0, Double::sum);
                }
            }
            personalExperienceType = RandomUtils.weightedSelect(liklihoods);
            assert (personalExperienceType != null);
        }
        // Choose a tenure length which does not overlap with the next experience
        double minTenure = personalExperienceType.getMinTenure(), maxTenure = personalExperienceType.getMaxTenure();
        double tenureLength = RandomUtils.randSamplePDF(RandomUtils.skewedDistribution(personalExperienceType.getAvgTenure(), minTenure, maxTenure), minTenure, maxTenure);
        LocalDate experienceEndDate = experienceStartDate.plusDays((int) (tenureLength * TimeUtils.daysInYear));
        if (!experienceEndDate.isBefore(nextStartDate)) { // Use Not Is Before instead of Is After to handle possibility they are the same day
            if (nextStartDate.equals(engine.TIME_MANAGER.getCurrentDate().toLocalDate())) experienceEndDate = null;
            else experienceEndDate = nextStartDate.minusDays(1);
        }

        // Build the experienceEntry
        personalExperienceHistory.add(personalExperienceType, experienceStartDate, experienceEndDate);
    }

}
