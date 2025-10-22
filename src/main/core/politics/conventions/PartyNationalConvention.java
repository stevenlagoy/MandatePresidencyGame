package main.core.politics.conventions;

import java.time.LocalDate;

import main.core.map.Municipality;
import main.core.politics.Party;

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