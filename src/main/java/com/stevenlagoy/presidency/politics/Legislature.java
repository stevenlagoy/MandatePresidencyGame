package com.stevenlagoy.presidency.politics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.stevenlagoy.presidency.characters.FederalOfficial;
import com.stevenlagoy.presidency.characters.LocalOfficial;
import com.stevenlagoy.presidency.characters.PoliticalActor;
import com.stevenlagoy.presidency.characters.StateOfficial;
import com.stevenlagoy.presidency.characters.FederalOfficial.FederalRole;
import com.stevenlagoy.presidency.characters.LocalOfficial.LocalRole;
import com.stevenlagoy.presidency.characters.StateOfficial.StateRole;

public class Legislature {

    public static enum GovernmentLevel {
        LOCAL,
        STATE,
        FEDERAL;
    }

    private String name;
    /**
     * The title or term for a member of this house, I.E. Senator, Representative,
     * Member.
     */
    private String title;
    private GovernmentLevel governmentLevel;
    private boolean isUpperHouse;
    private int termLength;
    private int nextElectionYear;
    private int totalSeats;
    private Map<Party, Integer> partyControlledSeats;
    private List<PoliticalActor> members;

    public Legislature(String name, String title, GovernmentLevel governmentLevel, boolean isUpperHouse, int termLength,
            int nextElectionYear, int totalSeats) {
        this.name = name;
        this.title = title;
        this.governmentLevel = governmentLevel;
        this.isUpperHouse = isUpperHouse;
        this.termLength = termLength;
        this.nextElectionYear = nextElectionYear;
        this.totalSeats = totalSeats;
        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public boolean isUpperHouse() {
        return isUpperHouse;
    }

    public boolean isLowerHouse() {
        return !isUpperHouse;
    }

    public int getTermLength() {
        return termLength;
    }

    public int getNextElectionYear() {
        return nextElectionYear;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public Map<Party, Integer> getSeats() {
        return partyControlledSeats;
    }

    public int getPartySeats(Party party) {
        return partyControlledSeats.get(party);
    }

    public void reassignPartySeats(Party fromParty, Party toParty, int seats) {
        if (seats > totalSeats)
            throw new IllegalArgumentException("Attempted to set more seats than are available.");
        if (seats > getPartySeats(fromParty))
            throw new IllegalArgumentException(
                    "Attempted to reassign more seats than were held by the specified 'from' party.");
        partyControlledSeats.put(fromParty, partyControlledSeats.get(fromParty) - seats);
        partyControlledSeats.put(toParty, partyControlledSeats.get(toParty) + seats);
    }

    /**
     * Return the members list of this House. Between 0 and {@code totalSeats}
     * characters are generated for any house.
     */
    public List<PoliticalActor> getMembers() {
        return members;
    }

    public void addMember(PoliticalActor member) {
        switch (governmentLevel) {
            case LOCAL:
                if (member instanceof LocalOfficial)
                    ((LocalOfficial) member).addRole(LocalRole.CITY_COUNCILOR);
                else
                    throw new IllegalArgumentException("Members of a local legislature must be local officials.");
                break;
            case STATE:
                if (member instanceof StateOfficial)
                    ((StateOfficial) member)
                            .addRole(isUpperHouse ? StateRole.STATE_SENATOR : StateRole.STATE_REPRESENTATIVE);
                else
                    throw new IllegalArgumentException("Members of a state legislature must be state officials.");
                break;
            case FEDERAL:
                if (member instanceof FederalOfficial)
                    ((FederalOfficial) member).addRole(isUpperHouse ? FederalRole.SENATOR : FederalRole.REPRESENTATIVE);
                else
                    throw new IllegalArgumentException("Members of a federal legislature must be federal officials.");
                break;
        }

        members.add(member);
    }

    public boolean isPartyControlled(Party party) {
        return partyControlledSeats.get(party) > totalSeats / 2;
    }

}
