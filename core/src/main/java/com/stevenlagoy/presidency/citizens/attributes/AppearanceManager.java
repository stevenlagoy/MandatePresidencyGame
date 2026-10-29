package com.stevenlagoy.presidency.citizens.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.demographics.Demographics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class AppearanceManager extends EntityManager<CharacterAppearance, Integer> {

    // Constructor

    public AppearanceManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {
    }

    @Override
    protected void doCleanup() {
    }

    @Override
    protected @Nullable Integer keyOf(@NotNull CharacterAppearance entity) {
        return entity.hashCode();
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
