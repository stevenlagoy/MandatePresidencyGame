package com.stevenlagoy.presidency.politics.elections;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectionFactory extends EntityFactory<Election, ElectionFactory.ElectionContext> {

    public static class ElectionContext extends EntityContext<Election> {

        public ElectionContext(@NotNull Engine engine) {
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

    public ElectionFactory(@NotNull Engine engine, @NotNull ElectionManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable Election doCreation(@NotNull ElectionFactory.ElectionContext context) {
        return null;
    }

    @Override
    protected @Nullable Election doSelection(@NotNull ElectionFactory.ElectionContext context) throws UnsupportedOperationException {
        return null;
    }

}
