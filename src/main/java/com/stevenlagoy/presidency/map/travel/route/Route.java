package com.stevenlagoy.presidency.map.travel.route;

import java.util.List;
import com.stevenlagoy.presidency.map.Municipality;

public abstract class Route {

    protected List<Municipality> connections;

    public List<Municipality> getConnections() {
        return connections;
    }

    public void setConnections(List<Municipality> connections) {
        this.connections = connections;
    }

    public boolean connects(Municipality connection) {
        return connections.contains(connection);
    }

}
