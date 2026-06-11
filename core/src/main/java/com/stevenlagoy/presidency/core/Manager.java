package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.util.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <h1><i>MANAGER</i></h1>
 * {@code ~/core/Manager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy     <br>
 *     <b>Created: </b> 28 December 2024 <br>
 *     <b>Modified:</b> 02 June 2026     <br>
 * </p>
 *
 * Abstract class for managers of game systems, which provide factory methods, track created
 * instances, allow searching objects, and methods for saving and loading state. May also have
 * submanagers which deal with one element of the system or a subsystem.
 */
public abstract class Manager implements Jsonic<Manager> {

    /** Possible internal States of a Manager. */
    public enum ManagerState {
        /**
         * Manager is uninitialized and not yet ready to receive messages.
         * May transition to {@link #INITIALIZING} via {@link #init()}.
         * Should reject all requests except for {@link #init()}.
         */
        INACTIVE,
        /**
         * Manager is currently being initialized and must reject all requests. Entered via
         * {@link #init()} and resolves to {@link #ACTIVE} or {@link #ERROR}.
         */
        INITIALIZING,
        /**
         * Manager is currently saving state and should act as read-only. Entered via any
         * serialization method. Resolves to {@link #ACTIVE} or {@link #ERROR}.
         * Should reject all external requests which modify state.
         */
        SAVING,
        /**
         * Manager is currently loading state and should act as write-only. Entered via any
         * deserialization method. Resolves to {@link #ACTIVE} or {@link #ERROR}.
         * Should reject all requests which query state. External modification requests are allowed
         * but may be undefined behavior.
         */
        LOADING,
        /**
         * Manager is holding regular state and is ready to receive messages.
         * Should respond to all normal read or write requests.
         */
        ACTIVE,
        /**
         * Manager is paused and should reject tick-driven mutations but allow reads.
         * May transition to {@link #ACTIVE} via {@link #unpause()}.
         * Should reject all mutation which depends on time progression, but respond to all other
         * normal requests.
         */
        PAUSED,
        /**
         * Manager is currently cleaning itself up and must reject all requests. Entered via
         * {@link #cleanup()} and resolves to {@link #INACTIVE} or {@link #ERROR}.
         * Should reject all external requests.
         */
        CLEANING_UP,
        /**
         * Manager has encountered a noncritical error and is running with reduced functionality.
         * May transition to {@link #ACTIVE} or to {@link #ERROR} via {@link #onDegraded(Exception)},
         * or to {@link #CLEANING_UP} via {@link #cleanup()}.
         * May choose not to respond to certain requests.
         */
        DEGRADED,
        /**
         * Manager has encountered a fatal, unrecoverable error and may only respond to limited
         * requests for saving and diagnostics.
         * May transition to {@link #CLEANING_UP} via {@link #cleanup()} or to {@link #INACTIVE}.
         * May respond only to select nonmutating requests.
         */
        ERROR;

        /**
         * Check whether this state is operational (able to respond to some normal requests).
         * @return {@code true} if the state is operational ({@link #ACTIVE}, {@link #PAUSED},
         * {@link #DEGRADED}), {@code false} otherwise.
         */
        public final boolean isOperational() {
            return this == ACTIVE || this == PAUSED || this == DEGRADED;
        }

        /**
         * Check whether this state is able to transition to a given next state.
         * @param next State being transitioned into.
         * @return {@code true} if this state may transition to the next state, {@code false} otherwise.
         */
        public final boolean canTransitionTo(@NotNull ManagerState next) {
            return switch (this) {
                case INACTIVE -> next == INITIALIZING || next == CLEANING_UP;
                case INITIALIZING, PAUSED, SAVING, LOADING -> next == ACTIVE || next == DEGRADED || next == ERROR;
                case ACTIVE -> next == SAVING || next == LOADING || next == PAUSED || next == CLEANING_UP || next == DEGRADED || next == ERROR;
                case DEGRADED -> next == ACTIVE || next == CLEANING_UP || next == ERROR || next == SAVING;
                case CLEANING_UP -> next == INACTIVE || next == ERROR;
                case ERROR -> next == CLEANING_UP || next == INACTIVE || next == SAVING;
            };
        }
    }

    // Instance Fields

    /** Owning engine for this manager. */
    protected final @NotNull Engine ENGINE;
    /** Manager which owns this manager. If null, this is the root manager (Engine). */
    public final @Nullable Manager superManager;
    /** Current state of this manager. */
    private @NotNull ManagerState state = ManagerState.INACTIVE;

    // Constructors

    /** Create a new root manager (Engine). */
    @SuppressWarnings("all")
    protected Manager() { this(null, null); } // Only called by the root manager component (Engine).
    /** Create a new Manager with the given Engine as the driving engine and direct super manager. */
    protected Manager(Engine engine) { this(engine, engine); }
    /** Create a new Manager with the given driving engine and super manager. */
    protected Manager(@NotNull Engine engine, @Nullable Manager superManager) {
        ENGINE = engine;
        this.superManager = superManager;
    }

    // Public lifecycle API

    /**
     * Initialize this Manager and all submanagers. Managers are responsible for actual
     * initialization logic within the {@link #doInit()} method.
     */
    public final void init() {
        transitionTo(ManagerState.INITIALIZING);
        try {
            doInit();
            getSubManagers().forEach(Manager::init);
            transitionTo(ManagerState.ACTIVE);
        }
        catch (Exception e) {
            onError(e);
        }
    }

    /**
     * Clean up this Manager and all submanagers. Managers are responsible for actual
     * cleanup logic within the {@link #doCleanup()} method.
     */
    public final void cleanup() {
        transitionTo(ManagerState.CLEANING_UP);
        try {
            doCleanup();
            getSubManagers().forEach(Manager::cleanup);
            transitionTo(ManagerState.INACTIVE);
            System.gc(); // Request the system to perform garbage collection
        }
        catch (Exception e) {
            transitionTo(ManagerState.ERROR);
            onError(e);
        }
    }

    /** Pause this manager. */
    public final void pause() {
        transitionTo(ManagerState.PAUSED);
    }

    /** Unpause this manager by moving to the {@link ManagerState#ACTIVE} state. */
    public final void unpause() {
        transitionTo(ManagerState.ACTIVE); // ERROR cannot transition to ACTIVE
    }

    /** Handle a noncritical exception which has caused this manager to become degraded. */
    protected void onDegraded(Exception e) {
        if (state != ManagerState.DEGRADED) transitionTo(ManagerState.DEGRADED);
        Logger.error(e);
    }
    /** Handle a critical exception which has caused this manager to crash. */
    protected void onError(Exception e) {
        if (state != ManagerState.ERROR) transitionTo(ManagerState.ERROR);
        Logger.error(e);
    }

    // Subclass Hooks

    /** Get all the direct submanagers of this Manager. */
    @Contract(pure = true)
    public abstract @NotNull Set<Manager> getSubManagers();

    /** Get all the descendent submanagers of this Manager, that is, direct submanagers and all of their descendents. */
    @Contract(pure = true)
    public final @NotNull Set<Manager> getAllSubManagers() {
        Set<Manager> subManagers = new HashSet<>(getSubManagers());
        getSubManagers().forEach(manager -> subManagers.addAll(manager.getAllSubManagers()));
        return subManagers;
    }

    /** Complete manager-subclass-specific initialization logic. Should not call {@link #init()} or {@code doInit()} on any Managers (including submanagers). */
    protected abstract void doInit();
    /** Complete manager-subclass-specific cleanup logic. Should not call {@link #cleanup()} or {@code doCleanup()} on any Managers (including submanagers). */
    protected abstract void doCleanup();

    /** Complete manager-subclass-specific toJson logic. Should not call {@link #toJson()} or {@code doToJson()} on any Managers (including submanagers). */
    protected abstract @NotNull JSONObject doToJson();
    /** Complete manager-subclass-specific fromJson logic. Should not call {@link #fromJson(JSONObject)} or {@code doFromJson(JSONObject)} on any Managers (including submanagers). */
    protected abstract void doFromJson(@NotNull JSONObject json); // Does not return, as we return self

    // State Access

    /** Get the internal state of the Manager. */
    public final @NotNull ManagerState getState() { return state; }

    public void printState() {
        Logger.log("%s is in state %s at %s", getClass().getSimpleName(), state, Engine.getInstance().getProgramTime());
    }

    /**
     * If valid, transition from the current ManagerState to the next state.
     * @param next State to transition into, if valid.
     * @see ManagerState#canTransitionTo(ManagerState)
     */
    protected final void transitionTo(ManagerState next) {
        if (!state.canTransitionTo(next)) {
            throw new IllegalStateException(
                getClass().getSimpleName() + ": illegal transition " + state + " -> " + next
            );
        }
        state = next;
        Logger.log("%s transitioned to %s at %s", getClass().getSimpleName(), next, Engine.getInstance().getProgramTime());
    }

    /**
     * Prevent continuing if this Manager is not in one of the specified allowed states.
     * @param allowed Set of states which are allowed to continue.
     * @throws IllegalStateException If the current state is not one of the allowed states.
     */
    protected final void requireState(@NotNull ManagerState... allowed) {
        for (ManagerState s : allowed) {
            if (s == state) return;
        }
        throw new IllegalStateException(
            getClass().getSimpleName() + " requires state " + Arrays.toString(allowed) + " but current state is " + state
        );
    }

    /**
     * Prevent continuing if this Manager is not in an operational state ({@link ManagerState#ACTIVE}, {@link ManagerState#PAUSED}, {@link ManagerState#DEGRADED}).
     * @throws IllegalStateException If the current state is not operational.
     */
    protected final void requireOperational() {
        if (!state.isOperational()) throw new IllegalStateException(
            getClass().getSimpleName() + " requires operational state but current state is " + state
        );
    }

    // Serialization

    @Override
    @SuppressWarnings("unchecked")
    public final @NotNull JSONObject toJson() {
        ManagerState prevState = getState();
        transitionTo(ManagerState.SAVING);
        JSONObject json = doToJson();
        if (!getSubManagers().isEmpty()) {
            List<JSONObject> subManagerJsons = getSubManagers().stream().map(Manager::toJson).toList();
            if (json.getValue() != null)
                json.setValue(Stream.concat(((List<JSONObject>) json.getAsList()).stream(), subManagerJsons.stream()).toList()); // Stream concat because these are immutable lists
            else json.setValue(subManagerJsons);
        }
        transitionTo(prevState);
        return json;
    }

    @Override
    public final @NotNull Manager fromJson(@NotNull JSONObject json) {
        ManagerState prevState = getState();
        transitionTo(ManagerState.LOADING);
        getSubManagers().forEach(manager -> manager.fromJson(json.get(manager.getClass().getSimpleName(), JSONObject.class)));
        doFromJson(json);
        transitionTo(prevState);
        return this;
    }
}
