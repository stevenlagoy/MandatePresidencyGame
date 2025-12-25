package com.stevenlagoy.presidency.core;

import org.junit.*;
import static org.junit.Assert.*;

import com.stevenlagoy.presidency.core.LanguageManager.Language;
import com.stevenlagoy.presidency.core.Manager.ManagerState;

import core.JSONObject;

public final class LanguageTest {
    
    static final Engine ENGINE = new Engine();

    @Before
    public void initLanguageManager() {
        if (ENGINE.LanguageManager().getState() != ManagerState.ACTIVE) {
            if (!ENGINE.LanguageManager().init()) {
                fail("Failed to initialize LanguageManager, necessary for testing");
            }
        }
    }

    @Test
    public void testLanguageFromName() {
        String name = "English";
        Language l = Language.fromName(name);
        assertEquals(Language.EN, l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguageFromInvalidNameThrows() {
        String name = "Invalid Language";
        Language.fromName(name);
    }

    @Test
    public void testLanguageFromLabel() {
        String label = "EN";
        Language l = Language.label(label);
        assertEquals(Language.EN, l);
    }

    @Test(expected =  IllegalArgumentException.class)
    public void testLanguageFromInvalidLabelThrows() {
        String label = "XX";
        Language.label(label);
    }

    @Test
    public void testGetGameLanguage() {
        Language l = ENGINE.LanguageManager().getGameLanguage();
        assertEquals(Language.defaultLanguage, l);
    }

    @Test
    public void testSetGameLanguage() {
        boolean res = ENGINE.LanguageManager().setGameLanguage(Language.ES);
        assertTrue(res);
        Language l = ENGINE.LanguageManager().getGameLanguage();
        assertEquals(Language.ES, l);
        ENGINE.LanguageManager().setGameLanguage(Language.EN);
    }

    @Test
    public void testGetLocalization() {
        String tag = "TITLE";
        String localization = ENGINE.LanguageManager().getLocalization(tag);
        assertNotNull(localization);
        localization = ENGINE.LanguageManager().getLocalization(tag, Language.ES);
        assertNotNull(localization);
        tag = "new game text";
        localization = ENGINE.LanguageManager().getLocalization(tag);
        assertNotNull(localization);
    }

    @Test
    public void testLanguageManagerCleanup() {
        ENGINE.LanguageManager().cleanup();
        ManagerState ms = ENGINE.LanguageManager().getState();
        assertEquals(ManagerState.INACTIVE, ms);
    }

    @Test
    public void testLanguageManagerToJson() {
        JSONObject json = ENGINE.LanguageManager().toJson();
        assertNotNull(json);
        String key = json.getKey();
        assertEquals("LanguageManager", key);
    }

}