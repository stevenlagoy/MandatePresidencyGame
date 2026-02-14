/*
 * State.java
 * Steven LaGoy
 * Created: 28 August 2024 at 11:25 PM
 * Modified: 11 June 2025
 */

package com.stevenlagoy.presidency.map;

// IMPORTS ----------------------------------------------------------------------------------------

// Standard Library Imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Internal Imports

import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.characters.FederalOfficial;
import com.stevenlagoy.presidency.characters.PoliticalActorJava;
import com.stevenlagoy.presidency.characters.StateOfficial;
import com.stevenlagoy.presidency.characters.FederalOfficial.FederalRole;
import com.stevenlagoy.presidency.characters.StateOfficial.StateRole;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.demographics.BlocJava;
import com.stevenlagoy.presidency.demographics.DemographicsManager;
import com.stevenlagoy.presidency.politics.ElectionResult;
import com.stevenlagoy.presidency.politics.Legislature;
import com.stevenlagoy.presidency.politics.Party;
import com.stevenlagoy.presidency.util.Logger;

/**
 * Map entity for the second-largest geographical division, including States,
 * Commonwealths, District, and Territories.
 * <p>
 * Holds values describing information about the state, its demographics and
 * voting patterns, and leadership.
 */
public class StateJava implements MapEntityJava, Repr<StateJava>, Jsonic<StateJava> {

    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    private NationJava nation;
    /** Unique state FIPS code between 01 and 56. */
    private String FIPS;
    /** Total population of the State. */
    private int population;
    /** Land area of the State. */
    private double landArea;
    /** Full name of the State, IE: State of Alabama, Commonwealth of Virginia. */
    private String fullName;
    /** Common name of the State, IE: Louisiana, Utah. */
    private String commonName;
    /** Two-letter postal abbreviation of the State, IE: NM, CO. */
    private String abbreviation;
    /** Nickname of the state. */
    private String nickname;
    /** Motto of the state. */
    private String motto;
    /** Capital municipality of the state. */
    private Municipality capital;
    private Set<String> descriptors;
    private Map<BlocJava, Float> demographics;
    private List<FederalOfficial> senators;
    private List<FederalOfficial> representatives;
    private List<Legislature> stateHouses;
    private String stateLegislatureName;
    private StateOfficial governor;
    private StateOfficial lieutenantGovernor;

    private List<ElectionResult> pastResults;

    // CONSTRUCTORS
    // -------------------------------------------------------------------------------

    /** To be used before Characters are ready to be generated. */
    public StateJava(DemographicsManager dm, MapManager mm, String FIPS, int population, double landArea, String fullName,
                     String commonName,
                     String abbreviation,
                     String nickname, String motto, String capitalName, Set<String> descriptors) {
        nation = NationJava.getInstance();
        setFIPS(FIPS);
        setPopulation(population);
        setLandArea(landArea);
        this.fullName = fullName;
        this.commonName = commonName;
        this.abbreviation = abbreviation;
        this.nickname = nickname;
        this.motto = motto;
        this.capital = capitalName.isEmpty() ? null
                : mm.matchMunicipality(capitalName, abbreviation);
        this.senators = new ArrayList<>();
        this.representatives = new ArrayList<>();
        this.stateHouses = new ArrayList<>();
        setDescriptors(dm, descriptors);
    }

    public StateJava(Engine engine, String FIPS, int population, double landArea, String fullName,
                     String commonName,
                     String abbreviation,
                     String nickname, String motto, String capitalName, Set<String> descriptors, List<FederalOfficial> senators,
                     StateOfficial governor, StateOfficial lieutenantGovernor) {
        this(engine, FIPS, population, landArea, fullName, commonName, abbreviation, nickname, motto,
                capitalName.isEmpty() ? null : engine.MapManager().matchMunicipality(capitalName, abbreviation),
                descriptors, senators, governor, lieutenantGovernor);
    }

    public StateJava(Engine engine, String FIPS, int population, double landArea,
                     String fullName,
                     String commonName,
                     String abbreviation,
                     String nickname, String motto, Municipality capital, Set<String> descriptors,
                     List<FederalOfficial> senators, StateOfficial governor, StateOfficial lieutenantGovernor) {
        nation = NationJava.getInstance();
        setFIPS(FIPS);
        setPopulation(population);
        setLandArea(landArea);
        this.fullName = fullName;
        this.commonName = commonName;
        this.abbreviation = abbreviation;
        this.nickname = nickname;
        this.motto = motto;
        this.capital = capital;
        setDescriptors(engine.DemographicsManager(), descriptors);
        setSenators(engine, senators);
        this.governor = governor != null ? governor
                : new StateOfficial(engine.CharacterManager(), engine.DemographicsManager(),
                        engine.MapManager(), engine.NameManager(), this);
        this.lieutenantGovernor = lieutenantGovernor != null ? lieutenantGovernor
                : new StateOfficial(engine.CharacterManager(), engine.DemographicsManager(),
                        engine.MapManager(), engine.NameManager(), this);
    }

    // GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    // Name : String

    @Override
    public String getName() {
        return getCommonName();
    }

    // FIPS Code : String
    public String getFIPS() {
        return FIPS;
    }

    public void setFIPS(String FIPS) {
        for (char c : FIPS.toCharArray()) {
            if (!Character.isDigit(c)) {
                Logger.log("INVALID FIPS",
                        String.format("Attempted to assign FIPS code %s, which is not numeric.", FIPS),
                        new Exception());
                return;
            }
        }
        this.FIPS = FIPS;
    }

    // Population : int
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }

    public void addPopulation(int population) {
        this.population = Math.max(0, this.population + population);
    }

    // Land Area : double
    public double getLandArea() {
        return landArea;
    }

    public void setLandArea(double area) {
        this.landArea = Math.max(0, area);
    }

    // Full Name : String
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    // Common Name : String
    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String name) {
        this.commonName = name;
    }

    // Abbreviation : String
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    // Nickname : String
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // Motto : String
    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    // Capital : Municipality
    public Municipality getCapital() {
        return capital;
    }

    public void setCapital(Municipality capital) {
        this.capital = capital;
    }

    // Descriptors : List of String
    @Override
    public Set<String> getDescriptors() {
        return descriptors;
    }

    @Override
    public void setDescriptors(DemographicsManager dm, Set<String> descriptors) {
        this.descriptors = new HashSet<>(descriptors);
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
        this.descriptors.addAll(nation.getDescriptors());
        this.demographics = dm.demographicsFromDescriptors(descriptors);
    }

    // Senators : List of Federal Official
    public List<FederalOfficial> getSenators() {
        return senators;
    }

    /**
     * Sets the senators list to the first two FederalOfficials in the passed List,
     * and generates any necessary.
     * Pass with an empty list or {@code null} to have both senators generated.
     */
    public void setSenators(Engine engine, List<FederalOfficial> senators) {
        this.senators = new ArrayList<>();
        if (senators == null || senators.isEmpty()) {
            addSenator(new FederalOfficial(engine.CharacterManager(), engine.DemographicsManager(),
                    engine.MapManager(), engine.NameManager()));
            addSenator(new FederalOfficial(engine.CharacterManager(), engine.DemographicsManager(),
                    engine.MapManager(), engine.NameManager()));
        }
        else if (senators.size() == 1) {
            addSenator(senators.get(0));
            addSenator(new FederalOfficial(engine.CharacterManager(), engine.DemographicsManager(),
                    engine.MapManager(), engine.NameManager()));
        }
        else {
            addSenator(senators.get(0));
            addSenator(senators.get(1));
        }
    }

    public boolean hasSenator(PoliticalActorJava senator) {
        return senators.contains(senator);
    }

    public boolean addSenator(FederalOfficial senator) {
        senator.addRole(FederalRole.SENATOR);
        senator.setJurisdiction(this);
        return this.senators.add(senator);
    }

    public boolean removeSenator(FederalOfficial senator) {
        senator.removeRole(FederalRole.SENATOR);
        return senators.remove(senator);
    }

    // Representatives : List of Federal Official
    public List<FederalOfficial> getRepresentatives() {
        return representatives;
    }

    public int getNumberRepresentatives() {
        return representatives.size();
    }

    public FederalOfficial setRepresentative(int index, FederalOfficial representative) {
        representative.addRole(FederalRole.REPRESENTATIVE);
        return representatives.set(index, representative);
    }

    public List<Legislature> getStateHouses() {
        return stateHouses;
    }

    public Legislature getStateUpperHouse() {
        for (Legislature house : stateHouses) {
            if (house.isUpperHouse())
                return house;
        }
        return null;
    }

    public Legislature getStateLowerHouse() {
        for (Legislature house : stateHouses) {
            if (house.isLowerHouse())
                return house;
        }
        return null;
    }

    public String getStateLegislatureName() {
        return stateLegislatureName;
    }

    // Governor State Official
    public StateOfficial getGovernor() {
        return governor;
    }

    public void setGovernor(Engine engine, StateOfficial governor) {
        if (governor == null) {
            this.governor = new StateOfficial(engine.CharacterManager(), engine.DemographicsManager(),
                    engine.MapManager(), engine.NameManager(), this);
            this.governor.addRole(StateRole.GOVERNOR);
            this.governor.setJurisdiction(this);
        }
        else this.governor = governor;
    }

    // Lieutenant Governor State Official
    public StateOfficial getLieutenantGovernor() {
        return lieutenantGovernor;
    }

    public void setLieutenantGovernor(Engine engine, StateOfficial lieutenantGovernor) {
        if (lieutenantGovernor == null) {
            this.lieutenantGovernor = new StateOfficial(engine.CharacterManager(),
                    engine.DemographicsManager(),
                    engine.MapManager(), engine.NameManager(), this);
            this.lieutenantGovernor.addRole(StateRole.LIEUTENANT_GOVERNOR);
            this.lieutenantGovernor.setJurisdiction(this);
        }
        else this.lieutenantGovernor = lieutenantGovernor;
    }

    // Past Results : List of Election Result
    public List<ElectionResult> getPastResults() {
        return pastResults;
    }

    public void addPastResult(ElectionResult result) {
        pastResults.add(result);
    }

    public List<ElectionResult> getResultsInYear(int year) {
        List<ElectionResult> response = new ArrayList<>();
        for (ElectionResult result : pastResults) {
            if (result.getElectionDate().getYear() == year) {
                response.add(result);
            }
        }
        return response;
    }

    public int getTotalVotesInYear(int year) {
        int response = 0;
        for (ElectionResult result : pastResults) {
            if (result.getElectionDate().getYear() == year) {
                response += result.getCandidateVotes().values().stream().mapToInt(Integer::intValue).sum();
            }
        }
        return response;
    }

    /**
     * Get the sum of all votes for a party sorted by year. Keys are years, values
     * are total votes for all candidates from the party cast that year.
     */
    public Map<Integer, Integer> getTotalResultsForPartyByYear(Party party) {
        Map<Integer, Integer> response = new HashMap<>();
        for (ElectionResult result : pastResults) {
            int year = result.getElectionDate().getYear();
            for (String candidateName : result.getCandidateParties().keySet()) {
                if (result.getCandidateParties().get(candidateName).equals(party)) {
                    response.put(year, response.getOrDefault(year, 0) + result.getVotesForCandidate(candidateName));
                }
            }
        }
        return response;
    }

    /**
     * Returns a double value representing the level of control of a party, based on
     * control of the
     * state legislature, governor, and previous electoral results. Higher values
     * mean more control.
     */
    public double getPartyControl(Party party, Engine engine) {
        double control = 0.0;

        /*
         * State:
         * Legislature
         * Same party seats
         * Same party majority
         * Governor
         * Lieutenant Governor
         * Trifecta (Legislature and Governor)
         * Federal:
         * Senators
         * Same party
         * Bonus for both same party
         * Representatives
         * Same party
         * Same party majority
         * Electoral History:
         * Number of same party victories
         * Margin of same party victories
         * Bonus for more recent elections
         */

        boolean trifecta = true;
        // Control from legislature
        for (Legislature house : stateHouses) {
            // Percentage of the houses controlled by party
            control += house.getPartySeats(party) / house.getTotalSeats() * 0.75;
            // Party control
            if (house.isPartyControlled(party))
                control += 0.25;
            else
                trifecta = false;
        }
        // Control from governorship
        if (governor.getPartyAlignment().equals(party))
            control += 0.65;
        else
            trifecta = false;
        // Add bonus control if trifecta
        if (trifecta)
            control += 1.0;
        // Control from lieutenant governorship
        if (lieutenantGovernor != null && lieutenantGovernor.getPartyAlignment().equals(party))
            control += 0.1;

        // Control from senators
        if (senators.get(0).getPartyAlignment().equals(party))
            control += 0.25;
        if (senators.get(1).getPartyAlignment().equals(party))
            control += 0.25;
        if (senators.get(0).getPartyAlignment().equals(party) && senators.get(1).getPartyAlignment().equals(party))
            control += 0.25;
        // Control from representatives
        int samePartyReps = 0;
        for (FederalOfficial representative : representatives) {
            if (representative.getPartyAlignment().equals(party))
                samePartyReps++;
        }
        control += samePartyReps / getNumberRepresentatives() * 0.60;
        control += samePartyReps > getNumberRepresentatives() / 2 ? 0.15 : 0.0;

        // Control from electoral history
        for (ElectionResult result : pastResults) {
            int year = result.getElectionDate().getYear();
            int currentYear = engine.TimeManager().getCurrentYear();
            if (year >= currentYear - 20) {
                control += getTotalResultsForPartyByYear(party).get(year) / getTotalVotesInYear(year)
                        * (1 / (currentYear - year)) * 0.125; // Weight recent elections higher
                control += getTotalResultsForPartyByYear(party).get(year) > getTotalVotesInYear(year) / 2 ? 0.1 : 0.0;
            }
        }

        return control;
    }

    // REPRESENTATION METHODS
    // ---------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StateJava fromRepr(String repr) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public String toRepr() {
        String[] demographicsStrings = new String[demographics.size()];
        for (int i = 0; i < demographics.size(); i++) {
            demographicsStrings[i] = "PLACEHOLDER : 0.0";
        }
        String demographicsRepr = Repr.arrayToReprList(demographicsStrings);
        String[] senatorsStrings = new String[senators.size()];
        for (int i = 0; i < senators.size(); i++) {
            senatorsStrings[i] = senators.get(i).getName() + ", " + senators.get(i).getName().getLegalName();
        }
        String senatorsRepr = Repr.arrayToReprList(senatorsStrings);
        String repr = String.format(
                "%s:[FIPS:%s;name=%s;population=%d;abbreviation=%s;motto=%s;nickname=%s;congressionalDistricts=[%s];counties=[%s];cities=[%s];universities=[%s];demographics=[%s];senators=[%s];governor=%s;];",
                this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1],
                this.FIPS,
                this.fullName,
                this.population,
                this.abbreviation,
                this.motto,
                this.nickname,
                demographicsRepr,
                senatorsRepr,
                this.governor.getName().getLegalName());
        return repr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StateJava fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        // nation is constant for all states
        fields.add(new JSONObject("FIPS", FIPS));
        fields.add(new JSONObject("population", population));
        fields.add(new JSONObject("land_area", landArea));
        fields.add(new JSONObject("full_name", fullName));
        fields.add(new JSONObject("common_name", commonName));
        fields.add(new JSONObject("abbreviation", abbreviation));
        fields.add(new JSONObject("nickname", nickname));
        fields.add(new JSONObject("motto", motto));
        fields.add(new JSONObject("capital", capital.getNameWithCountyAndState()));
        fields.add(new JSONObject("descriptors", List.copyOf(descriptors)));
        // Demographics are derived from descriptors
        fields.add(new JSONObject("senators", List.of(getSenators().get(0).getName().getBiographicalName(),
                getSenators().get(1).getName().getBiographicalName())));
        fields.add(new JSONObject("governor", getGovernor().getName().getBiographicalName()));
        fields.add(new JSONObject("lieutenant_governor", getLieutenantGovernor().getName().getBiographicalName()));

        return new JSONObject(this.getName(), fields);
    }

    // OBJECT METHODS
    // -----------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        StateJava other = (StateJava) obj;
        return this.toString().equals(other.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
