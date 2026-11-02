package com.stevenlagoy.presidency.demographics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlocManager extends EntityManager<Bloc, String> {

    public BlocManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        setFactory(new BlocFactory(engine, this));
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected @NotNull Bloc entityFromJson(JSONObject json) {
        return new Bloc(engine, json);
    }

    @Override
    protected @Nullable String keyOf(@NotNull Bloc entity) {
        return entity.getName();
    }


}
