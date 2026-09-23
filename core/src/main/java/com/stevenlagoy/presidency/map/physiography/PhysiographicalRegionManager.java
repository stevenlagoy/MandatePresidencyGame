package com.stevenlagoy.presidency.map.physiography;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhysiographicalRegionManager extends EntityManager<PhysiographicalRegion, String> {

    public PhysiographicalRegionManager(@NotNull Engine engine, @NotNull Manager superManager) {
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
    protected @Nullable String keyOf(@NotNull PhysiographicalRegion entity) {
        return entity.getName();
    }

}
