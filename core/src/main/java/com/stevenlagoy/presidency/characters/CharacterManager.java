package com.stevenlagoy.presidency.characters;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.characters.attributes.CharacterAppearance;
import com.stevenlagoy.presidency.characters.attributes.Family;
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.core.TimeManager;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.map.Municipality;
import com.stevenlagoy.presidency.util.CollectionUtils;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.RandomUtils;
import kotlin.uuid.Uuid;
import kotlin.uuid.UuidKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

/**
 * <h1>CHARACTER MANAGER</h1>
 * {@code ~/characters/CharacterManager.java}
 * <p>
 *     <b>Author:</b>
 *     Steven LaGoy
 *     <br>
 *     <b>Created:</b>
 *     01 June 2025 at 1:04 AM
 *     <br>
 *     <b>Modified:</b>
 *     08 April 2026
 * </p>
 * CharacterManager is responsible for creating and maintaining information about Characters.
 */
public class CharacterManager extends Manager {

    // CONSTANTS ----------------------------------------------------------------------------------

    // Percentage for each person in a family up to 12 people
    public static final float coreFamilySizePercentage = 0.95f;
    // Percentage for each person in a family from 13 to 24 people
    public static final float nearFamilySizePercentage = 0.60f;
    // Percentage for each person in a family from 25 to 36 people
    public static final float distantFamilySizePercentage = 0.25f;
    public static final float hasMotherPercent = 0.99f;
    public static final float hasFatherPercent = 0.98f;
    // Should be based on age, percentage of married people who have children
    public static final float eachChildPercent = 0.80f;
    // Should be based on age
    public static final float marriedPercent = 0.60f;

    // INSTANCE FIELDS ----------------------------------------------------------------------------

    private Map<String, Double> birthdateDistribution;
    private Map<Bloc, Map<Integer, Double>> ageDistribution;

    /** The player candidate character. */
    private PlayerCharacter playerCharacter;

    /** List of tracked Character instances. */
    private final Set<Citizen> citizens = new HashSet<>();

    private final Engine ENGINE;
    private ManagerState currentState;

    // CONSTRUCTOR --------------------------------------------------------------------------------

    public CharacterManager(Engine engine) {
        ENGINE = engine;
        currentState = ManagerState.INACTIVE;
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    @Override
    public boolean init() {
        readBirthdateDistributionData();
        readAgeDistributionData();
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

    // INSTANCE METHODS ---------------------------------------------------------------------------

    public PlayerCharacter getPlayer() {
        return playerCharacter;
    }

    public Set<Citizen> getCitizens() {
        return citizens;
    }

    public int getNumCitizens() {
        return citizens.size();
    }

    public @Nullable Citizen getById(Uuid id) {
        return citizens.stream().filter(citizen -> citizen.getId().equals(id)).findFirst().orElse(null);
    }

    private void readBirthdateDistributionData() {
        JSONObject json = JSONProcessor.processJson(FilePaths.BIRTHDATE_POPULARITIES);
        birthdateDistribution = new HashMap<>();
        for (Object dateObj : json.getAsList()) {
            if (dateObj instanceof JSONObject dateJson) {
                String date = dateJson.getKey();
                double value = dateJson.getAsNumber().doubleValue();
                birthdateDistribution.put(date, value);
            }
        }
    }

    private void readAgeDistributionData() {
        JSONObject json = JSONProcessor.processJson(FilePaths.BIRTHYEAR_PERCENTAGES);
        ageDistribution = new HashMap<>();
        for (Object blocObj : json.getAsList()) {
            if (blocObj instanceof JSONObject blocJson) {
                Bloc key = ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.matchBlocName(blocJson.getKey());
                Map<Integer, Double> distribution = new HashMap<>();
                for (Object dataObj : blocJson.getAsList()) {
                    if (dataObj instanceof JSONObject dataJson) {
                        int year = Integer.parseInt(dataJson.getKey());
                        double value = dataJson.getAsNumber().doubleValue();
                        distribution.put(year, value);
                    }
                }
                ageDistribution.put(key, distribution);
            }
        }
    }

    private @NotNull Map<Integer, Double> getAgeDistribution(@NotNull Demographics demographics) {
        return getAgeDistribution(demographics.getBlocs());
    }

    private @NotNull Map<Integer, Double> getAgeDistribution(@NotNull Set<Bloc> blocs) {
        if (blocs.isEmpty()) blocs = ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.getCommonDemographics().getBlocs();
        final Map<Integer, Double> distributionsSum = new HashMap<>();
        double totalPercentages = 0.0;
        // Add together all bloc distributions
        for (Bloc bloc : blocs) {
            if (!ageDistribution.containsKey(bloc)) continue; // Only consider valid blocs
            Map<Integer, Double> blocDistribution = ageDistribution.get(bloc);
            blocDistribution.forEach((year, dist) -> distributionsSum.compute(year, (k, prior) -> dist + ((prior != null) ? prior : 0.0)));
        }
        // Normalize and return
        return CollectionUtils.normalize(distributionsSum);
    }

    // JSONIC -------------------------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        return new JSONObject("CharacterManager", Map.of(

        ));
    }

    @Override
    public CharacterManager fromJson(JSONObject json) {
        return this;
    }

    // CITIZEN BUILDER --------------------------------------------------------------------------

    public @NotNull Citizen buildCitizen() {
        return buildCitizen(chooseFamilySize(), this::buildFamily);
    }

    public @NotNull Citizen buildCitizen(int familySize) {
        return buildCitizen(familySize, this::buildFamily);
    }

    public @NotNull Citizen buildCitizenWithoutSpouseOrChildren(int familySize) {
        return buildCitizen(familySize, this::buildFamilyWithoutSpouseOrChildren);
    }

    public @NotNull Citizen buildCitizenWithoutParents(int familySize) {
        return buildCitizen(familySize, this::buildFamilyWithoutParents);
    }

    public @NotNull Citizen buildCitizen(int familySize, Function<Integer, Family> familyBuilder) {
        // Generate fields
        Demographics demographics = ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.selectDemographics();
        LocalDate birthday = selectBirthday(demographics);
        int age = ENGINE.MANAGERS.TIME_MANAGER.yearsAgo(birthday);
        Family family = familyBuilder.apply(familySize);
        Municipality origin = ENGINE.MANAGERS.MAP_MANAGER.selectMunicipality(demographics);
        Municipality residence = ENGINE.MANAGERS.MAP_MANAGER.selectMunicipality();
        PersonalName name = ENGINE.MANAGERS.NAME_MANAGER.buildPersonalName(demographics, age, family, origin);
        CharacterAppearance appearance = buildAppearance(demographics, age);
        // Build the citizen
        Citizen citizen = new Citizen(
            ENGINE.MANAGERS,
            Uuid.Companion.random(),
            name,
            birthday,
            demographics,
            appearance,
            family,
            origin,
            residence, // Current location
            residence,
            null
        );
        fillRelationships(family, citizen);
        ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.addCharacterToBlocs(citizen);
        return citizen;
    }

    private @NotNull LocalDate selectBirthday(@NotNull Demographics demographics) {
        // Select a year
        int age;
        do {
            var selected = RandomUtils.weightedSelect(getAgeDistribution(demographics));
            assert selected != null;
            age = selected;
        } while (age < Citizen.MIN_AGE || age > Citizen.MAX_AGE);
        int year = ENGINE.MANAGERS.TIME_MANAGER.dateYearsAgo(age).getYear();

        // Select day and month
        int month, day;
        do {
            String date = RandomUtils.weightedSelect(birthdateDistribution);
            assert date != null;
            month = Integer.parseInt(date.split("-")[0]);
            day = Integer.parseInt(date.split("-")[1]);
        } while (day == 28 && month == 2 && !TimeManager.isLeapYear(year));

        return LocalDate.of(year, month, day);
    }

    // FAMILY BUILDER -----------------------------------------------------------------------------

    private int chooseFamilySize() {
        int familySize = 0;
        familySize += RandomUtils.probabilisticCount(coreFamilySizePercentage);
        if (familySize < 12) return familySize;
        else familySize = 12;
        familySize += RandomUtils.probabilisticCount(nearFamilySizePercentage);
        if (familySize < 24) return familySize;
        else familySize = 24;
        familySize += RandomUtils.probabilisticCount(distantFamilySizePercentage);
        if (familySize > 36) familySize = 36;
        return familySize;
    }

    public @NotNull Family buildFamily(int numberMembers) {
        return buildFamily(numberMembers, true, true);
    }

    public @NotNull Family buildFamily(int numberMembers, boolean mayHaveParents, boolean mayHaveSpouseAndChildren) {
        if (!mayHaveParents && !mayHaveSpouseAndChildren) throw new IllegalArgumentException("Cannot create a family with no parents or spouse");
        boolean hasMother, hasFather, hasSpouse;
        hasMother = mayHaveParents && RandomUtils.chance(hasMotherPercent);
        hasFather = mayHaveParents && RandomUtils.chance(hasFatherPercent);
        hasSpouse = mayHaveSpouseAndChildren && RandomUtils.chance(marriedPercent);
        int numberChildren = hasSpouse ? RandomUtils.probabilisticCount(eachChildPercent) : 0;
        int accounted = (hasMother ? 1 : 0) + (hasFather ? 1 : 0) + (hasSpouse ? 1 : 0) + numberChildren;
        // Make sure we have at least two family members
        while (accounted < 2) {
            if (mayHaveParents && !hasMother) hasMother = true;
            else if (mayHaveParents && !hasFather) hasFather = true;
            else if (mayHaveSpouseAndChildren && !hasSpouse) hasSpouse = true;
            accounted += 1;
        }
        // Make sure we don't already exceed the desired number of members
        while (accounted > numberMembers) {
            if (numberChildren > 0)
                numberChildren -= 1;
            else if (hasSpouse) hasSpouse = false;
            else if (hasFather) hasFather = false;
            else if (hasMother) hasMother = false;
            accounted -= 1;
        }
        // Distribute remaining number of family members between branches
        int spousesSidePortion, mothersSidePortion, fathersSidePortion, totalChildrenPortion;
        spousesSidePortion = (hasSpouse ? 1 : 0) * RandomUtils.nextInt(2, (numberMembers - accounted - 1) / 3);
        mothersSidePortion = (hasMother ? 1 : 0) * RandomUtils.nextInt(0, (numberMembers - accounted - 1) / 6);
        fathersSidePortion = (hasFather ? 1 : 0) * RandomUtils.nextInt(0, (numberMembers - accounted - 1) / 6);
        totalChildrenPortion = (hasSpouse ? 1 : 0) * RandomUtils.nextInt(0, (numberMembers - accounted - 1) / 3);
        accounted = (hasMother ? 1 : 0) + (hasFather ? 1 : 0) + (hasSpouse ? 1 : 0) + numberChildren;
        while (accounted < numberMembers) {
            spousesSidePortion += hasSpouse && RandomUtils.chance(0.30f) ? 1 : 0;
            mothersSidePortion += hasMother && RandomUtils.chance(0.15f) ? 1 : 0;
            fathersSidePortion += hasFather && RandomUtils.chance(0.15f) ? 1 : 0;
            totalChildrenPortion += hasSpouse && RandomUtils.chance(0.30f) ? 1 : 0;
            accounted = (hasMother ? 1 : 0) + (hasFather ? 1 : 0) + (hasSpouse ? 1 : 0) + numberChildren +
                spousesSidePortion + mothersSidePortion + fathersSidePortion + totalChildrenPortion;
        }
        // Reduce if we assigned too many
        while (accounted > numberMembers) {
            if (accounted % 2 == 0)
                totalChildrenPortion -= 1;
            else
                spousesSidePortion -= 1;
            accounted -= 1;
        }
        assert(accounted == numberMembers);

        // Apportion among children
        int[] childrenFamilySize = new int[numberChildren];
        int childrenApportioned = 0;
        while (childrenApportioned < totalChildrenPortion) {
            for (int i = 0; i < numberChildren; i++) {
                boolean shouldAdd = RandomUtils.chance(1.0f / numberChildren);
                if (shouldAdd) {
                    childrenFamilySize[i]++;
                    childrenApportioned++;
                }
            }
        }
        // Make the family tree
        Citizen mother = null, father = null, spouse = null;
        Set<Citizen> parents = new HashSet<>();
        Set<Citizen> children = new HashSet<>();
        if (hasMother) {
            mother = buildCitizen(mothersSidePortion);
            parents.add(mother);
        }
        if (hasFather) {
            father = buildCitizen(fathersSidePortion);
            parents.add(father);
        }
        if (hasSpouse) {
            spouse = buildCitizenWithoutSpouseOrChildren(spousesSidePortion);
        }
        for (int i = 0; i < numberChildren; i++) {
            Citizen child = buildCitizenWithoutParents(childrenFamilySize[i]);
            children.add(child);
        }
        return new Family(parents, spouse, children);
    }

    public @NotNull Family buildFamilyWithoutSpouseOrChildren(int numberMembers) {
        return buildFamily(numberMembers, true, false);
    }

    public @NotNull Family buildFamilyWithoutParents(int numberMembers) {
        return buildFamily(numberMembers, false, true);
    }

    public void fillRelationships(@NotNull Family family, @NotNull Citizen citizen) {
        family.getParents().forEach(p -> p.getFamily().getChildren().add(citizen));
        if (family.getSpouse() != null) {
            family.getSpouse().getFamily().setSpouse(citizen);
            family.getChildren().forEach(c -> c.getFamily().getParents().add(family.getSpouse()));
        }
        family.getChildren().forEach(c -> c.getFamily().getParents().add(citizen));
    }

    // APPEARANCE BUILDER -------------------------------------------------------------------------

    public @NotNull CharacterAppearance buildAppearance(@NotNull Demographics demographics, int age) {
        return new CharacterAppearance(age);
    }

}
