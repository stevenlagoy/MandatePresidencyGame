/*
 * Primary.java
 * Steven LaGoy
 * Created: 20 October 2025
 * Modified: 20 October 2025
 */

package main.core.politics.conventions;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import main.core.characters.Candidate;
import main.core.characters.StateOfficial;
import main.core.map.State;
import main.core.politics.Party;

public class Primary extends Convention {

    public static enum Franchise {
        OPEN,
        SEMI_OPEN,
        SEMI_CLOSED,
        CLOSED,
    }

    public static enum BindingType {
        PROPORTIONAL,
        PROPORTIONAL_TRIGGERED,
        PROPORTIONAL_LOOPHOLE,
        WINNER_TAKE_ALL,
        UNPLEDGED,
    }

    private State state;
    private Party associatedParty;
    private Franchise type;
    private LocalDate date;
    private Set<Candidate> candidates;
    private int numberDelegates;
    private Set<StateOfficial> delegates;
    /** Percentage of the vote which a candidate must receive in order to bind ANY delegates. */
    private float qualificationThreshold;
    /** Percentage of the vote which a candidate must receive in order to bind ALL delegates. */
    private float takeAllTriggerThreshold;

    public Primary(State state, Party associatedParty, Franchise type, LocalDate date, Collection<Candidate> candidates, int numberDelegates){
        this.state = state;
        this.associatedParty = associatedParty;
        this.type = type;
        this.date = date;
        this.candidates = new HashSet<Candidate>(candidates);
        this.numberDelegates = numberDelegates;
    }

    public void convene(){

    }

    // State : State
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }

    // Associated Party : Party
    public Party getAssociatedParty() {
        return associatedParty;
    }
    public void setAssocatedParty(Party party) {
        this.associatedParty = party;
    }

    // Type : Franchise
    public Franchise getFranchiseType() {
        return type;
    }
    public void setFranchiseType(Franchise type) {
        this.type = type;
    }

    // Date : LocalDate
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Candidates : List of Candidate
    public Set<Candidate> getCandidates() {
        return candidates;
    }
    public void setCandidates(Collection<Candidate> candidates) {
        this.candidates = new HashSet<Candidate>(candidates);
    }

    // Number Delegates : int
    public int getNumberDelegates() {
        return numberDelegates;
    }
    public void setNumberDelegates(int numberDelegates) {
        this.numberDelegates = numberDelegates;
    }

    // Delegates : List of State Official
    public Set<StateOfficial> getDelegates() {
        return delegates;
    }
    public void setDelegates(Collection<StateOfficial> delegates) {
        this.delegates = new HashSet<StateOfficial>(delegates);
    }

}
