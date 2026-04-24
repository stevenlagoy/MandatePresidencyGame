package com.stevenlagoy.presidency.core;

// IMPORTS ----------------------------------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.jsonic.JSONObject;

import com.stevenlagoy.presidency.characters.attributes.names.NameManager;
import com.stevenlagoy.presidency.characters.CharacterManager;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.map.MapManager;
import com.stevenlagoy.presidency.map.travel.TravelManager;
import com.stevenlagoy.presidency.politics.EventManager;
import com.stevenlagoy.presidency.politics.PartyManager;
import com.stevenlagoy.presidency.politics.PoliticsManager;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.IOUtils;
import com.stevenlagoy.presidency.util.Logger;
import com.stevenlagoy.presidency.util.NumberUtils;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>ENGINE</h1>
 * {@code ~/core/Engine.java}
 * <p>
 * <b>Author:</b>
 * Steven LaGoy
 * <br>
 * <b>Created:</b>
 * 26 September 2024 at 12:21 AM
 * <br>
 * <b>Modified:</b>
 * 10 February 2026
 * <br>
 * </p>
 *
 * Engine is the main driver of the game engine, facilitating the initialization and function of
 * the game by tracking critical details for game settings and other information.
 */
public final class Engine extends Manager {

    public static Engine currentInstance;

    public static final class Managers {

        public Managers(Engine engine) {
            LANGUAGE_MANAGER = new LanguageManager(engine);
            TIME_MANAGER = new TimeManager(engine);
            EVENT_MANAGER = new EventManager(engine);
            DEMOGRAPHICS_MANAGER = new DemographicsManager(engine);
            MAP_MANAGER = new MapManager(engine);
            POLITICS_MANAGER = new PoliticsManager(engine);
            TRAVEL_MANAGER = new TravelManager(engine);
            NAME_MANAGER = new NameManager(engine);
            CHARACTER_MANAGER = new CharacterManager(engine);
            PARTY_MANAGER = new PartyManager(engine);
        }

        // Language Manager
        public final LanguageManager LANGUAGE_MANAGER;

        // Time Manager
        public final TimeManager TIME_MANAGER;

        // Event Manager
        public final EventManager EVENT_MANAGER;

        // Demographics Manager
        public final DemographicsManager DEMOGRAPHICS_MANAGER;

        // Map Manager
        public final MapManager MAP_MANAGER;

        // Politics Manager
        public final PoliticsManager POLITICS_MANAGER;

        // Travel Manager
        public final TravelManager TRAVEL_MANAGER;

        // Name Manager
        public final NameManager NAME_MANAGER;

        // Character Manager
        public final CharacterManager CHARACTER_MANAGER;

        // Party Manager
        public final PartyManager PARTY_MANAGER;

        /** Set of Managers held by this Engine. Order of managers should be treated as arbitrary. */
        public Set<Manager> getManagers() { return Set.of(
            LANGUAGE_MANAGER,
            TIME_MANAGER,
            EVENT_MANAGER,
            DEMOGRAPHICS_MANAGER,
            MAP_MANAGER,
            POLITICS_MANAGER,
            TRAVEL_MANAGER,
            NAME_MANAGER,
            CHARACTER_MANAGER,
            PARTY_MANAGER
        ); }
    }

    public final Managers MANAGERS;

    /** Whether this Engine is running in debug mode. {@code true} means debug mode is active and
     * additional debug logging and logic will be enabled.
     */
    public final boolean DEBUG_MODE;

    /** Current state of this Engine. */
    private ManagerState currentState;

    /**
     * Start time for the program. Get current program time with
     * Main.Engine().getProgramTime()
     */
    public final long t_zero;

    /**
     * Get the current elapsed program time in seconds. Equivalent to:
     * {@code (System.nanoTime() - Main.Engine().t_zero) / 1e-9;}
     * @return Elapsed program time in seconds
     */
    public double getProgramTime() {
        return (System.nanoTime() - t_zero) * 1e-9;
    }

    /**
     * Create an Engine in normal (not debug) mode.
     * @see #Engine(boolean)
     */
    public Engine() {
        this(false);
    }

    /**
     * Create an Engine with the given mode. Create but do not initialize all managers. If an error
     * is encountered while constructing a manager, {@link #getState()} will return
     * {@code ManagerState.ERROR}.
     * @param debug Start in debug mode when {@code true}, start in normal mode when {@code false}.
     */
    public Engine(boolean debug) {
        t_zero = System.nanoTime();
        currentState = ManagerState.INACTIVE;
        DEBUG_MODE = debug;
        MANAGERS = new Managers(this);
        for (Manager manager : MANAGERS.getManagers()) {
            if (manager.getState().equals(ManagerState.ERROR)) {
                Logger.log("FATAL: FAILURE TO CONSTRUCT MANAGER",
                        manager.getClass().getSimpleName() + " could not be constructed.", new Exception());
                currentState = ManagerState.ERROR;
            }
        }
        currentInstance = this;
    }

    // MANAGER METHODS ----------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Initialize and Activate this Engine, and its Managers.
     * @return {@code true} if successfully initialized this Engine and its Managers, {@code false}
     * otherwise.
     */
    @Override
    public boolean init() {
        boolean successFlag = true;
        double startTime = getProgramTime();
        Logger.log(String.format("%s starting at %f", this.getClass().getSimpleName(), startTime));
        for (Manager manager : MANAGERS.getManagers()) {
            if (!manager.init())
                successFlag = false;
        }
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        double endTime = getProgramTime();
        Logger.log(String.format("%s initialized %s at %f. Elapsed: %f", this.getClass().getSimpleName(),
                successFlag ? "successfully" : "unsuccessfully", endTime, endTime - startTime));
        return successFlag;
    }

    /**
     * Get the current Manager State of this Engine.
     * @return Current Manager State
     */
    @NotNull
    @Override
    public ManagerState getState() {
        return currentState;
    }

    /**
     * Deactivate and clean up the data of this Engine, and its Managers.
     * @return {@code true} if successfully deactivated each held Manager and this Engine,
     * {@code false} otherwise. In the case of failure, {@link #getState()} will return
     * {@code ManagerState.ERROR}.
     */
    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        for (Manager manager : MANAGERS.getManagers()) {
            if (!manager.cleanup())
                successFlag = false;
        }
        currentState = ManagerState.INACTIVE;
        if (!successFlag)
            currentState = ManagerState.ERROR;
        return successFlag;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                    CONSTANTS AND ENUMS                                    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // DIFFICULTY ---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Difficulty values impact player-facing calculations, impacting the difficulty of the game.
     */
    public enum Difficulty {

        LEVEL_1(1, "Aspiring Politician"),
        LEVEL_2(2, "Fledgling Politician"),
        LEVEL_3(3, "Hometown Hero"),
        LEVEL_4(4, "Career Politician"),
        LEVEL_5(5, "Political Machine");

        public final int value;
        public final String name;

        Difficulty(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public static Difficulty level(int value) {
            for (Difficulty diff : Difficulty.values())
                if (diff.value == value)
                    return diff;
            throw new IllegalArgumentException("Invalid difficulty level: " + value);
        }
    }

    /** Current difficulty level of the game. */
    private static Difficulty gameDifficulty;

    public static Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public static boolean setGameDifficulty(Difficulty difficulty) {
        return (gameDifficulty = difficulty) != null;
    }

    // GAME SPEED SETTINGS ------------------------------------------------------------------------------------------------------------------------------------

    /**
     * The Base Speed of the game, representing the minimum tick time in milliseconds
     */
    public static final long baseSpeed = 125L;
    public static final long[] speedSettings = { baseSpeed, baseSpeed * 2, baseSpeed * 4, baseSpeed * 8,
            baseSpeed * 16 }; // Time in between ticks
    private static int speedSetting = 4;

    public static int getSpeedSetting() {
        return speedSetting;
    }

    public static void setSpeedSetting(int speed) {
        speedSetting = NumberUtils.clamp(speed, 0, speedSettings.length - 1);
        tickSpeed = speedSettings[speedSetting];
    }

    private static long tickSpeed = speedSettings[speedSetting];

    public static long getTickSpeed() {
        return tickSpeed;
    }

    public boolean tick() {
        boolean active = true;

        // System.out.println(DateManager.currentGameDate);
        active = active && MANAGERS.TIME_MANAGER.incrementQuarterHour();
        return active;
    }

    public static List<String> listSaveNames() {
        return new ArrayList<>();
    }

    public static void readSave(String saveName) {

    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    private static final Map<String, String> fieldsJsons = Map.of(
            "LANGUAGE_MANAGER", "language_manager",
            "TIME_MANAGER", "time_manager",
            "EVENT_MANAGER", "event_manager",
            "DEMOGRAPHICS_MANAGER", "demographics_manager",
            "MAP_MANAGER", "map_manager",
            "NAME_MANAGER", "name_manager",
            "CHARACTER_MANAGER", "character_manager",
            "GRAPHICS_MANAGER", "graphics_manager");

    @Override
    public JSONObject toJson() {
        try {
            List<JSONObject> fields = new ArrayList<>();
            for (String fieldName : fieldsJsons.keySet()) {
                Field field = getClass().getDeclaredField(fieldName);
                if (field.get(this) instanceof Jsonic jvalue)
                    fields.add(new JSONObject(fieldName, jvalue.toJson()));
                else
                    fields.add(new JSONObject(fieldName, field.get(this)));
            }
            return new JSONObject(this.getClass().getSimpleName(), fields);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            currentState = ManagerState.ERROR;
            Logger.log("JSON SERIALIZATION ERROR", "Failed to serialize " + getClass().getSimpleName() + " to JSON.", e);
            return null;
        }
    }

    @Override
    public Manager fromJson(JSONObject json) {
        currentState = ManagerState.INACTIVE;
        for (String fieldName : fieldsJsons.keySet()) {
            String jsonKey = fieldsJsons.get(fieldName);
            Object value = json.get(jsonKey);
            if (value == null)
                continue;
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isEnum()) {
                    // For enums, convert string to enum constant
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    Object enumValue = Enum.valueOf((Class<Enum>) type, value.toString());
                    field.set(this, enumValue);
                }
                else {
                    // For other types, set directly (may need conversion for complex types)
                    field.set(this, value);
                }
            }
            catch (Exception e) {
                currentState = ManagerState.ERROR;
                Logger.log("JSON DESERIALIZATION ERROR",
                        "Failed to set field " + fieldName + " in LanguageManager from JSON.", e);
            }
        }
        return this;
    }

    public void writeGameState() {
        // Get name for the file
        String fileName;
        try {
            String playerCharacterName = MANAGERS.CHARACTER_MANAGER.getPlayer().getName().getCommonName();
            String currentTime = MANAGERS.TIME_MANAGER.getFormattedCurrentDate();
            fileName = String.format("%s %s", playerCharacterName, currentTime);
        }
        catch (NullPointerException e) {
            // No player character. Use current real time
            fileName = new SimpleDateFormat("yyyy-MMM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        }

        // Generate save string
        String saveString = String.format("{%n\t%s%n}", this.toJson().toString().replace("\n", "\n\t"));

        // Write to save file with name, or to output file if unsuccessful.
        try {
            PrintWriter saveWriter = IOUtils.createWriter(FilePaths.SAVES_DIR.resolve(fileName + IOUtils.FileExtension.JSON.extension).toFile());
            saveWriter.print(saveString);
            saveWriter.close(); // Flush and close
        }
        catch (IOException e) {
            IOUtils.stdout.print(saveString);
            Logger.log("EXCEPTION DURING SAVE WRITE",
                    "An exception occurred while writing a save. The save data has been written to the standard output file.",
                    e);
        }
    }

}
