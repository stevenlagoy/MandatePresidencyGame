/*
 * State Official
 * ~/characters/StateOfficial.java
 * Steven LaGoy
 * Created: 11 October 2024 at 5:16 PM
 * Modified: 29 December 2025
 */

package com.stevenlagoy.presidency.characters;

// IMPORTS ----------------------------------------------------------------------------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.characters.attributes.Role;
import com.stevenlagoy.presidency.characters.attributes.names.NameManager;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.map.MapEntity;
import com.stevenlagoy.presidency.map.MapManager;
import com.stevenlagoy.presidency.map.State;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                         STATE OFFICIAL                                         //
////////////////////////////////////////////////////////////////////////////////////////////////////

/** StateOfficial is a {@code Character} with a role at the State level. */
public class StateOfficial extends PoliticalActorJava {

    public static enum StateRole implements Role {
        GOVERNOR,
        LIEUTENANT_GOVERNOR,
        STATE_SENATOR,
        STATE_SENATE_SPEAKER,
        STATE_REPRESENTATIVE,
        STATE_SECRETARY,
        ELECTOR;

        @Override
        public String getTitle() {
            return this.name();
        }
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------------------------------------------------------------------

    private Set<StateRole> roles;

    private MapEntity jurisdiction;

    // CONSTRUCTORS -------------------------------------------------------------------------------------------------------------------------------------------

    public StateOfficial(CharacterManager cm, DemographicsManager dm, MapManager mm, NameManager nm) {
        this(new PoliticalActorJava(cm, dm, mm, nm));
        this.jurisdiction = null;
    }

    public StateOfficial(StateOfficial other) {
        super(other);
        this.jurisdiction = other.jurisdiction;
    }

    public StateOfficial(CharacterJava character) {
        super(character);
    }

    public StateOfficial(PoliticalActorJava politicalActor) {
        super(politicalActor);
    }

    public StateOfficial(String buildstring) {
        super(buildstring);
        fromRepr(buildstring);
    }

    public StateOfficial(JSONObject json) {
        super(json);
        if (json == null) {
            throw new IllegalArgumentException("The passed JSONObject was null, and a " + getClass().getSimpleName()
                    + " object could not be created.");
        }
        fromJson(json);
    }

    public StateOfficial(CharacterManager cm, DemographicsManager dm, MapManager mm, NameManager nm, State state) {
        super(cm, dm, mm, nm);
        this.jurisdiction = state;
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------------------------------------------------------------------

    // Roles : List of StateRole

    public boolean addRole(StateRole role) {
        if (this.roles == null)
            this.roles = new HashSet<>();
        return this.roles.add(role);
    }

    // Jurisdiction : Map Entity

    public MapEntity getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(MapEntity jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();

        List<String> rolesStrings = new ArrayList<>();
        for (StateRole role : roles) {
            rolesStrings.add(role.getTitle());
        }
        fields.add(new JSONObject("state_roles", rolesStrings));
        if (jurisdiction != null)
            fields.add(new JSONObject("jurisdiction", jurisdiction.getName()));

        List<?> superFields = super.toJson().getAsList();
        for (Object obj : superFields) {
            if (obj instanceof JSONObject jsonObj) {
                fields.add(jsonObj);
            }
        }

        return new JSONObject(getName().getBiographicalName(), fields);
    }

    // OBJECT METHODS -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        StateOfficial other = (StateOfficial) obj;
        return this.toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int hash = super.hashCode();
        hash = prime * hash;
        return hash;
    }

    @Override
    public StateOfficial clone() {
        return new StateOfficial(this);
    }
}
