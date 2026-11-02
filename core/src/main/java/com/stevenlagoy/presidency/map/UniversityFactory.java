package com.stevenlagoy.presidency.map;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UniversityFactory extends EntityFactory<University, UniversityFactory.UniversityContext> {

    public static class UniversityContext extends EntityContext<University> {

        public UniversityContext(@NotNull Engine engine) {
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

    public UniversityFactory(@NotNull Engine engine, @NotNull UniversityManager manager) {
        super(engine, manager);
    }

    @Override
    protected @Nullable University doCreation(@NotNull UniversityFactory.UniversityContext context) {
        return null;
    }

    @Override
    protected @Nullable University doSelection(@NotNull UniversityFactory.UniversityContext context) throws UnsupportedOperationException {
        return null;
    }
}
