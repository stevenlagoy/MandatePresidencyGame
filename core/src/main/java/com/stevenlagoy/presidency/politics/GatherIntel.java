package com.stevenlagoy.presidency.politics;

import java.util.ArrayList;
import java.util.List;
import com.stevenlagoy.presidency.characters.CharacterJava;

public class GatherIntel extends Operation {
    public static List<Operation> instances = new ArrayList<>();

    private int aggressiveness;
    private int directness;
    private int secrecy;

    public GatherIntel(CharacterJava operator, CharacterJava[] agents) {
        super(operator, agents);
    }

    // GETTERS AND SETTERS

    // Aggressiveness : int
    public int getAggressiveness() {
        return aggressiveness;
    }

    public void setAggressiveness(int aggressiveness) {
        this.aggressiveness = aggressiveness;
    }

    // Directness : int
    public int getDirectness() {
        return directness;
    }

    public void setDirectness(int directness) {
        this.directness = directness;
    }

    // Secrecy : int
    public int getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(int secrecy) {
        this.secrecy = secrecy;
    }
}
