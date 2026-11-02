package com.stevenlagoy.presidency.map.travel.vehicle;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleManager extends EntityManager<Vehicle, Integer> {

    public VehicleManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        setFactory(new VehicleFactory(engine, this));
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected @NotNull Vehicle entityFromJson(JSONObject json) {
        return new Vehicle(engine, json);
    }

    @Override
    protected @Nullable Integer keyOf(@NotNull Vehicle entity) {
        return entity.hashCode();
    }

}
