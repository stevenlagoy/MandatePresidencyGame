package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.map.entities.MapEntity;
import com.stevenlagoy.presidency.util.FilePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RoadwayManager extends EntityManager<Roadway, String> {

    public RoadwayManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {
        try {
            JSONObject json = new JSONObject(FilePath.ROADWAYS.path);
            for (Object roadwayObj : json.requireArray()) {
                if (roadwayObj instanceof JSONObject roadwayJson) {
                    register(new Roadway(engine, roadwayJson));
                }
            }
        }
        catch (IOException e) {
            onError(e);
        }
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    @Override
    protected @Nullable String keyOf(@NotNull Roadway entity) {
        return entity.getCode();
    }

    // Instance Methods

    public Set<Roadway> getConnections(MapEntity source, MapEntity destination) {
        Set<Roadway> connections = new HashSet<>();
        for (Roadway roadway : entities) {
            if (roadway.connects(source) && roadway.connects(destination)) {
                connections.add(roadway);
            }
        }
        return connections;
    }

}
