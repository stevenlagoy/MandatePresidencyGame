package com.stevenlagoy.presidency.core.characters.attributes;

import com.stevenlagoy.presidency.characters.attributes.ExperienceManager;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ExperienceTest {


    @Test
    public void testGetExperiences() throws InterruptedException {
        Engine engine = new Engine();
        ExperienceManager experienceManager = engine.CHARACTER_MANAGER.EXPERIENCE_MANAGER;
        experienceManager.init();
        Thread.sleep(2000);
        assert(experienceManager.getState().equals(Manager.ManagerState.ACTIVE));
        assert(experienceManager.getExperiences().size() > 10);
    }

    @Test
    public void testGetFulfilledExperiences() throws InterruptedException {
        Engine engine = new Engine();
        ExperienceManager experienceManager = engine.CHARACTER_MANAGER.EXPERIENCE_MANAGER;
        experienceManager.init();
        Thread.sleep(2000);
        assert(experienceManager.getExperiences(new ArrayList<>()).size() > 5);
        assert(experienceManager.getExperiences(new ArrayList<>()).size() < experienceManager.getExperiences().size());
        assert(experienceManager.getExperiences(experienceManager.getExperiences()).size() == experienceManager.getExperiences().size());
    }
}
