package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.util.FilePaths;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <h1>LANGUAGE MANAGER</h1>
 * {@code ~/core/LanguageManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy   <br>
 *     <b>Created: </b> 26 August 2025 <br>
 *     <b>Modified:</b> 02 June 2026   <br>
 * </p>
 *
 * The LanguageManager tracks the current game language and provides localization for tags.
 */
public final class LanguageManager extends Manager {

    // Static Variables

    /** Possible languages in which to display game text. */
    public enum Language {

        EN("English"),
        ZH("简体中文"),
        RU("Русский"),
        ES("Español"),
        PT("Português"),
        DE("Deutsch"),
        FR("Français"),
        JA("日本語"),
        PL("Polski"),
        TR("Türkçe");

        public final String name;
        Language(String name) {
            this.name = name;
        }

        public static @NotNull Language fromName(String name) throws IllegalArgumentException {
            for (Language lang : Language.values())
                if (lang.name.equals(name))
                    return lang;
            throw new IllegalArgumentException("Invalid language name: " + name);
        }

        public static final Language defaultLanguage = Language.EN;
    }

    // Instance Fields

    /** Current language of the game. */
    private @NotNull Language gameLanguage;
    /** For each language, stores tag : sentence pairs for localization tags. */
    public final @NotNull Map<Language, Map<String, String>> localizations;

    // Constructor

    /** Create an inactive LanguageManager with default values. */
    public LanguageManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        gameLanguage = Language.defaultLanguage;
        localizations = new HashMap<>();
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
        loadLocalizations(gameLanguage);
    }

    @Override
    protected void doCleanup() {
        gameLanguage = Language.defaultLanguage;
        localizations.clear();
    }

    @Override
    protected void onDegraded(Exception e) {
        super.onDegraded(e);
        transitionTo(ManagerState.ACTIVE);
    }

    @Override
    protected void onError(Exception e) {
        super.onError(e);
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName(), List.of(
            new JSONObject("gameLanguage", gameLanguage.name)
        ));
    }

    @Override
    public void doFromJson(@NotNull JSONObject json) {
        try {
            gameLanguage = Language.valueOf(json.get("gameLanguage", String.class));
        } catch (IllegalArgumentException e) {
            onDegraded(e);
            gameLanguage = Language.defaultLanguage;
        }
    }

    // Getters and Setters

    // Game Language : Language
    public @NotNull Language getGameLanguage() {
        requireOperational();
        return gameLanguage;
    }

    /**
     * Loads localization for the language, and if successful sets the game language.
     */
    public void setGameLanguage(@NotNull Language language) {
        requireOperational();
        gameLanguage = language;
        loadLocalizations(language);
    }

    // Localizations : Map of String to String
    /**
     * Get the localization for a given tag in the current game language.
     *
     * @see #getLocalization(String, Language)
     */
    public @NotNull String getLocalization(@NotNull String tag) {
        requireOperational();
        return getLocalization(tag, gameLanguage);
    }

    /**
     * Get the localization for a given tag in the given language. Will attempt to find
     * localization for the tag as passed, in lower and upper case, with whitespaces changed to
     * underscores, and with all combinations of the above. If unsuccessful, the original tag will
     * be returned and the failure will be logged.
     */
    public @NotNull String getLocalization(@NotNull String tag, @NotNull Language language) {
        requireOperational();
        loadLocalizations(language);
        Map<String, String> langLocs = localizations.get(language);
        // Try localizing with several variations of the tag
        String res = langLocs.get(tag);
        res = res == null ? langLocs.get(tag.toLowerCase()) : res;
        res = res == null ? langLocs.get(tag.toUpperCase()) : res;
        res = res == null ? langLocs.get(tag.replace(" ", "_")) : res;
        res = res == null ? langLocs.get(tag.replace(" ", "_").toLowerCase()) : res;
        res = res == null ? langLocs.get(tag.replace(" ", "_").toUpperCase()) : res;
        if (res != null) return res;
        // Could not localize
        onDegraded(new IllegalArgumentException("Could not localize tag " + tag + " into language " + language));
        return tag;
    }

    /** Load the localizations for a given language. */
    public void loadLocalizations(Language language) {
        requireState(ManagerState.INITIALIZING, ManagerState.ACTIVE, ManagerState.PAUSED, ManagerState.DEGRADED);
        if (localizations.containsKey(language)) return;

        HashMap<String, String> local = new HashMap<>();

        Path localizationFile = Path.of(String.format("%s/%s/%s%s", FilePaths.LOCALIZATION_RESOURCES, language,
                language, FilePaths.SYSTEM_TEXT_LOC));
        JSONObject localizationData = JSONProcessor.processJson(localizationFile);

        for (Object entry : localizationData.getAsList()) {
            if (entry instanceof JSONObject entryJson) {
                local.put(entryJson.getKey(), entryJson.getAsString());
            }
        }
        localizations.put(language, local);
    }
}
