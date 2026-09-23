package com.stevenlagoy.presidency.map.hydrology;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HydrologicalFeatureManager extends EntityManager<HydrologicalFeature, String> {

    public HydrologicalFeatureManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {

    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    @Override
    protected @Nullable String keyOf(@NotNull HydrologicalFeature entity) {
        return entity.getName();
    }

}
