/*
 * Character Model
 * ~/characters/attributes/CharacterModel.java
 * Steven LaGoy
 * Created: 06 January 2025 at 9:01 PM
 * Updated: 28 December 2025
 */

package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;

// IMPORTS ----------------------------------------------------------------------------------------------------------------------------------------------------

import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.presidency.util.Logger;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                        CHARACTER MODEL                                         //
////////////////////////////////////////////////////////////////////////////////////////////////////

/** CharacterModel is the appearance of a character. */
public class CharacterModel implements Repr<CharacterModel>, Jsonic<CharacterModel> {

    public static final int DEFAULT_AGE = 45;

    int visualAge;

    public CharacterModel() {
        this(DEFAULT_AGE);
    }

    public CharacterModel(CharacterModel other) {
        this.visualAge = other.visualAge;
    }

    public CharacterModel(int visualAge) {
        this.visualAge = visualAge;
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------------------------------------------------------------------

    public int getVisualAge() {
        return visualAge;
    }

    public void setVisualAge(int visualAge) {
        this.visualAge = visualAge;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public String toRepr() {
        String repr = String.format("%s:[visualAge:%d;];",
                this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
                this.visualAge);
        return repr;
    }

    @Override
    public CharacterModel fromRepr(String repr) {
        return this;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject("character_model", new JSONObject("visual_age", visualAge));
    }

    @Override
    public CharacterModel fromJson(JSONObject json) {
        if (json == null || !json.getKey().equals("character_model")) {
            Logger.log("Could not create a Character Model from an invalid or null JSONObject structure.");
            return null;
        }
        this.visualAge = ((JSONObject) json.getValue()).getAsNumber().intValue();
        return this;
    }

}