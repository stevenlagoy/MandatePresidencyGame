/*
 * Engine.java
 * Steven LaGoy
 * Created: 26 September 2024 at 12:21 AM
 * Modified: 30 May 2025
 */

package com.stevenlagoy.presidency.core;

// IMPORTS ----------------------------------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import com.stevenlagoy.presidency.characters.CharacterManager;
import com.stevenlagoy.presidency.characters.attributes.names.NameManager;
import com.stevenlagoy.presidency.data.Jsonic;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.graphics.GraphicsManager;
import com.stevenlagoy.presidency.map.MapManager;
import com.stevenlagoy.presidency.politics.EventManager;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.IOUtil;
import com.stevenlagoy.presidency.util.Logger;

import core.JSONObject;

// ENGINE -----------------------------------------------------------------------------------------
/**
 * {@code Engine} is the main driver of the game engine, facilitating the
 * initialization and function
 * of the game by tracking critical details for game settings and other
 * information.
 */
public final class Engine extends Manager {

    // Language Manager
    private final LanguageManager LANGUAGE_MANAGER;

    public LanguageManager LanguageManager() {
        return LANGUAGE_MANAGER;
    }

    // Time Manager
    private final TimeManager TIME_MANAGER;

    public TimeManager TimeManager() {
        return TIME_MANAGER;
    }

    // Event Manager
    private final EventManager EVENT_MANAGER;

    public EventManager EventManager() {
        return EVENT_MANAGER;
    }

    // Demographics Manager
    private final DemographicsManager DEMOGRAPHICS_MANAGER;

    public DemographicsManager DemographicsManager() {
        return DEMOGRAPHICS_MANAGER;
    }

    // Map Manager
    private final MapManager MAP_MANAGER;

    public MapManager MapManager() {
        return MAP_MANAGER;
    }

    // Name Manager
    private final NameManager NAME_MANAGER;

    public NameManager NameManager() {
        return NAME_MANAGER;
    }

    // Character Manager
    private final CharacterManager CHARACTER_MANAGER;

    public CharacterManager CharacterManager() {
        return CHARACTER_MANAGER;
    }

    // Graphics Manager
    private final GraphicsManager GRAPHICS_MANAGER;

    public GraphicsManager GraphicsManager() {
        return GRAPHICS_MANAGER;
    }

    private final List<Manager> managers;

    public final boolean DEBUG_MODE = true;
    private ManagerState currentState;

    /**
     * Start time for the program. Get current program time with
     * Main.Engine().getProgramTime()
     */
    public final long t_zero;

    /**
     * Get the current elapsed program time in seconds. Equivalent to:
     * {@code (System.nanoTime() - Main.Engine().t_zero) / 1e-9;}
     */
    public double getProgramTime() {
        return (System.nanoTime() - t_zero) * 1e-9;
    }

    public Engine() {
        t_zero = System.nanoTime();
        currentState = ManagerState.INACTIVE;
        LANGUAGE_MANAGER = new LanguageManager(this);
        TIME_MANAGER = new TimeManager(this);
        EVENT_MANAGER = new EventManager(this);
        DEMOGRAPHICS_MANAGER = new DemographicsManager(this);
        MAP_MANAGER = new MapManager(this);
        NAME_MANAGER = new NameManager(this);
        CHARACTER_MANAGER = new CharacterManager(this);
        GRAPHICS_MANAGER = new GraphicsManager(this);
        managers = List.of(
                LANGUAGE_MANAGER,
                TIME_MANAGER,
                EVENT_MANAGER,
                DEMOGRAPHICS_MANAGER,
                MAP_MANAGER,
                NAME_MANAGER,
                CHARACTER_MANAGER,
                GRAPHICS_MANAGER);
        for (Manager manager : managers) {
            if (manager.getState().equals(ManagerState.ERROR)) {
                Logger.log("FATAL: FAILURE TO CONSTRUCT MANAGER",
                        manager.getClass().getSimpleName() + " could not be constructed.", new Exception());
                currentState = ManagerState.ERROR;
            }
        }
    }

    // MANAGER METHODS
    // ----------------------------------------------------------------------------

    /** Initialize and Activate this Engine, and its Managers. */
    @Override
    public boolean init() {
        boolean successFlag = true;
        double startTime = getProgramTime();
        Logger.log(String.format("%s starting at %f", this.getClass().getSimpleName(), startTime));
        for (Manager manager : managers) {
            if (!manager.init())
                successFlag = false;
        }
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        double endTime = getProgramTime();
        Logger.log(String.format("%s initialized %s at %f. Elapsed: %f", this.getClass().getSimpleName(),
                successFlag ? "successfully" : "unsuccessfully", endTime, endTime - startTime));
        return successFlag;
    }

    /** Get the current State of this Engine. */
    @Override
    public ManagerState getState() {
        return currentState;
    }

    /** Deactivate and clean up the data of this Engine, and its Managers. */
    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        currentState = ManagerState.INACTIVE;
        for (Manager manager : managers) {
            if (!manager.cleanup())
                successFlag = false;
        }
        if (!successFlag)
            currentState = ManagerState.ERROR;
        return successFlag;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTANTS AND ENUMS //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // DIFFICULTY
    // ---------------------------------------------------------------------------------

    /** Current difficulty level of the game. */
    private static Difficulty gameDifficulty;

    public static Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public static boolean setGameDifficulty(Difficulty difficulty) {
        return (gameDifficulty = difficulty) != null;
    }

    /**
     * Dificulty values impact some calculations, impacting the difficulty of the
     * game.
     */
    public static enum Difficulty {

        LEVEL_1(1, "Aspiring Politician"),
        LEVEL_2(2, "Fledgling Politician"),
        LEVEL_3(3, "Hometown Hero"),
        LEVEL_4(4, "Career Politican"),
        LEVEL_5(5, "Political Machine");

        public final int value;
        public final String name;

        private Difficulty(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public static Difficulty level(int value) {
            for (Difficulty diff : Difficulty.values())
                if (diff.value == value)
                    return diff;
            throw new IllegalArgumentException("Invalid difficulty level: " + value);
        }

        public static Difficulty label(String label) {
            String target = label.trim().toUpperCase().replace("\\s", "_");
            for (Difficulty diff : Difficulty.values())
                if (diff.toString().equals(target))
                    return diff;
            throw new IllegalArgumentException("Invalid difficulty label: " + label);
        }

        public static Difficulty name(String name) {
            for (Difficulty diff : Difficulty.values())
                if (diff.name.equals(name))
                    return diff;
            throw new IllegalArgumentException("Invalid difficulty name: " + name);
        }
    }

    // GAME SPEED SETTINGS
    // ------------------------------------------------------------------------
    /**
     * The Base Speed of the game, representing the minimum tick time in miliseconds
     */
    public static final long baseSpeed = 125L;
    public static final long[] speedSettings = { baseSpeed, baseSpeed * 2, baseSpeed * 4, baseSpeed * 8,
            baseSpeed * 16 }; // Time in between ticks
    private static int speedSetting = 4;

    public static int getSpeedSetting() {
        return speedSetting;
    }

    public static void setSpeedSetting(int speed) {
        speedSetting = Math.clamp(speed, 0, speedSettings.length - 1);
        tickSpeed = speedSettings[speedSetting];
    }

    private static long tickSpeed = speedSettings[speedSetting];

    public static long getTickSpeed() {
        return tickSpeed;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // GAME SETUP //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // public boolean init() {
    // boolean successFlag = true;
    // try {
    // long startTime = System.nanoTime();
    // GLFW.glfwSetErrorCallback(errorCallback =
    // GLFWErrorCallback.createPrint(System.err));
    // reset();
    // window = new Window(Consts.TITLE, 0, 0, false);
    // gameLogic = new TestGame();
    // mouse = new MouseInput();
    // window.init();
    // gameLogic.init();
    // mouse.init();

    // long elapsedTime = System.nanoTime() - startTime;
    // Logger.log("Engine initialization complete in " + String.valueOf(elapsedTime
    // / 1000000) + " miliseconds.");
    // }
    // catch (Exception e) {
    // // Engine.cleanup();
    // Logger.log("Failed to initialize game engine.");
    // Logger.log(e);
    // successFlag = false;
    // }
    // return successFlag;
    // }

    // public void run() {
    // Engine.isRunning = true;
    // int frames = 0;
    // long frameCounter = 0;
    // long lastTime = System.nanoTime();
    // double unprocessedTime = 0;

    // while (isRunning) {
    // boolean render = false;
    // long startTime = System.nanoTime();
    // long passedTime = startTime - lastTime;
    // lastTime = startTime;

    // unprocessedTime += passedTime / (double) NANOSECOND;
    // frameCounter += passedTime;

    // Engine.input();

    // while (unprocessedTime > frametime) {
    // render = true;
    // unprocessedTime -= frametime;

    // if (window.windowShouldClose())
    // stop();

    // if (frameCounter >= NANOSECOND) {
    // fps = frames;
    // window.setTitle(Consts.TITLE + " - FPS: " + fps);
    // frames = 0;
    // frameCounter = 0;
    // }
    // }

    // if (render) {
    // update(frametime);
    // render();
    // frames++;
    // }
    // }
    // cleanup();
    // }

    // public boolean reset() {
    // boolean successFlag = true;
    // successFlag = successFlag && Logger.writeErrorToLog();
    // successFlag = successFlag && Logger.clearErrorFile();
    // log("RESET", "Reset Engine");
    // return successFlag;
    // }

    // public void stop() {
    // if(!isRunning) return;
    // window.cleanup();
    // gameLogic.cleanup();
    // errorCallback.free();
    // GLFW.glfwTerminate();
    // Engine.isRunning = false;
    // Logger.log("DONE");
    // Logger.writeErrorToLog();
    // }

    public boolean tick() {
        GRAPHICS_MANAGER.input();
        boolean active = true;

        // System.out.println(DateManager.currentGameDate);
        active = active && TIME_MANAGER.incrementQuarterHour();
        return active;
    }

    // public void writeSave() {
    // if (CharacterManager.getPlayer() == null) return;
    // try {
    // String saveName = String.format("%s - %s",
    // CharacterManager.getPlayer().getName().getLegalName(),
    // TIME_MANAGER.formattedCurrentDate());
    // File saveFile = new File(FilePaths.SAVES_DIR.toString() + saveName + ".txt");
    // for (int i = 1; saveFile.exists(); i++) {
    // saveName = String.format("%s - %s %s",
    // CharacterManager.getPlayer().getName().getLegalName(),
    // TIME_MANAGER.formattedCurrentDate(), String.format("(%d)", i));
    // saveFile = new File(FilePaths.SAVES_DIR.toString() + saveName + ".txt");
    // }
    // saveFile.createNewFile();
    // FileWriter fw = new FileWriter(saveFile);
    // fw.append(CharacterManager.generateSaveString());
    // fw.close();
    // }
    // catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // public void cleanup() {
    // window.cleanup();
    // gameLogic.cleanup();
    // errorCallback.free();
    // GLFW.glfwTerminate();
    // }

    public static List<String> listSaveNames() {
        return new ArrayList<>();
    }

    public static void readSave(String saveName) {

    }

    // REPRESENTATION METHODS
    // ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

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
                if (field.get(this) instanceof Jsonic jvalue) {
                    fields.add(new JSONObject(fieldName, jvalue.toJson()));
                } else
                    fields.add(new JSONObject(fieldName, field.get(this)));
            }
            return new JSONObject(this.getClass().getSimpleName(), fields);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            currentState = ManagerState.ERROR;
            Logger.log("JSON SERIALIZATION ERROR", "Failed to serialize " + getClass().getSimpleName() + " to JSON.",
                    e);
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
                } else {
                    // For other types, set directly (may need conversion for complex types)
                    field.set(this, value);
                }
            } catch (Exception e) {
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
            String playerCharacterName = CHARACTER_MANAGER.getPlayer().getName().getCommonName();
            String currentTime = TIME_MANAGER.getFormattedCurrentDate();
            fileName = String.format("%s %s", playerCharacterName, currentTime);
        } catch (NullPointerException e) {
            // No player character. Use current real time
            fileName = new SimpleDateFormat("yyyy-MMM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        }

        // Generate savestring
        String saveString = String.format("{%n\t%s%n}", this.toJson().toString().replace("\n", "\n\t"));

        // Write to save file with name, or to output file if unsuccessful.
        try {
            PrintWriter saveWriter = IOUtil
                    .createWriter(FilePaths.SAVES_DIR.resolve(fileName + IOUtil.Extension.JSON.extension).toFile());
            saveWriter.print(saveString);
            saveWriter.close(); // Flush and close
        } catch (IOException e) {
            IOUtil.stdout.print(saveString);
            Logger.log("EXCEPTION DURING SAVE WRITE",
                    "An exception occurred while writing a save. The save data has been written to the standard output file.",
                    e);
        }
    }

}
