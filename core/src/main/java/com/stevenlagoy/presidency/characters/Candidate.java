/*
 * Candidate
 * ~/characters/Candidate.java
 * Steven LaGoy
 * Created: 28 August 2024 at 5:42 PM
 * Modified: 29 December 2025
 */

package com.stevenlagoy.presidency.characters;

// IMPORTS ----------------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.map.MapManager;
import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.characters.attributes.Personality;
import com.stevenlagoy.presidency.characters.attributes.names.NameManager;
import com.stevenlagoy.presidency.util.RandomUtils;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                           CANDIDATE                                            //
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * A Character subclass which extends PoliticalActor and models a Candidate in
 * the Presidential race.
 * <p>
 * Contains fields and methods particular to a Candidate's campaign details and
 * information.
 * 
 * @see Character
 * @see PoliticalActor
 */
public class Candidate extends PoliticalActor {

    // STATIC VARIABLES ---------------------------------------------------------------------------------------------------------------------------------------

    /** Minimum age of a Candidate for the Presidency */
    public static final int MIN_AGE = 35;
    /** Maximum age of a Candidate */
    public static final int MAX_AGE = PoliticalActor.MAX_AGE;

    final static int SKILLS_BONUS_EDUCATION = 10;
    final static double EDUCATION_SCALER = 1.25;
    final static int EDUCATION_COST = 100;
    final static int MAX_EDUCATION_LEVEL = 8;
    final static int POINTS_PER_SKILL = 10;

    private int numDelegates;
    private double influence;

    // CONSTRUCTORS -------------------------------------------------------------------------------------------------------------------------------------------

    public Candidate(CharacterManager cm, DemographicsManager dm, MapManager mm, NameManager nm) {
        this(new PoliticalActor(cm, dm, mm, nm));
    }

    public Candidate(PoliticalActor politicalActor) {
        super(politicalActor);
    }

    // INSTANCE METHODS ---------------------------------------------------------------------------------------------------------------------------------------

    protected void generateProfile() {
        int totalPoints = 0;
        int numCategories = 4;
        switch (Engine.getGameDifficulty()) {
            case LEVEL_1:
                totalPoints = RandomUtils.randInt(400, 500);
                break;
            case LEVEL_2:
                totalPoints = RandomUtils.randInt(400, 500);
                break;
            case LEVEL_3:
                totalPoints = RandomUtils.randInt(400, 500);
                break;
            case LEVEL_4:
                totalPoints = RandomUtils.randInt(400, 500);
                break;
            case LEVEL_5:
                totalPoints = RandomUtils.randInt(400, 500);
                break;
        }
        int allocations[] = new int[numCategories];
        int allocatedPoints = 0;
        int i = 0;
        while (allocatedPoints < totalPoints) {
            int value = RandomUtils
                    .randInt((int) Math.round((totalPoints - allocatedPoints) * 1.0 / numCategories));
            allocations[i++] += value;
            allocatedPoints += value;
            i %= numCategories;
        }
        // adjust point allocations based on age, expected apportionment, etc
        int res;
        res = generateEducation(allocations[0]);
        allocations[1] += res;
        allocations[1] += Math.pow(this.getEducation().value * SKILLS_BONUS_EDUCATION, EDUCATION_SCALER);
        res = generateSkills(allocations[1]);
        allocations[2] += res;
        res = generateTraits(allocations[2]);
        allocations[3] += res;
        res = generateExperience(allocations[3]);
        res = generateFamily(res + 100);
    }

    protected int generateEducation(int points) {
        // Give the maximum possible education with the provided points
        int educationLevel = points / EDUCATION_COST;
        if (educationLevel > MAX_EDUCATION_LEVEL)
            educationLevel = MAX_EDUCATION_LEVEL;
        this.setEducation(Education.level(points / EDUCATION_COST));
        if (this.getEducation().value > 4)
            this.selectEducationalInstitution();
        int refund = points - this.getEducation().value * EDUCATION_COST;

        // select a specific education type?

        return refund;
    }

    protected void selectEducationalInstitution() {
        // LocationManager.selectEducationalInstitution(this.getBirthplaceCity());
    }

    protected int generateSkills(int points) {
        // Evenly distribute 40% to 60% of points across all skills categories.
        int totalPoints = points, refund = 0;
        int[] pointsAllotments = { 0, 0, 0 };

        int evenApportionment = Math.round(RandomUtils.randPercent(0.4f, 0.8f) * points);
        points -= evenApportionment;
        int selection = RandomUtils.randInt(2);
        pointsAllotments[selection] += Math.round(RandomUtils.randPercent(0.5f, 0.9f) * points);
        points -= pointsAllotments[selection];
        selection = (selection + RandomUtils.randInt(1)) % 3;
        pointsAllotments[selection] += Math.round(1.0 * points);
        points -= pointsAllotments[selection];

        for (int i = 0; i < 3; i++)
            pointsAllotments[i] += evenApportionment;

        this.skills.setBaseLegislativeSkill(pointsAllotments[0] / POINTS_PER_SKILL);
        this.skills.setBaseExecutiveSkill(pointsAllotments[1] / POINTS_PER_SKILL);
        this.skills.setBaseJudicialSkill(pointsAllotments[2] / POINTS_PER_SKILL);

        refund = totalPoints - points;
        return refund;
    }

    protected int generateTraits(int points) {
        // Select traits with 25% to 75% of the points. Use the remaining points to
        // upgrade traits.
        int pointsForSelection, pointsForUpgrade, refund = 0;
        pointsForSelection = (int) Math.floor(RandomUtils.randPercent(0.25f, 0.75f) * points);
        pointsForUpgrade = points - pointsForSelection;

        while (pointsForUpgrade > 0) {

        }

        return refund;
    }

    protected int generateExperience(int points) {
        int refund = 0;
        return refund;
    }

    protected int generateFamily(int points) {
        int refund = 0;
        return refund;
    }

    protected void generatePolitics() {

    }

    protected void generateAppearance() {
        CharacterManager.generateAppearance(this);
    }

    protected void genPresentation(CharacterManager cm) {
        this.getDemographics().setPresentation(cm.generatePresentation(this.getDemographics()));
    }

    public int getNumDelegates() {
        return this.numDelegates;
    }

    public void setNumDelegates(int numDelegates) {
        this.numDelegates = numDelegates;
    }

    public void addNumDelegates(int numDelegates) {
        this.numDelegates += numDelegates;
    }

    public double getInfluence() {
        return influence;
    }

    public void setInfluence(double influence) {
        this.influence = influence;
    }

    public void addInfluence(double influence) {
        this.influence += influence;
    }

    public Personality determinePersonality() {
        return null;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public String toRepr() {
        String superRepr = super.toRepr();
        String[] splitSuperRepr = superRepr.split(":\\[");
        superRepr = "";
        for (int i = 1; i < splitSuperRepr.length; i++) {
            superRepr += splitSuperRepr[i] + ":[";
        }
        superRepr = superRepr.substring(0, superRepr.length() - 5);
        String repr = String.format("%s:[%snumDelegates=%d;influence=%s;];",
                this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
                superRepr,
                numDelegates,
                influence);
        return repr;
    }

    @Override
    public Candidate fromRepr(String repr) {
        return null;
    }

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();

        fields.add(new JSONObject("number_delegates", numDelegates));
        fields.add(new JSONObject("influence", influence));

        List<?> superFields = super.toJson().getAsList();
        for (Object obj : superFields) {
            if (obj instanceof JSONObject jsonObj) {
                fields.add(jsonObj);
            }
        }

        return new JSONObject(getName().getBiographicalName(), fields);
    }

    @Override
    public Candidate fromJson(JSONObject json) {
        return this;
    }
}