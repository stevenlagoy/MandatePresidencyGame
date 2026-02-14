/*
 * Municipality.java
 * Steven LaGoy
 * Created: 05 June 2025 at 11:55 PM. Renamed from City.java
 * Modified: 12 June 2025
 */

package com.stevenlagoy.presidency.map;

// IMPORTS ----------------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.characters.LocalOfficial;
import com.stevenlagoy.presidency.characters.LocalOfficial.LocalRole;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.demographics.BlocJava;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.util.Logger;

/**
 * Municipalities are various types of inhabited places in the United States.
 * This is the broadest
 * term used to describe Cities, Villages, Towns, and other kinds of
 * Census-Designated populated areas.
 */
public class Municipality implements MapEntityJava, Repr<Municipality>, Jsonic<Municipality> {

    /**
     * TypeClass describes the various classes or levels of local government, which
     * vary by state.
     */
    public enum TypeClass {
        CITY("City"),
        TOWN("Town"),
        VILLAGE("Village"),
        FIRST_CLASS("First Class"),
        SECOND_CLASS("Second Class"),
        THIRD_CLASS("Third Class"),
        HOME_RULE("Home Rule");

        public final String title;

        TypeClass(String title) {
            this.title = title;
        }

        public static final TypeClass defaultTypeClass = TypeClass.CITY;

        public static TypeClass matchTypeClass(String typeClass) {
            String target = typeClass.toUpperCase().trim().replaceAll("\\s+", "_");
            for (TypeClass tc : TypeClass.values()) {
                if (tc.toString().equals(target))
                    return tc;
            }
            return null;
        }
    }

    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** Unique FIPS code, including the State (and possibly County) FIPS. */
    private String FIPS;
    /** Population of the Municipality. */
    private int population;
    /** Land area of the Municipality. */
    private double landArea;
    /** Name of the Municipality, potentially including "City of ..." */
    private String name;
    /** TypeClass of the Municipality, like City, Town, Village, &c. */
    private TypeClass typeClass;
    private TimeZone standardTimeZone;
    private TimeZone daylightTimeZone;
    private List<County> counties;
    private StateJava state;

    private LocalOfficial mayor;

    /** Geographic descriptors. County and State descriptors will be inherited. */
    private Set<String> descriptors;
    /** Percentages of the municipality which belong to each Bloc. */
    private Map<BlocJava, Float> demographics;

    // For contract / proxy calculations in Travel
    private Municipality contractLocation;
    private double connectivity;

    // CONSTRUCTORS
    // -------------------------------------------------------------------------------

    public Municipality(DemographicsManager dm, MapManager mm, String FIPS, int population, double landArea,
            String name, String typeClass,
            String standardTimeZone, String daylightTimeZone, String stateName, List<String> countiesNames,
            Set<String> descriptors) {
        this.FIPS = FIPS;
        setPopulation(population);
        setLandArea(landArea);
        this.name = name;
        this.typeClass = TypeClass.matchTypeClass(typeClass);
        this.standardTimeZone = TimeZone.getTimeZone(standardTimeZone);
        this.daylightTimeZone = TimeZone.getTimeZone(daylightTimeZone);
        this.counties = new ArrayList<>();
        setState(mm.matchState(stateName));
        setCountiesByNames(mm, countiesNames);
        setDescriptors(dm, descriptors);
    }

    public Municipality(DemographicsManager dm, MapManager mm, String FIPS, int population, double landArea,
            String name, TypeClass typeClass,
            TimeZone standardTimeZone, TimeZone daylightTimeZone, List<County> counties, Set<String> descriptors) {
        this.FIPS = FIPS;
        this.population = population;
        this.landArea = landArea;
        this.name = name;
        this.typeClass = typeClass;
        this.standardTimeZone = standardTimeZone;
        this.daylightTimeZone = daylightTimeZone;
        this.counties = counties != null ? counties : new ArrayList<>();
        try {
            this.state = this.counties.get(0).getState();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            Logger.log("NO COUNTIES OR STATE",
                    String.format("A Municipality was created without counties or state. Name: %s", name), e);
        }
        setDescriptors(dm, descriptors);
        evaluateDemographics(dm);

        mm.addMunicipality(this);
    }

    public Municipality(DemographicsManager dm, MapManager mm, String FIPS, int population, double landArea,
                        String name, String nickname,
                        TypeClass typeClass,
                        TimeZone standardTimeZone, TimeZone daylightTimeZone, StateJava state, Set<String> descriptors) {
        this.FIPS = FIPS;
        this.population = population;
        this.landArea = landArea;
        this.name = name;
        this.typeClass = typeClass;
        this.standardTimeZone = standardTimeZone;
        this.daylightTimeZone = daylightTimeZone;
        this.state = state;
        setDescriptors(dm, getDescriptors());
        evaluateDemographics(dm);

        mm.addMunicipality(this);
    }

    // GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    // FIPS : String

    public String getFIPS() {
        return FIPS;
    }

    public void setFIPS(String FIPS) {
        this.FIPS = FIPS;
    }

    // Population : int

    public int getPopulation() {
        return this.population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void addPopulation(int population) {
        this.population += population;
    }

    // Land Area : double

    public double getLandArea() {
        return landArea;
    }

    public void setLandArea(double area) {
        this.landArea = Math.max(0, area);
    }

    // Name : String

    /**
     * Return identifying name of this Municipality.
     * <p>
     * To get the simple name of this Municipality, use {@link #getCommonName()}
     *
     * @see #getNameWithCountyAndState()
     */
    @Override
    public String getName() {
        return getNameWithCountyAndState();
    }

    public String getCommonName() {
        return name;
    }

    public String getNameWithState() {
        return name + ", " + state.getCommonName();
    }

    public String getNameWithCountyAndState() {
        return name + ", " + counties.get(0).getFullName() + ", " + state.getCommonName();
    }

    public void setName(String name) {
        this.name = name;
    }

    // Type Class : TypeClass

    public TypeClass getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(TypeClass typeClass) {
        this.typeClass = typeClass;
    }

    // Standard Time Zone : TimeZone

    public TimeZone getStandardTimeZone() {
        return standardTimeZone;
    }

    public void setStandardTimeZone(TimeZone timeZone) {
        this.standardTimeZone = timeZone;
    }

    // Daylight Time Zone : TimeZone

    public TimeZone getDaylightTimeZone() {
        return daylightTimeZone;
    }

    public void setDaylightTimeZone(TimeZone timeZone) {
        this.daylightTimeZone = timeZone;
    }

    // Counties : List of County

    public List<County> getCounties() {
        return counties;
    }

    public void setCounties(List<County> counties) {
        this.counties = counties;
    }

    public void SetCounties(County county) {
        this.counties = List.of(county);
    }

    public void setCountiesByNames(MapManager mm, List<String> countiesNames) {
        this.counties = mm.matchCounties(countiesNames, this.state.getAbbreviation());
    }

    public boolean addCounty(County county) {
        return this.counties.add(county);
    }

    public boolean addAllCounties(Collection<County> counties) {
        return this.counties.addAll(counties);
    }

    public boolean removeCounty(County county) {
        return this.counties.remove(county);
    }

    public boolean removeAllCounties(Collection<County> counties) {
        return this.counties.removeAll(counties);
    }

    // State : State

    public StateJava getState() {
        return this.state;
    }

    public void setState(StateJava state) {
        this.state = state;
    }

    // Descriptors : List of String

    @Override
    public Set<String> getDescriptors() {
        return descriptors;
    }

    @Override
    public boolean hasDescriptor(String descriptor) {
        return this.descriptors.contains(descriptor);
    }

    @Override
    public void setDescriptors(DemographicsManager dm, Set<String> descriptors) {
        this.descriptors = descriptors != null ? descriptors : new HashSet<>();
        evaluateDemographics(dm);
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
        boolean modified = this.descriptors.addAll(descriptors);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    @Override
    public boolean removeDescriptor(DemographicsManager dm, String descriptor) {
        boolean modified = this.descriptors.remove(descriptor);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    @Override
    public boolean removeAllDescriptors(DemographicsManager dm, Collection<String> descriptors) {
        boolean modified = this.descriptors.removeAll(descriptors);
        if (modified)
            evaluateDemographics(dm);
        return modified;
    }

    // Demographics : Map of Bloc to Float

    @Override
    public Map<BlocJava, Float> getDemographics() {
        return this.demographics;
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

    // Mayor : LocalOfficial

    public LocalOfficial getMayor() {
        return mayor;
    }

    public void setMayor(Engine engine, LocalOfficial mayor) {
        if (mayor == null) {
            this.mayor = new LocalOfficial(engine.CharacterManager(), engine.DemographicsManager(),
                    engine.MapManager(), engine.NameManager());
            this.mayor.addRole(LocalRole.MAYOR);
            this.mayor.setJurisdiction(this);
        }
        else this.mayor = mayor;
    }

    // Contract Location : Municipality
    public Municipality getContractLocation() {
        return contractLocation;
    }

    // Connectivity : double
    public double getConnectivity() {
        return connectivity;
    }

    // REPRESENATION METHODS
    // ----------------------------------------------------------------------

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("FIPS", FIPS));
        fields.add(new JSONObject("population", population));
        fields.add(new JSONObject("land_area", landArea));
        fields.add(new JSONObject("name", name));
        if (this.typeClass != null)
            fields.add(new JSONObject("type_class", typeClass.toString()));
        fields.add(new JSONObject("standard_time_zone", standardTimeZone.getDisplayName()));
        fields.add(new JSONObject("daylight_time_zone", daylightTimeZone.getDisplayName()));
        List<String> countiesNames = new ArrayList<>();
        for (County county : counties) {
            countiesNames.add(county.getName());
        }
        fields.add(new JSONObject("counties", countiesNames));
        fields.add(new JSONObject("state", state.getName()));
        if (mayor != null)
            fields.add(new JSONObject("mayor", mayor.getName().getBiographicalName()));
        fields.add(new JSONObject("descriptors", List.copyOf(descriptors)));
        // Demographics are derived from descriptors
        if (contractLocation != null)
            fields.add(new JSONObject("contract_location", contractLocation.getName()));
        fields.add(new JSONObject("connectivity", connectivity));

        return new JSONObject(this.getName(), fields);
    }

    @Override
    public Municipality fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Municipality fromRepr(String repr) {
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
    public boolean equals(Object other) {
        return this.toString().equals(other.toString());
        // TODO
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
}
