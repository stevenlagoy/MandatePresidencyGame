package com.stevenlagoy.presidency.map.travel.route;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.map.MapManager;
import com.stevenlagoy.presidency.map.Municipality;

public class Airport implements Jsonic<Airport> {

    // STATIC VARIABLES
    // ---------------------------------------------------------------------------

    public static enum AirportSize {
        LARGE,
        MEDIUM,
        SMALL;

        public static AirportSize fromString(String str) {
            str = str.toUpperCase().replaceAll("\\s+", "").trim();
            return switch (str) {
                case "LARGE" -> LARGE;
                case "L" -> LARGE;
                case "MEDIUM" -> MEDIUM;
                case "M" -> MEDIUM;
                case "SMALL" -> SMALL;
                case "S" -> SMALL;
                default -> null;
            };
        }
    }

    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    private String fullName;
    private String commonName;
    private String IATA;
    private Municipality location;
    private AirportSize size;
    private int enplanement;

    // CONSTRUCTORS
    // -------------------------------------------------------------------------------

    public Airport() {
        fullName = "";
        commonName = "";
        IATA = "";
        location = null;
        size = null;
        enplanement = 0;
    }

    public Airport(String fullName, String commonName, String IATA, Municipality location, AirportSize size,
                   int enplanement) {
        this.fullName = fullName;
        this.commonName = commonName;
        this.IATA = IATA;
        this.location = location;
        this.size = size;
        this.enplanement = enplanement;
    }

    public Airport(MapManager mm, String fullName, String commonName, String IATA, String locationName, String size,
                   int enplanement) {
    }

    public String getFullName() {
        return fullName;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getIATA() {
        return IATA;
    }

    public Municipality getMunicipality() {
        return location;
    }

    public AirportSize getSize() {
        return size;
    }

    public int getEnplanement() {
        return enplanement;
    }

    // REPRESENTATION METHODS
    // ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    @Override
    public Airport fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
}
