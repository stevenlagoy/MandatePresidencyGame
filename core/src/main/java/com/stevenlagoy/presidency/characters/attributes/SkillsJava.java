/*
 * Skills
 * ~/characters/attributes/Skills.java
 * Steven LaGoy
 * Created: 28 May 2025 at 10:18 PM
 * Modified: 28 December 2025
 */

package com.stevenlagoy.presidency.characters.attributes;

// IMPORTS ----------------------------------------------------------------------------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.presidency.util.NumberUtils;
import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.characters.PoliticalActorJava;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                             SKILLS                                             //
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Tracks the base and modified Legislative, Executive, and Judicial skills of a
 * PoliticalActor (or subclass), as well as their Aptitude.
 *
 * @see PoliticalActorJava#skills
 */
public class SkillsJava implements Repr<SkillsJava>, Jsonic<SkillsJava> {

    // INSTANCE VARIABLES -------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Base Legislative Skill, representing ability to plan effectively, leverage
     * advantages, and make lasting decisions.
     */
    private int baseLegislativeSkill;
    /**
     * Additive modifier to the Legislative Skill. A +1 modifier results in a skill
     * equal to baseSkill + 1. Additive multipliers are not affected by
     * Multiplicative modifiers.
     */
    private int legAdd;
    /**
     * Multiplicative modifier to the Legislative Skill. A 200% (2.0) modifier
     * results in a skill equal to baseSkill * 2.0. Multiplicative modifiers are not
     * affected by Additive modifiers.
     */
    private float legMult;
    /**
     * Base Judicial Skill, representing ability to make decisions quickly, apply
     * charismatic persuasion, and execute strong or decisive actions.
     */
    private int baseExecutiveSkill;
    /**
     * Additive modifier to the Executive Skill. A +1 modifier results in a skill
     * equal to baseSkill + 1. Additive multipliers are not affected by
     * Multiplicative modifiers.
     */
    private int execAdd;
    /**
     * Multiplicative modifier to the Executive Skill. A 200% (2.0) modifier results
     * in a skill equal to baseSkill * 2.0. Multiplicative modifiers are not
     * affected by Additive modifiers.
     */
    private float execMult;
    /**
     * Base Legislative Skill, representing ability to inspect facts, reason through
     * problems, and make informed decisions.
     */
    private int baseJudicialSkill;
    /**
     * Additive modifier to the Judicial Skill. A +1 modifier results in a skill
     * equal to baseSkill + 1. Additive multipliers are not affected by
     * Multiplicative modifiers.
     */
    private int judAdd;
    /**
     * Multiplicative modifier to the Judicial Skill. A 200% (2.0) modifier results
     * in a skill equal to baseSkill * 2.0. Multiplicative modifiers are not
     * affected by Additive modifiers.
     */
    private float judMult;
    /** Aptitude, representing the sum of the three base skills. */
    private int aptitude;

    // CONSTRUCTORS -------------------------------------------------------------------------------------------------------------------------------------------

    public SkillsJava() {
        this(50, 50, 50);
    }

    public SkillsJava(SkillsJava other) {
        this.baseLegislativeSkill = other.baseLegislativeSkill;
        this.legAdd = other.legAdd;
        this.legMult = other.legMult;
        this.baseExecutiveSkill = other.baseExecutiveSkill;
        this.execAdd = other.execAdd;
        this.execMult = other.execMult;
        this.baseJudicialSkill = other.baseJudicialSkill;
        this.judAdd = other.judAdd;
        this.judMult = other.judMult;
        this.aptitude = other.aptitude;
    }

    public SkillsJava(String buildstring) {
        if (buildstring == null || buildstring.isBlank()) {
            throw new IllegalArgumentException(
                    "The given buildstring was null, and a Skills object could not be created.");
        }
        fromRepr(buildstring);
    }

    public SkillsJava(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException(
                    "The passed JSON Object was null, and a Skills object could not be created.");
        }
        fromJson(json);
    }

    public SkillsJava(int baseLegislativeSkill, int baseExecutiveSkill, int baseJudicialSkill) {
        this.baseLegislativeSkill = baseLegislativeSkill;
        this.legAdd = 0;
        this.legMult = 1;
        this.baseExecutiveSkill = baseExecutiveSkill;
        this.execAdd = 0;
        this.execMult = 1;
        this.baseJudicialSkill = baseJudicialSkill;
        this.judAdd = 0;
        this.judMult = 1;
        this.aptitude = calculateAptitude();
    }

    public SkillsJava(int legislativeSkill, int executiveSkill, int judicialSkill, int aptitude) {
        this.aptitude = aptitude;
        this.legMult = 1;
        this.execMult = 1;
        this.judMult = 1;
        calculateBaseSkills(legislativeSkill, executiveSkill, judicialSkill, aptitude);
    }

    public SkillsJava(
        int baseLegislativeSkill,
        int legislativeSkill,
        int baseExecutiveSkill,
        int executiveSkill,
        int baseJudicialSkill,
        int judicialSkill
    ) {
        this(baseLegislativeSkill, baseExecutiveSkill, baseJudicialSkill);
        this.aptitude = calculateAptitude();
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------------------------------------------------------------------

    // Base Legislative Skill
    public int getBaseLegislativeSkill() {
        return baseLegislativeSkill;
    }

    public void setBaseLegislativeSkill(int skill) {
        this.baseLegislativeSkill = NumberUtils.clamp(skill, 0, 100);
        calculateAptitude();
    }

    public void addBaseLegislativeSkill(int skill) {
        this.baseLegislativeSkill = NumberUtils.clamp(baseLegislativeSkill + skill, 0, 100);
        calculateAptitude();
    }

    public void multiplyBaseLegislativeSkill(float factor) {
        this.baseLegislativeSkill = (int) NumberUtils.clamp((baseLegislativeSkill * factor), 0, 100);
    }

    // Legislative Skill
    public int getLegislativeSkill() {
        return NumberUtils.clamp(Math.round(baseLegislativeSkill * legMult + legAdd), 0, 100);
    }

    public void addLegislativeSkillAdditive(int skill) {
        this.legAdd += skill;
    }

    public void addLegislativeSkillMultiplicative(float factor) {
        this.legMult += factor;
    }

    // Base Executive Skill
    public int getBaseExecutiveSkill() {
        return baseExecutiveSkill;
    }

    public void setBaseExecutiveSkill(int skill) {
        this.baseExecutiveSkill = NumberUtils.clamp(skill, 0, 100);
        calculateAptitude();
    }

    public void addBaseExecutiveSkill(int skill) {
        this.baseExecutiveSkill = NumberUtils.clamp(baseExecutiveSkill + skill, 0, 100);
        calculateAptitude();
    }

    public void multiplyBaseExecutiveSkill(float factor) {
        this.baseExecutiveSkill = (int) NumberUtils.clamp((long) (baseExecutiveSkill * factor), 0, 100);
    }

    // Executive Skill
    public int getExecutiveSkill() {
        return NumberUtils.clamp(Math.round(baseExecutiveSkill * execMult + execAdd), 0, 100);
    }

    public void addExecutiveSkillAdditive(int skill) {
        this.execAdd += skill;
    }

    public void addExecutiveSkillMultiplicative(float factor) {
        this.execMult += factor;
    }

    // Base Judicial Skill
    public int getBaseJudicialSkill() {
        return baseJudicialSkill;
    }

    public void setBaseJudicialSkill(int skill) {
        this.baseJudicialSkill = NumberUtils.clamp(skill, 0, 100);
        calculateAptitude();
    }

    public void addBaseJudicialSkill(int skill) {
        this.baseJudicialSkill = NumberUtils.clamp(baseJudicialSkill + skill, 0, 100);
        calculateAptitude();
    }

    public void multiplyBaseJudicialSkill(float factor) {
        this.baseJudicialSkill = (int) NumberUtils.clamp((long) (baseJudicialSkill * factor), 0, 100);
    }

    // Judicial Skill
    public int getJudicialSkill() {
        return NumberUtils.clamp(Math.round(baseJudicialSkill * judMult + judAdd), 0, 100);
    }

    public void addJudicialSkillAdditive(int skill) {
        this.judAdd += skill;
    }

    public void addJudicialSkillMultiplicative(float factor) {
        this.judMult += factor;
    }

    // Aptitude
    public int getAptitude() {
        return aptitude;
    }

    // CALCULATION FUNCTIONS ----------------------------------------------------------------------------------------------------------------------------------

    private int calculateAptitude() {
        this.aptitude = baseLegislativeSkill + baseExecutiveSkill + baseJudicialSkill;
        return this.aptitude;
    }

    private void calculateBaseSkills(int leg, int exec, int jud, int ap) {
        if (ap < 0) {
            throw new IllegalStateException("Aptitude cannot be negative");
        }

        // Handle special case where all skills are 0
        if (leg == 0 && exec == 0 && jud == 0) {
            baseLegislativeSkill = aptitude / 3;
            baseExecutiveSkill = aptitude / 3;
            baseJudicialSkill = aptitude - (2 * (aptitude / 3));
            return;
        }

        // Calculate initial ratios
        double total = leg + exec + jud;
        double lr = leg / total;
        double er = exec / total;
        double jr = jud / total;

        // Initial distribution
        int l = (int) (aptitude * lr);
        int e = (int) (aptitude * er);
        int j = (int) (aptitude * jr);

        // Distribute remaining points based on smallest difference from target ratio
        int remainder = aptitude - (l + e + j);
        while (remainder > 0) {
            double currentTotal = l + e + j;
            double lDiff = Math.abs(lr - (l / currentTotal));
            double eDiff = Math.abs(er - (e / currentTotal));
            double jDiff = Math.abs(jr - (j / currentTotal));

            if (lDiff <= eDiff && lDiff <= jDiff) {
                l++;
                remainder--;
            }
            else if (eDiff <= jDiff) {
                e++;
                remainder--;
            }
            else {
                j++;
                remainder--;
            }
        }

        baseLegislativeSkill = l;
        baseExecutiveSkill = e;
        baseJudicialSkill = j;

        legAdd = leg - baseLegislativeSkill;
        execAdd = exec - baseExecutiveSkill;
        judAdd = jud - baseJudicialSkill;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public SkillsJava fromRepr(String repr) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toRepr() {
        String repr = String.format(
                "%s:[baseLegislativeSkill:%d;baseExecutiveSkill:%d;baseJudicialSkill:%d;aptitude:%d;];",
                this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
                this.baseLegislativeSkill,
                this.baseExecutiveSkill,
                this.baseJudicialSkill,
                this.aptitude);
        return repr;
    }

    /**
     * {@inheritDoc}
     */
    public SkillsJava fromJson(JSONObject json) {
        if (json == null)
            return null;
        Object baseLegislativeObj = json.get("base_legislative_skill");
        if (baseLegislativeObj == null)
            this.baseLegislativeSkill = 50;
        else if (baseLegislativeObj instanceof Number baseLeg)
            this.baseLegislativeSkill = baseLeg.intValue();
        else if (baseLegislativeObj instanceof JSONObject baseLegislativeJson)
            this.baseLegislativeSkill = baseLegislativeJson.getAsNumber().intValue();
        Object baseExecutiveObj = json.get("base_executive_skill");
        if (baseExecutiveObj == null)
            this.baseExecutiveSkill = 50;
        else if (baseExecutiveObj instanceof Number baseExec)
            this.baseExecutiveSkill = baseExec.intValue();
        else if (baseExecutiveObj instanceof JSONObject baseExecutiveJson)
            this.baseExecutiveSkill = baseExecutiveJson.getAsNumber().intValue();
        Object baseJudicialObj = json.get("base_judicial_skill");
        if (baseJudicialObj == null)
            this.baseJudicialSkill = 50;
        else if (baseJudicialObj instanceof Number baseJud)
            this.baseJudicialSkill = baseJud.intValue();
        else if (baseJudicialObj instanceof JSONObject baseJudicialJson)
            this.baseJudicialSkill = baseJudicialJson.getAsNumber().intValue();

        calculateAptitude();

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("base_legislative_skill", baseLegislativeSkill));
        fields.add(new JSONObject("legislative_additive_modifier", legAdd));
        fields.add(new JSONObject("legislative_multiplicative_modifier", legMult));

        fields.add(new JSONObject("base_executive_skill", baseExecutiveSkill));
        fields.add(new JSONObject("executive_additive_modifier", execAdd));
        fields.add(new JSONObject("executive_multiplicative_modifier", execMult));

        fields.add(new JSONObject("base_judicial_skill", baseJudicialSkill));
        fields.add(new JSONObject("judicial_additive_modifier", judAdd));
        fields.add(new JSONObject("judicial_multiplicative_modifier", judMult));

        return new JSONObject("skills", fields);
    }

    /**
     * @see #toRepr()
     */
    @Override
    public String toString() {
        return this.toRepr();
    }

    // OBJECT METHODS -----------------------------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        SkillsJava other = (SkillsJava) obj;
        return this.toString().equals(other.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 19;
        int hash = 9;
        hash = prime * hash + (baseLegislativeSkill * 1);
        hash = prime * hash + (baseExecutiveSkill * 101);
        hash = prime * hash + (baseJudicialSkill * 1009);
        hash = prime * hash + (aptitude * 10007);
        hash /= 1e5;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SkillsJava clone() {
        return new SkillsJava(this);
    }

}
