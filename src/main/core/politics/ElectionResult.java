package main.core.politics;

import java.time.LocalDate;
import java.util.Map;

public class ElectionResult {
    
    private LocalDate electionDate;
    private Map<String, Integer> candidateVotes;
    private Map<String, Party> candidateParties;

    public ElectionResult(LocalDate electionDate, Map<String, Integer> candidateVotes, Map<String, Party> candidateParties) {
        this.electionDate = electionDate;
        this.candidateVotes = candidateVotes;
        this.candidateParties = candidateParties;
    }

    public LocalDate getElectionDate() {
        return electionDate;
    }
    public Map<String, Integer> getCandidateVotes() {
        return candidateVotes;
    }
    public int getVotesForCandidate(String candidateName) {
        return candidateVotes.get(candidateName);
    }
    public Map<String, Party> getCandidateParties() {
        return candidateParties;
    }
    public Party getCandidateParty(String candidateName) {
        return candidateParties.get(candidateName);
    }

}
