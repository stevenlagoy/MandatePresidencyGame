package com.stevenlagoy.presidency.politics.government;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GovernmentFactory extends EntityFactory<Government, GovernmentFactory.GovernmentContext> {

    public static class GovernmentContext extends EntityFactory.EntityContext<Government> {

        public GovernmentContext(@NotNull Engine engine) {
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

    public GovernmentFactory(@NotNull Engine engine, @NotNull GovernmentManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable Government doCreation(@NotNull GovernmentFactory.GovernmentContext context) {
        return null;
    }

    @Override
    protected @Nullable Government doSelection(@NotNull GovernmentFactory.GovernmentContext context) throws UnsupportedOperationException {
        return null;
    }
}
