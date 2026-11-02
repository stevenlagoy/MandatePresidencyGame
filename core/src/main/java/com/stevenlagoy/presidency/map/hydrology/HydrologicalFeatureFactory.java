package com.stevenlagoy.presidency.map.hydrology;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HydrologicalFeatureFactory extends EntityFactory<HydrologicalFeature, HydrologicalFeatureFactory.HydrologicalFeatureContext> {

    public static class HydrologicalFeatureContext extends EntityContext<HydrologicalFeature> {

        public HydrologicalFeatureContext(@NotNull Engine engine) {
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

    public HydrologicalFeatureFactory(@NotNull Engine engine, @NotNull HydrologicalFeatureManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable HydrologicalFeature doCreation(@NotNull HydrologicalFeatureFactory.HydrologicalFeatureContext context) {
        return null;
    }

    @Override
    protected @Nullable HydrologicalFeature doSelection(@NotNull HydrologicalFeatureFactory.HydrologicalFeatureContext context) throws UnsupportedOperationException {
        return null;
    }

}
