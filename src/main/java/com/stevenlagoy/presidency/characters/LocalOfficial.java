/*
 * Local Official
 * ~/characters/LocalOfficial.java
 * Steven LaGoy
 * Created: 7 November 2024 at 11:11 PM
 * Modified: 29 December 2025
 */

package com.stevenlagoy.presidency.characters;

// IMPORTS ----------------------------------------------------------------------------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import core.JSONObject;

import com.stevenlagoy.presidency.characters.attributes.Role;
import com.stevenlagoy.presidency.characters.attributes.names.NameManager;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.map.MapEntity;
import com.stevenlagoy.presidency.map.MapManager;

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                         LOCAL OFFICIAL                                         //
////////////////////////////////////////////////////////////////////////////////////////////////////

/** LocalOfficial is a PoliticalActor character with a {@code Role} at the local level. */
public class LocalOfficial extends PoliticalActor {

    public static enum LocalRole implements Role {
        MAYOR,
        CITY_COUNCILOR,
        COUNTY_COMMISSIONER,
        MUNICIPAL_JUDGE;

        @Override
        public String getTitle() {
            return this.name(); // TODO Remember to localize in the caller
        }
    }

    private MapEntity jurisdiction;

    private List<LocalRole> roles;

    // CONSTRUCTORS -------------------------------------------------------------------------------------------------------------------------------------------

    public LocalOfficial(CharacterManager cm, DemographicsManager dm, MapManager mm, NameManager nm) {
        super(cm, dm, mm, nm);
        this.roles = new ArrayList<>();
    }

    public LocalOfficial(String buildstring) {
        super(buildstring);
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------------------------------------------------------------------

    // Jurisdiction : MapEntity

    public MapEntity getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(MapEntity jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    // Roles : List of StateRole

    public boolean addRole(LocalRole role) {
        return this.roles.add(role);
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();

        List<String> rolesStrings = new ArrayList<>();
        for (LocalRole role : roles) {
            rolesStrings.add(role.getTitle());
        }
        fields.add(new JSONObject("local_roles", rolesStrings));
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

    @Override
    public LocalOfficial fromJson(JSONObject json) {
        return this;
    }

    @Override
    public String toRepr() {
        return "";
    }

    @Override
    public LocalOfficial fromRepr(String repr) {
        return this;
    }
}
