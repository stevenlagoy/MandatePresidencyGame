/*
 * Player.java
 * Steven LaGoy
 * Created: 11 October 2024
 * Modified: 20 October 2025
 */

package main.core.characters;

import java.util.ArrayList;
import java.util.List;

import core.JSONObject;

public class Player extends Candidate {
    
    private static Player instance;

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private Player() {
        super();
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
     * As this is a Singleton class, {@link #equals(Object)} compares with the object addresses of {@code other} and {@code this}.
     */
    @Override
    public boolean equals(Object other){
        return other == this;
    }
}
