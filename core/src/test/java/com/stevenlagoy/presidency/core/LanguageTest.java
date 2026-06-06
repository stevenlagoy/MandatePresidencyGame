package com.stevenlagoy.presidency.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.LanguageManager.Language;
import com.stevenlagoy.presidency.core.Manager.ManagerState;

public final class LanguageTest {

    static final Engine ENGINE = new Engine();

    @BeforeEach
    public void initLanguageManager() {
        if (ENGINE.LANGUAGE_MANAGER.getState() != ManagerState.ACTIVE) {
            ENGINE.LANGUAGE_MANAGER.init();
            if (ENGINE.LANGUAGE_MANAGER.getState() != ManagerState.ACTIVE) {
                fail("Failed to initialize LanguageManager, necessary for testing");
            }
        }
    }

    @AfterEach
    public void resetLanguageManager() {
        ENGINE.LANGUAGE_MANAGER.cleanup();
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
        Language l = Language.valueOf(label);
        assertEquals(Language.EN, l);
    }

    @Test
    public void testLanguageFromInvalidLabelThrows() {
        String label = "XX";
        assertThrows(IllegalArgumentException.class, () -> Language.valueOf(label));
    }

    @Test
    public void testGetGameLanguage() {
        Language l = ENGINE.LANGUAGE_MANAGER.getGameLanguage();
        assertEquals(Language.defaultLanguage, l);
    }

    @Test
    public void testSetGameLanguage() {
        ENGINE.LANGUAGE_MANAGER.setGameLanguage(Language.ES);
        Language l = ENGINE.LANGUAGE_MANAGER.getGameLanguage();
        assertEquals(Language.ES, l);
        ENGINE.LANGUAGE_MANAGER.setGameLanguage(Language.EN);
    }

    @Test
    public void testGetLocalization() {
        String tag = "TITLE";
        String localization = ENGINE.LANGUAGE_MANAGER.getLocalization(tag);
        assertNotNull(localization);
        localization = ENGINE.LANGUAGE_MANAGER.getLocalization(tag, Language.ES);
        assertNotNull(localization);
        tag = "new game text";
        localization = ENGINE.LANGUAGE_MANAGER.getLocalization(tag);
        assertNotNull(localization);
    }

    @Test
    public void testLanguageManagerCleanup() {
        ENGINE.LANGUAGE_MANAGER.cleanup();
        ManagerState ms = ENGINE.LANGUAGE_MANAGER.getState();
        assertEquals(ManagerState.INACTIVE, ms);
    }

    @Test
    public void testLanguageManagerToJson() {
        JSONObject json = ENGINE.LANGUAGE_MANAGER.toJson();
        assertNotNull(json);
        String key = json.getKey();
        assertEquals("LanguageManager", key);
    }

}
