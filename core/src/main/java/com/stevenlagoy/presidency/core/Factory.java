package com.stevenlagoy.presidency.core;

import org.jetbrains.annotations.NotNull;

public abstract class Factory<T, C> extends EngineBound {

    protected Factory(@NotNull Engine engine) {
        super(engine);
    }

    public abstract @NotNull T build(@NotNull C context);
}
