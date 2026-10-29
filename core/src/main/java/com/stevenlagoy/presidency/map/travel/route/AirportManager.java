package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class AirportManager extends EntityManager<Airport, String> {

    public AirportManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {
        try {
            JSONObject json = new JSONObject(FilePath.AIRPORTS.path);
            for (Object obj : json.requireArray()) {
                if (obj instanceof JSONObject airportJson) {
                    register(new Airport(engine, airportJson));
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
    protected @Nullable String keyOf(@NotNull Airport entity) {
        return entity.getName();
    }

    // Instance Methods

    public @NotNull Set<Airport> getAirports(@NotNull Airport.AirportSize size) {
        return entities.stream().filter(airport -> airport.getSize().equals(size)).collect(Collectors.toSet());
    }

}
