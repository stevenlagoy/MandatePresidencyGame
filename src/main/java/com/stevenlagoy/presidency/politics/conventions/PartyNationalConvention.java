package com.stevenlagoy.presidency.politics.conventions;

import java.time.LocalDate;

import com.stevenlagoy.presidency.map.Municipality;
import com.stevenlagoy.presidency.politics.Party;

public class PartyNationalConvention extends Convention {

    private Party party;
    private String name;
    private Municipality location;
    private LocalDate date;

    public PartyNationalConvention(Party party, String name, Municipality location, LocalDate date) {
        this.party = party;
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public Party getParty() {
        return party;
    }

    public String getName() {
        return name;
    }

    public Municipality getLocation() {
        return location;
    }

    public void setLocation(Municipality location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}