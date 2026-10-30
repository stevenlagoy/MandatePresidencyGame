package com.stevenlagoy.presidency.citizens.attributes.experiences;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

public class PersonalExperienceManager extends EntityManager<PersonalExperience, Integer> {

    public PersonalExperienceManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        setFactory(new PersonalExperienceFactory(engine, this));
    }

    @Override
    protected @NotNull Integer keyOf(@NotNull PersonalExperience entity) {
        return entity.hashCode();
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    @Override
    protected @NotNull PersonalExperience entityFromJson(JSONObject json) {
        return new PersonalExperience(engine, json);
    }

}
