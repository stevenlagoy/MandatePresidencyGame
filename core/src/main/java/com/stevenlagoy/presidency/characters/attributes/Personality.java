/*
 * Personality
 * ~/characters/attributes/Personality.java
 * Steven LaGoy
 * Created: 06 January 2025 at 12:09 AM
 * Modified: 28 December 2025
 */

package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;

// IMPORTS ----------------------------------------------------------------------------------------------------------------------------------------------------

import com.stevenlagoy.presidency.data.Repr;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                          PERSONALITY                                           //
////////////////////////////////////////////////////////////////////////////////////////////////////

/** Personality is an element of some characters who evaluate their actions and goals against their
 * persistent personality traits. Personalities are themselves based on experiences, alignments,
 * personal history, demographics, and some chance.
 */
public class Personality implements Repr<Personality>, Jsonic<Personality> {

    // CONSTRUCTORS -------------------------------------------------------------------------------------------------------------------------------------------

    public Personality() {
        
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Personality fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public String toRepr() {
        String repr = String.format("%s:[];",
                this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1]);
        return repr;
    }

    @Override
    public Personality fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject("personality");
    }


    // OBJECT METHODS -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Personality clone() {
        return new Personality();
    }

}