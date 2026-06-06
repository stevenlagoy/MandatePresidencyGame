package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.characters.CharacterManager;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.map.MapManager;
import com.stevenlagoy.presidency.politics.EventManager;
import com.stevenlagoy.presidency.politics.PoliticsManager;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.IOUtils;
import com.stevenlagoy.presidency.util.Logger;
import com.stevenlagoy.presidency.util.NumberUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

/**
 * <h1>ENGINE</h1>
 * {@code ~/core/Engine.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy                  <br>
 *     <b>Created: </b> 26 September 2024 at 12:21 AM <br>
 *     <b>Modified:</b> 02 June 2026                  <br>
 * </p>
 *
 * Engine is the main driver of the game engine, facilitating the initialization and function of
 * the game by tracking critical details for game settings and other information.
 *
 * @author Steven LaGoy
 */
public final class Engine extends Manager {

    private static Engine instance = null;
    public static Engine getInstance() {
        return instance;
    }

    // Constants

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

        public static @NotNull Difficulty level(int value) {
            for (Difficulty diff : Difficulty.values())
                if (diff.value == value)
                    return diff;
            throw new IllegalArgumentException("Invalid difficulty level: " + value);
        }
    }

    // Instance Fields

    // Submanagers
    public final LanguageManager LANGUAGE_MANAGER;
    public final TimeManager TIME_MANAGER;
    public final EventManager EVENT_MANAGER;
    public final DemographicsManager DEMOGRAPHICS_MANAGER;
    public final MapManager MAP_MANAGER;
    public final PoliticsManager POLITICS_MANAGER;
    public final CharacterManager CHARACTER_MANAGER;

    /**
     * Whether this Engine is running in debug mode. {@code true} means debug mode is active and
     * additional debug logging and logic will be enabled.
     */
    public final boolean DEBUG_MODE;

    /** Current difficulty level of the game. */
    private @NotNull Difficulty gameDifficulty;

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
     * The Base Speed of the game, representing the minimum tick time in milliseconds
     */
    public static final long baseSpeed = 125L;
    /*
        Speed 1: 1 sec -> 1 min
        Speed 2: 1 sec -> 10 min
        Speed 3: 1 sec -> 30 mins
        Speed 4: 1 sec -> 1 hr
        Speed 5: 1 sec -> 3 hr
     */
    public static final long[] speedSettings = { baseSpeed, baseSpeed * 2, baseSpeed * 3, baseSpeed * 4, baseSpeed * 5 }; // Time in between ticks
    private static int speedSetting = 4;
    private static long tickSpeed = speedSettings[speedSetting];


    // Constructors

    /**
     * Create an Engine in normal (not debug) mode.
     * @see #Engine(boolean, Difficulty)
     */
    public Engine() {
        this(false, Difficulty.LEVEL_1);
    }

    /**
     * Create an engine in either normal or debug mode.
     * @param debug Whether to start the engine in debug mode.
     * @see #Engine(boolean, Difficulty)
     */
    public Engine(boolean debug) {
        this(debug, Difficulty.LEVEL_1);
    }

    /**
     * Create an Engine with the given mode. Create but do not initialize all managers. If an error
     * is encountered while constructing a manager, {@link #getState()} will return
     * {@code ManagerState.ERROR}.
     * @param debug Start in debug mode when {@code true}, start in normal mode when {@code false}.
     */
    public Engine(boolean debug, @NotNull Difficulty difficulty) {
        t_zero = System.nanoTime();
        DEBUG_MODE = debug;
        this.gameDifficulty = difficulty;
        LANGUAGE_MANAGER     = new LanguageManager(this, this);
        TIME_MANAGER         = new TimeManager(this, this);
        EVENT_MANAGER        = new EventManager(this, this);
        DEMOGRAPHICS_MANAGER = new DemographicsManager(this, this);
        MAP_MANAGER          = new MapManager(this, this);
        POLITICS_MANAGER     = new PoliticsManager(this, this);
        CHARACTER_MANAGER    = new CharacterManager(this, this);
        for (Manager manager : getSubManagers()) {
            if (manager.getState().equals(ManagerState.ERROR)) {
                onError(new Exception(manager.getClass().getSimpleName() + " could not be constructed."));
            }
        }
        Engine.instance = this;
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull Set<Manager> getSubManagers() {
        return Set.of(LANGUAGE_MANAGER, TIME_MANAGER, EVENT_MANAGER, DEMOGRAPHICS_MANAGER, MAP_MANAGER, POLITICS_MANAGER, CHARACTER_MANAGER);
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doCleanup() {
    }

    // Serialization Methods

    @Override
    public @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    public void doFromJson(@NotNull JSONObject json) {
    }

    // Instance Methods

    public @NotNull Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameDifficulty(@NotNull Difficulty difficulty) {
        gameDifficulty = difficulty;
    }

    public static int getSpeedSetting() {
        return speedSetting;
    }

    public static void setSpeedSetting(int speed) {
        speedSetting = NumberUtils.clamp(speed, 0, speedSettings.length - 1);
        tickSpeed = speedSettings[speedSetting];
    }

    public static long getTickSpeed() {
        return tickSpeed;
    }

    public void writeGameState() {
        // Get name for the file
        String fileName;
        try {
            String playerCharacterName = CHARACTER_MANAGER.getPlayer().getName().getCommonName();
            String currentTime = TIME_MANAGER.getFormattedCurrentDate();
            fileName = String.format("%s %s", playerCharacterName, currentTime);
        }
        catch (NullPointerException e) {
            // No player character. Use current real time
            fileName = new SimpleDateFormat("yyyy-MMM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        }

        // Generate save string
        String saveString = String.format("{%n\t%s%n}", this.toJson().toString().replace("\n", "\n\t"));

        // Write to save file with name, or to stdout if unsuccessful.
        try {
            PrintWriter saveWriter = IOUtils.createWriter(FilePaths.SAVES_DIR.resolve(fileName + IOUtils.FileExtension.JSON.extension).toFile());
            saveWriter.print(saveString);
            saveWriter.close(); // Flush and close
        }
        catch (IOException e) {
            IOUtils.stdout.print(saveString);
            Logger.error("EXCEPTION DURING SAVE WRITE",
                    "An exception occurred while writing a save. The save data has been written to the standard output file.", e);
        }
    }

}
