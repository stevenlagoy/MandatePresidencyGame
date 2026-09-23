package com.stevenlagoy.presidency.map.entities;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CongressionalDistrictManager extends EntityManager<CongressionalDistrict, String> {

    public CongressionalDistrictManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    @Override
    protected @Nullable String keyOf(@NotNull CongressionalDistrict entity) {
        return entity.getOfficeID();
    }
}
