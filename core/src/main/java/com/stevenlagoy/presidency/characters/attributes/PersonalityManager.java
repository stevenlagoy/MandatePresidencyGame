package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * <h1>PERSONALITY MANAGER</h1>
 * {@code ~/characters/PersonalityManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy            <br>
 *     <b>Created: </b> 01 June 2026 at 5:22 PM <br>
 *     <b>Modified:</b> 02 June 2026            <br>
 * </p>
 *
 * PersonalityManager is responsible for creating {@link Personality} attributes for
 * {@link com.stevenlagoy.presidency.characters.PoliticalActor} instances.
 *
 * @author Steven LaGoy
 */
public class PersonalityManager extends Manager {

    // Constructors

    public PersonalityManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doCleanup() {
    }

    // Serialization

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
    }

    // Creational Methods

    public Personality generatePersonality() {
        requireState(ManagerState.ACTIVE);
        return new Personality();
    }
}
