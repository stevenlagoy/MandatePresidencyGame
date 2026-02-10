package com.stevenlagoy.presidency.map.travel.route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.presidency.map.Municipality;

public class Railway extends Route implements Repr<Railway>, Jsonic<Railway> {

    private String name;
    private List<Municipality> connections;

    public Railway() {
        name = "";
        connections = new ArrayList<>();
    }

    public Railway(String name, Municipality... connections) {
        this(name, List.of(connections));
    }

    public Railway(String name, Collection<Municipality> connections) {
        this.name = name;
        this.connections = new ArrayList<>(connections);
    }

    public String getName() {
        return name;
    }

    @Override
    public List<Municipality> getConnections() {
        return connections;
    }

    public boolean connects(Municipality connection) {
        return connections.contains(connection);
    }

    // REPRESENTATION METHODS
    // ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Railway fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Railway fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public String toString() {
        return "";
    }

}
