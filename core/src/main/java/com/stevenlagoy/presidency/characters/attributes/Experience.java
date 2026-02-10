/*
 * Experience
 * ~/characters/attributes/Experience.java
 * Steven LaGoy
 * Created: 06 March 2025 at 12:41 AM
 * Updated: 28 December 2025
 */

package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.presidency.data.Repr;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                           EXPERIENCE                                           //
////////////////////////////////////////////////////////////////////////////////////////////////////

/** Experience is an attribute of a character showing a past moment which impacts their identity, associations, and choices. */
public class Experience implements Repr {

    // CONSTRUCTORS -------------------------------------------------------------------------------------------------------------------------------------------

    public Experience() {
        
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    public String toRepr() {
        String repr = String.format("%s:[];",
                this.getClass().getName().replace("class ", ""));
        return repr;
    }

    @Override
    public Repr fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

}