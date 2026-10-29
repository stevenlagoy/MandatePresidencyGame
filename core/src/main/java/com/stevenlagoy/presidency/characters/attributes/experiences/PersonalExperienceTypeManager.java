package com.stevenlagoy.presidency.characters.attributes.experiences;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePath;import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;import java.io.IOException;

class PersonalExperienceTypeManager extends EntityManager<PersonalExperienceType, String> {

    public PersonalExperienceTypeManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    @Override
    protected void doInit() {
        try {
            fromJson(new JSONObject(FilePath.EXPERIENCE_TYPES.path));
        }
        catch (IOException e) {
            onError(e);
        }
    }

    @Override
    protected @NotNull PersonalExperienceType entityFromJson(JSONObject json) {
        return new PersonalExperienceType(engine, json);
    }

    @Override
    protected @Nullable String keyOf(@NotNull PersonalExperienceType entity) {
        return entity.getName();
    }
}
