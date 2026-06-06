package com.stevenlagoy.presidency.politics.conventions;

import com.stevenlagoy.presidency.politics.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Primary(@NotNull List<Primary> instances, boolean isClosed, @NotNull Party associatedParty) implements Convention {

    public void convene(){
        // TODO
    }
}
