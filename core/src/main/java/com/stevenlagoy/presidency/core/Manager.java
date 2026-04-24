package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for game Managers, like MapManager, CharacterManager, etc.
 * Managers should hold an internal ManagerState which tracks the state of the
 * Manager.
 * Any class extending Manager should set the ManagerState to {@code INACTIVE}
 * when instantiated or after {@link #cleanup()}, {@code ACTIVE} following {@link #init()},
 * and {@code ERROR} when any fatal error is encountered.
 */
public abstract class Manager implements Jsonic<Manager> {

    /** Possible internal States of a Manager. */
    public enum ManagerState {
        /** Manager is initialized, prepared, and ready to receive messages. */
        ACTIVE,
        /** Manager is uninitialized and not yet ready to receive messages. @see {@link #init()} */
        INACTIVE,
        /** Manager has encountered a fatal error which must be resolved. */
        ERROR;
    }

    /**
     * Initialize the Manager, setting the internal ManagerState to {@code ACTIVE} when
     * successful. Returns a flag for success. Failure should result in an {@code ERROR} state.
     */
    public abstract boolean init();

    /** Get the internal state of the Manager. */
    public abstract @NotNull ManagerState getState();

    /**
     * Perform cleanup operations on this Manager's internal data, deallocating
     * members and setting the internal state to {@code INACTIVE}.
     */
    public abstract boolean cleanup();

    @Override
    public abstract JSONObject toJson();

    @Override
    public abstract Manager fromJson(JSONObject json);

}
