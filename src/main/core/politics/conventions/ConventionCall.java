package main.core.politics.conventions;

import main.core.Main;
import main.core.map.CongressionalDistrict;
import main.core.map.Municipality;
import main.core.map.State;
import main.core.politics.Party;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A convention call, or call of the convention, is an official document issued by a political
 * party in the year before a party convention. It gives rules for primaries, apportions delegates,
 * places the convention in a city on a certain date, and many other rules for party conduct
 * leading to the convention.
 */
public class ConventionCall {

    private class PrimaryStage {
        private int stageNumber;
        private LocalDate stageBegin;
        private LocalDate stageEnd;
        private float stageBonus;
        public PrimaryStage(int stageNumber, LocalDate stageBegin, LocalDate stageEnd, float stageBonus) {
            this.stageNumber = stageNumber;
            this.stageBegin = stageBegin;
            this.stageEnd = stageEnd;
            this.stageBonus = stageBonus;
        }

        public int getStageNumber() {
            return stageNumber;
        }
        public LocalDate getStageBegin() {
            return stageBegin;
        }
        public LocalDate getStageEnd() {
            return stageEnd;
        }
        public float getStageBonus() {
            return stageBonus;
        }
    }

    private LocalDate issueDate;
    private Party issuingParty;
    private Map<State, Integer> atLargeAllocations;
    private Map<CongressionalDistrict, Integer> districtAllocations;
    private Map<State, Integer> automaticPledgedBoundDelegates;
    private Map<State, Integer> automaticUnpledgedUnboundDelegates;

    private int rollCallCount;
    private final int TOTAL_ELECTORAL_VOTES = 538;

    private LocalDate conventionStartDate;
    private LocalDate conventionEndDate;
    private Municipality conventionLocation;

    private List<PrimaryStage> stages;
    private float clusteringBonus;

    public ConventionCall(LocalDate issueDate, Party issuingParty, LocalDate conventionStartDate, Municipality conventionLocation) {
        this.issueDate = issueDate;
        this.issuingParty = issuingParty;
        this.atLargeAllocations = new HashMap<>();
        this.districtAllocations = new HashMap<>();
        this.automaticPledgedBoundDelegates = new HashMap<>();
        this.automaticUnpledgedUnboundDelegates = new HashMap<>();
        rollCallCount = 0;
        this.conventionStartDate = conventionStartDate;
        this.conventionEndDate = conventionStartDate.plusDays(2);
        this.conventionLocation = conventionLocation;
        this.stages = new ArrayList<>();
        this.clusteringBonus = 0.0f;
    }

    public void apportion() {

        for (State state : Main.Engine().MapManager().getStates()) {
            
        }
        for (CongressionalDistrict district : Main.Engine().MapManager().getCongressionalDistricts()) {

        }

    }

}
