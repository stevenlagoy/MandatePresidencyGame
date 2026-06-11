package com.stevenlagoy.presidency.core.characters.attributes;

import com.stevenlagoy.presidency.characters.attributes.ExperienceManager;
import com.stevenlagoy.presidency.characters.attributes.experiences.Experience;
import com.stevenlagoy.presidency.characters.attributes.experiences.ExperienceHistory;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExperienceTest {

    @Test
    public void testGetExperiences() throws InterruptedException {
        ExperienceManager experienceManager = new Engine().CHARACTER_MANAGER.EXPERIENCE_MANAGER;
        experienceManager.init();
        Thread.sleep(2000);
        assertEquals(Manager.ManagerState.ACTIVE, experienceManager.getState());
        assertTrue(experienceManager.getExperiences().size() > 10);
    }

    @Test
    public void testGetFulfilledExperiences() throws InterruptedException {
        ExperienceManager experienceManager = new Engine().CHARACTER_MANAGER.EXPERIENCE_MANAGER;
        experienceManager.init();
        Thread.sleep(2000);
        assertTrue(experienceManager.getExperiences(new ArrayList<>()).size() > 5);
        assertTrue(experienceManager.getExperiences(new ArrayList<>()).size() < experienceManager.getExperiences().size());
        assertEquals(experienceManager.getExperiences(experienceManager.getExperiences()).size(), experienceManager.getExperiences().size());
    }

    @Test
    public void validateExperience() throws InterruptedException {
        ExperienceManager experienceManager = new Engine().CHARACTER_MANAGER.EXPERIENCE_MANAGER;
        experienceManager.init();
        Thread.sleep(2000);
        boolean foundPrerequisites = false, foundConnections = false;
        for (Experience experience : experienceManager.getExperiences()) {
            assert(!experience.getLabel().isBlank());
            assert(!experience.getName().isBlank());
            assert(!experience.getTrack().isBlank());
            assert(experience.getCapacity() > 0.0);
            assert(experience.getMinTenure() > 0.0);
            assert(experience.getAvgTenure() > 0.0);
            assert(experience.getMaxTenure() > 0.0);
            foundPrerequisites = foundPrerequisites || !experience.getPrerequisites().isEmpty();
            assert(experience.getMinAge() >= 18);
            assert(experience.getYearlySkills().getFirst() >= 0.0);
            assert(experience.getYearlySkills().getSecond() >= 0.0);
            assert(experience.getYearlySkills().getThird() >= 0.0);
            assert(!experience.getDescription().isBlank());
            foundConnections = foundConnections || !experience.getConnections().isEmpty();
        }
        assertTrue(foundPrerequisites);
        assertTrue(foundConnections);
        assertEquals(Manager.ManagerState.ACTIVE, experienceManager.getState());
    }

    @Test
    public void testBuildExperienceHistory() throws InterruptedException {
        Engine engine = new Engine();
        engine.TIME_MANAGER.init();
        ExperienceManager experienceManager = engine.CHARACTER_MANAGER.EXPERIENCE_MANAGER;
        experienceManager.init();
        Thread.sleep(2000);
        ExperienceHistory experienceHistory = experienceManager.buildExperienceHistory(LocalDate.of(1970, 1, 1));
        assertEquals(Manager.ManagerState.ACTIVE, experienceManager.getState());
        assertTrue(experienceHistory.getExperiences().size() > 5);
    }

    @Test
    public void testBuildExperienceHistoryWithGoalExperience() throws InterruptedException {
        Engine engine = new Engine();
        engine.TIME_MANAGER.init();
        ExperienceManager experienceManager = engine.CHARACTER_MANAGER.EXPERIENCE_MANAGER;
        experienceManager.init();
        Thread.sleep(2000);
        ExperienceHistory experienceHistory = new ExperienceHistory(engine, List.of(new ExperienceHistory.ExperienceEntry(engine, experienceManager.matchExperience("supreme_court_justice").orElseThrow(), LocalDate.of(2010, 1, 1), null)));
        experienceManager.fillExperienceHistory(LocalDate.of(1970, 1, 1), experienceHistory);
        assertTrue(experienceHistory.getExperiences().size() > 5);
        for (ExperienceHistory.ExperienceEntry experienceEntry : experienceHistory.getExperiences()) {
            assertTrue(experienceEntry.getExperience().prerequisitsMet(experienceHistory.getExperiencesBefore(experienceEntry).stream().map(ExperienceHistory.ExperienceEntry::getExperience).collect(Collectors.toSet())));
        }
        assertEquals(Manager.ManagerState.ACTIVE, experienceManager.getState());
    }
}
