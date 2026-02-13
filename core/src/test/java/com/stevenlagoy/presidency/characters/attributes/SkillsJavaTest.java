package com.stevenlagoy.presidency.characters.attributes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.stevenlagoy.jsonic.JSONObject;

public final class SkillsJavaTest {

    @Test
    public void testCreateSkills() {
        SkillsJava s = new SkillsJava();
        s = new SkillsJava(s);
        s = new SkillsJava(100, 100, 100);
        s = new SkillsJava(60, 60, 60, 150);
        s = new SkillsJava(25, 35, 50, 61, 44, 40);
    }

    @Test
    public void testLegislativeSkills() {
        SkillsJava s = new SkillsJava();
        s.setBaseLegislativeSkill(75);
        assertEquals(75, s.getBaseLegislativeSkill());
        s.addBaseLegislativeSkill(5);
        assertEquals(80, s.getBaseLegislativeSkill());
        s.multiplyBaseLegislativeSkill(0.5f);
        assertEquals(40, s.getBaseLegislativeSkill());
        assertEquals(40, s.getLegislativeSkill());
        s.addLegislativeSkillAdditive(10);
        assertEquals(50, s.getLegislativeSkill());
        s.addLegislativeSkillMultiplicative(0.25f);
        assertEquals(60, s.getLegislativeSkill());
        assertEquals(40, s.getBaseLegislativeSkill());
    }

    @Test
    public void testExecutiveSkills() {
        SkillsJava s = new SkillsJava();
        s.setBaseExecutiveSkill(75);
        assertEquals(75, s.getBaseExecutiveSkill());
        s.addBaseExecutiveSkill(5);
        assertEquals(80, s.getBaseExecutiveSkill());
        s.multiplyBaseExecutiveSkill(0.5f);
        assertEquals(40, s.getBaseExecutiveSkill());
        assertEquals(40, s.getExecutiveSkill());
        s.addExecutiveSkillAdditive(10);
        assertEquals(50, s.getExecutiveSkill());
        s.addExecutiveSkillMultiplicative(0.25f);
        assertEquals(60, s.getExecutiveSkill());
        assertEquals(40, s.getBaseExecutiveSkill());
    }

    @Test
    public void testJudicialSkills() {
        SkillsJava s = new SkillsJava();
        s.setBaseJudicialSkill(75);
        assertEquals(75, s.getBaseJudicialSkill());
        s.addBaseJudicialSkill(5);
        assertEquals(80, s.getBaseJudicialSkill());
        s.multiplyBaseJudicialSkill(0.5f);
        assertEquals(40, s.getBaseJudicialSkill());
        assertEquals(40, s.getJudicialSkill());
        s.addJudicialSkillAdditive(10);
        assertEquals(50, s.getJudicialSkill());
        s.addJudicialSkillMultiplicative(0.25f);
        assertEquals(60, s.getJudicialSkill());
        assertEquals(40, s.getBaseJudicialSkill());
    }

    @Test
    public void testAptitude() {
        SkillsJava s = new SkillsJava(60, 60, 60, 150);
        assertEquals(150, s.getAptitude());
    }

    @Test
    public void testInvalidAptitude() {
        assertThrows(IllegalStateException.class, () -> {
            new SkillsJava(50, 50, 50, -100);
        });
    }

    @Test
    public void testCalcBaseSkillsZeroes() {
        SkillsJava s = new SkillsJava(0, 0, 0, 30);
        assertEquals(10, s.getLegislativeSkill());
        assertEquals(10, s.getExecutiveSkill());
        assertEquals(10, s.getJudicialSkill());
    }

    @Test
    public void testCalcBaseSkillsRemainder() {
        SkillsJava s = new SkillsJava(50, 50, 50, 152);
        assertEquals(51, s.getBaseLegislativeSkill());
        assertEquals(50, s.getLegislativeSkill());
        assertEquals(51, s.getBaseExecutiveSkill());
        assertEquals(50, s.getExecutiveSkill());
        assertEquals(50, s.getBaseJudicialSkill());
        assertEquals(50, s.getJudicialSkill());
    }

    @Test
    public void testSkillsToRepr() {
        SkillsJava s = new SkillsJava(75, 33, 52);
        String repr = s.toRepr();
        assertTrue(repr.contains("baseLegislativeSkill:75;"));
        assertTrue(repr.contains("baseExecutiveSkill:33;"));
        assertTrue(repr.contains("baseJudicialSkill:52;"));
        assertTrue(repr.contains("aptitude:160;"));
    }

    @Test
    public void testSkillsToJson() {
        SkillsJava s = new SkillsJava(75, 33, 52);
        JSONObject json = s.toJson();
        assertEquals(json.getKey(), "skills");
        assertEquals(75, json.get("base_legislative_skill"));
        assertEquals(33, json.get("base_executive_skill"));
        assertEquals(52, json.get("base_judicial_skill"));
    }

    @Test
    public void testSkillsEqualsAndHashCode() {
        SkillsJava s1 = new SkillsJava(10, 10, 10);
        assertEquals(s1, s1);
        assertNotEquals(s1, null);
        assertNotEquals(s1, new Object());
        SkillsJava s2 = new SkillsJava(50, 50, 50);
        int hash1 = s1.hashCode();
        int hash2 = s2.hashCode();
        assertNotEquals(hash1, hash2);
        SkillsJava s3 = s1.clone();
        boolean addressesDifferent = s1 == s2;
        assertFalse(addressesDifferent);
        int hash3 = s3.hashCode();
        assertEquals(s1, s3);
        assertEquals(hash1, hash3);
    }

    @Test
    public void testSkillsFromJson() {
        SkillsJava s1 = new SkillsJava(50, 20, 70);
        JSONObject json = s1.toJson();
        SkillsJava s2 = new SkillsJava(json);
        assertEquals(s1, s2);
    }
}
