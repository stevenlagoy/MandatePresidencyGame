package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class RailwayManager extends EntityManager<Railway, String> {

    public RailwayManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {
        try {
            JSONObject json = new JSONObject(FilePath.RAILWAYS.path);
            for (Object obj : json.requireArray()) {
                if (obj instanceof JSONObject railwayJson) {
                    register(new Railway(engine, railwayJson));
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
    protected @Nullable String keyOf(@NotNull Railway entity) {
        return entity.getName();
    }

}
