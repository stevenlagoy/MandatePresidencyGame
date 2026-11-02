package com.stevenlagoy.presidency.map.travel.vehicle;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleFactory extends EntityFactory<Vehicle, VehicleFactory.VehicleContext> {

    public static class VehicleContext extends EntityFactory.EntityContext<Vehicle> {

        public VehicleContext(@NotNull Engine engine) {
            super(engine);
        }

        @Override
        public @NotNull String getImputationFailureConditionsMessage() throws NoSuchFieldException {
            return "";
        }

        @Override
        public void desentinelize() {

        }

    }

    public VehicleFactory(@NotNull Engine engine, @NotNull VehicleManager superManager) {
        super(engine, superManager);
    }

    @Override
    protected @Nullable Vehicle doCreation(@NotNull VehicleFactory.VehicleContext context) {
        return null;
    }

    @Override
    protected @Nullable Vehicle doSelection(@NotNull VehicleFactory.VehicleContext context) throws UnsupportedOperationException {
        return null;
    }
}
