/*
 * Nation.java
 * Steven LaGoy
 * Created: 10 June 2025 at 10:44 PM
 * Modified: 10 June 2025
 */

package com.stevenlagoy.presidency.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.characters.CharacterManager;
import com.stevenlagoy.presidency.characters.FederalOfficial;
import com.stevenlagoy.presidency.demographics.BlocJava;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.util.Logger;

/**
 * Singleton class holding values for the Nation, or the United States.
 * <p>
 * Most important field is population, but other values like the name, capital,
 * and president can also be accessed through this class.
 * <p>
 * This is a Singleton class. It cannot be instantiated, and access to the
 * single
 * class instance is achieved through the {@link #getInstance()} method.
 */
public class NationJava implements MapEntityJava, Repr<NationJava>, Jsonic<NationJava> {

    // SINGLETON PATTERN
    // --------------------------------------------------------------------------

    /** Singleton instance of the Nation */
    private static NationJava instance;

    /**
     * This is a Singleton class. It cannot be instantiated, and access to the
     * single class instance is achieved through the {@link #getInstance()} method.
     */
    private NationJava() {
    } // Non-Instantiable

    /**
     * Get the singleton Nation instance.
     */
    public static NationJava getInstance() {
        if (instance == null) {
            instance = new NationJava();
            instance.descriptors = new HashSet<>();
            instance.demographics = new HashMap<>();
        }
        return instance;
    }

    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** Population of the Nation */
    private int population;
    /** Land area of the Nation */
    private double landArea;

    /** Full Name of the Nation; in English, "United States of America" */
    private final String fullName = "UNITED_STATES_OF_AMERICA";
    /** Common Name of the Nation; in English, "United States" */
    private final String commonName = "UNITED_STATES";
    /** Motto of the Nation; in English, "In God We Trust" */
    private final String motto = "US_MOTTO";
    /** Abbreviation of the Nation; in English, "U.S." */
    private final String abbreviation = "US";
    /** String representing the capital of the Nation, Washington, DC. */
    private final String capitalString = "Washington, DC";
    /** Capital Municipality of the Nation. */
    private Municipality capital;
    /** President of the Nation. */
    private FederalOfficial president;
    /** Vice President of the Nation. */
    private FederalOfficial vicePresident;
    /** Demographics of the whole Nation. */
    private Map<BlocJava, Float> demographics;
    /** Descriptors shared by every geographic division of the Nation. */
    private Set<String> descriptors;

    // GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    // Name : String

    @Override
    public String getName() {
        return getFullName();
    }

    // Population : int

    /** Get the total population of the Nation. */
    @Override
    public int getPopulation() {
        return population;
    }

    /**
     * Set the total population of the Nation.
     * <p>
     * Minimum population is zero. If the population is found to be lower, it will
     * be set to zero.
     *
     * @param population New population of the Nation.
     */
    @Override
    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }

    /**
     * Add to the total population of the Nation.
     * <p>
     * Minimum population is zero. If the population is found to be lower, it will
     * be set to zero.
     *
     * @param population Amount of population to be added.
     */
    @Override
    public void addPopulation(int population) {
        this.population = Math.max(0, this.population + population);
    }

    // Land Area : double

    /** Get the total land area of the Nation. */
    @Override
    public double getLandArea() {
        return landArea;
    }

    /**
     * Set the total land area of the Nation.
     * <p>
     * Minimum land area is zero. If the land area is found to be lower, it will be
     * set to zero.
     *
     * @param area New land area of the Nation.
     */
    @Override
    public void setLandArea(double area) {
        this.landArea = Math.max(0, area);
    }

    // Full Name : String

    /**
     * Get the localized full name of the Nation.
     * <p>
     * In English, "United States of America"
     *
     * @return String name.
     */
    public String getFullName() {
        return fullName; // TODO remember to localize in caller
    }

    // Commmon Name : String

    /**
     * Get the localized common name of the Nation.
     * <p>
     * In English, "United States"
     *
     * @return
     */
    public String getCommonName() {
        return commonName;
    }

    // Motto : String

    /**
     * Get the localized motto of the Nation.
     * <p>
     * In English, "In God We Trust"
     *
     * @return String motto.
     */
    public String getMotto() {
        return motto;
    }

    // Abbreviation : String

    /**
     * Get the localized abbreviation of the Nation.
     * <p>
     * In English, "U.S."
     *
     * @return String abbreviation.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    // Capital : Municipality

    public Municipality getCapital(MapManager mm) {
        if (capital == null) {
            capital = mm.matchMunicipality(capitalString);
            if (capital == null) {
                Logger.log("NATIONAL CAPITAL UNFOUND",
                        String.format("The national capital, %s, was unable to be found.", capitalString),
                        new Exception());
            }
        }
        return capital;
    }

    // President : FederalOfficial

    public FederalOfficial getPresident(CharacterManager cm) {
        if (president == null)
            president = cm.getPresident();
        return president;
    }

    // Vice President : FederalOfficial

    public FederalOfficial getVicePresident(CharacterManager cm) {
        if (vicePresident == null)
            vicePresident = cm.getVicePresident();
        return vicePresident;
    }

    // Descriptors : Set of Strings

    @Override
    public Set<String> getDescriptors() {
        return descriptors;
    }

    @Override
    public void setDescriptors(DemographicsManager dm, Set<String> descriptors) {
        this.descriptors = descriptors != null ? descriptors : new HashSet<>();
        evaluateDemographics(dm);
    }

    @Override
    public boolean hasDescriptor(String descriptor) {
        return this.descriptors.contains(descriptor);
    }

    @Override
    public boolean addDescriptor(DemographicsManager dm, String descriptor) {
        boolean modified = this.descriptors.add(descriptor);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    @Override
    public boolean addAllDescriptors(DemographicsManager dm, Collection<String> descriptors) {
        boolean modified = false;
        for (String descriptor : descriptors) {
            modified = addDescriptor(dm, descriptor) || modified;
        }
        return modified;
    }

    @Override
    public boolean removeDescriptor(DemographicsManager dm, String descriptor) {
        boolean modified = this.descriptors.add(descriptor);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    @Override
    public boolean removeAllDescriptors(DemographicsManager dm, Collection<String> descriptors) {
        boolean modified = descriptors.removeAll(descriptors);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    // Demographics : Map of Bloc to Float

    @Override
    public Map<BlocJava, Float> getDemographics() {
        return demographics;
    }

    @Override
    public float getDemographicPercentage(BlocJava bloc) {
        return this.demographics.get(bloc) != null ? this.demographics.get(bloc) : 0.0f;
    }

    @Override
    public int getDemographicPopulation(BlocJava bloc) {
        return Math.round(getDemographicPercentage(bloc) * population);
    }

    @Override
    public void evaluateDemographics(DemographicsManager dm) {
        this.demographics = dm.demographicsFromDescriptors(descriptors);
    }

    // REPRESENTATION METHODS
    // ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        return "";
    }

    @Override
    public NationJava fromRepr(String repr) {
        return this;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        // static finals do not need to be saved
        // fields.add(new JSONObject("capital",
        // getCapital().getNameWithCountyAndState()));
        // capital is a derived field from a static final
        fields.add(new JSONObject("president", president.getName().getBiographicalName()));
        fields.add(new JSONObject("vice_president", vicePresident.getName().getBiographicalName()));
        // demographics are a derived field from descriptors
        fields.add(new JSONObject("descriptors", List.copyOf(descriptors)));
        return new JSONObject("nation", fields);
    }

    @Override
    public NationJava fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    // OBJECT METHODS
    // -----------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

}
