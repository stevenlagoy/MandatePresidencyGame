package com.stevenlagoy.presidency.characters;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.characters.attributes.*;
import com.stevenlagoy.presidency.characters.attributes.experiences.ExperienceHistory;
import com.stevenlagoy.presidency.characters.attributes.names.NameManager;
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.map.Municipality;
import com.stevenlagoy.presidency.politics.PoliticalAlignment;
import com.stevenlagoy.presidency.util.CollectionUtils;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.RandomUtils;
import com.stevenlagoy.presidency.util.TimeUtils;
import kotlin.uuid.Uuid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <h1>CHARACTER MANAGER</h1>
 * {@code ~/characters/CharacterManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy            <br>
 *     <b>Created: </b> 01 June 2025 at 1:04 AM <br>
 *     <b>Modified:</b> 04 June 2026            <br>
 * </p>
 *
 * CharacterManager is responsible for creating and maintaining information about {@link Citizen},
 * {@link PoliticalActor}, and {@link PlayerCharacter}.
 *
 * @author Steven LaGoy
 */
public class CharacterManager extends Manager {

    // Constants

    /** Percentage of people who are female. */
    public static final double femalePercentage = 0.507;
    /** Percentage of people who are intersex, per <a href="https://pubmed.ncbi.nlm.nih.gov/12476264/">NIH</a>. */
    public static final double intersexPercentage = 0.00018;
    /** Percentage of people who are male. */
    public static final double malePercentage = 1 - femalePercentage - intersexPercentage;

    // Instance Fields

    private Map<String, Double> birthdateDistribution;
    private Map<Bloc, Map<Integer, Double>> ageDistribution;

    /** The player candidate character. */
    private PlayerCharacter playerCharacter;

    /** List of tracked Character instances. */
    private final Set<Citizen> citizens = new HashSet<>();

    public final NameManager NAME_MANAGER;
    public final SkillsManager SKILLS_MANAGER;
    public final AppearanceManager APPEARANCE_MANAGER;
    public final PersonalityManager PERSONALITY_MANAGER;
    public final FamilyManager FAMILY_MANAGER;
    public final ExperienceManager EXPERIENCE_MANAGER;

    // Constructors

    public CharacterManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        NAME_MANAGER        = new NameManager(engine, this);
        SKILLS_MANAGER      = new SkillsManager(engine, this);
        APPEARANCE_MANAGER  = new AppearanceManager(engine, this);
        PERSONALITY_MANAGER = new PersonalityManager(engine, this);
        FAMILY_MANAGER      = new FamilyManager(engine, this);
        EXPERIENCE_MANAGER  = new ExperienceManager(engine, this);
        for (Manager manager : getSubManagers()) {
            if (manager.getState().equals(ManagerState.ERROR)) {
                onError(new Exception(manager.getClass().getSimpleName() + " could not be constructed."));
            }
        }
    }

    // Manager Methods

    @Override
    public @NotNull Set<Manager> getSubManagers() {
        return Set.of(NAME_MANAGER, SKILLS_MANAGER, APPEARANCE_MANAGER, PERSONALITY_MANAGER, FAMILY_MANAGER, EXPERIENCE_MANAGER);
    }

    @Override
    protected void doInit() {
        readBirthdateDistributionData();
        readAgeDistributionData();
    }

    @Override
    protected void doCleanup() {
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        getSubManagers().forEach(manager -> manager.fromJson(json.get(manager.getClass().getSimpleName(), JSONObject.class)));
    }

    // Instance Methods

    // Read

    private void readBirthdateDistributionData() {
        requireState(ManagerState.INITIALIZING);
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
        requireState(ManagerState.INITIALIZING);
        JSONObject json = JSONProcessor.processJson(FilePaths.BIRTHYEAR_PERCENTAGES);
        ageDistribution = new HashMap<>();
        for (Object blocObj : json.getAsList()) {
            if (blocObj instanceof JSONObject blocJson) {
                Bloc key = ENGINE.DEMOGRAPHICS_MANAGER.matchBlocName(blocJson.getKey());
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

    // Player

    public PlayerCharacter getPlayer() {
        requireOperational();
        return playerCharacter;
    }

    // Citizens

    public Set<Citizen> getCitizens() {
        requireOperational();
        return citizens;
    }

    public int getNumCitizens() {
        requireOperational();
        return citizens.size();
    }

    public @Nullable Citizen matchCitizenById(String id) throws IllegalArgumentException {
        requireOperational();
        return matchCitizenById(Uuid.Companion.parse(id));
    }

    public @Nullable Citizen matchCitizenById(Uuid id) {
        requireOperational();
        return citizens.stream().filter(citizen -> citizen.getId().equals(id)).findFirst().orElse(null);
    }

    /** Context for the creation of citizens and related attributes. */
    public static class CitizenContext {
        // Originally made this a record, but immutability was inconvenient for filling in fields
        final Engine ENGINE;
        Sex sex;
        Demographics demographics;
        LocalDate birthday;
        Family family;
        CharacterAppearance appearance;
        PersonalName name;
        Municipality origin;
        Municipality residence;

        public CitizenContext(
            @NotNull Engine ENGINE,
            Sex sex,
            Demographics demographics,
            LocalDate birthday,
            Family family,
            CharacterAppearance appearance,
            PersonalName name,
            Municipality origin,
            Municipality residence
        ) {
            this.ENGINE = ENGINE;
            this.sex = sex;
            this.demographics = demographics;
            this.birthday = birthday;
            this.family = family;
            this.appearance = appearance;
            this.name = name;
            this.origin = origin;
            this.residence = residence;
        }

        public CitizenContext(@NotNull Engine ENGINE, @NotNull Citizen citizen) { this(
            ENGINE,
            citizen.getSex(),
            citizen.getDemographics(),
            citizen.getBirthday(),
            citizen.getFamily(),
            citizen.getAppearance(),
            citizen.getName(),
            citizen.getOrigin(),
            citizen.getResidence()
        ); }

        public int getAge() {
            return ENGINE.TIME_MANAGER.yearsAgo(birthday);
        }

        public static @NotNull CitizenContext emptyContext(@NotNull Engine ENGINE) {
            return new CitizenContext(ENGINE, null, null, null, null, null, null, null, null);
        }
    }

    public @NotNull Citizen buildCitizen() {
        return buildCitizen(true);
    }

    public @NotNull Citizen buildCitizen(boolean addToCharactersList) {
        return buildCitizen(CitizenContext.emptyContext(this.ENGINE), addToCharactersList);
    }

    public @NotNull Citizen buildCitizen(@NotNull CitizenContext context) {
        return buildCitizen(context, true);
    }

    public @NotNull Citizen buildCitizen(@NotNull CitizenContext context, boolean addToCharactersList) {
        requireOperational();

        if (context.sex == null) {
            context.sex = selectSex();
        }
        if (context.demographics == null) {
            Demographics selectedDemographics = ENGINE.DEMOGRAPHICS_MANAGER.selectDemographics();
            if (context.birthday != null) {
                selectedDemographics.setGeneration(ENGINE.DEMOGRAPHICS_MANAGER.getGenerationForBirthday(context.birthday));
            }
            context.demographics = selectedDemographics;
        }
        if (context.birthday == null) {
            context.birthday = selectBirthday(context.demographics);
        }
        if (context.family == null) {
            // This will be done later, after constructing the citizen
            // context.family = FAMILY_MANAGER.planFamily(context);
        }
        if (context.appearance == null) {
            context.appearance = APPEARANCE_MANAGER.generateAppearance(context.demographics, ENGINE.TIME_MANAGER.yearsAgo(context.birthday));
        }
        if (context.name == null) {
            // This will be done later, after building the family
            // context.name = NAME_MANAGER.buildPersonalName(new NameManager.NameContext(context.demographics, ENGINE.TIME_MANAGER.yearsAgo(context.birthday), null));
        }
        if (context.origin == null) {
            context.origin = ENGINE.MAP_MANAGER.selectMunicipality(context.demographics);
        }
        if (context.residence == null) {
            context.residence = ENGINE.MAP_MANAGER.selectMunicipality(context.demographics);
        }

        Citizen citizen = new Citizen(
            ENGINE,
            context.sex,
            context.birthday,
            context.demographics,
            context.family != null ? context.family : new Family(ENGINE, null, null, null, new HashSet<>()), // Built afterward
            context.appearance,
            context.name != null ? context.name : NAME_MANAGER.emptyName(context.demographics, context.getAge(), context.family),
            context.origin,
            context.residence,
            context.residence,
            null
        );
        if (context.family == null) {
            context.family = FAMILY_MANAGER.buildFamily(citizen, FAMILY_MANAGER.planFamily(citizen));
            // Copy it in
            citizen.getFamily().copy(context.family);
        }
        if (context.name == null) {
            context.name = NAME_MANAGER.buildPersonalName(context.demographics, context.getAge(), context.family, context.origin, citizen.getName().getClass());
            // Copy it in
            citizen.getName().copy(context.name);
        }
        return citizen;
    }

    public @NotNull Sex selectSex() {
        Sex selected = RandomUtils.weightedSelect(Map.of(
            Sex.FEMALE, femalePercentage,
            Sex.INTERSEX, intersexPercentage,
            Sex.MALE, malePercentage
        ));
        assert(selected != null);
        return selected;
    }

    // Age Distribution

    private @NotNull Map<Integer, Double> getAgeDistribution(@NotNull Demographics demographics) {
        requireOperational();
        return getAgeDistribution(demographics.getBlocs());
    }

    private @NotNull Map<Integer, Double> getAgeDistribution(@NotNull Set<Bloc> blocs) {
        requireOperational();
        if (blocs.isEmpty()) blocs = ENGINE.DEMOGRAPHICS_MANAGER.getCommonDemographics().getBlocs();
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

    private @NotNull LocalDate selectBirthday(@NotNull Demographics demographics) {
        requireOperational();
        // Select a year
        int age;
        do {
            var selected = RandomUtils.weightedSelect(getAgeDistribution(demographics));
            assert selected != null;
            age = selected;
        } while (age < Citizen.MIN_AGE || age > Citizen.MAX_AGE);
        int year = ENGINE.TIME_MANAGER.dateYearsAgo(age).getYear();

        // Select day and month
        int month, day;
        do {
            String date = RandomUtils.weightedSelect(birthdateDistribution);
            assert date != null;
            month = Integer.parseInt(date.split("-")[0]);
            day = Integer.parseInt(date.split("-")[1]);
        } while (day == 28 && month == 2 && !TimeUtils.isLeapYear(year));

        return LocalDate.of(year, month, day);
    }

    public @NotNull PoliticalActor buildPoliticalActor() {
        Citizen citizen = buildCitizen(false);
        ExperienceHistory experiences = EXPERIENCE_MANAGER.buildExperienceHistory(citizen.getBirthday().plusYears(18));
        Skills skills = SKILLS_MANAGER.generateSkills();
        Personality personality = new Personality();
        PoliticalAlignment alignment = new PoliticalAlignment();
        IssuePositionMap issuePositions = new IssuePositionMap(new HashMap<>());
        return new PoliticalActor(
            ENGINE,
            citizen.getSex(),
            citizen.getBirthday(),
            citizen.getDemographics(),
            citizen.getFamily(),
            citizen.getAppearance(),
            citizen.getName(),
            citizen.getOrigin(),
            citizen.getLocation(),
            citizen.getResidence(),
            citizen.getFinancialProfile(),
            experiences,
            skills,
            personality,
            alignment,
            issuePositions,
            null,
            null
        );
    }
}
