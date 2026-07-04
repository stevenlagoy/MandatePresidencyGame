package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.characters.attributes.experiences.Experience;
import com.stevenlagoy.presidency.characters.attributes.experiences.ExperienceHistory;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePaths;
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
 *     <b>Author:  </b> Steven LaGoy             <br>
 *     <b>Created: </b> 09 June 2025 at 12:48 AM <br>
 *     <b>Modified:</b> 09 June 2026             <br>
 * </p>
 *
 * ExperienceManager is responsible for creating and tracking Experiences and ExperienceHistories.
 *
 * @author Steven LaGoy
 */
public class ExperienceManager extends Manager {

    // Constants

    public static final int maxGapYearsBetweenExperiences = 3;
    public static final int minAgeForExperience = 18;

    // Instance Fields

    Set<Experience> experiences;

    // Constructors

    public ExperienceManager(@NotNull Engine engine, @Nullable Manager superManager) {
        super(engine, superManager);
        experiences = new HashSet<>();
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
        experiences.clear();
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
            JSONObject experiencesJson = new JSONObject(FilePaths.EXPERIENCES);
            for (Object obj : experiencesJson.requireArray()) {
                if (obj instanceof JSONObject experienceJson) {
                    experiences.add(new Experience(engine, experienceJson));
                }
            }
            resolveConnections();
        } catch (IOException e) {
            onError(e);
        }
    }

    private void resolveConnections() {
        requireState(ManagerState.INITIALIZING);
        try {
            JSONObject experiencesJson = new JSONObject(FilePaths.EXPERIENCES);
            for (Object obj : experiencesJson.requireArray()) {
                if (obj instanceof JSONObject experienceJson) {
                    Experience experience = matchExperience(experienceJson.getKey()).orElseThrow();
                    for (Object connectionObj : experienceJson.requireArray("connections")) {
                        if (connectionObj instanceof JSONObject connection) {
                            try {
                                experience.getConnections().put(matchExperience(connection.getKey()).orElseThrow(), connection.requireNumber().doubleValue());
                            } catch (Exception e) {
                                Logger.error("Could not make connection '%s' for experience '%s': %s. Check keys in %s.", connection.getKey(), experience.getLabel(), e.getMessage(), FilePaths.EXPERIENCES);
                            }
                        }
                        else throw new RuntimeException("Encountered an unexpected value in " + FilePaths.EXPERIENCES);
                    }
                }
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    public @NotNull Optional<Experience> matchExperience(@NotNull String experienceName) {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        Optional<Experience> res;
        res = experiences.stream().filter(experience -> experience.getLabel().equalsIgnoreCase(experienceName)).findFirst();
        if (res.isEmpty()) res = experiences.stream().filter(experience -> experience.getName().replace(" ", "_").equalsIgnoreCase(experienceName.replace(" ", "_"))).findFirst();
        return res;
    }

    public @NotNull Set<Experience> getExperiences() {
        requireState(ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED, ManagerState.INITIALIZING);
        return experiences;
    }

    public @NotNull Set<Experience> getExperiences(@NotNull String track) {
        requireOperational();
        return experiences.stream().filter(experience -> experience.getTrack().equals(track)).collect(Collectors.toSet());
    }

    public @NotNull Set<Experience> getExperiences(int age) {
        requireOperational();
        return experiences.stream().filter(experience -> experience.getMinAge() <= age).collect(Collectors.toSet());
    }

    public @NotNull Set<Experience> getExperiences(@NotNull Collection<Experience> priors) {
        requireOperational();
        return experiences.stream().filter(experience -> experience.prerequisitsMet(priors)).collect(Collectors.toSet());
    }

    public @NotNull Set<Experience> getExperiences(@NotNull Collection<Experience> priors, int age) {
        requireOperational();
        return getExperiences(priors).stream().filter(experience -> experience.getMinAge() <= age && experience.getMaxAge() >= age).collect(Collectors.toSet());
    }

    public @NotNull ExperienceHistory buildExperienceHistory(LocalDate experienceHistoryStartDate) {
        requireOperational();
        ExperienceHistory experienceHistory = new ExperienceHistory(engine, new TreeSet<>());
        fillExperienceHistory(experienceHistoryStartDate, experienceHistory);
        return experienceHistory;
    }

    public void fillExperienceHistory(LocalDate experienceHistoryStartDate, @NotNull ExperienceHistory experienceHistory) {
        requireOperational();
        double targetCompleteness = chooseTargetExperienceHistoryCompleteness();
        final long maxAttempts = 25;
        long attempts = 0;
        while (experienceHistory.getTotalOccupiedYears() / engine.TIME_MANAGER.yearsAgo(experienceHistoryStartDate) < targetCompleteness && attempts++ < maxAttempts) {
            addOneExperienceToExperienceHistory(experienceHistoryStartDate, experienceHistory);
        }
    }

    private double chooseTargetExperienceHistoryCompleteness() {
        requireOperational();
        return RandomUtils.randNextFloat(0.65f, 0.9f);
    }

    public void addOneExperienceToExperienceHistory(LocalDate experienceHistoryStartDate, @NotNull ExperienceHistory experienceHistory) {
        requireOperational();

        Experience experience = null;

        // Check if there's an experience which is unfulfilled
        boolean foundRootExperience = false;
        for (ExperienceHistory.ExperienceEntry experienceEntry : experienceHistory.getExperiences().descendingSet()) {
            experience = experienceEntry.getExperience();
            while (!experience.prerequisitsMet(
                experienceHistory.getExperiencesBefore(experienceEntry.getStartDate()).stream().map(ExperienceHistory.ExperienceEntry::getExperience).collect(Collectors.toSet())
            )) {
                foundRootExperience = true;
                // Choose a prerequisite
                experience = RandomUtils.randSelect(experience.getPrerequisites());
                assert(experience != null); // Can only be null in the case there are no prerequisites, which is impossible by the loop condition
            }
            if (foundRootExperience) break;
        }
        if (!foundRootExperience) experience = null;

        // Find a gap of sufficient size between experiences
        LocalDate prevEndDate = experienceHistoryStartDate, nextStartDate = null;
        boolean foundGap = false;
        for (ExperienceHistory.ExperienceEntry experienceEntry : experienceHistory.getExperiences()) {
            nextStartDate = experienceEntry.getStartDate();
            assert(prevEndDate != null); // The only null end date should be the final experienceEntry, so this state is impossible
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
        if (experience == null) {
            // Get all the experiences completed before that date
            List<Experience> experiencesCompleted = experienceHistory.getExperiencesBefore(experienceStartDate).stream().map(ExperienceHistory.ExperienceEntry::getExperience).collect(Collectors.toList());
            // Get all valid experiences with those completed experiences and the age
            Set<Experience> possibleExperiences = getExperiences(experiencesCompleted, ageAtStartDate);
            // Rank possible experiences using the connections
            Map<Experience, Double> liklihoods = new HashMap<>();
            possibleExperiences.forEach(possible -> liklihoods.put(possible, possible.getBaseChance()));
            for (Experience completed : experiencesCompleted) {
                for (var entry : completed.getConnections().entrySet()) {
                    liklihoods.merge(entry.getKey(), entry.getValue(), Double::sum);
                }
            }
            liklihoods.keySet().removeIf(key -> !possibleExperiences.contains(key) || (experiencesCompleted.contains(key) && !key.isRepeatable()));
            // Make academic experiences much more likely if the age is under 28 and there are fewer than 2 academic experiences
            if (ageAtStartDate < 28 && experiencesCompleted.stream().filter(prior -> !prior.getTrack().equals("acadmic")).count() < 2) {
                for (Experience possible : liklihoods.keySet()) {
                    if (possible.getTrack().equals("academic")) liklihoods.merge(possible, 5.0, Double::sum);
                }
            }
            experience = RandomUtils.weightedSelect(liklihoods);
            assert(experience != null);
        }
        // Choose a tenure length which does not overlap with the next experience
        double minTenure = experience.getMinTenure(), maxTenure = experience.getMaxTenure();
        double tenureLength = RandomUtils.randSamplePDF(RandomUtils.skewedDistribution(experience.getAvgTenure(), minTenure, maxTenure), minTenure, maxTenure);
        LocalDate experienceEndDate = experienceStartDate.plusDays((int) (tenureLength * TimeUtils.daysInYear));
        if (!experienceEndDate.isBefore(nextStartDate)) { // Use Not Is Before instead of Is After to handle possibility they are the same day
            if (nextStartDate.equals(engine.TIME_MANAGER.getCurrentDate().toLocalDate())) experienceEndDate = null;
            else experienceEndDate = nextStartDate.minusDays(1);
        }

        // Build the experienceEntry
        experienceHistory.add(experience, experienceStartDate, experienceEndDate);
    }

}
