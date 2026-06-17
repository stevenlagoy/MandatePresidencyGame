package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Demographics;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * <h1>APPEARANCE MANAGER</h1>
 * {@code ~/characters/attributes/AppearanceManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy            <br>
 *     <b>Created: </b> 01 June 2026 at 5:15 PM <br>
 *     <b>Modified:</b> 02 June 2026            <br>
 * </p>
 *
 * AppearanceManager is responsible for creating {@link CharacterAppearance} attributes.
 *
 * @author Steven LaGoy
 */
public class AppearanceManager extends Manager {

    // Constructor

    public AppearanceManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doInit() {
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

    // Creational Methods

    public @NotNull CharacterAppearance generateAppearance(@NotNull Demographics demographics, int age) {
        return new CharacterAppearance();
    }
}
