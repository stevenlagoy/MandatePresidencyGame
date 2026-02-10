package com.stevenlagoy.presidency.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.LanguageManager.Language;
import com.stevenlagoy.presidency.core.Manager.ManagerState;

public final class LanguageTest {
    
    static final Engine ENGINE = new Engine();

    @BeforeAll
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

    @Test
    public void testLanguageFromInvalidNameThrows() {
        String name = "Invalid Language";
        assertThrows(IllegalArgumentException.class, () -> Language.fromName(name));
    }

    @Test
    public void testLanguageFromLabel() {
        String label = "EN";
        Language l = Language.label(label);
        assertEquals(Language.EN, l);
    }

    @Test
    public void testLanguageFromInvalidLabelThrows() {
        String label = "XX";
        assertThrows(IllegalArgumentException.class, () -> Language.label(label));
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