package com.stevenlagoy.presidency.characters.attributes;

import org.junit.*;
import static org.junit.Assert.*;

import core.JSONObject;


public final class SkillsTest {
    
    @Test
    public void testCreateSkills() {
        Skills s = new Skills();
        s = new Skills(s);
        s = new Skills(100, 100, 100);
        s = new Skills(60, 60, 60, 150);
        s = new Skills(25, 35, 50, 61, 44, 40);
    }

    @Test
    public void testLegislativeSkills() {
        Skills s = new Skills();
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
        Skills s = new Skills();
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
        Skills s = new Skills();
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
        Skills s = new Skills(60, 60, 60, 150);
        assertEquals(150, s.getAptitude());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidAptitude() {
        new Skills(50, 50, 50, -100);
    }

    @Test
    public void testCalcBaseSkillsZeroes() {
        Skills s = new Skills(0, 0, 0, 30);
        assertEquals(10, s.getLegislativeSkill());
        assertEquals(10, s.getExecutiveSkill());
        assertEquals(10, s.getJudicialSkill());
    }

    @Test
    public void testCalcBaseSkillsRemainder() {
        Skills s = new Skills(50, 50, 50, 152);
        assertEquals(51, s.getBaseLegislativeSkill());
        assertEquals(50, s.getLegislativeSkill());
        assertEquals(51, s.getBaseExecutiveSkill());
        assertEquals(50, s.getExecutiveSkill());
        assertEquals(50, s.getBaseJudicialSkill());
        assertEquals(50, s.getJudicialSkill());
    }

    @Test
    public void testSkillsToRepr() {
        Skills s = new Skills(75, 33, 52);
        String repr = s.toRepr();
        assertTrue(repr.contains("baseLegislativeSkill:75;"));
        assertTrue(repr.contains("baseExecutiveSkill:33;"));
        assertTrue(repr.contains("baseJudicialSkill:52;"));
        assertTrue(repr.contains("aptitude:160;"));
    }

    @Test
    public void testSkillsToJson() {
        Skills s = new Skills(75, 33, 52);
        JSONObject json = s.toJson();
        assertEquals(json.getKey(), "skills");
        assertEquals(75, json.get("base_legislative_skill"));
        assertEquals(33, json.get("base_executive_skill"));
        assertEquals(52, json.get("base_judicial_skill"));
    }

    @Test
    public void testSkillsEqualsAndHashCode() {
        Skills s1 = new Skills(10, 10, 10);
        assertEquals(s1, s1);
        assertNotEquals(s1, null);
        assertNotEquals(s1, new Object());
        Skills s2 = new Skills(50, 50, 50);
        int hash1 = s1.hashCode();
        int hash2 = s2.hashCode();
        assertNotEquals(hash1, hash2);
        Skills s3 = s1.clone();
        boolean addressesDifferent = s1 == s2;
        assertFalse(addressesDifferent);
        int hash3 = s3.hashCode();
        assertEquals(s1, s3);
        assertEquals(hash1, hash3);
    }

    @Test
    public void testSkillsFromJson() {
        Skills s1 = new Skills(50, 20, 70);
        JSONObject json = s1.toJson();
        Skills s2 = new Skills(json);
        assertEquals(s1, s2);
    }
}
