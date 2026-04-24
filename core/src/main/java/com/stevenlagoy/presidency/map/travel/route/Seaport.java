package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.map.Municipality;

public class Seaport extends Route implements Jsonic<Seaport> {

    private String fullName;
    private String commonName;
    private Municipality location;

    public Seaport() {
        fullName = "";
        commonName = "";
        location = null;
    }

    public Seaport(String fullName, String commonName, Municipality location) {
        this.fullName = fullName;
        this.commonName = commonName;
        this.location = location;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String name) {
        this.commonName = name;
    }

    public Municipality getMunicipality() {
        return location;
    }

    public void setMunicipality(Municipality location) {
        this.location = location;
    }

    // REPRESENTATION METHODS
    // ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Seaport fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
}
