package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.presidency.characters.Citizen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.HashSet;

public class Operation {
    public static List<Operation> instances = new ArrayList<>();

    private Citizen operator;
    private Set<Citizen> agents;

    public Operation(Citizen operator) {
        this.operator = operator;
        this.agents = new HashSet<Citizen>();
    }

    public Operation(Citizen operator, Citizen[] agents) {
        this.operator = operator;
        this.agents = new HashSet<Citizen>();
        for (Citizen c : agents) {
            this.agents.add(c);
        }
    }

    public Citizen getOperator() {
        return this.operator;
    }

    public void setOperator(Citizen c) {
        this.operator = c;
    }

    public Set<Citizen> getAgents() {
        return this.agents;
    }

    public void addAgent(Citizen c) {
        this.agents.add(c);
    }

    public void removeAgent(Citizen c) {
        this.agents.remove(c);
    }

    public void resetAgents() {
        this.agents.clear();
    }
}
