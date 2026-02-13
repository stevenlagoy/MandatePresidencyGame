package com.stevenlagoy.presidency.politics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.stevenlagoy.presidency.characters.CharacterJava;

import java.util.HashSet;

public class Operation {
    public static List<Operation> instances = new ArrayList<>();

    private CharacterJava operator;
    private Set<CharacterJava> agents;

    public Operation(CharacterJava operator) {
        this.operator = operator;
        this.agents = new HashSet<CharacterJava>();
    }

    public Operation(CharacterJava operator, CharacterJava[] agents) {
        this.operator = operator;
        this.agents = new HashSet<CharacterJava>();
        for (CharacterJava c : agents) {
            this.agents.add(c);
        }
    }

    public CharacterJava getOperator() {
        return this.operator;
    }

    public void setOperator(CharacterJava c) {
        this.operator = c;
    }

    public Set<CharacterJava> getAgents() {
        return this.agents;
    }

    public void addAgent(CharacterJava c) {
        this.agents.add(c);
    }

    public void removeAgent(CharacterJava c) {
        this.agents.remove(c);
    }

    public void resetAgents() {
        this.agents.clear();
    }
}
