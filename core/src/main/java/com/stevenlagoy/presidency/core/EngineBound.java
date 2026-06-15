package com.stevenlagoy.presidency.core;

import org.jetbrains.annotations.NotNull;

public abstract class EngineBound {

    protected final @NotNull Engine engine;

    protected EngineBound(@NotNull Engine engine) {
        this.engine = engine;
    }
}
