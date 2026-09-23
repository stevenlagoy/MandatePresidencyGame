package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.characters.CharacterManager;
import com.stevenlagoy.presidency.characters.Citizen;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.demographics.DemographicCategory;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.RandomUtils;
import com.stevenlagoy.presidency.util.TimeUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <h1>FAMILY MANAGER</h1>
 * {@code ~/characters/attributes/FamilyManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy            <br>
 *     <b>Created: </b> 02 June 2026 at 2:46 PM <br>
 *     <b>Modified:</b> 04 June 2026            <br>
 * </p>
 *
 * FamilyManager is responsible for creating {@link Family} attributes.
 *
 * @author Steven LaGoy
 */
public class FamilyManager extends Manager {

    // Constants

    public static final float eachFamilyMemberPercent = 0.9715f; // Results in families around 24 members
    public static final float hasMotherPercent = 0.99f;
    public static final float hasFatherPercent = 0.98f;
    // Should be based on age, percentage of married people who have children
    public static final float eachChildPercent = 0.80f;
    // Should be based on age
    public static final float marriedPercent = 0.80f;
    /** Via <a href="https://usafacts.org/articles/what-is-the-state-of-gay-marriage-in-the-us/">USA Facts</a>*/
    public static final float wlwMarriagePercent = 0.013f;
    /** Via <a href="https://usafacts.org/articles/what-is-the-state-of-gay-marriage-in-the-us/">USA Facts</a>*/
    public static final float mlmMarriagePercent = 0.013f;
    public static final int minGenerationGapYears = 20; // Minimum number of years between generations
    public static final int maxGenerationGapYears = 50; // Maximum number of years between generations
    public static final int maxSpouseAgeGapYears = 10;

    public static final float mothersSideMaxPercentOfFamily = 0.25f;
    public static final float fathersSideMaxPercentOfFamily = 0.25f;
    public static final float siblingsMaxPercentOfFamily = 0.15f;
    public static final float allSiblingsSidesMaxPercentOfFamily = 0.20f;
    public static final float spousesSideMaxPercentOfFamily = 0.35f;
    public static final float allChildrensSidesMaxPercentOfFamily = 0.25f;


    // Constructor

    public FamilyManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
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
    }

    // Creational Methods

    public record FamilyPlan(
        boolean hasMother,
        int mothersSide,
        boolean hasFather,
        int fathersSide,
        int siblings,
        int allSiblingsSides,
        boolean hasSpouse,
        int spousesSide,
        int children,
        int allChildrensSides
    ) {}

    private @NotNull FamilyPlan coreFamilyPlan(boolean hasMother, boolean hasFather, int siblings, boolean hasSpouse, int children) {
        return new FamilyPlan(hasMother, 0, hasFather, 0, siblings, 0, hasSpouse, 0, children, 0);
    }

    public @NotNull FamilyPlan planFamily(@NotNull Citizen citizen) {
        requireState(ManagerState.ACTIVE);
        return planFamily(citizen, chooseFamilySize());
    }

    public @NotNull FamilyPlan planFamily(@NotNull CharacterManager.CitizenContext context) {
        requireState(ManagerState.ACTIVE);
        return planFamily(context, chooseFamilySize());
    }

    private @NotNull FamilyPlan planFamily(@NotNull Citizen citizen, int numberMembers) {
        requireState(ManagerState.ACTIVE);
        return planFamily(citizen, numberMembers, true, true, true);
    }

    private @NotNull FamilyPlan planFamily(@NotNull CharacterManager.CitizenContext context, int numberMembers) {
        return planFamily(context, numberMembers, true, true, true);
    }

    private @NotNull FamilyPlan planFamily(@NotNull Citizen citizen, int numberMembers, boolean allowParents, boolean allowSpouse, boolean allowChildren) {
        requireState(ManagerState.ACTIVE);
        return planFamily(new CharacterManager.CitizenContext(engine, citizen), numberMembers, allowParents, allowSpouse, allowChildren);
    }


    private @NotNull FamilyPlan planFamily(@NotNull CharacterManager.CitizenContext context, int numberMembers, boolean allowParents, boolean allowSpouse, boolean allowChildren) {
        requireState(ManagerState.ACTIVE);

        // Check for invalid or easy cases
        if (numberMembers < 0) throw new IllegalArgumentException("Number members in a family must be nonnegative");
        else if (numberMembers == 0) return coreFamilyPlan(false, false, 0, false, 0);
        else if (numberMembers == 1) {
            if (RandomUtils.chance(0.66)) return coreFamilyPlan(true, false, 0, false, 0);
            else return coreFamilyPlan(false, true, 0, false, 0);
        }
        else if (numberMembers == 2) return coreFamilyPlan(true, true, 0, false, 0);
        else if (numberMembers == 3) return coreFamilyPlan(true, true, 0, true, 0);

        // Core family
        boolean hasMother = allowParents && RandomUtils.chance(hasMotherPercent);
        boolean hasFather = allowParents && RandomUtils.chance(hasFatherPercent);
        boolean hasSpouse = allowSpouse && context.getAge() >= 18 && RandomUtils.chance(marriedPercent);
        int children = hasSpouse && allowChildren ? RandomUtils.probabilisticCount(eachChildPercent) : 0;
        if (children > 6) children = 6;
        int siblings = hasMother && hasFather ? RandomUtils.probabilisticCount(eachChildPercent) : 0;
        if (siblings > 6) siblings = 6;
        int coreFamilySize = (hasMother?1:0) + (hasFather?1:0) + (hasSpouse?1:0) + children + siblings;
        while (coreFamilySize > numberMembers) {
            if (siblings > 0) { siblings--; coreFamilySize--; }
            else if (children > 0) { children--; coreFamilySize--; }
            else if (hasSpouse) { hasSpouse = false; coreFamilySize--; }
            else if (hasFather) { hasFather = false; coreFamilySize--; }
            else if (hasMother) { hasMother = false; coreFamilySize--; }
        }
        if (coreFamilySize == numberMembers) return coreFamilyPlan(hasMother, hasFather, siblings, hasSpouse, children);

        // Extended family
        int remaining = numberMembers - coreFamilySize;
        float mothersSidePercent       = hasMother    ? RandomUtils.randNextPercent() * mothersSideMaxPercentOfFamily       : 0;
        float fathersSidePercent       = hasFather    ? RandomUtils.randNextPercent() * fathersSideMaxPercentOfFamily       : 0;
        float allSiblingsSidesPercent  = siblings > 0 ? RandomUtils.randNextPercent() * allSiblingsSidesMaxPercentOfFamily  : 0;
        float spousesSidePercent       = hasSpouse    ? RandomUtils.randNextPercent() * spousesSideMaxPercentOfFamily       : 0;
        float allChildrensSidesPercent = children > 0 ? RandomUtils.randNextPercent() * allChildrensSidesMaxPercentOfFamily : 0;
        float totalPercent = mothersSidePercent + fathersSidePercent + allSiblingsSidesPercent + spousesSidePercent + allChildrensSidesPercent;
        if (totalPercent < 1.0f) allChildrensSidesPercent += 1.0f - totalPercent;
        else if (totalPercent > 1.0f) allSiblingsSidesPercent -= totalPercent - 1.0f;
        int mothersSide       = (int) (remaining * mothersSidePercent);
        int fathersSide       = (int) (remaining * fathersSidePercent);
        int allSiblingsSides  = (int) (remaining * allSiblingsSidesPercent);
        int spousesSide       = (int) (remaining * spousesSidePercent);
        int allChildrensSides = (int) (remaining * allChildrensSidesPercent);
        int total = coreFamilySize + mothersSide + fathersSide + allSiblingsSides + spousesSide + allChildrensSides;
        if (total < numberMembers) allChildrensSides += numberMembers - total; // Rounding loss
        else if (total > numberMembers) allSiblingsSides -= total - numberMembers; // Never

        return new FamilyPlan(
            hasMother,
            mothersSide,
            hasFather,
            fathersSide,
            siblings,
            allSiblingsSides,
            hasSpouse,
            spousesSide,
            children,
            allChildrensSides
        );
    }

    public @NotNull Family buildFamily(@NotNull Citizen citizen) {
        return buildFamily(citizen, planFamily(citizen));
    }

    public @NotNull Family buildFamily(@NotNull Citizen citizen, @NotNull FamilyPlan plan) {
        Citizen mother, father, spouse;
        Set<Citizen> children = new HashSet<>();
        if (plan.hasMother) {
            mother = buildMother(citizen);
            buildFamily(mother, planFamily(mother, plan.mothersSide, true, false, false));
        }
        if (plan.hasFather) {
            father = buildFather(citizen);
            buildFamily(father, planFamily(father, plan.fathersSide, true, false, false));
        }
        if (plan.hasSpouse) {
            spouse = addSpouse(citizen);
            buildFamily(spouse, planFamily(spouse, plan.spousesSide, true, false, false));
        }

        // Siblings
        int allSiblingsSidesAccounted = 0;
        for (int i = 0; i < plan.siblings; i++) {
            int siblingSide;
            if (i == plan.siblings - 1) siblingSide = plan.allSiblingsSides - allSiblingsSidesAccounted;
            else siblingSide = plan.allSiblingsSides / plan.siblings;
            allSiblingsSidesAccounted += siblingSide;
            Citizen sibling;
            if (citizen.getFamily().getMother() != null) sibling = addChild(citizen.getFamily().getMother());
            else if (citizen.getFamily().getFather() != null) sibling = addChild(citizen.getFamily().getFather());
            else throw new IllegalArgumentException("A family plan must include one or more parents to include siblings");
            buildFamily(sibling, planFamily(sibling, siblingSide, false, true, true));
        }

        // Children
        int allChildrensSidesAccounted = 0;
        for (int i = 0; i < plan.children; i++) {
            int childsSide;
            if (i == plan.children - 1) childsSide = plan.allChildrensSides - allChildrensSidesAccounted;
            else childsSide = plan.allChildrensSides / plan.children;
            allChildrensSidesAccounted += childsSide;
            Citizen child = addChild(citizen);
            children.add(child);
            buildFamily(child, planFamily(child, childsSide, false, true, true));
        }

        citizen.getFamily().getChildren().addAll(children);
        return citizen.getFamily();
    }

    /**
     * Add a new citizen as the given citizen's mother.
     * @param citizen Citizen for whom to add a mother.
     * @throws IllegalArgumentException When the passed citizen already has a mother.
     * @return The new citizen who is the mother of the passed citizen.
     */
    public @NotNull Citizen buildMother(@NotNull Citizen citizen) {
        requireState(ManagerState.ACTIVE);

        return buildParent(citizen, RandomUtils.chance(CharacterManager.intersexPercentage * 2) ? Sex.INTERSEX : Sex.FEMALE);
    }

    /**
     * Add a new citizen as the given citizen's father.
     * @param citizen Citizen for whom to add a father.
     * @throws IllegalArgumentException When the passed citizen already has a father.
     * @return The new citizen who is the father of the passed citizen.
     */
    public @NotNull Citizen buildFather(@NotNull Citizen citizen) {
        requireState(ManagerState.ACTIVE);

        return buildParent(citizen, RandomUtils.chance(CharacterManager.intersexPercentage * 2) ? Sex.INTERSEX : Sex.MALE);
    }

    /**
     * Add a new citizen as a parent of the given sex for the given citizen.
     * @param citizen Citizen for whom to add a parent.
     * @param sex Sex of the new parent.
     * @throws IllegalArgumentException When there is an existing parent with conflicting sex.
     * @return The new citizen who is a parent for the passed citizen.
     */
    public @NotNull Citizen buildParent(@NotNull Citizen citizen, @NotNull Sex sex) {
        requireState(ManagerState.ACTIVE);

        if (sex == Sex.FEMALE && citizen.getFamily().getMother() != null) throw new IllegalArgumentException("Citizen already has a mother");
        if (sex == Sex.MALE && citizen.getFamily().getFather() != null) throw new IllegalArgumentException("Citizen already has a father");
        if (citizen.getFamily().getParents().size() == 2) throw new IllegalArgumentException("Citizen already has two parents");

        // Choose a birth year in generation range
        int generationGapYears = RandomUtils.nextInt(minGenerationGapYears, maxGenerationGapYears);
        // Choose a random date in the year following that date
        int dayOffset = RandomUtils.nextInt(0, TimeUtils.daysInYear);
        LocalDate parentBirthday = citizen.getBirthday().minusYears(generationGapYears).plusDays(dayOffset);

        Bloc generationBloc = engine.DEMOGRAPHICS_MANAGER.getGenerationForBirthday(parentBirthday);
        Bloc religionBloc = citizen.getDemographics().getReligion();
        Bloc raceEthnicityBloc = citizen.getDemographics().getRaceEthnicity();
        Bloc presentationBloc = engine.DEMOGRAPHICS_MANAGER.selectPresentationForSex(sex);

        Demographics parentDemographics = new Demographics(engine, generationBloc, religionBloc, raceEthnicityBloc, presentationBloc);

        Family family = new Family(engine, null, null, null, Set.of(citizen));

        CharacterManager.CitizenContext parentContext = new CharacterManager.CitizenContext(
            engine, sex, parentDemographics, parentBirthday, family, null, null, null, null
        );

        Citizen parent = engine.CHARACTER_MANAGER.buildCitizen(parentContext);
        switch (sex) {
            case FEMALE :
                citizen.getFamily().setMother(parent);
                break;
            case MALE:
                citizen.getFamily().setFather(parent);
                break;
            case INTERSEX:
                if (citizen.getFamily().getMother() == null)
                    citizen.getFamily().setMother(parent);
                else citizen.getFamily().setFather(parent);
                break;
        }
        return parent;
    }

    public @NotNull Citizen addChild(@NotNull Citizen citizen) {
        requireState(ManagerState.ACTIVE);

        if (citizen.getAge() < 18) throw new IllegalArgumentException("Citizens must be 18 years old or older to have a child");

        // Choose a birth year in generation range
        int generationGapYears = RandomUtils.nextInt(minGenerationGapYears, maxGenerationGapYears);
        // Choose a random date in the year following that date
        int dayOffset = RandomUtils.nextInt(0, TimeUtils.daysInYear);
        LocalDate childBirthday = citizen.getBirthday().plusYears(generationGapYears).plusDays(dayOffset);

        Bloc generationBloc = engine.DEMOGRAPHICS_MANAGER.getGenerationForBirthday(childBirthday);
        Bloc presentationBloc = engine.DEMOGRAPHICS_MANAGER.selectBloc(
            DemographicCategory.PRESENTATION, Set.of(citizen.getDemographics().getReligion(), citizen.getDemographics().getRaceEthnicity(), generationBloc)
        );
        Demographics childDemographics = new Demographics(engine, generationBloc, citizen.getDemographics().getReligion(), citizen.getDemographics().getRaceEthnicity(), presentationBloc);

        boolean citizenIsMother = citizen.getSex() == Sex.FEMALE ||
            (citizen.getFamily().getSpouse() != null && citizen.getFamily().getSpouse().getSex() == Sex.MALE) ||
            (citizen.getSex() == Sex.INTERSEX && citizen.getDemographics().getPresentation().getName().equals("Female"));
        Family family = new Family(engine, citizenIsMother ? citizen : citizen.getFamily().getSpouse(), citizenIsMother ? citizen.getFamily().getSpouse() : citizen, null, new HashSet<>());

        CharacterManager.CitizenContext childContext = new CharacterManager.CitizenContext(
            engine, null, childDemographics, childBirthday, family, null, null, null, null
        );

        Citizen child = engine.CHARACTER_MANAGER.buildCitizen(childContext);
        citizen.getFamily().getChildren().add(child);
        if (citizen.getFamily().getSpouse() != null) citizen.getFamily().getSpouse().getFamily().getChildren().add(child);
        return child;
    }

    public @NotNull Citizen addSpouse(@NotNull Citizen citizen) {
        requireState(ManagerState.ACTIVE);

        if (citizen.getAge() < 18) throw new IllegalArgumentException("Citizens must be 18 years old or older to have a spouse");

        Sex partnerSex = switch (citizen.getSex()) {
            case MALE -> RandomUtils.chance(mlmMarriagePercent) ? Sex.MALE : Sex.FEMALE;
            case FEMALE -> RandomUtils.chance(wlwMarriagePercent) ? Sex.FEMALE : Sex.MALE;
            case INTERSEX -> RandomUtils.chance(CharacterManager.femalePercentage) ? Sex.FEMALE : Sex.MALE;
        };
        if (RandomUtils.chance(CharacterManager.intersexPercentage)) partnerSex = Sex.INTERSEX;

        // Choose a birth year
        int ageGapYears = RandomUtils.nextInt(-maxSpouseAgeGapYears/2, maxSpouseAgeGapYears/2);
        // Choose a random date in the year following that date
        int dayOffset = RandomUtils.nextInt(0, TimeUtils.daysInYear);
        LocalDate spouseBirthday = citizen.getBirthday().plusYears(ageGapYears).plusDays(dayOffset);

        Family family = new Family(engine, null, null, citizen, new HashSet<>());

        CharacterManager.CitizenContext spouseContext = new CharacterManager.CitizenContext(
            engine, partnerSex, null, spouseBirthday, family, null, null, null, citizen.getResidence()
        );

        Citizen spouse = engine.CHARACTER_MANAGER.buildCitizen(spouseContext);
        citizen.getFamily().setSpouse(spouse);
        return spouse;
    }

    public int chooseFamilySize() {
        requireOperational();
        int familySize = RandomUtils.probabilisticCount(eachFamilyMemberPercent);
        if (familySize > 48) familySize = 48;
        return familySize;
    }
}
