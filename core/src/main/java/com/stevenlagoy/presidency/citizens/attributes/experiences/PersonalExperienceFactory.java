package com.stevenlagoy.presidency.citizens.attributes.experiences;

import com.stevenlagoy.presidency.util.CompletionMethodFor;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import com.stevenlagoy.presidency.util.RandomUtils;
import com.stevenlagoy.presidency.util.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PersonalExperienceFactory extends EntityFactory<PersonalExperience, PersonalExperienceFactory.PersonalExperienceContext> {

    /**
     * Context for creating or selecting a {@link PersonalExperience}. One of the following is required for imputation:
     * {@link #type}, {@link #startDate}, {@link #endDate}.
     */
    public static class PersonalExperienceContext extends EntityFactory.EntityContext<PersonalExperience> {

        /** Date of birth of a person who could have an experience with this context. */
        protected final @NotNull LocalDate birthDate;
        /** Latest possible date for experiences, may be current date or date of death. */
        protected final @NotNull LocalDate latestDate;
        /** Experience history of a person who could have an experience with this context. */
        protected final @NotNull PersonalExperienceHistory history;
        /** Type of the experience. */
        protected @Nullable PersonalExperienceType type;
        /** Start date of the experience. */
        protected @Nullable LocalDate startDate;
        /** End date of the experience. */
        protected @Nullable LocalDate endDate;
        /** Sentinel value which indicates that the endDate is actually {@code null}. */
        protected static final LocalDate endDateNullSentinel = LocalDate.MIN;
        /** Whether the experience should be considered complete when added to a person's history. */
        protected @Nullable Boolean isComplete;

        public PersonalExperienceContext(
            @NotNull Engine engine,
            @NotNull LocalDate birthDate,
            @NotNull LocalDate latestDate,
            @NotNull PersonalExperienceHistory history,
            @Nullable PersonalExperienceType type,
            @Nullable LocalDate startDate,
            @Nullable LocalDate endDate,
            @Nullable Boolean isComplete
        ) {
            super(engine);
            this.birthDate = birthDate;
            this.latestDate = latestDate;
            this.history = history;
            this.type = type;
            this.startDate = startDate;
            this.endDate = endDate;
            this.isComplete = isComplete;
        }

        @Override
        public @NotNull String getImputationFailureConditionsMessage() throws NoSuchFieldException {
            return String.format(
                "At least one of the following is required: %s, %s, %s.",
                this.getClass().getDeclaredField("name").getName(),
                this.getClass().getDeclaredField("startDate").getName(),
                this.getClass().getDeclaredField("endDate").getName()
            );
        }

        @CompletionMethodFor("type")
        protected @Nullable PersonalExperienceType selectType() {
            PersonalExperienceType type = this.type;
            if (type == null) {
                Set<PersonalExperienceType> possibleTypes = new HashSet<>();
                for (PersonalExperienceType candidate : engine.getManager(PersonalExperienceTypeManager.class).getAll()) {
                    if (candidate.arePrerequisitesMet(history)) possibleTypes.add(candidate);
                }
                if (startDate != null) {
                    double startAge = TimeUtils.yearsBetween(birthDate, startDate);
                    possibleTypes.removeIf((candidate) -> candidate.getMinAge() > startAge);
                    LocalDate finalStartDate = startDate;
                    possibleTypes.removeIf((candidate) -> !candidate.arePrerequisitesMet(history.getExperiencesStartingBefore(finalStartDate)));
                }
                else if (endDate != null) {
                    double endAge = TimeUtils.yearsBetween(birthDate, endDate);
                    possibleTypes.removeIf((candidate) -> candidate.getMaxAge() < endAge + candidate.getMinTenureYears());
                }
                else return null; // Unable to impute
                if (startDate != null && endDate != null) {
                    double tenureYears = TimeUtils.yearsBetween(startDate, endDate);
                    possibleTypes.removeIf((candidate) -> candidate.getMinTenureYears() < tenureYears);
                }
                Map<PersonalExperienceType, Double> weights = new HashMap<>();
                for (PersonalExperience prior : history) {
                    prior.getType().getConnections().forEach((conn, weight) -> {
                        if (possibleTypes.contains(conn)) {
                            weights.merge(conn, weight, Double::sum);
                        }
                    });
                }
                type = RandomUtils.weightedSelect(weights);
            }
            return type;
        }

        @CompletionMethodFor("startDate")
        protected @Nullable LocalDate selectStartDate() {
            LocalDate startDate = this.startDate;
            if (startDate == null) {
                if (type == null) return null; // Unable to impute

                // Choose a tenure duration
                double min = type.getMinTenureYears(), avg = type.getAvgTenureYears(), max = type.getMaxTenureYears();
                double tenureYears = RandomUtils.randSamplePDF(RandomUtils.triangularPDF(min, avg, max), min, max, max);
                if (endDate != null) { // Have an end date but not a start date
                    LocalDate earliestPossible = history.getNearestEndDateBefore(endDate);
                    if (earliestPossible == null) earliestPossible = birthDate.plusYears(18L);
                    if (TimeUtils.yearsBetween(earliestPossible, endDate) > type.getMaxTenureYears()) {
                        earliestPossible = endDate.minusDays((long) (type.getMaxTenureYears() * TimeUtils.daysInYear)); // Truncating is fine
                    }
                    // Subtract the tenure years from the end date
                    LocalDate selected = endDate.minusDays((long) (tenureYears * TimeUtils.daysInYear));
                    // Ensure the selected date is after the earliest possible date
                    if (selected.isBefore(earliestPossible)) selected = earliestPossible;
                    startDate = selected;
                }
                else { // Both start and end date are null
                    // Choose the largest gap
                }
                // Choose an acceptable start date before the end date.
                // Must agree with the max tenure specified by the type.
                // Must agree with the minimum age specified by the type.
            }
            return startDate;
        }

        @CompletionMethodFor("endDate")
        protected @Nullable LocalDate selectEndDate() {
            LocalDate endDate = this.endDate;
            if (endDate == null) {
                if (type == null) return null; // Unable to impute
            }
            return endDate;
        }

        @CompletionMethodFor("isComplete")
        protected @Nullable Boolean determineIsComplete() {
            Boolean isComplete = this.isComplete;
            if (isComplete == null) {
                if (type == null || startDate == null || endDate == null) return null; // Unable to impute
            }
            return isComplete;
        }
    }

    public PersonalExperienceFactory(@NotNull Engine engine, @NotNull PersonalExperienceManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable PersonalExperience doCreation(@NotNull PersonalExperienceContext context) {
        if (context.endDate == PersonalExperienceContext.endDateNullSentinel) context.endDate = null;
        return new PersonalExperience(engine, context.type, context.startDate, context.endDate, context.isComplete);
    }

    @Override
    protected @Nullable PersonalExperience doSelection(@NotNull PersonalExperienceContext context) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Selection is invalid for type " + PersonalExperience.class.getSimpleName());
    }

}
