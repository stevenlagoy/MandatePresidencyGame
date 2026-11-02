package com.stevenlagoy.presidency.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.LanguageManager.Language;
import com.stevenlagoy.presidency.core.Manager.ManagerState;

public final class LanguageTest {

    static final Engine engine = new Engine();

    @BeforeEach
    public void initLanguageManager() {
        if (engine.getManager(LanguageManager.class).getState() != ManagerState.ACTIVE) {
            engine.getManager(LanguageManager.class).init();
            if (engine.getManager(LanguageManager.class).getState() != ManagerState.ACTIVE) {
                fail("Failed to initialize LanguageManager, necessary for testing");
            }
        }
    }

    @AfterEach
    public void resetLanguageManager() {
        engine.getManager(LanguageManager.class).cleanup();
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
        Language l = engine.getManager(LanguageManager.class).getGameLanguage();
        assertEquals(Language.defaultLanguage, l);
    }

    @Test
    public void testSetGameLanguage() {
        engine.getManager(LanguageManager.class).setGameLanguage(Language.ES);
        Language l = engine.getManager(LanguageManager.class).getGameLanguage();
        assertEquals(Language.ES, l);
        engine.getManager(LanguageManager.class).setGameLanguage(Language.EN);
    }

    @Test
    public void testGetLocalization() {
        String tag = "TITLE";
        String localization = engine.getManager(LanguageManager.class).getLocalization(tag);
        assertNotNull(localization);
        localization = engine.getManager(LanguageManager.class).getLocalization(tag, Language.ES);
        assertNotNull(localization);
        tag = "new game text";
        localization = engine.getManager(LanguageManager.class).getLocalization(tag);
        assertNotNull(localization);
    }

    @Test
    public void testLanguageManagerCleanup() {
        engine.getManager(LanguageManager.class).cleanup();
        ManagerState ms = engine.getManager(LanguageManager.class).getState();
        assertEquals(ManagerState.INACTIVE, ms);
    }

    @Test
    public void testLanguageManagerToJson() {
        JSONObject json = engine.getManager(LanguageManager.class).toJson();
        assertNotNull(json);
        String key = json.getKey();
        assertEquals("LanguageManager", key);
    }

}
