/*
 * CongressionalDistrict.java
 * Steven LaGoy
 * Created: 29 October 2024 at 11:20 PM
 * Modified: 12 June 2025
 */

package com.stevenlagoy.presidency.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.JSONObject;
import com.stevenlagoy.presidency.data.Jsonic;
import com.stevenlagoy.presidency.app.Main;
import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.presidency.characters.FederalOfficial;
import com.stevenlagoy.presidency.characters.FederalOfficial.FederalRole;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.util.NumberOperations;

public class CongressionalDistrict implements MapEntity, Repr<State>, Jsonic<State> {

    // INSTANCE VARIBALES
    // -------------------------------------------------------------------------

    /**
     * Office ID of the District, including the 2-digit FIPS state portion. I.E.:
     * "3625" is New York's 25th District.
     */
    private String officeID;
    /** Population of the District. */
    private int population;
    /** Land Area of the District. */
    private double landArea;
    /** Name of the District, I.E.: "Congressional District 1". */
    private String name;
    /** State which the District is within. */
    private State state;
    /** Number of the District. At-large districts are 0. */
    private int districtNum;

    private FederalOfficial representative;

    private Set<String> descriptors;
    private Map<Bloc, Float> demographics;

    // CONSTRUCTORS
    // -------------------------------------------------------------------------------

    public CongressionalDistrict(String officeID, int population, double landArea, String name, String stateName,
            int districtNum, Set<String> descriptors) {
        this(officeID, population, landArea, name, Main.Engine().MapManager().matchState(stateName), districtNum,
                descriptors);
    }

    public CongressionalDistrict(String officeID, int population, double landArea, String name, State state,
            int districtNum, Set<String> descriptors) {
        this.officeID = officeID;
        this.population = population;
        this.landArea = landArea;
        this.name = name;
        this.state = state;
        this.districtNum = districtNum;
        setDescriptors(descriptors);
    }

    // GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    // Population : int
    @Override
    public int getPopulation() {
        return population;
    }

    @Override
    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }

    @Override
    public void addPopulation(int population) {
        this.population = Math.max(0, this.population + population);
    }

    // Land Area : double

    @Override
    public double getLandArea() {
        return landArea;
    }

    @Override
    public void setLandArea(double area) {
        this.landArea = Math.max(0.0, area);
    }

    // Name : string

    @Override
    public String getName() {
        if (this.districtNum == 0) {
            return this.state.getCommonName() + "'s At-Large Congressional District";
        }
        return this.state.getCommonName() + "'s " + NumberOperations.toOrdinal(districtNum) + " Congressional District";
    }

    public String getSimpleName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // State : State

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setState(String stateName) {
        setState(Main.Engine().MapManager().matchState(stateName));
    }

    // District Num : int

    public int getDistrictNum() {
        return districtNum;
    }

    public void setDistrictNum(int districtNum) {
        this.districtNum = districtNum;
    }

    // Office ID : String

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    // Descriptors : List of String
    @Override
    public Set<String> getDescriptors() {
        return descriptors;
    }

    @Override
    public void setDescriptors(Set<String> descriptors) {
        this.descriptors = new HashSet<>(descriptors);
        evaluateDemographics();
    }

    @Override
    public boolean hasDescriptor(String descriptor) {
        return this.descriptors.contains(descriptor);
    }

    @Override
    public boolean addDescriptor(String descriptor) {
        boolean modified = this.descriptors.add(descriptor);
        if (modified)
            evaluateDemographics();
        return modified;
    }

    @Override
    public boolean addAllDescriptors(Collection<String> descriptors) {
        boolean modified = this.descriptors.addAll(descriptors);
        if (modified)
            evaluateDemographics();
        return modified;
    }

    @Override
    public boolean removeDescriptor(String descriptor) {
        boolean modified = this.descriptors.remove(descriptor);
        if (modified)
            evaluateDemographics();
        return modified;
    }

    @Override
    public boolean removeAllDescriptors(Collection<String> descriptors) {
        boolean modified = this.descriptors.removeAll(descriptors);
        if (modified)
            evaluateDemographics();
        return modified;
    }

    // Demographics : Map of Bloc to Float

    @Override
    public Map<Bloc, Float> getDemographics() {
        return demographics;
    }

    @Override
    public float getDemographicPercentage(Bloc bloc) {
        return this.demographics.get(bloc) != null ? this.demographics.get(bloc) : 0.0f;
    }

    @Override
    public int getDemographicPopulation(Bloc bloc) {
        return Math.round(getDemographicPercentage(bloc) * population);
    }

    @Override
    public void evaluateDemographics() {
        this.descriptors.addAll(state.getDescriptors());
        this.demographics = Main.Engine().DemographicsManager().demographicsFromDescriptors(descriptors);
    }

    // Representative : FederalOfficial

    public FederalOfficial getRepresentative() {
        return representative;
    }

    public void setRepresentative(FederalOfficial representative) {
        if (representative == null) {
            this.representative = new FederalOfficial();
            this.representative.addRole(FederalRole.REPRESENTATIVE);
        } else
            this.representative = representative;
    }

    // REPRESENTATION METHODS
    // ---------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("office_id", officeID));
        fields.add(new JSONObject("population", population));
        fields.add(new JSONObject("land_area", landArea));
        fields.add(new JSONObject("name", name));
        fields.add(new JSONObject("state", state.getName()));
        fields.add(new JSONObject("district_num", districtNum));
        fields.add(new JSONObject("representative", representative.getName().getBiographicalName()));
        fields.add(new JSONObject("descriptors", List.copyOf(descriptors)));
        // Demographics are derived from Descriptors
        // List<JSONObject> demographicsJsons = new ArrayList<>();
        // for (Bloc bloc : demographics.keySet()) {
        // demographicsJsons.add(new JSONObject(bloc.getName(), demographics.get(bloc));
        // }
        // fields.add(new JSONObject("demographics", demographicsJsons));

        return new JSONObject(this.getName(), fields);
    }

    @Override
    public State fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public State fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
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
