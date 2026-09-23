package com.stevenlagoy.presidency.map.entities;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class CensusRegionManager extends EntityManager<CensusRegion, String> {

    public CensusRegionManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {
        try {
            JSONObject regionsDivisionsJson = new JSONObject(FilePath.CENSUS_REGIONS_DIVISIONS.path);
            for (Object regionObj : regionsDivisionsJson.requireArray()) {
                if (regionObj instanceof JSONObject regionJson) {
                    CensusRegion region = new CensusRegion(engine, regionJson);
                    register(region);
                    region.getCensusDivisions().forEach(censusDivision -> engine.getManager(CensusDivisionManager.class).register(censusDivision));
                }
            }
        }
        catch (IOException e) {
            onError(e);
        }
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    @Override
    protected @Nullable String keyOf(@NotNull CensusRegion entity) {
        return entity.getName();
    }

}
