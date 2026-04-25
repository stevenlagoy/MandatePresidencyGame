/*
 * Player
 * ~/characters/Player.java
 * Steven LaGoy
 * Created: 11 October 2024 at 5:16 PM
 * Modified: 29 December 2025
 */

package com.stevenlagoy.presidency.characters;

// IMPORTS ----------------------------------------------------------------------------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import core.JSONObject;

import com.stevenlagoy.presidency.app.Main;
import com.stevenlagoy.presidency.characters.attributes.names.NameManager;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.map.MapManager;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                             PLAYER                                             //
////////////////////////////////////////////////////////////////////////////////////////////////////

/** Player is a special character controlled directly by the player. Singleton class accessed with {@link #getInstance()}. */
public class Player extends Candidate {

    /** The singleton Player instance */
    private static Player instance;

    /** Get the singleton player instance, or create it if it is null. */
    public static Player getInstance() {
        if (instance == null) {
            instance = new Player(Main.Engine().CharacterManager(), Main.Engine().DemographicsManager(),
                    Main.Engine().MapManager(), Main.Engine().NameManager());
        }
        return instance;
    }

    /** Private constructor can be called by {@link #getInstance()} when {@code instance} is null. */
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

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

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

    @Override
    public Player fromJson(JSONObject json) {
        return this;
    }

    @Override
    public String toRepr() {
        return "";
    }

    @Override
    public Player fromRepr(String repr) {
        return this;
    }

    // OBJECT METHODS -----------------------------------------------------------------------------------------------------------------------------------------

    /**
     * As this is a Singleton class, {@link #equals(Object)} compares with the
     * object addresses of {@code other} and {@code this}.
     */
    @Override
    public boolean equals(Object other) {
        return other == this;
    }
}
