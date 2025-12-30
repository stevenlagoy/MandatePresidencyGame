/*
 * Has Personality (Interface)
 * ~/characters/attributes/HasPersonality.java
 * Steven LaGoy
 * Created: 06 January 2025 at 12:09 AM
 * Modified: 28 December 2025
 */

package com.stevenlagoy.presidency.characters.attributes;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                  HAS PERSONALITY (INTERFACE)                                   //
////////////////////////////////////////////////////////////////////////////////////////////////////

/** Classes which implement HasPersonality are Characters which are able to evaluate an action
 * against a persistent assigned personality, which may itself be based on experiences, alignments,
 * and other elements of that character's identity. */
public interface HasPersonality {

    public Personality getPersonality();

    public void setPersonality(Personality personality);

    /**
     * Evaluate the given action in alignment with this Character's personality.
     * 
     * @param action
     * @return A float between +1 and -1 which represents the liklihood of this
     *         Character to take the given action.
     */
    public float evaluateAction();

    public Personality determinePersonality();
}
