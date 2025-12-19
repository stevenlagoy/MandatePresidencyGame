/*
 * Player.java
 * Steven LaGoy
 * Created: 11 October 2024
 * Modified: 20 October 2025
 */

package com.stevenlagoy.presidency.characters;

import java.util.ArrayList;
import java.util.List;

import com.stevenlagoy.presidency.app.Main;
import com.stevenlagoy.presidency.characters.attributes.names.NameManager;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.map.MapManager;

import core.JSONObject;

public class Player extends Candidate {

    private static Player instance;

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player(Main.Engine().CharacterManager(), Main.Engine().DemographicsManager(),
                    Main.Engine().MapManager(), Main.Engine().NameManager());
        }
        return instance;
    }

    private Player(CharacterManager cm, DemographicsManager dm, MapManager mm, NameManager nm) {
        super(cm, dm, mm, nm);
    }

    public void setNameInput() {

    }

    public void setAgeInput() {

    }

    public void setPresentationInput() {

    }

    public void setOriginInput() {

    }

    public void setEducationInput() {

    }

    public void setAlignmentInput() {

    }

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();

        fields.add(new JSONObject("player", true));

        List<?> superFields = super.toJson().getAsList();
        for (Object obj : superFields) {
            if (obj instanceof JSONObject jsonObj) {
                fields.add(jsonObj);
            }
        }

        return new JSONObject(getName().getBiographicalName(), fields);
    }

    /**
     * As this is a Singleton class, {@link #equals(Object)} compares with the
     * object addresses of {@code other} and {@code this}.
     */
    @Override
    public boolean equals(Object other) {
        return other == this;
    }
}
