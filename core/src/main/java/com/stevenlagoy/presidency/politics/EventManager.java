/*
 * EventManager.java
 * Steven LaGoy
 * Created: 10 December 2024 at 8:21 AM
 * Modified: 26 August 2025
 */

package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.Logger;
import org.jetbrains.annotations.NotNull;

public class EventManager extends Manager {

    private final Engine ENGINE;
    private ManagerState currentState;

    public EventManager(Engine engine) {
        this.ENGINE = engine;
        currentState = ManagerState.INACTIVE;
    }

    // MANAGER METHODS
    // ----------------------------------------------------------------------------

    @Override
    public boolean init() {
        boolean successFlag = true;
        double startTime = ENGINE.getProgramTime();
        Logger.log(String.format("%s starting at %f", this.getClass().getSimpleName(), startTime));
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        double endTime = ENGINE.getProgramTime();
        Logger.log(String.format("%s initialized %s at %f. Elapsed: %f", this.getClass().getSimpleName(),
                successFlag ? "successfully" : "unsuccessfully", endTime, endTime - startTime));
        return successFlag;
    }

    @NotNull
    @Override
    public ManagerState getState() {
        return currentState;
    }

    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        currentState = ManagerState.INACTIVE;
        if (!successFlag)
            currentState = ManagerState.ERROR;
        return successFlag;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }

    @Override
    public Manager fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    /*
     * EVENTS
     * Scripted / Scheduled:
     * - Primary and Caucus Conventions
     * - National Conventions
     * - Federal and popular holidays:
     * - New Years Day
     * - Martin Luther King Jr. Day
     * - Presidents' Day
     * - Memorial Day
     * - Flag Day
     * - Juneteenth
     * - Independence Day
     * - Labor Day
     * - Day of Commemoration (9/11)
     * - Columbus / Indigenous Peoples' Day
     * - Veterans Day
     * - Thanksgiving Day
     * - Christmas Day
     * - Debates
     *
     * Incidental / Random:
     *
     */

}
