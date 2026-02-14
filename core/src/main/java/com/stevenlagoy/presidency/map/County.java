/*
 * County.java
 * Steven LaGoy
 * Created: 11 December 2024 at 5:18 PM
 * Modified: 11 June 2025
 */

package com.stevenlagoy.presidency.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.presidency.demographics.BlocJava;
import com.stevenlagoy.presidency.demographics.DemographicsManager;

public class County implements MapEntityJava, Repr<County>, Jsonic<County> {

    // INSTANCE VARIABLES

    /** State the County is within. */
    private StateJava state;
    /** Unique 5-digit FIPS code, including the State FIPS. */
    private String FIPS;
    /** Population of the County. */
    private int population;
    /** Land area of the County. */
    private double landArea;
    /** Full name of the County, including "County of ..." or "... Borough" &c. */
    private String fullName;
    /** Common name of the County, without prefix. */
    private String commonName;
    /**
     * Municipality serving as the County Seat. Does not have to be within the
     * County itself, but must be in the same State.
     */
    private Municipality countySeat;

    private Set<String> descriptors;
    private Map<BlocJava, Float> demographics;

    // CONSTRUCTORS
    // -------------------------------------------------------------------------------

    public County(DemographicsManager dm, MapManager mm, String FIPS, int population, double landArea, String fullName, String commonName, String stateName,
            String countySeatName, Set<String> descriptors) {
        this(dm, FIPS, population, landArea, fullName, commonName, mm.matchState(stateName),
                countySeatName.isEmpty() ? null : mm.matchMunicipality(countySeatName,stateName), descriptors);
    }

    public County(DemographicsManager dm, String FIPS, int population, double landArea, String fullName, String commonName, StateJava state,
            Municipality countySeat, Set<String> descriptors) {
        this.FIPS = FIPS;
        setPopulation(population);
        setLandArea(landArea);
        this.fullName = fullName;
        this.commonName = commonName;
        this.state = state;
        this.countySeat = countySeat;
        setDescriptors(dm,descriptors);
    }

    // GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    // Name : String

    @Override
    public String getName() {
        return getNameWithState();
    }

    // FIPS Code : String
    public String getFIPS() {
        return FIPS;
    }

    public void setFIPS(String FIPS) {
        this.FIPS = FIPS;
    }

    // Population : int
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
        if (this.population < 0)
            this.population = 0;
    }

    public void addPopulation(int population) {
        this.population += population;
        if (this.population < 0)
            this.population = 0;
    }

    // Land Area : double
    public double getLandArea() {
        return this.landArea;
    }

    public void setLandArea(double area) {
        this.landArea = area;
        if (this.landArea < 0)
            this.landArea = 0;
    }

    // Full Name : String
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getNameWithState() {
        return getFullName() + ", " + getState().getName();
    }

    // Common Name : String
    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String name) {
        this.commonName = name;
    }

    // State : State
    public StateJava getState() {
        return state;
    }

    public void setState(StateJava state) {
        this.state = state;
    }

    public void setState(MapManager mm, String stateName) {
        this.state = mm.matchState(stateName);
    }

    // County Seat : Municipality
    public Municipality getCountySeat() {
        return countySeat;
    }

    public void setCountySeat(Municipality countySeat) {
        this.countySeat = countySeat;
    }

    public void setCountySeat(MapManager mm, String countySeat) {
        this.countySeat = mm.matchMunicipality(countySeat,this.state);
    }

    // Descriptors : List of Strings
    @Override
    public Set<String> getDescriptors() {
        return descriptors;
    }

    @Override
    public void setDescriptors(DemographicsManager dm,Set<String> descriptors) {
        this.descriptors = new HashSet<>(descriptors);
        evaluateDemographics(dm);
    }

    @Override
    public boolean hasDescriptor(String descriptor) {
        return this.descriptors.contains(descriptor);
    }

    @Override
    public boolean addDescriptor(DemographicsManager dm,String descriptor) {
        boolean modified = this.descriptors.add(descriptor);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    @Override
    public boolean addAllDescriptors(DemographicsManager dm,Collection<String> descriptors) {
        boolean modified = this.descriptors.addAll(descriptors);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    @Override
    public boolean removeDescriptor(DemographicsManager dm,String descriptor) {
        boolean modified = this.descriptors.remove(descriptor);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    @Override
    public boolean removeAllDescriptors(DemographicsManager dm,Collection<String> descriptors) {
        boolean modified = this.descriptors.removeAll(descriptors);
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
        this.descriptors.addAll(state.getDescriptors());
        this.demographics = dm.demographicsFromDescriptors(descriptors);
    }

    // REPRESENTATION METHODS
    // ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("state", state.getName()));
        fields.add(new JSONObject("FIPS", FIPS));
        fields.add(new JSONObject("population", population));
        fields.add(new JSONObject("land_area", landArea));
        fields.add(new JSONObject("full_name", fullName));
        fields.add(new JSONObject("common_name", commonName));
        if (countySeat != null)
            fields.add(new JSONObject("county_seat", countySeat.getName()));
        fields.add(new JSONObject("descriptors", List.copyOf(descriptors)));
        // Demographics are derived from Descriptors
        // List<JSONObject> demographicsJsons = new ArrayList<>();
        // for (Bloc bloc : demographics.keySet()) {
        // demographicsJsons.add(new JSONObject(bloc.getName(),
        // demographics.get(bloc)));
        // }
        // fields.add(new JSONObject("demographics", demographicsJsons));

        return new JSONObject(this.getName(), fields);
    }

    @Override
    public County fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public County fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public String toString() {
        return this.toRepr();
    }

    // OBJECT METHODS
    // -----------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
}
