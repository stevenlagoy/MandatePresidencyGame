package com.stevenlagoy.presidency.politics.conventions;
import java.util.ArrayList;
import java.util.List;

public record Caucus(
    boolean isClosed
) implements Convention {
    public static List<Primary> instances = new ArrayList<>();

    public void convene(){
        // TODO
    }
}
