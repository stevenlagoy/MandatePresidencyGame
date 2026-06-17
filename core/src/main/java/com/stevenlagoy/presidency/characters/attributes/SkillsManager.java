package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * <h1>SKILLS MANAGER</h1>
 * {@code ~/characters/attributes/SkillsManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy            <br>
 *     <b>Created: </b> 01 June 2026 at 5:14 PM <br>
 *     <b>Modified:</b> 02 June 2026            <br>
 * </p>
 *
 * The SkillsManager allows for creation of {@link Skills} attributes
 * {@link com.stevenlagoy.presidency.characters.PoliticalActor}.
 *
 * @author Steven LaGoy
 */
public class SkillsManager extends Manager {

    // Constructor

    public SkillsManager(@NotNull Engine engine, @NotNull Manager superManager) {
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

    public @NotNull Skills generateSkills() {
        requireState(ManagerState.ACTIVE);
        return new Skills(
            RandomUtils.nextInt(0, 100),
            RandomUtils.nextInt(0, 100),
            RandomUtils.nextInt(0, 100)
        );
    }

    public @NotNull Skills getAverageSkills() {
        requireOperational();
        return new Skills();
    }
}
