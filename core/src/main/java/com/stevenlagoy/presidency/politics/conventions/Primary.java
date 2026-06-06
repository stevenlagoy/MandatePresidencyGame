package com.stevenlagoy.presidency.politics.conventions;
import java.util.ArrayList;
import java.util.List;

import com.stevenlagoy.presidency.politics.Party;
import org.jetbrains.annotations.NotNull;

public record Primary(@NotNull List<Primary> instances, boolean isClosed, @NotNull Party associatedParty) implements Convention {
    
    public void convene(){

    }
}
