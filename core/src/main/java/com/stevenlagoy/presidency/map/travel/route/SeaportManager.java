package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class SeaportManager extends EntityManager<Seaport, String> {

    public SeaportManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {
        try {
            JSONObject json = new JSONObject(FilePath.SEAPORTS.path);
            for (Object obj : json.requireArray()) {
                if (obj instanceof JSONObject seaportJson) {
                    register(new Seaport(engine, seaportJson));
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
    protected @Nullable String keyOf(@NotNull Seaport entity) {
        return entity.getName();
    }

}
