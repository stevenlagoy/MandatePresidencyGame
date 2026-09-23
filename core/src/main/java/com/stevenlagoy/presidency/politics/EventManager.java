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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <h1>EVENT MANAGER</h1>
 * {@code ~/politics/EventManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy                <br>
 *     <b>Created: </b> 10 December 2026 at 8:21 AM <br>
 *     <b>Modified:</b> 02 June 2026                <br>
 * </p>
 *
 * EventManager is responsible for creating and managing events, including scripted and scheduled
 * events as well as incidental and random events.
 *
 * @author Steven LaGoy
 */
public class EventManager extends Manager {

    // Constructor

    public EventManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    public void doInit() {
    }

    @Override
    protected void doCleanup() {
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
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
